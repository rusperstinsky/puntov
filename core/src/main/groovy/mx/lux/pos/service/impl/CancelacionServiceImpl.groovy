package mx.lux.pos.service.impl

import com.mysema.query.BooleanBuilder
import com.mysema.query.types.OrderSpecifier
import com.mysema.query.types.Predicate
import groovy.util.logging.Slf4j
import mx.lux.pos.service.CancelacionService
import mx.lux.pos.service.NotaVentaService
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

import mx.lux.pos.model.*
import mx.lux.pos.repository.*
import mx.lux.pos.service.business.Registry
import java.text.NumberFormat

@Slf4j
@Service('cancelacionService')
@Transactional(readOnly = true)
class CancelacionServiceImpl implements CancelacionService {

    @Resource
    private CausaCancelacionRepository causaCancelacionRepository

    @Resource
    private NotaVentaRepository notaVentaRepository

    @Resource
    private NotaVentaService notaVentaService

    @Resource
    private ParametroRepository parametroRepository

    @Resource
    private ModificacionRepository modificacionRepository

    @Resource
    private ModificacionEmpRepository modificacionEmpRepository

    @Resource
    private ModificacionCanRepository modificacionCanRepository

    @Resource
    private PagoRepository pagoRepository

    @Resource
    private DevolucionRepository devolucionRepository

    @Override
    List<CausaCancelacion> listarCausasCancelacion() {
        log.info("listando causas de cancelacion")
        List<CausaCancelacion> causas = causaCancelacionRepository.findByDescripcionNotNullOrderByDescripcionAsc()
        log.debug("obtiene causas: ${causas*.id}")
        return causas?.any() ? causas : []
    }

    @Override
    boolean permitirCancelacionExtemporanea(String idNotaVenta) {
        log.info("determinando autorizacion para cancelacion extemporanea de notaVenta id: ${idNotaVenta}")
        if (Registry.isCancellationLimitedToSameDay()) {
            log.info('requiere cancelar dia de venta')
            NotaVenta notaVenta = notaVentaService.obtenerNotaVenta(idNotaVenta)
            if (notaVenta?.fechaHoraFactura && DateUtils.isSameDay(new Date(), notaVenta.fechaHoraFactura)) {
                return true
            } else {
                log.debug('fechaHoraFactura es distinta al dia actual')
            }
            return false
        } else {
            log.debug('parametro cancelacion mismo dia inactivo')
        }
        return true
    }

    @Override
    @Transactional
    Modificacion registrarCancelacionDeNotaVenta(String idNotaVenta, Modificacion modificacion) {
        log.info("registrando cancelacion para notaVenta id: ${idNotaVenta}")
        if (StringUtils.isNotBlank(idNotaVenta)) {
            NotaVenta notaVenta = notaVentaService.obtenerNotaVenta(idNotaVenta)
            log.debug("status: ${notaVenta?.sFactura}")
            boolean esActiva = StringUtils.isNotBlank(notaVenta?.sFactura) && !'T'.equalsIgnoreCase(notaVenta?.sFactura)
            if (StringUtils.isNotBlank(notaVenta?.id) && esActiva) {
                List<Pago> pagos = pagoRepository.findByIdFactura(notaVenta.id)
                pagos?.each { Pago pago ->
                    log.debug("pago id: ${pago?.id}, monto: ${pago?.monto}, por devolver: ${pago?.monto}")
                    pago?.porDevolver = pago?.monto
                }
                modificacion.idFactura = idNotaVenta
                modificacion.tipo = 'can'
                modificacion = modificacionRepository.save(modificacion)
                ModificacionCan modificacionCan = new ModificacionCan(
                        id: modificacion?.id,
                        estadoAnterior: notaVenta.sFactura
                )
                notaVenta.sFactura = 'T'
                modificacionCanRepository.save(modificacionCan)
                notaVentaRepository.save(notaVenta)
                pagoRepository.save(pagos)
                if (!ServiceFactory.inventory.solicitarTransaccionDevolucion(notaVenta)) {
                    log.warn("no se registra el movimiento, error al registrar devolucion")
                    modificacion.id = null
                }
                return modificacion
            } else {
                log.warn('no se registra cancelacion, notaVenta invalida o no existe')
            }
        } else {
            log.warn('no se registra cancelacion, parametros invalidos')
        }
        return null
    }

    @Override
    List<Devolucion> listarDevolucionesDeNotaVenta(String idFactura) {
        log.info("listando devoluciones por idFactura ${idFactura}")
        if (StringUtils.isNotBlank(idFactura)) {
            List<Modificacion> modificaciones = modificacionRepository.findByIdFacturaOrderByFechaAsc(idFactura)
            log.debug("obtiene modificaciones: ${modificaciones*.id}")
            if (modificaciones?.any()) {
                List<Devolucion> devoluciones = devolucionRepository.findByIdModInOrderByFechaAsc(modificaciones*.id)
                log.debug("obtiene devoluciones: ${devoluciones*.id}")
                return devoluciones?.any() ? devoluciones : []
            } else {
                log.warn('no se listan devoluciones, no existen modificaciones')
            }
        } else {
            log.warn('no se listan devoluciones, parametros invalidos')
        }
        return []
    }

    @Override
    @Transactional
    List<Devolucion> registrarDevolucionesDeNotaVenta(String idNotaVenta, Map<Integer, String> devolucionesPagos) {
        log.info("registrando devoluciones: ${devolucionesPagos} de notaVenta id: ${idNotaVenta}")
        boolean tieneElementos = devolucionesPagos?.any() && devolucionesPagos?.keySet()?.any()
        if (StringUtils.isNotBlank(idNotaVenta) && tieneElementos) {
            List<Modificacion> mods = modificacionRepository.findByIdFacturaAndTipo(idNotaVenta, 'can')
            log.debug("modificaciones: ${mods*.id}")
            Modificacion modificacion = mods?.any() ? mods.first() : null
            log.debug("obtiene modificacion: ${modificacion?.id}")
            if (modificacion?.id) {
                List<Pago> pagos = []
                List<Devolucion> devoluciones = []
                try {
                    devolucionesPagos.each { Integer pagoId, String valor ->
                        Pago pago = pagoRepository.findOne(pagoId)
                        log.debug("obtiene pago: ${pago?.id}")
                        if (pago?.id) {
                            String formaPago = 'EFM'
                            if ('ORIGINAL'.equalsIgnoreCase(valor)) {
                                formaPago = 'TR'.equalsIgnoreCase(pago.idFPago) ? pago.clave : pago.idFPago
                            }
                            Devolucion devolucion = new Devolucion(
                                    idMod: modificacion.id,
                                    idPago: pagoId,
                                    idFormaPago: formaPago,
                                    idBanco: pago.idBancoEmisor?.isInteger() ? pago.idBancoEmisor.toInteger() : null,
                                    monto: pago.porDevolver,
                                    tipo: 'd'
                            )
                            log.debug("genera devolucion: ${devolucion.dump()}")
                            pago.porDevolver = 0
                            pagos.add(pago)
                            devoluciones.add(devolucion)
                        } else {
                            throw new Exception("no se encuentra el pago con id: ${pagoId}")
                        }
                    }
                    pagoRepository.save(pagos)
                    devoluciones = devolucionRepository.save(devoluciones)
                    log.debug("devoluciones registradas: ${devoluciones*.id}")
                    return devoluciones
                } catch (ex) {
                    log.warn("no se registran devoluciones, ${ex.message}")
                }
            } else {
                log.warn('no se registran devoluciones, notaVenta sin cancelacion')
            }
        } else {
            log.warn('no se registran devoluciones, parametros invalidos')
        }
        return []
    }

    @Override
    @Transactional
    List<Pago> registrarTransferenciasParaNotaVenta(String idOrigen, String idDestino, Map<Integer, BigDecimal> transferenciasPagos) {
        log.info("registrando transferencias: ${transferenciasPagos} de notaVenta origen: ${idOrigen}, destino: ${idDestino}")
        boolean tieneElementos = transferenciasPagos?.any() && transferenciasPagos?.keySet()?.any()
        if (StringUtils.isNotBlank(idOrigen) && StringUtils.isNotBlank(idDestino) && tieneElementos) {
            NotaVenta destino = notaVentaService.obtenerNotaVenta(idDestino)
            List<Modificacion> mods = modificacionRepository.findByIdFacturaAndTipo(idOrigen, 'can')
            log.debug("modificaciones: ${mods*.id}")
            Modificacion modificacion = mods?.any() ? mods.first() : null
            log.debug("obtiene modificacion: ${modificacion?.id}")
            if (StringUtils.isNotBlank(destino?.id) && modificacion?.id) {
                List<Pago> pagos = []
                List<Pago> transferencias = []
                List<Devolucion> devoluciones = []
                Date fechaActual = new Date()
                try {
                    transferenciasPagos.each { Integer pagoId, BigDecimal valor ->
                        Pago pago = pagoRepository.findOne(pagoId)
                        log.debug("obtiene pago: ${pago?.id}")
                        if (pago?.id && valor) {
                            pago.porDevolver -= valor
                            Pago transferencia = new Pago(
                                    idFactura: idDestino,
                                    idFormaPago: pago.idFPago,
                                    referenciaPago: idOrigen,
                                    monto: valor,
                                    idEmpleado: destino.idEmpleado,
                                    idSucursal: pago.idSucursal,
                                    idFPago: 'TR',
                                    clave: pago.idFPago,
                                    referenciaClave: "${idOrigen}:${pagoId}",
                                    idSync: '2',
                                    tipoPago: DateUtils.isSameDay(destino.fechaHoraFactura ?: fechaActual, fechaActual) ? 'a' : 'l',
                                    idBancoEmisor: pago.idBancoEmisor,
                                    idTerminal: pago.idTerminal,
                                    idPlan: pago.idPlan
                            )
                            log.debug("genera transferencia: ${transferencia.dump()}")
                            Devolucion devolucion = new Devolucion(
                                    idMod: modificacion.id,
                                    idPago: pagoId,
                                    idFormaPago: pago.idFPago,
                                    idBanco: pago.idBancoEmisor?.isInteger() ? pago.idBancoEmisor.toInteger() : null,
                                    monto: valor,
                                    tipo: 't',
                                    transf: idDestino
                            )
                            log.debug("genera devolucion: ${devolucion.dump()}")
                            pagos.add(pago)
                            transferencias.add(transferencia)
                            devoluciones.add(devolucion)
                        } else {
                            throw new Exception("no se encuentra el pago con id: ${pagoId} o el monto es invalido: ${valor}")
                        }
                    }
                    pagoRepository.save(pagos)
                    transferencias = pagoRepository.save(transferencias)
                    log.debug("transferencias de pago registradas: ${transferencias*.id}")
                    devoluciones.each { Devolucion dev ->
                        Pago pago = transferencias.find { Pago tmp ->
                            tmp?.referenciaClave?.equalsIgnoreCase("${idOrigen}:${dev?.idPago}")
                        }
                        if (pago?.id) {
                            dev.referencia = pago.id
                        } else {
                            throw new Exception("no se encuentra transferencia con pago origen id: ${dev.idPago}")
                        }
                    }
                    devoluciones = devolucionRepository.save(devoluciones)
                    log.debug("devoluciones registradas: ${devoluciones*.id}")
                    notaVentaService.registrarNotaVenta(destino)
                    return transferencias
                } catch (ex) {
                    log.warn("no se registran transferencias, ${ex.message}")
                }
            } else {
                log.warn('no se registran transferencias, notaVenta origen y/o destino invalidas, y/o sin cancelacion')
            }
        } else {
            log.warn('no se registran transferencias, parametros invalidos')
        }
        return []
    }

    @Override
    List<NotaVenta> listarNotasVentaOrigenDeNotaVenta(String idNotaVenta) {
        log.info("obteniendo notaVenta origen de notaVenta id: ${idNotaVenta}")
        if (StringUtils.isNotBlank(idNotaVenta)) {
            BooleanBuilder builder = new BooleanBuilder(QPago.pago.idFPago.eq('TR'))
            builder.and(QPago.pago.idFactura.eq(idNotaVenta))
            List<Pago> transferencias = pagoRepository.findAll(builder) as List<Pago>
            log.debug("obtiene pagos tipo transferencia: ${transferencias*.id}")
            if (transferencias?.any()) {
                Predicate predicate = QNotaVenta.notaVenta.id.in(transferencias*.referenciaPago)
                OrderSpecifier order = QNotaVenta.notaVenta.fechaHoraFactura.asc()
                List<NotaVenta> notas = notaVentaRepository.findAll(predicate, order) as List<NotaVenta>
                log.debug("obtiene notas origen: ${notas*.id}")
                return notas?.any() ? notas : []
            } else {
                log.warn('no se obtiene notasVenta origen, notaVenta no ha recibido transferencias')
            }
        } else {
            log.warn('no se obtienen notasVenta origen, parametros invalidos')
        }
        return []
    }

    @Override
    BigDecimal obtenerCreditoDeNotaVenta(String idNotaVenta) {
        log.info("obteniendo credito de notaVenta id: ${idNotaVenta}")
        if (StringUtils.isNotBlank(idNotaVenta)) {
            List<Modificacion> mods = modificacionRepository.findByIdFacturaAndTipo(idNotaVenta, 'can')
            log.debug("modificaciones: ${mods*.id}")
            Modificacion modificacion = mods?.any() ? mods.first() : null
            log.debug("obtiene modificacion: ${modificacion?.id}")
            if (modificacion?.id) {
                BigDecimal porDevolver = BigDecimal.ZERO
                List<Pago> pagos = pagoRepository.findByIdFactura(idNotaVenta) ?: []
                pagos?.each { Pago pmt ->
                    porDevolver += pmt?.porDevolver ?: BigDecimal.ZERO
                }
                log.debug("obtiene credito: ${porDevolver}")
                return porDevolver
            } else {
                log.warn('no se obtiene credito de notaVenta, notaVenta sin cancelacion')
            }
        } else {
            log.warn('no se obtiene credito de notaVenta, parametros invalidos')
        }
        return null
    }


    @Override
    Boolean validandoTransferencia(String idNotaVenta) {
        Boolean transfer = true
        List<Pago> lstPagos = pagoRepository.findByReferenciaPago(idNotaVenta)
        List<Pago> lstPagosTransf = pagoRepository.findByIdFactura(idNotaVenta)
        BigDecimal sumaPagos = BigDecimal.ZERO
        BigDecimal sumaPagosTransf = BigDecimal.ZERO
        for (Pago pagoTransf : lstPagosTransf) {
            sumaPagosTransf = sumaPagosTransf.add(pagoTransf.monto)
        }
        for (Pago pago : lstPagos) {
            sumaPagos = sumaPagos.add(pago.monto)
        }
        if ( ( sumaPagos < sumaPagosTransf ) ) {
            transfer = false
        }
        return transfer
    }

    @Override
    @Transactional
    void restablecerValoresDeCancelacion(String idNotaVenta) {
        log.info("restableciendo valores de cancelacion")
      QPago payment = QPago.pago
        List<Pago> lstPagos = pagoRepository.findByReferenciaPago(idNotaVenta)
        List<Pago> lstPagosTransf = pagoRepository.findByIdFactura(idNotaVenta)
        BigDecimal sumaPagos = BigDecimal.ZERO
        BigDecimal sumaPagosTransf = BigDecimal.ZERO
        for (Pago pagoTransf : lstPagosTransf) {
            sumaPagosTransf = sumaPagosTransf.add(pagoTransf.monto)
        }
        for (Pago pago : lstPagos) {
            sumaPagos = sumaPagos.add(pago.monto)
        }
        for( Pago pagoTransf : lstPagos ){
          if( StringUtils.trimToEmpty( pagoTransf.notaVenta?.factura ).isEmpty() && !StringUtils.trimToEmpty(pagoTransf.referenciaClave).isEmpty() ){
            String[] idPagoTransf = pagoTransf.referenciaClave.split( ':' )
            Pago pagoFuente = pagoRepository.findOne( Integer.parseInt( idPagoTransf[1].trim() ) )
            QDevolucion dev = QDevolucion.devolucion
            Devolucion devolucion = devolucionRepository.findOne( dev.idPago.eq(pagoFuente.getId()).and(dev.monto.eq(pagoTransf.monto)).
                and(dev.transf.eq(pagoTransf.idFactura)))
            if( devolucion != null ){
              BigDecimal montoTotal = pagoTransf.monto.add( pagoFuente.porDevolver )
              devolucionRepository.delete( devolucion.id )
              pagoFuente.setPorDevolver( montoTotal )
              pagoRepository.save( pagoFuente )
              pagoRepository.flush()
            }
          }
        }
    }


    @Override
    @Transactional
    void restablecerMontoAlBorrarPago( Integer idPago ) {
        Pago pago = pagoRepository.findOne( idPago )
        if( pago != null && !StringUtils.trimToEmpty(pago.referenciaClave).isEmpty() ){
            String[] idPagoTransf = pago.referenciaClave.split( ':' )
          if( idPagoTransf.length > 1 ){
            Pago pagoDeTransf = pagoRepository.findOne( Integer.parseInt( idPagoTransf[1].trim() ) )
            BigDecimal porDevolver = pago.monto.add(pagoDeTransf.porDevolver)
            pagoDeTransf.setPorDevolver( porDevolver )
            QDevolucion dev = QDevolucion.devolucion
            Devolucion devolucion = devolucionRepository.findOne( dev.idPago.eq(pagoDeTransf.getId()).and(dev.monto.eq(pago.monto)).
                and(dev.transf.eq(pago.idFactura)))
            devolucionRepository.delete( devolucion.id )
            pagoRepository.save( pagoDeTransf )
            pagoRepository.flush()
          }
        }
    }

    @Override
    @Transactional
    Modificacion registrarCambiodeEmpleado(Modificacion modificacion, String idEmpAnterior, String idEmpleadoFinal) {
        log.info("registrando cambio de empleado")
        Modificacion mod = modificacionRepository.save( modificacion )
        ModificacionEmp modEmp = new ModificacionEmp()
        modEmp.id = mod.id
        modEmp.empleadoAnterior = idEmpAnterior
        modEmp.empleadoActual = idEmpleadoFinal
        modificacionEmpRepository.save( modEmp )
        return mod
    }


    @Override
    CausaCancelacion causaCancelacion( Integer id ) {
        log.info("cancelacion con id: ${id}")
        CausaCancelacion causa = causaCancelacionRepository.findOne( id )
        log.debug("obtiene causa: ${causa?.descripcion}")
        return causa != null ? causa : null
    }
}

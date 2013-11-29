package mx.lux.pos.service.impl

import com.mysema.query.BooleanBuilder
import com.mysema.query.types.OrderSpecifier
import com.mysema.query.types.Predicate
import groovy.util.logging.Slf4j
import mx.lux.pos.repository.impl.RepositoryFactory
import mx.lux.pos.service.NotaVentaService
import mx.lux.pos.service.business.EliminarNotaVentaTask
import mx.lux.pos.service.business.Registry
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

import mx.lux.pos.model.*
import mx.lux.pos.repository.*

@Slf4j
@Service( 'notaVentaService' )
@Transactional( readOnly = true )
class NotaVentaServiceImpl implements NotaVentaService {

  private static final String DATE_TIME_FORMAT = 'dd-MM-yyyy HH:mm:ss'

  @Resource
  private NotaVentaRepository notaVentaRepository

  @Resource
  private DetalleNotaVentaRepository detalleNotaVentaRepository

  @Resource
  private PagoRepository pagoRepository

  @Resource
  private SucursalRepository sucursalRepository

  @Resource
  private ArticuloRepository articuloRepository

  @Resource
  private PrecioRepository precioRepository

  @Resource
  private ParametroRepository parametroRepository

  @Resource
  private ModificacionRepository modificacionRepository

  @Override
  NotaVenta obtenerNotaVenta( String idNotaVenta ) {
    log.info( "obteniendo notaVenta: ${idNotaVenta}" )
    if ( StringUtils.isNotBlank( idNotaVenta ) ) {
      NotaVenta notaVenta = notaVentaRepository.findOne( idNotaVenta )
      log.debug( "obtiene notaVenta id: ${notaVenta?.id}," )
      log.debug( "fechaHoraFactura: ${notaVenta?.fechaHoraFactura?.format( DATE_TIME_FORMAT )}" )
      return notaVenta
    } else {
      log.warn( 'no se obtiene notaVenta, parametros invalidos' )
    }
    return null
  }

  @Override
  @Transactional
  NotaVenta abrirNotaVenta( ) {
    log.info( 'abriendo nueva notaVenta' )
    Parametro parametro = parametroRepository.findOne( TipoParametro.ID_CLIENTE_GENERICO.value )
    NotaVenta notaVenta = new NotaVenta(
        id: notaVentaRepository.getNotaVentaSequence(),
        idSucursal: sucursalRepository.getCurrentSucursalId(),
        idCliente: parametro?.valor?.isInteger() ? parametro.valor.toInteger() : null
    )
    try {
      notaVenta = notaVentaRepository.save( notaVenta )
      log.info( "notaVenta registrada id: ${notaVenta?.id}" )
      return notaVenta
    } catch ( ex ) {
      log.error( "problema al registrar notaVenta: ${notaVenta?.dump()}", ex )
    }
    return null
  }

  @Override
  @Transactional
  NotaVenta registrarNotaVenta( NotaVenta notaVenta ) {
    log.info( "registrando notaVenta id: ${notaVenta?.id}," )
    log.info( "fechaHoraFactura: ${notaVenta?.fechaHoraFactura?.format( DATE_TIME_FORMAT )}" )
    if ( StringUtils.isNotBlank( notaVenta?.id ) ) {
      String idNotaVenta = notaVenta.id
      if ( notaVentaRepository.exists( idNotaVenta ) ) {
        notaVenta.idSucursal = sucursalRepository.getCurrentSucursalId()
        BigDecimal total = BigDecimal.ZERO
        List<DetalleNotaVenta> detalles = detalleNotaVentaRepository.findByIdFactura( idNotaVenta )
        detalles?.each { DetalleNotaVenta detalleNotaVenta ->
          BigDecimal precio = detalleNotaVenta?.precioUnitFinal ?: 0
          Integer cantidad = detalleNotaVenta?.cantidadFac ?: 0
          BigDecimal subtotal = precio.multiply( cantidad )
          total = total.add( subtotal )
        }
        BigDecimal pagado = BigDecimal.ZERO
        List<Pago> pagos = pagoRepository.findByIdFactura( idNotaVenta )
        pagos?.each { Pago pago ->
          BigDecimal monto = pago?.monto ?: 0
          pagado = pagado.add( monto )
        }
        log.debug( "ventaNeta: ${notaVenta.ventaNeta} -> ${total}" )
        log.debug( "ventaTotal: ${notaVenta.ventaTotal} -> ${total}" )
        log.debug( "sumaPagos: ${notaVenta.sumaPagos} -> ${pagado}" )
        notaVenta.ventaNeta = total
        notaVenta.ventaTotal = total
        notaVenta.sumaPagos = pagado
        try {
          notaVenta = notaVentaRepository.save( notaVenta )
          log.info( "notaVenta registrada id: ${notaVenta?.id}" )
          return notaVenta
        } catch ( ex ) {
          log.error( "problema al registrar notaVenta: ${notaVenta?.dump()}", ex )
        }
      } else {
        log.warn( "no se registra notaVenta, id no existe" )
      }
    } else {
      log.warn( "no se registra notaVenta, parametros invalidos" )
    }
    return null
  }

  private DetalleNotaVenta establecerPrecios( DetalleNotaVenta detalle ) {
    log.debug( "estableciendo precios para detalleNotaVenta articulo: ${detalle?.idArticulo}" )
    if ( detalle?.idArticulo ) {
      Articulo articulo = articuloRepository.findOne( detalle.idArticulo )
      log.debug( "obtiene articulo id: ${articulo?.id}, codigo: ${articulo?.articulo}, color: ${articulo?.codigoColor}" )
      if ( articulo?.id ) {
        List<Precio> precios = precioRepository.findByArticulo( articulo.articulo )
        log.debug( "obtiene lista de precios ${precios*.lista}" )
        if ( precios?.any() ) {
          Precio precioLista = precios.find { Precio tmp ->
            'L'.equalsIgnoreCase( tmp?.lista )
          }
          log.debug( "precio lista: ${precioLista?.dump()}" )
          BigDecimal lista = precioLista?.precio ?: BigDecimal.ZERO
          Precio precioOferta = precios.find { Precio tmp ->
            'O'.equalsIgnoreCase( tmp?.lista )
          }
          log.debug( "precio oferta: ${precioOferta?.dump()}" )
          BigDecimal oferta = precioOferta?.precio ?: BigDecimal.ZERO
          BigDecimal unitario = oferta && ( oferta < lista ) ? oferta : lista
          detalle.precioCalcLista = lista
          detalle.precioCalcOferta = oferta
          detalle.precioUnitLista = unitario
          detalle.precioUnitFinal = unitario
          detalle.precioFactura = unitario
          detalle.precioConv = BigDecimal.ZERO
          log.debug( "detalleNotaVenta actualizado: ${detalle.dump()}" )
        } else {
          log.warn( 'no se establecen precios, lista de precios vacia' )
        }
      } else {
        log.warn( 'no se establecen precios, articulo invalido' )
      }
    } else {
      log.warn( 'no se establecen precios, parametros invalidos' )
    }
    return detalle
  }

  @Override
  @Transactional
  NotaVenta registrarDetalleNotaVentaEnNotaVenta( String idNotaVenta, DetalleNotaVenta detalleNotaVenta ) {
    log.info( "registrando detalleNotaVenta id: ${detalleNotaVenta?.id} idArticulo: ${detalleNotaVenta?.idArticulo}" )
    log.info( "en notaVenta id: ${idNotaVenta}" )
    NotaVenta notaVenta = obtenerNotaVenta( idNotaVenta )
    if ( StringUtils.isNotBlank( notaVenta?.id ) && detalleNotaVenta?.idArticulo ) {
      detalleNotaVenta.idFactura = idNotaVenta
      detalleNotaVenta.idSucursal = sucursalRepository.getCurrentSucursalId()
      DetalleNotaVenta tmp = detalleNotaVentaRepository.findByIdFacturaAndIdArticulo( idNotaVenta, detalleNotaVenta.idArticulo )
      log.debug( "obtiene detalleNotaVenta existente: ${tmp?.dump()}" )
      if ( tmp?.id ) {
        log.debug( "actualizando detalleNotaVenta con id: ${tmp.id} cantidadFac: ${tmp.cantidadFac}" )
        detalleNotaVenta.id = tmp.id
        detalleNotaVenta.cantidadFac += tmp.cantidadFac
        log.debug( "actualizados cantidadFac: ${detalleNotaVenta.cantidadFac}" )
      } else {
        log.debug( "registrando nuevo detalleNotaVenta" )
      }
      detalleNotaVenta = establecerPrecios( detalleNotaVenta )
      try {
        detalleNotaVenta = detalleNotaVentaRepository.save( detalleNotaVenta )
        log.debug( "detalleNotaVenta registrado id: ${detalleNotaVenta.id}" )
        return registrarNotaVenta( notaVenta )
      } catch ( ex ) {
        log.error( "problema al registrar detalleNotaVenta: ${detalleNotaVenta?.dump()}", ex )
      }
    } else {
      log.warn( "no se registra detalleNotaVenta, parametros invalidos" )
    }
    return null
  }

  @Override
  @Transactional
  NotaVenta eliminarDetalleNotaVentaEnNotaVenta( String idNotaVenta, Integer idArticulo ) {
    log.info( "eliminando detalleNotaVenta idArticulo: ${idArticulo} de notaVenta id: ${idNotaVenta}" )
    if ( idArticulo && StringUtils.isNotBlank( idNotaVenta ) ) {
      DetalleNotaVenta detalle = detalleNotaVentaRepository.findByIdFacturaAndIdArticulo( idNotaVenta, idArticulo )
      if ( detalle?.id ) {
        log.debug( "obtiene detalleNotaVenta id: ${detalle.id}" )
        NotaVenta notaVenta = obtenerNotaVenta( idNotaVenta )
        if ( StringUtils.isNotBlank( notaVenta?.id ) ) {
          detalleNotaVentaRepository.delete( detalle.id )
          log.debug( "detalleNotaVenta eliminado" )
          return registrarNotaVenta( notaVenta )
        } else {
          log.warn( "no se elimina detalleNotaVenta, no existe notaVenta id: ${idNotaVenta}" )
        }
      } else {
        log.warn( "no se elimina detalleNotaVenta, no existe con idNotaVenta: ${idNotaVenta} idArticulo: ${idArticulo}" )
      }
    } else {
      log.warn( "no se elimina detalleNotaVenta, parametros invalidos" )
    }
    return null
  }

  @Override
  @Transactional
  NotaVenta registrarPagoEnNotaVenta( String idNotaVenta, Pago pago ) {
    log.info( "registrando pago id: ${pago?.id} idFormaPago: ${pago?.idFormaPago} monto: ${pago?.monto}" )
    log.info( "en notaVenta id: ${idNotaVenta}" )
    NotaVenta notaVenta = obtenerNotaVenta( idNotaVenta )
    if ( StringUtils.isNotBlank( notaVenta?.id ) && StringUtils.isNotBlank( pago?.idFormaPago ) && pago?.monto ) {
      String formaPago = pago.idFormaPago
      if ( 'ES'.equalsIgnoreCase( formaPago ) ) {
        formaPago = 'EFM'
      } else if ( 'TS'.equalsIgnoreCase( formaPago ) ) {
        formaPago = 'TCM'
      }
      log.debug( "forma pago definida: ${formaPago}" )
      Date fechaActual = new Date()
      pago.idFormaPago = formaPago
      pago.idFactura = idNotaVenta
      pago.idSucursal = sucursalRepository.getCurrentSucursalId()
      pago.tipoPago = DateUtils.isSameDay( notaVenta.fechaHoraFactura ?: fechaActual, fechaActual ) ? 'a' : 'l'
      log.debug( "obteniendo existencia de pago con id: ${pago.id}" )
      Pago tmp = pagoRepository.findOne( pago.id ?: 0 )
      if ( tmp?.id ) {
        log.debug( "pago ya registrado, no se puede modificar" )
      } else {
        log.debug( "registrando pago con monto: ${pago.monto}" )
        try {
          pago = pagoRepository.save( pago )
          log.debug( "pago registrado id: ${pago.id}" )
          return registrarNotaVenta( notaVenta )
        } catch ( ex ) {
          log.error( "problema al registrar pago: ${pago?.dump()}", ex )
        }
      }
    } else {
      log.warn( "no se registra pago, parametros invalidos" )
    }
    return null
  }

  @Override
  @Transactional
  NotaVenta eliminarPagoEnNotaVenta( String idNotaVenta, Integer idPago ) {
    log.info( "eliminando pago id: ${idPago} idFactura: ${idNotaVenta}" )
    if ( idPago && StringUtils.isNotBlank( idNotaVenta ) ) {
      Pago pago = pagoRepository.findOne( idPago )
      if ( pago?.id ) {
        log.debug( "obtiene pago id: ${pago.id} idFormaPago: ${pago.idFormaPago} monto: ${pago.idFormaPago}" )
        NotaVenta notaVenta = obtenerNotaVenta( idNotaVenta )
        if ( StringUtils.isNotBlank( notaVenta?.id ) ) {
          pagoRepository.delete( pago.id )
          pagoRepository.flush()
          log.debug( "pago eliminado" )
          return registrarNotaVenta( notaVenta )
        } else {
          log.warn( "no se elimina pago, no existe notaVenta id: ${idNotaVenta}" )
        }
      } else {
        log.warn( "no se elimina pago, no existe con id: ${idPago}" )
      }
    } else {
      log.warn( "no se elimina pago, parametros invalidos" )
    }
    return null
  }

  @Override
  @Transactional
  NotaVenta cerrarNotaVenta( NotaVenta notaVenta ) {
    log.info( "cerrando notaVenta id: ${notaVenta?.id}" )
    if ( StringUtils.isNotBlank( notaVenta?.id ) ) {
      String idNotaVenta = notaVenta.id
      if ( notaVentaRepository.exists( idNotaVenta ) ) {
        Date fecha = new Date()
        notaVenta.factura = notaVentaRepository.getFacturaSequence()
        notaVenta.tipoNotaVenta = 'F'
        notaVenta.tipoDescuento = 'N'
        notaVenta.tipoEntrega = 'S'
        notaVenta.setfExpideFactura( true )
        notaVenta.fechaEntrega = notaVenta.fechaEntrega ?: fecha
        notaVenta.horaEntrega = notaVenta.horaEntrega ?: fecha
        notaVenta.fechaPrometida = notaVenta.fechaPrometida ?: fecha
        return registrarNotaVenta( notaVenta )
      } else {
        log.warn( "no se cierra notaVenta, id no existe" )
      }
    } else {
      log.warn( "no se cierra notaVenta, parametros invalidos" )
    }
    return null
  }

  @Override
  List<NotaVenta> listarUltimasNotasVenta( ) {
    log.info( "listando ultimas notasVenta" )
    List<NotaVenta> results = notaVentaRepository.findByFacturaNotEmptyLimitingLatestResults( 10 )
    return results?.any() ? results : [ ]
  }

  private Predicate generarPredicadoTicket( String ticket ) {
    log.info( "generando predicado para busqueda de notaVenta con ticket: ${ticket}" )
    List<String> tokens = StringUtils.splitPreserveAllTokens( ticket, '-' )
    if ( StringUtils.isNotBlank( ticket ) && tokens?.size() >= 2 ) {
      String centroCostos = StringUtils.trimToEmpty( tokens.get( 0 ) )
      String factura = StringUtils.trimToEmpty( tokens.get( 1 ) )
      log.debug( "ticket con centro de costos: ${centroCostos} y factura: ${factura}" )
      if ( factura.length() > 0 && centroCostos.length() > 0 ) {
        QNotaVenta qNotaVenta = QNotaVenta.notaVenta
        BooleanBuilder builder = new BooleanBuilder( qNotaVenta.factura.eq( factura ) )
        builder.and( qNotaVenta.sucursal.centroCostos.eq( centroCostos ) )
        return builder
      } else {
        log.warn( 'no se genera predicado, factura y/o centro de costos invalidos' )
      }
    } else {
      log.warn( 'no se genera predicado, parametros invalidos' )
    }
    return null
  }

  @Override
  List<NotaVenta> listarNotasVentaPorParametros( Map<String, Object> parametros ) {
    log.info( "listando notasVenta por parametros: ${parametros}" )
    if ( parametros?.any() ) {
      Date dateFrom = parametros.dateFrom as Date
      Date dateTo = parametros.dateTo as Date
      String folio = parametros.folio
      String ticket = parametros.ticket
      String employee = parametros.employee
      QNotaVenta qNotaVenta = QNotaVenta.notaVenta
      BooleanBuilder builder = new BooleanBuilder()
      if ( dateFrom && dateTo ) {
        dateTo = new Date( dateTo.next().time - 1 )
        log.debug( "fecha inicio: ${dateFrom?.format( DATE_TIME_FORMAT )}" )
        log.debug( "fecha fin: ${dateTo?.format( DATE_TIME_FORMAT )}" )
        builder.and( qNotaVenta.fechaHoraFactura.between( dateFrom, dateTo ) )
      }
      if ( StringUtils.isNotBlank( folio ) ) {
        log.debug( "folio: ${folio}" )
        builder.and( qNotaVenta.id.eq( folio ) )
      }
      Predicate predicate = generarPredicadoTicket( ticket )
      if ( predicate ) {
        builder.and( predicate )
      }
      if ( StringUtils.isNotBlank( employee ) ) {
        log.debug( "empleado: ${employee}" )
        builder.and( qNotaVenta.idEmpleado.eq( employee ) )
      }
      if ( builder.args?.any() ) {
        builder.and( qNotaVenta.factura.isNotEmpty() )
        List<NotaVenta> results = notaVentaRepository.findAll( builder, qNotaVenta.fechaHoraFactura.desc() ) as List<NotaVenta>
        return results?.any() ? results : [ ]
      }
    } else {
      log.warn( "no se realiza busqueda, parametros invalidos" )
    }
    return [ ]
  }

  @Override
  NotaVenta obtenerNotaVentaPorTicket( String ticket ) {
    log.info( "obteniendo notaVenta con ticket: ${ticket}" )
    Predicate predicate = generarPredicadoTicket( ticket )
    if ( predicate ) {
      OrderSpecifier orderSpecifier = QNotaVenta.notaVenta.fechaHoraFactura.desc()
      List<NotaVenta> resultados = notaVentaRepository.findAll( predicate, orderSpecifier ) as List<NotaVenta>
      NotaVenta notaVenta = resultados?.any() ? resultados.first() : null
      log.debug( "obtiene notaVenta id: ${notaVenta?.id}" )
      return notaVenta
    } else {
      log.warn( 'no se obtiene notaVenta, parametros invalidos' )
    }
    return null
  }

  void eliminarNotaVenta( String pOrderNbr ) {
    log.debug( String.format( "Eliminar Nota Venta: %s", pOrderNbr ) )
    EliminarNotaVentaTask task = new EliminarNotaVentaTask()
    NotaVenta order = notaVentaRepository.findOne( pOrderNbr )
    if ( order != null ) {
      task.addNotaVenta( order.id )
      log.debug( task.toString() )
      task.run()
    } else {
      log.debug( String.format( 'No existe Nota Venta: %s', pOrderNbr ) )
    }
  }

  SalesWithNoInventory obtenerConfigParaVentasSinInventario( ) {
    SalesWithNoInventory autorizacion = Registry.configForSalesWithNoInventory
    return autorizacion
  }

  Empleado obtenerEmpleadoDeNotaVenta( pOrderId ) {
    Empleado employee = null
    if ( StringUtils.trimToNull( pOrderId ) != null ) {
      NotaVenta order = notaVentaRepository.findOne( StringUtils.trimToEmpty( pOrderId ) )
      if ( ( order != null ) && ( StringUtils.trimToNull( order.idEmpleado ) != null ) ) {
        employee = RepositoryFactory.employeeCatalog.findOne( StringUtils.trimToEmpty( order.idEmpleado ) )
      }
    }
    return employee
  }

  @Transactional
  void saveOrder( NotaVenta pNotaVenta ) {
    if ( pNotaVenta != null ) {
      notaVentaRepository.save( pNotaVenta )
    }
  }

  @Override
  NotaVenta obtenerNotaVentaPorFactura( String factura ){
      log.debug( String.format( 'Obteniendo factura %s', factura) )
      QNotaVenta nv = QNotaVenta.notaVenta
      NotaVenta notaVenta = notaVentaRepository.findOne( nv.factura.eq(factura.trim()))
      return notaVenta
  }

  @Override
  List<NotaVenta> obtenerDevolucionesPendientes( Date fecha ) {
      log.info( "obteniendo pagos del dia: ${fecha}" )
      Date fechaInicio = DateUtils.truncate( fecha, Calendar.DAY_OF_MONTH );
      Date fechaFin = new Date( DateUtils.ceiling( fecha, Calendar.DAY_OF_MONTH ).getTime() - 1 );
      List<NotaVenta> lstNotasVentas = new ArrayList<NotaVenta>()
      QModificacion mod = QModificacion.modificacion
      List<Modificacion> lstModificaciones = modificacionRepository.findAll( mod.fecha.between(fechaInicio, fechaFin).
              and(mod.tipo.equalsIgnoreCase('can')))
      for(Modificacion modificacion : lstModificaciones){
          NotaVenta notaVenta = notaVentaRepository.findOne( modificacion.idFactura )
          if(notaVenta != null){
              Boolean pendiente = false
              for(Pago pago : notaVenta.pagos){
                  if( pago.porDevolver.compareTo(BigDecimal.ZERO) > 0){
                      pendiente = true
                  }
              }
              if(pendiente){
                  lstNotasVentas.add(notaVenta)
              }
          }
      }
      return lstNotasVentas
  }
}
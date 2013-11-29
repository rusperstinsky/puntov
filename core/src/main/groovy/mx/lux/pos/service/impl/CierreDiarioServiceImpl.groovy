package mx.lux.pos.service.impl

import groovy.util.logging.Slf4j
import mx.lux.pos.service.CierreDiarioService
import mx.lux.pos.service.business.EliminarNotaVentaTask
import mx.lux.pos.service.business.InventorySearch
import mx.lux.pos.service.business.Registry
import mx.lux.pos.util.CustomDateUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils
import org.apache.velocity.app.VelocityEngine
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.velocity.VelocityEngineUtils
import org.springframework.util.Assert

import java.text.SimpleDateFormat
import javax.annotation.Resource

import mx.lux.pos.model.*
import mx.lux.pos.repository.*
import mx.lux.pos.service.io.ArchiveTask
import java.math.RoundingMode
import java.text.NumberFormat

@Slf4j
@Service( 'cierreDiarioService' )
@Transactional( readOnly = true )
class CierreDiarioServiceImpl implements CierreDiarioService {

  private static final String TIME_FORMAT = 'HH:mm:ss'
  private static final String DATE_FORMAT = 'dd-MM-yyyy'
  private static final String DATE_TIME_FORMAT = 'dd-MM-yyyy HH:mm:ss'
  private static final String PAIS_DEFAULT = 'MEXICO'
  private static final String FMT_ARCHIVE_FILENAME = '%d.%s'
  private static final String FMT_FILE_PATTERN = '*%s.*'
  private static final Double VALOR_CERO = 0.005

  @Resource
  private ClienteRepository clienteRepository

  @Resource
  private ArticuloRepository articuloRepository

  @Resource
  private NotaFacturaRepository notaFacturaRepository

  @Resource
  private CierreDiarioRepository cierreDiarioRepository

  @Resource
  private DepositoRepository depositoRepository

  @Resource
  private OrdenPromDetRepository ordenPromDetRepository

  @Resource
  private PagoRepository pagoRepository

  @Resource
  private PagoExternoRepository pagoExternoRepository

  @Resource
  private ResumenDiarioRepository resumenDiarioRepository

  @Resource
  private ParametroRepository parametroRepository

  @Resource
  private NotaVentaRepository notaVentaRepository

  @Resource
  private ModificacionRepository modificacionRepository

  @Resource
  private DevolucionRepository devolucionRepository

  @Resource
  private TerminalRepository terminalRepository

  @Resource
  private ResumenTerminalRepository resumenTerminalRepository

  @Resource
  private PlanRepository planRepository

  @Resource
  private AcuseRepository acuseRepository

  @Resource
  private VoucherTmpRepository voucherTmpRepository

  @Resource
  private SucursalRepository sucursalRepository

  @Resource
  private DescuentoRepository descuentoRepository

  @Resource
  private PrecioRepository precioRepository

  @Resource
  private EntregadoExternoRepository entregadoExternoRepository

  @Resource
  private ClasifCliRepository clasifCliRepository

  @Resource
  private VelocityEngine velocityEngine

  @Override
  List<CierreDiario> buscarConEstadoAbierto( ) {
    log.info( "obteniendo lista de cierreDiario con estado abierto" )
    List<CierreDiario> resultados = cierreDiarioRepository.findByEstadoOrderByFechaAsc( 'a' )
    log.debug( "obtiene dias: ${resultados*.fecha}" )
    return resultados?.any() ? resultados : [ ]
  }

  @Override
  CierreDiario buscarPorFecha( Date fecha ) {
    log.info( "buscando cierreDiario por fecha: ${fecha?.format( DATE_FORMAT )}" )
    return cierreDiarioRepository.findOne( fecha )
  }

  @Override
  List<Deposito> buscarDepositosPorFecha( Date fecha ) {
    log.info( "buscando lista de depositos por fecha: ${fecha?.format( DATE_FORMAT )}" )
    if ( fecha != null ) {
      return depositoRepository.findBy_Fecha( fecha ).collect()
    }
    return [ ]
  }

  @Override
  @Transactional
  CierreDiario abrirCierreDiario( ) {
    log.info( "abriendo el dia para el cierre diario con fecha: ${new Date().format( DATE_FORMAT )}" )
    CierreDiario cierreDiario = cierreDiarioRepository.findOne( new Date() )
    if ( cierreDiario?.fecha ) {
      return cierreDiario
    }
    return cierreDiarioRepository.save( new CierreDiario( fecha: new Date(), estado: 'a' ) )
  }

  void eliminarVentasAbiertas( ) {
    EliminarNotaVentaTask task = new EliminarNotaVentaTask()
    List<NotaVenta> listOrders = notaVentaRepository.findByFactura( '' )
    listOrders.addAll( notaVentaRepository.findByFacturaIsNull( ) )
    for ( NotaVenta order : listOrders ) {
      if ( StringUtils.trimToNull( order.factura ) == null ) {
        task.addNotaVenta( order.id )
      }
    }
    log.debug( task.toString() )
    task.run()
    log.debug( task.toString() )
  }

  @Override
  @Transactional
  void cerrarCierreDiario( Date fechaCierre, String observaciones, Boolean cierre ) {
    log.info( "Cerrando el Dia ${fechaCierre?.format( 'dd/MM/yyyy' )}" )
    Assert.notNull( fechaCierre, "Fecha de Cierre no puede ser NULL" )

    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    eliminarVentasAbiertas()
    CierreDiario cierreDiario = cierreDiarioRepository.findOne( fechaCierre )
    cierreDiario.estado = 'c'
    Date fecha = new Date()
    cierreDiario.fechaCierre = fecha
    cierreDiario.horaCierre = fecha
    cierreDiario.observaciones = observaciones
    cierreDiarioRepository.save( cierreDiario )

    Parametro parametro = parametroRepository.findOne( TipoParametro.ID_SUCURSAL.value )
    Sucursal sucursal = sucursalRepository.findOne( Integer.parseInt( parametro.getValor() ) )
    insertarAcuse( fechaCierre, cierreDiario.horaCierre, sucursal )

    Parametro ubicacion = Registry.find( TipoParametro.RUTA_CIERRE )
    try {
      generarFicheroZD( fechaCierre, sucursal, ubicacion.valor )
      generarFicheroZO( fechaCierre, sucursal, ubicacion.valor )
      generarFicheroZP( fechaCierre, sucursal, ubicacion.valor )
      generarFicheroZM( fechaCierre, sucursal, ubicacion.valor )
      generarFicheroZS( fechaCierre, sucursal, ubicacion.valor )
      generarFicheroZV( fechaCierre, sucursal, ubicacion.valor )
      generarFicheroCLI( fechaCierre, sucursal, ubicacion.valor )
      generarFicheroff( fechaCierre, sucursal, ubicacion.valor )
      generarFicheroZZ( fechaCierre, sucursal, ubicacion.valor )

      String dateClose = df.format(fechaCierre)
      String today = df.format( new Date() )
      if( dateClose.compareTo( today ) == 0 ){
        generarFicheroInv( )
      }

      /*if(Registry.generateMonthTransactions()){
        closeDaysBeforeNov()
      }*/
      Calendar cal = Calendar.getInstance()
      cal.set(Calendar.DAY_OF_MONTH,Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
      String firstDay = CustomDateUtils.format( cal.getTime(), 'dd-MM-yyyy' )
      String dayClose = CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' )

      if( firstDay.equalsIgnoreCase(dayClose) && cierre ){
        cal.add(Calendar.MONTH, -1)
        Date dateStart = cal.getTime()
        cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH,Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.add( Calendar.DAY_OF_MONTH, -1)
        Date dateFinish = cal.getTime()
        InventorySearch.generateInFile2( dateStart, dateFinish, fechaCierre )
      }

      /*if( Registry.generateMonthTransactions() ){
        generaIN2( fechaCierre )
      }*/
      InventorySearch.generateInFile( fechaCierre, fechaCierre, cierre )
      archivarCierre( fechaCierre )
    } catch ( Exception e ) {
      log.error( e.getMessage(), e )
    }
  }

  @Override
  @Transactional
  void regenerarArchivosZ( Date fechaCierre ) {
    Parametro parametro = parametroRepository.findOne( TipoParametro.ID_SUCURSAL.value )
    Sucursal sucursal = sucursalRepository.findOne( Integer.parseInt( parametro.getValor() ) )
    Parametro ubicacion = Registry.find( TipoParametro.RUTA_CIERRE )
    try {
      generarFicheroZD( fechaCierre, sucursal, ubicacion.valor )
      generarFicheroZO( fechaCierre, sucursal, ubicacion.valor )
      generarFicheroZP( fechaCierre, sucursal, ubicacion.valor )
      generarFicheroZM( fechaCierre, sucursal, ubicacion.valor )
      generarFicheroZS( fechaCierre, sucursal, ubicacion.valor )
      generarFicheroZV( fechaCierre, sucursal, ubicacion.valor )
      generarFicheroCLI( fechaCierre, sucursal, ubicacion.valor )
      generarFicheroff( fechaCierre, sucursal, ubicacion.valor )
      generarFicheroZZ( fechaCierre, sucursal, ubicacion.valor )
      InventorySearch.generateInFile( fechaCierre, fechaCierre )
      archivarCierre( fechaCierre )
    } catch ( Exception e ) {
      log.error( e.getMessage(), e )
    }
  }

  private void insertarAcuse( Date fechaCierre, Date horaCierre, Sucursal sucursal ) {
    // VENTA VAL
    Date fechaInicio = DateUtils.addDays( fechaCierre, -1 )
    Date fechaFin = DateUtils.addDays( fechaCierre, 1 )
    List<NotaVenta> notasVenta = notaVentaRepository.findByFechaHoraFacturaBetweenAndFacturaNotNull( fechaInicio, fechaFin )
    BigDecimal ventaVal = BigDecimal.ZERO
    notasVenta.each { notaVenta ->
      ventaVal = ventaVal + notaVenta.ventaTotal
    }
    // INGRE VAL
    List<Pago> pagos = pagoRepository.findBy_Fecha( fechaCierre )
    pagos = pagos.findAll { pago ->
      List<EntregadoExterno> ees = entregadoExternoRepository.findByIdFactura( pago.notaVenta?.id )
      pago.tipoPago != null && pago.tipoPago != 'l' && ees.size() == 0
    }
    BigDecimal ingreVal = BigDecimal.ZERO
    pagos.each { pago ->
      ingreVal = ingreVal + pago.monto
    }
    // MODIF VAL
    List<Modificacion> modificaciones = modificacionRepository.findBy_Fecha( fechaCierre )
    BigDecimal modifVal = BigDecimal.ZERO
    modificaciones.each { mod ->
      ModificacionImp modificacionImp = mod.modificacionImp
      if ( modificacionImp != null ) {
        modifVal = modifVal + ( mod.modificacionImp.ventaAnterior - mod.modificacionImp.ventaNueva )
      }
    }
    // DEV VAL
    List<Devolucion> devoluciones = devolucionRepository.findBy_Fecha( fechaCierre )
    BigDecimal devVal = BigDecimal.ZERO
    devoluciones.each { dev ->
      devVal = devVal + dev.monto
    }
    // CANCEL VAL
    BigDecimal cancelVal = BigDecimal.ZERO
    modificaciones.each { mod ->
      if ( 'can' == mod.tipo && mod.notaVenta?.factura != null ) {
        cancelVal = cancelVal + mod.notaVenta.ventaNeta
      }
    }
    def datos = [
        id_suv_val: StringUtils.defaultIfBlank( sucursal?.id?.toString(), '' ),
        fec_val: CustomDateUtils.format( fechaCierre, 'dd/MM/yyyy' ),
        hora_val: CustomDateUtils.format( horaCierre, 'HH:mm' ),
        venta_val: StringUtils.defaultIfBlank( ventaVal?.toPlainString(), '' ),
        ingre_val: StringUtils.defaultIfBlank( ingreVal?.toPlainString(), '' ),
        modif_val: StringUtils.defaultIfBlank( modifVal?.toPlainString(), '' ),
        dev_val: StringUtils.defaultIfBlank( devVal?.toPlainString(), '' ),
        cancel_val: StringUtils.defaultIfBlank( cancelVal?.toPlainString(), '' )
    ]
    String contenido = VelocityEngineUtils.mergeTemplateIntoString( velocityEngine, "template/contenido-acuse-cierre-diario.vm", "ASCII", datos )
    log.debug( "Insertando acuse: ${contenido}" )
    Acuse acuse = new Acuse()
    acuse.idTipo = 'CIERRE'
    acuse.folio = CustomDateUtils.format( fechaCierre, 'ddMMyyyy' )
    acuse.fechaAcuso = fechaCierre
    acuse.intentos = 0
    acuse.contenido = contenido
    acuse.fechaCarga = new Date()
    acuseRepository.save( acuse )
  }

  // Fichero Detalle Venta ZD
  private void generarFicheroZD( Date fechaCierre, Sucursal sucursal, String ubicacion ) {
    String nombreFichero = "2.${ sucursal.id }.${ CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ) }.ZD"
    log.info( "Generando fichero ZD ${ nombreFichero }" )
    List<NotaVenta> notasVenta = obtenerListaporFechaCierre( fechaCierre )
    log.debug( "Se han encontrado ${notasVenta.size()} Notas Venta" )
    List<DetalleNotaVenta> detallesTmp = new ArrayList<DetalleNotaVenta>()
    notasVenta.each { notaVenta -> detallesTmp.addAll( notaVenta.detalles ) }
    log.debug( "Se han encontrado ${detallesTmp.size()} Detalles Notas Venta" )
    def detalles = detallesTmp.collect { detalleNotaVenta ->
      BigDecimal precio = obtenerPrecioArticulo( detalleNotaVenta?.articulo?.articulo,
          detalleNotaVenta?.articulo?.id )
      String idTipoDetalle = ''
      if ( detalleNotaVenta.idTipoDetalle != null ) {
        switch ( detalleNotaVenta.idTipoDetalle ) {
          case 'VD':
            idTipoDetalle = '1'
            break
          case 'VI':
            idTipoDetalle = '2'
            break
          case 'FT':
            idTipoDetalle = '3'
            break
          case 'LD':
            idTipoDetalle = '4'
            break
          case 'LI':
            idTipoDetalle = '5'
            break
          case 'CI':
          case 'CD':
          case 'REM':
            idTipoDetalle = '6'
            break
          case 'NO':
            idTipoDetalle = '7'
            break
          case 'PE':
            idTipoDetalle = '8'
            break
          case 'SLC':
            idTipoDetalle = '9'
            break
        }
      }
      [
          id_factura: detalleNotaVenta.idFactura,
          sku: detalleNotaVenta.idArticulo,
          articulo: detalleNotaVenta.articulo.articulo,
          cantidad: detalleNotaVenta.cantidadFac,
          precio: precio != null ? precio.toPlainString() : '',
          precio_factura: detalleNotaVenta.precioFactura?.toPlainString(),
          id_tipo_detalle: idTipoDetalle,
          surte: StringUtils.isNotBlank( detalleNotaVenta.surte ) ? detalleNotaVenta.surte : '',
          descuento: detalleNotaVenta.notaVenta.montoDescuento ? detalleNotaVenta.notaVenta.montoDescuento : '',
          codigo_color: StringUtils.isNotBlank( detalleNotaVenta.articulo?.codigoColor ) ? detalleNotaVenta.articulo.codigoColor : '',
          factura: StringUtils.trimToEmpty( detalleNotaVenta.notaVenta?.factura )
      ]
    }

    if ( Registry.isFileFormatSunglass() ) {
      def datos = [
          sucursal: sucursal,
          fecha_ahora: CustomDateUtils.format( new Date(), 'dd-MM-yyyy' ),
          fecha_cierre: CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ),
          detalles: detalles,
          parte: '',
          numero_detalles: detalles.size()
      ]
      generarFichero( ubicacion, nombreFichero, 'fichero-ZD-si', datos )
    } else {
      def datos = [
          sucursal: sucursal,
          fecha_ahora: CustomDateUtils.format( new Date(), 'dd-MM-yyyy' ),
          fecha_cierre: CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ),
          detalles: detalles,
          numero_detalles: detalles.size()
      ]
      generarFichero( ubicacion, nombreFichero, 'fichero-ZD', datos )
    }
  }

  // Fichero Ordenes Venta ZO
  private void generarFicheroZO( Date fechaCierre, Sucursal sucursal, String ubicacion ) {
    String nombreFichero = "2.${ sucursal.id }.${ CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ) }.ZO"
    log.info( "Generando fichero ZO ${ nombreFichero }" )
    log.debug( "Para el dia ${CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy HH:mm' )}" )
    List<NotaVenta> notasVentaTmp = obtenerListaporFechaCierre( fechaCierre )
    def notasVenta = notasVentaTmp.collect { nv ->
      BigDecimal descuento = BigDecimal.ZERO
      log.info( "Generando fichero ZO ${ nombreFichero }" )
      if ( nv.detalles.find { it.articulo.articulo == 'BOD' || it.articulo.articulo == 'AUDI' } != null ) {
        log.debug( "El Descuento es ZERO porque hay articulos BOD o AUDI" )
      } else {
        BigDecimal suma = BigDecimal.ZERO
        nv.detalles.each { detalle ->
          if ( detalle.idTipoDetalle != 'N' ) {
            if ( [ 'NO', 'PE', 'OS', '2A', '3A', '4A', '5A', 'OD' ].contains( detalle.idTipoDetalle ) ) {
              suma += 0.01
            }
            BigDecimal precio = obtenerPrecioArticulo( detalle?.articulo?.articulo, detalle?.articulo?.id )
            if ( precio == null ) {
              throw new Exception( "El Articulo ${detalle?.articulo?.articulo} no tiene precio en la BBDD" )
            }
            BigDecimal porcentaje = detalle.tipoDetalle?.porcentaje
            if ( porcentaje == null ) {
              throw new Exception( "El Detalle de nota venta no tiene porcentaje" )
            }
            suma += detalle.cantidadFac * ( ( precio / 100 ) * porcentaje )
          } else {
            BigDecimal precio = obtenerPrecioArticulo( detalle?.articulo?.articulo, detalle?.articulo?.id )
            if ( precio == null ) {
              throw new Exception( "El Articulo ${detalle?.articulo?.articulo} no tiene precio en la BBDD" )
            }
            suma += detalle.cantidadFac * precio
          }
        }
        if ( suma > 0 ) {
          descuento = 100 - ( ( nv.ventaNeta * 100 ) / suma )
        }
      }
      // TIPO DESCUENTO
      String tipoDescuento = ''
      Descuento descuentoTmp = descuentoRepository.getBy_IdFactura( nv.id )
      Boolean hayDescuento = descuentoTmp != null
      Boolean noHayDescuento = !hayDescuento
      Boolean hayConvenio = StringUtils.isNotBlank( nv.idConvenio )
      Boolean noHayConvenio = !hayConvenio
      if ( descuento <= 10 && noHayConvenio && hayDescuento ) {
        tipoDescuento = 'G'
      } else if ( descuento > 10 && noHayConvenio && hayDescuento ) {
        tipoDescuento = 'D'
      } else if ( hayConvenio && noHayDescuento ) {
        tipoDescuento = 'C'
      } else if ( descuento <= 10 && hayConvenio && hayDescuento ) {
        tipoDescuento = 'CG'
      } else if ( descuento > 10 && hayConvenio && hayDescuento ) {
        tipoDescuento = 'CD'
      }
      // CLAVE
      String clave = ''
      if ( descuento > 10 ) {
        clave = descuentoTmp?.clave
      }
      // CLASIF CLIENTE
      ClasifCliente clasifCliente = clasifCliRepository.getBy_IdFactura( nv.id )

      //Pais Cliente
      /*String paisCliente = nv.cliente?.clientePais?.pais
      if( StringUtils.trimToNull(paisCliente) == null ){
        paisCliente = PAIS_DEFAULT
      }*/
      String paisCliente = PAIS_DEFAULT
      try{
        if(StringUtils.isNotEmpty(nv.udf2)){
          paisCliente = nv.udf2
        }
      } catch( Exception e ){}

        String tipoDescuentoSunI
        Double descuentoSunI = 0
        Double porcentajeDescSunI = 0
        String claveSunI
        Double montoOrdenPromDet = 0
        Double montoDescuento = Double.parseDouble( String.format("%.2f",nv.montoDescuento) )
        for( OrdenPromDet promo : nv.ordenPromDet ){
            montoOrdenPromDet = montoOrdenPromDet + Double.parseDouble( String.format("%.2f",promo.descuentoMonto) )
        }
        if( (Math.abs(montoDescuento) <= VALOR_CERO) && (Math.abs(montoOrdenPromDet) <= VALOR_CERO)){
            tipoDescuentoSunI = ''
            claveSunI = ''
        } else if( (Math.abs(montoDescuento) > VALOR_CERO) && (Math.abs(montoOrdenPromDet) <= VALOR_CERO) ){
            tipoDescuentoSunI = StringUtils.isBlank(descuentoTmp?.clave) ? 'G':'C'
            claveSunI = StringUtils.trimToEmpty(descuentoTmp?.clave)
        } else if( (Math.abs(montoDescuento) <= VALOR_CERO) && (Math.abs(montoOrdenPromDet) > VALOR_CERO) ){
            tipoDescuentoSunI = 'P'
            claveSunI = ''
        } else if( (Math.abs(montoDescuento) > VALOR_CERO) && (Math.abs(montoOrdenPromDet) > VALOR_CERO) ){
            tipoDescuentoSunI = 'M'
            claveSunI = StringUtils.trimToEmpty(descuentoTmp?.clave)
        }
        descuentoSunI = montoDescuento+montoOrdenPromDet
        porcentajeDescSunI = (descuentoSunI*100)/(descuentoSunI+Double.parseDouble( String.format("%.2f",nv.ventaTotal) ))
      [
          factura: StringUtils.defaultIfBlank( nv.factura, '' ),
          id_factura: StringUtils.defaultIfBlank( nv.id, '' ),
          id_empleado: StringUtils.defaultIfBlank( nv.idEmpleado, '' ),
          id_convenio: StringUtils.defaultIfBlank( nv.idConvenio, '' ),
          id_cliente: StringUtils.defaultIfBlank( nv.idCliente?.toString(), '' ),
          s_factura: StringUtils.defaultIfBlank( nv.sFactura, '' ),
          total: nv.ventaTotal.toString().replace( '$', '' ) ?: '0.00',
          udf4: StringUtils.defaultIfBlank( nv.udf4, '' ),
          descuento: StringUtils.defaultIfBlank( descuento?.toPlainString(), '' ),
          tipo_descuento: StringUtils.defaultIfBlank( tipoDescuento, '' ),
          tipo_descuentoSunI: StringUtils.defaultIfBlank( tipoDescuentoSunI, '' ),
          descuentoSunI: String.format("%.2f",descuentoSunI),
          porcentajeDescSunI: String.format("%.2f",porcentajeDescSunI),
          claveSunI: StringUtils.defaultIfBlank( claveSunI, '' ),
          clave: StringUtils.defaultIfBlank( clave, '' ),
          id_clasif_cli: StringUtils.defaultIfBlank( clasifCliente?.idClasifCliente?.toString(), '' ),
          fecha_entrega: nv.fechaEntrega != null ? CustomDateUtils.format( nv.fechaEntrega, 'dd-MM-yyyy' ) : '',
          hora_entrega: nv.horaEntrega != null ? CustomDateUtils.format( nv.horaEntrega, 'HH:mm:ss' ) : '',
          pais: paisCliente
      ]
    }
    def datos = [ sucursal: sucursal,
        fecha_ahora: CustomDateUtils.format( new Date(), 'dd-MM-yyyy' ),
        fecha_cierre: CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ),
        numero_registros: notasVenta.size(),
        convenio: '',
        tipoVenta: '',
        referido: '',
        tipoCliente: '',
        notas_venta: notasVenta ]
    if ( Registry.isFileFormatSunglass() ) {
      generarFichero( ubicacion, nombreFichero, 'fichero-ZO-si', datos )
    } else {
      generarFichero( ubicacion, nombreFichero, 'fichero-ZO', datos )
    }
  }

  // Fichero Detalle Pagos ZP
  private void generarFicheroZP( Date fechaCierre, Sucursal sucursal, String ubicacion ) {
    String nombreFichero = "3.${ sucursal.id }.${ CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ) }.ZP"
    log.info( "Generando fichero ZP ${ nombreFichero }" )
    List<Pago> pagosTmp = pagoRepository.findBy_Fecha( fechaCierre )
    log.debug( "Se han encontrado ${pagosTmp.size()} pagos" )
    pagosTmp = pagosTmp.findAll { pago ->
      List<EntregadoExterno> ees = entregadoExternoRepository.findByIdFactura( pago.notaVenta?.id )
      pago.tipoPago != null && pago.tipoPago != 'l' && ees.size() == 0
    }
    log.debug( "Hay ${pagosTmp.size()} pagos despues de filtrar" )
    def pagos = pagosTmp.collect { pago ->
      String soiOrigen = ''
      if ( '2' == pago.idSync ) {
        soiOrigen = pago.referenciaPago
      } else {
        Modificacion modificacion = modificacionRepository.getBy_IdFactura( pago.notaVenta.id )
        if ( StringUtils.isNotBlank( modificacion?.modificacionPag?.referenciaViejo ) ) { soiOrigen = modificacion.modificacionPag.referenciaViejo }
      }
      String idMod = ''
      if ( StringUtils.isNotBlank( soiOrigen ) && pago.idFPago == 'TR' ) {
        Modificacion modificacion = modificacionRepository.getBy_IdFactura( soiOrigen )
        idMod = modificacion?.id?.toString()
      }
      String parcialidad
      if ( Registry.isFileFormatSunglass() ) {
        parcialidad = ''
      } else {
        parcialidad = StringUtils.defaultIfBlank( pago.parcialidad?.trim(), '' )
      }
      [
          id_factura: pago.notaVenta.id,
          tipo_pago: pago.tipoPago == 'a' ? 'A' : '',
          id_f_pago: StringUtils.defaultIfBlank( pago.idFPago?.trim(), '' ),
          monto: pago.monto != null ? pago.monto.toPlainString() : '',
          soi_origen: StringUtils.defaultIfBlank( soiOrigen?.trim(), '' ),
          id_mod: StringUtils.defaultIfBlank( idMod?.trim(), '' ),
          factura: StringUtils.defaultIfBlank( pago.notaVenta.factura?.trim(), '' ),
          id_recibo: StringUtils.defaultIfBlank( pago.idRecibo?.trim(), '' ),
          parcialidad: parcialidad,
          clave_p: StringUtils.defaultIfBlank( pago.clave?.trim(), '' ),
          referencia_clave: StringUtils.defaultIfBlank( pago.referenciaClave?.trim(), '' ),
          id_banco_emi: StringUtils.defaultIfBlank( pago.idBancoEmisor?.trim(), '' ),
          id_terminal: StringUtils.defaultIfBlank( pago.idTerminal?.trim(), '' ),
          id_plan: StringUtils.defaultIfBlank( pago.idPlan?.trim(), '' ),
          id_pago: pago.id != null ? pago.id.toString() : ''
      ]
    }
    def datos = [
        sucursal: sucursal,
        fecha_ahora: CustomDateUtils.format( new Date(), 'dd-MM-yyyy' ),
        fecha_cierre: CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ),
        numero_registros: pagos.size(),
        pagos: pagos
    ]
    generarFichero( ubicacion, nombreFichero, 'fichero-ZP', datos )
  }

  // Fichero Detalle Modificaciones ZM
  private void generarFicheroZM( Date fechaCierre, Sucursal sucursal, String ubicacion ) {
    String nombreFichero = "3.${ sucursal.id }.${ CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ) }.ZM"
    log.info( "Generando fichero ZM ${ nombreFichero }" )
    List<Modificacion> modificaciones = modificacionRepository.findBy_Fecha( fechaCierre )
    log.debug( "Se han encontrado ${ modificaciones.size() } modificaciones" )
    Integer mod1 = 0
    Integer mod2 = 0
    modificaciones.each {
      if ( it.tipo != 'ste' && it.tipo != 'emp' ) { mod1++ }
      if ( it.tipo == 'pag' ) { mod2++ }
    }
    modificaciones = modificaciones.findAll { it.tipo != 'ste' && it.tipo != 'emp' }
    def mods = modificaciones.collect { mod ->
      String tipo = ''
      switch ( mod.tipo ) {
        case 'imp':
          if ( mod.modificacionImp.ventaNueva - mod.modificacionImp.ventaAnterior == 0 ) {
            tipo = 'I'
          } else {
            tipo = 'M'
          }
          break
        case 'fac':
          tipo = 'F'
          break
        case 'can':
          tipo = 'C'
          break
        case 'pag':
          tipo = 'P'
          break
      }
      String monto = ''
      if ( mod.tipo == 'imp' || mod.tipo == 'can' ) {
        BigDecimal ventaNueva = mod.modificacionImp?.ventaNueva
        if ( ventaNueva != null ) {
          monto = ventaNueva.toPlainString()
        }
      }
      String total = ''
      if ( mod.tipo == 'imp' ) {
        Devolucion devolucion = devolucionRepository.findOne( mod.id )
        total = devolucion?.monto?.toPlainString()
      } else if ( mod.tipo == 'can' ) {
        total = mod.notaVenta?.sumaPagos?.toPlainString()
      }
      String descuento = ''
      if ( mod.tipo == 'imp' ) {
        descuento = ( 100 - ( ( mod.notaVenta.ventaNeta * 100 ) / mod.notaVenta.ventaTotal ) ).toPlainString()
      }
      Descuento descuentoTmp = descuentoRepository.getBy_IdFactura( mod.idFactura )
      String tipoDescuento = ''
      if ( mod.tipo == 'imp' ) {
        switch ( descuentoTmp.tipoClave ) {
          case 'GERENTE':
            tipoDescuento = 'G'
            break
          case 'CONVENIO':
            tipoDescuento = 'C'
            break
          case 'DIRECCION':
            tipoDescuento = 'D'
            break
          case 'MIXTO':
            if ( StringUtils.isNotBlank( descuentoTmp.clave ) ) {
              tipoDescuento = 'CD'
            } else if ( StringUtils.isNotBlank( descuentoTmp.porcentaje ) ) {
              tipoDescuento = 'GD'
            }
            break
        }
      }
      [
          id_factura: mod.idFactura,
          id_mod: mod.id,
          tipo: tipo,
          monto: monto,
          total: total,
          observaciones: mod.observaciones,
          descuento: descuento,
          tipo_descuento: tipoDescuento,
          clave: mod.tipo == 'imp' ? descuentoTmp.clave : '',
          convenio: mod.tipo == 'imp' ? mod.notaVenta.idConvenio : '',
          factura: mod.notaVenta.factura
      ]
    }
    def datos = [
        sucursal: sucursal,
        fecha_ahora: CustomDateUtils.format( new Date(), 'dd-MM-yyyy' ),
        fecha_cierre: CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ),
        numero_registros: ( mod1 - mod2 ),
        modificaciones: mods
    ]
    generarFichero( ubicacion, nombreFichero, 'fichero-ZM', datos )
  }

  // Fichero Detalle Depositos ZS
  private void generarFicheroZS( Date fechaCierre, Sucursal sucursal, String ubicacion ) {
    String nombreFichero = "2.${ sucursal.id }.${ CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ) }.ZS"
    log.info( "Generando fichero ZS ${ nombreFichero }" )
    List<Deposito> depositos = depositoRepository.findBy_Fecha( fechaCierre )
    def datos = [ sucursal: sucursal,
        fecha_ahora: CustomDateUtils.format( new Date(), 'dd-MM-yyyy' ),
        fecha_cierre: CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ),
        numero_registros: depositos.size(),
        depositos: depositos,
        sdf: new SimpleDateFormat( 'dd-MM-yyyy' ) ]
    generarFichero( ubicacion, nombreFichero, 'fichero-ZS', datos )
  }

  // Fichero Detalle Devoluciones ZV
  private void generarFicheroZV( Date fechaCierre, Sucursal sucursal, String ubicacion ) {
    String nombreFichero = "3.${ sucursal.id }.${ CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ) }.ZV"
    log.info( "Generando fichero ZV ${ nombreFichero }" )
    List<Devolucion> devoluciones = devolucionRepository.findBy_Fecha( fechaCierre )
    def datos = [ sucursal: sucursal,
        fecha_ahora: CustomDateUtils.format( new Date(), 'dd-MM-yyyy' ),
        fecha_cierre: CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ),
        numero_registros: devoluciones.size(),
        devoluciones: devoluciones ]
    generarFichero( ubicacion, nombreFichero, 'fichero-ZV', datos )
  }

  // Fichero Clientes CLI
  private void generarFicheroCLI( Date fechaCierre, Sucursal sucursal, String ubicacion ) {
    String nombreFichero = "3.${ sucursal.id }.${ CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ) }.CLI"

    log.info( "Generando fichero CLI ${ nombreFichero }" )
    List<Cliente> clientes = clienteRepository.findByFechaAlta( fechaCierre )
    def datos = [ sucursal: sucursal,
        fecha_ahora: CustomDateUtils.format( new Date(), 'dd-MM-yyyy' ),
        fecha_cierre: CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ),
        numero_registros: clientes.size(),
        clientes: clientes ]
    generarFichero( ubicacion, nombreFichero, 'fichero-CLI', datos )
  }

  // Fichero Facturas Fiscales ff
  private void generarFicheroff( Date fechaCierre, Sucursal sucursal, String ubicacion ) {
    String nombreFichero = "3.${ sucursal.id }.${ CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ) }.ff"

    log.info( "Generando fichero ff ${ nombreFichero }" )
    List<NotaFactura> facturas = notaFacturaRepository.findByFechaImpresion( fechaCierre )
    def datos = [ sucursal: sucursal,
        fecha_ahora: CustomDateUtils.format( new Date(), 'dd-MM-yyyy' ),
        fecha_cierre: CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ),
        numero_registros: facturas.size(),
        facturas: facturas ]
    generarFichero( ubicacion, nombreFichero, 'fichero-ff', datos )
  }


  // Fichero Promociones ZZ
  private void generarFicheroZZ( Date fechaCierre, Sucursal sucursal, String ubicacion ) {
    String nombreFichero = "3.${ sucursal.id }.${ CustomDateUtils.format( fechaCierre, 'dd-MM-yyyy' ) }.ZZ"
    log.info( "Generando fichero ZZ ${ nombreFichero }" )
    List<OrdenPromDet> promociones = ordenPromDetRepository.findByFechaMod( fechaCierre )
    def datos = [ sucursal: sucursal,
        fecha_ahora: CustomDateUtils.format( new Date(), 'dd/MM/yyyy' ),
        fecha_cierre: CustomDateUtils.format( fechaCierre, 'dd/MM/yyyy' ),
        numero_registros: promociones.size(),
        promociones: promociones ]
    generarFichero( ubicacion, nombreFichero, 'fichero-ZZ', datos )
  }

  private void generarFicheroInv( ){
    log.debug( "generarArchivoInventario( )" )

    Parametro ubicacion = Registry.find( TipoParametro.RUTA_CIERRE )
    Parametro sucursal = Registry.find( TipoParametro.ID_SUCURSAL )
    String nombreFichero = "${ String.format("%02d", NumberFormat.getInstance().parse(sucursal.valor)) }.${ CustomDateUtils.format( new Date(), 'dd-MM-yyyy' ) }.${ CustomDateUtils.format( new Date(), 'HHmm' ) }.inv"
    log.info( "Generando archivo ${ nombreFichero }" )
    QArticulo articulo = QArticulo.articulo1
    List<Articulo> lstArticulos = articuloRepository.findAll( articulo.cantExistencia.ne( 0 ).and(articulo.cantExistencia.isNotNull()), articulo.id.asc() )
    def datos = [
        articulos:lstArticulos
    ]
    Boolean generado = true
    try{
      String fichero = "${ ubicacion.valor }/${ nombreFichero }"
      log.debug( "Generando Fichero: ${ fichero }" )
      log.debug( "Plantilla: fichero-inv.vm" )
      File file = new File( fichero )
      if ( file.exists() ) { file.delete() } // Borramos el fichero si ya existe para crearlo de nuevo
      log.debug( 'Creando Writer' )
      FileWriter writer = new FileWriter( file )
      datos.writer = writer
      log.debug( 'Merge template' )
      VelocityEngineUtils.mergeTemplate( velocityEngine, "template/fichero-inv.vm", "ASCII", datos, writer )
      log.debug( 'Writer close' )
      writer.close()

    }catch(Exception e){
      log.error( "Error al generar archivo de inventario", e )
      generado = false
    }
  }


  @Override
  Deposito buscarDepositoPorId( Integer id ) {
    if ( id != null ) {
      Deposito deposito = depositoRepository.findBy_Id( id )
      log.debug( "DEPOSITO -> ${ deposito.dump() }" )
      return deposito
    }
    null
  }

  @Override
  @Transactional
  void guardarDeposito( Deposito deposito ) {
    log.debug( "GUARDANDO DEPOSITO -> ${ deposito.dump() }" )
    if ( deposito?.id == null ) {
      List<Deposito> depositos = depositoRepository.findBy_Fecha( deposito.fechaCierre )
      Integer numeroDeposito = 0
      depositos.each {
        if ( it.numeroDeposito > numeroDeposito ) {
          numeroDeposito = it.numeroDeposito
        }
      }
      if ( numeroDeposito == 0 ) {
        numeroDeposito = 1
      } else {
        numeroDeposito = numeroDeposito + 1
      }
      deposito.numeroDeposito = numeroDeposito
    }
    depositoRepository.save( deposito )
  }

  @Override
  @Transactional
  void actualizarDeposito( Deposito deposito ) {
    log.debug( "ACTUALIZANDO DEPOSITO: ${ deposito.dump() }" )
    depositoRepository.save( deposito )
  }

  @Override
  @Transactional
  void eliminarDeposito( Integer id ) {
    Deposito deposito = depositoRepository.findOne( QDeposito.deposito.id.eq( id ) )
    depositoRepository.delete( deposito.id )
  }

  @Override
  List<Pago> buscarPagosPorFechaCierrePorFacturaPorTerminal( Date fechaCierre, String terminal, String plan ) {
    List<Pago> pagos = pagoRepository.findBy_Fecha_IdFactura_DescripcionTerminal( fechaCierre, terminal, plan ) ?: [ ]
    List<Pago> selected = new ArrayList<Pago>()
    for ( Pago p : pagos ) {
      if ( 'TCM'.equals( p.formaPago?.id ) || 'TCD'.equals( p.formaPago?.id ) || 'TDM'.equals( p.formaPago?.id )
          || 'TDD'.equals( p.formaPago?.id ) ) {
        if( !StringUtils.trimToEmpty(p.notaVenta.factura).isEmpty() ){
          selected.add( p )
        }
      }
    }
    return selected
  }

  @Override
  Pago buscarPagoPorId( Integer id ) {
    pagoRepository.findBy_Id( id )
  }

  @Override
  List<ResumenDiario> buscarResumenDiarioPorFechaPorTerminal( Date fechaCierre, String terminal ) {
    String termUC = StringUtils.trimToEmpty( terminal ).toUpperCase()
    List<ResumenDiario> atCloseDate = resumenDiarioRepository.findByFechaCierre( fechaCierre )
    List<ResumenDiario> selected = new ArrayList<ResumenDiario>()
    for ( ResumenDiario resumen : atCloseDate ) {
      if ( 'TODAS'.equals( termUC ) || termUC.length() == 0
          || termUC.equalsIgnoreCase( StringUtils.trimToEmpty( resumen.idTerminal ).toUpperCase() ) ) {
        selected.add( resumen )
      }
    }
    return selected
  }

  @Override
  @Transactional
  boolean cargarDatosCierreDiario( Date fecha ) {
    log.info( "cargando datos para el cierre diario con fecha: ${fecha?.format( DATE_FORMAT )}" )
    CierreDiario dia = cierreDiarioRepository.findOne( fecha )
    log.info( "se obtiene cierreDiario con fecha: ${dia?.fechaCierre?.format( DATE_FORMAT )}" )
    log.info( "y estado: ${dia?.estado}" )
    //if ( dia?.fecha && dia?.estado?.equalsIgnoreCase( 'a' ) ) {
      if ( cargarVouchersResumenes( fecha ) ) {
        if ( cargarResumenTerminales( fecha ) ) {
          CierreDiario cierreDiario = actualizarCierreDiario( fecha )
          if ( cierreDiario?.fecha ) {
            return true
          } else {
            log.error( "Error al cargar las tablas de Cierre Diario" )
          }
        } else {
          log.error( "Error al cargar las tablas de Resumenes terminales" )
        }
      } else {
        log.error( "Error al cargar las tablas de Vouchers" )
      }
    /*} else {
      log.warn( "no se cargaron los datos, el dia no tiene estado abierto" )
    } */
    return false
  }

  @Transactional
  boolean cargarVouchersResumenes( Date fecha ) {
    log.info( "cargando vouchers y resumenes para la fecha: ${fecha?.format( DATE_FORMAT )}" )
    if ( fecha ) {
      limpiarVouchersResumenes( fecha )
      Set<VoucherTmp> vouchers = [ ]
      Set<ResumenDiario> resumenes = [ ]
      Set<Terminal> terminales = terminalRepository.findByDescripcionNotNullOrderByDescripcionAsc() ?: [ ]
      Set<String> terminalesId = terminales*.id
      log.debug( "obteniendo terminales: ${terminalesId}" )
      Date fechaInicio = fecha.clearTime()
      Date fechaFin = new Date( fecha.next().time - 1 )
      log.debug( "buscando pagos del: ${fechaInicio.format( DATE_TIME_FORMAT )} al: ${fechaFin.format( DATE_TIME_FORMAT )}" )
      Set<Pago> pagos = pagoRepository.findByFechaBetweenAndIdTerminalIn( fechaInicio, fechaFin, terminalesId ) ?: [ ]
      procesarVouchersResumenesDePagos( pagos, vouchers, resumenes )
      log.debug( "buscando pagos externos del:${fechaInicio.format( DATE_TIME_FORMAT )} al: ${fechaFin.format( DATE_TIME_FORMAT )}" )
      Set<PagoExterno> pagosExternos = pagoExternoRepository.findByFechaBetweenAndIdTerminalIn( fechaInicio, fechaFin, terminalesId ) ?: [ ]
      procesarVouchersResumenesDePagosExternos( pagosExternos, vouchers, resumenes )
      log.debug( "buscando devoluciones del: ${fechaInicio.format( DATE_TIME_FORMAT )} al: ${fechaFin.format( DATE_TIME_FORMAT )} " )
      Set<Devolucion> devoluciones = devolucionRepository.findByFechaBetweenAndTipo( fechaInicio, fechaFin, 'd' )
      procesarVouchersResumenesDeDevoluciones( devoluciones, vouchers, resumenes )
      resumenes = actualizarResumenes( resumenes, vouchers )
      voucherTmpRepository.save( vouchers )
      voucherTmpRepository.flush()
      resumenDiarioRepository.save( resumenes )
      resumenDiarioRepository.flush()
      return true
    } else {
      log.warn( "no se cargan vouchers ni resumenes, parametros invalidos" )
    }
    return false
  }

  @Transactional
  private void limpiarVouchersResumenes( Date fecha ) {
    log.info( "limpiando vouchers y resumenes para la fecha: ${fecha?.format( DATE_FORMAT )}" )
    if ( fecha ) {
      Set<VoucherTmp> vouchers = voucherTmpRepository.findByFechaCierre( fecha ) ?: [ ]
      log.debug( "limpiando # ${vouchers?.size()} vouchers existentes con fechaCierre: ${fecha.format( DATE_FORMAT )}" )
      voucherTmpRepository.deleteInBatch( vouchers )
      Set<ResumenDiario> resumenes = resumenDiarioRepository.findByFechaCierre( fecha ) ?: [ ]
      log.debug( "limpiando # ${resumenes?.size()} resumenes existentes con fechaCierre: ${fecha.format( DATE_FORMAT )}" )
      resumenDiarioRepository.deleteInBatch( resumenes )
    } else {
      log.warn( "no se limpiaron vouchers y resumenes, parametros invalidos" )
    }
  }

  private void procesarVouchersResumenesDePagos( Set<Pago> pagos, Set<VoucherTmp> vouchers, Set<ResumenDiario> resumenes ) {
    log.info( "procesando vouchers y resumenes a partir de pagos: ${pagos*.id}" )
    pagos?.removeAll { Pago tmp ->
      'TR'.equalsIgnoreCase( tmp?.idFPago?.trim() ) ||
          'C'.equalsIgnoreCase( tmp?.idPlan?.trim() ) ||
          'D'.equalsIgnoreCase( tmp?.idPlan?.trim() ) ||
          StringUtils.trimToEmpty(tmp?.notaVenta?.factura).equalsIgnoreCase( '' )
    }
    log.debug( "pagos filtrados: ${pagos*.id}" )
    BigDecimal dolares = BigDecimal.ZERO
    pagos?.each { Pago pago ->
      String idTerminal = pago?.terminal?.descripcion?.trim()
      String idPlan = pago?.idPlan?.trim()
      if( Registry.isCardPaymentInDollars(pago?.idFPago) ){
        if( StringUtils.trimToEmpty(pago?.idPlan).equalsIgnoreCase('') ){
          pago?.idPlan = '0'
        }
      }
      String idFPago = pago?.idFPago?.trim()
      Date fecha = pago?.fecha
      VoucherTmp voucher = vouchers?.find { VoucherTmp tmp ->
        tmp?.idTerminal?.trim()?.equalsIgnoreCase( idTerminal ) &&
            tmp?.idFPago?.trim()?.equalsIgnoreCase( idFPago ) &&
            tmp?.plan?.trim()?.equalsIgnoreCase( idPlan ) &&
            tmp?.numeroTarjeta?.trim()?.equalsIgnoreCase( pago?.clave?.trim() ) &&
            tmp?.autorizacion?.trim()?.equalsIgnoreCase( pago?.referenciaClave?.trim() )
      }
      if ( voucher?.fechaCierre ) {
        voucher.cantidad += 1
      } else {
        voucher = new VoucherTmp(
            idTerminal: idTerminal,
            numeroTarjeta: pago?.clave?.trim(),
            autorizacion: pago?.referenciaClave?.trim(),
            idFPago: idFPago,
            plan: idPlan,
            cantidad: 1,
            fechaCierre: fecha
        )
      }
      vouchers?.add( voucher )
      ResumenDiario resumen = resumenes?.find { ResumenDiario tmp ->
        tmp?.idTerminal?.trim()?.equalsIgnoreCase( idTerminal ) &&
            tmp?.plan?.trim()?.equalsIgnoreCase( idPlan )
      }
      if ( resumen?.fechaCierre ) {
        resumen.facturas += 1
        resumen.importe += pago?.monto
        if( Registry.isCardPaymentInDollars(pago?.idFPago) && Registry.isCardPaymentInDollars(resumen.tipo) &&
            ((pago?.idPlan.isNumber() || StringUtils.trimToEmpty(pago?.idPlan).equalsIgnoreCase('')) && resumen.plan.isNumber()) ){
          if( StringUtils.trimToEmpty(pago?.idPlan).equalsIgnoreCase('') ){
            pago?.idPlan = '0'
          }
          dolares = NumberFormat.getInstance().parse(pago?.idPlan).doubleValue()+NumberFormat.getInstance().parse(resumen?.plan).doubleValue()
          resumen.plan = dolares.toString()
        }
      } else {
        resumen = new ResumenDiario(
            idTerminal: idTerminal,
            facturas: 1,
            tipo: idFPago,
            plan: idPlan,
            importe: pago?.monto,
            fechaCierre: fecha
        )
      }
      resumenes?.add( resumen )
    }
  }

  private void procesarVouchersResumenesDePagosExternos( Set<PagoExterno> pagos, Set<VoucherTmp> vouchers, Set<ResumenDiario> resumenes ) {
    log.info( "procesando vouchers y resumenes a partir de pagos externos: ${pagos*.id}" )
    pagos?.removeAll { PagoExterno tmp ->
      'C'.equalsIgnoreCase( tmp?.idPlan?.trim() ) ||
          'D'.equalsIgnoreCase( tmp?.idPlan?.trim() )
    }
    log.debug( "pagos externos filtrados: ${pagos*.id}" )
    pagos?.each { PagoExterno pago ->
      String idTerminal = pago?.terminal?.descripcion?.trim()
      String idPlan = pago?.idPlan?.trim()
      String idFPago = pago?.idFPago?.trim()
      Date fecha = pago?.fecha
      VoucherTmp voucher = vouchers?.find { VoucherTmp tmp ->
        tmp?.idTerminal?.trim()?.equalsIgnoreCase( idTerminal ) &&
            tmp?.idFPago?.trim()?.equalsIgnoreCase( idFPago ) &&
            tmp?.plan?.trim()?.equalsIgnoreCase( idPlan ) &&
            tmp?.numeroTarjeta?.trim()?.equalsIgnoreCase( pago?.claveP?.trim() ) &&
            tmp?.autorizacion?.trim()?.equalsIgnoreCase( pago?.refClave?.trim() )
      }
      if ( voucher?.fechaCierre ) {
        voucher.cantidad += 1
      } else {
        voucher = new VoucherTmp(
            idTerminal: idTerminal,
            numeroTarjeta: pago?.claveP?.trim(),
            autorizacion: pago?.refClave?.trim(),
            idFPago: idFPago,
            plan: idPlan,
            cantidad: 1,
            fechaCierre: fecha
        )
      }
      vouchers?.add( voucher )
      ResumenDiario resumen = resumenes.find { ResumenDiario tmp ->
        tmp?.idTerminal?.trim()?.equalsIgnoreCase( idTerminal ) &&
            tmp?.plan?.trim()?.equalsIgnoreCase( idPlan )
      }
      if ( resumen?.fechaCierre ) {
        resumen.facturas += 1
        resumen.importe += pago?.monto
      } else {
        resumen = new ResumenDiario(
            idTerminal: idTerminal,
            facturas: 1,
            tipo: idFPago,
            plan: idPlan,
            importe: pago?.monto,
            fechaCierre: fecha
        )
      }
      resumenes?.add( resumen )
    }
  }

  private void procesarVouchersResumenesDeDevoluciones( Set<Devolucion> devoluciones, Set<VoucherTmp> vouchers, Set<ResumenDiario> resumenes ) {
    log.info( "procesando vouchers y resumenes a partir de devoluciones: ${devoluciones*.idMod}" )
    devoluciones?.retainAll { Devolucion tmp ->
      StringUtils.isNotBlank( tmp?.pago?.idTerminal )
    }
    log.debug( "devoluciones filtradas: ${devoluciones*.idMod}" )
    devoluciones?.each { Devolucion devolucion ->
      String idTerminal = devolucion?.pago?.terminal?.descripcion?.trim()
      Date fecha = devolucion?.fecha
      String idPlan = DateUtils.isSameDay( devolucion?.pago?.fecha, fecha ) ? 'C' : 'D'
      VoucherTmp voucher = vouchers?.find { VoucherTmp tmp ->
        tmp?.idTerminal?.trim()?.equalsIgnoreCase( idTerminal ) &&
            tmp?.plan?.trim()?.equalsIgnoreCase( idPlan )
      }
      if ( voucher?.fechaCierre ) {
        voucher.cantidad += 1
      } else {
        voucher = new VoucherTmp(
            idTerminal: idTerminal,
            plan: idPlan,
            cantidad: 1,
            fechaCierre: fecha
        )
      }
      vouchers?.add( voucher )
      ResumenDiario resumen = resumenes.find { ResumenDiario tmp ->
        tmp?.idTerminal?.trim()?.equalsIgnoreCase( idTerminal ) &&
            tmp?.plan?.trim()?.equalsIgnoreCase( idPlan )
      }
      if ( resumen?.fechaCierre ) {
        resumen.facturas += 1
        resumen.importe += devolucion?.monto
      } else {
        resumen = new ResumenDiario(
            idTerminal: idTerminal,
            facturas: 1,
            plan: idPlan,
            importe: devolucion?.monto,
            fechaCierre: fecha
        )
      }
      resumenes?.add( resumen )
    }
  }

  private Set<ResumenDiario> actualizarResumenes( Set<ResumenDiario> resumenes, Set<VoucherTmp> vouchers ) {
    log.info( "actualizando resumenes: ${resumenes*.idTerminal}" )
    resumenes?.each { ResumenDiario resumen ->
      String idTerminal = resumen?.idTerminal
      String tipo = resumen?.tipo?.trim()
      String plan = resumen?.plan?.trim()
      Integer cantidad = vouchers?.count { VoucherTmp tmp ->
        tmp?.idTerminal?.trim()?.equalsIgnoreCase( idTerminal ) &&
            tmp?.plan?.trim()?.equalsIgnoreCase( plan ) &&
            tmp?.idFPago?.trim()?.equalsIgnoreCase( tipo )
      }
      resumen?.vouchers = cantidad
      switch ( plan ) {
        case 'N': resumen?.orden = '1'
          break
        case '': resumen?.orden = '2'
          break
        case '6M': resumen?.orden = '3'
          break
        case 'C': resumen?.orden = '4'
          break
        case 'D': resumen?.orden = '5'
          break
      }
    }
    return resumenes
  }

  @Transactional
  boolean cargarResumenTerminales( Date fecha ) {
    log.info( "cargando resumenes terminales para la fecha: ${fecha?.format( DATE_FORMAT )}" )
    if ( fecha ) {
      Set<ResumenTerminal> existentes = resumenTerminalRepository.findByFechaCierre( fecha ) ?: [ ]
      log.debug( "limpiando # ${existentes?.size()} resumenes terminales existentes con fechaCierre: ${fecha.format( DATE_FORMAT )}" )
      resumenTerminalRepository.deleteInBatch( existentes )
      Set<Terminal> terminales = terminalRepository.findByDescripcionNotNullOrderByDescripcionAsc() ?: [ ]
      Set<String> terminalesId = terminales*.id
      log.debug( "obteniendo terminales: ${terminalesId}" )
      Set<ResumenDiario> diarios = resumenDiarioRepository.findByFechaCierreAndIdTerminalIn( fecha, terminalesId ) ?: [ ]
      log.debug( "obteniendo resumenes diarios: ${diarios*.id}" )
      diarios?.each { ResumenDiario diario ->
        String terminal = diario?.idTerminal?.trim()
        String plan = diario?.plan?.trim()
        BigDecimal importe = diario?.importe ?: 0
        BigDecimal total = BigDecimal.ZERO
        if ( 'C'.equalsIgnoreCase( plan ) || 'D'.equalsIgnoreCase( plan ) ) {
          total = total.subtract( importe )
        } else {
          total = total.add( importe )
        }
        ResumenTerminal resumen = new ResumenTerminal(
            fechaCierre: fecha,
            total: total,
            idTerminal: terminal
        )
        resumenTerminalRepository.save( resumen )
      }
      return true
    } else {
      log.warn( "no se cargan resumenes terminales, parametros invalidos" )
    }
    return false
  }

  @Override
  @Transactional
  CierreDiario actualizarCierreDiario( Date fecha ) {
    fecha = DateUtils.truncate( fecha, Calendar.DAY_OF_MONTH );
    log.info( "actualizando cierre diario para la fecha: ${fecha?.format( DATE_FORMAT )}" )
    CierreDiario dia = cierreDiarioRepository.findOne( fecha )
    log.info( "se obtiene cierreDiario con fecha: ${dia?.fecha?.format( DATE_FORMAT )} estado: ${dia?.estado}" )
    if ( dia?.fecha ) {
    //if ( dia?.fecha && dia?.estado?.equalsIgnoreCase( 'a' ) ) {
      Parametro parametro = parametroRepository.findOne( TipoParametro.CONV_NOMINA.value )
      String[] convenios = parametro?.valor?.split( ',' )
      log.debug( "se obtienen convenios nomina: ${convenios}" )
      Date fechaFin = new Date( fecha.next().time - 1 )
      log.debug( "buscando notas del: ${fecha.format( DATE_TIME_FORMAT )} al: ${fechaFin.format( DATE_TIME_FORMAT )}" )
      //Set<NotaVenta> notas = notaVentaRepository.findByFechaHoraFacturaBetweenAndFacturaNotNull( fecha, fechaFin ) ?: [ ]
      QNotaVenta notVen = QNotaVenta.notaVenta
      Set<NotaVenta> notas = notaVentaRepository.findAll(notVen.fechaHoraFactura.between( fecha, fechaFin).and(notVen.factura.isNotEmpty()).and(notVen.factura.isNotNull())) ?: [ ]
      notas.removeAll { NotaVenta tmp ->
        convenios?.contains( tmp?.idConvenio ) || StringUtils.isBlank( tmp?.factura )
      }
      log.debug( "notas obtenidas: ${notas?.id}" )
      BigDecimal ventaBruta = BigDecimal.ZERO
      Integer facturaInicial = Integer.MAX_VALUE
      Integer facturaFinal = 0
      notas.each { NotaVenta nota ->
        Integer numeroFactura = nota?.factura?.integer ? nota.factura.toInteger() : 0
        for(DetalleNotaVenta det : nota?.detalles ){
          BigDecimal precio = det.getPrecioUnitLista().multiply( new BigDecimal(det.getCantidadFac()) );
          ventaBruta = ventaBruta.add(precio)
        }
        facturaInicial = numeroFactura < facturaInicial ? numeroFactura : facturaInicial
        facturaFinal = numeroFactura > facturaFinal ? numeroFactura : facturaFinal
      }
      BigDecimal modificaciones = BigDecimal.ZERO
      BigDecimal cancelaciones = BigDecimal.ZERO
      Integer modificados = 0
      Integer cancelados = 0
      Set<Modificacion> mods = modificacionRepository.findByFechaBetween( fecha, fechaFin ) ?: [ ]
      BigDecimal desc = BigDecimal.ZERO
      BigDecimal descOrdenPromDet = BigDecimal.ZERO
      Integer cantDesc = 0
      //TODO: BMM cambiar el campo modificaciones (de la entidad)
      for( NotaVenta nv : notas ){
        List<OrdenPromDet> lstOrdenPromDet = ordenPromDetRepository.findByIdFactura( nv.id )
        for( OrdenPromDet promo : lstOrdenPromDet ){
          desc = desc.add( promo.descuentoMonto )
          cantDesc = cantDesc+1
        }
        if( nv.montoDescuento.abs().compareTo(VALOR_CERO) > 0 ){
          desc = desc.add( nv.montoDescuento )
          cantDesc = cantDesc+1
        }
      }
      mods.each { Modificacion mod ->
        ModificacionImp imp = mod?.modificacionImp
        if ( imp?.id ) {
          BigDecimal anterior = ( imp?.ventaAnterior ) ?: 0
          BigDecimal nuevo = ( imp?.ventaNueva ) ?: 0
          modificaciones += ( anterior - nuevo )
          modificados += 1
        } else if ( 'can'.equalsIgnoreCase( mod?.tipo ) ) {
          cancelaciones += ( mod?.notaVenta?.ventaNeta ) ?: 0
          cancelados += 1
        }
      }
      modificados = modificados+cantDesc
      modificaciones = modificaciones.add(desc)

      BigDecimal ingresoBruto = BigDecimal.ZERO
      BigDecimal efectivoRecibido = BigDecimal.ZERO
      BigDecimal dolaresRecibidos = BigDecimal.ZERO

      QPago payment = QPago.pago
      Set<Pago> pagos = pagoRepository.findAll( payment.fecha.between(fecha, fechaFin).
          and(payment.notaVenta.factura.isNotEmpty()).and(payment.notaVenta.factura.isNotNull()) ) ?: [ ] as Set<Pago>
      //Set<Pago> pagos = pagoRepository.findByFechaBetween( fecha, fechaFin ) ?: [ ]
      pagos.removeAll { Pago tmp ->
        convenios?.contains( tmp?.notaVenta?.idConvenio ) || 'BD'.equalsIgnoreCase( tmp?.idFPago ) ||
            StringUtils.trimToEmpty(tmp?.notaVenta?.factura).isEmpty()
        // TODO revisar condicion || 'l'.equalsIgnoreCase( tmp?.tipoPago )
      }
      pagos.each { Pago pago ->
        BigDecimal montoPago = ( pago?.monto ) ?: 0
        ingresoBruto += montoPago
        if ( 'EFM'.equalsIgnoreCase( pago?.idFPago ) ) {
          efectivoRecibido += montoPago
        } else if ( 'EFD'.equalsIgnoreCase( pago?.idFPago ) ) {
          dolaresRecibidos += montoPago
        }
      }
      BigDecimal devoluciones = BigDecimal.ZERO
      BigDecimal efectivoDevoluciones = BigDecimal.ZERO
      BigDecimal dolaresDevoluciones = BigDecimal.ZERO
      Set<Devolucion> devs = devolucionRepository.findByFechaBetween( fecha, fechaFin ) ?: [ ]
      devs.each { Devolucion devolucion ->
        BigDecimal montoDevolucion = ( devolucion?.monto ) ?: 0
        devoluciones += montoDevolucion
        if ( 'd'.equalsIgnoreCase( devolucion?.tipo ) ) {
          if ( 'EFM'.equalsIgnoreCase( devolucion?.idFormaPago ) ) {
            efectivoDevoluciones += montoDevolucion
          } else if ( 'EFD'.equalsIgnoreCase( devolucion?.idFormaPago ) ) {
            dolaresDevoluciones += montoDevolucion
          }
        }
      }
      BigDecimal efectivoExternos = BigDecimal.ZERO
      Set<PagoExterno> externos = pagoExternoRepository.findByFechaBetween( fecha, fechaFin ) ?: [ ]
      externos.each { PagoExterno externo ->
        BigDecimal montoPago = ( externo?.monto ) ?: 0
        if ( 'EFM'.equalsIgnoreCase( externo?.idFPago ) ) {
          efectivoExternos += montoPago
          // Code commented for Sunglass
          // } else if ( 'ES'.equalsIgnoreCase( externo?.idFPago ) ) {
          //   efectivoExternos += montoPago
        }
      }
      BigDecimal ventaNeta = ( ventaBruta - cancelaciones - modificaciones )
      BigDecimal ingresoNeto = ( ingresoBruto - devoluciones )
      BigDecimal efectivoNeto = ( ( efectivoRecibido + efectivoExternos ) - efectivoDevoluciones )
      dia.ventaBruta = ventaBruta
      dia.cantidadVentas = notas.size()
      dia.modificaciones = modificaciones
      dia.cantidadModificaciones = modificados
      dia.cancelaciones = cancelaciones
      dia.cantidadCancelaciones = cancelados
      dia.ventaNeta = ventaNeta
      dia.ingresoBruto = ingresoBruto
      dia.devoluciones = devoluciones
      dia.ingresoNeto = ingresoNeto
      dia.efectivoRecibido = efectivoRecibido
      dia.efectivoExternos = efectivoExternos
      dia.efectivoDevoluciones = efectivoDevoluciones
      dia.efectivoNeto = efectivoNeto
      dia.dolaresRecibidos = dolaresRecibidos
      dia.dolaresDevoluciones = dolaresDevoluciones
      dia.facturaInicial = facturaInicial
      dia.facturaFinal = facturaFinal
      dia = cierreDiarioRepository.save( dia )
      log.debug( "se registro el cierre diario: ${dia?.dump()}" )
      return dia
    } else {
      log.warn( "no se cargaron los datos, el dia no tiene estado abierto" )
    }
    return null
  }

  private String generarFichero( String ubicacion, String nombreFichero, String plantilla, Map<String, Object> datos ) {
    Assert.isTrue( StringUtils.isNotBlank( ubicacion ), "No se ha especificado una Ubicacion para el fichero" )
    Assert.isTrue( StringUtils.isNotBlank( nombreFichero ), "No se ha especificado un Nombre para el fichero" )
    Assert.isTrue( StringUtils.isNotBlank( plantilla ), "No se ha especificado una Plantilla para el fichero" )
    Assert.notNull( datos, "No hay datos para la Plantilla" )
    try {
      String fichero = "${ ubicacion }/${ nombreFichero }"
      log.debug( "Generando Fichero: ${ fichero }" )
      log.debug( "Plantilla: ${ plantilla }" )
      File file = new File( fichero )
      if ( file.exists() ) { file.delete() } // Borramos el fichero si ya existe para crearlo de nuevo
      log.debug( 'Creando Writer' )
      FileWriter writer = new FileWriter( file )
      datos.writer = writer
      log.debug( 'Merge template' )
      VelocityEngineUtils.mergeTemplate( velocityEngine, "template/${ plantilla }.vm", "ASCII", datos, writer )
      log.debug( 'Writer close' )
      writer.close()
      return file.absolutePath
    } catch ( ex ) {
      log.error( 'Error al generar archivo', ex )
      return null
    }
  }

  private BigDecimal obtenerPrecioArticulo( String articulo, Integer sku ) {
    BigDecimal precio = BigDecimal.ZERO
    boolean tieneOferta = false
    Precio precioOferta = precioRepository.findByArticuloAndLista( articulo, 'O' )
    if ( precioOferta?.precio ) {
      precio = precioOferta.precio
      tieneOferta = true
    }
    if ( !tieneOferta ) {
      Precio precioLista = precioRepository.findByArticuloAndLista( articulo, 'L' )
      precio = precioLista?.precio
    }
    if ( precio == null ) {
      Articulo part = ServiceFactory.partMaster.obtenerArticulo( sku )
      if ( part != null ) {
        precio = part.precio
      } else {
        precio = BigDecimal.ZERO
      }
    }
    return precio
  }

  private List<NotaVenta> obtenerListaporFechaCierre( Date fechaCierre ) {

    Date fechaInicio = DateUtils.truncate( fechaCierre, Calendar.DATE )
    Date fechaFin = DateUtils.addDays( fechaInicio, 1 )
    List<NotaVenta> notasVentaTmp = notaVentaRepository.findByFechaHoraFacturaBetween( fechaInicio, fechaFin )

    return notasVentaTmp
  }

  void archivarCierre( Date pForDate ) {
    Thread th = Thread.start {
      this.archivarCierre( pForDate, true )
    }
  }

  void archivarCierre( Date pForDate, Boolean pDeleteAfter ) {
    String strDate = CustomDateUtils.format( DateUtils.truncate( pForDate, Calendar.DATE), DATE_FORMAT)
    log.debug (String.format( 'CierreDiarioService.archivarCierre( %s )', strDate) )
    ArchiveTask task = new ArchiveTask(  )
    task.baseDir = Registry.dailyClosePath
    task.archiveFile = String.format( FMT_ARCHIVE_FILENAME, Registry.currentSite, strDate )
    task.filePattern = String.format( FMT_FILE_PATTERN, strDate )
    task.run()
    sleep 20000
    if ( pDeleteAfter ) {
      long nFiles = 0
      new File( Registry.dailyClosePath ).eachFile( ) { File f ->
        if ( f.getName().contains( strDate ) ) {
          f.delete()
          nFiles ++
        }
      }
      if (nFiles > 0) {
        log.debug( String.format( 'Archivos eliminados: %,d', nFiles ) )
    }
    }
  }

  @Override
  List<CierreDiario> buscarPorFechasEntre( Date fechaInicio, Date fechaFin ){
    log.debug( "Obteniendo dias por fechas" )
    List<CierreDiario> lstDias = cierreDiarioRepository.findByFechaBetween( fechaInicio, fechaFin )
    return lstDias
  }

  @Override
  List<CierreDiario> diasCerrados(){
      log.debug( "Validando venta en dia cerrado" )
      QCierreDiario cierre = QCierreDiario.cierreDiario
      List<CierreDiario> lstDias = cierreDiarioRepository.findAll( cierre.estado.equalsIgnoreCase('c'), cierre.fecha.asc() )
      return lstDias
  }

  @Override
  @Transactional
  void cambiarEstatuCerrado( Date fecha ){
    log.debug( "cambiarEstatuCerrado( )" )
    CierreDiario cierre = cierreDiarioRepository.findOne( fecha )
    if(cierre != null && cierre.estado.trim().equalsIgnoreCase('a')){
      cierre.estado = 'c'
      cierre.fechaCierre = new Date()
      cierre.horaCierre = new Date()
      cierre.observaciones = 'CIERRE AUTOMATICO'
      cierreDiarioRepository.saveAndFlush( cierre )
    }
  }


  @Override
  @Transactional
  void closeDaysBeforeNov( ){
    if(Registry.generateMonthTransactions()){
      List<CierreDiario> lstDiasAbiertos = buscarConEstadoAbierto()
      Calendar cal = Calendar.getInstance()
      cal.set(Calendar.DAY_OF_MONTH,Calendar.getInstance().getActualMinimum(Calendar.NOVEMBER));
      for(CierreDiario dia : lstDiasAbiertos){
          if(dia.fecha.before(cal.getTime())){
              dia.setEstado( 'c' )
              dia.setFechaCierre( new Date() )
              dia.setHoraCierre( new Date() )
              dia.setObservaciones( 'CIERRE AUTOMATICO' )
              cierreDiarioRepository.saveAndFlush( dia )
              cierreDiarioRepository.flush()
          }
      }
    }
  }

  @Override
  @Transactional
  void generaIN2( Date fechaCierre ){
    if(Registry.generateMonthTransactions()){
      Calendar cal = Calendar.getInstance()
      cal.set(Calendar.DAY_OF_MONTH,Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
      Date dateStart = cal.getTime()

      cal = Calendar.getInstance()
      cal.add(Calendar.DATE,-1)
      Date current = cal.getTime()
      InventorySearch.generateInFile2Unique( dateStart, current, fechaCierre )
      Parametro p = parametroRepository.findOne( TipoParametro.GENERA_ARCHIVO_TRANSACCIONES_MENSUALES.value )
      if( p != null ){
          p.setValor( 'no' )
          parametroRepository.saveAndFlush( p )
      }
    }
  }
}

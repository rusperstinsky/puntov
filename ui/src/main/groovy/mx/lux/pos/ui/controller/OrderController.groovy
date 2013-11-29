package mx.lux.pos.ui.controller

import groovy.util.logging.Slf4j
import mx.lux.pos.ui.MainWindow
import mx.lux.pos.ui.resources.ServiceManager
import mx.lux.pos.ui.view.dialog.ManualPriceDialog
import mx.lux.pos.ui.view.panel.OrderPanel
import org.apache.commons.lang.NumberUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.swing.JOptionPane
import javax.swing.JPanel

import mx.lux.pos.model.*
import mx.lux.pos.service.*
import mx.lux.pos.ui.model.*

import java.text.DateFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import mx.lux.pos.service.business.Registry

@Slf4j
@Component
class OrderController {

  private static NotaVentaService notaVentaService
  private static DetalleNotaVentaService detalleNotaVentaService
  private static PagoService pagoService
  private static TicketService ticketService
  private static BancoService bancoService
  private static InventarioService inventarioService
  private static MonedaExtranjeraService fxService
  private static Boolean displayUsd
  private static PromotionService promotionService
  private static CancelacionService cancelacionService
  private static EmpleadoService empleadoService
  private static CierreDiarioService cierreDiarioService
  private static SucursalService sucursalService

  private static final String TAG_USD = "USD"
  private static final String TAG_TIPO_PAGO_NOTA_CREDITO = "NOT"

  @Autowired
  public OrderController(
      NotaVentaService notaVentaService,
      DetalleNotaVentaService detalleNotaVentaService,
      PagoService pagoService,
      TicketService ticketService,
      BancoService bancoService,
      InventarioService inventarioService,
      MonedaExtranjeraService monedaExtranjeraService,
      PromotionService promotionService,
      CancelacionService cancelacionService,
      EmpleadoService empleadoService,
      CierreDiarioService cierreDiarioService,
      SucursalService sucursalService
  ) {
    this.notaVentaService = notaVentaService
    this.detalleNotaVentaService = detalleNotaVentaService
    this.pagoService = pagoService
    this.ticketService = ticketService
    this.bancoService = bancoService
    this.inventarioService = inventarioService
    fxService = monedaExtranjeraService
    this.promotionService = promotionService
    this.cancelacionService = cancelacionService
    this.empleadoService = empleadoService
    this.cierreDiarioService = cierreDiarioService
    this.sucursalService = sucursalService
  }

  static Order getOrder( String orderId ) {
    log.info( "obteniendo orden id: ${orderId}" )
    NotaVenta notaVenta = notaVentaService.obtenerNotaVenta( orderId )
    Order order = Order.toOrder( notaVenta )
    if ( StringUtils.isNotBlank( order?.id ) ) {
      order.items?.clear()
      List<DetalleNotaVenta> detalles = detalleNotaVentaService.listarDetallesNotaVentaPorIdFactura( orderId )
      detalles?.each { DetalleNotaVenta tmp ->
        order.items?.add( OrderItem.toOrderItem( tmp ) )
      }
      order.payments?.clear()
      List<Pago> pagos = pagoService.listarPagosPorIdFactura( orderId )
      pagos?.each { Pago tmp ->
        Payment paymentTmp = Payment.toPaymment( tmp )
        if ( tmp?.idBancoEmisor?.integer ) {
          BancoEmisor banco = bancoService.obtenerBancoEmisor( tmp?.idBancoEmisor?.toInteger() )
          paymentTmp.issuerBank = banco?.descripcion
        }
        order.payments?.add( paymentTmp )
      }
      return order
    } else {
      log.warn( 'no se obtiene orden, notaVenta no existe' )
    }
    return null
  }

  static Order openOrder( ) {
    log.info( 'abriendo nueva orden' )
    NotaVenta notaVenta = notaVentaService.abrirNotaVenta()
    return Order.toOrder( notaVenta )
  }

  static Order addItemToOrder( String orderId, Item item ) {
    log.info( "agregando articulo id: ${item?.id} a orden id: ${orderId}" )
    if ( item?.id ) {
      orderId = ( notaVentaService.obtenerNotaVenta( orderId ) ? orderId : openOrder()?.id )
      NotaVenta nota = notaVentaService.obtenerNotaVenta( orderId )
      DetalleNotaVenta detalle = null
      if ( item.isManualPriceItem() ) {
        String rmks = nota.observacionesNv
        ManualPriceDialog dlg = ManualPriceDialog.instance
        dlg.item = item
        dlg.remarks = rmks
        dlg.activate()
        if ( dlg.itemAccepted ) {
          item.listPrice = item.price
          detalle = new DetalleNotaVenta(
              idArticulo: item.id,
              cantidadFac: 1,
              precioUnitLista: item.listPrice,
              precioUnitFinal: item.price,
              precioCalcLista: item.listPrice,
              precioFactura: item.price,
              precioCalcOferta: 0,
              precioConv: 0,
              idTipoDetalle: 'N',
              surte: 'S'
          )
          nota.observacionesNv = dlg.remarks
          notaVentaService.registrarNotaVenta( nota )
        }
      } else {
        detalle = new DetalleNotaVenta(
            idArticulo: item.id,
            cantidadFac: 1,
            precioUnitLista: item.listPrice,
            precioUnitFinal: item.price,
            precioCalcLista: item.listPrice,
            precioFactura: item.price,
            precioCalcOferta: 0,
            precioConv: 0,
            idTipoDetalle: 'N',
            surte: 'S'
        )
      }
      if ( detalle != null ) {
        nota = notaVentaService.registrarDetalleNotaVentaEnNotaVenta( orderId, detalle )
      }
      return Order.toOrder( nota )
    } else {
      log.warn( "no se agrega articulo, parametros invalidos" )
    }
    return null
  }

  static Order addOrderItemToOrder( String orderId, OrderItem orderItem ) {
    log.info( "actualizando orderItem id: ${orderItem?.item?.id} en orden id: ${orderId}" )
    if ( StringUtils.isNotBlank( orderId ) && orderItem?.item?.id ) {
      DetalleNotaVenta detalle = new DetalleNotaVenta(
          idArticulo: orderItem.item.id,
          cantidadFac: orderItem.quantity ?: 1,
          precioUnitLista: orderItem.item.listPrice,
          precioUnitFinal: orderItem.item.price,
          precioCalcLista: orderItem.item.listPrice,
          precioFactura: orderItem.item.price,
          precioCalcOferta: 0,
          precioConv: 0,
          idTipoDetalle: 'N',
          surte: 'S'
      )
      NotaVenta notaVenta = notaVentaService.registrarDetalleNotaVentaEnNotaVenta( orderId, detalle )
      return Order.toOrder( notaVenta )
    } else {
      log.warn( "no se actualiza articulo, parametros invalidos" )
    }
    return null
  }

  static Order removeOrderItemFromOrder( String orderId, OrderItem orderItem ) {
    log.info( "eliminando orderItem, articulo id: ${orderItem?.item?.id} de orden id: ${orderId}" )
    if ( StringUtils.isNotBlank( orderId ) && orderItem?.item?.id ) {
      NotaVenta notaVenta = notaVentaService.eliminarDetalleNotaVentaEnNotaVenta( orderId, orderItem.item.id )
      if ( notaVenta?.id ) {
        return Order.toOrder( notaVenta )
      } else {
        log.warn( "no se elimina orderItem, notaVenta no existe" )
      }
    } else {
      log.warn( "no se elimina orderItem, parametros invalidos" )
    }
    return null
  }

  static Order addPaymentToOrder( String orderId, Payment payment ) {
    log.info( "agregando pago monto: ${payment?.amount}, tipo: ${payment?.paymentTypeId} a orden id: ${orderId}" )
    if ( StringUtils.isNotBlank( orderId ) && StringUtils.isNotBlank( payment?.paymentTypeId ) && payment?.amount ) {

      User user = Session.get( SessionItem.USER ) as User
      println "Banco Emisor:: ${payment.issuerBankId}"
      Pago pago = new Pago(
          idFormaPago: payment.paymentTypeId,
          referenciaPago: payment.paymentReference,
          monto: payment.amount,
          idEmpleado: user?.username,
          idFPago: payment.paymentTypeId,
          clave: payment.paymentReference,
          referenciaClave: payment.codeReference,
          idBancoEmisor: payment.issuerBankId,
          idTerminal: payment.terminalId,
          idPlan: payment.planId
      )
      NotaVenta notaVenta = notaVentaService.registrarPagoEnNotaVenta( orderId, pago )
      return Order.toOrder( notaVenta )
    } else {
      log.warn( "no se agrega pago, parametros invalidos" )
    }
    return null
  }

  static Order removePaymentFromOrder( String orderId, Payment payment ) {
    log.info( "eliminando pago id: ${payment?.id}, monto: ${payment?.amount}, tipo: ${payment?.paymentTypeId}" )
    log.info( "de orden id: ${orderId}" )
    if ( StringUtils.isNotBlank( orderId ) && payment?.id ) {
        cancelacionService.restablecerMontoAlBorrarPago( payment.id )
        NotaVenta notaVenta = notaVentaService.eliminarPagoEnNotaVenta( orderId, payment.id )
      if ( notaVenta?.id ) {
        return Order.toOrder( notaVenta )
      } else {
        log.warn( "no se elimina pago, notaVenta no existe" )
      }
    } else {
      log.warn( "no se elimina pago, parametros invalidos" )
    }
    return null
  }

  static Order placeOrder( Order order ) {
    log.info( "registrando orden id: ${order?.id}, cliente: ${order?.customer?.id}" )
    if ( StringUtils.isNotBlank( order?.id ) && order?.customer?.id ) {
      NotaVenta notaVenta = notaVentaService.obtenerNotaVenta( order.id )
      if ( StringUtils.isNotBlank( notaVenta?.id ) ) {
        User user = Session.get( SessionItem.USER ) as User
        if ( StringUtils.isBlank( notaVenta.idEmpleado ) ) {
          notaVenta.idEmpleado = user?.username
        }
        if ( notaVenta.idCliente != null ) {
          notaVenta.idCliente = order.customer.id
        }
        notaVenta.observacionesNv = order.comments
        notaVenta.empEntrego = user?.username
        notaVenta.udf2 = order.country.toUpperCase()
        notaVenta = notaVentaService.cerrarNotaVenta( notaVenta )
        for(Pago pago : notaVenta.pagos){
            if( pago.idFPago.equalsIgnoreCase(TAG_TIPO_PAGO_NOTA_CREDITO)){
                Retorno retorno = pagoService.obtenerRetorno( pago.referenciaPago.trim() )
                if(retorno != null ){
                    retorno.ticketDestino = notaVenta.factura
                    pagoService.actualizarRetorno( retorno )
                }
            }
        }
        if ( inventarioService.solicitarTransaccionVenta( notaVenta ) ) {
          log.debug( "transaccion de inventario correcta" )
        } else {
          log.warn( "no se pudo procesar la transaccion de inventario" )
        }
        ServiceManager.ioServices.logSalesNotification( notaVenta.id )
        return Order.toOrder( notaVenta )
      } else {
        log.warn( "no se registra orden, notaVenta no existe" )
      }
    } else {
      log.warn( "no se registra orden, parametros invalidos" )
    }
    return null
  }

  static void printOrder( String orderId ) {
    printOrder( orderId, true )
  }

  static void printOrder( String orderId, boolean pNewOrder ) {
    log.info( "imprimiendo orden id: ${orderId}" )
    if ( StringUtils.isNotBlank( orderId ) ) {
      ticketService.imprimeVenta( orderId, pNewOrder )
    } else {
      log.warn( "no se imprime orden, parametros invalidos" )
    }
  }

  static List<Order> findLastOrders( ) {
    log.info( "obteniendo ultimas ordenes" )
    List<NotaVenta> results = notaVentaService.listarUltimasNotasVenta()
    return results?.collect { NotaVenta tmp ->
      Order.toOrder( tmp )
    }
  }

  static List<Order> findOrdersByParameters( Map<String, Object> params ) {
    log.info( "buscando ordenes por parametros: ${params}" )
    List<NotaVenta> results = notaVentaService.listarNotasVentaPorParametros( params )
    log.debug( "ordenes obtenidas: ${results*.id}" )
    return results.collect { NotaVenta tmp ->
      Order.toOrder( tmp )
    }
  }

  static Order findOrderByTicket( String ticket ) {
    log.info( "buscando orden por ticket: ${ticket}" )
    NotaVenta result = notaVentaService.obtenerNotaVentaPorTicket( ticket )
    return Order.toOrder( result )
  }

  static Double requestUsdRate( ) {
    log.info( "Request USD rate" )
    Double rate = 1.0
    MonedaDetalle fxrate = fxService.findActiveRate( TAG_USD )
    if ( fxrate != null ) {
      rate = fxrate.tipoCambio.doubleValue()
    }
    return rate
  }

  static Boolean requestUsdDisplayed( ) {
    if ( displayUsd == null ) {
      log.info( "Request USD rate" )
      displayUsd = fxService.requestUsdDisplayed()
    }
    return displayUsd
  }

  static SalesWithNoInventory requestConfigSalesWithNoInventory( ) {
    return notaVentaService.obtenerConfigParaVentasSinInventario()
  }

  static DetalleNotaVenta getDetalleNotaVenta( String idFactura, Integer idArticulo ) {
    log.debug( "getDetalleNotaVenta( String idFactura, Integer idArticulo )" )

    DetalleNotaVenta venta = detalleNotaVentaService.obtenerDetalleNotaVenta( idFactura, idArticulo )

    return venta
  }

  static Promocion getPromocion( Integer idPromocion ) {
    log.debug( "getPromocion( Integer idPromocion )" )
    Promocion promocion = promotionService.obtenerPromocion( idPromocion )
    return promocion
  }

  static void requestSaveAsQuote( Order pOrder, Customer pCustomer ) {
    Integer pQuoteId = ServiceManager.quote.copyFromOrder( pOrder.id, pCustomer.id,
        ( ( User ) Session.get( SessionItem.USER ) ).username )
    if ( pQuoteId != null ) {
      ticketService.imprimeCotizacion( pQuoteId )
      notaVentaService.eliminarNotaVenta( pOrder.id )
      String msg = String.format( 'La cotización fue registrada como: %d    ', pQuoteId )
      JOptionPane.showMessageDialog( MainWindow.instance, msg, 'Cotización', JOptionPane.INFORMATION_MESSAGE )
    }
  }

  static String requestOrderFromQuote( JPanel pComponent ) {
    String orderNbr = null
    String confirm = JOptionPane.showInputDialog( MainWindow.instance, OrderPanel.MSG_INPUT_QUOTE_ID,
        OrderPanel.TXT_QUOTE_TITLE, JOptionPane.QUESTION_MESSAGE )
    if ( StringUtils.trimToNull( confirm ) != null ) {
      Integer quoteNbr = NumberUtils.createInteger( StringUtils.trimToEmpty( confirm ) )
      if ( quoteNbr != null ) {
        Map<String, Object> result = ServiceManager.quote.toOrder( quoteNbr )
        if ( result != null ) {
          orderNbr = StringUtils.trimToNull( ( String ) result.get( 'orderNbr' ) )
        }
        if ( orderNbr == null ) {
          JOptionPane.showMessageDialog( MainWindow.instance, ( String ) result.get( 'statusMessage' ),
              OrderPanel.TXT_QUOTE_TITLE, JOptionPane.ERROR_MESSAGE )
        }
      }
    }
    return orderNbr
  }

  static String requestEmployee( String pOrderId ) {
    String empName = ''
    if ( StringUtils.trimToNull( StringUtils.trimToEmpty(pOrderId) ) != null ) {
      Empleado employee = notaVentaService.obtenerEmpleadoDeNotaVenta( pOrderId )
      if ( employee != null ) {
        if ( ( ( User ) Session.get( SessionItem.USER ) ).equals( employee ) ) {
          empName = ( ( User ) Session.get( SessionItem.USER ) ).toString()
        } else {
          empName = User.toUser( employee ).toString()
        }
      }
    }
    return empName
  }

  static void saveCustomerForOrder( String pOrderNbr, Integer pCustomerId ) {
    if ( StringUtils.isNotBlank( pOrderNbr ) ) {
      NotaVenta order = notaVentaService.obtenerNotaVenta( pOrderNbr )
      if ( order != null ) {
        order.idCliente = pCustomerId
        notaVentaService.saveOrder( order )
      }
    }
  }

  static Customer getCustomerFromOrder( String pOrderNbr ) {
    Customer cust = null
    if ( StringUtils.trimToNull( pOrderNbr ) != null ) {
      NotaVenta order = notaVentaService.obtenerNotaVenta( pOrderNbr )
      if ( order != null ) {
        cust = Customer.toCustomer( order.cliente )
      }
    }
    return cust
  }

  static boolean validaDatos( String factura, String vendedor ){
      log.debug( "Cambiando vendedor de factura $factura" )
      Boolean cambioValido = false
      NotaVenta notaVenta = notaVentaService.obtenerNotaVentaPorFactura( factura.trim() )
      Empleado empleado = empleadoService.obtenerEmpleado( vendedor.trim() )
      if( empleado != null && notaVenta != null ){
          cambioValido = true
      }
      return cambioValido
  }

  static boolean validaDiaAbierto( String factura, String vendedor ){
      Boolean diaAbierto = false
      NotaVenta notaVenta = notaVentaService.obtenerNotaVentaPorFactura( factura.trim() )
      Empleado empleado = empleadoService.obtenerEmpleado( vendedor.trim() )
      CierreDiario diaCerrado = cierreDiarioService.buscarPorFecha( notaVenta.fechaHoraFactura )
      if( diaCerrado.estado.equalsIgnoreCase('a')){
          diaAbierto = true
      }
      return diaAbierto
  }

  static boolean cambiaVendedor( String factura, String vendedor, String observaciones ){
      log.debug( "Cambiando factura $factura" )
      try{
          User user = Session.get( SessionItem.USER ) as User
          Empleado employee = empleadoService.obtenerEmpleado( user.username )
          NotaVenta notaVenta = notaVentaService.obtenerNotaVentaPorFactura( factura.trim() )
          String idEmpleadoOrig = notaVenta.idEmpleado
          String idEmpleadoFinal = vendedor.trim()
          notaVenta.idEmpleado = vendedor.trim()
          notaVentaService.saveOrder( notaVenta )

          Modificacion mod = new Modificacion()
          mod.idFactura = notaVenta.id
          mod.tipo = 'emp'
          mod.fecha = new Date()
          mod.idEmpleado = employee.id
          mod.causa = ''
          mod.observaciones = observaciones
          Modificacion modificacion = cancelacionService.registrarCambiodeEmpleado( mod, idEmpleadoOrig, idEmpleadoFinal )
          if( modificacion != null ){
              return true
          } else {
              return false
          }
      }catch (Exception e){
          println e
          return false
      }
  }


  static BigDecimal obtenerNotaCredito( String folio ){
      Retorno retorno = pagoService.obtenerRetorno( folio.trim() )
      BigDecimal monto = BigDecimal.ZERO
      if( retorno != null){
          monto = retorno.monto
      }
      return monto
  }


  static Boolean validDate(){
      String fechaLogeo = sucursalService.obtenerParametroFecha()
      DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )
      return df.format(new Date()).equalsIgnoreCase(StringUtils.trimToEmpty(fechaLogeo.trim()))
  }

  static Boolean validateCloseDate(){
      Boolean diaValido = false
      DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )
      String fechaLogeo = sucursalService.obtenerParametroFecha()
      List<CierreDiario> lstCierre = cierreDiarioService.diasCerrados()
      CierreDiario ultimoCierre = lstCierre.last()
      if( new Date().compareTo(ultimoCierre.fecha) >= 0 ){
          diaValido = true
      }
      return diaValido
  }


  static Promotion findPromotionalArticle( String idPromotion ){
      log.debug( "find article from id ${idPromotion}" )
      Integer idPromocion = 0
      try{
          idPromocion = NumberFormat.getInstance().parse(idPromotion).intValue()
      } catch(ParseException e){}
      Promocion promocion = promotionService.obtenerPromocion( idPromocion )
      if( StringUtils.trimToEmpty(promocion.articuloProm) != '' ){
          return Promotion.toPromotion( promocion )
      } else {
          return null
      }
  }

  static Boolean isPromotionalArticleAutomatic(){
      return promotionService.esArticuloPromocionalAutomatico()
  }

  static String findArticlesOfGroupPromotion( Integer idGroup ){
      return promotionService.articulosGupoPromocion( idGroup )
  }

  static String obtenerEmpleadoPorNotaVenta( String factura ){
      String empleado = ''
      NotaVenta nota = notaVentaService.obtenerNotaVentaPorFactura( factura )
      if( nota != null ){
          empleado = String.format( '[%s]%s',nota.empleado.id.trim(), nota.empleado.nombreCompleto.trim() )
      } else {
          empleado = 'No existe ticket'
      }
      return empleado
  }


  static BigDecimal maximumDollars( ){
    log.debug( 'maximumDollars( )' )
    BigDecimal montoLimite = BigDecimal.ZERO
    try{
      montoLimite = NumberFormat.getInstance().parse( Registry.maximumDollars.trim() )
    } catch( Exception e ){}
    return montoLimite
  }

}

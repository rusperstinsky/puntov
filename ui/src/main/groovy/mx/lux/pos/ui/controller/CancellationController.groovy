package mx.lux.pos.ui.controller

import groovy.util.logging.Slf4j
import mx.lux.pos.service.CancelacionService
import mx.lux.pos.service.TicketService
import mx.lux.pos.ui.resources.ServiceManager
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import mx.lux.pos.model.*
import mx.lux.pos.ui.model.*

@Slf4j
@Component
class CancellationController {

  private static CancelacionService cancelacionService
  private static TicketService ticketService

  @Autowired CancellationController( CancelacionService cancelacionService, TicketService ticketService ) {
    this.cancelacionService = cancelacionService
    this.ticketService = ticketService
  }

  static List<String> findAllCancellationReasons( ) {
    log.info( 'obteniendo lista de causas de cancelacion' )
    List<CausaCancelacion> results = cancelacionService.listarCausasCancelacion()
    return results?.collect { CausaCancelacion causa ->
      causa.descripcion
    }
  }

  static boolean allowLateCancellation( String orderId ) {
    log.info( 'solicitando autorizacion para cancelacion posterior a compra' )
    if ( StringUtils.isNotBlank( orderId ) ) {
      return cancelacionService.permitirCancelacionExtemporanea( orderId )
    }
    log.warn( 'no se obtiene autorizacion, parametro nulo o vacio' )
    return false
  }

  static boolean cancelOrder( String orderId, String reason, String comments ) {
    log.info( "solicitando cancelacion de orden id: ${orderId}, causa: ${reason}" )
    if ( StringUtils.isNotBlank( orderId ) && StringUtils.isNotBlank( reason ) ) {
      User user = Session.get( SessionItem.USER ) as User
      Modificacion modificacion = new Modificacion(
          idEmpleado: user?.username,
          causa: reason,
          observaciones: comments
      )
      modificacion = cancelacionService.registrarCancelacionDeNotaVenta( orderId, modificacion )
      if ( modificacion?.id ) {
        log.debug( "modificacion de cancelacion registrada id: ${modificacion.id}" )
        ServiceManager.ioServices.logAdjustmentNotification( modificacion.id )
        return true
      } else {
        log.warn( 'error, no se registra cancelacion' )
      }
    } else {
      log.warn( 'no se registra cancelacion, parametros invalidos' )
    }
    return false
  }

  static List<Refund> findRefundsByOrderId( String orderId ) {
    log.info( "obteniendo devoluciones por orden id: ${orderId}" )
    List<Devolucion> results = cancelacionService.listarDevolucionesDeNotaVenta( orderId )
    if ( results?.any() ) {
      return results.collect { Devolucion devolucion ->
        Refund.toRefund( devolucion )
      }
    }
    return [ ]
  }

  static boolean refundPaymentsCreditFromOrder( String orderId, Map<Integer, String> creditRefunds ) {
    log.info( "solicitando registrar devoluciones: ${creditRefunds} de orden id: ${orderId}" )
    if ( StringUtils.isNotBlank( orderId ) ) {
      creditRefunds?.each { Integer pagoId, String valor ->
        if ( StringUtils.isBlank( valor ) ) {
          creditRefunds.remove( pagoId )
        }
      }
      List<Devolucion> results = cancelacionService.registrarDevolucionesDeNotaVenta( orderId, creditRefunds )
      if ( results?.any() ) {
        log.debug( "devoluciones registradas obtenidas: ${results*.id}" )
        return true
      }
    }
    return false
  }

  static boolean transferPaymentsCreditToOrder( String fromOrderId, String toOrderId, Map<Integer, BigDecimal> creditTransfers ) {
    log.info( "solicitando registrar transferencias: ${creditTransfers} de orden id: ${fromOrderId} a orden id: ${toOrderId}" )
    if ( StringUtils.isNotBlank( fromOrderId ) && StringUtils.isNotBlank( toOrderId ) ) {
        Map<Integer, BigDecimal> creditTransfers2 = new HashMap<Integer, BigDecimal>()
        creditTransfers2.putAll( creditTransfers )
        creditTransfers2?.each { Integer pagoId, BigDecimal valor ->
        if ( !valor ) {
          creditTransfers.remove( pagoId )
        }
      }
      List<Pago> results = cancelacionService.registrarTransferenciasParaNotaVenta( fromOrderId, toOrderId, creditTransfers )
      if ( results?.any() ) {
        log.debug( "transferencias registradas obtenidas: ${results*.id}" )
        return true
      }
    }
    return false
  }

  static boolean orderHasTransfers( String orderId ) {
    log.info( "verificando transferencias para la orden id: ${orderId}" )
    if ( StringUtils.isNotBlank( orderId ) ) {
      List<NotaVenta> results = cancelacionService.listarNotasVentaOrigenDeNotaVenta( orderId )
      return results?.any()
    } else {
      log.warn( 'no se verifican transferencias para la orden, parametros invalidos' )
    }
    return false
  }

  static List<String> findSourceOrdersWithCredit( String orderId ) {
    log.info( "obteniendo ordenes origen con credito a partir de la orden id: ${orderId}" )
    if ( StringUtils.isNotBlank( orderId ) ) {
      List<NotaVenta> results = cancelacionService.listarNotasVentaOrigenDeNotaVenta( orderId )
      List<String> sources = [ ]
      results?.each { NotaVenta tmp ->
        BigDecimal credit = cancelacionService.obtenerCreditoDeNotaVenta( tmp?.id )
        if ( credit != null && credit >= 1 ) {
          sources.add( tmp?.id )
        }
      }
      return sources.any() ? sources : [ ]
    } else {
      log.warn( 'no se obtenienen ordenes origen con credito a partir de la orden, parametros invalidos' )
    }
    return [ ]
  }

  static void printCancellationPlan( String orderId ) {
    log.info( "imprimiendo plan de cancelacion de orden id: ${orderId}" )
    if ( StringUtils.isNotBlank( orderId ) ) {
      ticketService.imprimePlanCancelacion( orderId )
    } else {
      log.warn( 'no se imprime plan de cancelacion de orden, parametros invalidos' )
    }
  }

  static void printOrderCancellation( String orderId ) {
    log.info( "imprimiendo cancelacion de orden id: ${orderId}" )
    if ( StringUtils.isNotBlank( orderId ) ) {
      ticketService.imprimeCancelacion( orderId )
    } else {
      log.warn( 'no se imprime cancelacion de orden, parametros invalidos' )
    }
  }

  static void printCancellationsFromOrder( String orderId ) {
    log.info( "imprimiendo cancelaciones a partir de orden id: ${orderId}" )
    if ( StringUtils.isNotBlank( orderId ) ) {
      List<NotaVenta> results = cancelacionService.listarNotasVentaOrigenDeNotaVenta( orderId )
      results?.each { NotaVenta tmp ->
        ticketService.imprimeCancelacion( tmp?.id )
      }
    } else {
      log.warn( 'no se imprimen cancelaciones a partir de orden, parametros invalidos' )
    }
  }

  static void refreshOrder( Order order ) {
    Order newOrder = OrderController.getOrder( order.id )
    //Order newOrder = order
    order.id = newOrder.id
    order.bill = newOrder.bill
    order.comments = newOrder.comments
    order.status = newOrder.status
    order.date = newOrder.date
    order.branch = newOrder.branch
    order.customer = newOrder.customer
    order.items = newOrder.items
    order.payments = newOrder.payments
    order.deals = newOrder.deals
    order.total = newOrder.total
    order.paid = newOrder.paid
    order.due = newOrder.due
  }


    static boolean validateTransfer( String idOrder ) {
        log.info( "Validadndo transferencia" )
        boolean transfValida = cancelacionService.validandoTransferencia( idOrder )
        return transfValida
    }

    static void resetValuesofCancellation( String idOrder ) {
        log.info( "restableciendo valores de cancelacion con id: ${idOrder}" )
            cancelacionService.restablecerValoresDeCancelacion( idOrder )
    }

  static List<Order> findOrderToResetValues( String idOrder ){
    log.info( "obteniendo notaventa para restablecer los montos de transferencia" )
    List<NotaVenta> lstNotasVentas = cancelacionService.listarNotasVentaOrigenDeNotaVenta( idOrder )
    return lstNotasVentas?.collect { NotaVenta tmp ->
      Order.toOrder( tmp )
    }
  }


  static String findCancellationReasonById( Integer id ) {
      log.info( 'obteniendo lista de causas de cancelacion' )
      CausaCancelacion result = cancelacionService.causaCancelacion( id )
      return result.descripcion
  }
}

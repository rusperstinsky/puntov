package mx.lux.pos.ui.controller

import mx.lux.pos.service.CancelacionService
import mx.lux.pos.service.TicketService
import mx.lux.pos.ui.model.Refund
import mx.lux.pos.ui.model.Session
import mx.lux.pos.ui.model.SessionItem
import mx.lux.pos.ui.model.User
import spock.lang.Specification
import mx.lux.pos.model.*

import static mx.lux.pos.ui.assertions.Assert.assertRefundEquals

class CancellationControllerTest extends Specification {

  private CancellationController controller
  private CancelacionService cancelacionService
  private TicketService ticketService

  def setup( ) {
    cancelacionService = Mock( CancelacionService )
    ticketService = Mock( TicketService )
    controller = new CancellationController(
        cancelacionService,
        ticketService
    )
  }

  def 'obtiene lista vacia al buscar todas las causas de cancelacion'( ) {
    when:
    def actual = controller.findAllCancellationReasons()

    then:
    1 * cancelacionService.listarCausasCancelacion() >> [ ]

    then:
    0 * _

    then:
    actual == [ ]
  }

  def 'obtiene lista de causas de cancelacion al buscar todas las causas de cancelacion'( ) {
    given:
    def expected = [
        'DESCUENTO POSTERIOR',
        'POR FALTA DE ANTICIPO',
        'CASOS ESPECIALES SOLO CON AUTORIZACION',
        'TRABAJO DE PINO NO AUTORIZADO',
        'CAMBIO DE OPINION Y REALIZA OTRA COMPRA',
        'CLIENTE NO ESTA SATISFECHO CON EL TRABAJO'
    ]
    def causas = [
        new CausaCancelacion(
            id: 1,
            descripcion: 'DESCUENTO POSTERIOR'
        ),
        new CausaCancelacion(
            id: 2,
            descripcion: 'POR FALTA DE ANTICIPO'
        ),
        new CausaCancelacion(
            id: 3,
            descripcion: 'CASOS ESPECIALES SOLO CON AUTORIZACION'
        ),
        new CausaCancelacion(
            id: 4,
            descripcion: 'TRABAJO DE PINO NO AUTORIZADO'
        ),
        new CausaCancelacion(
            id: 5,
            descripcion: 'CAMBIO DE OPINION Y REALIZA OTRA COMPRA'
        ),
        new CausaCancelacion(
            id: 6,
            descripcion: 'CLIENTE NO ESTA SATISFECHO CON EL TRABAJO'
        )
    ]

    when:
    def actual = controller.findAllCancellationReasons()

    then:
    1 * cancelacionService.listarCausasCancelacion() >> causas

    then:
    0 * _

    then:
    actual == expected
  }

  def 'obtiene false al solicitar autorizacion para cancelacion posterior a compra con ordenId nulo o vacio'( ) {
    when:
    def actual = controller.allowLateCancellation( orderId )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | orderId
    false    | null
    false    | ' '
  }

  def 'obtiene true al solicitar autorizacion para cancelacion posterior a compra con orderId invalida'( ) {
    given:
    String orderId = 'X'

    when:
    def actual = controller.allowLateCancellation( orderId )

    then:
    1 * cancelacionService.permitirCancelacionExtemporanea( orderId ) >> false

    then:
    0 * _

    then:
    !actual
  }

  def 'obtiene true al solicitar autorizacion para cancelacion posterior a compra con orderId valida'( ) {
    given:
    String orderId = 'A'

    when:
    def actual = controller.allowLateCancellation( orderId )

    then:
    1 * cancelacionService.permitirCancelacionExtemporanea( orderId ) >> true

    then:
    0 * _

    then:
    actual
  }

  def 'obtiene false al cancelar compra con ordenId y/o razon nulos o vacios'( ) {
    when:
    def actual = controller.cancelOrder( orderId, reason, comments )

    then:
    0 * _

    then:
    !actual

    where:
    orderId | reason | comments
    null    | null   | null
    ' '     | null   | null
    ' '     | ' '    | null
    ' '     | ' '    | ' '
  }

  def 'obtiene false al cancelar compra con modificacion invalida'( ) {
    given:
    String orderId = 'X'
    String reason = 'text'

    when:
    def actual = controller.cancelOrder( orderId, reason, '' )

    then:
    1 * cancelacionService.registrarCancelacionDeNotaVenta( orderId, _ ) >> null

    then:
    0 * _

    then:
    !actual
  }

  def 'obtiene true al cancelar compra con modificacion valida y usuario invalido'( ) {
    given:
    String orderId = 'X'
    String reason = 'text'
    String comments = 'text'
    Session.put( SessionItem.USER, null )
    def modificacion = new Modificacion(
        id: 1,
        idFactura: orderId,
        idEmpleado: null,
        causa: reason,
        observaciones: comments
    )

    when:
    def actual = controller.cancelOrder( orderId, reason, comments )

    then:
    1 * cancelacionService.registrarCancelacionDeNotaVenta( orderId, { Modificacion tmp ->
      assert tmp.idEmpleado == modificacion.idEmpleado
      assert tmp.causa == modificacion.causa
      assert tmp.observaciones == modificacion.observaciones
      return tmp
    } as Modificacion ) >> modificacion

    then:
    0 * _

    then:
    actual
  }

  def 'obtiene true al cancelar compra con modificacion valida y usuario valido'( ) {
    given:
    String orderId = 'X'
    String reason = 'text'
    String comments = 'text'
    Session.put( SessionItem.USER, new User( username: '0' ) )
    def modificacion = new Modificacion(
        id: 1,
        idFactura: orderId,
        idEmpleado: '0',
        causa: reason,
        observaciones: comments
    )

    when:
    def actual = controller.cancelOrder( orderId, reason, comments )

    then:
    1 * cancelacionService.registrarCancelacionDeNotaVenta( orderId, { Modificacion tmp ->
      assert tmp.idEmpleado == modificacion.idEmpleado
      assert tmp.causa == modificacion.causa
      assert tmp.observaciones == modificacion.observaciones
      return tmp
    } as Modificacion ) >> modificacion

    then:
    0 * _

    then:
    actual
  }

  def 'obtiene lista vacia al buscar devoluciones por orderId con orderId invalido'( ) {
    when:
    def actual = controller.findRefundsByOrderId( orderId )

    then:
    1 * cancelacionService.listarDevolucionesDeNotaVenta( orderId ) >> results

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | results  | orderId
    [ ]      | null     | null
    [ ]      | [ ]      | null
    [ ]      | [ null ] | null
  }

  def 'obtiene lista de refunds al buscar devoluciones por orderId con orderId valido'( ) {
    given:
    String orderId = 'A'
    List<Devolucion> results = [
        new Devolucion(
            id: 1,
            idMod: 1,
            idPago: 1,
            idFormaPago: 'EF',
            monto: 10
        ),
        new Devolucion(
            id: 2,
            idMod: 1,
            idPago: 1,
            idFormaPago: 'EF',
            monto: 10
        ),
        new Devolucion(
            id: 3,
            idMod: 2,
            idPago: 2,
            idFormaPago: 'EF',
            monto: 10
        ),
        new Devolucion(
            id: 4,
            idMod: 2,
            idPago: 2,
            idFormaPago: 'EF',
            monto: 10
        )
    ]

    when:
    def actual = controller.findRefundsByOrderId( orderId )

    then:
    1 * cancelacionService.listarDevolucionesDeNotaVenta( orderId ) >> results

    then:
    0 * _

    then:
    actual.eachWithIndex { it, idx ->
      Refund expected = Refund.toRefund( results.get( idx ) )
      assertRefundEquals( expected, it )
    }
  }

  def 'obtiene false al solicitar registrar devoluciones con orderId nulo o vacio'( ) {
    when:
    def actual = controller.refundPaymentsCreditFromOrder( orderId, null )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | orderId
    false    | null
    false    | ' '
  }

  def 'obtiene false al solicitar registrar devoluciones con resultados nulos o vacios'( ) {
    given:
    def orderId = 'A'

    when:
    def actual = controller.refundPaymentsCreditFromOrder( orderId, creditRefunds )

    then:
    1 * cancelacionService.registrarDevolucionesDeNotaVenta( orderId, creditRefunds ) >> results

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | results  | creditRefunds
    false    | null     | null
    false    | null     | [ : ]
    false    | [ ]      | [ : ]
    false    | [ null ] | [ : ]
  }

  def 'obtiene true al solicitar registrar devoluciones con resultados validos'( ) {
    given:
    def orderId = 'A'
    def creditRefunds = [
        ( 1 ): 'text',
        ( 2 ): 'text'
    ]
    def results = [
        new Devolucion(
            id: 1,
            idMod: 1,
            idPago: 1,
            idFormaPago: 'EF',
            monto: 10
        ),
        new Devolucion(
            id: 2,
            idMod: 1,
            idPago: 1,
            idFormaPago: 'EF',
            monto: 10
        )
    ]

    when:
    def actual = controller.refundPaymentsCreditFromOrder( orderId, creditRefunds )

    then:
    1 * cancelacionService.registrarDevolucionesDeNotaVenta( orderId, creditRefunds ) >> results

    then:
    0 * _

    then:
    actual
  }

  def 'obtiene false al solicitar registrar transferencias con orderIds nulos o vacios'( ) {
    when:
    def actual = controller.transferPaymentsCreditToOrder( fromOrderId, toOrderId, null )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | fromOrderId | toOrderId
    false    | null        | null
    false    | ' '         | null
    false    | null        | ' '
    false    | ' '         | ' '
  }

  def 'obtiene false al solicitar registrar transferencias con resultados nulos o vacios'( ) {
    given:
    def fromOrderId = 'A'
    def toOrderId = 'B'

    when:
    def actual = controller.transferPaymentsCreditToOrder( fromOrderId, toOrderId, creditTransfers )

    then:
    1 * cancelacionService.registrarTransferenciasParaNotaVenta( fromOrderId, toOrderId, creditTransfers ) >> results

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | results  | creditTransfers
    false    | null     | null
    false    | null     | [ : ]
    false    | [ ]      | [ : ]
    false    | [ null ] | [ : ]
    false    | [ null ] | [ : ]
  }

  def 'obtiene true al solicitar registrar transferencias con resultados validos'( ) {
    given:
    def fromOrderId = 'A'
    def toOrderId = 'B'
    def creditTransfers = [
        ( 1 ): 1000,
        ( 2 ): 2000
    ]
    def results = [
        new Pago(
            id: 1,
            monto: 1000
        ),
        new Pago(
            id: 2,
            monto: 2000
        )
    ]

    when:
    def actual = controller.transferPaymentsCreditToOrder( fromOrderId, toOrderId, creditTransfers )

    then:
    1 * cancelacionService.registrarTransferenciasParaNotaVenta( fromOrderId, toOrderId, creditTransfers ) >> results

    then:
    0 * _

    then:
    actual
  }

  def 'obtiene false al verificar transferencias para orden con parametros invalidos'( ) {
    when:
    def actual = controller.orderHasTransfers( orderId )

    then:
    0 * _

    then:
    !actual

    where:
    orderId << [ null, ' ' ]
  }

  def 'obtiene false al verificar transferencias para orden con orden sin transferencias'( ) {
    given:
    def orderId = 'A'

    when:
    def actual = controller.orderHasTransfers( orderId )

    then:
    1 * cancelacionService.listarNotasVentaOrigenDeNotaVenta( orderId ) >> results

    then:
    0 * _

    then:
    !actual

    where:
    results << [ null, [ ], [ null ] ]
  }

  def 'obtiene true al verificar transferencias para la orden'( ) {
    given:
    def orderId = 'A'

    when:
    def actual = controller.orderHasTransfers( orderId )

    then:
    1 * cancelacionService.listarNotasVentaOrigenDeNotaVenta( orderId ) >> [ new NotaVenta() ]

    then:
    0 * _

    then:
    actual
  }

  def 'obtiene lista vacia al verificar credito de ordenes origen con parametros invalidos'( ) {
    when:
    def actual = controller.findSourceOrdersWithCredit( orderId )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | orderId
    [ ]      | null
    [ ]      | ' '
  }

  def 'obtiene lista vacia al verificar credito de ordenes origen sin ordenes origen'( ) {
    given:
    def orderId = 'A'

    when:
    def actual = controller.findSourceOrdersWithCredit( orderId )

    then:
    1 * cancelacionService.listarNotasVentaOrigenDeNotaVenta( orderId ) >> results
    _ * cancelacionService.obtenerCreditoDeNotaVenta( _ )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | results
    [ ]      | null
    [ ]      | [ ]
    [ ]      | [ null ]
  }

  def 'obtiene lista vacia al verificar credito de ordenes origen con ordenes sin credito'( ) {
    given:
    def orderId = 'Z'
    def sourceOrderId1 = 'A'
    def sourceOrderId2 = 'B'
    def results = [
        new NotaVenta( id: sourceOrderId1 ),
        new NotaVenta( id: sourceOrderId2 )
    ]

    when:
    def actual = controller.findSourceOrdersWithCredit( orderId )

    then:
    1 * cancelacionService.listarNotasVentaOrigenDeNotaVenta( orderId ) >> results
    1 * cancelacionService.obtenerCreditoDeNotaVenta( sourceOrderId1 ) >> 0.9
    1 * cancelacionService.obtenerCreditoDeNotaVenta( sourceOrderId2 ) >> 0.05

    then:
    0 * _

    then:
    actual == [ ]
  }

  def 'obtiene lista de ordenes al verificar credito de ordenes origen'( ) {
    given:
    def orderId = 'Z'
    def sourceOrderId1 = 'A'
    def sourceOrderId2 = 'B'
    def results = [
        new NotaVenta( id: sourceOrderId1 ),
        new NotaVenta( id: sourceOrderId2 )
    ]

    when:
    def actual = controller.findSourceOrdersWithCredit( orderId )

    then:
    1 * cancelacionService.listarNotasVentaOrigenDeNotaVenta( orderId ) >> results
    1 * cancelacionService.obtenerCreditoDeNotaVenta( sourceOrderId1 ) >> 1
    1 * cancelacionService.obtenerCreditoDeNotaVenta( sourceOrderId2 ) >> 0.05

    then:
    0 * _

    then:
    actual == [ sourceOrderId1 ]
  }
}

package mx.lux.pos.ui.controller

import mx.lux.pos.model.Pago
import mx.lux.pos.model.TipoPago
import mx.lux.pos.ui.model.Payment
import mx.lux.pos.ui.model.PaymentType
import spock.lang.Shared
import spock.lang.Specification
import mx.lux.pos.service.*

import static mx.lux.pos.ui.assertions.Assert.assertPaymentEquals

class PaymentControllerTest extends Specification {

  private PaymentController controller
  private PagoService pagoService
  private TipoPagoService tipoPagoService
  private BancoService bancoService
  private TerminalService terminalService
  private PlanService planService
  @Shared def paymentTypes = [
      new PaymentType(
          id: 'EF',
          description: 'EFECTIVO',
          soi: 'EF'
      ),
      new PaymentType(
          id: 'TC',
          description: 'TARJETA CREDITO',
          soi: 'TC',
          f1: 'text',
          f2: 'text',
          f3: 'text',
          f4: 'text',
          f5: 'text'
      ),
  ]
  @Shared def tiposPago = [
      new TipoPago(
          id: 'EF',
          descripcion: 'EFECTIVO',
          tipoSoi: 'EF'
      ),
      new TipoPago(
          id: 'TC',
          descripcion: 'TARJETA CREDITO',
          tipoSoi: 'TC',
          f1: 'text',
          f2: 'text',
          f3: 'text',
          f4: 'text',
          f5: 'text'
      )
  ]

  def setup( ) {
    pagoService = Mock( PagoService )
    tipoPagoService = Mock( TipoPagoService )
    bancoService = Mock( BancoService )
    terminalService = Mock( TerminalService )
    planService = Mock( PlanService )
    controller = new PaymentController(
        pagoService,
        tipoPagoService,
        bancoService,
        terminalService,
        planService
    )
  }

  def 'regresa lista al solicitar formas de pago activas'( ) {
    given:
    def expected = [ paymentTypes[ 0 ], paymentTypes[ 1 ] ]
    def results = [ tiposPago[ 0 ], tiposPago[ 1 ] ]

    when:
    def actual = controller.findActivePaymentTypes()

    then:
    1 * tipoPagoService.listarTiposPagoActivos() >> results
    actual == expected
  }

  def 'obtiene lista vacia al buscar pagos por orderId con orderId invalido'( ) {
    when:
    def actual = controller.findPaymentsByOrderId( orderId )

    then:
    1 * pagoService.listarPagosPorIdFactura( orderId ) >> results

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

  def 'obtiene lista de payments al buscar pagos por orderId con orderId valido'( ) {
    given:
    String orderId = 'A'
    Date fecha = new Date()
    String idEmpleado = '0'
    def results = [
        new Pago(
            id: 1,
            idFactura: orderId,
            idFormaPago: 'EF',
            idFPago: 'EF',
            monto: 10,
            fecha: fecha,
            idEmpleado: idEmpleado
        ),
        new Pago(
            id: 2,
            idFactura: orderId,
            idFormaPago: 'TC',
            idFPago: 'TC',
            monto: 20,
            fecha: fecha,
            idEmpleado: idEmpleado
        )
    ]

    when:
    def actual = controller.findPaymentsByOrderId( orderId )

    then:
    1 * pagoService.listarPagosPorIdFactura( orderId ) >> results

    then:
    0 * _

    then:
    actual.eachWithIndex { it, idx ->
      Payment expected = Payment.toPaymment( results.get( idx ) )
      assertPaymentEquals( expected, it )
    }
  }
}

package mx.lux.pos.service.impl

import mx.lux.pos.model.Pago
import mx.lux.pos.repository.PagoRepository
import spock.lang.Specification

import static mx.lux.pos.assertions.Assert.assertPagoEquals

class PagoServiceImplTest extends Specification {

  private PagoServiceImpl service
  private PagoRepository pagoRepository

  def setup( ) {
    pagoRepository = Mock( PagoRepository )
    service = new PagoServiceImpl(
        pagoRepository: pagoRepository
    )
  }

  def 'obtiene lista vacia al listar pagos por idFactura con idFactura nulo o vacio'( ) {
    when:
    def actual = service.listarPagosPorIdFactura( idFactura )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | idFactura
    [ ]      | null
    [ ]      | ' '
  }

  def 'obtiene lista vacia al listar pagos por idFactura con idFactura invalido'( ) {
    given:
    String idFactura = 'A'

    when:
    def actual = service.listarPagosPorIdFactura( idFactura )

    then:
    1 * pagoRepository.findByIdFacturaOrderByFechaAsc( idFactura )

    then:
    0 * _

    then:
    actual == [ ]
  }

  def 'obtiene lista vacia al listar pagos por idFactura con idFactura valido'( ) {
    given:
    String idFactura = 'A'
    Date fecha = new Date()
    String idEmpleado = '0'
    def pagos = [
        new Pago(
            id: 1,
            idFactura: idFactura,
            idFormaPago: 'EF',
            idFPago: 'EF',
            monto: 10,
            fecha: fecha,
            idEmpleado: idEmpleado
        ),
        new Pago(
            id: 2,
            idFactura: idFactura,
            idFormaPago: 'TC',
            idFPago: 'TC',
            monto: 20,
            fecha: fecha,
            idEmpleado: idEmpleado
        )
    ]

    when:
    def actual = service.listarPagosPorIdFactura( idFactura )

    then:
    1 * pagoRepository.findByIdFacturaOrderByFechaAsc( idFactura ) >> pagos

    then:
    0 * _

    then:
    actual.eachWithIndex { it, idx ->
      assertPagoEquals( pagos.get( idx ), it )
    }
  }
}

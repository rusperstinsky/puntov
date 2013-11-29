package mx.lux.pos.ui.controller

import mx.lux.pos.model.Contribuyente
import mx.lux.pos.service.ContribuyenteService
import mx.lux.pos.ui.model.Taxpayer
import spock.lang.Specification

import static mx.lux.pos.ui.assertions.Assert.assertTaxpayerEquals

class TaxpayerControllerTest extends Specification {

  private TaxpayerController controller
  private ContribuyenteService contribuyenteService

  def setup( ) {
    contribuyenteService = Mock( ContribuyenteService )
    controller = new TaxpayerController( contribuyenteService )
  }

  def 'es null al obtener taxpayer por rfc con rfc invalido'( ) {
    when:
    def actual = controller.findTaxpayerByRfc( rfc )

    then:
    1 * contribuyenteService.obtenerContribuyentePorRfc( rfc ) >> result

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | result                     | rfc
    null     | null                       | null
    null     | null                       | ' '
    null     | null                       | '-'
    null     | null                       | 'XEXX'
    null     | new Contribuyente()        | 'XEXX'
    null     | new Contribuyente( id: 0 ) | 'XEXX'
  }

  def 'es taxpayer al obtener taxpayer por rfc'( ) {
    given:
    def rfc = 'XEXX010101000'
    def expected = new Taxpayer(
        id: 1,
        customerId: 1,
        rfc: 'XEXX010101000',
        name: 'text',
        primary: 'text',
        location: 'text',
        city: 'text',
        stateId: '01',
        zipcode: 'text',
        email: 'text',
        date: new Date()

    )
    def result = new Contribuyente(
        id: 1,
        idCliente: 1,
        rfc: 'XEXX010101000',
        nombre: 'text',
        domicilio: 'text',
        colonia: 'text',
        ciudad: 'text',
        idEstado: '01',
        codigoPostal: 'text',
        fechaRegistro: new Date(),
        email: 'text'
    )

    when:
    def actual = controller.findTaxpayerByRfc( rfc )

    then:
    1 * contribuyenteService.obtenerContribuyentePorRfc( rfc ) >> result

    then:
    0 * _

    then:
    assertTaxpayerEquals( expected, actual )
  }

  def 'es lista vacia al buscar taxpayers similares con input invalido'( ) {
    when:
    def actual = controller.findTaxpayersLike( input )

    then:
    1 * contribuyenteService.listarContribuyentesPorRfcSimilar( input ) >> results

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | results                        | input
    [ ]      | null                           | null
    [ ]      | null                           | ' '
    [ ]      | null                           | '-'
    [ ]      | null                           | 'AA'
    [ ]      | [ ]                            | 'AA'
    [ ]      | [ null ]                       | 'AA'
    [ ]      | [ new Contribuyente() ]        | 'AA'
    [ ]      | [ new Contribuyente( id: 0 ) ] | 'AA'
  }

  def 'es lista de taxpayers al buscar taxpayers similares'( ) {
    given:
    def input = 'XEXX'
    def expected = new Taxpayer(
        id: 1,
        customerId: 1,
        rfc: 'XEXX010101000',
        name: 'text',
        primary: 'text',
        location: 'text',
        city: 'text',
        stateId: '01',
        zipcode: 'text',
        email: 'text',
        date: new Date()

    )
    def results = [
        new Contribuyente(
            id: 1,
            idCliente: 1,
            rfc: 'XEXX010101000',
            nombre: 'text',
            domicilio: 'text',
            colonia: 'text',
            ciudad: 'text',
            idEstado: '01',
            codigoPostal: 'text',
            fechaRegistro: new Date(),
            email: 'text'
        )
    ]

    when:
    def actual = controller.findTaxpayersLike( input )

    then:
    1 * contribuyenteService.listarContribuyentesPorRfcSimilar( input ) >> results

    then:
    0 * _

    then:
    assertTaxpayerEquals( expected, actual.first() )
  }
}

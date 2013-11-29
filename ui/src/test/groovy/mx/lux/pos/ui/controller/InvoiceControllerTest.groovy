package mx.lux.pos.ui.controller

import mx.lux.pos.model.Comprobante
import mx.lux.pos.model.Contribuyente
import mx.lux.pos.service.ComprobanteService
import mx.lux.pos.service.ContribuyenteService
import mx.lux.pos.service.TicketService
import mx.lux.pos.ui.model.Invoice
import mx.lux.pos.ui.model.Session
import mx.lux.pos.ui.model.SessionItem
import mx.lux.pos.ui.model.User
import spock.lang.Specification

import static mx.lux.pos.ui.assertions.Assert.assertInvoiceEquals

class InvoiceControllerTest extends Specification {

  private InvoiceController controller
  private ComprobanteService comprobanteService
  private ContribuyenteService contribuyenteService
  private TicketService ticketService

  def setup( ) {
    comprobanteService = Mock( ComprobanteService )
    contribuyenteService = Mock( ContribuyenteService )
    ticketService = Mock( TicketService )
    controller = new InvoiceController( comprobanteService, contribuyenteService, ticketService )
  }

  def 'es null al obtener invoice con invoiceId invalido'( ) {
    when:
    def actual = controller.getInvoice( invoiceId )

    then:
    1 * comprobanteService.obtenerComprobante( invoiceId )

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | invoiceId
    null     | null
    null     | ' '
    null     | '-'
  }

  def 'es invoice al obtener invoice con invoiceId'( ) {
    given:
    def invoiceId = '1'
    def idC = 1
    def expected = new Invoice(
        id: idC,
        invoiceId: invoiceId
    )
    def result = new Comprobante(
        id: idC,
        idFiscal: invoiceId
    )

    when:
    def actual = controller.getInvoice( invoiceId )

    then:
    1 * comprobanteService.obtenerComprobante( invoiceId ) >> result

    then:
    0 * _

    then:
    assertInvoiceEquals( expected, actual )
  }

  def 'es null al buscar invoice por ticket con ticket invalido'( ) {
    when:
    def actual = controller.findInvoiceByTicket( ticket )

    then:
    1 * comprobanteService.listarComprobantesPorTicket( ticket ) >> results

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | results                      | ticket
    null     | null                         | null
    null     | null                         | ' '
    null     | null                         | '-'
    null     | null                         | 'text-text'
    null     | [ ]                          | 'text-text'
    null     | [ null ]                     | 'text-text'
    null     | [ new Comprobante() ]        | 'text-text'
    null     | [ new Comprobante( id: 0 ) ] | 'text-text'
  }

  def 'es invoice al buscar invoice por ticket'( ) {
    given:
    def ticket = 'text-text'
    def invoiceId = '1'
    def idC = 1
    def expected = new Invoice(
        id: idC,
        invoiceId: invoiceId
    )
    def results = [
        new Comprobante(
            id: idC,
            idFiscal: invoiceId
        )
    ]

    when:
    def actual = controller.findInvoiceByTicket( ticket )

    then:
    1 * comprobanteService.listarComprobantesPorTicket( ticket ) >> results

    then:
    0 * _

    then:
    assertInvoiceEquals( expected, actual )
  }

  def 'es null al solicitar invoice con invoice invalido'( ) {
    when:
    def actual = controller.requestInvoice( invoice )

    then:
    _ * comprobanteService.registrarComprobante( _ ) >> comprobante

    then:
    0 * _

    then:
    actual == expected

    where:
    expected | comprobante              | invoice
    null     | null                     | null
    null     | null                     | new Invoice( ticket: ' ' )
    null     | null                     | new Invoice( orderId: ' ' )
    null     | null                     | new Invoice( ticket: ' ', orderId: ' ' )
    null     | null                     | new Invoice( ticket: '-', orderId: '-' )
    null     | new Comprobante()        | new Invoice( ticket: 'text-text', orderId: 'text' )
    null     | new Comprobante( id: 0 ) | new Invoice( ticket: 'text-text', orderId: 'text' )
  }

  def 'es invoice al solicitar invoice'( ) {
    given:
    def rfc = 'XEXX010101000'
    def ticket = '9999-999999'
    def orderId = 'A1'
    def bizName = 'text'
    def invoiceType = 'N'
    def primary = 'text'
    def location = 'text'
    def city = 'text'
    def state = 'text'
    def country = 'text'
    def zipcode = 'text'
    def email = 'text'
    def rx = 'text'
    def patient = 'text'
    def comments = 'text'
    Session.put( SessionItem.USER, new User( username: '01' ) )
    def expected = new Invoice(
        id: 1,
        invoiceId: '123456',
        rfc: rfc,
        ticket: ticket,
        orderId: orderId,
        customerId: '99',
        bizName: bizName,
        invoiceType: invoiceType,
        primary: primary,
        location: location,
        city: city,
        state: state,
        country: country,
        zipcode: zipcode,
        email: email,
        rx: rx,
        patient: patient,
        comments: comments,
        url: 'text',
        xml: 'text'
    )
    def invoice = new Invoice(
        rfc: rfc,
        ticket: ticket,
        orderId: orderId,
        bizName: bizName,
        invoiceType: invoiceType,
        primary: primary,
        location: location,
        city: city,
        state: state,
        country: country,
        zipcode: zipcode,
        email: email,
        rx: rx,
        patient: patient,
        comments: comments
    )
    def comprobante = new Comprobante(
        id: 1,
        idFiscal: '123456',
        rfc: rfc,
        ticket: ticket,
        idEmpleado: '01',
        idFactura: orderId,
        idCliente: '99',
        razon: bizName,
        tipoFactura: invoiceType,
        calle: primary,
        colonia: location,
        municipio: city,
        estado: state,
        pais: country,
        codigoPostal: zipcode,
        email: email,
        rx: rx,
        paciente: patient,
        observaciones: comments,
        url: 'text',
        xml: 'text'
    )
    def contribuyente = new Contribuyente(
        idCliente: 99,
        rfc: rfc,
        nombre: bizName,
        domicilio: primary,
        colonia: location,
        ciudad: city,
        idEstado: state,
        codigoPostal: zipcode,
        email: email
    )

    when:
    def actual = controller.requestInvoice( invoice )

    then:
    1 * comprobanteService.registrarComprobante( { Comprobante tmp ->
      assert tmp.rfc == comprobante.rfc
      assert tmp.ticket == comprobante.ticket
      assert tmp.idFactura == comprobante.idFactura
      assert tmp.razon == comprobante.razon
      assert tmp.tipoFactura == comprobante.tipoFactura
      assert tmp.calle == comprobante.calle
      assert tmp.colonia == comprobante.colonia
      assert tmp.municipio == comprobante.municipio
      assert tmp.estado == comprobante.estado
      assert tmp.pais == comprobante.pais
      assert tmp.codigoPostal == comprobante.codigoPostal
      assert tmp.email == comprobante.email
      assert tmp.rx == comprobante.rx
      assert tmp.paciente == comprobante.paciente
      assert tmp.observaciones == comprobante.observaciones
      assert tmp.idEmpleado == comprobante.idEmpleado
      return tmp
    } as Comprobante ) >> comprobante
    1 * contribuyenteService.registrarContribuyente( { Contribuyente tmp ->
      assert tmp.idCliente == contribuyente.idCliente
      assert tmp.rfc == contribuyente.rfc
      assert tmp.nombre == contribuyente.nombre
      assert tmp.domicilio == contribuyente.domicilio
      assert tmp.colonia == contribuyente.colonia
      assert tmp.ciudad == contribuyente.ciudad
      assert tmp.idEstado == contribuyente.idEstado
      assert tmp.codigoPostal == contribuyente.codigoPostal
      assert tmp.email == contribuyente.email
      return tmp
    } as Contribuyente )

    then:
    0 * _

    then:
    assertInvoiceEquals( expected, actual )
  }
}

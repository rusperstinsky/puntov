package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.lux.pos.model.Comprobante

@Bindable
@ToString
@EqualsAndHashCode
class Invoice {
  Integer id
  String invoiceId
  String rfc
  String ticket
  String orderId
  String bill
  String type
  String sourceId
  String customerId
  String bizName
  String status
  String invoiceType
  String primary
  String location
  String city
  String state
  String country
  String zipcode
  String amount
  String email
  String url
  String xml
  String rx
  String patient
  String paymentForm
  String paymentMethod
  String comments
  Date issueDate

  static Invoice toInvoice( Comprobante comprobante ) {
    if ( comprobante?.id ) {
      return new Invoice(
          id: comprobante.id,
          invoiceId: comprobante.idFiscal,
          rfc: comprobante.rfc,
          ticket: comprobante.ticket,
          orderId: comprobante.idFactura,
          bill: comprobante.factura,
          type: comprobante.tipo,
          sourceId: comprobante.idOrigen,
          customerId: comprobante.idCliente,
          bizName: comprobante.razon,
          status: comprobante.estatus,
          invoiceType: comprobante.tipoFactura,
          primary: comprobante.calle,
          location: comprobante.colonia,
          city: comprobante.municipio,
          state: comprobante.estado,
          country: comprobante.pais,
          zipcode: comprobante.codigoPostal,
          issueDate: comprobante.fechaImpresion,
          amount: comprobante.importe,
          email: comprobante.email,
          url: comprobante.url,
          xml: comprobante.xml,
          rx: comprobante.rx,
          patient: comprobante.paciente,
          paymentForm: comprobante.formaPago,
          paymentMethod: comprobante.metodoPago,
          comments: comprobante.observaciones
      )
    }
    return null
  }
}

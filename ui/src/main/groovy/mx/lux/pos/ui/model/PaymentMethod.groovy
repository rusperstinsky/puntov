package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.lux.pos.model.FormaPago

@Bindable
@ToString
@EqualsAndHashCode
class PaymentMethod {
  String id
  String description
  boolean requestReference
  boolean requestBank
  boolean acceptReturn
  boolean acceptPaymentPlan
  boolean editable
  String defaultReturn
  Date editDate

  static toPaymentMethod( FormaPago formaPago ) {
    if ( formaPago?.id ) {
      PaymentMethod paymentMethod = new PaymentMethod(
          id: formaPago.id.trim(),
          description: formaPago.descripcion,
          requestReference: formaPago.pedirReferencia,
          requestBank: formaPago.pedirBanco,
          acceptReturn: formaPago.aceptaDevoluciones,
          acceptPaymentPlan: formaPago.aceptaEnPagos,
          editable: formaPago.modificable,
          defaultReturn: formaPago.defaultFormaDevolucion,
          editDate: formaPago.fechaModificacion
      )
      return paymentMethod
    }
    return null
  }
}

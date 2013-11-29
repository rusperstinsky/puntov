package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.lux.pos.model.TipoPago

@Bindable
@ToString
@EqualsAndHashCode
class PaymentType {
  String id
  String description
  String soi
  String con
  String f1
  String f2
  String f3
  String f4
  String f5

  static toPaymentType( TipoPago tipoPago ) {
    if ( tipoPago?.id ) {
      PaymentType paymentType = new PaymentType(
          id: tipoPago.id.trim(),
          description: tipoPago.descripcion,
          soi: tipoPago.tipoSoi,
          con: tipoPago.tipoCon,
          f1: tipoPago.f1,
          f2: tipoPago.f2,
          f3: tipoPago.f3,
          f4: tipoPago.f4,
          f5: tipoPago.f5
      )
      return paymentType
    }
    return null
  }
}

package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.lux.pos.model.Devolucion

@Bindable
@ToString
@EqualsAndHashCode
class Refund {
  Integer id
  Integer modificationId
  Integer paymentId
  Integer bankId
  String paymentTypeId
  String reference
  String transfer
  String type
  BigDecimal amount
  Date date

  static Refund toRefund( Devolucion devolucion ) {
    if ( devolucion?.idMod ) {
      Refund refund = new Refund(
          id: devolucion.id,
          modificationId: devolucion.idMod,
          paymentId: devolucion.idPago,
          paymentTypeId: devolucion.idFormaPago,
          bankId: devolucion.idBanco,
          reference: devolucion.referencia,
          amount: devolucion.monto,
          date: devolucion.fecha,
          transfer: devolucion.transf,
          type: devolucion.tipo
      )
      return refund
    }
    return null
  }
}

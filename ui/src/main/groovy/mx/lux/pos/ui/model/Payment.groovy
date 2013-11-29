package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.lux.pos.model.Pago
import org.apache.commons.lang3.math.NumberUtils

@Bindable
@ToString
@EqualsAndHashCode
class Payment {
  Integer id
  String order
  String paymentReference
  String codeReference
  String username
  String paymentType
  String paymentTypeId
  String terminal
  String terminalId
  String plan
  String planId
  String issuerBank
  String issuerBankId
  String refundMethod
  String factura
  BigDecimal amount
  BigDecimal refund
  BigDecimal refundable
  Date date

  String getDescription( ) {
    Integer pos = ( paymentReference?.size() >= 4 ) ? ( paymentReference.size() - 4 ) : 0
    "${paymentTypeId ? "${paymentTypeId} " : ''}${paymentReference ? "${paymentReference.substring( pos )} " : ''}"
  }

  void setAmount( BigDecimal pAmount ) {
    this.amount = NumberUtils.createBigDecimal( String.format( "%.2f", pAmount ) )
  }

  void setAmount( Double pAmount ) {
    this.amount = NumberUtils.createBigDecimal( String.format( "%.2f", pAmount ) )
  }

  static toPaymment( Pago pago ) {
    if ( pago?.id ) {
      Payment payment = new Payment(
          id: pago.id,
          order: pago.idFactura,
          paymentReference: pago.referenciaPago,
          codeReference: pago.referenciaClave,
          username: pago.idEmpleado,
          paymentType: pago.eTipoPago?.descripcion,
          paymentTypeId: pago.idFPago,
          terminal: pago.terminal?.descripcion,
          terminalId: pago.idTerminal,
          plan: pago.plan?.descripcion,
          planId: pago.idPlan,
          issuerBankId: pago.idBancoEmisor,
          factura: pago.notaVenta?.factura,
          amount: pago.monto,
          refundable: pago.porDevolver,
          date: pago.fecha
      )
      return payment
    }
    return null
  }
}

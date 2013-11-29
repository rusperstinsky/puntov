package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.lux.pos.model.DetalleComprobante

@Bindable
@ToString
@EqualsAndHashCode
class InvoiceItem {
  Integer id
  String invoiceId
  String itemId
  String itemName
  String itemColor
  String itemType
  String description
  String quantity
  BigDecimal unitPrice
  BigDecimal amount

  static InvoiceItem toInvoiceItem( DetalleComprobante detalle ) {
    if ( detalle?.id ) {
      return new InvoiceItem(
          id: detalle.id,
          invoiceId: detalle.idFiscal,
          itemId: detalle.idArticulo,
          itemName: detalle.articulo,
          itemColor: detalle.color,
          itemType: detalle.idGenerico,
          description: detalle.descripcion,
          quantity: detalle.cantidad,
          unitPrice: detalle.precioUnitario,
          amount: detalle.importe
      )
    }
    return null
  }
}

package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.lux.pos.model.DetalleNotaVenta
import mx.lux.pos.model.OrdenPromDet
import mx.lux.pos.model.Promocion
import mx.lux.pos.ui.controller.OrderController

@Bindable
@ToString
@EqualsAndHashCode
class OrderLinePromotion implements IPromotion {

  private OrdenPromDet promotionItem
  private DetalleNotaVenta item
  private Promocion promotion

  private OrderLinePromotion( ) { }

  static IPromotion toPromotions( OrdenPromDet ordenPromDet ) {
    if ( ordenPromDet?.idFactura ) {
      OrderLinePromotion promotion = new OrderLinePromotion()
      promotion.promotionItem = ordenPromDet
      promotion.item = OrderController.getDetalleNotaVenta( promotion.promotionItem.idFactura, promotion.promotionItem.idArticulo )
      promotion.promotion = OrderController.getPromocion( ordenPromDet.idPromocion )
      return promotion
    }
    return null
  }

  String getDescripcion( ) {
    String desc = "N/A"
    if ( promotion != null ) {
      desc = promotion.descripcion
    }
    return desc
  }

  String getArticulo( ) {
    String art = "N/A"
    if ( item != null ) {
      art = item.articulo.articulo
    }
    return art
  }

  BigDecimal getPrecioLista( ) {
    BigDecimal listPrice = BigDecimal.ZERO
    if ( item != null ) {
      listPrice = item.precioUnitLista
    }
    return listPrice
  }

  BigDecimal getDescuento( ) {
    BigDecimal discount = BigDecimal.ZERO
    if ( promotionItem != null ) {
      discount = promotionItem.descuentoMonto
    }
    return discount
  }

  BigDecimal getPrecioNeto( ) {
    BigDecimal netPrice = BigDecimal.ZERO
    if ( item != null && promotionItem != null ) {
      netPrice = item.precioUnitLista.subtract( promotionItem.descuentoMonto )
    }
    return netPrice
  }


}

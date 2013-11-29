package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.lux.pos.model.NotaVenta

@Bindable
@ToString
@EqualsAndHashCode
class OrderDiscount implements IPromotion {

  NotaVenta notaVenta = new NotaVenta()
  private OrderDiscount( ) { }

  private static final String DESCRIPCION = "%s%% descuento sobre venta"

  static IPromotion toPromotions( NotaVenta notaVenta ) {
    if ( (notaVenta != null) && ( notaVenta.por100Descuento > 0 ) && ( notaVenta.montoDescuento > 0 ) ) {
      OrderDiscount promotion = new OrderDiscount()
      promotion.notaVenta = notaVenta
      return promotion
    }
    return null
  }

  String getDescripcion( ) {
    String desc = "N/A"
    if ( notaVenta != null ) {
      desc = String.format( DESCRIPCION, notaVenta.por100Descuento.toString() )
    }
    return desc
  }

  String getArticulo( ) {
    String art = '*'
    return art
  }

  BigDecimal getPrecioLista( ) {
    BigDecimal listPrice = BigDecimal.ZERO
    if ( notaVenta != null ) {
      listPrice = notaVenta.montoDescuento.add( notaVenta.ventaNeta )
    }
    return listPrice
  }

  BigDecimal getDescuento( ) {
    BigDecimal discount = BigDecimal.ZERO
    if ( notaVenta != null ) {
      discount = notaVenta.montoDescuento
    }
    return discount
  }

  BigDecimal getPrecioNeto( ) {
    BigDecimal netPrice = BigDecimal.ZERO
    if ( notaVenta != null ) {
      netPrice = notaVenta.ventaNeta
    }
    return netPrice
  }

}

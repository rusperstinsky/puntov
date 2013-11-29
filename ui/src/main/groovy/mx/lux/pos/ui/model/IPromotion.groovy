package mx.lux.pos.ui.model

import mx.lux.pos.model.Articulo
import mx.lux.pos.model.PromotionClass
import mx.lux.pos.model.PromotionModel
import mx.lux.pos.model.PromotionPart

interface IPromotion {

  String getDescripcion( )

  String getArticulo( )

  BigDecimal getPrecioLista( )

  BigDecimal getDescuento( )

  BigDecimal getPrecioNeto( )
  
}

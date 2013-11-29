package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.lux.pos.model.Promocion
import org.apache.commons.lang3.math.NumberUtils

@Bindable
@ToString
@EqualsAndHashCode
class Promotion {
  Integer id
  String description
  String articleProm
  Boolean aplyConv
  Integer priority
  Boolean offerPrice
  Date initialTerm
  Date finalTerm
  String storeGroupId
  Boolean aplyAuto
  Boolean mandatory
  String typePromotion
  String genericId
  String type
  String subType
  String brand
  String article
  String priceType
  BigDecimal discountedPrice
  BigDecimal discount
  String genericC
  String typeC
  String subtypeC
  String brandC
  String articleC
  String priceTypeC
  String discountedPriceC
  String discountC

  static toPromotion( Promocion promocion ) {
    if ( promocion?.idPromocion ) {
      Promotion promotion = new Promotion(
          id: promocion.idPromocion,
          description: promocion.descripcion,
          articleProm: promocion.articuloProm,
          aplyConv: promocion.aplicaConvenio,
          priority: promocion.prioridad,
          offerPrice: promocion.precioOferta,
          initialTerm: promocion.vigenciaIni,
          finalTerm: promocion.vigenciaFin,
          storeGroupId: promocion.idGrupoTienda,
          aplyAuto: promocion.aplicaAuto,
          mandatory: promocion.obligatoria,
          typePromotion: promocion.tipoPromocion,
          genericId: promocion.idGenerico,
          type: promocion.tipo,
          subType: promocion.subtipo,
          brand: promocion.marca,
          article: promocion.articulo,
          priceType: promocion.tipoPrecio,
          discountedPrice: promocion.precioDescontado,
          discount: promocion.descuento,
          genericC: promocion.idGenericoC,
          typeC: promocion.idGenericoC,
          subtypeC: promocion.subtipoC,
          brandC: promocion.marcaC,
          articleC: promocion.articuloC,
          priceTypeC: promocion.tipoPrecioC,
          discountedPriceC: promocion.precioDescontadoC,
          discountC: promocion.descuentoC
      )
      return promotion
    }
    return null
  }
}

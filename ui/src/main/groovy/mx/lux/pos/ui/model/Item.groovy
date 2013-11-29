package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.util.logging.Slf4j
import mx.lux.pos.model.Articulo
import mx.lux.pos.model.DetalleNotaVenta
import mx.lux.pos.ui.controller.ItemController

@Slf4j
@Bindable
@ToString
@EqualsAndHashCode
class Item {
  Integer id
  BigDecimal price
  BigDecimal listPrice
  String name
  String color
  String colorDesc
  String reference
  String type
  String genericType
  String genericSubType
  String lensDesign
  String operation
  String priceType
  String location
  String brand
  String typ
  String typeArticle
  String subtype
  Integer stock

  static String manualPriceTypeList

  String getDescription( ) {
    "${reference ? "${reference} " : ''}${color ? "[${color}] " : ''}${colorDesc ?: ''}"
  }

  static Item toItem( Articulo articulo ) {
    if ( articulo?.id ) {
      Item item = new Item(
          id: articulo.id,
          price: articulo.precio,
          listPrice: articulo.precio,
          name: articulo.articulo,
          color: articulo.codigoColor,
          colorDesc: articulo.descripcionColor,
          reference: articulo.descripcion,
          type: articulo.idGenerico,
          genericType: articulo.idGenTipo,
          genericSubType: articulo.idGenSubtipo,
          lensDesign: articulo.idDisenoLente,
          operation: articulo.operacion,
          priceType: articulo.tipoPrecio,
          location: articulo.ubicacion,
          brand: articulo.marca,
          typ: articulo.tipo,
          typeArticle: articulo.tipo,
          subtype: articulo.subtipo,
          stock: articulo.cantExistencia,
      )
      return item
    }
    return null
  }

  static Item toItem( DetalleNotaVenta detalleNotaVenta ) {
    try {
      if ( detalleNotaVenta?.id ) {
        Articulo articulo = detalleNotaVenta.articulo
        Item item = new Item(
            id: articulo.id,
            price: detalleNotaVenta.precioUnitFinal,
            listPrice: detalleNotaVenta.precioUnitLista,
            name: articulo.articulo,
            color: articulo.codigoColor,
            colorDesc: articulo.descripcionColor,
            reference: articulo.descripcion,
            type: articulo.idGenerico,
            genericType: articulo.idGenTipo,
            genericSubType: articulo.idGenSubtipo,
            lensDesign: articulo.idDisenoLente,
            operation: articulo.operacion,
            priceType: articulo.tipoPrecio,
            location: articulo.ubicacion
        )
        return item
      }
    } catch ( Exception e ) {
      log.error( e.message, e )
    }
    return null
  }

  boolean isManualPriceItem( ) {
    if ( manualPriceTypeList == null ) {
      manualPriceTypeList = ItemController.getManualPriceTypeList()
    }
    return manualPriceTypeList.contains( this.type )
  }
}

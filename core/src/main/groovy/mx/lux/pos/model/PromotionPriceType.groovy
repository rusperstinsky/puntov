package mx.lux.pos.model

import org.apache.commons.lang3.StringUtils;

enum PromotionPriceType {

  FixedPrice( "F", "Precio final fijo" ),
  FixedDiscountAmount( "DESCONTADO", "Monto a descontar del precio" ),
  Undefined( "DESCUENTO", "Promocion expresada en porcentaje" )

  private String id
  private String text

  PromotionPriceType( String pId, String pText ) {
    id = pId
    text = pText
  }

  static PromotionPriceType parse( String pId ) {
    PromotionPriceType parsed = Undefined
    for ( PromotionPriceType type : values() ) {
      String typeId = StringUtils.trimToEmpty( pId ).toUpperCase()
      if ( typeId.length() > type.id.length() ) {
        typeId = typeId.substring( 0, type.id.length() )
      }
      if ( typeId.equals( type.id ) ) {
        parsed = type
      }
    }
    return parsed
  }

  String toString( ) {
    return String.format( "%s - %s", id, text )
  }

}

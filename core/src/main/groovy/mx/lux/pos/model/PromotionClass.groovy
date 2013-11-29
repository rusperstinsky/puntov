package mx.lux.pos.model

import org.apache.commons.lang3.StringUtils

enum PromotionClass {

  Simple( "S", "Promocion sobre un articulo" ),
  Combo( "C", "Promocion en la compra de dos articulos" )

  String id, text

  PromotionClass( String pId, String pText ) {
    this.setId( pId )
    this.setText( pText )
  }

  private void setId( String pId ) {
    this.id = pId
  } 
  
  private void setText( String pText ) {
    this.text = pText
  }
  
  static PromotionClass parse( String pId ) {
    PromotionClass parsed = Simple
    for ( PromotionClass pc in values( ) ) {
      String classId = StringUtils.trimToEmpty( pId ).toUpperCase( )
      if ( classId.length() > pc.id.length() ) {
        classId = classId.substring( 0, pc.id.length() )
      }
      if ( classId.equals( pc.id ) ) {
        parsed = pc
      }
    }
    return parsed
  }

  String getLegend( ) {
    return String.format( "%s - %s", this.id, this.text )
  }
  
}

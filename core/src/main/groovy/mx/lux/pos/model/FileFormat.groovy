package mx.lux.pos.model

import org.apache.commons.lang3.StringUtils

enum FileFormat {
  DEFAULT, LUX, SUNGLASS, MAS_VISION
  
  boolean equals(String pValue) {
    boolean result = false
    String value = StringUtils.trimToEmpty( pValue ).toLowerCase( )
    if ( ( value.length( ) > 0 ) && ( value.length( ) <= this.name( ).length( ) ) ){
      String lcName = this.name( ).toLowerCase( ).substring( 0, value.length( ) )
      result = lcName.equals( value )
    } 
    return result  
  }
}

package mx.lux.pos.model

import org.apache.commons.lang3.StringUtils

class InvOhSummary {

  String genre = null
  String brand = null
  List<InvOhDet> lines = new ArrayList<InvOhDet>()

  Integer getQtyTotal( ) {
    Integer qty = 0
    for ( InvOhDet line : this.lines ) {
      qty += line.qty
    }
    return qty
  }

  void setBrand( String pBrand ) {
    brand = null
    if ( pBrand != null ) {
      brand = StringUtils.trimToNull( pBrand.toUpperCase() )
    }
  }

  void setGenre( String pGenre ) {
    genre = null
    if ( pGenre != null ) {
      genre = StringUtils.trimToNull( pGenre.toUpperCase() )
    }
  }

  String toString( ) {
    StringBuffer sb = new StringBuffer()
    sb.append( String.format( "[%s] Genre: %s   Brand:%s", this.getClass().getSimpleName(), this.genre, this.brand ) )
    for ( InvOhDet line : lines ) {
      sb.append( String.format( "\n    %s", line.toString() ) )
    }
    return sb.toString()
  }
}

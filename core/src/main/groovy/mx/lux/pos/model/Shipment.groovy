package mx.lux.pos.model

import org.apache.commons.lang3.StringUtils

class Shipment {

  FileFormat source
  String ref
  Integer lineNbr
  String genre
  String fullRef
  String code
  String trType
  String trReason
  String supplier
  Integer siteTo
  Integer siteFrom
  Date trDate
  Double vat
  String currCode
  Double usdRate
  Collection<ShipmentLine> lines

  Shipment( ) {
    this.setSource( FileFormat.DEFAULT )
    this.setRef( "" )
    this.setLineNbr( 0 )
    this.setGenre( "" )
    this.setFullRef( "" )
    this.setCode( "" )
    this.lines = new ArrayList<ShipmentLine>()
  }

  Collection<ArticuloSombra> getPartShadows( ) {
    Collection<ArticuloSombra> shadows = new ArrayList<ArticuloSombra>()
    if ( FileFormat.DEFAULT.equals( source ) || FileFormat.LUX.equals( source ) ) {
      for ( ShipmentLine det in lines ) {
        ArticuloSombra shadow = det.getPartShadow()
        shadow.setGenerico( genre )
        shadows.add( shadow )
      }
    }
    return shadows
  }

  void setCode( String pCode ) {
    this.code = StringUtils.trimToEmpty( pCode ).toUpperCase()
  }

  void setFullRef( String pFullRef ) {
    this.fullRef = StringUtils.trimToEmpty( pFullRef ).toUpperCase()
  }

  void setGenre( String pGenre ) {
    this.genre = StringUtils.trimToEmpty( pGenre ).toUpperCase()
  }

  void setLineNbr( Integer pLineNbr ) {
    this.lineNbr = ( pLineNbr != null ? pLineNbr : 0 )
  }

  void setRef( String pRef ) {
    this.ref = StringUtils.trimToEmpty( pRef ).toUpperCase()
  }

  String toString( ) {
    StringBuffer sb = new StringBuffer()
    sb.append( String.format( "[%s] Ref:%s Lines:%d", this.source.toString(), this.fullRef, this.lineNbr ) )
    for ( ShipmentLine line : lines ) {
      sb.append( String.format( "\n    %s", line.toString() ) )
    }
    return sb.toString()
  }

}

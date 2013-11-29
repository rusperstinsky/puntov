package mx.lux.pos.model

import org.apache.commons.lang3.StringUtils

class InvAdjustSheet {

  FileFormat source
  String ref
  Integer lineCount
  String genre
  String fullRef
  String code
  String trType
  String trReason
  Integer site
  Date trDate
  Collection<InvAdjustLine> lines

  InvAdjustSheet( ) {
    this.setSource( FileFormat.DEFAULT )
    this.setRef( "" )
    this.setLineCount( 0 )
    this.setGenre( "" )
    this.setFullRef( "" )
    this.setCode( "" )
    this.lines = new ArrayList<InvAdjustLine>()
  }

  Collection<ArticuloSombra> getPartShadows( ) {
    Collection<ArticuloSombra> shadows = new ArrayList<ArticuloSombra>()
    if ( FileFormat.DEFAULT.equals( source ) || FileFormat.LUX.equals( source ) ) {
      for ( InvAdjustLine det in lines ) {
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

  void setLineCount( Integer pLineNbr ) {
    this.lineCount = ( pLineNbr != null ? pLineNbr : 0 )
  }

  void setRef( String pRef ) {
    this.ref = StringUtils.trimToEmpty( pRef ).toUpperCase()
  }

  String headerToString( ) {
    StringBuffer sb = new StringBuffer()
    sb.append( String.format( "[%s] Ref:%s Lines:%d/%d", this.source.toString(), this.ref, this.lineCount,
        this.lines.size() ) )
    return sb.toString()
  }

  String toString( ) {
    StringBuffer sb = new StringBuffer()
    sb.append( this.headerToString() )
    for ( InvAdjustLine line : lines ) {
      sb.append( String.format( "\n    %s", line.toString() ) )
    }
    return sb.toString()
  }

}

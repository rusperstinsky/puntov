package mx.lux.pos.model

import mx.lux.pos.util.StringList
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.text.ParseException

class GrupoArticuloImportLine implements Comparable<GrupoArticuloImportLine> {

  private static enum Field {
    Field, Tag, GroupId, PartNbr, Desc
  }

  private static final String MSG_UNABLE_TO_PARSE = "Unable to parse <%s>"
  private static final String TXT_TO_STRING = 'Line[ Group:%d  Desc:"%s"  Articulo:%s ]'
  private static final String TAG_RECORD = 'D'
  private static final String DELIMITER = '|'

  private static final Logger logger = LoggerFactory.getLogger( GrupoArticuloImportLine.class )
  private StringList importedString;

  protected GrupoArticuloImportLine( StringList pList ) {
    this.importedString = pList
  }

  // Public Methods
  String getTagId( ) {
    return importedString.entry( Field.Tag.ordinal() ).toUpperCase()
  }

  Integer getGroupId( ) {
    return importedString.asInteger( Field.GroupId.ordinal() )
  }

  String getDescription( ) {
    return importedString.entry( Field.Desc.ordinal() )
  }

  String getPartNbr( ) {
    return importedString.entry( Field.PartNbr.ordinal() )
  }

  int compareTo( GrupoArticuloImportLine pImportLine ) {
    int result = this.groupId.compareTo( pImportLine.groupId )
    if ( result == 0 ) {
      result = this.partNbr.compareTo( pImportLine.partNbr )
    }
    return result
  }

  static GrupoArticuloImportLine adapt( String pLine ) throws ParseException {
    StringList list = new StringList( pLine, DELIMITER )
    GrupoArticuloImportLine line = null
    Boolean valid = false
    if ( list.size >= Field.values().size() ) {
      try {
        line = new GrupoArticuloImportLine( list )
        valid = ( line.tagId.equals( TAG_RECORD ) ) && ( line.groupId > 0 ) && ( line.partNbr.length() > 0 )
      } catch ( ParseException e ) {
        logger.debug( e.getMessage() )
      }
    }
    if ( !valid || ( line == null ) ) {
      throw new ParseException( String.format( MSG_UNABLE_TO_PARSE, pLine ), 0 )
    }
    return line
  }

  static List<GrupoArticuloImportLine> adaptList( List<String> pLineList ) {
    List<GrupoArticuloImportLine> adapted = new ArrayList<GrupoArticuloImportLine>()
    for ( String line : pLineList ) {
      try {
        adapted.add( adapt( line ) )
      } catch ( ParseException e ) {
        logger.debug( e.getMessage() )
      }
    }
    Collections.sort( adapted )
    return adapted
  }

  String toString( ) {
    return String.format( TXT_TO_STRING, this.groupId, this.description, this.partNbr )
  }
}

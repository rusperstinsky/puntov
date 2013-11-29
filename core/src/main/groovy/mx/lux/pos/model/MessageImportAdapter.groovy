package mx.lux.pos.model

import mx.lux.pos.util.StringList
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.text.ParseException

class MessageImportAdapter {
  private static enum Field {
    id, text
  }
  private static final String DELIMITER = '¡'
  private static final String MSG_UNABLE_TO_PARSE = 'Unable to parse: <%s>'
  private static Logger logger = LoggerFactory.getLogger( MessageImportAdapter.class )

  StringList messageImportString

  private MessageImportAdapter( StringList pImportString ) {
    this.messageImportString = pImportString
  }

  // public methods
  static MessageImportAdapter parse( String pString ) throws ParseException {
    return parse( pString, DELIMITER )
  }

  static MessageImportAdapter parse( String pString, String pDelimiter ) throws ParseException {
    MessageImportAdapter adapter = null
    StringList list = new StringList( pString, pDelimiter )
    try {
      list.asInteger( Field.id.ordinal() )
      if ( list.size >= Field.values().size() ) {
        adapter = new MessageImportAdapter( list )
      }
    } catch ( Exception e ) {
      logger.debug( e.message )
    }
    if ( adapter == null ) {
      throw new ParseException( String.format( MSG_UNABLE_TO_PARSE, pString ), 0 )
    }
    return adapter
  }

  Integer getId( ) {
    return this.messageImportString.asInteger( Field.id.ordinal() )
  }

  String getText( ) {
    return this.messageImportString.entry( Field.text.ordinal() )
  }

  // Identity
  int compareTo( MessageImportAdapter pAdapter ) {
    return this.getId().compareTo( pAdapter.getId() )
  }

  boolean equals( Object pObj ) {
    boolean result = false
    if ( pObj instanceof MessageImportAdapter ) {
      result = this.getId().equals( ( pObj as MessageImportAdapter ).getId() )
    } else if ( pObj instanceof Mensaje ) {
      result = this.getId().equals( ( pObj as Mensaje ).id )
    }
    return result
  }

  int hashCode( ) {
    return this.getId().hashCode()
  }

  String toString( ) {
    return String.format( 'adapter: (%d) %s', this.getId(), this.getText() )
  }
}

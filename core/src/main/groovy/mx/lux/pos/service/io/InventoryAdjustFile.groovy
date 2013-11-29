package mx.lux.pos.service.io

import mx.lux.pos.service.business.Registry
import mx.lux.pos.service.impl.ServiceFactory
import mx.lux.pos.util.StringList
import mx.lux.pos.util.SunglassUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import mx.lux.pos.model.*

class InventoryAdjustFile {

  private static final String MSG_ERROR_READING_FILE = "[Reader] ERROR reading from file: <%s>"
  private static final DELIMITER = "|"

  private static enum DetFld {
    Sku, QtyAdjust
  }
  private static enum HdrFld {
    Site, Date, RecCount, Ref, Rmks
  }

  private static final String FMT_TR_DATE = 'dd/MM/yyyy'

  private Logger logger = LoggerFactory.getLogger( this.getClass() )

  // Internal Methods
  protected Boolean hasTaggedFields( StringList pList ) {
    Boolean result = true
    return result
  }

  protected InvAdjustLine parseDetail( String pDetailLine ) {
    InvAdjustLine parsed = null
    StringList tokens = new StringList( pDetailLine, DELIMITER )
    if ( tokens.size >= DetFld.values().size() ) {
      parsed = new InvAdjustLine()
      parsed.sku = tokens.asInteger( DetFld.Sku.ordinal() )
      parsed.qty = (Math.round( tokens.asDouble( DetFld.QtyAdjust.ordinal() ) ) as Integer)
    }
    return parsed
  }

  protected InvAdjustSheet parseHeader( String pHeaderLine ) {
    InvAdjustSheet parsed = null
    StringList tokens = new StringList( pHeaderLine, DELIMITER )
    if ( tokens.size > HdrFld.RecCount.ordinal() && hasTaggedFields( tokens ) ) {
      parsed = new InvAdjustSheet()
      parsed.source = FileFormat.DEFAULT
      parsed.ref = tokens.entry( HdrFld.Ref.ordinal() )
      parsed.site = SunglassUtils.parseSite( tokens.entry( HdrFld.Site.ordinal() ) )
      parsed.lineCount = tokens.asInteger( HdrFld.RecCount.ordinal() )
      parsed.trDate = tokens.asDate( HdrFld.Date.ordinal(), FMT_TR_DATE )
      parsed.trReason = tokens.entry( HdrFld.Rmks.ordinal() )
    }
    return parsed
  }

  // Public methods
  InvAdjustSheet read( String pFilename ) {
    InvAdjustSheet document = null
    File inFile = new File( pFilename )

    if ( inFile.exists() ) {
      try {
        Integer iLine = 0
        inFile.eachLine() {
          iLine++
          if ( iLine == 1 ) {
            document = parseHeader( it )
          } else {
            if ( document != null ) {
              InvAdjustLine line = parseDetail( it )
              if ( line != null ) {
                document.lines.add( line )
              }
            }
          }
        }
      } catch ( Exception e ) {
        if ( this.logger.debugEnabled ) {
          this.logger.error( String.format( MSG_ERROR_READING_FILE, inFile.absolutePath ), e )
        } else {
          this.logger.error( String.format( MSG_ERROR_READING_FILE, inFile.absolutePath ) )
        }
      }
    }
    return document
  }

}

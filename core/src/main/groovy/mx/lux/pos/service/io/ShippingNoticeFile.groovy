package mx.lux.pos.service.io

import mx.lux.pos.service.business.ResourceManager
import mx.lux.pos.service.impl.ServiceFactory
import mx.lux.pos.util.StringList
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import mx.lux.pos.model.*

import java.text.NumberFormat

class ShippingNoticeFile {

  private static enum DetFld {
    PartCode, ColorCode, fld_1, QtyTrans, fld_2, Sku, BarCode, ColorDesc, SubType, Type, fld_3
  }
  private static enum HdrFld {
    Ref, LnCount, Fam, FullRef, Code, fld_1
  }

  private static final String MSG_ERROR_READING_FILE = "[Reader] ERROR reading from file: <%s>"
  private static final DELIMITER = "|"
  private static final ANOTHER_DELIMITER = ">"

  private Logger log = LoggerFactory.getLogger( this.getClass() )

  // Internal Methods
  protected String getLocation( ) {
    return ResourceManager.getLocation( TipoParametro.RUTA_REMISION ).absolutePath
  }


  protected  Shipment parseSolicitud(String data) {
      ShipmentLine parsed = null
      Shipment shipment = null
      //StringList tokens = new StringList( data, DELIMITER )
      int ocurr = data.lastIndexOf(">");
      String dataHeader = data.substring(0, ocurr)
      StringList tokensHeader = new StringList( dataHeader, ANOTHER_DELIMITER )
      String dataTmp = data.substring(ocurr + 1, data.length())
      StringList tokens = new StringList( dataTmp, DELIMITER )
      if (tokens.size > 0) {
          shipment = new Shipment()
          shipment.source = FileFormat.LUX
          shipment.ref = ""
          shipment.lineNbr = 1
          shipment.genre = ""//tokensHeader.entry(2);
          shipment.fullRef = ""
          shipment.code = ""
          shipment.siteFrom = NumberFormat.getInstance().parse(tokensHeader.entry(0))
      }
      for (int i = 0; i < tokens.size; i++) {
          if (tokens.entry(i).trim().length() > 0) {
              String[] articulo = tokens.entry(i).trim().split(",")
              String idArt = articulo[0]
            Articulo part = ServiceFactory.partMaster.obtenerArticulo( new Integer(articulo[0]), false )
              if( part != null ){
                  log.debug(part.articulo);
                  parsed = new ShipmentLine()
                  parsed.partCode = part.articulo
                  parsed.colorCode = part.codigoColor
                  parsed.qty = NumberFormat.getInstance().parse(articulo[1])
                  parsed.sku = part.id
                  parsed.barcode = part.idCb
                  parsed.colorDesc = part.descripcionColor
                  parsed.brand = part.marca
                  parsed.type = part.tipo
                  shipment.lines.add(parsed)
              } else {
                  parsed = new ShipmentLine()
                  parsed.sku = NumberFormat.getInstance().parse(articulo[0])
                  shipment.lines.add(parsed)
              }

          }
      }
     return shipment
  }

  protected ShipmentLine parseDetail( String pDetailLine ) {
    Shipment shipment = null
    ShipmentLine parsed = null
    StringList tokens = new StringList( pDetailLine, DELIMITER )
    if ( tokens.size >= DetFld.values().size() ) {
      parsed = new ShipmentLine()
      parsed.partCode = tokens.entry( DetFld.PartCode.ordinal() )
      parsed.colorCode = tokens.entry( DetFld.ColorCode.ordinal() )
      parsed.qty = tokens.asInteger( DetFld.QtyTrans.ordinal() )
      parsed.sku = tokens.asInteger( DetFld.Sku.ordinal() )
      parsed.barcode = tokens.entry( DetFld.BarCode.ordinal() )
      parsed.colorDesc = tokens.entry( DetFld.ColorDesc.ordinal() )
      parsed.brand = tokens.entry( DetFld.SubType.ordinal() )
      parsed.type = tokens.entry( DetFld.Type.ordinal() )
    }
    return parsed
  }

  protected Shipment parseHeader( String pHeaderLine ) {
    Shipment parsed = null
    StringList tokens = new StringList( pHeaderLine, DELIMITER )
    if ( tokens.size >= HdrFld.values().size() ) {
      parsed = new Shipment()
      parsed.source = FileFormat.LUX
      parsed.ref = tokens.entry( HdrFld.Ref.ordinal() )
      parsed.lineNbr = tokens.asInteger( HdrFld.LnCount.ordinal() )
      parsed.genre = tokens.entry( HdrFld.Fam.ordinal() )
      parsed.fullRef = tokens.entry( HdrFld.FullRef.ordinal() )
      parsed.code = tokens.entry( HdrFld.Code.ordinal() )
    }
    return parsed
  }


  // Public methods
  Shipment read( String pFilename ) {
    Shipment shipment = null
    File inFile = new File( pFilename )

    if ( inFile.exists() ) {
      try {
        Integer iLine = 0
        inFile.eachLine() {
          iLine++
          if ( iLine == 1 ) {
            shipment = parseHeader( it )
          } else {
            if ( shipment != null ) {
              ShipmentLine line = parseDetail( it )
              if ( line != null ) {
                shipment.lines.add( line )
              }
            }
          }
        }
      } catch ( Exception e ) {
        if ( log.debugEnabled ) {
          log.error( String.format( MSG_ERROR_READING_FILE, inFile.absolutePath ), e )
        } else {
          log.error( String.format( MSG_ERROR_READING_FILE, inFile.absolutePath ) )
        }
      }
    }
    return shipment
  }

  String write( TransInv pIssueTr ) {
    return new InvTrFile().write( pIssueTr )
  }

}

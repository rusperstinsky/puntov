package mx.lux.pos.service.io

import mx.lux.pos.service.business.Registry
import mx.lux.pos.service.impl.ServiceFactory
import mx.lux.pos.util.StringList
import mx.lux.pos.util.SunglassUtils
import mx.lux.pos.model.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ShippingNoticeFileSunglass extends ShippingNoticeFile {

  private static enum DetFld {
    Sku, Qty, fld_1, Price, fld_2, fld_3, fld_4, fld_5, fld_6, fld_7, Supplier, fld_8
  }
  private static enum HdrFld {
    MovType, MovCode, Supplier, SiteTo, TrDate, VAT, fld_1, fld_2, fld_3, fld_4, fld_5, fld_6, TrRef, Curr,
    UsdRate, fld_7
  }

  private static final String MOV_TYPE = "E"
  private static final String MOV_CODE = "07"
  private static final String SUPPLIER = "NINGUNO"
  private static final String DELIMITER = ","
  private static final String CURRENCY = "N"
  private static final Double USD_RATE = 13.0
  private static final Integer UNKNOWN = 0
  private static final String FMT_TR_DATE = "yyyy/MM/dd"

  private Logger logger = LoggerFactory.getLogger( this.getClass() )

  private String delimiter


  // Internal Methods
  String format( TransInv pIssueTr ) {
    StringBuffer sb = new StringBuffer()
    if ( InvTrType.ISSUE.equals( pIssueTr.idTipoTrans ) ) {
      sb.append( this.formatHeader( pIssueTr ) )
      for ( TransInvDetalle det : pIssueTr.trDet ) {
        sb.append( "\n" )
        sb.append( this.formatDetail( det ) )
      }
    }
    return sb.toString()
  }

  protected String formatDetail( TransInvDetalle pIssueDet ) {
    StringList det = new StringList()
    Articulo part = ServiceFactory.partMaster.obtenerArticulo( pIssueDet.sku )
    for ( DetFld fld : DetFld.values() ) {
      switch ( fld ) {
        case DetFld.Sku: det.addInteger( pIssueDet.sku, "%06d" ); break
        case DetFld.Qty: det.addDouble( pIssueDet.cantidad as Double, "%.6f" ); break
        case DetFld.Price: det.addDouble( part.precio, "%.6f" ); break
        case DetFld.Supplier: det.add( part.proveedor ); break
        case DetFld.fld_1: det.addDouble( UNKNOWN as Double, "%.6f" ); break
        case DetFld.fld_7:
        case DetFld.fld_8: det.addInteger( UNKNOWN ); break
        case DetFld.fld_2:
        case DetFld.fld_3:
        case DetFld.fld_4:
        case DetFld.fld_5:
        case DetFld.fld_6: det.add( " " ); break
      }
    }
    return det.toString( DELIMITER )
  }

  protected String formatHeader( TransInv pIssueTr ) {
    StringList hdr = new StringList()
    for ( HdrFld fld : HdrFld.values() ) {
      // TODO: (rld) Integrate USD Rate
      switch ( fld ) {
        case HdrFld.MovType: hdr.add( MOV_TYPE ); break
        case HdrFld.MovCode: hdr.add( MOV_CODE ); break
        case HdrFld.Supplier: hdr.add( SUPPLIER ); break
        case HdrFld.TrDate: hdr.addDate( pIssueTr.fecha, FMT_TR_DATE ); break
        case HdrFld.VAT: hdr.addDouble( Registry.currentVAT, "%.2f" ); break
        case HdrFld.Curr: hdr.add( CURRENCY ); break
        case HdrFld.UsdRate: hdr.addDouble( USD_RATE, "%,.4f" ); break
        case HdrFld.fld_1:
        case HdrFld.fld_2:
        case HdrFld.fld_3:
        case HdrFld.fld_4:
        case HdrFld.fld_5:
        case HdrFld.fld_6:
        case HdrFld.fld_7: hdr.add( "" ); break
        case HdrFld.TrRef:
          hdr.add( String.format( "%s%06d", pIssueTr.idTipoTrans.substring( 0, 1 ), pIssueTr.folio ) )
          break
        case HdrFld.SiteTo:
          if ( pIssueTr.sucursalDestino != null ) {
            hdr.add( SunglassUtils.formatSite( pIssueTr.sucursalDestino ) )
          } else {
            hdr.add( " " )
          }
          break
      }
    }
    return hdr.toString( DELIMITER )
  }

  protected File getIssueFile( TransInv pIssueTr ) {
    String filename = String.format( "%s.%s.%d.txt", SunglassUtils.formatSite( pIssueTr.sucursalDestino ),
        pIssueTr.idTipoTrans.substring( 0, 1 ), pIssueTr.folio )
    String absolutePath = String.format( "%s%s%s", this.location, File.separator, filename )
    return new File( absolutePath )
  }

  protected Boolean hasTaggedFields( StringList pList ) {
    Boolean result = true
    if ( result )
      result &= MOV_TYPE.equalsIgnoreCase( pList.entry( HdrFld.MovType.ordinal() ) )
    if ( result )
      result &= MOV_CODE.equalsIgnoreCase( pList.entry( HdrFld.MovCode.ordinal() ) )
    if ( result )
      result &= SUPPLIER.equalsIgnoreCase( pList.entry( HdrFld.Supplier.ordinal() ) )
    return result
  }

  protected ShipmentLine parseDetail( String pDetailLine ) {
    ShipmentLine parsed = null
    StringList tokens = new StringList( pDetailLine, DELIMITER )
    if ( tokens.size >= DetFld.values().size() ) {
      parsed = new ShipmentLine()
      parsed.sku = tokens.asInteger( DetFld.Sku.ordinal() )
      parsed.qty = Math.round( tokens.asDouble( DetFld.Qty.ordinal() ) )
    }
    return parsed
  }


  protected Shipment parseHeader( String pHeaderLine ) {
    Shipment parsed = null
    StringList tokens = new StringList( pHeaderLine, DELIMITER )
    if ( tokens.size >= HdrFld.values().size() && hasTaggedFields( tokens ) ) {
      parsed = new Shipment()
      parsed.source = FileFormat.LUX
      parsed.ref = tokens.entry( HdrFld.TrRef.ordinal() )
      parsed.fullRef = tokens.entry( HdrFld.TrRef.ordinal() )
      parsed.siteTo = SunglassUtils.parseSite( tokens.entry( HdrFld.SiteTo.ordinal() ) )
      parsed.trDate = tokens.asDate( HdrFld.TrDate.ordinal(), FMT_TR_DATE )
    }
    return parsed
  }


  // Public methods
  String write( TransInv pIssueTr ) {
    File file = this.getIssueFile( pIssueTr )
    PrintStream strOut = new PrintStream( file )
    strOut.println this.format( pIssueTr )
    strOut.close()
    logger.debug( String.format( 'Generando archivo de salida (MiEmpresa): %s', file.absolutePath ) )
    return file.absolutePath
  }

}

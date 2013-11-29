package mx.lux.pos.service.io

import mx.lux.pos.service.business.ResourceManager
import mx.lux.pos.service.impl.ServiceFactory
import mx.lux.pos.util.CustomDateUtils
import mx.lux.pos.util.StringList
import org.apache.commons.lang3.StringUtils
import mx.lux.pos.model.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class InvTrFile {

  private static enum DetFld {
    Site, TrType, TrNbr, LineNum, Sku, MovType, Qty
  }
  private static enum HdrFld {
    Site, TrType, TrNbr, TrDate, SiteTo, Ref, LineNum, Remarks
  }
  private static final String DELIMITER = "|"
  private static final String FMT_TR_DATE = "dd/MM/yyyy"

  private Logger logger = LoggerFactory.getLogger( this.getClass() )
  private String delimiter

  // Internal Methods
  protected Boolean canFindSku( Integer pSku ) {
    Articulo part = ServiceFactory.partMaster.obtenerArticulo( pSku, false )
    return ( part != null )
  }

  protected Boolean canFindTrType( String pTrType ) {
    TipoTransInv trType = ServiceFactory.inventory.obtenerTipoTransaccion( pTrType )
    return ( trType != null )
  }

  String format( TransInv pInvTr ) {
    StringBuffer sb = new StringBuffer()
    sb.append( this.formatHeader( pInvTr ) )
    int iLine = 0
    for ( TransInvDetalle det : pInvTr.trDet ) {
      iLine++
      sb.append( "\n" )
      sb.append( this.formatDetail( pInvTr, iLine, det ) )
    }
    return sb.toString()
  }

  protected String formatDetail( TransInv pInvTr, Integer pLineNum, TransInvDetalle pTrDet ) {
    StringList det = new StringList()
    for ( DetFld fld : DetFld.values() ) {
      switch ( fld ) {
        case DetFld.Site: det.add( String.format( "%04d", pInvTr.sucursal ) ); break
        case DetFld.TrType: det.add( pInvTr.idTipoTrans ); break
        case DetFld.TrNbr: det.addInteger( pInvTr.folio, "%08d" ); break
        case DetFld.LineNum: det.addInteger( pLineNum ); break
        case DetFld.Sku: det.addInteger( pTrDet.sku ); break
        case DetFld.MovType: det.add( pTrDet.tipoMov ); break
        case DetFld.Qty: det.addInteger( pTrDet.cantidad ); break
      }
    }
    det.add( "" )
    return det.toString( this.getDelimiter() )
  }

  protected String formatHeader( TransInv pInvTr ) {
    StringList hdr = new StringList()
    for ( HdrFld fld : HdrFld.values() ) {
      switch ( fld ) {
        case HdrFld.Site: hdr.add( String.format( "%04d", pInvTr.sucursal ) ); break
        case HdrFld.TrType: hdr.add( pInvTr.idTipoTrans ); break
        case HdrFld.TrNbr: hdr.addInteger( pInvTr.folio, "%08d" ); break
        case HdrFld.TrDate: hdr.addDate( pInvTr.fecha, FMT_TR_DATE ); break
        case HdrFld.LineNum: hdr.addInteger( pInvTr.trDet.size() ); break
        case HdrFld.Remarks: hdr.add( pInvTr.observaciones ); break
        case HdrFld.Ref: hdr.add( pInvTr.referencia ); break
        case HdrFld.SiteTo:
          if ( pInvTr.sucursalDestino != null ) {
            hdr.add( String.format( "%04d", pInvTr.sucursalDestino ) )
          } else {
            hdr.add( " " )
          }
          break
      }
    }
    hdr.add( "" )
    return hdr.toString( this.getDelimiter() )
  }

  protected String getDelimiter( ) {
    if ( this.delimiter == null ) {
      this.delimiter = DELIMITER
    }
    return this.delimiter
  }

  protected File getInvTrFile( TransInv pInvTr ) {
    String filename = String.format( "%s.%d.%04d.%s.inv", pInvTr.idTipoTrans, pInvTr.folio, pInvTr.sucursal,
        CustomDateUtils.format( pInvTr.fecha, "dd-MM-yyyy" ) )
    String absolutePath = String.format( "%s%s%s", this.location, File.separator, filename )
    return new File( absolutePath )
  }

  protected String getLocation( ) {
    return ResourceManager.getLocation( TipoParametro.RUTA_INVENTARIO ).absolutePath
  }

  TransInv parse( List<String> pInputLines ) {
    TransInv tr = null
    int iLine = 0
    for ( String line : pInputLines ) {
      iLine++
      if ( iLine == 1 ) {
        tr = parseHeader( line )
      } else {
        if ( tr != null ) {
          TransInvDetalle trDet = parseDetail( line )
          if ( trDet != null ) {
            tr.add( trDet )
          }
        }
      }
    }
    return tr
  }

  TransInvDetalle parseDetail( String pDetailLine ) {
    TransInvDetalle parsed = null
    StringList list = new StringList( pDetailLine, this.getDelimiter() )
    if ( list.size >= DetFld.values().size() && canFindSku( list.asInteger( DetFld.Sku.ordinal() ) ) ) {
      parsed = new TransInvDetalle()
      parsed.idTipoTrans = list.entry( DetFld.TrType.ordinal() )
      parsed.folio = list.asInteger( DetFld.TrNbr.ordinal() )
      parsed.linea = list.asInteger( DetFld.LineNum.ordinal() )
      parsed.sku = list.asInteger( DetFld.Sku.ordinal() )
      parsed.tipoMov = list.entry( DetFld.MovType.ordinal() )
      parsed.cantidad = list.asInteger( DetFld.Qty.ordinal() )
    }
    return parsed
  }

  TransInv parseHeader( String pHeaderLine ) {
    TransInv parsed = null
    StringList list = new StringList( pHeaderLine, this.getDelimiter() )
    if ( list.size >= HdrFld.values().size() && canFindTrType( list.entry( HdrFld.TrType.ordinal() ) ) ) {
      parsed = new TransInv()
      parsed.idTipoTrans = list.entry( HdrFld.TrType.ordinal() )
      parsed.folio = list.asInteger( HdrFld.TrNbr.ordinal() )
      parsed.sucursal = list.asInteger( HdrFld.Site.ordinal() )
      parsed.fecha = list.asDate( HdrFld.TrDate.ordinal(), FMT_TR_DATE )
      parsed.observaciones = list.entry( HdrFld.Remarks.ordinal() )
      parsed.referencia = list.entry( HdrFld.Ref.ordinal() )
      if ( StringUtils.trimToNull( list.entry( HdrFld.SiteTo.ordinal() ) ) != null ) {
        parsed.sucursalDestino = list.asInteger( HdrFld.SiteTo.ordinal() )
      }
    }
    return parsed
  }

  protected void setDelimiter( String pDelimiter ) {
    this.delimiter = pDelimiter
  }

  // Public Methods
  Boolean identify( String pHeaderLine ) {
    return ( this.parseHeader( pHeaderLine ) != null )
  }

  InvTrFile read( String pFilename ) {
    InvTrFile tr = null
    return tr
  }

  String write( TransInv pInvTr ) {
    File file = this.getInvTrFile( pInvTr )
    PrintStream strOut = new PrintStream( file )
    strOut.println this.format( pInvTr )
    strOut.close()
    logger.debug( String.format( 'Transaction written to: %s', file.absolutePath ) )
    return file.absolutePath
  }
}

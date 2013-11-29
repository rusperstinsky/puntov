package mx.lux.pos.ui.model.adapter

import mx.lux.pos.model.Articulo
import mx.lux.pos.model.TransInv
import mx.lux.pos.model.TransInvDetalle
import mx.lux.pos.ui.model.InvTr
import mx.lux.pos.ui.model.User
import mx.lux.pos.ui.resources.ServiceManager
import org.apache.commons.lang3.StringUtils

import java.text.DateFormat
import java.text.SimpleDateFormat

class InvTrAdapter extends StringAdapter {

  static final String FLD_TODAY = "Today"
  private static final String FLD_TR_EFF_DATE = "InvTr.EffDate"
  private static final String FLD_TR_PART_LIST = "InvTr.[Part.PartCode]"
  private static final String FLD_TR_NBR = "InvTr.TrNbr"

  DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )

  String getText( Object pObject ) {
    String text = super.getText( pObject )
    if ( pObject instanceof Date ) {
      text = df.format( ( Date ) pObject )
    } else if ( pObject instanceof User ) {
      text = getUserToString( pObject as User )
    }
    return text
  }

  String getText( Object pObject, String pField ) {
    String text = super.getText( pObject, pField )
    if ( pObject instanceof InvTr ) {
      InvTr data = ( InvTr ) pObject
      if ( pField.equals( FLD_TODAY ) ) {
        text = df.format( data.today )
      }
    } else if ( pObject instanceof TransInv ) {
      TransInv tr = ( TransInv ) pObject
      if ( pField.equals( FLD_TR_EFF_DATE ) ) {
        text = df.format( tr.fecha )
      } else if ( pField.equals( FLD_TR_NBR ) ) {
        text = String.format( "%s.%06d", tr.idTipoTrans, tr.folio )
      } else if ( pField.equals( FLD_TR_PART_LIST ) ) {
        text = getPartList( tr )
      }
    }
    return text
  }

  private String getPartList( TransInv pTransInv ) {
    String partCodes = ""
    List<Integer> skuList = new ArrayList<Integer>( pTransInv.trDet.size() )
    for ( TransInvDetalle det in pTransInv.trDet ) {
      if ( !skuList.contains( det.sku ) ) {
        skuList.add( det.sku )
      }
    }
    if ( skuList.size() > 0 ) {
      List<Articulo> partList = ServiceManager.partService.obtenerListaArticulosPorId( skuList )
      for ( Articulo part in partList ) {
        partCodes += ( partCodes.length() == 0 ? "" : ", " ) + part.articulo
      }
    }
    return partCodes
  }

  String getUserToString( User pUser ) {
    String str = String.format( "[%s] %s", StringUtils.trimToEmpty( pUser.username ), pUser.fullName )
    return str
  }
}

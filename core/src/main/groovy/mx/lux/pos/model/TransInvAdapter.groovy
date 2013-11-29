package mx.lux.pos.model

import org.apache.commons.lang3.StringUtils

import java.text.DateFormat
import java.text.SimpleDateFormat
import mx.lux.pos.util.StringList

@Singleton
class TransInvAdapter {
  static final String REF_DELIMITER = ' | '
  static final String FLD_TR_EFF_DATE = "TransInv.effDate"
  static final String FLD_TR_NBR = "TransInv.TrNbr"
  static final String FLD_SRC_TICKET = "TransInv.TrRef.Ticket"
  static final String FLD_SALES_PERSON = "TransInv.TrRef.SalesPerson"
  static final String FLD_RETURN_AMOUNT = "TransInv.TrRef.ReturnAmount"
  static final String FLD_TRD_QTY = "TransInvDetalle.qty"
  static final String FLD_TRD_SKU = "TransInvDetalle.sku"
  static final String FLD_PART_CODE = "Articulo.articulo"
  static final String FLD_PART_COLOR_CODE = "Articulo.codigoColor"
  static final String FLD_PART_CODE_PLUS_COLOR = "Articulo.codigoAndColor"
  static final String FLD_PART_PRICE = "Articulo.precio"
  static final String FLD_PART_DESC = "Articulo.desciption"
  
  static final DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )
  String getText( Object pObject ) {
    String text = ""
    if ( pObject instanceof Empleado ) {
      Empleado emp = pObject as Empleado
      String nombre = StringUtils.trimToNull( emp.nombre )
      String apellidoPaterno = StringUtils.trimToNull( emp.apellidoPaterno )
      String apellidoMaterno = StringUtils.trimToNull( emp.apellidoMaterno )
      text = String.format( "[%s]%s%s%s", StringUtils.trimToEmpty( emp.id ),
          ( nombre != null ? " " + nombre : "" ), ( apellidoPaterno != null ? " " + apellidoPaterno : "" ),
          ( apellidoMaterno != null ? " " + apellidoMaterno : "" )
      )
    } else if ( pObject instanceof Sucursal ) {
      Sucursal suc = pObject as Sucursal
      String desc = StringUtils.trimToNull( suc.nombre )
      text = String.format( "%s(%d)", ( desc != null ? desc + " " : "" ), suc.id ) 
    }
    return text 
  }
  
  String getText( Object pObject, String pField ) {
    String text = ""
    if ( pObject instanceof TransInv ) {
      TransInv tr = pObject as TransInv
      if ( pField.equalsIgnoreCase( FLD_TR_EFF_DATE ) ) {
        text = df.format( tr.fecha )
      } else if ( pField.equals( FLD_TR_NBR ) ) {
        text = String.format( "%s.%06d", tr.idTipoTrans, tr.folio )
      } else if ( pField.equals( FLD_SRC_TICKET ) ) {
        StringList ref = new StringList( tr.referencia, TransInvAdapter.REF_DELIMITER.trim() )
        text = String.format( "%s", ref.entry(0) )
      } else if ( pField.equals( FLD_SALES_PERSON ) ) {
        StringList ref = new StringList( tr.referencia, TransInvAdapter.REF_DELIMITER.trim() )
        text = String.format( "%s", ref.entry(2) )
      } else if ( pField.equals( FLD_RETURN_AMOUNT ) ) {
        StringList ref = new StringList( tr.referencia, TransInvAdapter.REF_DELIMITER.trim() )
        text = String.format( "%s", ref.entry(1) )
      }
    } else if ( pObject instanceof TransInvDetalle ) {
      TransInvDetalle trd = pObject as TransInvDetalle
      if ( pField.equalsIgnoreCase( FLD_TRD_SKU ) ) {
        text = String.format( "%d      ", trd.sku )
        text = text.substring(0, 6)
      } else if ( pField.equals( FLD_TRD_QTY ) ) {
        text = String.format( "%,d", trd.cantidad )
      }
    } else if ( pObject instanceof Articulo ) {
      Articulo part = pObject as Articulo
      if ( pField.equalsIgnoreCase( FLD_PART_CODE ) ) {
        text = StringUtils.trimToEmpty( part.articulo )
      } else if ( pField.equals( FLD_PART_COLOR_CODE ) ) {
        text = StringUtils.trimToEmpty( part.codigoColor )
      } else if ( pField.equals( FLD_PART_CODE_PLUS_COLOR ) ) {
        text = String.format( '%s %s', StringUtils.trimToEmpty( part.articulo ),
            StringUtils.trimToEmpty( part.codigoColor ) )
        if ( text.length() > 32 ) {
          text = text.substring( 0, 32 )
        }
      } else if ( pField.equals( FLD_PART_PRICE ) ) {
        text = String.format( '$%,.2f', part.precio )
      } else if ( pField.equals( FLD_PART_DESC ) ) {
        text = StringUtils.trimToEmpty( part.descripcion )
        if (text.length() > 32) {
          text = text.substring(0, 32)
        }
      }
    }
    return text
  }
  
  List<String> split( String pString, Integer pMaxLength) {
    List<String> list = new ArrayList<String>()
    String[] words = pString.trim().split( " " )
    String curr = ""
    for ( String word in words ) {
      String w = word.trim( )
      if ( ( curr + " " + w ).length( ) > pMaxLength ) {
        list.add( curr )
        curr = w.trim()
      } else {
        curr += ( curr.length() == 0 ? "" : " " ) + w
      }
    }
    if ( curr.length() > 0 ) {
      list.add( curr )
    }
    return list
  }
}

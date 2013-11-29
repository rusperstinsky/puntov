package mx.lux.pos.ui.model.adapter

import mx.lux.pos.model.Articulo
import mx.lux.pos.model.TransInv
import mx.lux.pos.model.TransInvDetalle
import mx.lux.pos.ui.resources.ServiceManager
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils
import org.apache.commons.lang3.time.DateUtils

class InvTrFilter extends Filter<TransInv> {

  InvTrAdapter adapter = new InvTrAdapter()
  Date dateFrom, dateTo
  String trType, partCode
  Integer sku, siteTo

  // Public Methods
  Boolean isDateRangeActive() {
    return ( ( dateFrom != null ) && ( dateTo != null ) )
  }
  
  Boolean isPartCodeActive() {
    return ( partCode != null )
  }

  Boolean isSiteToActive() {
    return ( siteTo != null )
  }

  Boolean isSkuActive() {
    return ( sku != null )
  }

  Boolean isTrTypeActive() {
    return ( trType != null )
  }

  void reset() {
    dateFrom = null
    dateTo = null
    trType = null
    partCode = null
    sku = null
    siteTo = null
  }
  
  void resetDateRange( ) {
    this.dateFrom = null
    this.dateTo = null
  }
  void setDateRange( Date pDate ) {
    setDateRange( pDate, pDate )
  }
  void setDateRange( Date pDateFrom, Date pDateTo) {
    this.dateFrom = DateUtils.truncate( pDateFrom, Calendar.DATE )
    this.dateTo = DateUtils.truncate( pDateTo, Calendar.DATE )
  }
  
  void setPartCode( String pPartCode ) {
    partCode = StringUtils.trimToNull( pPartCode.trim( ).toUpperCase( ) )
  }

  void setSiteTo( String pSiteValue ) {
    String value = StringUtils.trimToNull( pSiteValue )
    if ( value != null) {
      try {
        siteTo = NumberUtils.createInteger( value )
      } catch (Exception e) {
        println String.format( "[Filter] SiteTo:<%s> Error:<%s> ", value, e.getMessage() )
      }
    }
  }

  void setSku( String pSkuValue ) {
    String value = StringUtils.trimToNull( pSkuValue )
    if ( value != null) {
      try {
        sku = NumberUtils.createInteger( value )
      } catch (Exception e) {
        println String.format( "[Filter] Sku:<%s> Error:<%s> ", value, e.getMessage() )
      }
    }
  }

  void setTrType( String pTrType ) {
    trType = StringUtils.trimToNull( pTrType.trim( ).toUpperCase( ) )
  }
  
  String toString() {
    String str = "[Filter] "
    if ( isDateRangeActive( ) )
      str += String.format( "DateRange:<%s - %s>", adapter.getText( dateFrom ), adapter.getText( dateTo ) )
    if ( isTrTypeActive( ) ) str += String.format(  "TrType:<%s>", trType )
    if ( isSiteToActive( ) ) str += String.format(  "SiteTo:<%d>", siteTo )
    if ( isSkuActive( ) ) str += String.format(  "TrType:<%d>", sku )
    if ( isPartCodeActive( ) ) str += String.format(  "TrType:<%s*>", partCode )
    return str
  }
  
  // Filter Methods
  Boolean select( TransInv pInvTr ) {
    Boolean selected = true
    if ( selected && isDateRangeActive( ) ) {
      selected = ( ( dateFrom.compareTo( pInvTr.fecha ) <= 0 )
                && ( dateTo.compareTo( pInvTr.fecha ) >= 0 ) )
    }
    if ( selected && this.isTrTypeActive( ) ) {
      selected = ( pInvTr.idTipoTrans.equalsIgnoreCase( trType ) )
    }
    if ( selected && this.isSiteToActive( ) ) {
      selected = ( siteTo.equals( pInvTr.sucursalDestino ) )
    }
    if ( selected && this.isSkuActive( ) ) {
      Boolean found = false
      for ( TransInvDetalle trDet : pInvTr.trDet ) {
        if ( sku.equals( trDet.sku ) ) {
          found = true
          break
        }
      }
      selected = found
    }
    if ( selected && isPartCodeActive( ) ) {
      Boolean found = false
      for ( TransInvDetalle trDet : pInvTr.trDet ) {
        Articulo part = ServiceManager.partService.obtenerArticulo( trDet.sku, false )
        if (part.articulo.toUpperCase().startsWith( partCode ) ) {
          found = true
          break
        }
      }
      selected = found
    }
    return selected
  }

}

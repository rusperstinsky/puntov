package mx.lux.pos.model

import mx.lux.pos.service.business.PromotionCommit
import org.apache.commons.lang3.StringUtils

class OrdenPromList {

  private static final Double ZERO_TOLERANCE = PromotionModel.ZERO_TOLERANCE
  
  List<OrdenProm> list = new ArrayList<OrdenProm>( )
  Integer siteNbr
  
  OrdenPromList( Integer pSiteNbr ) {
    this.setSiteNbr( pSiteNbr )
  }

  OrdenPromList( OrdenPromDetList pOrdenPromDetList ) {
    this( pOrdenPromDetList.siteNbr )
    this.loadFromOrdenPromDetList( pOrdenPromDetList )
  }
  
  // Internal Methods
  protected static final BigDecimal asAmount( Double pDoubleValue ) {
    return PromotionCommit.asAmount( pDoubleValue )
  }
  protected static final BigDecimal asPercent( Double pDoubleValue ) {
    return PromotionCommit.asPercent( pDoubleValue )
  }
  
  protected OrdenProm find( String pOrderNbr, Integer pPromotionId ) {
    OrdenProm found = null
    for ( OrdenProm prom : this.list ) {
      if ( prom.equals( pOrderNbr, pPromotionId ) ) {
        found = prom
        break
      }
    }
    return found
  }
  
  // Public Methods
  OrdenProm add( String pOrderNbr, Integer pPromotionId ) {
    OrdenProm prom = this.find( pOrderNbr, pPromotionId )
    if ( prom == null ) {
      prom = new OrdenProm( )
      prom.idFactura = StringUtils.trimToEmpty( pOrderNbr ).toUpperCase( )
      prom.idPromocion = pPromotionId
      prom.idSucursal = 0
      prom.totalDescMonto = BigDecimal.ZERO
      this.list.add( prom )    
    }  
    return prom
  }
  
  void loadFromOrdenPromDetList( OrdenPromDetList pOrdenPromDetList ) {
    for ( OrdenPromDet det : pOrdenPromDetList.list ) {
      OrdenProm prom = this.add( det.idFactura, det.idPromocion )
      prom.idSucursal = this.siteNbr
      prom.totalDescMonto = asAmount( prom.totalDescMonto.doubleValue( ) + det.descuentoMonto.doubleValue( ) )
    }
  }
  
  void setSiteNbr( Integer pSiteNbr ) {
    this.siteNbr = pSiteNbr
    for ( OrdenProm prom : this.list ) {
      prom.idSucursal = this.siteNbr
    }
  }
  
  String toString( ) {
    StringBuffer sb = new StringBuffer( )
    sb.append( String.format( "[%s] %d elements", this.getClass( ).getSimpleName( ), this.list.size( ) ) )
    for ( OrdenProm prom : this.list ) {
      sb.append( String.format( "\n    %s", prom.toString( ) ) )
    }
    return sb.toString( )
  }

}

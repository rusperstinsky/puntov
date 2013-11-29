package mx.lux.pos.model

import mx.lux.pos.service.business.PromotionCommit
import org.apache.commons.lang3.StringUtils

import java.math.MathContext
import java.math.RoundingMode

class OrdenPromDetList {

  private static final Double ZERO_TOLERANCE = PromotionModel.ZERO_TOLERANCE
  private static final MathContext AS_PERCENT = new MathContext( 3, RoundingMode.HALF_UP )

  List<OrdenPromDet> list = new ArrayList<OrdenPromDet>( )
  Integer siteNbr
  
  OrdenPromDetList( Integer pSiteNbr ) {
    this.setSiteNbr( pSiteNbr )
  }

  OrdenPromDetList( Integer pSiteNbr, PromotionModel pModel ) {
    this( pSiteNbr )
    this.loadFromModel( pModel )
  }
  
  // Internal Methods
  protected static final BigDecimal asAmount( Double pDoubleValue ) {
    return PromotionCommit.asAmount( pDoubleValue )
  }
  protected static final BigDecimal asPercent( Double pDoubleValue ) {
    return PromotionCommit.asPercent( pDoubleValue )
  }

  protected OrdenPromDet find( String pOrderNbr, Integer pSku ) {
    OrdenPromDet found = null
    for ( OrdenPromDet det : this.list ) {
      if ( det.equals( pOrderNbr, pSku) ) {
        found = det
        break
      }
    }
    return found  
  }
  
  // Public Methods
  OrdenPromDet add( String pOrderNbr, Integer pSku ) {
    OrdenPromDet det = find( pOrderNbr, pSku )
    if ( det == null ) {
      det = new OrdenPromDet( ) 
      det.idFactura = StringUtils.trimToEmpty( pOrderNbr ).toUpperCase( )
      det.idArticulo = pSku
      det.idPromocion = 0
      det.idSucursal = 0
      det.precioBase = BigDecimal.ZERO
      det.descuentoMonto = BigDecimal.ZERO
      det.descuentoPorcentaje = BigDecimal.ZERO
      this.list.add( det )
    } 
    return det 
  }
  
  void loadFromModel( PromotionModel pModel ) {
    this.list.clear( )
    for ( PromotionAvailable promotion : pModel.listAvailablePromotions( ) ) {
      if ( promotion.applied ) {
        for ( PromotionApplied promotionLine : promotion.appliesToList ) {
          OrdenPromDet promDet = this.add( pModel.order.orderNbr, promotionLine.sku )
          promDet.idSucursal = this.siteNbr
          promDet.idPromocion = promotion.idPromotion
          promDet.precioBase = asAmount( promDet.precioBase.doubleValue( ) +  promotionLine.baseAmount )
          promDet.descuentoMonto = asAmount( promDet.descuentoMonto.doubleValue( ) + promotionLine.discountAmount )
          if ( Math.abs( promDet.precioBase.doubleValue( ) ) > ZERO_TOLERANCE ) {
            Double percent = ( 100.0 * promDet.descuentoMonto.doubleValue( ) ) /  promDet.precioBase.doubleValue( )  
            promDet.descuentoPorcentaje = asPercent( percent )
          } else {
            promDet.descuentoPorcentaje = BigDecimal.ZERO
          }
        }
      }
    }
  }
  
  void setRelation( List<OrdenProm> pOrdenPromList ) {
    for ( OrdenProm op : pOrdenPromList ) {
      for ( OrdenPromDet opd : this.list ) {
        if ( opd.idFactura.equals( op.idFactura ) &&  opd.idPromocion.equals( op.idPromocion ) ) {
          opd.idOrdenProm = op.id
        }
      }
    }
  }
  
  void setSiteNbr( Integer pSiteNbr ) {
    this.siteNbr = pSiteNbr
    for ( OrdenPromDet orderPromLine : list ) {
      orderPromLine.idSucursal = this.siteNbr
    }
  }

  String toString( ) {
    StringBuffer sb = new StringBuffer( )
    sb.append( String.format( "[%s] %d elements", this.getClass( ).getSimpleName( ), this.list.size( ) ) )
    for ( OrdenPromDet det : this.list ) {
      sb.append( String.format( "\n    %s", det.toString( ) ) )
    }
    return sb.toString( ) 
  }
}

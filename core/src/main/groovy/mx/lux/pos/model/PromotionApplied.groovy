package mx.lux.pos.model

class PromotionApplied {
  
  private static final Double ZERO_TOLERANCE = PromotionModel.ZERO_TOLERANCE
  
  PromotionOrderDetail orderDetail
  Boolean applied
  Boolean pricingRequired
  Double basePrice
  Double promotionPrice
  
  PromotionApplied( PromotionOrderDetail pDetail ) {
    applied = false
    basePrice = 0.0
    promotionPrice = 0.0
    pricingRequired = true
    this.setOrderDetail( pDetail )
  }
  
  // Internal Methods
  protected void setOrderDetail( PromotionOrderDetail pDetail ) {
    this.orderDetail = pDetail
  }

  // Public methods
  Double getBaseAmount( ) {
    return ( this.orderDetail.qtyOrder * this.basePrice )  
  }

  Double getDiscountAmount( ) {
    return ( this.baseAmount - this.promotionAmount )  
  }  

  Double getDiscountPercent( ) {
    Double percent = 0
    if ( Math.abs( this.baseAmount ) > ZERO_TOLERANCE ) {
      percent = this.discountAmount / this.baseAmount  
    }
    return percent
  }
  
  String getPartNbr( ) {
    return this.orderDetail.partNbr
  }
  
  Double getPromotionAmount( ) {
    return ( this.orderDetail.qtyOrder * this.promotionPrice )  
  }  

  Double getRegularPrice( ) {
    return this.orderDetail.regularPrice
  }
  
  Integer getSku( ) {
    return this.orderDetail.sku
  }
  
  Boolean isPricingRequired( ) {
    return pricingRequired  
  }
  
  void setPricing( Pricing pPricing ) {
    if ( this.sku.equals( pPricing.sku ) ) {
      
    }
  }
  // Entity
  int compareTo( PromotionApplied pPromotionApplied ) {
    return this.orderDetail.sku.compareTo( pPromotionApplied.orderDetail.sku )
  }
  
  boolean equals( Object pObj ) {
    boolean result = false
    if ( pObj instanceof PromotionApplied ) {
      result = this.orderDetail.equals( ( pObj as PromotionApplied ).orderDetail ) 
    } else if ( pObj instanceof PromotionOrderDetail ) {
      result = this.orderDetail.equals( pObj as PromotionOrderDetail )
    } else if ( pObj instanceof Articulo ) {
      result = this.sku.equals( ( pObj as Articulo ).id )
    } else if ( pObj instanceof Integer ) {
      result = this.sku.equals( pObj as Integer )
    }
    return result
  }
  
  int hashCode( ) {
    return this.sku.hashCode( )
  }
  
  String toString( ) {
    String str = String.format( "[%d] %s  RegularPrice:%,.2f  BasePrice:%,.2f  PromotionPrice:%,.2f(%,.1f%%)", 
      this.sku, this.partNbr, this.regularPrice, this.basePrice, this.promotionPrice, 100 * this.discountPercent )
  }
}

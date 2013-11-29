package mx.lux.pos.model

import org.apache.commons.lang3.StringUtils

class PromotionOrderDetail implements Comparable<PromotionOrderDetail> {
  
  private static final Double ZERO_TOLERANCE = PromotionModel.ZERO_TOLERANCE
  
  Integer sku
  String partNbr
  Integer qtyOrder
  Double regularPrice
  Double orderDiscountPercent
  PromotionApplied promotion
  
  protected PromotionOrderDetail( ) {
    this.qtyOrder = 0
    this.regularPrice = 0
    this.orderDiscountPercent = 0
    this.setPartNbr( "" )
    this.setPromotion( null )
  }
  PromotionOrderDetail( Integer pSku ) {
    this( )
    this.setSku( pSku )
  }
  PromotionOrderDetail( DetalleNotaVenta pOrderLine ) {
    this( pOrderLine.idArticulo )
    this.assign( pOrderLine )
  }
  
  // Internal Methods
  protected void setPromotion( PromotionApplied pPromotion ) {
    this.promotion = pPromotion
  }
  
  protected void setSku( Integer pSku ) {
    this.sku = pSku
  }
  
  // Public Methods
  void apply( PromotionApplied pPromotion ) {
    this.setPromotion( pPromotion )
  }
  
  void assign( DetalleNotaVenta pOrderLine ) {
    if ( this.equals( pOrderLine ) ) {
      if ( isPartRequired( ) && ( pOrderLine.articulo != null ) ) {
        this.setPartNbr( pOrderLine.articulo.articulo ) 
      }
      this.qtyOrder = pOrderLine.cantidadFac
      this.regularPrice = pOrderLine.precioUnitLista
    }
  }

  Double getPromotionPrice( ) {
    Double price = 0
    if ( this.hasPromotionApplied( ) ) {
      price = this.promotion.promotionPrice
    }
    return price
  }
  
  Double getRegularAmount( ) {
    return this.qtyOrder * this.regularPrice 
  }
  
  Double getFinalAmount( ) {
    return this.qtyOrder * this.finalPrice 
  }
   
  Double getFinalDiscountAmount( ) {
    return this.getRegularAmount( ) - this.getFinalAmount( )
  }
  
  Double getFinalDiscountPercent( ) {
    Double discount = 0
    if ( this.regularAmount > 0 ) {
      discount = 1 - ( this.finalAmount / this.regularAmount )
    }  
    return discount
  }
  
  Double getFinalPrice() {
    Double price = regularPrice
    if ( this.hasPromotionApplied( ) ) {
      price = this.promotionPrice
    }
    if ( this.hasOrderDiscountApplied( ) ) {
      price = this.getOrderDiscountPrice( )
    }
    return price
  }
  
  Double getOrderDiscountPrice( ) {
    Double price = 0.0
    if ( this.hasOrderDiscountApplied( ) ) {
      price = ( this.hasPromotionApplied( ) ? this.promotionPrice : this.regularPrice ) * 
          ( 1 - this.orderDiscountPercent )
    }
    return price
  }
  
  Double getOrderDiscountBaseAmount( ) {
    Double price = regularPrice  
    if ( this.hasPromotionApplied( ) ) {
      price = this.promotionPrice
    }
    return ( this.qtyOrder * price )
  }
  
  Double getOrderDiscountAmount( ) {
    return this.orderDiscountBaseAmount - this.finalAmount 
  }
    
  Boolean hasOrderDiscountApplied( ) {
    return ( Math.abs( this.orderDiscountPercent ) > ZERO_TOLERANCE )  
  }
  
  Boolean hasPromotionApplied( ) {
    return ( this.promotion != null )  
  }
  
  Boolean isPartRequired( ) {
    return ( this.partNbr.length( ) == 0 )
  }
  
  void setPartNbr( String pPartNbr ) {
    this.partNbr = StringUtils.trimToEmpty( pPartNbr ).toUpperCase( )
  }
  
  // Entity
  int compareTo( PromotionOrderDetail pOrderDetail ) {
    return this.partNbr.compareTo( pOrderDetail.partNbr )
  }
  
  boolean equals( Object pObj ) {
    boolean result = false
    if ( pObj instanceof PromotionOrderDetail ) {
      result = this.sku.equals( ( pObj as PromotionOrderDetail ).sku )
    } else if ( pObj instanceof DetalleNotaVenta ) {
      result = this.sku.equals( ( pObj as DetalleNotaVenta ).idArticulo )
    } else if ( pObj instanceof Integer ) {
      result = this.sku.equals( pObj as Integer )
    }
    return result
  }
  
  public int hashCode( ) {
    return this.sku.hashCode( )
  }
  
  public String toString( ) {
    // Include Promotion & Discount
    String str = String.format( "Sku:%d  Part:%s  QtyOrder:%d  RegPrice:%,.2f  FinalPrice:%,.2f", 
      this.sku, this.partNbr, this.qtyOrder, this.regularPrice, this.finalPrice, ( this.finalDiscountPercent * 100.0 ) )
    return str
  }
}

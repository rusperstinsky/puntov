package mx.lux.pos.model

class Pricing {
  
  protected static final ZERO_TOLERANCE = PromotionModel.ZERO_TOLERANCE  

  Integer sku
  Double listPrice
  Double redPrice
  Double agreementPrice

  private Pricing( ) {
    listPrice = 0
    redPrice = 0
    agreementPrice = 0
  }
  Pricing( Integer pSku ) {
    this( )
    setSku( pSku )
  }

  // Internal methods
  protected void setSku( Integer pSku ) {
    sku = pSku  
  } 
    
  // Public Methods
  Double getAgreementPrice( ) {
    return ( Math.abs(agreementPrice) < ZERO_TOLERANCE ? listPrice : agreementPrice )
  }

  Double getRedPrice( ) {
    return ( Math.abs(redPrice) < ZERO_TOLERANCE ? listPrice : redPrice )
  }

  Boolean isPricingRequired( ) {
    return ( Math.abs( listPrice ) < ZERO_TOLERANCE )  
  }
  
  // Entity
  int compareTo( Pricing pPricing ) {
    int result = this.sku.compareTo( pPricing.sku )
    return result  
  }
  
  boolean equals( Object pObj ) {
    boolean result = false
    if ( pObj instanceof Pricing ) {
      result = sku.equals( ( pObj as Pricing ).sku )
    } else if ( pObj instanceof PromotionOrderDetail ) {
      result = sku.equals( ( pObj as PromotionOrderDetail ).sku )
    } else if ( pObj instanceof DetalleNotaVenta ) {
      result = sku.equals( ( pObj as DetalleNotaVenta ).idArticulo )
    }
    return result
  }

  int hashCode( ) {
    return sku.hashCode( )
  }

  String toString( ) {
    String str = String.format( "[%d]  List:%,.2f  Red:%,.2f  Agreement:%,.2f", 
        this.sku, this.listPrice, this.redPrice, this.agreementPrice )
    return str
  }

}

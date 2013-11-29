package mx.lux.pos.model

import org.apache.commons.lang3.StringUtils

class PricingDataset {
  
  Map<Integer, Pricing> dataset = new TreeMap<Integer, Pricing>( )
  String agreement
  
  PricingDataset( ) {
    this.setAgreement( "" )
  }
  
  // Public Methods
  Pricing add( Integer pSku ) {
    Pricing p = dataset.get( pSku )
    if ( p == null ) {
      p = new Pricing( pSku )
      this.dataset.put( p.sku, p )
    }
    return p
  }
  
  void clear( ) {
    this.dataset.clear( )
  }
  
  Pricing find( Integer pSku ) {
    return dataset.get( pSku )  
  }
  
  Boolean isPricingRequired( Integer pSku ) {
    Pricing p = add( pSku )
    return p.isPricingRequired( )
  }
  
  List<Pricing> listPricingRequired( ) {
    List<Pricing> requiredList = new ArrayList<Pricing>( ) 
    for ( Pricing p : dataset.values( ) ) {
      if ( p.isPricingRequired( ) ) {
        requiredList.add( p )
      }
    } 
    return requiredList 
  }
  
  void setAgreement( String pAgreement ) {
    agreement = StringUtils.trimToEmpty( pAgreement ).toUpperCase( )
  }

  String toString( ) {
    StringBuffer sb = new StringBuffer( String.format( "[%s] %d elements", this.getClass( ).getSimpleName( ), this.dataset.size( ) ) )
    for ( Pricing p : this.dataset.values( ) ) {
      sb.append( String.format( "\n    %s", p.toString( ) ) )
    }
    return sb.toString( )
  }
  
}

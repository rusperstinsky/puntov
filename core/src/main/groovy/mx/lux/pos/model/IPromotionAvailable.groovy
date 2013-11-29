package mx.lux.pos.model

interface IPromotionAvailable extends Comparable<IPromotionAvailable> {

  // Properties
  Boolean getApplied( )
  
  String getDescription( )
  
  String getPartNbrList( )
  
  Double getBaseAmount( )
  
  Double getDiscountAmount( )
  
  Double getPromotionAmount( )
  
  // Methods
  
  
}

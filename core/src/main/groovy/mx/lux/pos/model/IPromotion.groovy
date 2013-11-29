package mx.lux.pos.model

interface IPromotion extends Comparable<IPromotion> {

  Integer getIdPromotion( )

  String getDescription( )

  PromotionClass getPromotionClass( )
  
  Date getStartEff( )

  Date getEndEff( )

  Integer getPriority( )

  Boolean isAutoApplied( )

  Boolean isMandatory( )

  Boolean appliesOnContractPrice( )

  Boolean appliesOnRedTag( )

  Boolean isTriggeredBy( Articulo pPart )
  
  List<PromotionPart> listGroupRequired( )
  
  void register( Articulo pPartTrigger, PromotionModel pDataset )
  
}

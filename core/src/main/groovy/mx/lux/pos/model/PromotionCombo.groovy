package mx.lux.pos.model

class PromotionCombo implements IPromotion {

  private static final Double ZERO_TOLERANCE = PromotionModel.ZERO_TOLERANCE

  PromotionSingle base
  PromotionPart combo

  PromotionCombo( Promocion pEntity ) {
    this.base = new PromotionSingle( pEntity )
    this.init()
  }

  // Internal Methods
  protected void applyPromotionPricing( Pricing pPricing, PromotionApplied pApplied, PromotionPart pPart ) {
    Double basePrice = pApplied.regularPrice
    Double promotionPrice = basePrice
    if ( pPricing != null ) {
      basePrice = pPricing.listPrice
      if ( this.appliesOnRedTag() && !this.appliesOnContractPrice() ) {
        basePrice = Math.min( pPricing.listPrice, pPricing.redPrice )
      } else if ( !this.appliesOnRedTag() && this.appliesOnContractPrice() ) {
        basePrice = Math.min( pPricing.listPrice, pPricing.agreementPrice )
      } else if ( this.appliesOnRedTag() && this.appliesOnContractPrice() ) {
        basePrice = Math.min( pPricing.listPrice, Math.min( pPricing.redPrice, pPricing.agreementPrice ) )
      }
      promotionPrice = basePrice
      if ( PromotionPriceType.FixedPrice.equals( pPart.priceType ) ) {
        promotionPrice = pPart.discountAmount
      } else if ( PromotionPriceType.FixedDiscountAmount.equals( pPart.priceType ) ) {
        promotionPrice = basePrice - pPart.discountAmount
      } else if ( PromotionPriceType.Undefined.equals( pPart.priceType ) ) {
        promotionPrice = ( ( 100.0 - pPart.discountPercent ) * basePrice ) / 100.0
      }
    }
    pApplied.basePrice = basePrice
    pApplied.promotionPrice = promotionPrice
  }

  protected void init( ) {
    combo = PromotionPart.createPromotionPart( this.base.entity, true )
  }

  // Public Methods
  Integer getIdPromotion( ) {
    return this.base.idPromotion
  }

  String getDescription( ) {
    return this.base.description
  }

  PromotionClass getPromotionClass( ) {
    return PromotionClass.Combo
  }

  Date getStartEff( ) {
    return this.base.startEff
  }

  Date getEndEff( ) {
    return this.base.endEff
  }

  Integer getPriority( ) {
    return this.base.priority
  }

  Boolean isAutoApplied( ) {
    return this.base.isAutoApplied()
  }

  Boolean isMandatory( ) {
    return this.base.isMandatory()
  }

  Boolean appliesOnContractPrice( ) {
    return this.base.appliesOnContractPrice()
  }

  Boolean appliesOnRedTag( ) {
    return this.base.appliesOnRedTag()
  }

  Boolean isTriggeredBy( Articulo pPart ) {
    return this.base.isTriggeredBy( pPart )
  }

  List<PromotionPart> listGroupRequired( ) {
    List<PromotionPart> partList = this.base.listGroupRequired()
    if ( this.combo.isGroupRequired() ) {
      partList.add( this.combo )
    }
    return partList
  }

  void register( Articulo pPartTrigger, PromotionModel pDataset ) {
    PromotionOrderDetail baseOrderDetail = null
    if ( this.isTriggeredBy( pPartTrigger ) ) {
      if ( ( Math.abs( this.base.appliesTo.discountAmount ) > ZERO_TOLERANCE ) ||
          ( Math.abs( this.base.appliesTo.discountPercent ) > ZERO_TOLERANCE ) ) {
        baseOrderDetail = pDataset.order.orderDetailSet.get( pPartTrigger.id )
      }
      if ( ( Math.abs( this.combo.discountAmount ) > ZERO_TOLERANCE ) ||
          ( Math.abs( this.combo.discountPercent ) > ZERO_TOLERANCE ) ) {
        for ( PromotionOrderDetail comboDetail : pDataset.order.orderDetailSet.values() ) {
          Articulo part = pDataset.partSet.find( comboDetail.sku )
          if ( ( part != null ) && ( this.combo.appliesToPart( part ) ) ) {
            if ( baseOrderDetail != null ) {
              PromotionAvailable available =
                pDataset.availablePromotionList.find( this.idPromotion, baseOrderDetail.sku, comboDetail.sku )
              if ( available == null ) {
                available = pDataset.availablePromotionList.add( this )
                PromotionApplied baseApplied = available.addAppliesTo( baseOrderDetail )
                applyPromotionPricing( pDataset.pricingSet.find( baseApplied.sku ), baseApplied, this.base.appliesTo )
                PromotionApplied comboApplied = available.addAppliesTo( comboDetail )
                applyPromotionPricing( pDataset.pricingSet.find( comboApplied.sku ), comboApplied, this.combo )
              }
              available.addEnabler( baseOrderDetail.sku )
            } else {
              PromotionAvailable available =
                pDataset.availablePromotionList.find( this.idPromotion, comboDetail.sku )
              if ( available == null ) {
                available = pDataset.availablePromotionList.add( this )
                PromotionApplied applied = available.addAppliesTo( comboDetail )
                applyPromotionPricing( pDataset.pricingSet.find( applied.sku ), applied, this.combo )
              }
              available.addEnabler( pPartTrigger.id )
            }
          }
        }
      }
    }
  }

  // Entity
  int compareTo( IPromotion pPromotion ) {
    return this.base.compareTo( pPromotion )
  }

  boolean equals( Object pObj ) {
    boolean result = this.base.equals( pObj )
    return result
  }

  int hashCode( ) {
    return this.base.hashCode()
  }

  String toString( ) {
    StringBuffer sb = new StringBuffer()
    sb.append( String.format( "[%d] %s (%s)  TriggeredBy:<%s>", idPromotion, description, promotionClass.toString(),
        this.base.appliesTo.toString() ) )
    sb.append( String.format( "\n        appliesTo:<%s>", this.base.appliesTo.toString() ) )
    sb.append( String.format( "\n        appliesTo:<%s>", this.combo.toString() ) )
    return sb.toString()
  }

}

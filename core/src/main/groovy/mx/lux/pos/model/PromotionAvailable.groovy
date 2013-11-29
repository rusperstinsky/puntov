package mx.lux.pos.model


class PromotionAvailable implements IPromotionAvailable {

  private static final Double ZERO_TOLERANCE = PromotionModel.ZERO_TOLERANCE

  IPromotion promotion
  List<Integer> enabledByList
  List<PromotionApplied> appliesToList
  Boolean applied
  Boolean userChoice

  PromotionAvailable( IPromotion pPromotion ) {
    this.applied = false
    this.appliesToList = new ArrayList<PromotionApplied>()
    this.enabledByList = new ArrayList<Integer>()
    this.setPromotion( pPromotion )
  }

  // Internal Methods
  protected Boolean canFindAppliesTo( Integer pSku ) {
    Boolean found = false
    for ( PromotionApplied prom : appliesToList ) {
      found = prom.equals( pSku )
      if ( found )
        break
    }
    return found
  }

  protected void setPromotion( IPromotion pPromotion ) {
    this.promotion = pPromotion
  }

  protected void setApplied( Boolean pApplied ) {
    this.applied = pApplied
  }

  // Public Methods
  void addEnabler( Integer pSku ) {
    if ( !enabledByList.contains( pSku ) ) {
      enabledByList.add( pSku )
    }
  }

  PromotionApplied addAppliesTo( PromotionOrderDetail pOrderDetail ) {
    PromotionApplied promApplied = this.findAppliesTo( pOrderDetail.sku )
    if ( promApplied == null ) {
      promApplied = new PromotionApplied( pOrderDetail )
      this.appliesToList.add( promApplied )
    }
    return promApplied
  }

  void apply( Boolean pApplied ) {
    this.apply( pApplied, false )
  }

  void apply( Boolean pApplied, Boolean pUserSelection ) {
    this.userChoice = pUserSelection
    for ( PromotionApplied promLine : this.appliesToList ) {
      if ( pApplied ) {
        promLine.orderDetail.apply( promLine )
      } else {
        promLine.orderDetail.apply( null )
      }
    }
    this.setApplied( pApplied )
  }

  void clear( ) {
    this.enabledByList.clear()
    this.appliesToList.clear()
  }

  PromotionApplied findAppliesTo( Integer pSku ) {
    PromotionApplied found = null
    for ( PromotionApplied pa : appliesToList ) {
      if ( pa.equals( pSku ) ) {
        found = pa
        break
      }
    }
    return found
  }

  Double getBaseAmount( ) {
    Double amount = 0
    for ( PromotionApplied applied : this.appliesToList ) {
      amount += applied.baseAmount
    }
    return amount
  }

  String getDescription( ) {
    return this.promotion.description
  }

  Double getDiscountAmount( ) {
    return ( this.baseAmount - this.promotionAmount )
  }

  Double getDiscountPercent( ) {
    Double percent = 0
    Double amount = this.baseAmount
    if ( Math.abs( amount ) > ZERO_TOLERANCE ) {
      percent = ( amount - this.promotionAmount ) / amount
    }
    return percent
  }

  Integer getIdPromotion( ) {
    return this.promotion.idPromotion
  }

  String getPartNbrList( ) {
    String partList = ""
    for ( PromotionApplied prom : appliesToList ) {
      partList = String.format( "%s%s%s", partList, ( partList.length() > 0 ? ", " : "" ), prom.partNbr )
    }
    return partList
  }

  Double getPromotionAmount( ) {
    Double amount = 0
    for ( PromotionApplied applied : this.appliesToList ) {
      amount += applied.promotionAmount
    }
    return amount
  }

  String getSkuList( ) {
    String skuList = ""
    for ( PromotionApplied prom : appliesToList ) {
      skuList = String.format( "%s%s%d", skuList, ( skuList.length() > 0 ? ", " : "" ), prom.sku )
    }
    return skuList
  }

  String getTriggerList( ) {
    String triggerList = ""
    for ( Integer sku : enabledByList ) {
      triggerList = String.format( "%s%s%s", triggerList, ( triggerList.length() > 0 ? ", " : "" ), sku.toString() )
    }
    return triggerList
  }

  Boolean isAbleToApply( ) {
    Boolean ableToApply = true
    for ( PromotionApplied appliedTo : this.appliesToList ) {
      ableToApply &= !appliedTo.orderDetail.hasPromotionApplied()
    }
    return ableToApply
  }

  void removeEnabler( Integer pSku ) {
    for ( int ix = this.enabledByList.size() - 1; ix >= 0; ix-- ) {
      if ( this.enabledByList.get( ix ).equals( pSku ) ) {
        this.enabledByList.remove( ix )
      }
    }
  }

  // Entity
  int compareTo( IPromotionAvailable pPromotion ) {
    int result = 0
    if ( pPromotion instanceof PromotionAvailable ) {
      result = this.promotion.compareTo( ( pPromotion as PromotionAvailable ).promotion )
      if ( result == 0 )
        this.partNbrList.compareTo( pPromotion.partNbrList )
    } else if ( pPromotion instanceof PromotionDiscount ) {
      result = 1
    }
    return result
  }

  boolean equals( Object pObj ) {
    boolean result = false
    if ( pObj instanceof PromotionAvailable ) {
      PromotionAvailable prom = ( pObj as PromotionAvailable )
      result = this.equalsAppliedList( prom.idPromotion, prom.appliesToList )
    } else if ( pObj instanceof IPromotion ) {
      result = this.promotion.equals( pObj as IPromotion )
    }
    return result
  }

  boolean equals( Integer pPromotionId, Integer pSku ) {
    boolean result = this.idPromotion.equals( pPromotionId ) && ( this.appliesToList.size() == 1 )
    if ( result )
      result = this.canFindAppliesTo( pSku )
    return result
  }

  boolean equals( Integer pPromotionId, Integer pSku_1, Integer pSku_2 ) {
    boolean result = this.idPromotion.equals( pPromotionId ) && ( this.appliesToList.size() == 2 )
    if ( result )
      result = this.canFindAppliesTo( pSku_1 )
    if ( result )
      result = this.canFindAppliesTo( pSku_2 )
    return result
  }

  boolean equals( Integer pPromotionId, List<Integer> pSkuList ) {
    boolean result = this.idPromotion.equals( pPromotionId ) &&
        ( this.appliesToList.size() == pSkuList.size() )
    for ( Integer sku : pSkuList ) {
      if ( result )
        result = this.canFindAppliesTo( sku )
    }
    return result
  }

  boolean equalsAppliedList( Integer pPromotionId, List<PromotionApplied> pAppliedList ) {
    boolean result = this.idPromotion && this.appliesToList.size() == pAppliedList.size()
    for ( PromotionApplied promApplied : pAppliedList ) {
      if ( result )
        result = this.canFindAppliesTo( promApplied.sku )
    }
    return result
  }

  int hashCode( ) {
    return String.format( "%d¡%s", this.idPromotion, this.skuList ).hashCode()
  }

  public String toString( ) {
    StringBuffer sb = new StringBuffer()
    sb.append( String.format( "[%d] %s  TriggeredBy:<%s>", this.idPromotion,
        this.description, this.triggerList ) )
    for ( PromotionApplied prom : this.appliesToList ) {
      sb.append( String.format( "\n        AppliesTo:%s", prom.toString() ) )
    }
    return sb.toString()
  }
}

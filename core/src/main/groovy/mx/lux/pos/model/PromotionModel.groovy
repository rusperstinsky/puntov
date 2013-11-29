package mx.lux.pos.model

import org.apache.commons.lang3.StringUtils

class PromotionModel {

  static final Double ZERO_TOLERANCE = 0.0001

  PromotionOrder order
  PricingDataset pricingSet
  PartDataset partSet
  PromotionList activePromotionList
  PromotionAvailableList availablePromotionList
  PromotionDiscount orderDiscount

  PromotionModel( ) {
    pricingSet = new PricingDataset()
    partSet = new PartDataset()
    activePromotionList = new PromotionList()
    availablePromotionList = new PromotionAvailableList()
    orderDiscount = null
  }

  // Internal methods
  protected void appendOrderLines( List<DetalleNotaVenta> pOrderLines ) {
    for ( DetalleNotaVenta orderLine : pOrderLines ) {
      order.addOrderDetail( orderLine )
    }
  }

  protected Boolean canFindOrderLine( List<DetalleNotaVenta> pOrderLines, Integer pSku ) {
    Boolean canFind = false
    for ( DetalleNotaVenta orderLine : pOrderLines ) {
      canFind = orderLine.idArticulo.equals( pSku )
      if ( canFind )
        break
    }
    return canFind
  }

  protected void removeOrderDetail( List<PromotionOrderDetail> pOrderDetailList ) {
    for ( PromotionOrderDetail det : pOrderDetailList ) {
      if ( det.hasPromotionApplied() ) {
        // Remove any applied Promotion on sku
        PromotionAvailable promotion = this.availablePromotionList.findApplied( det.sku )
        if ( promotion != null ) {
          this.availablePromotionList.remove( promotion )
        }
      }
      // Remove any unapplied Promotion on sku
      List<PromotionAvailable> promotions = this.availablePromotionList.listAppliesToSku( det.sku )
      for ( PromotionAvailable promotion : promotions ) {
        this.availablePromotionList.remove( promotion )
      }
      // Remove any available Promotion triggered by sku
      this.availablePromotionList.removeEnabler( det.sku )
      this.availablePromotionList.purgePromotions()
      // Remove from OrderDetailSet
      order.orderDetailSet.remove( det.sku )
    }
  }

  protected void verifyDroppedOrderLines( List<DetalleNotaVenta> pOrderLines ) {
    List<PromotionOrderDetail> toDelete = new ArrayList<PromotionOrderDetail>()
    for ( PromotionOrderDetail det : order.orderDetailSet.values() ) {
      if ( !canFindOrderLine( pOrderLines, det.sku ) ) {
        toDelete.add( det )
      }
    }
    removeOrderDetail( toDelete )
  }

  protected void verifyOrderLineRelations( ) {
    for ( PromotionOrderDetail det : order.orderDetailSet.values() ) {
      partSet.add( det.sku )
      pricingSet.add( det.sku )
    }
  }

  // Public methods
  void clear( ) {
    this.pricingSet.clear()
    this.partSet.clear()
    this.availablePromotionList.clear()
    if ( order != null ) {
      this.order.orderDetailSet.clear()
      this.order = null
    }
    this.orderDiscount = null
    this.activePromotionList.clear()
  }

  // Order methods
  void fillOrderDetail( ) {
    for ( PromotionOrderDetail orderDetail : order.orderDetailSet.values() ) {
      if ( orderDetail.isPartRequired() ) {
        Articulo part = partSet.find( orderDetail.sku )
        orderDetail.partNbr = StringUtils.trimToEmpty( part?.articulo )
      }
    }
  }

  Map<Integer, PromotionOrderDetail> getPartsRequiredDataset( ) {
    Map<Integer, PromotionOrderDetail> partRequired = new TreeMap<Integer, PromotionOrderDetail>()
    for ( PromotionOrderDetail det : order.orderDetailSet.values() ) {
      if ( det.isPartRequired() ) {
        partRequired.put( det.sku, det )
      }
    }
    return partRequired
  }

  Boolean isOrderEquals( Object pObject ) {
    Boolean result = ( this.order != null )
    if ( result )
      result = this.order.equals( pObject )
    return result
  }

  void loadOrder( NotaVenta pOrder ) {
    order = new PromotionOrder( pOrder )
    verifyOrderLineRelations()
  }

  void updateOrder( NotaVenta pOrder ) {
    verifyDroppedOrderLines( new ArrayList<DetalleNotaVenta>( pOrder.detalles ) )
    appendOrderLines( new ArrayList<DetalleNotaVenta>( pOrder.detalles ) )
    verifyOrderLineRelations()
  }

  // Pricing methods

  // Parts Methods

  // Promotion methods
  Boolean applyPromotion( PromotionAvailable pPromotion ) {
    this.applyPromotion( pPromotion, false )
  }

  Boolean applyPromotion( PromotionAvailable pPromotion, Boolean pUserSelection ) {
    if ( pPromotion.isAbleToApply() && ( !isStoreDiscountApplied() ) ) {
      pPromotion.apply( true, pUserSelection )
    }
    return pPromotion.applied
  }

  Boolean applyPromotionsAuto( ) {
    Boolean changed = false
    for ( PromotionAvailable promotion : availablePromotionList.list ) {
      if ( promotion.promotion.isAutoApplied() && !promotion.applied && !promotion.userChoice ) {
        changed |= applyPromotion( promotion )
      }
    }
    return changed
  }

  Boolean cancelPromotion( PromotionAvailable pPromotion, Boolean pUserSelection ) {
    if ( pPromotion.applied ) {
      pPromotion.apply( false, pUserSelection )
    }
    return !pPromotion.applied
  }

  Boolean cancelPromotionDiscount( PromotionDiscount pPromotion, Boolean pUserSelection ) {
      if ( pPromotion.applied ) {
          pPromotion.apply( false )
      }
      return pPromotion.applied
  }

  Boolean isAnyApplied( ) {
    return availablePromotionList.isAnyApplied()
  }

  List<PromotionAvailable> listAvailablePromotions( ) {
    return availablePromotionList.list
  }

  void refreshAvailablePromotions( ) {
    for ( PromotionOrderDetail orderDetail : order.orderDetailSet.values() ) {
      Articulo part = partSet.find( orderDetail.sku )
      for ( IPromotion promotion : activePromotionList.list ) {
        promotion.register( part, this )
      }
    }
  }

  // Order Discount
  Boolean isStoreDiscountApplied( ) {
    Boolean discountApplied = false
    if ( hasOrderDiscountApplied() ) {
      discountApplied = PromotionDiscountType.StoreDiscount.equals( orderDiscount.discountType )
    }
    return discountApplied
  }

  Boolean hasOrderDiscountApplied( ) {
    return ( this.orderDiscount != null )
  }

  void setupOrderDiscount( String pCorporateKey, Double pDiscountPercent ) {
    String key = StringUtils.trimToEmpty( pCorporateKey )
    if ( key.length() == 0 ) {
      this.orderDiscount = PromotionDiscount.discountInstance
    } else if ( key.length() > 0 ) {
      this.orderDiscount = PromotionDiscount.corporateDiscountInstance
      this.orderDiscount.corporateKey = pCorporateKey
    }
    if ( this.hasOrderDiscountApplied() ) {
      this.orderDiscount.order = this.order
      this.orderDiscount.discountPercent = pDiscountPercent
      this.orderDiscount.apply( true )
    }
  }

  String toString( ) {
    StringBuffer sb = new StringBuffer()
    sb.append( String.format( "\n[%s]", this.getClass().getSimpleName() ) )
    if ( order != null )
      sb.append( String.format( "\n%s", order ) )
    sb.append( String.format( "\n%s", partSet.toString() ) )
    sb.append( String.format( "\n%s", pricingSet.toString() ) )
    sb.append( String.format( "\n%s", activePromotionList.toString() ) )
    sb.append( String.format( "\n%s", availablePromotionList.toString() ) )
    sb.append( String.format( "\n%s", orderDiscount.toString() ) )
    return sb.toString()
  }
}

package mx.lux.pos.model

import mx.lux.pos.service.business.Registry
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils

class PromotionSingle implements IPromotion {

  private static final Double ZERO_TOLERANCE = PromotionModel.ZERO_TOLERANCE

  private Promocion entity
  private PromotionPart appliesTo

  PromotionSingle( Promocion pEntity ) {
    entity = pEntity
    this.init()
  }

  // Internal Methods
  protected void applyPromotionPricing( Pricing pPricing, PromotionApplied pApplied ) {
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
      if ( PromotionPriceType.FixedPrice.equals( this.appliesTo.priceType ) ) {
        promotionPrice = this.appliesTo.discountAmount
      } else if ( PromotionPriceType.FixedDiscountAmount.equals( this.appliesTo.priceType ) ) {
        promotionPrice = basePrice - this.appliesTo.discountAmount
      } else if ( PromotionPriceType.Undefined.equals( this.appliesTo.priceType ) ) {
        promotionPrice = ( ( 100.0 - this.appliesTo.discountPercent ) * basePrice ) / 100.0
      }
    }
    pApplied.basePrice = basePrice
    pApplied.promotionPrice = promotionPrice
  }

  protected void init( ) {
    appliesTo = PromotionPart.createPromotionPart( entity, false )
  }

  // Public Methods
  PromotionPart getAppliesTo() {
    return this.appliesTo
  }

  Integer getIdPromotion( ) {
    return entity.idPromocion
  }

  String getDescription( ) {
    return StringUtils.trimToEmpty( entity.descripcion )
  }

  Promocion getEntity( ) {
    return this.entity
  }

  PromotionClass getPromotionClass( ) {
    return PromotionClass.Simple
  }

  Date getStartEff( ) {
    return DateUtils.truncate( entity.vigenciaIni, Calendar.DATE )
  }

  Date getEndEff( ) {
    return DateUtils.truncate( entity.vigenciaFin, Calendar.DATE )
  }

  Integer getPriority( ) {
    return entity.prioridad
  }

  Boolean isAutoApplied( ) {
    return entity.aplicaAuto
  }

  Boolean isMandatory( ) {
    return entity.obligatoria
  }

  Boolean appliesOnContractPrice( ) {
    return entity.aplicaConvenio
  }

  Boolean appliesOnRedTag( ) {
    return entity.precioOferta
  }

  Boolean isTriggeredBy( Articulo pPart ) {
    Boolean triggered = this.appliesTo.appliesToPart( pPart )
    if ( triggered ) {
      triggered = !( Registry.getManualPriceTypeList().contains( pPart.idGenerico ) )
    }
    return triggered
  }

  List<PromotionPart> listGroupRequired( ) {
    List<PromotionPart> partList = new ArrayList<PromotionPart>()
    if ( appliesTo.isGroupRequired() ) {
      partList.add( appliesTo )
    }
    return partList
  }

  void register( Articulo pPartTrigger, PromotionModel pDataset ) {
    if ( this.isTriggeredBy( pPartTrigger ) ) {
      PromotionOrderDetail orderDetail = pDataset.order.orderDetailSet.get( pPartTrigger.id )
      if ( orderDetail != null ) {
        if ( ( Math.abs( this.appliesTo.discountAmount ) > ZERO_TOLERANCE ) ||
            ( Math.abs( this.appliesTo.discountPercent ) > ZERO_TOLERANCE ) ) {
          PromotionAvailable available = pDataset.availablePromotionList.find( this.idPromotion, pPartTrigger.id )
          if ( available == null ) {
            available = pDataset.availablePromotionList.add( this )
            PromotionApplied applied = available.addAppliesTo( orderDetail )
            applyPromotionPricing( pDataset.pricingSet.find( applied.sku ), applied )
          }
          available.addEnabler( pPartTrigger.id )
        }
      }
    }
  }

  // Entity
  int compareTo( IPromotion pPromotion ) {
    return this.idPromotion.compareTo( pPromotion.idPromotion )
  }

  boolean equals( Object pObj ) {
    boolean result = false
    if ( pObj instanceof IPromotion ) {
      result = this.idPromotion.equals( ( pObj as IPromotion ).idPromotion )
    } else if ( pObj instanceof Promocion ) {
      result = this.idPromotion.equals( ( pObj as Promocion ).idPromocion )
    } else if ( pObj instanceof Integer ) {
      result = this.idPromotion.equals( ( pObj as Integer ) )
    }
    return result
  }

  int hashCode( ) {
    return this.idPromotion.hashCode()
  }

  String toString( ) {
    String str = String.format( "[%d] %s (%s)  appliesTo:<%s>", idPromotion, description, promotionClass.toString(),
        appliesTo.toString() )
    return str
  }
}

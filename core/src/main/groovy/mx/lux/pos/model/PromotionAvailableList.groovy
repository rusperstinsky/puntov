package mx.lux.pos.model
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class PromotionAvailableList {

  Logger logger
  List<PromotionAvailable> list
  
  PromotionAvailableList( ) {
    this.setList( )
    this.logger = LoggerFactory.getLogger( this.getClass() )
  }
  
  // Internal Methods
  protected void setList( ) {
    this.list = new ArrayList<PromotionAvailable>( )
  }
  
  // Public Methods
  PromotionAvailable add( IPromotion pPromotion ) {
    PromotionAvailable prom = new PromotionAvailable( pPromotion )
    this.list.add( prom )
    return prom  
  }
  
  void clear( ) {
    this.list.clear( )
  }
  
  PromotionAvailable find( Integer pPromotionId, Integer pSku_1 ) {
    PromotionAvailable found = null 
    for ( PromotionAvailable prom : list ) {
      if ( prom.equals( pPromotionId, pSku_1 ) ) {
        found = prom
        break
      }
    }
    return found
  }

  PromotionAvailable find( Integer pPromotionId, Integer pSku_1, Integer pSku_2 ) {
    PromotionAvailable found = null
    for ( PromotionAvailable prom : list ) {
      if ( prom.equals( pPromotionId, pSku_1, pSku_2 ) ) {
        found = prom
        break
      }
    }
    return found
  }

  PromotionAvailable find( Integer pPromotionId, List<Integer> pSkuList ) {
    PromotionAvailable found = null
    for ( PromotionAvailable prom : list ) {
      if ( prom.equals( pPromotionId, pSkuList ) ) {
        found = prom
        break
      }
    }
    return found
  }

  PromotionAvailable findApplied( Integer pSku ) {
    PromotionAvailable found = null
    for ( PromotionAvailable promotion : this.list ) {
      if ( promotion.applied ) {
        for ( PromotionApplied appliesTo : promotion.appliesToList ) {
          if ( appliesTo.sku.equals( pSku ) ) {
            found = promotion
            break
          }
        }
      }
      if ( found != null ) break
    } 
    return found 
  }
  
  Boolean isAnyApplied( ) {
    Boolean anyApplied = false
    for ( PromotionAvailable prom : this.list ) {
      anyApplied = ( anyApplied || prom.applied )
    }
    return anyApplied
  }

  List<PromotionAvailable> listAppliesToSku( Integer pSku ) {
    List<PromotionAvailable> selected = new ArrayList<PromotionAvailable>()
    for ( PromotionAvailable promotion : this.list ) {
      if ( !promotion.applied ) {
        for ( PromotionApplied appliesTo : promotion.appliesToList ) {
          if ( appliesTo.sku.equals( pSku ) ) {
            selected.add( promotion )
          }
        }
      }
    }
    return selected
  }

  void purgePromotions( ) {
    // Remove any promotions that does not have any valid trigger
    List<Integer> toPurge = []
    for ( Integer ix = 0; ix < this.list.size(); ix++) {
      if (this.list.get(ix).triggerList.size() == 0) {
        toPurge.add(ix)
      }
    }
    for (Integer ix : toPurge) {
      this.list.remove(ix)
    }
  }

  void removeEnabler( Integer pEnabler ) {
    List<PromotionAvailable> noEnablerList = new ArrayList<PromotionAvailable>( )
    for ( PromotionAvailable promotion : this.list ) {
      promotion.removeEnabler( pEnabler )
      if ( promotion.enabledByList.size( ) == 0 ) {
        noEnablerList.add( promotion )
      }
    } 
    for ( PromotionAvailable promotion : noEnablerList ) {
      this.remove( promotion )
    } 
  }
  
  void remove( PromotionAvailable pPromotion ) {
    this.logger.debug( String.format( "Removing from Available: <%s>", pPromotion.toString() ) )
    this.list.remove( pPromotion )
  }
  
  String toString( ) {
    StringBuffer sb = new StringBuffer( )
    sb.append( String.format( "[%s] %d elements", this.getClass( ).getSimpleName( ), this.list.size( ) ) )
    for ( PromotionAvailable prom : this.list ) {
      sb.append( String.format( "\n    %s", prom.toString( ) ) )
    }
    return sb.toString( )
  }
}

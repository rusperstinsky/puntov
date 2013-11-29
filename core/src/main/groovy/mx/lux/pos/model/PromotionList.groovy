

package mx.lux.pos.model

class PromotionList {

  List<IPromotion> list = new ArrayList<IPromotion>( )
  
  // Factory Methods
  IPromotion create( Promocion pEntity) {
    IPromotion promotion = null
    if ( PromotionClass.Simple.equals( PromotionClass.parse( pEntity.tipoPromocion ) ) ) {
      promotion = new PromotionSingle( pEntity ) 
    } else if ( PromotionClass.Combo.equals( PromotionClass.parse( pEntity.tipoPromocion ) ) ) {
      promotion = new PromotionCombo( pEntity )
    }
    return promotion
  }
  
  // Public Methods
  void add( IPromotion pPromotion ) {
    IPromotion p = find( pPromotion.idPromotion )
    if ( p == null ) {
      this.list.add( pPromotion )
    }
  }

  void add( Promocion pEntity ) {
    IPromotion promotion = find( pEntity.idPromocion )
    if ( promotion == null ) {
      promotion = create( pEntity )
      if ( promotion != null ) { 
        this.add( promotion )
      }
    }
  }

  void clear( ) {
    this.list.clear( )
  }

  IPromotion find( Integer pPromotionId ) {
    IPromotion found = null
    for ( IPromotion p : list ) {
      if ( p.equals( pPromotionId) ) {
        found = p
        break
      }
    }
    return found
  }  
  
  List<PromotionPart> listGroupRequired( ) {
    List<PromotionPart> groupRequiredList = new ArrayList<PromotionPart>( )
    for ( IPromotion p : list ) {
      groupRequiredList.addAll( p.listGroupRequired( ) )
    }
    return groupRequiredList
  } 
  
  String toString( ) {
    StringBuffer sb = new StringBuffer( ) 
    sb.append( String.format( "[%s] %d elements", this.getClass( ).getSimpleName( ), this.list.size( ) ) )
    for ( IPromotion p : this.list ) {
      sb.append( String.format( "\n    %s", p.toString( ) ) )
    }
    return sb.toString( )
  }
}

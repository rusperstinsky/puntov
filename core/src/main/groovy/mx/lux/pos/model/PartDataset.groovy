package mx.lux.pos.model

class PartDataset {
  
  Map<Integer, Articulo> dataset = new TreeMap<Integer, Articulo>( )
  
  // Public Methods
  void add( Integer pSku ) {
    Articulo part = find( pSku ) 
    if ( part == null ) {
      dataset.put( pSku, null ) 
    }
  }

  void add( List<Articulo> pPartList ) {
    for ( Articulo part : pPartList ) {
      if ( isPartRequired( part.id ) ) {
        dataset.put( part.id, part )
      }
    }
  }
  
  void clear( ) {
    this.dataset.clear( )
  }
  
  Articulo find( Integer pSku ) {
    return dataset.get( pSku )
  }
  
  Boolean isPartRequired( Integer pSku ) {
    Articulo part = find( pSku )
    return ( part == null )
  }
  
  List<Integer> listRequiredParts( ) {
    List<Integer> requiredPartList = new ArrayList<Integer>( ) 
    for ( Integer sku : dataset.keySet( ) ) {
      if ( isPartRequired( sku ) ) {
        requiredPartList.add( sku )
      }
    }
    return requiredPartList 
  }
  
  String toString( ) {
    StringBuffer sb = new StringBuffer( )
    sb.append( String.format( "[%s] %d elements", this.getClass( ).getSimpleName( ), this.dataset.size( ) ) )
    for ( Integer sku : dataset.keySet( ) ) {
      if ( isPartRequired( sku ) ) {
        sb.append( String.format( "\n    [%d] Required", sku ) )
      } else {
        Articulo part = find( sku )
        sb.append( String.format( "\n    [%d] %s %s", sku, part.articulo, part.descripcion ) )
      }  
    }
    return sb.toString( )
  }
}

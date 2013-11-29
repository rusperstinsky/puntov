package mx.lux.pos.ui.model.adapter

class StringAdapter<T extends Object> {
  
  boolean equals( T pObject, String pAdaptedObject ) {
    return adapt( pObject ).trim().equalsIgnoreCase( pAdaptedObject.trim() )
  }

  // Validate if an object is adaptable by this Adapter   
  boolean isAdaptable( T pObj ) {
    return true
  }
  
  // Default implementation for adapt
  String adapt( T pObject ) {
    return getText( pObject )
  }
  
  List<String> adaptList( List<T> pObjectList ) {
    List<String> list = new ArrayList<String>( pObjectList.size() )
    for ( T obj in pObjectList ) {
      if ( isAdaptable( obj ) ) {
        list.add( adapt( obj ) )
      }
    }
    return list;
  }
  
  Object findObject( String pAdaptedObject, List<T> pObjectList ) {
    Object found = null
    for ( T obj in pObjectList ) {
      if (equals( obj, pAdaptedObject ) ) {
        found = obj
        break
      }
    }
    return found
  }
   
  String getText( T pObject ) {
    return pObject.toString()
  }
  
  String getText( T pObject, String pField ) {
    return String.format( "%s.%s", pObject.getClass().getSimpleName(), pField )
  }
}

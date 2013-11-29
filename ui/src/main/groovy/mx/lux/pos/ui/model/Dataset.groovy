package mx.lux.pos.ui.model

import mx.lux.pos.ui.model.adapter.Filter
import mx.lux.pos.ui.view.component.NavigationBar.Command

class Dataset<T extends Object> {

  List<T> dataset = new ArrayList<T>()
  Filter<T> filter = new Filter<T>()
  Integer currentIndex = null
  
  // Public methods
  T get( Command pCommand ) {
    T selected = null
    if ( size > 0 ) {
      switch (pCommand) {
        case Command.FIRST:   selected = getFirst();      break
        case Command.LAST:    selected = getLast();       break
        case Command.NEXT:    selected = getNext();       break
        case Command.PREV:    selected = getPrev();       break
      }
    }
    return selected
  }
  
  T getCurrent( ) {
    return ( currentIndex != null ? dataset.get(  currentIndex ) : first )
  }
  
  T getFirst() {
    T selected = null
    if ( size > 0 ) {
      currentIndex = 0
      selected = dataset.get( currentIndex )
    }
    return selected
  }

  T getLast() {
    T selected = null
    if ( size > 0 ) {
      currentIndex = size - 1
      selected = dataset.get( currentIndex )
    }
    return selected
  }

  T getNext() {
    T selected = null
    if ( size > 0 ) {
      if ( currentIndex == null ) {
        selected = getFirst()
      } else {
        currentIndex ++
        if ( currentIndex >= size ) {
          selected = getLast()
        } else {
          selected = dataset.get( currentIndex )
        }
      }
    }
    return selected
  }

  T getPrev() {
    T selected = null
    if ( size > 0 ) {
      if ( currentIndex == null ) {
        selected = getLast()
      } else {
        currentIndex --
        if ( currentIndex < 0 ) {
          selected = getFirst()
        } else {
          selected = dataset.get( currentIndex )
        }
      }
    }
    return selected
  }
  
  String getDatasetLabel() {
    String str = "- - -"
    if ( ( size > 0 ) && ( currentIndex != null ) ) {
      str = String.format("%d / %d", currentIndex + 1, size )
    }
  }

  Integer getSize() {
    return dataset.size()
  }
  
  void reset() {
    dataset.clear()
    filter.reset()
  }
  
  void setItems( List<T> pRawList ) {
    dataset.clear()
    currentIndex = null
    for ( T element in pRawList ) {
      if ( filter.select( element ) ) {
        dataset.add( element )
      }
    }
  }
  
}

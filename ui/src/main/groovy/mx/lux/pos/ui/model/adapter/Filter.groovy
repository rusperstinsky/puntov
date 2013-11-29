package mx.lux.pos.ui.model.adapter

class Filter<T extends Object> {
  
  void reset() { }
  
  Boolean select( T pObject ) {
    return true
  }
  
}

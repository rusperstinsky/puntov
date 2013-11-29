package mx.lux.pos.ui.model.adapter

import mx.lux.pos.model.Sucursal

class SiteAdapter extends StringAdapter<Sucursal> {
  
  public String getText( Sucursal pSucursal ) {
    return String.format( "[%d] %s", pSucursal.id, pSucursal.nombre );
  }
  
}

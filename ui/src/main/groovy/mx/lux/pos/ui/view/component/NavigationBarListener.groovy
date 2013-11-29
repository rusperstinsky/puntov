package mx.lux.pos.ui.view.component

import mx.lux.pos.ui.view.component.NavigationBar.Command

interface NavigationBarListener {

  void requestItem( Command pCommand )
  
  void requestNewSearch( )
  
}

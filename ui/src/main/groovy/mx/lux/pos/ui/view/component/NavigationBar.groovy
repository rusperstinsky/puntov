package mx.lux.pos.ui.view.component

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.resources.ImageLibrary
import net.miginfocom.swing.MigLayout

import javax.swing.JButton
import javax.swing.JPanel

class NavigationBar extends JPanel {
  private static final String TXT_DEFAULT = "New"

  enum Command { FIRST, PREV, NEXT, LAST }
  
  private SwingBuilder sb = new SwingBuilder()
  
  private JButton btnSearch
  private String text
  
  private List<NavigationBarListener> listeners = new ArrayList<NavigationBarListener>()
  
  NavigationBar( ) { 
    buildUI()
  }

  private void buildUI( ) {
    sb.panel( this, 
      layout: new MigLayout( "", "5[30!]2[30!]6[]8[30!]2[30!]5", "[]" ) 
    ) {
      label( icon: imageIcon( url: ImageLibrary.getURL( ImageLibrary.IMG_FIRST_MID ) ),
          mouseClicked: { dispatchRequestItem( Command.FIRST ) } )
      label( icon: imageIcon( url: ImageLibrary.getURL( ImageLibrary.IMG_PREV_MID ) ),
          mouseClicked: { dispatchRequestItem( Command.PREV ) } )
      btnSearch = button ( text: TXT_DEFAULT,
          actionPerformed: { dispatchRequestNewSearch( ) } )
      label( icon: imageIcon( url: ImageLibrary.getURL( ImageLibrary.IMG_NEXT_MID ) ),
          mouseClicked: { dispatchRequestItem( Command.NEXT ) } )
      label( icon: imageIcon( url: ImageLibrary.getURL( ImageLibrary.IMG_LAST_MID ) ),
          mouseClicked: { dispatchRequestItem( Command.LAST ) } )
    }
  }
  
  void addNavigationListener( NavigationBarListener pListener ) {
    listeners.add( pListener )
  }

  void removeNavigationListener( NavigationBarListener pListener ) {
    listeners.remove( pListener )
  }

  void setText( String pText ) {
    btnSearch.setText( pText )
  }
  
  // UI Response
  private void dispatchRequestItem( Command pCommand ) {
    for( NavigationBarListener listener in listeners ) {
      listener.requestItem( pCommand )
    }
  }

  private void dispatchRequestNewSearch( ) {
    for( NavigationBarListener listener in listeners ) {
      listener.requestNewSearch( )
    }
  }  
}

package mx.lux.pos.ui.view.component

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.view.driver.PromotionDriver

import java.awt.event.MouseEvent
import javax.swing.JMenuItem
import javax.swing.JPopupMenu

class DiscountContextMenu extends JPopupMenu {

  private SwingBuilder sb = new SwingBuilder( )
  private PromotionDriver driver
  private JMenuItem menuDiscount
  private JMenuItem menuCorporateDiscount
  
  DiscountContextMenu( PromotionDriver pDriver ) {
    driver = pDriver
    buildUI( )
  }
  
  protected buildUI( ) {
    sb.popupMenu( this ) {
      menuDiscount = menuItem( text: "Descuento Tienda",
        visible: true,
        actionPerformed: { onDiscountSelected( ) },
      )
      menuCorporateDiscount = menuItem( text: "Descuento Corporativo", 
        visible: true,
        actionPerformed: { onCorporateDiscountSelected( ) },
      )
    }
  }
  
  // Public Methods
  void activate( MouseEvent pEvent ) {
    menuDiscount.setEnabled( driver.isDiscountEnabled( ) )
    menuCorporateDiscount.setEnabled( driver.isCorporateDiscountEnabled( ) )
    show( pEvent.getComponent(), pEvent.getX(), pEvent.getY() )
  } 
  
  // UI Response
  protected void onDiscountSelected( ) {
    driver.requestDiscount( true, true )
  }
  
  protected void onCorporateDiscountSelected( ) {
    driver.requestCorporateDiscount( false, false )
  }

  
}

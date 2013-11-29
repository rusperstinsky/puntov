package mx.lux.pos.ui.resources

import java.awt.Color
import javax.swing.text.JTextComponent
import java.awt.Dimension
import javax.swing.JOptionPane
import java.awt.Component

class UI_Standards {

  static def BUTTON_SIZE = [ 100, 35 ] as Dimension
  static def BIG_BUTTON_SIZE = [ 125, 40 ] as Dimension
  static def NORMAL_BACKGROUND = Color.WHITE
  static def NORMAL_FOREGROUND = Color.BLACK
  static def LOCKED_BACKGROUND = new Color( 249, 250, 223 )
  // Light Yellow
  static def WARNING_BACKGROUND = NORMAL_BACKGROUND
  static def WARNING_FOREGROUND = Color.RED

  private static final String MSJ_LONG_RUN_CONFIRM = "Este proceso puede tardar algunos minutos ¿Esta seguro que desea realizarlo ahora?"
  private static final String MSJ_OPERATION_DONE = "Proceso terminado"

  static void setFlagged( JTextComponent pFillIn ) {
    setFlagged( pFillIn, true )
  }

  static void setFlagged( JTextComponent pFillIn, boolean pFlagged ) {
    pFillIn.setForeground( pFlagged ? WARNING_FOREGROUND : NORMAL_FOREGROUND )
    pFillIn.setBackground( pFlagged ? WARNING_BACKGROUND : NORMAL_BACKGROUND )
  }

  static void setLocked( JTextComponent pFillIn ) {
    setLocked( pFillIn, true )
  }

  static void setLocked( JTextComponent pFillIn, boolean pLocked ) {
    pFillIn.setEditable( !pLocked )
    pFillIn.setBackground( pLocked ? LOCKED_BACKGROUND : NORMAL_BACKGROUND )
  }

  static Boolean confirmLongRunOperation( Component component ){

    return ( JOptionPane.showConfirmDialog( component, MSJ_LONG_RUN_CONFIRM ) == 0 )

  }

  static void notifyOperationDone( Component component ){
    JOptionPane.showMessageDialog( component, MSJ_OPERATION_DONE )
  }

}

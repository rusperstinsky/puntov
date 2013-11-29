package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.resources.UI_Standards
import net.miginfocom.swing.MigLayout

import javax.swing.ButtonGroup
import javax.swing.JDialog
import javax.swing.JList
import javax.swing.JScrollPane
import javax.swing.border.TitledBorder

class SingleListDialog extends JDialog {

  private def sb = new SwingBuilder()

  private JScrollPane lista
  private JList lstElegir
  private JList lstElegidos
  private ButtonGroup botones

  SingleListDialog( ) {
    buildUI()
  }

  void buildUI( ) {
    sb.dialog( this,
        title: "Lista Doble",
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [ 400, 245 ],
        location: [ 200, 250 ],
    ) {
      panel() {
        borderLayout()
        panel( layout: new MigLayout( "center,fill,wrap ", "[center]", "[push]" ) ) {
          lista = scrollPane( constraints: "center" ) {
            lstElegir = list( autoscrolls: true, border: new TitledBorder( "" ) )
          }
          button( text: "Cerrar", preferredSize: UI_Standards.BUTTON_SIZE )
        }

      }
    }
  }

}

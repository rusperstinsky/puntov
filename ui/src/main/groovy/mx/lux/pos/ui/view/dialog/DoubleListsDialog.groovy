package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.resources.UI_Standards
import net.miginfocom.swing.MigLayout

import java.awt.BorderLayout
import javax.swing.ButtonGroup
import javax.swing.JDialog
import javax.swing.JList
import javax.swing.JScrollPane
import javax.swing.border.TitledBorder

class DoubleListsDialog extends JDialog {

  private def sb = new SwingBuilder()

  private JScrollPane lista
  private JList lstElegir
  private JList lstElegidos
  private ButtonGroup botones

  DoubleListsDialog( ) {
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
        panel( layout: new MigLayout( "center,fill,wrap 3", "[grow,fill][][grow,fill]", "[push]" ) ) {
          lista = scrollPane( constraints: "center" ) {
            lstElegir = list( autoscrolls: true, border: new TitledBorder( "" ) )
          }
          label( text: "" )
          lista = scrollPane( constraints: "center" ) {
            lstElegidos = list( autoscrolls: true, border: new TitledBorder( "" ) )
          }
          label( text: "" )
          button( text: "<" )
          label( text: "" )
          label( text: "" )
          button( text: ">" )
          label( text: "" )
        }
      }

      panel( constraints: BorderLayout.PAGE_END ) {
        borderLayout()
        panel( constraints: BorderLayout.LINE_END ) {
          button( text: "Aceptar", preferredSize: UI_Standards.BUTTON_SIZE
          )
          button( text: "Cancelar", preferredSize: UI_Standards.BUTTON_SIZE
          )
        }
      }

    }
  }
}

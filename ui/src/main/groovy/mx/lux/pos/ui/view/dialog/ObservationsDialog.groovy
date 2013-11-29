package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.resources.UI_Standards
import net.miginfocom.swing.MigLayout

import java.awt.BorderLayout
import javax.swing.JDialog
import javax.swing.JTextField

class ObservationsDialog extends JDialog {

  private def sb = new SwingBuilder()
  private JTextField txtObservaciones

  ObservationsDialog( ) {
    buildUI()
  }

  void buildUI( ) {
    sb.dialog( this,
        title: "Observaciones",
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [ 400, 245 ],
        location: [ 200, 250 ],
    ) {
      panel() {
        borderLayout()
        panel( layout: new MigLayout( "center,fill,wrap", "[fill,grow]", "[push, grow, fill]" ) ) {

          txtObservaciones = textField()
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

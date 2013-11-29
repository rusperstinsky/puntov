package mx.lux.pos.ui.view.dialog.dailyclose

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.ComboBoxController
import mx.lux.pos.ui.controller.DailyCloseController
import mx.lux.pos.ui.model.Terminal
import net.miginfocom.swing.MigLayout

import java.awt.event.ActionEvent
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JDialog
import javax.swing.JOptionPane
import java.awt.Point
import mx.lux.pos.ui.MainWindow

class TerminalCloseDialog extends JDialog {

  private SwingBuilder sb
  private Date closeDate
  private JComboBox terminalInput
  private List<Terminal> terminals

  TerminalCloseDialog( Date closeDate ) {
    this.closeDate = closeDate
    sb = new SwingBuilder()
    terminals = ComboBoxController.allTerminalsPlusTodas
    buildUI()
  }

  private void buildUI( ) {
    Point location = MainWindow.instance.location
    location.x += 60
    location.y += 60
    sb.dialog( this,
        title: "Cierres de Terminal del día ${closeDate?.format( 'dd/MM/yyyy' )}",
        resizable: false,
        pack: true,
        modal: true,
        layout: new MigLayout( 'fill,wrap 2', '[fill]' ),
        location: location
    ) {
      label( 'Terminal' )
      terminalInput = comboBox( items: terminals*.description )
      panel( layout: new MigLayout( 'fill', '[right]' ), constraints: 'span' ) {
        button( 'Imprimir', defaultButton: true, actionPerformed: printTicket )
        button( 'Cancelar', actionPerformed: {dispose()} )
      }
    }
  }

  private def printTicket = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    String description = terminalInput.selectedItem
    boolean terminal = DailyCloseController.printDailyClose( closeDate, description )
      if(terminal){
          sb.optionPane().showMessageDialog(null, 'No existe pago para ninguna terminal', 'Mensaje', JOptionPane.INFORMATION_MESSAGE)
      }
    source.enabled = true
  }
}

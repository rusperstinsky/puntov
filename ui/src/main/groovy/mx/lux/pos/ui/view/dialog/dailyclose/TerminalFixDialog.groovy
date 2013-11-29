package mx.lux.pos.ui.view.dialog.dailyclose

import groovy.model.DefaultTableModel
import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.DailyCloseController
import mx.lux.pos.ui.model.Payment
import net.miginfocom.swing.MigLayout

import java.awt.event.ActionEvent
import java.awt.event.MouseEvent
import javax.swing.*
import mx.lux.pos.ui.model.Branch
import mx.lux.pos.ui.model.Session
import mx.lux.pos.ui.model.SessionItem

class TerminalFixDialog extends JDialog {

  private SwingBuilder sb
  private Date date
  //private JTextField invoiceFilter
  private JTextField terminalFilter
  private JTextField planFilter
  private List<Payment> payments
  private DefaultTableModel paymentsModel
  private Branch branch

  TerminalFixDialog( Date date ) {
    this.date = date
    sb = new SwingBuilder()
    branch = Session.get( SessionItem.BRANCH ) as Branch
    payments = [ ] as ObservableList
    buildUI()
    fetchPayments()
  }

  private void buildUI( ) {
    sb.dialog( this,
        title: 'Correcci\u00f3n de terminales',
        resizable: false,
        pack: true,
        modal: true,
        layout: new MigLayout( 'fill,wrap', '[fill]' )
    ) {
      panel( border: titledBorder( 'B\u00fasqueda' ), layout: new MigLayout( '', '[][fill,100][][fill,100]' ) ) {
        /*label( 'Ticket  ' )
        label( "${branch?.costCenter}-" )
        invoiceFilter = textField(  )*/
        label( 'Terminal' )
        terminalFilter = textField()
        label( 'Plan' )
        planFilter = textField()
        button( 'Buscar', actionPerformed: doSearch )
        button( 'Limpiar', actionPerformed: doClean )
      }

      scrollPane( border: titledBorder( 'Pagos con Tarjeta' ), constraints: 'h 250!' ) {
        table( selectionMode: ListSelectionModel.SINGLE_SELECTION, mouseClicked: doClick ) {
          paymentsModel = tableModel( list: payments ) {
            closureColumn( header: 'Ticket', read: {Payment tmp -> tmp?.factura} )
            closureColumn( header: 'Terminal', read: {Payment tmp -> tmp?.terminal} )
            closureColumn( header: 'Plan', read: {Payment tmp -> tmp?.planId} )
            closureColumn( header: 'Importe', read: {Payment tmp -> tmp?.amount} )
          } as DefaultTableModel
        }
      }

      panel( layout: new MigLayout( 'fill', '[right]' ) ) {
        button( text: 'Cancelar', actionPerformed: {dispose()} )
      }
    }
  }

  private void fetchPayments( ) {
    payments.clear()
    payments.addAll( DailyCloseController.findPaymentsByDayByInvoiceByTerminal( date, terminalFilter.text, planFilter.text ) )
    paymentsModel.fireTableDataChanged()
  }

  private def doSearch = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    payments.clear()
    payments.addAll( DailyCloseController.findPaymentsByDayByInvoiceByTerminal( date, terminalFilter.text, planFilter.text ) )
    paymentsModel.fireTableDataChanged()
    source.enabled = true
  }

  private def doClean = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    //invoiceFilter.text = null
    terminalFilter.text = null
    planFilter.text = null
    fetchPayments()
    source.enabled = true
  }

  private def doClick = { MouseEvent ev ->
    if ( SwingUtilities.isLeftMouseButton( ev ) ) {
      Payment payment = ev.source.selectedElement as Payment
      if ( ev.clickCount == 2 && payment?.id ) {
        new EditPaymentDialog( payment ).show()
        fetchPayments()
      }
    }
  }
}

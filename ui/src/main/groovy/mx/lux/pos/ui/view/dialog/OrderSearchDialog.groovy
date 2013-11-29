package mx.lux.pos.ui.view.dialog

import groovy.model.DefaultTableModel
import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.AccessController
import mx.lux.pos.ui.controller.OrderController
import mx.lux.pos.ui.view.renderer.DateCellRenderer
import mx.lux.pos.ui.view.renderer.MoneyCellRenderer
import net.miginfocom.swing.MigLayout

import java.awt.Component
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import java.text.DateFormat
import java.text.SimpleDateFormat

import mx.lux.pos.ui.model.*

import javax.swing.*

class OrderSearchDialog extends JDialog {

  private static final String DATE_FORMAT = 'dd-MM-yyyy'

  private SwingBuilder sb
  private Map<String, Object> params
  private List<Order> results
  private JFormattedTextField dateFrom
  private JFormattedTextField dateTo
  private JTextField folio
  private JTextField ticket
  private JTextField employee
  private JLabel employeeName
  private JLabel message
  private DefaultTableModel ordersModel
  private Branch branch

  OrderSearchDialog( Component parent, List<Order> results ) {
    this.results = [ ]
    this.results.addAll( results )
    sb = new SwingBuilder()
    params = [ : ]
    branch = Session.get( SessionItem.BRANCH ) as Branch
    buildUI( parent )
    doBindings()
  }

  List<Order> getResults( ) {
    return results
  }

  private void buildUI( Component parent ) {
    DateFormat dateFormat = new SimpleDateFormat( DATE_FORMAT )
    sb.dialog( this,
        title: 'Selector de Facturas',
        location: parent.location,
        resizable: false,
        modal: true,
        pack: true,
        layout: new MigLayout( 'fill,wrap', '[fill,700!]', '[fill]' )
    ) {
      panel( layout: new MigLayout( 'fill,insets 0', '[fill][fill,right]' ) ) {
        panel( layout: new MigLayout( 'wrap 4', '[]10[fill,200]' ) ) {
          label( 'De:' )
          dateFrom = formattedTextField( value: new Date(), format: dateFormat, toolTipText: 'dd-mm-aaaa' )

          label( 'A:' )
          dateTo = formattedTextField( value: new Date(), format: dateFormat, toolTipText: 'dd-mm-aaaa' )

          label( 'Folio:' )
          folio = textField( document: new UpperCaseDocument() )

          label( 'Ticket:' )
          ticket = textField( "${branch?.costCenter}-" )

          label( 'Empleado:' )
          employee = textField( document: new UpperCaseDocument(), keyReleased: employeeChanged )
          employeeName = label( constraints: 'span' )
        }

        panel( layout: new MigLayout( 'fill,right,wrap', '[fill,100!]' ) ) {
          button( text: 'Limpiar', actionPerformed: doClear )
          button( text: 'Buscar', defaultButton: true, actionPerformed: doSearch )
        }
      }

      message = label( "Resultados: ${results.size()}" )

      scrollPane( constraints: 'h 240!' ) {
        table( selectionMode: ListSelectionModel.SINGLE_SELECTION, mouseClicked: doSelectOrderClick ) {
          ordersModel = tableModel( list: results ) {
            closureColumn( header: 'Fecha', read: {Order tmp -> tmp?.date}, cellRenderer: new DateCellRenderer() )
            closureColumn( header: 'Estado', read: {Order tmp -> tmp?.status}, maxWidth: 50 )
            closureColumn( header: 'Folio', read: {Order tmp -> tmp?.id} )
            closureColumn( header: 'Ticket', read: {Order tmp -> tmp?.ticket} )
            closureColumn( header: 'Cliente', read: {Order tmp -> tmp?.customer?.fullName} )
            closureColumn( header: 'Artículos', read: {Order tmp -> tmp?.items*.item*.name} )
            closureColumn( header: 'Monto', read: {Order tmp -> tmp?.total}, cellRenderer: new MoneyCellRenderer() )
          } as DefaultTableModel
        }
      }

      panel( layout: new MigLayout( 'right', '[fill,100!]' ) ) {
        button( text: 'Aceptar', actionPerformed: {dispose()} )
      }
    }
  }

  private void doBindings( ) {
    sb.build {
      bean( dateFrom, value: bind( target: params, targetProperty: 'dateFrom' ) )
      bean( dateTo, value: bind( target: params, targetProperty: 'dateTo' ) )
      bean( folio, text: bind( target: params, targetProperty: 'folio' ) )
      bean( ticket, text: bind( target: params, targetProperty: 'ticket' ) )
      bean( employee, text: bind( target: params, targetProperty: 'employee' ) )
    }
  }

  private def employeeChanged = { KeyEvent ev ->
    JTextField source = ev.source as JTextField
    User user = AccessController.getUser( source.text )
    employeeName.text = user?.fullName ?: ''
  }

  private def doClear = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    dateFrom.value = null
    dateTo.value = null
    ticket.text = null
    folio.text = null
    employee.text = null
    employeeName.text = null
    results.clear()
    ordersModel.fireTableDataChanged()
    message.text = "Resultados:"
    source.enabled = true
  }

  private def doSearch = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    results.clear()
    results.addAll( OrderController.findOrdersByParameters( params ) )
    ordersModel.fireTableDataChanged()
    message.text = "Resultados: ${results.size()}"
    source.enabled = true
  }

  private def doSelectOrderClick = { MouseEvent ev ->
    if ( SwingUtilities.isLeftMouseButton( ev ) ) {
      Order selection = ev.source.selectedElement as Order
      if ( ev.clickCount == 2 && selection?.id ) {
        results.clear()
        results.add( selection )
        dispose()
      }
    }
  }
}

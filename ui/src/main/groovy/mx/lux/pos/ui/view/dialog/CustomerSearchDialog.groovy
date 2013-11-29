package mx.lux.pos.ui.view.dialog

import groovy.model.DefaultTableModel
import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.CustomerController
import mx.lux.pos.ui.model.Customer
import mx.lux.pos.ui.model.CustomerType
import mx.lux.pos.ui.model.Order
import mx.lux.pos.ui.model.UpperCaseDocument
import mx.lux.pos.ui.view.renderer.DateCellRenderer
import net.miginfocom.swing.MigLayout

import java.awt.Component
import java.awt.event.ActionEvent
import java.awt.event.MouseEvent
import javax.swing.*

class CustomerSearchDialog extends JDialog {

  private SwingBuilder sb
  private Customer customer
  private List<Customer> customers
  private JTextField firstName
  private JTextField fathersName
  private JTextField mothersName
  private JLabel message
  private JButton newButton
  private DefaultTableModel customersModel
  private JButton btnCancel

  Boolean canceled

  CustomerSearchDialog( Component parent, Order order ) {
    sb = new SwingBuilder()
    customers = [ ] as ObservableList
    customer = new Customer( type: order?.customer?.type )
    if ( order?.customer?.id ) {
      customer.id = order.customer.id
      customers.add( order.customer )
    }
    buildUI( parent )
    doBindings()
  }

  Customer getCustomer( ) {
    return customer
  }

  private void buildUI( Component parent ) {
    sb.dialog( this,
        title: "Búsqueda de cliente",
        location: parent.locationOnScreen,
        resizable: false,
        modal: true,
        pack: true,
        layout: new MigLayout( 'wrap 2', '[fill][fill]' )
    ) {
      panel( layout: new MigLayout( 'wrap 2', '[][fill,270!]' ) ) {
        label( 'Nombre' )
        firstName = textField( document: new UpperCaseDocument() )

        label( 'Apellido Paterno' )
        fathersName = textField( document: new UpperCaseDocument() )

        label( 'Apellido Materno' )
        mothersName = textField( document: new UpperCaseDocument() )
      }

      panel( layout: new MigLayout( 'fill,right,wrap', '[fill,100!]' ) ) {
        button( 'Buscar', defaultButton: true, actionPerformed: doSearch )
        button( 'Limpiar', actionPerformed: doClear )
      }

      message = label( 'Resultados:', constraints: 'span' )

      scrollPane( constraints: 'span, h 200!' ) {
        table( selectionMode: ListSelectionModel.SINGLE_SELECTION, mouseClicked: onCustomersClick, mousePressed: onCustomersPress ) {
          customersModel = tableModel( list: customers ) {
            closureColumn( header: 'Nombre', read: {Customer tmp -> tmp?.name} )
            closureColumn( header: 'Apellido Paterno', read: {Customer tmp -> tmp?.fathersName} )
            closureColumn( header: 'Apellido Materno', read: {Customer tmp -> tmp?.mothersName} )
            closureColumn( header: 'Fecha de Nacimiento', read: {Customer tmp -> tmp?.dob}, cellRenderer: new DateCellRenderer(), maxWidth: 100 )
          } as DefaultTableModel
        }
      }

      panel( layout: new MigLayout( 'right', '[fill,100!]' ), constraints: 'span' ) {
        newButton = button( 'Nuevo', enabled: false, actionPerformed: doNewCustomer )
        btnCancel = button( 'Cancelar', actionPerformed: doCancel )
      }
    }
  }

  private void doBindings( ) {
    sb.build {
      bean( firstName, text: bind( source: customer, sourceProperty: 'name', mutual: true ) )
      bean( fathersName, text: bind( source: customer, sourceProperty: 'fathersName', mutual: true ) )
      bean( mothersName, text: bind( source: customer, sourceProperty: 'mothersName', mutual: true ) )
    }
  }

  private def doSearch = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    customers.clear()
    customers.addAll( CustomerController.findCustomers( customer ) )
    customersModel.fireTableDataChanged()
    newButton.enabled = true
    message.text = "Resultados: ${customers.size()}"
    source.enabled = true
  }

  private def doClear = {
    fathersName.text = null
    mothersName.text = null
    firstName.text = null
    message.text = 'Resultados:'
    customers.clear()
    customersModel.fireTableDataChanged()
  }

  private def onCustomersClick = { MouseEvent ev ->
    if ( SwingUtilities.isLeftMouseButton( ev ) ) {
      if ( ev.clickCount == 2 ) {
        customer = ev.source.selectedElement
        dispose()
      }
    }
  }

  private def onCustomersPress = { MouseEvent ev ->
    Customer customer = ev.source.selectedElement as Customer
    if ( SwingUtilities.isRightMouseButton( ev ) && customer?.id ) {
      sb.popupMenu {
        menuItem( 'Editar',
            actionPerformed: {
              dispose()
              openCustomerDialog( customer, true )
            }
        )
      }.show( ev.component, ev.x, ev.y )
    }
  }

  private def doNewCustomer = {
    dispose()
    customer = new Customer()
    openCustomerDialog( customer, false )
  }

  private def doCancel = {
    canceled = true
    dispose()
  }

  private void openCustomerDialog( Customer customer, boolean editar ) {
    if ( CustomerType.FOREIGN.equals( customer.type ) ) {
      ForeignCustomerDialog dialog = new ForeignCustomerDialog( this, customer, editar )
      dialog.show()
      this.customer = dialog.customer
    } else {
      CustomerDialog dialog = new CustomerDialog( this, customer, editar )
      dialog.show()
      this.customer = dialog.customer
    }
  }
}

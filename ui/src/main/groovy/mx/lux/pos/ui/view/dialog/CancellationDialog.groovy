package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.AccessController
import mx.lux.pos.ui.controller.CancellationController
import mx.lux.pos.ui.controller.OrderController
import mx.lux.pos.ui.model.Order
import mx.lux.pos.ui.model.Payment
import mx.lux.pos.ui.model.UpperCaseDocument
import mx.lux.pos.ui.view.renderer.DateCellRenderer
import mx.lux.pos.ui.view.renderer.MoneyCellRenderer
import net.miginfocom.swing.MigLayout

import java.awt.Component
import java.awt.event.ActionEvent
import javax.swing.*

class CancellationDialog extends JDialog {

  private SwingBuilder sb
  private Order order
  private JLabel billField
  private JLabel customerField
  private JTextArea itemsField
  private JTextArea commentsField
  private JComboBox reasonField
  private JButton transferButton
  private JButton returnButton
  private List<String> reasons

  CancellationDialog( Component parent, String orderId ) {
    sb = new SwingBuilder()
    order = OrderController.getOrder( orderId )
    reasons = CancellationController.findAllCancellationReasons()
    buildUI( parent )
    doBindings()
  }

  private void buildUI( Component parent ) {
    sb.dialog( this,
        title: 'Cancelaci\u00f3n',
        location: parent.locationOnScreen,
        resizable: false,
        modal: true,
        pack: true,
        layout: new MigLayout( 'fill,wrap 2', '[][fill]', '[fill]' )
    ) {
      label( 'Ticket' )
      billField = label()

      label( 'Cliente' )
      customerField = label()

      label( 'Art\u00edculos' )
      scrollPane( constraints: 'h 40!' ) {
        itemsField = textArea( lineWrap: true, editable: false )
      }

      label( 'Pagos' )
      scrollPane( constraints: 'h 100!,w 285!' ) {
        table( selectionMode: ListSelectionModel.SINGLE_SELECTION ) {
          tableModel( list: order.payments ) {
            closureColumn( header: 'Fecha', read: {Payment tmp -> tmp?.date}, cellRenderer: new DateCellRenderer() )
            closureColumn( header: 'Tipo', read: {Payment tmp -> tmp?.paymentTypeId} )
            closureColumn( header: 'Monto', read: {Payment tmp -> tmp?.amount}, cellRenderer: new MoneyCellRenderer() )
          }
        }
      }

      label( 'Raz\u00f3n' )
      reasonField = comboBox( items: reasons, constraints: 'w 285!' )

      label( 'Observaciones' )
      scrollPane( constraints: 'h 40!' ) {
        commentsField = textArea( document: new UpperCaseDocument(), lineWrap: true )
      }

      panel( layout: new MigLayout( 'right', '[fill,100]' ), constraints: 'span' ) {
        transferButton = button( 'Transferencia', actionPerformed: doTransfer )
        returnButton = button( 'Devoluci\u00f3n', actionPerformed: doRefund )
        button( 'Cerrar', actionPerformed: {dispose()} )
      }
    }
  }

  private void doBindings( ) {
    sb.build {
      bean( billField, text: bind {order.ticket} )
      bean( customerField, text: bind {order.customer?.fullName} )
      bean( itemsField, text: bind {order.items*.item*.name} )
      bean( transferButton, enabled: bind {!'T'.equalsIgnoreCase( order.status )} )
      bean( returnButton, enabled: bind {!'T'.equalsIgnoreCase( order.status )} )
    }
  }

  private boolean allowLateCancellation( ) {
    if ( CancellationController.allowLateCancellation( order.id ) ) {
      return true
    } else {
      sb.optionPane(
          message: "No se permite cancelaci\u00f3n posterior \na la fecha de compra: ${order.date?.format( 'dd-MM-yyyy HH:mm' )}",
          optionType: JOptionPane.DEFAULT_OPTION
      ).createDialog( this, "No se permite cancelaci\u00f3n" )
          .show()
    }
    return false
  }

  private boolean cancelOrder( ) {
    if ( CancellationController.cancelOrder( order.id, reasonField.selectedItem as String, commentsField.text ) ) {
      CancellationController.printCancellationPlan( order.id )
      return true
    } else {
      sb.optionPane( message: "Ocurrio un error al cancelar", optionType: JOptionPane.DEFAULT_OPTION )
          .createDialog( this, "Error" )
          .show()
      return false
    }
  }

  private def doTransfer = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    if ( allowLateCancellation() ) {
      if ( cancelOrder() ) {
        dispose()
      }
    }
    source.enabled = true
  }

  private def doRefund = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    if ( allowLateCancellation() ) {
      boolean authorized
      if ( AccessController.authorizerInSession ) {
        authorized = true
      } else {
        AuthorizationDialog authDialog = new AuthorizationDialog( this, "Cancelaci\u00f3n requiere autorizaci\u00f3n", false )
        authDialog.show()
        authorized = authDialog.authorized
      }
      if ( authorized ) {
        if ( cancelOrder() ) {
          dispose()
          new RefundDialog( this, order.id ).show()
        }
      }
    }
    source.enabled = true
  }
}

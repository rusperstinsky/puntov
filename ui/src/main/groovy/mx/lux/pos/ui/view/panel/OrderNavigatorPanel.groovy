package mx.lux.pos.ui.view.panel

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.OrderController
import mx.lux.pos.ui.model.Order
import mx.lux.pos.ui.view.dialog.OrderSearchDialog
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang3.StringUtils
import org.springframework.core.io.ClassPathResource

import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.JButton
import javax.swing.JPanel
import mx.lux.pos.ui.controller.CancellationController

class OrderNavigatorPanel extends JPanel {

  private SwingBuilder sb
  private Order order
  private List<Order> results
  private JButton searchButton
  private Closure update

  OrderNavigatorPanel( Order order, Closure update ) {
    this.order = order
    this.update = update
    sb = new SwingBuilder()
    results = [ ] as ObservableList
    buildUI()
    fetchLastOrders()
  }

  private void buildUI( ) {
    Dimension buttonSize = [ 32, 32 ]
    sb.panel( this, layout: new MigLayout( 'wrap 5,ins 0', '[shrink]' ) ) {
      button( icon: imageIcon( url: new ClassPathResource( 'img/first_16.png' )?.URL ), maximumSize: buttonSize, actionPerformed: goFirst )
      button( icon: imageIcon( url: new ClassPathResource( 'img/prev_16.png' )?.URL ), maximumSize: buttonSize, actionPerformed: goPrevious )
      searchButton = button( actionPerformed: openSearchDialog )
      button( icon: imageIcon( url: new ClassPathResource( 'img/next_16.png' )?.URL ), maximumSize: buttonSize, actionPerformed: goNext )
      button( icon: imageIcon( url: new ClassPathResource( 'img/last_16.png' )?.URL ), maximumSize: buttonSize, actionPerformed: goLast )
    }
  }

  private Order getElementAt( Integer idx ) {
    if ( results.any() ) {
      Integer max = ( results.size() - 1 )
      if ( idx > max ) {
        return results.get( max )
      } else if ( idx < 0 ) {
        return results.get( 0 )
      }
      return results.get( idx )
    }
    return order
  }

  private void fetchLastOrders( ) {
    results.addAll( OrderController.findLastOrders() )
    updateOrder( getElementAt( 0 ) )
    updateSearchButtonText()
  }

  private void updateOrder( Order newOrder ) {
    if ( StringUtils.isNotBlank( newOrder?.id ) ) {
      //newOrder = OrderController.getOrder( newOrder.id )
      order.id = newOrder.id
      order.bill = newOrder.bill
      order.comments = newOrder.comments
      order.status = newOrder.status
      order.date = newOrder.date
      order.branch = newOrder.branch
      order.customer = newOrder.customer
      order.items = newOrder.items
      order.payments = newOrder.payments
      order.deals = newOrder.deals
      order.total = newOrder.total
      order.paid = newOrder.paid
      order.due = newOrder.due
      order.employee = newOrder.employee
      updateSearchButtonText()
      update.call()
    }
  }

  private void updateSearchButtonText( ) {
    searchButton.text = results.any() ? "${getCurrentIndex() + 1}/${results.size()}" : "0/0"
  }

  private Integer getCurrentIndex( ) {
    return results.findIndexOf { Order tmp ->
      tmp?.id?.equalsIgnoreCase( order?.id )
    }
  }

  private def goFirst = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    Order first = getElementAt( 0 )
    updateOrder( first )
    source.enabled = true
  }

  private def goNext = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    Order next = getElementAt( getCurrentIndex() + 1 )
    updateOrder( next )
    source.enabled = true
  }

  private def goPrevious = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    Order previous = getElementAt( getCurrentIndex() - 1 )
    updateOrder( previous )
    source.enabled = true
  }

  private def goLast = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    Order last = getElementAt( results?.size() )
    updateOrder( last )
    source.enabled = true
  }

  private def openSearchDialog = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    OrderSearchDialog osd = new OrderSearchDialog( this, results )
    osd.show()
    if ( osd.results?.any() ) {
      results.clear()
      results.addAll( osd.results )
      updateOrder( getElementAt( 0 ) )
    }
    source.enabled = true
  }
}

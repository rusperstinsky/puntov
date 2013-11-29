package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.CancellationController
import mx.lux.pos.ui.controller.PaymentController
import mx.lux.pos.ui.model.Payment
import mx.lux.pos.ui.model.Refund
import mx.lux.pos.ui.view.renderer.MoneyCellRenderer
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang3.StringUtils

import java.awt.Component
import java.awt.event.ActionEvent
import java.text.NumberFormat
import javax.swing.*

class RefundDialog extends JDialog {

  private static final String DATE_FORMAT = 'dd-MM-yyyy'

  private SwingBuilder sb
  private String orderId
  private List<Payment> payments
  private List<Refund> refunds
  private List<LinkedHashMap<String, Object>> transitions
  private BigDecimal totalAmount
  private BigDecimal totalPending
  private BigDecimal totalReturn
  private BigDecimal totalTransfer
  private List<String> refundMethods

  RefundDialog( Component parent, String orderId ) {
    this.orderId = orderId
    sb = new SwingBuilder()
    totalAmount = BigDecimal.ZERO
    totalPending = BigDecimal.ZERO
    totalReturn = BigDecimal.ZERO
    totalTransfer = BigDecimal.ZERO
    refundMethods = [ 'EFECTIVO', 'ORIGINAL' ]
    fetchData()
    buildUI( parent )
  }

  private void fetchData( ) {
    transitions = [ ]
    payments = PaymentController.findPaymentsByOrderId( orderId )
    payments.retainAll { Payment payment ->
      payment?.refundable
    }
    payments.each { Payment payment ->
      totalAmount += payment?.amount ?: 0
      totalPending += payment?.refundable ?: 0
      transitions.add( [
          method: payment?.paymentTypeId,
          amount: payment?.refundable,
          type: 'p',
          date: payment?.date?.format( DATE_FORMAT )
      ] )
    }
    refunds = CancellationController.findRefundsByOrderId( orderId )
    refunds.each { Refund refund ->
      String type = refund?.type
      BigDecimal amount = refund?.amount ?: 0
      if ( 'd'.equalsIgnoreCase( type ) ) {
        totalReturn += amount
      } else if ( 't'.equalsIgnoreCase( type ) ) {
        totalTransfer += amount
      }
      transitions.add( [
          method: refund?.paymentTypeId,
          amount: refund?.amount,
          type: refund?.type,
          transfer: refund?.transfer,
          date: refund?.date?.format( DATE_FORMAT )
      ] )
    }
  }

  private void buildUI( Component parent ) {
    NumberFormat formatter = NumberFormat.getCurrencyInstance( Locale.US )
    sb.dialog( this,
        title: 'Devoluci\u00f3n',
        location: parent.location,
        resizable: false,
        modal: true,
        pack: true,
        layout: new MigLayout( 'fill,wrap', '[fill]' )
    ) {
      scrollPane( border: titledBorder( 'Devoluciones' ), constraints: 'h 150!' ) {
        table( selectionMode: ListSelectionModel.SINGLE_SELECTION ) {
          tableModel( list: payments ) {
            closureColumn( header: 'Forma Pago', read: {Payment pmt -> pmt?.paymentType} )
            closureColumn( header: 'Monto', read: {Payment pmt -> pmt?.refundable}, cellRenderer: new MoneyCellRenderer() )
            closureColumn(
                header: 'Forma Devoluci\u00f3n',
                read: {Payment pmt -> pmt?.refundMethod},
                write: {Payment pmt, String val -> pmt?.refundMethod = val},
                cellEditor: new DefaultCellEditor( comboBox( items: refundMethods ) )
            )
          }
        }
      }

      scrollPane( border: titledBorder( 'Pagos' ), constraints: 'h 150!' ) {
        table( selectionMode: ListSelectionModel.SINGLE_SELECTION ) {
          tableModel( list: transitions ) {
            propertyColumn( header: 'Forma', propertyName: 'method', editable: false )
            propertyColumn( header: 'Monto', propertyName: 'amount', editable: false, cellRenderer: new MoneyCellRenderer() )
            propertyColumn( header: 'Tipo', propertyName: 'type', editable: false )
            propertyColumn( header: 'Factura', propertyName: 'transfer', editable: false )
            propertyColumn( header: 'Fecha', propertyName: 'date', editable: false )
          }
        }
      }

      panel( border: titledBorder( 'Resumen de Cancelaci\u00f3n' ), layout: new MigLayout( 'fill,wrap 4', '[fill]' ) ) {
        label( 'Monto' )
        label( 'Devoluci\u00f3n' )
        label( 'Transferencia' )
        label( 'Pendiente' )
        label( "${formatter.format( totalAmount )}" )
        label( "${formatter.format( totalReturn )}" )
        label( "${formatter.format( totalTransfer )}" )
        label( "${formatter.format( totalPending )}" )
      }

      panel( layout: new MigLayout( 'right', '[fill,100]' ), constraints: 'span' ) {
        button( 'Aplicar', enabled: totalPending > 0, actionPerformed: doRefund )
        button( 'Cerrar', actionPerformed: {dispose()} )
      }
    }
  }

  private boolean hasValidData( ) {
    boolean result = true
    payments.each { Payment pmt ->
      String tmpRefundMethod = pmt?.refundMethod
      if ( StringUtils.isBlank( tmpRefundMethod ) || !refundMethods.contains( tmpRefundMethod ) ) {
        result = false
      }
    }
    if ( !result ) {
      sb.optionPane(
          message: 'Se deben registar todas las formas de devoluci\u00f3n',
          messageType: JOptionPane.ERROR_MESSAGE
      ).createDialog( this, 'Formas de devoluci\u00f3n inv\u00e1lidas' )
          .show()
    }
    return result
  }

  private def doRefund = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    if ( hasValidData() ) {
      Map<Integer, String> creditRefunds = [ : ]
      payments.each { Payment pmt ->
        creditRefunds.put( pmt?.id, pmt?.refundMethod )
      }
      if ( CancellationController.refundPaymentsCreditFromOrder( orderId, creditRefunds ) ) {
        CancellationController.printOrderCancellation( orderId )
        dispose()
      } else {
        sb.optionPane(
            message: 'Ocurrio un error al registrar devoluciones',
            messageType: JOptionPane.ERROR_MESSAGE
        ).createDialog( this, 'No se registran devoluciones' )
            .show()
      }
    }
    source.enabled = true
  }
}

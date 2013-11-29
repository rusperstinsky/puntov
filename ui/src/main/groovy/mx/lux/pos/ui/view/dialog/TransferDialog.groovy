package mx.lux.pos.ui.view.dialog

import groovy.model.DefaultTableModel
import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.CancellationController
import mx.lux.pos.ui.controller.OrderController
import mx.lux.pos.ui.controller.PaymentController
import mx.lux.pos.ui.view.renderer.MoneyCellRenderer
import net.miginfocom.swing.MigLayout

import java.awt.Color
import java.awt.Component
import java.awt.event.ActionEvent
import java.text.NumberFormat

import mx.lux.pos.ui.model.*

import javax.swing.*

class TransferDialog extends JDialog {

  private static final String DATE_FORMAT = 'dd-MM-yyyy'

  private SwingBuilder sb
  private String fromOrderId
  private String toOrderId
  private Branch branch
  private JTextField ticketField
  private JLabel messageLabel
  private JLabel amountLabel
  private JLabel returnLabel
  private JLabel transferLabel
  private JLabel pendingLabel
  private DefaultTableModel paymentsModel
  private DefaultTableModel transitionsModel
  private JButton applyButton
  private BigDecimal totalAmount
  private BigDecimal totalPending
  private BigDecimal totalReturn
  private BigDecimal totalTransfer
  private BigDecimal totalDue
  private List<Payment> payments
  private List<Refund> refunds
  private List<LinkedHashMap<String, Object>> transitions
  private NumberFormat formatter

  TransferDialog( Component parent, String toOrderId ) {
    this.toOrderId = toOrderId
    sb = new SwingBuilder()
    branch = Session.get( SessionItem.BRANCH ) as Branch
    totalAmount = BigDecimal.ZERO
    totalPending = BigDecimal.ZERO
    totalReturn = BigDecimal.ZERO
    totalTransfer = BigDecimal.ZERO
    totalDue = BigDecimal.ZERO
    payments = [ ]
    refunds = [ ]
    transitions = [ ]
    formatter = NumberFormat.getCurrencyInstance( Locale.US )
    buildUI( parent )
    doBindings()
  }

  private void buildUI( Component parent ) {
    sb.dialog( this,
        title: 'Transferencia',
        location: parent.location,
        resizable: false,
        modal: true,
        pack: true,
        layout: new MigLayout( 'fill,wrap', '[fill]' )
    ) {
      panel( layout: new MigLayout( 'fill,wrap 3', '[][fill,120!][fill]' ) ) {
        label( 'Ticket' )
        ticketField = textField( "${branch?.costCenter}-" )
        button( 'Buscar', defaultButton: true, actionPerformed: doSearch )

        messageLabel = label( foreground: Color.RED, constraints: 'span' )
      }

      scrollPane( border: titledBorder( 'Pagos a Transferir' ), constraints: 'h 150!' ) {
        table( selectionMode: ListSelectionModel.SINGLE_SELECTION ) {
          paymentsModel = tableModel( list: payments ) {
            closureColumn( header: 'Forma Pago', read: {Payment pmt -> pmt?.paymentType} )
            closureColumn( header: 'Monto', read: {Payment pmt -> pmt?.refundable}, cellRenderer: new MoneyCellRenderer() )
            closureColumn(
                header: 'Transferir',
                read: {Payment pmt -> pmt?.refund},
                write: {Payment pmt, String val -> pmt?.refund = val?.isBigDecimal() ? val.toBigDecimal() : 0},
                cellRenderer: new MoneyCellRenderer()
            )
          } as DefaultTableModel
        }
      }

      scrollPane( border: titledBorder( 'Pagos' ), constraints: 'h 150!' ) {
        table( selectionMode: ListSelectionModel.SINGLE_SELECTION ) {
          transitionsModel = tableModel( list: transitions ) {
            propertyColumn( header: 'Forma', propertyName: 'method', editable: false )
            propertyColumn( header: 'Monto', propertyName: 'amount', editable: false, cellRenderer: new MoneyCellRenderer() )
            propertyColumn( header: 'Tipo', propertyName: 'type', editable: false )
            propertyColumn( header: 'Factura', propertyName: 'transfer', editable: false )
            propertyColumn( header: 'Fecha', propertyName: 'date', editable: false )
          } as DefaultTableModel
        }
      }

      panel( border: titledBorder( 'Resumen de Cancelaci\u00f3n' ), layout: new MigLayout( 'fill,wrap 4', '[fill]' ) ) {
        label( 'Monto' )
        label( 'Devoluci\u00f3n' )
        label( 'Transferencia' )
        label( 'Pendiente' )
        amountLabel = label()
        returnLabel = label()
        transferLabel = label()
        pendingLabel = label()
      }

      panel( layout: new MigLayout( 'right', '[fill,100]' ), constraints: 'span' ) {
        applyButton = button( 'Aplicar', actionPerformed: doTransfer )
        button( 'Cerrar', actionPerformed: {dispose()} )
      }
    }
  }

  private void doBindings( ) {
    sb.build {
      bean( amountLabel, text: bind {formatter.format( totalAmount )} )
      bean( returnLabel, text: bind {formatter.format( totalReturn )} )
      bean( transferLabel, text: bind {formatter.format( totalTransfer )} )
      bean( pendingLabel, text: bind {formatter.format( totalPending )} )
      bean( applyButton, enabled: bind {totalPending > 0} )
    }
    paymentsModel.fireTableDataChanged()
    transitionsModel.fireTableDataChanged()
  }

  private void clearData( ) {
    fromOrderId = null
    totalAmount = BigDecimal.ZERO
    totalPending = BigDecimal.ZERO
    totalReturn = BigDecimal.ZERO
    totalTransfer = BigDecimal.ZERO
    totalDue = BigDecimal.ZERO
    payments.clear()
    refunds.clear()
    transitions.clear()
    doBindings()
  }

  private void fetchData( String orderId ) {
    clearData()
    fromOrderId = orderId
    payments.addAll( PaymentController.findPaymentsByOrderId( orderId ) )
    payments.retainAll { Payment payment ->
      payment?.refundable
    }
    payments.each { Payment payment ->
      totalAmount += payment?.amount ?: 0
      totalPending += payment?.refundable ?: 0
      transitions.add( [
          method: payment?.paymentTypeId,
          amount: payment?.amount,
          type: 'p',
          date: payment?.date?.format( DATE_FORMAT )
      ] )
    }
    refunds.addAll( CancellationController.findRefundsByOrderId( orderId ) )
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
    Order toOrder = OrderController.getOrder( toOrderId )
    totalDue = toOrder?.due ?: 0
    doBindings()
  }

  private boolean hasValidData( ) {
    boolean result = true
    BigDecimal tmpTotalRefund = BigDecimal.ZERO
    payments.each { Payment pmt ->
      BigDecimal toRefund = pmt?.refund ?: BigDecimal.ZERO
      BigDecimal refundable = pmt?.refundable ?: BigDecimal.ZERO
      tmpTotalRefund += toRefund
      if ( toRefund > refundable ) {
        sb.optionPane(
            message: """Monto de transferencia: ${formatter.format( toRefund )} debe ser menor
al permitido: ${formatter.format( refundable )}""",
            messageType: JOptionPane.ERROR_MESSAGE
        ).createDialog( this, 'Transferencia inv\u00e1lida' )
            .show()
        result = false
      }
      if ( toRefund > totalDue ) {
        sb.optionPane(
            message: """Monto de transferencia: ${formatter.format( toRefund )} debe ser menor
al saldo: ${formatter.format( totalDue )}""",
            messageType: JOptionPane.ERROR_MESSAGE
        ).createDialog( this, 'Transferencia inv\u00e1lida' )
            .show()
        result = false
      }
    }
    if ( !tmpTotalRefund ) {
      sb.optionPane(
          message: 'Se debe registar al menos una transferencia',
          messageType: JOptionPane.ERROR_MESSAGE
      ).createDialog( this, 'Transferencias inv\u00e1lidas' )
          .show()
      return false
    }
    if ( tmpTotalRefund > totalPending ) {
      sb.optionPane(
          message: 'El monto transferencias debe ser menor al monto pendiente',
          messageType: JOptionPane.ERROR_MESSAGE
      ).createDialog( this, 'Transferencias inv\u00e1lidas' )
          .show()
      return false
    }
    return result
  }

  private def doSearch = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    messageLabel.text = null
    Order order = OrderController.findOrderByTicket( ticketField.text )
    if ( order?.id ) {
      fetchData( order.id )
    } else {
      clearData()
      messageLabel.text = 'No se encontraron resultados'
    }
    pack()
    source.enabled = true
  }

  private def doTransfer = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    if ( hasValidData() ) {
      Map<Integer, BigDecimal> creditTransfers = [ : ]
      payments.each { Payment pmt ->
        creditTransfers.put( pmt?.id, pmt?.refund ?: BigDecimal.ZERO )
      }
      if ( CancellationController.transferPaymentsCreditToOrder( fromOrderId, toOrderId, creditTransfers ) ) {
        dispose()
      } else {
        sb.optionPane(
            message: 'Ocurrio un error al registrar transferencias',
            messageType: JOptionPane.ERROR_MESSAGE
        ).createDialog( this, 'No se registran transferencias' )
            .show()
      }
    }
    source.enabled = true
  }
}

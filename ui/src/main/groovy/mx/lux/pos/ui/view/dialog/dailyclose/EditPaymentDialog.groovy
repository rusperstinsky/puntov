package mx.lux.pos.ui.view.dialog.dailyclose

import groovy.swing.SwingBuilder
import mx.lux.pos.service.business.Registry
import mx.lux.pos.ui.controller.DailyCloseController
import mx.lux.pos.ui.controller.OrderController
import mx.lux.pos.ui.controller.PaymentController
import mx.lux.pos.ui.model.Payment
import mx.lux.pos.ui.model.Plan
import mx.lux.pos.ui.model.Terminal
import net.miginfocom.swing.MigLayout

import javax.swing.JLabel
import javax.swing.JTextField
import java.awt.event.ActionEvent
import java.awt.event.ItemEvent
import java.math.RoundingMode
import java.text.NumberFormat
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JDialog
import javax.swing.JOptionPane

class EditPaymentDialog extends JDialog {

  private SwingBuilder sb
  private Payment tmpPayment
  private JComboBox terminal
  private JComboBox plan
  private JTextField planUSD
  private JLabel lblMontoUSD
  private JLabel lblPlan
  private List<Terminal> terminals
  private List<Plan> plans
  private String planId
  private final String TAG_TARJETA_DEBITO_MN = 'TDM'

  EditPaymentDialog( final Payment payment ) {
    this.tmpPayment = payment ?: new Payment()
    planId = payment.planId
    sb = new SwingBuilder()
    terminals = PaymentController.findActiveTerminals( payment.paymentTypeId )
    plans = [ ]
    buildUI()
    terminalChanged( payment )
    doBindings()
  }

  private void buildUI( ) {
    String amount = NumberFormat.getCurrencyInstance( Locale.US ).format( tmpPayment?.amount ?: 0 )
    sb.dialog( this,
        title: "Factura: ${tmpPayment?.factura}, Importe: ${amount}",
        resizable: false,
        pack: true,
        modal: true,
        layout: new MigLayout( 'fill,wrap', '[fill]' )
    ) {
      panel( layout: new MigLayout( 'fill,wrap 2', '[fill,120][fill,180]' ) ) {
        label( 'Factura' )
        label( tmpPayment?.factura )
        label( 'Importe' )
        label( amount )
        label( 'Terminal' )
        terminal = comboBox( items: terminals*.description, itemStateChanged: terminalChanged )
        lblPlan = label( 'Plan', constraints: 'hidemode 3' )
        plan = comboBox( items: plans*.description, itemStateChanged: planChanged, constraints: 'hidemode 3' )
        lblMontoUSD = label( 'Monto Dolares', constraints: 'hidemode 3', visible: false )
        planUSD = textField( constraints: 'hidemode 3', visible: false )
      }

      panel( layout: new MigLayout( 'fill', '[right]' ) ) {
        button( text: 'Guardar', actionPerformed: doSave )
        button( text: 'Cancelar', actionPerformed: {dispose()} )
      }
    }
  }

  private void doBindings( ) {
    sb.build {
      bean( terminal, selectedItem: bind( source: tmpPayment, sourceProperty: 'terminal', mutual: true ) )
      if( !Registry.isCardPaymentInDollars( tmpPayment.paymentTypeId ) ){
        bean( plan, selectedItem: bind( source: tmpPayment, sourceProperty: 'plan', mutual: true ) )
      } else if( Registry.isCardPaymentInDollars( tmpPayment.paymentTypeId ) ){
        bean( planUSD, text: bind {tmpPayment.planId} )
      }
    }
  }

  private def currencyConverter = {
      NumberFormat.getCurrencyInstance( Locale.US ).format( it ?: 0 )
  }

  private def terminalChanged = { ItemEvent ev ->
    if ( ev.stateChange == ItemEvent.SELECTED ) {
      Terminal terminalTmp = terminals.find { Terminal tmp ->
        tmp?.description?.equalsIgnoreCase( ev.item as String )
      }
      tmpPayment.terminalId = terminalTmp?.id
      if ( !Registry.isCardPaymentInDollars( tmpPayment.paymentTypeId ) && !TAG_TARJETA_DEBITO_MN.equalsIgnoreCase(tmpPayment?.paymentTypeId?.trim()) ) {
          plans = PaymentController.findPlansByTerminal( terminalTmp?.id )
          plans?.each { Plan tmp ->
              plan.addItem( tmp?.description )
          }
          plan.selectedIndex = -1
      } else {
          if( Registry.isCardPaymentInDollars( tmpPayment.paymentTypeId ) ){
            planUSD.visible = true
            lblMontoUSD.visible = true
          }
          lblPlan.visible = false
          plan.visible = false
          tmpPayment.planId = planId
      }
    } else {
      tmpPayment.terminalId = null
      plan.removeAllItems()
    }
  }

  private void terminalChanged ( Payment payment ) {
    Terminal terminalTmp = terminals.find { Terminal tmp ->
        tmp?.description?.equalsIgnoreCase( payment.terminal )
      }
      tmpPayment.terminalId = terminalTmp?.id
      if ( !Registry.isCardPaymentInDollars( tmpPayment.paymentTypeId ) && !TAG_TARJETA_DEBITO_MN.equalsIgnoreCase(tmpPayment?.paymentTypeId?.trim()) ) {
          plans = PaymentController.findPlansByTerminal( terminalTmp?.id )
          plans?.each { Plan tmp ->
              plan.addItem( tmp?.description )
          }
          plan.selectedIndex = -1
      } else {
          if( Registry.isCardPaymentInDollars( tmpPayment.paymentTypeId ) ){
              planUSD.visible = true
              lblMontoUSD.visible = true
          }
          lblPlan.visible = false
          plan.visible = false
          tmpPayment.planId = planId
      }
  }

  private def planChanged = { ItemEvent ev ->
    if ( ev.stateChange == ItemEvent.SELECTED ) {
      Plan planTmp = plans.find { Plan tmp ->
        tmp?.description?.equalsIgnoreCase( ev.item as String )
      }
      tmpPayment.planId = planTmp?.id
    } else {
      tmpPayment.planId = null
    }
  }

  private def doSave = { ActionEvent ev ->
    JButton source = ev.source as JButton
    source.enabled = false
    Boolean validUSD = true
    Payment payment = new Payment()
    Boolean terminalUpdate = false
    if( Registry.isCardPaymentInDollars( tmpPayment.paymentTypeId ) && planUSD.text.isNumber() ){
      Double tipoCambio = OrderController.requestUsdRate()
      BigDecimal pesosRec = tmpPayment.amount
      BigDecimal dolaresRec = new BigDecimal(NumberFormat.getInstance().parse(planUSD.text.trim()))
      BigDecimal dolaresCalc = pesosRec.divide(tipoCambio, 10, RoundingMode.HALF_EVEN)
      Double diferencia = dolaresCalc.subtract(dolaresRec).doubleValue()
      if( (diferencia < 1.0) && (diferencia > -1.0) ){
        tmpPayment.planId = planUSD.text
        payment = DailyCloseController.updatePayment( tmpPayment )
        terminalUpdate = DailyCloseController.updateTerminal( tmpPayment.date )
      } else {
        validUSD = false
        sb.optionPane().showMessageDialog( null, 'Cantidad de dolares incorrecta', 'Error', JOptionPane.ERROR_MESSAGE )
      }
    } else if( !Registry.isCardPaymentInDollars( tmpPayment.paymentTypeId ) ){
      payment = DailyCloseController.updatePayment( tmpPayment )
      terminalUpdate = DailyCloseController.updateTerminal( tmpPayment.date )
    }
    if ( payment?.id && terminalUpdate ) {
      //sb.optionPane().showMessageDialog( null, 'Se ha aztualizado correctamente el Terminal y/o Plan del Pago', 'Ok', JOptionPane.INFORMATION_MESSAGE )
      dispose()
    } else {
      if( validUSD ){
        sb.optionPane().showMessageDialog( null, 'Se ha producido un error al actualizar el Terminal y/o Plan del Pago', 'Error', JOptionPane.ERROR_MESSAGE )
      }
    }
    source.enabled = true
  }
}

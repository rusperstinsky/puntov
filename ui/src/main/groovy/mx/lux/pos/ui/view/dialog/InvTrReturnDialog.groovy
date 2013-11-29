package mx.lux.pos.ui.view.dialog

import groovy.model.DefaultTableModel
import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.IOController
import mx.lux.pos.ui.controller.InvTrController
import mx.lux.pos.ui.model.DailyClose
import mx.lux.pos.ui.model.ICorporateKeyVerifier
import mx.lux.pos.ui.model.InvTrSku
import mx.lux.pos.ui.model.Item
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.model.InvTr
import mx.lux.pos.ui.view.component.NumericTextField
import mx.lux.pos.ui.view.component.PercentTextField
import mx.lux.pos.ui.view.renderer.DateCellRenderer
import mx.lux.pos.ui.view.renderer.MoneyCellRenderer
import net.miginfocom.swing.MigLayout

import java.awt.BorderLayout
import java.awt.Font
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.*
import java.awt.Point
import org.apache.commons.lang3.StringUtils

import java.text.NumberFormat

class InvTrReturnDialog extends JDialog {

  private static final String TXT_DIALOG_TITLE = "Devolucion ajena a Sucursal"
  private static final String TXT_AMOUNT_LABEL = "Monto"
  private static final String TXT_TICKET_LABEL = "Ticket"
  private static final String TXT_EMPLOYEE_LABEL = "Empleado"
  private static final String TXT_BUTTON_CANCEL = "Cancelar"
  private static final String TXT_BUTTON_OK = "Imprimir"
  private static final String MSG_EMPTY_FIELDS = "Verifique los datos"

  private static final Double ZERO_TOLERANCE = 0.001

  private SwingBuilder sb = new SwingBuilder( )

  private Font bigLabel
  private Font bigInput
  IOController controller

  private JTextField txtCentroCostos
  private JTextField txtTicket
  private NumericTextField txtImporte1
  private NumericTextField txtImporte2
  private NumericTextField txtImporte3
  private NumericTextField txtImporte4
  private JLabel lblSku1
  private JLabel lblSku2
  private JLabel lblSku3
  private JLabel lblSku4
  private JLabel lblWarning
  private List<String> items
  private JTextField txtEmployee
  private NumericTextField txtAmount
  private JButton btnOk
  private InvTr data

  private Boolean cancelPressed

  InvTrReturnDialog( List<String> lstItems ) {
    controller = IOController.instance
    init( )
    items = lstItems
    buildUI( )
    setupTriggers( )
  }
  
  // Internal Methods
  protected void assign() {
    this.data.ticketNum = StringUtils.trimToEmpty( String.format( "%s-%s", this.txtCentroCostos.text, this.txtTicket.text) )
    this.data.empName = StringUtils.trimToEmpty( this.txtEmployee.text )
    this.data.returnAmount1 = this.txtImporte1.value
    this.data.returnAmount2 = this.txtImporte2.value
    this.data.returnAmount3 = this.txtImporte3.value
    this.data.returnAmount4 = this.txtImporte4.value
    this.data.sku1 = this.lblSku1.text.trim()
    this.data.sku2 = this.lblSku2.text.trim()
    this.data.sku3 = this.lblSku3.text.trim()
    this.data.sku4 = this.lblSku4.text.trim()
  }

  protected void display() {
    this.txtTicket.text = StringUtils.trimToEmpty( this.data.ticketNum )
    this.txtEmployee.text = StringUtils.trimToEmpty( this.data.empName )
    this.txtImporte1.value = this.data.returnAmount1
    this.txtImporte2.value = this.data.returnAmount2
    this.txtImporte3.value = this.data.returnAmount3
    this.txtImporte4.value = this.data.returnAmount4
  }

  protected void init( ) {
    bigLabel = new Font( '', Font.PLAIN, 14 )
    bigInput = new Font( '', Font.BOLD, 14 )
    txtImporte1 = new NumericTextField( )
    txtImporte2 = new NumericTextField( )
    txtImporte3 = new NumericTextField( )
    txtImporte4 = new NumericTextField( )
  }
  
  protected void buildUI( ) {
      NumberFormat formatter = NumberFormat.getInstance( Locale.US )
      sb.dialog( this,
        title: ( TXT_DIALOG_TITLE ) ,
        location: [ 300, 300 ] as Point,
        resizable: true,
        modal: true,
        pack: true,
        layout: new MigLayout( 'fill,wrap', '[fill]' )
    ) {
      panel( layout: new MigLayout( "wrap 4", "[]5[fill,grow][fill][fill,grow]", "[]10[]10[]") ) {
        label( text: TXT_TICKET_LABEL, font: bigLabel )
        txtCentroCostos = textField(
                font: bigInput,
                horizontalAlignment: JTextField.LEFT,
                actionPerformed: {  }
        )
        label( text: '-', font: bigInput )
        txtTicket = textField(
            font: bigInput,
            horizontalAlignment: JTextField.LEFT,
            actionPerformed: {  }
        )
        label( TXT_EMPLOYEE_LABEL, font: bigLabel )
        txtEmployee = textField(
            font: bigInput,
            horizontalAlignment: JTextField.LEFT,
            constraints: 'span 3',
            actionPerformed: {  }
        )
      }
      panel( border: titledBorder( title: '' ), layout: new MigLayout( "wrap 2", "[fill,grow][fill,grow]") ) {
          label( text: 'SKU', border: titledBorder( title: '' ) )
          label( text: 'Importe', border: titledBorder( title: '' ) )
          lblSku1 = label( text: items.get(0) )
          textField( txtImporte1,
                  font: bigInput,
                  horizontalAlignment: JTextField.LEFT )
          lblSku2 = label( text: items.size() > 1 ? items.get(1) : "" )
          textField( txtImporte2,
                  visible: items.size() > 1,
                  font: bigInput,
                  horizontalAlignment: JTextField.LEFT )
          lblSku3 = label( text: items.size() > 2 ? items.get(2) : "" )
          textField( txtImporte3,
                  visible: items.size() > 2,
                  font: bigInput,
                  horizontalAlignment: JTextField.LEFT)
          lblSku4 = label( text: items.size() > 3 ? items.get(3) : "" )
          textField( txtImporte4,
                  visible: items.size() > 3,
                  font: bigInput,
                  horizontalAlignment: JTextField.LEFT)
      }

      lblWarning = label( text: MSG_EMPTY_FIELDS, visible: false,
              foreground: UI_Standards.WARNING_FOREGROUND )
      panel( border: BorderFactory.createEmptyBorder( 0, 10, 10, 20 ) ) {
        borderLayout( )
        panel( constraints: BorderLayout.LINE_END ) {
          button( text: TXT_BUTTON_CANCEL,
              preferredSize: UI_Standards.BUTTON_SIZE,
              actionPerformed: { onButtonCancel( ) }
          )
          btnOk = button( text: TXT_BUTTON_OK,
              preferredSize: UI_Standards.BUTTON_SIZE,
              actionPerformed: { onButtonOk( ) } 
          )
        }
      }
    }
  }

  protected void setupTriggers() {

  }

  // Public methods
  void activate( ) {
    this.cancelPressed = false
    this.display()
    this.setVisible( true )
  }

  Boolean isCancel() {
    this.assign()
    return this.cancelPressed
  }

  void setData( InvTr pData ) {
    this.data = pData
  }

  // UI Response
  void onButtonCancel() {
    this.assign()
    this.cancelPressed = true
    setVisible( false )  
  }
  
  void onButtonOk() {
    if( validateData() ){
        this.assign( )
        this.data.postReturn()
        setVisible( false )
    } else {
        lblWarning.visible = true
    }
  }

  Boolean validateData(){
    Boolean valid = false
    Boolean centroCostos = StringUtils.trimToEmpty(txtCentroCostos.text) != ''
    Boolean ticket = StringUtils.trimToEmpty(txtTicket.text) != ''
    Boolean empleado = StringUtils.trimToEmpty(txtEmployee.text) != '' && txtEmployee.text.isNumber()
    if( ticket && empleado && centroCostos && controller.validateCentroCostos(txtCentroCostos.text) ){
        valid = true
    }
    return valid
  }
  
}

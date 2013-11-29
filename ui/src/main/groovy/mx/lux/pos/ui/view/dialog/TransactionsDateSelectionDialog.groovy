package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.InvTrController
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.view.verifier.DateVerifier
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang3.time.DateUtils

import javax.swing.*
import java.awt.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class TransactionsDateSelectionDialog extends JDialog {

  private DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )
  private DateVerifier dv = DateVerifier.instance
  private def sb = new SwingBuilder()

  private JTextField txtDate
  private Date selectedDate

  public boolean button = false

    TransactionsDateSelectionDialog( ) {
    buildUI()
    txtDate.text = df.format( new Date() )
  }  
  
  // UI Layout Definition
  void buildUI() {
    sb.dialog( this,
        title: "Seleccionar fecha",
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [360, 180],
        location: [200, 250],
    ) {
      panel() {
        borderLayout()
        panel( constraints: BorderLayout.CENTER, layout: new MigLayout( "wrap 2", "20[][grow,fill]40", "20[]10[]" ) ) {
          label( text: "Seleccione la fecha del día a consultar", constraints: "span 2" )
		  label( text: " ", constraints: "span 2" )
          label( text: "Fecha:" )
          txtDate = textField()
        }
        panel( constraints: BorderLayout.PAGE_END ) {
          borderLayout() 
          panel( constraints: BorderLayout.LINE_END ) {
            button( text: "Imprimir", preferredSize: UI_Standards.BUTTON_SIZE,
                actionPerformed: { onButtonOk( ) }
            )
            button( text: "Cerrar", preferredSize: UI_Standards.BUTTON_SIZE, 
                actionPerformed: { onButtonCancel( ) }
            )
          }
        }
      }    
    }
  }
  
  // UI Management
  protected void refreshUI() {
    if ( selectedDate == null ) {
      selectedDate = DateUtils.truncate( new Date(), Calendar.DATE )
    }
    txtDate.setText( df.format( selectedDate ) )
  }
  
  // Public Methods
  void activate() {
    refreshUI()
    setVisible(true)
  }
  
  Date getSelectedDate(  ) {
    return selectedDate
  }
  
  void setDefaultDate( Date pDate ) {
    selectedDate = DateUtils.truncate( pDate, Calendar.DATE )
  }
  
  // UI Response
  protected void onButtonCancel() {
    selectedDate = null
	button = false
    dispose()
  }

  protected void onButtonOk() {
    selectedDate = dv.parse( txtDate.getText() )
    InvTrController inv = InvTrController.instance
    inv.requestPrintTransactions( selectedDate )
	button = true
    dispose()
  }
}

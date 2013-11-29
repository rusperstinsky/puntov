package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.view.verifier.DateVerifier
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang3.time.DateUtils

import java.awt.BorderLayout
import java.text.DateFormat
import java.text.SimpleDateFormat
import javax.swing.ButtonGroup
import javax.swing.JDialog
import javax.swing.JRadioButton
import javax.swing.JTextField

class TwoDatesSelectionRadioFilterDialog extends JDialog {

  private DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )
  private DateVerifier dv = DateVerifier.instance
  private def sb = new SwingBuilder()

  private JTextField txtDateStart
  private JTextField txtDateEnd
  private JRadioButton cbTodoTipo
  private JRadioButton cbReferidos
  private JRadioButton cbRx
  private JRadioButton cbLux

  private ButtonGroup tipo
  private ButtonGroup ventas

  private JRadioButton cbTodoVentas
  private JRadioButton cbPrimera
  private JRadioButton cbMayor
  private JRadioButton cbResumen

  private Date selectedDateStart
  private Date selectedDateEnd
  private String selectedArticle

  public boolean button = false

  TwoDatesSelectionRadioFilterDialog( ) {
    buildUI()
  }

  // UI Layout Definition
  void buildUI( ) {
    sb.dialog( this,
        title: "Seleccionar fechas",
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [ 490, 350 ],
        location: [ 200, 250 ],
    ) {
      panel() {
        borderLayout()
        panel( constraints: BorderLayout.CENTER, layout: new MigLayout( "wrap 5", "20[][grow,fill]5", "20[]10[]" ) ) {
          label( text: "Seleccione las fechas de los días a consultar", constraints: "span 5" )
          label( text: " ", constraints: "span 5" )
          label( text: "Fecha Inicio:" )
          txtDateStart = textField( constraints: "span 4", maximumSize: [ 185, 50 ] )
          label( text: "Fecha Fin:" )
          txtDateEnd = textField( constraints: "span 4", maximumSize: [ 185, 50 ] )

          label( text: "Tipo:" )
          tipo = buttonGroup()
          cbTodoTipo = radioButton( text: "Todos", buttonGroup: tipo, selected: true )
          cbReferidos = radioButton( text: "Referidos", buttonGroup: tipo )
          cbRx = radioButton( text: "Rx", buttonGroup: tipo )
          cbLux = radioButton( text: "Lux", buttonGroup: tipo )

          label( text: "Ventas" )
          ventas = buttonGroup()
          cbTodoVentas = radioButton( text: "Todas", buttonGroup: ventas, selected: true )
          cbPrimera = radioButton( text: "Primera", buttonGroup: ventas )
          cbMayor = radioButton( text: "Mayor", buttonGroup: ventas )
          cbResumen = radioButton( text: "Resumen", buttonGroup: ventas )
        }

        panel( constraints: BorderLayout.PAGE_END ) {
          borderLayout()
          panel( constraints: BorderLayout.LINE_END ) {
            button( text: "Generar", preferredSize: UI_Standards.BUTTON_SIZE,
                actionPerformed: { onButtonOk() }
            )
            button( text: "Cerrar", preferredSize: UI_Standards.BUTTON_SIZE,
                actionPerformed: { onButtonCancel() }
            )
          }
        }
      }

    }
  }

  // UI Management
  protected void refreshUI( ) {
    if ( selectedDateStart == null || selectedDateEnd == null ) {
      selectedDateStart = DateUtils.truncate( new Date(), Calendar.MONTH )
      selectedDateEnd = DateUtils.truncate( new Date(), Calendar.DATE )
    }
    txtDateStart.setText( df.format( selectedDateStart ) )
    txtDateEnd.setText( df.format( selectedDateEnd ) )

  }

  // Public Methods
  void activate( ) {
    refreshUI()
    setVisible( true )
  }

  Date getSelectedDateStart( ) {
    return selectedDateStart
  }

  Date getSelectedDateEnd( ) {
    return selectedDateEnd
  }


  boolean getCbTodoTipo( ) {
    return cbTodoTipo.selected
  }

  boolean getCbReferidos( ) {
    return cbReferidos.selected
  }

  boolean getCbRx( ) {
    return cbRx.selected
  }

  boolean getCbLux( ) {
    return cbLux.selected
  }

  boolean getCbTodoVentas( ) {
    return cbTodoVentas.selected
  }

  boolean getCbPrimeras( ) {
    return cbPrimera.selected
  }

  boolean getCbMayor( ) {
    return cbMayor.selected
  }

  boolean getCbResumen( ) {
    return cbResumen.selected
  }

  void setDefaultDates( Date pDateStart, Date pDateEnd ) {
    selectedDateStart = DateUtils.truncate( pDateStart, Calendar.DATE )
    selectedDateEnd = DateUtils.truncate( pDateEnd, Calendar.DATE )
  }

  // UI Response
  protected void onButtonCancel( ) {
    selectedDateStart = null
    selectedDateEnd = null
    button = false
    setVisible( false )
  }

  protected void onButtonOk( ) {
    selectedDateStart = dv.parse( txtDateStart.getText() )
    selectedDateEnd = dv.parse( txtDateEnd.getText() )
    button = true
    setVisible( false )
  }
}

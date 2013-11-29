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

class TwoDatesSelectionFilterLineDialog extends JDialog {

  private DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )
  private DateVerifier dv = DateVerifier.instance
  private def sb = new SwingBuilder()

  private JTextField txtDateStart
  private JTextField txtDateEnd
  private JTextField txtLine
  private JRadioButton cbArticulo
  private JRadioButton cbFactura
  private ButtonGroup reportTipo
  private ButtonGroup lentTipo
  private JRadioButton cbGogle
  private JRadioButton cbOftalmico
  private JRadioButton todo
  private Date selectedDateStart
  private Date selectedDateEnd
  private String selectedArticle

  public boolean button = false

  TwoDatesSelectionFilterLineDialog( ) {
    buildUI()
  }

  // UI Layout Definition
  void buildUI( ) {
    sb.dialog( this,
        title: "Seleccionar fechas",
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [ 450, 320 ],
        location: [ 200, 250 ],
    ) {
      panel() {
        borderLayout()
        panel( constraints: BorderLayout.CENTER, layout: new MigLayout( "wrap 4", "20[][grow,fill]5", "20[]10[]" ) ) {
          label( text: "Seleccione las fechas de los días a consultar", constraints: "span 4" )
          label( text: " ", constraints: "span 4" )
          label( text: "Fecha Inicio:" )
          txtDateStart = textField( constraints: "span 3", maximumSize: [ 185, 50 ] )
          label( text: "Fecha Fin:" )
          txtDateEnd = textField( constraints: "span 3", maximumSize: [ 185, 50 ] )
          label( text: "Linea:" )
          txtLine = textField( constraints: "span 3", maximumSize: [ 185, 50 ] )
          // panel( constraints:"span",  layout: new MigLayout( "wrap 4", "30[right][grow,fill]30", "" ) ){
          label( text: "" )
          reportTipo = buttonGroup()
          cbArticulo = radioButton( text: "Articulo", buttonGroup: reportTipo, selected: true )
          cbFactura = radioButton( text: "Factura", buttonGroup: reportTipo, constraints: "span 3" )

          label( text: "" )
          lentTipo = buttonGroup()
          todo = radioButton( text: "Todos", buttonGroup: lentTipo, selected: true )
          cbGogle = radioButton( text: "Gogle", buttonGroup: lentTipo )
          cbOftalmico = radioButton( text: "Oftalmico", buttonGroup: lentTipo )
          //}
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

  String getselectedArticle( ) {
    return selectedArticle
  }

  boolean getCbFactura( ) {
    return cbFactura.selected
  }

  boolean getCbArticulo( ) {
    return cbArticulo.selected
  }

  boolean getCbGogle( ) {
    return cbGogle.selected
  }

  boolean getCbTodo( ) {
    return todo.selected
  }

  boolean getCbOftalmico( ) {
    return cbOftalmico.selected
  }

  void setDefaultDates( Date pDateStart, Date pDateEnd ) {
    selectedDateStart = DateUtils.truncate( pDateStart, Calendar.DATE )
    selectedDateEnd = DateUtils.truncate( pDateEnd, Calendar.DATE )
  }

  // UI Response
  protected void onButtonCancel( ) {
    selectedDateStart = null
    selectedDateEnd = null
    selectedArticle = null
    button = false
    setVisible( false )
  }

  protected void onButtonOk( ) {
    selectedDateStart = dv.parse( txtDateStart.getText() )
    selectedDateEnd = dv.parse( txtDateEnd.getText() )
    selectedArticle = txtLine.getText().trim()
    button = true
    setVisible( false )
  }
}

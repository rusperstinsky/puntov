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
import javax.swing.JCheckBox
import mx.lux.pos.ui.model.UpperCaseDocument

class TwoDatesSelectionFilterBrandArticleDialog extends JDialog {

  private DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )
  private DateVerifier dv = DateVerifier.instance
  private def sb = new SwingBuilder()

  private JTextField txtDateStart
  private JTextField txtDateEnd
  private JTextField txtLine
  private JCheckBox cbArticulos
  private JRadioButton cbArmazon
  private JRadioButton cbAccesorio
  private JRadioButton cbTodo
  private ButtonGroup reportTipo
  private ButtonGroup orden
  private JRadioButton importe
  private JRadioButton marca
  private Date selectedDateStart
  private Date selectedDateEnd
  private String selectedArticle
  private Boolean resumido;

  public boolean button = false

  TwoDatesSelectionFilterBrandArticleDialog( Boolean opcionResumido ) {
      resumido = opcionResumido
    buildUI()
  }

  // UI Layout Definition
  void buildUI( ) {
    sb.dialog( this,
        title: "Seleccionar fechas",
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [ 450, 350 ],
        location: [ 200, 250 ],
    ) {
      panel() {
        borderLayout()
        panel( constraints: BorderLayout.CENTER, layout: new MigLayout( "wrap 2", "20[][grow,fill]60", "20[]10[]" ) ) {
          label( text: "Seleccione las fechas de los días a consultar", constraints: "span 2" )
          label( text: " ", constraints: "span 2" )
          label( text: "Fecha Inicio:" )
          txtDateStart = textField()
          label( text: "Fecha Fin:" )
          txtDateEnd = textField()
          label( text: "Linea:" )
          txtLine = textField( document: new UpperCaseDocument() )
          panel( constraints: 'span 2', layout: new MigLayout( "wrap 4", "[fill]20[fill][fill][fill]", "" ) ) {
          reportTipo = buttonGroup()
            cbArticulos = checkBox( text: "Resumido", selected: true, visible: resumido )
            cbTodo = radioButton( text: "Todo", buttonGroup: reportTipo, selected: true )
            cbArmazon = radioButton( text: "Armazones", buttonGroup: reportTipo )
            cbAccesorio = radioButton( text: "Accesorios", buttonGroup: reportTipo )
          orden = buttonGroup()
            label( text: 'Ordenar por:' )
            marca = radioButton( text: "Marca", buttonGroup: orden, selected: true )
            importe = radioButton( text: "Importe", buttonGroup: orden )
            label( text:' ')
          }
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

  boolean getmarca( ) {
    return marca.selected
  }

  boolean getTodo( ) {
    return cbTodo.selected
  }

  boolean getAccesorios( ) {
    return cbAccesorio.selected
  }

  boolean getArmazon( ) {
    return cbArmazon.selected
  }

  boolean getCbArticulos( ) {
    return cbArticulos.selected
  }

  boolean getImporte( ) {
    return importe.selected
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

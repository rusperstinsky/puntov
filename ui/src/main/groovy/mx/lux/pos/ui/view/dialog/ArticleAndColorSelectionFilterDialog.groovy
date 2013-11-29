package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.model.UpperCaseDocument
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.view.verifier.DateVerifier
import net.miginfocom.swing.MigLayout

import java.awt.BorderLayout
import java.text.DateFormat
import java.text.SimpleDateFormat
import javax.swing.JDialog
import javax.swing.JTextField

class ArticleAndColorSelectionFilterDialog extends JDialog {

  private DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )
  private DateVerifier dv = DateVerifier.instance
  private def sb = new SwingBuilder()

  private JTextField txtLine
  private JTextField txtColor
  private JTextField txtDescripcion
  private String selectedArticle
  private String selectedDescription
  private String selectedColor

  public boolean button = false

  ArticleAndColorSelectionFilterDialog( ) {
    buildUI()
  }

  // UI Layout Definition
  void buildUI( ) {
    sb.dialog( this,
        title: "Seleccionar fechas",
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [ 400, 300 ],
        location: [ 200, 250 ],
    ) {
      panel() {
        borderLayout()
        panel( constraints: BorderLayout.CENTER, layout: new MigLayout( "wrap 2", "20[][grow,fill]60", "20[]10[]" ) ) {
          label( text: "Inserte el Articulo que desea consultar", constraints: "span 2" )
          label( text: " ", constraints: "span 2" )
          label( text: "Articulo:" )
          txtLine = textField( document: new UpperCaseDocument() )
          label( text: "Descripcion:" )
          txtDescripcion = textField( document: new UpperCaseDocument() )
          label( text: "Color:" )
          txtColor = textField( document: new UpperCaseDocument() )
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
    /*if ( selectedDateStart == null || selectedDateEnd == null ) {
      selectedDateStart = DateUtils.truncate( new Date(), Calendar.DATE )
	  selectedDateEnd = DateUtils.truncate( new Date(), Calendar.DATE )
	}
    txtDateStart.setText( df.format( selectedDateStart ) )
	txtDateEnd.setText( df.format( selectedDateEnd ) )*/

  }

  // Public Methods
  void activate( ) {
    refreshUI()
    setVisible( true )
  }

  String getselectedArticle( ) {
    return selectedArticle
  }

  String getselectedColor( ) {
    return selectedColor
  }

  String getselectedDescription( ) {
    return selectedDescription
  }

  void setDefaultDates( Date pDateStart, Date pDateEnd ) {
    //selectedDateStart = DateUtils.truncate( pDateStart, Calendar.DATE )
    //selectedDateEnd = DateUtils.truncate( pDateEnd, Calendar.DATE )
  }

  // UI Response
  protected void onButtonCancel( ) {
    selectedArticle = null
    button = false
    setVisible( false )
  }

  protected void onButtonOk( ) {
    selectedArticle = txtLine.getText().trim()
    selectedDescription = txtDescripcion.getText().trim()
    selectedColor = txtColor.getText().trim()
    button = true
    setVisible( false )
  }
}

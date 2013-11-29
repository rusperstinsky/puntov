package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.resources.UI_Standards
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang3.StringUtils

import java.awt.BorderLayout
import java.awt.Font
import javax.swing.*
import java.awt.Point

class PartClassDialog extends JDialog {

  private static final String TXT_DIALOG_TITLE = 'Importar Clasificación de Artículos'
  private static final String TXT_DESCRIPTION = 'El proceso de importacion terminó satisfactoriamente.'
  private static final String TXT_FILENAME_LABEL = 'Archivo'
  private static final String TXT_RECORD_COUNT_LABEL = 'Registros'
  private static final String TXT_PART_COUNT_LABEL = 'Artículos (Act / Leídos)'
  private static final String TXT_BUTTON_CLOSE = 'Cerrar'

  private SwingBuilder sb = new SwingBuilder()

  private Font bigLabel
  private Font bigInput

  private JTextField txtFilename, txtRecordCount, txtPartCount
  private JButton btnClose

  private String filename
  private Integer recordsCount, partsRead, partsUpdated

  PartClassDialog( JComponent pParent ) {
    this.init()
    this.buildUI( pParent )
  }

  // Internal Methods
  protected void init( ) {
    bigLabel = new Font( '', Font.PLAIN, 14 )
    bigInput = new Font( '', Font.BOLD, 14 )
  }

  protected void buildUI( JComponent pParent ) {
    sb.dialog( this,
        title: TXT_DIALOG_TITLE,
        location: [ 300, 300 ] as Point,
        resizable: false,
        modal: true,
        pack: true,
    ) {
      borderLayout()
      panel( constraints: BorderLayout.CENTER,
          layout: new MigLayout( "wrap 2", "[]30[fill,grow]", "[]20[][]10[]10[]" ),
          border: BorderFactory.createEmptyBorder( 10, 20, 0, 20 )
      ) {
        label( text: TXT_DESCRIPTION, constraints: "span 2" )
        label( text: TXT_FILENAME_LABEL, constraints: "span 2" )
        txtFilename = textField( constraints: "span 2, grow", horizontalAlignment: JTextField.LEFT, editable: false )
        label( TXT_RECORD_COUNT_LABEL, font: bigLabel )
        txtRecordCount = textField( font: bigInput, horizontalAlignment: JTextField.LEFT, editable: false )
        label( TXT_PART_COUNT_LABEL, font: bigLabel )
        txtPartCount = textField( font: bigInput, horizontalAlignment: JTextField.LEFT, editable: false )
      }

      panel( constraints: BorderLayout.PAGE_END,
          border: BorderFactory.createEmptyBorder( 0, 10, 10, 20 )
      ) {
        borderLayout()
        panel( constraints: BorderLayout.LINE_END ) {
          btnClose = button( text: TXT_BUTTON_CLOSE,
              preferredSize: UI_Standards.BUTTON_SIZE,
              actionPerformed: { onButtonClose() }
          )
        }
      }
    }
  }

  // Public methods
  void activate( ) {
    txtFilename.text = this.filename
    txtRecordCount.text = String.format( "%,d", this.recordsCount )
    txtPartCount.text = String.format( "%,d / %,d ", this.partsRead, this.partsUpdated )
    this.setVisible( true )
  }

  void setFilename( String pFilename ) {
    this.filename = StringUtils.trimToEmpty( pFilename )
  }

  void setPartsRead( Integer pPartsRead ) {
    this.partsRead = pPartsRead
  }

  void setPartsUpdated( Integer pPartsUpdated ) {
    this.partsUpdated = pPartsUpdated
  }

  void setRecordsCount( Integer pRecordsCount ) {
    this.recordsCount = pRecordsCount
  }

  // UI Response
  protected void onButtonClose( ) {
    this.setVisible( false )
  }

}

package mx.lux.pos.ui.view.dialog

import javax.swing.JDialog
import mx.lux.pos.ui.model.Item
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import groovy.swing.SwingBuilder
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JComponent
import net.miginfocom.swing.MigLayout
import mx.lux.pos.ui.resources.UI_Standards
import java.awt.Point
import java.awt.Dimension
import javax.swing.JTextField
import org.apache.commons.lang3.math.NumberUtils
import javax.swing.JTextArea
import java.awt.Color
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.border.Border
import mx.lux.pos.ui.view.component.NumericTextField
import javax.swing.SwingUtilities

class ManualPriceDialog extends JDialog {

  private static final String MSG_NULL_ITEM_INVALID = "ERROR! Item no definido"
  private static final String TXT_DIALOG_TITLE = "Asignacion manual de precios"
  private static final String TXT_INSTRUCTIONS = ""
  private static final String TXT_PART_LABEL = "Articulo"
  private static final String TXT_PRICE_LABEL = "Precio"
  private static final String TXT_REMARKS_LABEL = "Observaciones"
  private static final String TXT_OK_CAPTION = "Aceptar"
  private static final String TXT_CANCEL_CAPTION = "Cancelar"

  static ManualPriceDialog instance = new ManualPriceDialog()

  private Logger logger = LoggerFactory.getLogger( this.getClass() )
  private SwingBuilder sb = new SwingBuilder()

  private Item item
  private String remarks
  private Boolean itemAccepted
  private JTextField txtPart
  private NumericTextField txtPrice
  private JTextArea txtRemarks
  private Border redRemarks, blackRemarks

  private FocusAdapter trgRemarks

  ManualPriceDialog( ) {
    init()
    buildUI()
    setupTriggers()
  }

  // Private Methods
  protected FocusListener getRemarksListener() {
    if (this.trgRemarks == null) {
      this.trgRemarks = new FocusAdapter() {
        void focusGained( FocusEvent e ) {
          (e.source as JTextArea).border = redRemarks
        }

        void focusLost( FocusEvent e ) {
          (e.source as JTextArea).border = blackRemarks
        }
      }
    }
    return this.trgRemarks
  }

  protected void init( ) {
    redRemarks = BorderFactory.createLineBorder( Color.RED )
    blackRemarks = BorderFactory.createLineBorder( Color.BLACK )
    txtPrice = new NumericTextField( )
  }

    // Dialog Layout
  protected void buildUI( ) {
    sb.dialog( this,
        title: TXT_DIALOG_TITLE,
        location: [ 200, 85 ] as Point,
        preferredSize: [ 380, 280 ] as Dimension,
        resizable: false,
        modal: true,
        pack: true,
    ) {
      borderLayout()
      panel( constraints: BorderLayout.PAGE_START, border: BorderFactory.createEmptyBorder( 10, 10, 5, 10 ) ) {
        borderLayout()
        label( text: TXT_INSTRUCTIONS, constraints: BorderLayout.LINE_START )
      }
      composeSelectionPane()
      composeButtonPane()
    }
  }

  protected JComponent composeSelectionPane( ) {
    sb.panel( constraints: BorderLayout.CENTER,
        layout: new MigLayout( "wrap 2", "10[][fill,grow]10" )
    ) {
      label( text: TXT_PART_LABEL )
      txtPart = textField( text: "Descripcion de Servicio", editable: false )
      label( text: TXT_PRICE_LABEL )
      textField( txtPrice, text: "1,000.00" )
      label( text: TXT_REMARKS_LABEL )
      txtRemarks = textArea( rows:6, border: blackRemarks, wrapStyleWord: true, lineWrap: true )
    }
  }

  protected JComponent composeButtonPane( ) {
    sb.panel( constraints: BorderLayout.PAGE_END, border: BorderFactory.createEmptyBorder( 15, 10, 5, 1 ) ) {
      borderLayout()
      panel( constraints: BorderLayout.LINE_END ) {
        button( text: TXT_OK_CAPTION,
            preferredSize: UI_Standards.BUTTON_SIZE,
            actionPerformed: { onButtonOk() }
        )
        button( text: TXT_CANCEL_CAPTION,
            preferredSize: UI_Standards.BUTTON_SIZE,
            actionPerformed: { onButtonCancel() }
        )
      }
    }
  }

  protected void setupTriggers( ) {
    this.txtRemarks.addFocusListener( this.getRemarksListener() )
  }


  // Public methods
  void activate( ) throws IllegalArgumentException {
    if ( this.item != null ) {
      this.txtPart.text = StringUtils.trimToEmpty( this.item.name )
      this.txtPrice.text = String.format( "%,.2f", this.item.price )
      this.txtRemarks.text = StringUtils.trimToEmpty( this.remarks )
      itemAccepted = false
      SwingUtilities.invokeLater( new Runnable() {
        void run() {
          ManualPriceDialog.this.txtPrice.requestFocus()
        }
      })
      this.show()
    } else {
      throw new IllegalArgumentException( MSG_NULL_ITEM_INVALID )
    }
  }

  Item getItem( ) {
    return this.item
  }

  String getRemarks( ) {
    return this.remarks
  }

  Boolean getItemAccepted( ) {
    return itemAccepted
  }

  void setItem( Item pItem ) {
    this.item = pItem
  }

  void setRemarks( String pRemarks ) {
    this.remarks = StringUtils.trimToEmpty( pRemarks )
  }

  // UI Response
  void onButtonOk() {
    this.remarks = StringUtils.trimToEmpty( this.txtRemarks.text )
    this.item.price = NumberUtils.createBigDecimal( String.format("%.2f", this.txtPrice.value ) )
    this.itemAccepted = true
    this.setVisible( false )
  }

  void onButtonCancel() {
    this.setVisible( false )
  }

}

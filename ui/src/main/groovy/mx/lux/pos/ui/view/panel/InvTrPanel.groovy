package mx.lux.pos.ui.view.panel

import groovy.swing.SwingBuilder
import mx.lux.pos.model.Sucursal
import mx.lux.pos.ui.model.InvTrViewMode
import mx.lux.pos.ui.model.adapter.SiteAdapter
import mx.lux.pos.ui.model.adapter.StringAdapter
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.view.component.ComboBoxHintSelector
import mx.lux.pos.ui.view.component.ComboBoxSelector
import mx.lux.pos.ui.view.component.NavigationBar
import net.miginfocom.swing.MigLayout

import java.awt.BorderLayout
import java.awt.Font
import javax.swing.table.AbstractTableModel
import javax.swing.*
import java.awt.Dimension
import javax.swing.table.TableModel
import mx.lux.pos.ui.MainWindow

import java.awt.event.ActionEvent

class InvTrPanel extends JPanel {

  static final String MSG_INCORRECT_BRANCH = 'El archivo: <%s> hace referencia a otra sucursal'
  static final String MSG_CONFIRM_REMOVE_ISSUE = "Desea eliminar del ticket de salida el artículo:\n\n  [%d] %s\n \n "
  static final String MSG_CONFIRM_TO_PROCEED = "Confirme para pasar a: %s"
  static final String MSG_NO_RESULTS_FOUND = "No se encontraron resultados para: <%s>"
  static final String MSG_POST_FAILED = "La transacción NO fue registrada.\nNotifique a soporte técnico."
  static final String MSG_NO_DOCUMENT_AVAILABLE = "Remisión no disponible."
  static final String MSG_DOCUMENT_ALREADY_PROCCESED = "El archivo ya fue cargado."
  static final String MSG_ARTICLE_NOT_FOUND = "Articulo %s no existe."
  static final String MSG_CUM_QTY_ADJUST_WRONG = "Cantidad de piezas incorrecta. Reintente."
  static final String MSG_RECEIPT_WARNING = "%,d transacciones registradas con Tipo:<%s> y Ref:<%s> (%s - %s)"
  static final String MSG_TRANSACTION_POSTED = "Transaccion aplicada"
  static final String MSG_UNABLE_TO_PARSE_QTY = 'La cantidad debe ser numérica'
  static final String MSG_NO_STOCK = 'Articulo sin existencia ¿Desea continuar?'
  static final String TXT_NO_STOCK = 'Articulo sin existencia'
  static final String TXT_CONFIRM_TITLE = "Confirmar selección"
  static final String TXT_SECTION_DISPLAY_TITLE = "Informacion"
  static final String TXT_SECTION_INPUT_TITLE = "Registro"
  static final String TXT_BTN_CANCEL_CAPTION = "Cancelar"
  static final String TXT_BTN_PRINT_CAPTION = "Imprimir"
  static final String TXT_BTN_REGISTER_CAPTION = "Registrar"
  static final String TXT_QUERY_TITLE = "Búsqueda de artículos con: <%s>"
  static final String TXT_POST_TITLE = "Registro de Inventario"
  static final String TXT_TR_EFF_DATE_LABEL = "Fecha"
  static final String TXT_TR_NBR_LABEL = "Folio"
  static final String TXT_TR_REF_LABEL = "Referencia"
  static final String TXT_TR_REMARKS_LABEL = "Observaciones"
  static final String TXT_TR_SITE_TO_LABEL = "Almacén"
  static final String TXT_TR_TYPE_LABEL = "Cantidad"
  static final String TXT_TR_QUANTITY_LABEL = "Cantidad"
  static final String TXT_TR_USER_LABEL = "Empleado"
  static final String TXT_TRD_DESC_LABEL = "Descripción"
  static final String TXT_TRD_LINE_LABEL = "Línea"
  static final String TXT_TRD_MOVTYPE_LABEL = "Mov"
  static final String TXT_TRD_QTY_LABEL = "Cant"
  static final String TXT_TRD_SKU_LABEL = "SKU#"
  static final String TXT_UNDER_CONSTRUCTION_TEXT = "Opción no implementada"
  static final String TXT_VIEW_MODE_LABEL = "Mov"
  static final String TXT_REMARKS_PROMPT =  "<Ingrese observaciones de la transacción>"
  static final String TXT_SEED_ISSUE_PROMPT =  "<Ingrese una cadena de búsqueda de artículos>"
  static final String TXT_SEED_ADJUST_PROMPT =  "<Ingrese códigos de artículos para procesar el ajuste>"
  static final String TXT_SITE_TO_PROMPT =  "<Seleccione el destino de la mercancía>"
  static final String TXT_RESULTADO_SALIDA_SUC = "Folio asignado %s"
  static final String TXT_SALIDA_SUC_TITLE = "Salida de Sucursal"
  
  SwingBuilder sb = new SwingBuilder() 

  InvTrView view
  JButton btnPrint
  JButton btnCancel
  ComboBoxHintSelector<Sucursal> comboSiteTo = new ComboBoxHintSelector<Sucursal>( new SiteAdapter( ) )
  ComboBoxSelector<InvTrViewMode> comboViewMode = new ComboBoxSelector<InvTrViewMode>( new StringAdapter<InvTrViewMode>( ) )
  JLabel lblSelector
  JLabel lblStatus
  JLabel lblType
  JTextField txtType
  JTextField txtNbr
  JTextField txtEffDate
  JTextField txtPartSeed
  JTextField txtRef
  JTextField txtRemarks
  JTextField txtUser
  JTable tBrowser
  JPanel selector
  TableModel browserSku
  Boolean stock = true
  Sucursal site = new Sucursal()
  Boolean newTransaction = true

  InvTrPanel( InvTrView pView ) {
    view = pView
    buildUI()
  }
  
  // Internal Methods
  private void buildUI() {
    sb.panel ( this ) {
      borderLayout( )
      onSiteChange( )
      composeTopSection( )
      composeMidSection( )
      composeBottomSection( )
    }
  }

  private JComponent composeBottomSection() {
    sb.panel( constraints: BorderLayout.PAGE_END ) {
      borderLayout( )
      panel( constraints: BorderLayout.PAGE_START,
          layout: new MigLayout( "wrap 2", "[][fill, grow]" )
      ) {
        label( TXT_TR_REMARKS_LABEL )
        txtRemarks = textField( )
      }
      panel( constraints: BorderLayout.CENTER,
             border: BorderFactory.createEmptyBorder( 3, 10, 3, 10 )
      ) {
        borderLayout( )
        lblStatus = label( text: "", 
            constraints: BorderLayout.CENTER,
            foreground: UI_Standards.WARNING_FOREGROUND
        )
      }
      panel( constraints: BorderLayout.LINE_END) {
        btnCancel =  button( text: TXT_BTN_CANCEL_CAPTION, 
            preferredSize: UI_Standards.BUTTON_SIZE,
            actionPerformed: { view.onButtonCancel( ) }
        )
        btnPrint = button( text: TXT_BTN_PRINT_CAPTION,
            preferredSize: UI_Standards.BUTTON_SIZE,
            actionPerformed: { view.onButtonPrint( ) } 
        )
      }
    }
  }
  
  private JComponent composeDisplaySection( ) {
    sb.panel ( border:BorderFactory.createTitledBorder( TXT_SECTION_DISPLAY_TITLE ),
      layout: new MigLayout( "wrap 2", "[][fill,grow]" )
      ) {
        label( TXT_TR_NBR_LABEL )
        txtNbr = textField( editable: false, background: UI_Standards.LOCKED_BACKGROUND )
        label( TXT_TR_EFF_DATE_LABEL )
        txtEffDate = textField ( editable: false, background: UI_Standards.LOCKED_BACKGROUND )
        label( TXT_TR_REF_LABEL )
        txtRef = textField( editable: false, background: UI_Standards.LOCKED_BACKGROUND )
        label( TXT_TR_USER_LABEL )
        txtUser = textField ( editable: false, background: UI_Standards.LOCKED_BACKGROUND )
      }
  }
  
  private JComponent composeInputSection( ) {
    Dimension MAX_SIZE = [310, 30] as Dimension
    sb.panel( border:BorderFactory.createTitledBorder( TXT_SECTION_INPUT_TITLE ),
        layout: new MigLayout( "wrap 2", "[][fill,grow]" )
    ) {
      label( TXT_TR_SITE_TO_LABEL )
      comboBox ( comboSiteTo.comboBox,
          minimumSize: [160, 8] as Dimension,
          maximumSize: MAX_SIZE,
          actionPerformed: {onSiteChange()}
      )
      comboSiteTo.setItems( view.data.siteList )
      label( TXT_VIEW_MODE_LABEL )
      comboBox( comboViewMode.comboBox, maximumSize: MAX_SIZE )
      comboViewMode.setItems( view.data.viewModeList )
      comboViewMode.comboBox.actionPerformed = { view.onViewModeChanged( ) }
      lblType = label( TXT_TR_TYPE_LABEL, constraints: 'hidemode 3' )
      txtType = textField( editable: false,
          background: UI_Standards.LOCKED_BACKGROUND,
          maximumSize: MAX_SIZE,
          constraints: 'hidemode 3' )
      lblSelector = label( "" )
      selector = panel( new NavigationBar( ) )
    }
  }
  
  private JComponent composeMidSection() {
    sb.panel( constraints: BorderLayout.CENTER ) {
      borderLayout( )
      txtPartSeed = textField( constraints: BorderLayout.PAGE_START,
          font: new Font( "", Font.BOLD, 16 ),
          actionPerformed: { event -> view.onPartSeedValueChanged( event.source.text ) }
      )
      scrollPane( constraints: BorderLayout.CENTER,
                  border:BorderFactory.createTitledBorder( "Artículos" )
      ) {
        tBrowser = table( selectionMode: ListSelectionModel.SINGLE_SELECTION,  
            mouseClicked: { ev -> if (ev.clickCount == 2) { view.onSkuDoubleClicked( ) } } 
        ) {
          browserSku = sb.tableModel( list: view.data.getSkuList( ) ) {
            propertyColumn( header: TXT_TRD_LINE_LABEL, propertyName: "line", maxWidth: 50, editable: false )
            propertyColumn( header: TXT_TRD_SKU_LABEL, propertyName: "sku", maxWidth: 100, editable: false )
            propertyColumn( header: TXT_TRD_DESC_LABEL, propertyName: "description", minWidth: 300, editable: false )
            propertyColumn( header: TXT_TRD_MOVTYPE_LABEL, propertyName: "movType", maxWidth: 50, editable: false )
            propertyColumn( header: TXT_TRD_QTY_LABEL, propertyName: "qty", maxWidth: 100, editable: false )
          }
        }
      }
    }
  }
      
  private JComponent composeTopSection() {
    sb.panel( constraints: BorderLayout.PAGE_START ) {
      borderLayout( )
      panel ( constraints: BorderLayout.CENTER ) {
        gridLayout(rows:1, columns:2)
        composeInputSection( )
        composeDisplaySection( )
      }
    }
  }

  private def onSiteChange( ) {
    if( comboSiteTo?.comboBox?.selectedItem != null ){
      site = comboSiteTo.selection
    }
  }
}

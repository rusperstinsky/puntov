package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.model.TransInv
import mx.lux.pos.ui.model.InvTrDataset
import mx.lux.pos.ui.model.adapter.InvTrAdapter
import mx.lux.pos.ui.model.adapter.InvTrFilter
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.view.verifier.DateVerifier
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JDialog
import javax.swing.JTable
import javax.swing.JTextField
import javax.swing.table.AbstractTableModel

class InvTrSelectorDialog extends JDialog {

  private static final String TXT_BUTTON_CLEAR_CAPTION = "Limpiar"
  private static final String TXT_BUTTON_OK_CAPTION = "Aceptar"
  private static final String TXT_BUTTON_SEARCH_CAPTION = "Buscar"
  private static final String TXT_FROM_DATE_LABEL = "De fecha"
  private static final String TXT_TO_DATE_LABEL = "a"
  private static final String TXT_TR_TYPE_LABEL = "Tipo"
  private static final String TXT_SITE_TO_LABEL = "Almacén"
  private static final String TXT_PART_NUMBER_LABEL = "Sku"
  private static final String TXT_PART_CODE_LABEL = "Artículo"
  private static final String TXT_TR_EFF_DATE_LABEL = "Fecha"
  private static final String TXT_TR_NUMBER_LABEL = "Folio"
  private static final String TXT_TR_PART_LIST_LABEL = "Artículos"


  private Logger logger = LoggerFactory.getLogger( this.class )
  private def sb = new SwingBuilder()
  
  private InvTrDataset dsInvTr
  private InvTrAdapter adapter = new InvTrAdapter( )
  private DateVerifier dv = DateVerifier.instance

  private AbstractTableModel trBrowser
  private JTextField txtDateFrom
  private JTextField txtDateTo
  private JTextField txtType
  private JTextField txtSiteTo
  private JTextField txtSku
  private JTextField txtPart
  private JTable tBrowser

  InvTrSelectorDialog ( InvTrDataset pDataset ) {
    dsInvTr = pDataset
    buildUI()
  }
  
  // UI Layout Definition
  private void buildUI() {
    sb.dialog( this,
        title: "Selector de Transacciones de Inventario",
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [600, 400],
        location: [180, 80]
    ) {
      borderLayout()
      composeFilterPanel( )
      composeTablePanel( )
      composeButtonsPanel( )
    }
  }

  
  private def composeButtonsPanel( ) {
    sb.panel( constraints: BorderLayout.PAGE_END
    ) {
      borderLayout()
      panel( constraints: BorderLayout.LINE_END ) {
        button( text: TXT_BUTTON_OK_CAPTION,
            preferredSize: UI_Standards.BUTTON_SIZE,
            actionPerformed: { onButtonOk() }
        )
      }
    }
  }
  
  private def composeFilterPanel( ) {
    sb.panel( constraints: BorderLayout.PAGE_START ) {
      borderLayout()
      panel(  constraints: BorderLayout.PAGE_START,
             layout: new MigLayout( 'fill, wrap 4', '[][fill,grow][][fill,grow]' )
      ) {
        label( TXT_FROM_DATE_LABEL )
        txtDateFrom = textField ( text: "23/07/12" )
        label( TXT_TO_DATE_LABEL )
        txtDateTo = textField ( text: "23/07/12" )
      
        label( TXT_TR_TYPE_LABEL )
        txtType = textField (  )
        label( TXT_SITE_TO_LABEL )
        txtSiteTo = textField (  )

        label( TXT_PART_NUMBER_LABEL )
        txtSku = textField (  )
        label( TXT_PART_CODE_LABEL )
        txtPart = textField (  )
      }
      panel( constraints: BorderLayout.LINE_END,
             border: BorderFactory.createEmptyBorder(0, 0, 0, 5)
      ) {
//        hbox {
          button( text: TXT_BUTTON_CLEAR_CAPTION,
              preferredSize: UI_Standards.BUTTON_SIZE,
              actionPerformed: { onButtonClear() } 
          )
          button( text: TXT_BUTTON_SEARCH_CAPTION,
                  preferredSize: UI_Standards.BUTTON_SIZE,
                  constraints: BorderLayout.EAST,
                  actionPerformed: { onButtonSearch() }
          )
//        }
      }
    }
  }
  
  private def composeTablePanel( ) {
    sb.scrollPane( constraints: BorderLayout.CENTER ) {
      tBrowser = table( ) {
        trBrowser = tableModel( list: dsInvTr.dataset ) {
          closureColumn( header: TXT_TR_EFF_DATE_LABEL, width: 160, read: { TransInv tr -> adapter.getText( tr, InvTrAdapter.FLD_TR_EFF_DATE ) } )
          propertyColumn( header: TXT_TR_TYPE_LABEL, propertyName: "idTipoTrans", width: 140, editable: false )
          propertyColumn( header: TXT_TR_NUMBER_LABEL, propertyName: "folio", maxWidth: 60, editable: false )
          closureColumn( header: TXT_TR_PART_LIST_LABEL, minWidth: 300, read: { TransInv tr ->  adapter.getText( tr, InvTrAdapter.FLD_TR_PART_LIST ) } )
        }
      }
    }
  }
  
  // Public Methods
  void activate() {
    refreshUI()
    setVisible(true)
  }
  
  void assignFilter() {
    logger.debug( "[Dialog] Assign Filter")
    InvTrFilter filter = dsInvTr.filter
    
    Date from = dv.parse( txtDateFrom.getText( ).trim( ) )
    Date to = dv.parse( txtDateTo.getText( ).trim() )
    filter.setDateRange( from, to )
    
    filter.setTrType( txtType.getText() )
    filter.setPartCode( txtPart.getText() )
    filter.setSiteTo( txtSiteTo.getText() )
    filter.setSku( txtSku.getText() )
  }
  
  void refreshUI() {
    logger.debug( "[Dialog] Update UI" )
    InvTrFilter filter = dsInvTr.filter
    txtDateFrom.setText( filter.dateFrom != null ? adapter.getText( filter.dateFrom ) : "" )
    txtDateTo.setText( filter.dateTo != null ? adapter.getText( filter.dateTo ) : "" )
    txtType.setText( StringUtils.trimToEmpty( filter.trType ) )
    txtSiteTo.setText( filter.siteTo == null ? "" : filter.siteTo.toString( ) )
    txtSku.setText( filter.sku == null ? "" : filter.sku.toString( ) )
    txtPart.setText( StringUtils.trimToEmpty( filter.partCode ) )
    trBrowser.fireTableDataChanged()
  }
  
  // UI Response
  private void onButtonClear() {
    logger.debug( "[Dialog] Button Clear Trigger")
    (dsInvTr.filter as InvTrFilter).reset( )
    refreshUI( )
  }
  
  private void onButtonOk() {
    logger.debug( "[Dialog] Button Ok Trigger")
    if ( tBrowser.getSelectedRows( ).size( ) > 0 ) {
      dsInvTr.currentIndex = tBrowser.selectedRows [0]
    }
    setVisible( false )
  }
  
  private void onButtonSearch() {
    logger.debug( "[Dialog] Button Search Trigger")
    assignFilter()
    dsInvTr.requestTransactions( )
    refreshUI( )
  }
  
}

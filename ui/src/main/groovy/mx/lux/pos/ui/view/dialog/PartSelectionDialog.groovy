package mx.lux.pos.ui.view.dialog

import java.awt.BorderLayout
import java.awt.Color

import javax.swing.*
import javax.swing.table.AbstractTableModel

import groovy.swing.SwingBuilder

import org.apache.poi.hssf.record.formula.functions.T
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import mx.lux.pos.model.Articulo
import mx.lux.pos.ui.model.adapter.PartAdapter
import mx.lux.pos.ui.resources.UI_Standards

class PartSelectionDialog extends JDialog {
  // Constants
  private static final String TXT_DIALOG_TITLE = "Selector de Articulos"
  private static final String TXT_BUTTON_OK_CAPTION = "Aplicar"
  private static final String TXT_BUTTON_CANCEL_CAPTION = "Cancelar"
  private static final String TXT_SUMMARY_LABEL = "Se encontraron %,d artículos similares a <%s>"
  private static final String TXT_DESCRIPTION_LABEL = "Descripción"
  private static final String TXT_SKU_LABEL = "SKU"
  
  private Logger logger = LoggerFactory.getLogger( this.class )
  private def sb = new SwingBuilder()

  private JComponent parent
  private JLabel lblListSummary
  private JTable tblPart
  private AbstractTableModel browser
  private String seed
  private String seedText = TXT_SUMMARY_LABEL
  
  private List<Articulo> itemList = new ArrayList<Articulo>()
  private List<Articulo> selectionList
  
  // Constructor
  PartSelectionDialog( JComponent pParent ) {
    parent = pParent
    compose()
  }
  
  // UI Layout Definition
  private void compose() {
    sb.dialog( this,
        title: TXT_DIALOG_TITLE,
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [480, 270],
        locationRelativeTo: parent
    ) {
      panel( border: BorderFactory.createEmptyBorder(10, 5, 5, 5)) {
        borderLayout()
        composeTopSection( )
        composeListSection(  )
        composeButtonArea(  )
      }
    }
  }

  private JComponent composeButtonArea( ) {
    sb.panel( constraints: BorderLayout.PAGE_END ) {
      borderLayout()
      panel( constraints: BorderLayout.LINE_END ) {
        button( text: TXT_BUTTON_OK_CAPTION,
            preferredSize: UI_Standards.BUTTON_SIZE,
            actionPerformed: { onButtonOK() }
        )
        button( text: TXT_BUTTON_CANCEL_CAPTION,
            preferredSize: UI_Standards.BUTTON_SIZE,
            actionPerformed: { onButtonCancel() }
        )
      }
    }
  }
  
  private JComponent composeListSection() {
    sb.scrollPane( constraints: BorderLayout.CENTER,
        border: BorderFactory.createLineBorder( Color.BLACK )
    ) {
      tblPart = table( mouseClicked: { ev -> if (ev.clickCount == 2) { onButtonOK() } }
      ) {
        browser = sb.tableModel( list: itemList ) {
          propertyColumn( header: TXT_SKU_LABEL, propertyName: "id", maxWidth: 140, editable: false )
          closureColumn( header: TXT_DESCRIPTION_LABEL, minWidth: 320,
              read: { Articulo part -> this.formatDescription(part) } )
        }
      }
    }
  }
  
  private JComponent composeTopSection( ) {
    sb.panel( constraints: BorderLayout.PAGE_START
    ) {
      borderLayout()
      lblListSummary = label( constraints: BorderLayout.CENTER,
          horizontalAlignment: SwingConstants.LEFT,
          text: seedText )
    }
  }
  
  // UI Behavior
  protected String formatDescription( Articulo pPart ) {
    return PartAdapter.instance.getText( pPart, PartAdapter.FLD_INV_DESC )
  }
  
  // Public Methods
  void activate() {
    refreshUI()
    tblPart.clearSelection()
    selectionList = null
    setVisible( true )
  }
  
  List<Articulo> getSelection() {
    return selectionList
  }
  
  void setSeed( String pSeed ) {
    seed = pSeed
  }
  
  void setItems(List<Articulo> pPartList) {
    itemList.clear()
    itemList.addAll( pPartList )
  }
  
  void setMultiSelection( Boolean pEnabled ) {
    if ( pEnabled ) {
      tblPart.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION )
    } else {
      tblPart.setSelectionMode( ListSelectionModel.SINGLE_SELECTION )
    }
  }
  
  void refreshUI() {
    lblListSummary.setText( String.format( TXT_SUMMARY_LABEL, itemList.size(), seed ) )
    browser.fireTableDataChanged()
  }
  
  // UI Response
  private void onButtonCancel() {
    logger.debug( "[Dialog] Cancelar" )
    selectionList = null
    setVisible(false)
  }

  private void onButtonOK() {
    logger.debug( "[Dialog] Aplicar" )
    if ( tblPart.getSelectedRowCount() > 0 ) {
      selectionList = new ArrayList<Articulo>()
      for ( iRow in tblPart.getSelectedRows() ) {
        selectionList.add( itemList[iRow] )
      }
    }
    logger.debug("[Dialog] Selected Rows: ${ selection.size() }")
    setVisible(false)
  }


}

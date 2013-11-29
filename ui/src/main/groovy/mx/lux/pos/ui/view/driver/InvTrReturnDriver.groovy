package mx.lux.pos.ui.view.driver

import mx.lux.pos.ui.model.InvTrSku
import mx.lux.pos.ui.model.adapter.InvTrAdapter
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.view.panel.InvTrView
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.swing.JOptionPane
import mx.lux.pos.ui.controller.InvTrController
import mx.lux.pos.ui.view.dialog.InvTrReturnDialog

import java.util.regex.Pattern

class InvTrReturnDriver extends InvTrDriver {

  Logger logger = LoggerFactory.getLogger( InvTrDriver.class )

  // Internal Methods
  private Boolean isRemarksValid( InvTrView pView ) {
    String rmks = pView.panel.txtRemarks.getText( ).trim( )
    Boolean valid = (! StringUtils.isEmpty( rmks ) )
    if (valid) {
      valid = ! rmks.equalsIgnoreCase( pView.panel.TXT_REMARKS_PROMPT.trim( ) )
    }
    return valid
  }

  protected void renderFlaggedItems( InvTrView pView )  {
    if ( pView.data.flagOnSiteTo ) {
      pView.panel.comboSiteTo.renderAsFlagged( )
      pView.panel.comboSiteTo.setText( pView.panel.TXT_SITE_TO_PROMPT )
    } else {
      pView.panel.comboSiteTo.renderAsFlagged( false )
    }

    if ( pView.data.flagOnPartSeed ) {
      UI_Standards.setFlagged( pView.panel.txtPartSeed )
      pView.panel.txtPartSeed.setText( pView.panel.TXT_SEED_ISSUE_PROMPT )
    } else {
      UI_Standards.setFlagged( pView.panel.txtPartSeed, false )
    }

    if ( pView.data.flagOnRemarks ) {
      UI_Standards.setFlagged( pView.panel.txtRemarks  )
      pView.panel.txtRemarks.setText( pView.panel.TXT_REMARKS_PROMPT )
    } else {
      UI_Standards.setFlagged( pView.panel.txtRemarks, false )
    }
  }

  // Public methods
  Boolean assign( InvTrView pView ) {
    Boolean validated = false
    // Validate
    pView.data.flagOnPartSeed = ( pView.data.getSkuList( ).size( ) == 0 )
    pView.data.flagOnRemarks = ( ! isRemarksValid( pView ) )
    validated = ! ( pView.data.flagOnPartSeed || pView.data.flagOnRemarks )

    // Assign
    if ( validated ) {
      pView.data.postRemarks = pView.panel.txtRemarks.getText( )
    }

    return validated
  }

  void assignPartSeed( InvTrView pView ) {
    pView.data.flagOnRemarks &= ( ! isRemarksValid( pView ) )
    super.assignPartSeed( pView )
  }

  void displayPartSeedPrompt( InvTrView pView ) {
    pView.panel.txtPartSeed.setText( pView.panel.TXT_SEED_ISSUE_PROMPT )
  }

  Boolean doBeforeSave( InvTrView pView ) {
    List<String> lstItems = new ArrayList<>()
      for(InvTrSku item : pView.data.skuList){
          lstItems.add( item.sku.toString() )
      }
    InvTrReturnDialog dialog = new InvTrReturnDialog( lstItems )
    dialog.setData( pView.data )
    dialog.activate()
    return !dialog.isCancel()
  }

  void enableUI( InvTrView pView ) {
    super.enableUI( pView )

    UI_Standards.setLocked( pView.panel.txtPartSeed, false )
    UI_Standards.setLocked( pView.panel.txtRemarks, false )

    pView.panel.lblType.setVisible( true )
    pView.panel.txtType.setVisible( true )
    pView.panel.txtType.setText( "0" )
    pView.panel.txtType.setEditable( false )
    pView.panel.selector.setVisible( false )

    pView.panel.btnPrint.setEnabled( true )

  }

  Boolean isPartSeedValid( InvTrView pView ) {
    String seed = pView.panel.txtPartSeed.getText( ).trim( ).toUpperCase( )
    Boolean valid = (! StringUtils.isEmpty( seed ) )
    if (valid) {
      valid = ! seed.equals( pView.panel.TXT_SEED_ISSUE_PROMPT.trim( ).toUpperCase( ) )
    }
    return valid
  }

  void refreshUI( InvTrView pView ) {
    renderFlaggedItems( pView )
      Integer quantity = 0
      for(InvTrSku article : pView.data.skuList){
          quantity = quantity+article.qty
      }
    pView.panel.lblStatus.setText( pView.data.accessStatus( ) )
    pView.panel.txtEffDate.setText( pView.adapter.getText( pView.data, InvTrAdapter.FLD_TODAY ) )
    pView.panel.txtUser.setText( pView.adapter.getText( pView.data.currentUser ) )
    pView.panel.browserSku.fireTableDataChanged( )
    pView.panel.txtType.setText( String.format( '%d', quantity ) )
  }

  void onSkuDoubleClicked( InvTrView pView ) {
    logger.debug( "[Driver] Double clicked on sku table" )
    if ( pView.panel.tBrowser.selectedRow >= 0 ) {
      InvTrSku line = pView.data.skuList[ pView.panel.tBrowser.selectedRow ]
      String msg = String.format( pView.panel.MSG_CONFIRM_REMOVE_ISSUE, line.sku, line.description ) 
      Integer selection = JOptionPane.showConfirmDialog( pView.panel, msg, pView.panel.TXT_CONFIRM_TITLE, 
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE )
      if ( selection.equals( JOptionPane.OK_OPTION ) ) {
        pView.data.skuList.remove( line )
        pView.fireRefreshUI( )
      }
    }
  }
  
}

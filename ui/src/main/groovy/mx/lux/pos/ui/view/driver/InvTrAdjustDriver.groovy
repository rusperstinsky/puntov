package mx.lux.pos.ui.view.driver

import mx.lux.pos.ui.model.InvTrSku
import mx.lux.pos.ui.model.InvTrViewMode
import mx.lux.pos.ui.model.adapter.InvTrAdapter
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.view.panel.InvTrView
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class InvTrAdjustDriver extends InvTrDriver {

  Logger logger = LoggerFactory.getLogger( InvTrDriver.class )

  // Internal Methods
  protected Boolean isRemarksValid( InvTrView pView ) {
    String rmks = pView.panel.txtRemarks.getText( ).trim( )
    Boolean valid = (! StringUtils.isEmpty( rmks ) )
    if (valid) {
      valid = ! rmks.equalsIgnoreCase( pView.panel.TXT_REMARKS_PROMPT.trim( ) )
    }
    return valid
  }

  protected void renderFlaggedItems( InvTrView pView )  {
    if ( pView.data.flagOnPartSeed ) {
      UI_Standards.setFlagged( pView.panel.txtPartSeed )
      pView.panel.txtPartSeed.setText( pView.panel.TXT_SEED_ADJUST_PROMPT )
    } else {
      if ( pView.data.skuList.size() >= 2 ) {
        UI_Standards.setLocked( pView.panel.txtPartSeed )
      } else {
        UI_Standards.setFlagged( pView.panel.txtPartSeed, false )
      }
    }

    if ( pView.data.flagOnRemarks ) {
      UI_Standards.setFlagged( pView.panel.txtRemarks  )
      pView.panel.txtRemarks.setText( pView.panel.TXT_REMARKS_PROMPT )
    } else {
      UI_Standards.setFlagged( pView.panel.txtRemarks, false )
    }
    
    if ( pView.data.flagOnQty ) {
      pView.data.txtStatus = pView.panel.MSG_CUM_QTY_ADJUST_WRONG
    }
  }

  // Public methods
  Boolean assign( InvTrView pView ) {
    Boolean validated = false
    // Validate
    Integer qty = 0
    for ( InvTrSku trLine : pView.data.skuList ) {
      qty += trLine.qty
    }
    pView.data.flagOnQty = (qty != 0) // In adjust sum(qty) == 0
    pView.data.flagOnPartSeed = ( pView.data.getSkuList( ).size( ) <= 1 )
    pView.data.flagOnRemarks = ( ! isRemarksValid( pView ) )
    validated = ! ( pView.data.flagOnPartSeed || pView.data.flagOnRemarks || pView.data.flagOnQty )

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
    pView.panel.txtPartSeed.setText( pView.panel.TXT_SEED_ADJUST_PROMPT )
  }

  void enableUI( InvTrView pView ) {
    super.enableUI( pView )

    pView.panel.lblType.setVisible( false )
    pView.panel.txtType.setVisible( false )
    pView.panel.selector.setVisible( false )

    UI_Standards.setLocked( pView.panel.txtPartSeed, false )
    UI_Standards.setLocked( pView.panel.txtRemarks, false )
    pView.panel.btnPrint.setEnabled( true )
  }

  Boolean isPartSeedValid( InvTrView pView ) {
    String seed = pView.panel.txtPartSeed.getText( ).trim( ).toUpperCase( )
    Boolean valid = (! StringUtils.isEmpty( seed ) )
    if (valid) {
      valid = ! seed.equals( pView.panel.TXT_SEED_ADJUST_PROMPT.trim( ).toUpperCase( ) )
    }
    return valid
  }

  void refreshUI( InvTrView pView ) {
    pView.panel.btnCancel.setVisible( pView.data.skuList.size() > 0 ) 
    renderFlaggedItems( pView )
    pView.panel.lblStatus.setText( pView.data.accessStatus( ) )
    pView.panel.txtEffDate.setText( pView.adapter.getText( pView.data, InvTrAdapter.FLD_TODAY ) )
    pView.panel.txtUser.setText( pView.adapter.getText( pView.data.currentUser ) )
    pView.panel.browserSku.fireTableDataChanged( )
  }

  void onButtonCancel( InvTrView pView ) {
    pView.data.clear( )
    pView.fireResetUI( )
    pView.notifyViewMode( InvTrViewMode.ADJUST )
    pView.fireDisplay( )
  }
}

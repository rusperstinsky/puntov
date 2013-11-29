package mx.lux.pos.ui.view.driver

import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.view.panel.InvTrView

class InvTrDriver {

  // Internal Methods

  // Public Methods
  Boolean assign( InvTrView pView ) { return true }

  void assignPartSeed( InvTrView pView ) {
    pView.data.partSeed = pView.panel.txtPartSeed.getText().trim().toUpperCase()
    pView.data.flagOnPartSeed = false
    pView.panel.txtPartSeed.setText( pView.data.partSeed )
  }

  void assignQuantity( InvTrView pView ) {
    pView.data.postQty = 1
  }

  void consumePartSeed( InvTrView pView ) {
    pView.data.partSeed = ""
    pView.panel.txtPartSeed.setText( pView.data.partSeed )
  }

  void displayPartSeedPrompt( InvTrView pView ) {
    pView.panel.txtPartSeed.setText( "" )
  }

  Boolean doBeforeSave( InvTrView pView ) {
    return true
  }

  void enableUI( InvTrView pView ) {
    pView.panel.comboSiteTo.setLocked( true )
    UI_Standards.setLocked( pView.panel.txtType )
    UI_Standards.setLocked( pView.panel.txtNbr )
    UI_Standards.setLocked( pView.panel.txtEffDate )
    UI_Standards.setLocked( pView.panel.txtRef )
    UI_Standards.setLocked( pView.panel.txtUser )
    UI_Standards.setLocked( pView.panel.txtPartSeed )
    UI_Standards.setLocked( pView.panel.txtRemarks )

    pView.panel.selector.setVisible( false )
    pView.panel.btnCancel.setVisible( false )
    pView.panel.btnPrint.setEnabled( false )
    pView.panel.lblType.setText( pView.panel.TXT_TR_TYPE_LABEL )
  }

  void flagAdjust( InvTrView pView ) { }

  void flagQuantity( InvTrView pView, Boolean flagged ) {
    UI_Standards.setFlagged( pView.panel.txtType, flagged )
    if ( flagged ) {
      pView.data.txtStatus = pView.panel.MSG_UNABLE_TO_PARSE_QTY
      pView.panel.lblStatus.setText( pView.panel.MSG_UNABLE_TO_PARSE_QTY )
    }
  }

  void flagRemission( InvTrView pView ) { }

  Boolean isPartSeedValid( InvTrView pView ) { return false }

  Boolean isQuantityValid( InvTrView pView ) { return true }

  void processAdjust( InvTrView pView ) { }

  void processRemission( InvTrView pView ) { }

  void resetUI( InvTrView pView ) {
    pView.panel.comboSiteTo.setText( "" )
    pView.panel.txtType.setText( "" )
    pView.panel.selector.setText( "" )

    pView.panel.txtNbr.setText( "" )
    pView.panel.txtEffDate.setText( "" )
    pView.panel.txtRef.setText( "" )
    pView.panel.txtUser.setText( "" )

    pView.panel.txtPartSeed.setText( "" )
    pView.panel.txtRemarks.setText( "" )

    pView.panel.lblStatus.setText( "" )
    pView.panel.btnCancel.setVisible( false )
    pView.panel.btnPrint.setText( pView.panel.TXT_BTN_PRINT_CAPTION )
  }

  void refreshUI( InvTrView pView ) { }

  Boolean searchAdjust( InvTrView pView ) { return false }

  Boolean searchRemission( InvTrView pView ) { return false }

  void onButtonCancel( InvTrView pView ) {
    pView.panel.btnCancel.setVisible( false )
  }

  void onSkuDoubleClicked( InvTrView pView ) { }

}

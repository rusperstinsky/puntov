package mx.lux.pos.ui.view.driver

import mx.lux.pos.model.Articulo
import mx.lux.pos.model.ShipmentLine
import mx.lux.pos.model.TransInv
import mx.lux.pos.service.ArticuloService
import mx.lux.pos.service.InventarioService
import mx.lux.pos.ui.model.InvTrViewMode
import mx.lux.pos.ui.model.adapter.InvTrAdapter
import mx.lux.pos.ui.resources.ServiceManager
import mx.lux.pos.ui.view.panel.InvTrView
import mx.lux.pos.model.InvAdjustLine
import org.apache.commons.lang3.StringUtils
import mx.lux.pos.ui.controller.InvTrController

class InvTrFileAdjustDriver extends InvTrDriver {

  // Public methods
  Boolean assign( InvTrView pView ) {
    Boolean validated
    // Validate
    validated = ( pView.data.getSkuList().size() > 0 )
    if (validated)
      validated = ( StringUtils.isNotBlank( pView.panel.txtRemarks.text ) )

    // Assign
    if ( validated ) {
      pView.data.postRemarks = pView.panel.txtRemarks.text.trim()
    } else {
      if ( pView.data.getSkuList().size() == 0 ) {
        pView.data.flagOnPartSeed
      }
      if (StringUtils.isBlank( pView.panel.txtRemarks.text )) {
        pView.data.flagOnRemarks
      }
    }
    return validated
  }

  Boolean doBeforeSave( InvTrView pView ) {
    boolean result = super.doBeforeSave( pView )
    if ( result ) {
      // TODO: RLD RequestSystemPassword
      // InvTrController.instance.requestSystemPassword( )
    }
    return result
  }

  void enableUI( InvTrView pView ) {
    super.enableUI( pView )
    pView.panel.btnPrint.setEnabled( !pView.data.flagOnDocument )
    pView.panel.selector.setVisible( false )
  }

  void flagAdjust( InvTrView pView ) {
    pView.panel.lblStatus.setText( pView.data.documentWarning )
  }

  void processAdjust( InvTrView pView ) {
    ArticuloService partMaster = ServiceManager.partService
    pView.data.postReference = StringUtils.trimToEmpty( pView.data.adjustDocument.ref )
    pView.data.postRemarks = StringUtils.trimToEmpty( pView.data.adjustDocument.trReason )
    for ( InvAdjustLine ln in pView.data.adjustDocument.lines ) {
      Articulo part = partMaster.obtenerArticulo( ln.sku, false )
      if ( part != null ) {
        pView.data.addPart( part, ln.qty )
      }
    }
  }

  void refreshUI( InvTrView pView ) {
    pView.panel.lblStatus.setText( pView.data.accessStatus() )
    pView.panel.txtEffDate.setText( pView.adapter.getText( pView.data, InvTrAdapter.FLD_TODAY ) )
    pView.panel.txtRef.setText( pView.data.postReference )
    pView.panel.txtRemarks.setText( pView.data.postRemarks )
    pView.panel.txtUser.setText( pView.adapter.getText( pView.data.currentUser ) )
    pView.panel.browserSku.fireTableDataChanged()
  }

  Boolean searchAdjust( InvTrView pView ) {
    InventarioService service = ServiceManager.inventoryService
    List<TransInv> trList = new ArrayList<TransInv>()
    pView.data.flagOnDocument = true
    if ( StringUtils.isNotBlank(pView.data.adjustDocument.ref)) {
      trList = service.listarTransaccionesPorTipoAndReferencia( InvTrViewMode.ADJUST.trType.idTipoTrans,
        pView.data.adjustDocument.ref )
    }
    if ( trList.size() > 0 ) {
      pView.data.flagOnDocument = true
      pView.data.documentWarning = String.format( pView.panel.MSG_RECEIPT_WARNING,
          trList.size() + 1, InvTrViewMode.FILE_ADJUST.trType.idTipoTrans, pView.data.adjustDocument.ref,
          pView.adapter.getText( trList[ 0 ].fecha ), pView.adapter.getText( trList[ trList.size - 1 ].fecha ) )
    }
    return ( trList.size() > 0 )
  }

}

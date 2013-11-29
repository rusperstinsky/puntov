package mx.lux.pos.ui.view.driver

import mx.lux.pos.model.Articulo
import mx.lux.pos.model.ShipmentLine
import mx.lux.pos.model.TransInv
import mx.lux.pos.service.ArticuloService
import mx.lux.pos.service.InventarioService
import mx.lux.pos.ui.model.InvTrSku
import mx.lux.pos.ui.model.InvTrViewMode
import mx.lux.pos.ui.model.adapter.InvTrAdapter
import mx.lux.pos.ui.resources.ServiceManager
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.view.driver.InvTrDriver
import mx.lux.pos.ui.view.panel.InvTrView
import org.apache.commons.lang.StringUtils

class InvTrInBoundDriver extends InvTrDriver {

    // Public methods
    Boolean assign( InvTrView pView ) {
        Boolean validated
        // Validate
        validated = ( pView.data.getSkuList().size() > 0 )

        // Assign
        if ( validated ) {
            pView.data.postRemarks = pView.panel.txtRemarks.getText( )
            // No assign, no modifications allowed,
            // As the remission was processed a transaction is generated
        }

        return validated
    }

    void enableUI( InvTrView pView ) {
        super.enableUI( pView )
        pView.panel.btnPrint.setEnabled( !pView.data.flagOnDocument )
        pView.panel.btnPrint.setText( pView.panel.TXT_BTN_REGISTER_CAPTION )
        pView.panel.selector.setVisible( false )
        UI_Standards.setLocked( pView.panel.txtRemarks, false )
    }

    void flagRemission( InvTrView pView ) {
        pView.panel.lblStatus.setText( '' )
    }

    public void processRemission( InvTrView pView ) {
        ArticuloService partMaster = ServiceManager.partService
        pView.data.postReference = pView.data.receiptDocument.fullRef
        for ( ShipmentLine det in pView.data.receiptDocument.lines ) {
            Articulo part = partMaster.obtenerArticulo( det.sku, false )
            if ( part != null ) {
                pView.data.addPart( part, det.qty )
            }
        }
    }

    public void refreshUI( InvTrView pView ) {
        String sucOrigen = ''
        Integer quantity = 0
        for(InvTrSku article : pView.data.skuList){
            quantity = quantity+article.qty
        }
        if( pView?.data?.receiptDocument != null ){
            sucOrigen = String.format( '[%02d] SUNGLASS ISLAND %02d', pView?.data?.receiptDocument?.siteFrom, pView?.data?.receiptDocument?.siteFrom )
        }
        pView.panel.lblStatus.setText( pView.data.accessStatus() )
        pView.panel.txtEffDate.setText( pView.adapter.getText( pView.data, InvTrAdapter.FLD_TODAY ) )
        pView.panel.txtRef.setText( sucOrigen )
        pView.panel.txtUser.setText( pView.adapter.getText( pView.data.currentUser ) )
        pView.panel.txtType.setText( String.format( '%d', quantity ) )
        pView.panel.browserSku.fireTableDataChanged()
    }

    Boolean searchRemission( InvTrView pView ) {
        InventarioService service = ServiceManager.inventoryService
        List<TransInv> trList = service.listarTransaccionesPorTipoAndReferencia( InvTrViewMode.INBOUND.trType.idTipoTrans,
                pView.data.receiptDocument.fullRef )
        if ( trList.size > 0 ) {
            pView.data.flagOnDocument = true
            pView.data.documentWarning = String.format( pView.panel.MSG_RECEIPT_WARNING,
                    trList.size, InvTrViewMode.INBOUND.trType.idTipoTrans, pView.data.receiptDocument.fullRef,
                    pView.adapter.getText( trList[ 0 ].fecha ), pView.adapter.getText( trList[ trList.size - 1 ].fecha ) )
        }
        return ( trList.size() > 0 )
    }

}

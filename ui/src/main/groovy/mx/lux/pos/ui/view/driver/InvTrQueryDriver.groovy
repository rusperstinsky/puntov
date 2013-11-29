package mx.lux.pos.ui.view.driver

import mx.lux.pos.model.TransInvDetalle
import mx.lux.pos.ui.model.adapter.InvTrAdapter
import mx.lux.pos.ui.view.panel.InvTrView
import org.apache.commons.lang3.StringUtils
import mx.lux.pos.model.TransInv
import mx.lux.pos.model.InvTrType

class InvTrQueryDriver extends InvTrDriver {

  // Internal methods
  protected boolean isReprintable(TransInv pTrans) {
    boolean reprintable = false
    if (InvTrType.ADJUST.equals( pTrans.idTipoTrans ) ) {
      reprintable = true
    } else if (InvTrType.ISSUE.equals( pTrans.idTipoTrans ) ) {
      reprintable = true
    } else if (InvTrType.RETURN_XO.equals( pTrans.idTipoTrans )) {
      reprintable = true
    } else if (InvTrType.RECEIPT.equals( pTrans.idTipoTrans )) {
        reprintable = true
    } else if (InvTrType.OUTBOUND.equals( pTrans.idTipoTrans )) {
        reprintable = true
    } else if (InvTrType.INBOUND.equals( pTrans.idTipoTrans )) {
        reprintable = true
    }
    return reprintable
  }

  // Public methods
  void enableUI( InvTrView pView ) {
    super.enableUI( pView ) 

    pView.panel.lblType.setVisible( true )
    pView.panel.txtType.setVisible( true )
    pView.panel.selector.setVisible( true )
  }
  
  public void refreshUI( InvTrView pView ) {
    pView.panel.lblStatus.setText( pView.data.accessStatus( ) )
    if ( pView?.data?.qryInvTr != null ) {
        Integer cantArticulos = 0;
        for(TransInvDetalle detalle : pView?.data?.qryInvTr?.trDet ){
            cantArticulos = cantArticulos+detalle.cantidad
        }
      pView.panel.txtType.setText( StringUtils.trimToEmpty( cantArticulos.toString() ) )
      pView.panel.txtNbr.setText( pView.adapter.getText( pView.data.qryInvTr,  InvTrAdapter.FLD_TR_NBR ) )
      pView.panel.txtEffDate.setText( pView.adapter.getText( pView.data.qryInvTr,  InvTrAdapter.FLD_TR_EFF_DATE ) )
        if( pView.data.qryInvTr.idTipoTrans.equalsIgnoreCase('ENTRADA_TIENDA') ){
            pView.panel.txtRef.setText( pView.data?.qryInvTr?.sucursalDestino != null ? pView.data?.qryInvTr?.sucursalDestino.toString() : '' )
        } else if( pView.data.qryInvTr.idTipoTrans.equalsIgnoreCase('ENTRADA') ){
            pView.panel.txtRef.setText( pView.data?.qryInvTr?.referencia != null ? pView.data?.qryInvTr?.referencia.toString() : '' )
        } else {
            pView.panel.txtRef.setText( pView.data?.order?.factura ?: '' )
        }
      pView.panel.txtUser.setText(  pView.adapter.getText( pView.data.qryUser ) )
      pView.panel.comboSiteTo.setSelection( pView.data.qrySiteTo )
      pView.panel.txtRemarks.setText( pView.data.qryInvTr.observaciones )
      pView.panel.btnPrint.setEnabled( this.isReprintable(pView.data.qryInvTr) )
    } else {
      pView.panel.txtType.setText( "" )
      pView.panel.txtNbr.setText( "" )
      pView.panel.txtEffDate.setText( "" )
      pView.panel.txtRef.setText( "" )
      pView.panel.txtUser.setText( "" )
      pView.panel.comboSiteTo.setText( "" )
      pView.panel.txtRemarks.setText( "" )
    }
    pView.panel.selector.setText( pView.data.selectorText )
    pView.panel.browserSku.fireTableDataChanged()
  }

}

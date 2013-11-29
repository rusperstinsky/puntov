package mx.lux.pos.ui.model.adapter

import mx.lux.pos.model.InvTrDetRequest
import mx.lux.pos.model.InvTrRequest
import mx.lux.pos.ui.model.InvTr
import mx.lux.pos.ui.model.InvTrSku

class RequestAdapter {

  static InvTrRequest getRequest( InvTr pBuffer ) {
    InvTrRequest request = new InvTrRequest() 
    request.effDate = new Date()
    request.idUser = pBuffer.currentUser.username
    request.remarks = pBuffer.postRemarks
      if("ENTRADA_TIENDA".equalsIgnoreCase(pBuffer?.postTrType?.idTipoTrans)){
          request.siteFrom = ( pBuffer.receiptDocument.siteFrom != null ? pBuffer.receiptDocument.siteFrom : null )
          request.reference = pBuffer.claveCodificada
      } else {
          request.reference = pBuffer.postReference
      }
    request.siteTo = ( pBuffer.postSiteTo != null ? pBuffer.postSiteTo.id : null )
    request.trType = pBuffer?.postTrType?.idTipoTrans
    for (InvTrSku inSku in pBuffer.skuList) {
      request.skuList.add( new InvTrDetRequest( inSku.sku, inSku.qty ) )
    }
    return request
  }
  
}

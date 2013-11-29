package mx.lux.pos.ui.model

import mx.lux.pos.model.TipoTransInv

class InventoryViewMode {
  
  def String idTransType
  def String text
  def boolean isQuery
  def boolean isTransaction
  
  InventoryViewMode( TipoTransInv pTipoTransInv ) {
    idTransType = pTipoTransInv.getIdTipoTrans()
    text = String.format( "[%s] %s", pTipoTransInv.getIdTipoTrans(), pTipoTransInv.getDescripcion() )
    isQuery = false
    isTransaction = true
  }
  
  InventoryViewMode(String pText) {
    idTransType = ""
    text = pText
    isQuery = true
    isTransaction = false
  }
  
  String toString() {
    return this.getText()
  }

}

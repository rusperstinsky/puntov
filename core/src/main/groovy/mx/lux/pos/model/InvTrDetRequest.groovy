package mx.lux.pos.model

class InvTrDetRequest {
  def Integer sku
  def Integer qty
  
  InvTrDetRequest( Integer pSku, Integer pQty ) {
    sku = pSku
    qty = pQty
  }
}

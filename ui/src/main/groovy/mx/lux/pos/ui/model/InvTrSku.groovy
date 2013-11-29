package mx.lux.pos.ui.model

import mx.lux.pos.model.Articulo
import mx.lux.pos.ui.model.adapter.PartAdapter

class InvTrSku {

  private static PartAdapter adapter = PartAdapter.instance
  private InvTr parent
  private Integer line
  def Articulo part
  def Integer qty

  InvTrSku( InvTr pParent, Articulo pPart ) {
    this( pParent, pParent.nextLine( ), pPart, 1)
  }
  
  InvTrSku( InvTr pParent, Articulo pPart, Integer pQty ) {
    this( pParent, pParent.nextLine( ), pPart, pQty)
  }

  InvTrSku( InvTr pParent, Integer pLinea, Articulo pPart, Integer pQty ) {
    parent = pParent
    line = pLinea
    part = pPart
    qty = pQty
  }

  Integer getLine() {
    return line
  }
  
  Integer getSku() {
    return part.id
  }
  
  String getDescription() {
    return adapter.getText( part, PartAdapter.FLD_INV_DESC )
  }
  
  String getMovType() {
    return parent.movType
  }
  
  String toString() {
    return adapter.getText( part )
  }

}

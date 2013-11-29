package mx.lux.pos.model

import mx.lux.pos.util.CustomDateUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils

class InvTrRequest {
  
  def String trType = ""
  def Date effDate = DateUtils.truncate( new Date(), Calendar.DATE)
  def Integer siteTo = null
  def Integer siteFrom = null
  def String reference = ""
  def String remarks = ""
  def String idUser = ""
  def List<InvTrDetRequest> skuList = new ArrayList<InvTrDetRequest>()

  String toString() {
    String str = String.format("[InvTr Request] TrType:%s  EffDate:%s  SiteTo:%s %s %s  User:%s", 
      trType,
      CustomDateUtils.format( effDate, "dd/MM/yyyy" ), 
      (siteTo == null ? "none" : siteTo),
      (siteFrom == null ? "none" : siteFrom),
      ( !StringUtils.isEmpty( reference ) ? " Reference:${ reference } " : ""),
      ( !StringUtils.isEmpty( remarks ) ? "Remarks:${ remarks }" : ""),
      idUser
    )
    for ( part in skuList ) {
      str += "\n    SKU:${ part.sku }  Qty:${ part.qty }"
    }
    return str
  }
}

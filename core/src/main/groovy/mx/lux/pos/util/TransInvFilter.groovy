package mx.lux.pos.util

import mx.lux.pos.model.Articulo
import mx.lux.pos.model.TransInv
import mx.lux.pos.model.TransInvDetalle
import mx.lux.pos.service.ArticuloService
import org.apache.commons.lang3.time.DateUtils

import java.text.DateFormat
import java.text.SimpleDateFormat

class TransInvFilter {

  private DateFormat df

  Date dateFrom, dateTo
  String idTrType, idPartCode
  Integer sku, siteTo
  
  private ArticuloService partMaster

  public TransInvFilter() {
    this.setDateRange(new Date())
  }
  
  // Internal Methods
  protected DateFormat getDateFormatter() {
    if (this.df == null) {
      this.df = new SimpleDateFormat("dd/MM/yyyy")
    }
    return this.df
  }
  
  protected ArticuloService getPartMaster() {
    return partMaster
  }
  

  protected Date round(Date pDate) {
    return DateUtils.truncate(pDate, Calendar.DATE)
  }

  protected boolean select(TransInv pInvTrans) {
    boolean selected = true
    if (selected && this.isDateRangeActive()) {
      selected = ((this.getDateFrom().compareTo(pInvTrans.getFecha()) <= 0)
                && (this.getDateTo().compareTo(pInvTrans.getFecha()) >= 0))
    }
    if (selected && this.isTrTypeActive()) {
      selected = (pInvTrans.getIdTipoTrans().equalsIgnoreCase(this.getIdTrType()))
    }
    if (selected && this.isSiteToActive()) {
      selected = (pInvTrans.getSucursalDestino().equals(this.getSiteTo()))
    }
    if (selected && this.isSkuActive()) {
      boolean found = false
      for (TransInvDetalle trDet : pInvTrans.getTrDet()) {
        if (trDet.getSku().equals(this.getSku())) {
          found = true
          break
        }
      }
      selected = found
    }
    if (selected && this.isPartCodeActive()) {
      boolean found = false
      for (TransInvDetalle trDet : pInvTrans.getTrDet()) {
        Articulo part = this.getPartMaster().obtenerArticulo(trDet.getSku(), false)
        if (part.getArticulo().toUpperCase().startsWith(this.getIdPartCode())) {
          found = true
          break
        }
      }
      selected = found
    }
    return selected
  }
  
  // Public Properties
  boolean isDateRangeActive() {
    return ((this.getDateFrom() != null) && (this.getDateTo() != null))
  }
  
  boolean isTrTypeActive() {
    return (this.getIdTrType() != null)
  }

  boolean isPartCodeActive() {
    return (this.getIdPartCode() != null)
  }

  boolean isSiteToActive() {
    return (this.getSiteTo() != null)
  }

  boolean isSkuActive() {
    return (this.getSku() != null)
  }

  void setDateRange(Date pDate) {
    this.setDateRange(pDate, pDate)
  }
  
  void setDateRange(Date pDateFrom, Date pDateTo) {
    this.dateFrom = (pDateFrom != null ? this.round(pDateFrom) : null)
    this.dateTo = (pDateTo != null ? this.round(pDateTo) : null)
  }
  
  void setIdPartCode(String pIdPartCode) {
    if (pIdPartCode != null) {
      this.idPartCode = pIdPartCode.trim().toUpperCase()
    } else {
      this.idPartCode = null
    }
  }
  
  void setIdTrType(String pIdTrType) {
    if (pIdTrType != null) {
      this.idTrType = pIdTrType.trim().toUpperCase()
    } else {
      this.idTrType = null
    }
  }
  
  String toString() {
    String str = "[Inv Trans Filter]"
    if (this.isDateRangeActive()) {
      str = String.format("%s DateFrom:%s DateTo:%s", str, this.getDateFormatter().format(this.getDateFrom()),
                this.getDateFormatter().format(this.getDateTo()))
    }
    if (this.isTrTypeActive()) {
      str = String.format("%s TrType:%s", str, this.getIdTrType())
    }
    if (this.isSiteToActive()) {
      str = String.format("%s SiteTo:%d", str, this.getSiteTo())
    }
    if (this.isSkuActive()) {
      str = String.format("%s SKU:%d", str, this.getSku())
    }
    if (this.isPartCodeActive()) {
      str = String.format("%s Part:%s", str, this.getIdPartCode())
    }
    return str
  }
  
  // Public Filtering Methods
  List<TransInv> filter(List<TransInv> pInvTransList) {
    List<TransInv> filtered = new ArrayList<TransInv>(pInvTransList.size())
    for (TransInv tr in pInvTransList) {
      if (this.select(tr)) {
        filtered.add(tr)
      }
    }
    return filtered
  }
  
  void reset() {
    this.setDateRange(null)
    this.setIdTrType(null)
    this.setIdPartCode(null)
    this.setSiteTo(null)
    this.setSku(null)
  }
}

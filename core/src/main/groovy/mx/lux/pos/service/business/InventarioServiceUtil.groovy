package mx.lux.pos.service.business

import mx.lux.pos.model.Articulo
import mx.lux.pos.model.TransInv
import mx.lux.pos.model.TransInvDetalle
import mx.lux.pos.repository.TransInvDetalleRepository
import mx.lux.pos.repository.TransInvRepository
import org.springframework.stereotype.Component

import javax.annotation.Resource

@Component
class InventarioServiceUtil {

  @Resource
  private TransInvRepository trMstrRepository
  
  @Resource
  private TransInvDetalleRepository trDetRepository
  
  private Comparator<TransInv> trMstrSorter
  private Comparator<TransInvDetalle> trDetSorter
  
  // Internal Methods
  private Comparator<TransInv> getTrMstrSorter() {
    if (this.trMstrSorter == null) {
      this.trMstrSorter = new Comparator<TransInv>() {
        public int compare(TransInv pTr1, TransInv pTr2) {
          return -1 * (pTr1.getFechaMod().compareTo(pTr2.getFechaMod()))
        }
      }
    }
    return this.trMstrSorter
  }

  private Comparator<TransInvDetalle> getTrDetSorter() {
    if (this.trDetSorter == null) {
      this.trDetSorter = new Comparator<TransInvDetalle>() {
        public int compare(TransInvDetalle pTrDet1, TransInvDetalle pTrDet2) {
          int result = pTrDet1.getIdTipoTrans().compareToIgnoreCase(pTrDet2.getIdTipoTrans())
          if (result == 0) {
            result = pTrDet1.getFolio().compareTo(pTrDet2.getFolio())
          }
          return result
        }
      }
    }
    return this.trDetSorter
  }

  // Public Methods
  List<Integer> buildSkuList(List<Articulo> pPartList) {
    List<Integer> list = new ArrayList<Integer>(pPartList.size())
    for (Articulo part : pPartList) {
      list.add(part.getId())
    }
    return list
  }
  
  void fillTrMstr( List<TransInv> pInvTransList ) {
    for (TransInv tr in pInvTransList ) {
      tr.setTrDet(trDetRepository.findByIdTipoTransAndFolio(tr.getIdTipoTrans(), tr.getFolio()))
    }
  }
  
  List<TransInv> listTrMstr( List<TransInvDetalle> pTrDetList ) {
    Collections.sort(pTrDetList, this.getTrDetSorter())
    List<TransInv> trMstrList = new ArrayList<TransInv>()
    TransInv prev = null
    for (TransInvDetalle trDet in pTrDetList) {
      if ((prev == null) || (!prev.equals(trDet))) {
        List<TransInv> one = trMstrRepository.findByIdTipoTransAndFolio(trDet.getIdTipoTrans(), trDet.getFolio())
        if ((one != null) && (one.size() > 0)) {
          prev = one.get(0)
          trMstrList.add(prev)
        }
      }
    }
    sortTrMstr(trMstrList)
    return trMstrList
  }

  void sortTrMstr( List<TransInv> pTrList ) {
    Collections.sort(pTrList, this.getTrMstrSorter())
  }

}

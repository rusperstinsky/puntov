package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.lux.pos.model.ListaPrecios

@Bindable
@ToString
@EqualsAndHashCode
class PriceList {
  String id
  String filename
  String branch
  Date activated
  Date autoActivated
  Date loaded
  PriceListLoadType loadType
  List<Item> items = [ ]

  static PriceList toPriceList( ListaPrecios listaPrecios ) {
    if ( listaPrecios?.id ) {
      return new PriceList(
          id: listaPrecios.id,
          filename: listaPrecios.filename,
          activated: listaPrecios.fechaAct,
          autoActivated: listaPrecios.fechaActAuto,
          loaded: listaPrecios.fechaCarga,
          loadType: PriceListLoadType.parse( listaPrecios.tipoCarga )
      )
    }
    return null
  }
}

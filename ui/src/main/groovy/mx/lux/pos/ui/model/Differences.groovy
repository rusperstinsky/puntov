package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.util.logging.Slf4j
import mx.lux.pos.model.Articulo
import mx.lux.pos.model.DetalleNotaVenta
import mx.lux.pos.model.Diferencia
import mx.lux.pos.ui.controller.ItemController

@Slf4j
@Bindable
@ToString
@EqualsAndHashCode
class Differences {
  Integer sku
  Integer physicalDiff
  Integer soiDiff
  Integer differences
  Articulo article

  static Differences toDifferences( Diferencia diferencia ) {
    if ( diferencia?.id ) {
      Differences differences = new Differences(
          sku: diferencia.id,
          physicalDiff: diferencia.cantidadFisico,
          soiDiff: diferencia.cantidadSoi,
          differences: diferencia.diferencias,
          article: diferencia.articulo
      )
      return differences
    }
    return null
  }

}
package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.util.logging.Slf4j
import mx.lux.pos.model.DetalleNotaVenta
import mx.lux.pos.ui.resources.ServiceManager

@Slf4j
@Bindable
@ToString
@EqualsAndHashCode
class OrderItem {
  Item item
  Integer quantity = 1
  String delivers

  String getDescription( ) {
    String descripcion
    if ( ServiceManager.partService.useShortItemDescription() ) {
      String desc = item?.description?.replaceFirst( item.name.trim() + "/", "" )
      descripcion = "[${item?.id ?: ''}] ${desc ?: ''}"
    } else {
      descripcion = "${item?.description ?: ''}${delivers ? " (${delivers})" : ''}"

    }
    return descripcion
  }


  static OrderItem toOrderItem( DetalleNotaVenta detalleNotaVenta ) {
    try {
      if ( detalleNotaVenta?.id ) {
        OrderItem orderItem = new OrderItem(
            item: Item.toItem( detalleNotaVenta ),
            quantity: detalleNotaVenta.cantidadFac,
            delivers: detalleNotaVenta.surte
        )
        return orderItem
      }
    } catch ( Exception e ) {
      log.error( "Error en el DetalleNotaVenta ", e.toString() )
    }
    return null
  }
}

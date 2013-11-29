package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@Bindable
@ToString
@EqualsAndHashCode
class Deal {
  String id
  String name
  String reference
  DealType type = DealType.DISCOUNT
  String value

  String getDescription( ) {
    "${name}"
  }
}

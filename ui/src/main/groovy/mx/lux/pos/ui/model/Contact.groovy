package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@Bindable
@ToString
@EqualsAndHashCode
class Contact {
  String primary
  String extra
  String comments
  ContactType type = ContactType.HOME_PHONE
}

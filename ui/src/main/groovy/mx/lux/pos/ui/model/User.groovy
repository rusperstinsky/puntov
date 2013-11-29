package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.lux.pos.model.Empleado

@Bindable
@ToString( excludes = 'password' )
@EqualsAndHashCode
class User {
  String name
  String fathersName
  String mothersName
  String username
  String password

  String getFullName( ) {
    "${name ?: ''} ${fathersName ?: ''} ${mothersName ?: ''}"
  }

  static toUser( Empleado empleado ) {
    if ( empleado?.id ) {
      def user = new User()
      user.name = empleado.nombre
      user.fathersName = empleado.apellidoPaterno
      user.mothersName = empleado.apellidoMaterno
      user.username = empleado.id
      user.password = empleado.passwd
      return user
    }
    return null
  }

  boolean equals(Object pObj) {
    boolean result = false
    if (pObj instanceof User) {
      result = this.getUsername().trim().equalsIgnoreCase(pObj.getUsername().trim())
    } else if ( pObj instanceof Empleado ) {
      result = this.getUsername().trim().equalsIgnoreCase(pObj.getId().trim())
    }
    return result
  }

  String toString() {
    return String.format('(%s) %s', this.getUsername(), this.getFullName())
  }
}

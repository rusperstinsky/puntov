package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.lux.pos.model.Sucursal

@Bindable
@ToString
@EqualsAndHashCode
class Branch {
  Integer id
  String name
  String address
  String colony
  String postalCode
  String city
  String telephoneNumbers
  String costCenter

  static Branch toBranch( Sucursal sucursal ) {
    if ( sucursal?.id ) {
      Branch branch = new Branch(
          id: sucursal.id,
          name: sucursal.nombre,
          address: sucursal.direccion,
          colony: sucursal.colonia,
          postalCode: sucursal.cp,
          city: sucursal.ciudad,
          telephoneNumbers: sucursal.telefonos,
          costCenter: sucursal.centroCostos
      )
      return branch
    }
    return null
  }
}

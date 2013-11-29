package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.lux.pos.model.Contribuyente

@Bindable
@ToString
@EqualsAndHashCode
class Taxpayer {
  Integer id
  Integer customerId
  String rfc
  String name
  String primary
  String location
  String city
  String stateId
  String zipcode
  String phone
  String email
  Date date

  static Taxpayer toTaxpayer( Contribuyente contribuyente ) {
    if ( contribuyente?.id ) {
      return new Taxpayer(
          id: contribuyente.id,
          customerId: contribuyente.idCliente,
          rfc: contribuyente.rfc,
          name: contribuyente.nombre,
          primary: contribuyente.domicilio,
          location: contribuyente.colonia,
          city: contribuyente.ciudad,
          stateId: contribuyente.idEstado,
          zipcode: contribuyente.codigoPostal,
          phone: contribuyente.telefono,
          email: contribuyente.email,
          date: contribuyente.fechaRegistro
      )
    }
    return null
  }
}

package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.lux.pos.model.Cliente
import org.apache.commons.lang3.StringUtils
import groovy.util.logging.Slf4j

@Slf4j
@Bindable
@ToString
@EqualsAndHashCode
class Customer {
  Integer id
  String name
  String fathersName
  String mothersName
  String title
  boolean legalEntity
  CustomerType type = CustomerType.DOMESTIC
  String rfc = CustomerType.DOMESTIC.rfc
  Date dob
  GenderType gender = GenderType.MALE
  Address address
  List<Contact> contacts = [ ]
  Integer age

  private static final Integer EDAD_DEFAULT = 25

  String getFullName( ) {
    "${title ? "${title} " : ''}${name ?: ''} ${fathersName ?: ''} ${mothersName ?: ''}"
  }

  static Integer parse( String edad ){
    Integer age = EDAD_DEFAULT
    try{
      if( edad.length() > 0 ){
        age = Integer.parseInt( edad )
      } else {
        age = null
      }
    } catch( Exception e ){
      log.error( "Error en la edad", e )
    }
    return age
  }

  static Customer toCustomer( Cliente cliente ) {

    if ( cliente?.id ) {
      Customer customer = new Customer(
          id: cliente.id,
          name: cliente.nombre,
          fathersName: cliente.apellidoPaterno,
          mothersName: cliente.apellidoMaterno,
          title: cliente.titulo,
          rfc: cliente.rfc,
          dob: cliente.fechaNacimiento,
          gender: GenderType.parse( cliente.sexo ),
          address: Address.toAddress( cliente ),
          age:  parse( StringUtils.trimToEmpty( cliente.udf1 ) )
      )
      if ( cliente.clientePais?.id ) {
        customer.type = CustomerType.FOREIGN
        customer.rfc = CustomerType.FOREIGN.rfc
      }
      if ( StringUtils.isNotBlank( cliente.telefonoCasa ) ) {
        customer.contacts.add( new Contact(
            primary: cliente.telefonoCasa,
            type: ContactType.HOME_PHONE
        ) )
      }
      if ( StringUtils.isNotBlank( cliente.email ) ) {
        customer.contacts.add( new Contact(
            primary: cliente.email,
            type: ContactType.EMAIL
        ) )
      }
      return customer
    }
    return null
  }

  boolean equals(Object pObj) {
    boolean result = false;
    if ( pObj instanceof Customer ) {
      result = this.getId().equals( (pObj as Customer).getId() )
    }
    return result
  }
}

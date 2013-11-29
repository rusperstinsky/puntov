package mx.lux.pos.model

import mx.lux.pos.service.business.Registry
import mx.lux.pos.service.impl.ServiceFactory
import org.apache.commons.lang3.StringUtils

class AddressAdapter {

  private enum ObjType {
    Unknown, Contribuyente, Sucursal
  }

  Object obj
  ObjType type = ObjType.Unknown

  AddressAdapter( Contribuyente pEntity ) {
    type = ObjType.Contribuyente
    obj = pEntity
  }

  // Internal methods
  protected Contribuyente asEntity( ) {
    return ( obj as Contribuyente )
  }

  // Public methods
  String getShortName( ) {
    String name = obj.toString()
    if ( ObjType.Contribuyente.equals( type ) ) {
      name = Registry.companyShortName
    }
    return name
  }

  String getName( ) {
    String name = obj.toString()
    if ( ObjType.Contribuyente.equals( type ) ) {
      name = StringUtils.trimToEmpty( asEntity().nombre ).toUpperCase()
    }
    return name
  }

  String getAddress_1( ) {
    String address = ""
    if ( ObjType.Contribuyente.equals( type ) ) {
      address = StringUtils.trimToEmpty( asEntity().domicilio ).toUpperCase()
    }
    return address
  }

  String getAddress_2( ) {
    String address = ""
    if ( ObjType.Contribuyente.equals( type ) ) {
      address = StringUtils.trimToEmpty( asEntity().colonia ).toUpperCase()
    }
    return address
  }

  String getAddress_3( ) {
    String address = ""
    if ( ObjType.Contribuyente.equals( type ) ) {
      Estado estado = ServiceFactory.states.obtenerEstado( StringUtils.trimToEmpty( asEntity().idEstado ) )
      address = String.format( "C.P. %s  %s, %s",
          StringUtils.trimToEmpty( asEntity().codigoPostal ).toUpperCase(),
          StringUtils.trimToEmpty( asEntity().ciudad ).toUpperCase(),
          StringUtils.trimToEmpty( estado?.nombre ).toUpperCase() )
    }
    return address
  }


  String getCity( ) {
    String city = ""
    if ( ObjType.Contribuyente.equals( type ) ) {
      city = StringUtils.trimToEmpty( asEntity().ciudad ).toUpperCase()
    }
    return city
  }


  String getCP( ) {
    String cp = ""
    if ( ObjType.Contribuyente.equals( type ) ) {
      cp = StringUtils.trimToEmpty( asEntity().codigoPostal ).toUpperCase()
    }
    return cp
  }


  String getPhone( ) {
    String taxid = ""
    if ( ObjType.Contribuyente.equals( type ) ) {
      taxid = StringUtils.trimToEmpty( asEntity().telefono ).toUpperCase()
    }
    return taxid
  }

  String getTaxId( ) {
    String taxid = ""
    if ( ObjType.Contribuyente.equals( type ) ) {
      taxid = String.format( "RFC: %s", StringUtils.trimToEmpty( asEntity().rfc ).toUpperCase() )
    }
    return taxid
  }

  String getWebAddress( ) {
    String address = ""
    if ( ObjType.Contribuyente.equals( type ) ) {
      address = StringUtils.trimToEmpty( asEntity().email )
    }
    if ( address.length() == 0 ) {
      address = this.getShortName()
    }
    return address
  }

  Boolean hasCustomerService( ) {
    Boolean enabled = false
    if ( ObjType.Contribuyente.equals( type ) ) {
      enabled = ( StringUtils.trimToNull( asEntity().telefono ) != null )
    }
    return enabled
  }

  String toString( ) {
    StringBuffer sb = new StringBuffer()
    sb.append( this.getShortName() )
    sb.append( String.format( "\n%s", this.getName() ) )
    sb.append( String.format( "\n%s", this.getAddress_1() ) )
    sb.append( String.format( "\n%s", this.getAddress_2() ) )
    sb.append( String.format( "\n%s", this.getAddress_3() ) )
    sb.append( String.format( "\n%s", this.getTaxId() ) )
    return sb.toString()
  }
}

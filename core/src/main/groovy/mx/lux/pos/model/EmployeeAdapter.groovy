package mx.lux.pos.model

import mx.lux.pos.util.StringList
import org.apache.commons.lang.StringUtils

class EmployeeAdapter {

  private static enum FieldOrder {
    UserId, Name, IdEmp, Position, Password, Fld_1
  }
  private static final String MSG_INVALID_DATA = 'Invalid Data Empleado(%s)'

  private StringList data

  private EmployeeAdapter( ) { }

  // Public methods
  static EmployeeAdapter parse( StringList pData ) {
    EmployeeAdapter adapter
    if ( pData.getSize() >= FieldOrder.values().size() ) {
      Integer id = pData.asInteger( FieldOrder.IdEmp.ordinal() )
      if ( id > 0 ) {
        adapter = new EmployeeAdapter()
        adapter.data = pData
      } else {
        throw new IllegalArgumentException( String.format( MSG_INVALID_DATA, pData ) )
      }
    } else {
      throw new IllegalArgumentException( String.format( MSG_INVALID_DATA, pData ) )
    }
    return adapter
  }

  static EmployeeAdapter toAdapter( Empleado pEmpleado, String pClave ) {
    EmployeeAdapter adapter = new EmployeeAdapter()
    adapter.data = new StringList()
    String nombre = StringUtils.trimToEmpty( pEmpleado.getNombreCompleto() )
    for ( FieldOrder f : FieldOrder.values() ) {
      switch ( f ) {
        case FieldOrder.UserId:
          adapter.data.add( pClave )
          break
        case FieldOrder.Name:
          adapter.data.add( nombre )
          break
        case FieldOrder.IdEmp:
          adapter.data.add( pEmpleado.id )
          break
        case FieldOrder.Position:
          adapter.data.add( String.format( '%d', pEmpleado.idPuesto ) )
          break
        case FieldOrder.Password:
          adapter.data.add( pEmpleado.passwd )
          break
        default:
          adapter.data.add( '' )
          break
      }
    }
    return adapter
  }

  Boolean isValid( ) {
    return true
  }

  String getUserId( ) {
    return this.data.entry( FieldOrder.UserId.ordinal() )
  }

  String getNombre( ) {
    return this.data.entry( FieldOrder.Name.ordinal() )
  }

  String getIdEmpleado( ) {
    return String.format( '%04d', this.data.asInteger( FieldOrder.IdEmp.ordinal() ) )
  }

  Integer getIdPuesto( ) {
    return this.data.asInteger( FieldOrder.Position.ordinal() )
  }

  String getPassword( ) {
    return this.data.entry( FieldOrder.Password.ordinal() )
  }

  void update( Empleado pEmpleado ) {
    pEmpleado.nombre = this.getNombre()
    pEmpleado.id = this.getIdEmpleado()
    pEmpleado.idPuesto = this.getIdPuesto()
    pEmpleado.passwd = this.getPassword()
    pEmpleado.setFechaModificado( new Date() )
  }

  String toExportString( ) {
    return this.data.toString( "|" )
  }
}

package mx.lux.pos.repository.custom

import java.util.Date;
import java.util.List;

import mx.lux.pos.model.Cliente

interface ClienteRepositoryCustom {

  List<Cliente> findByNombreApellidos( String nombre, String apellidoPaterno, String apellidoMaterno )
  List<Cliente> findByFechaAlta( Date fecha )

}

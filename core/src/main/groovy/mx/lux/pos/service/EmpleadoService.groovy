package mx.lux.pos.service

import mx.lux.pos.model.Empleado

interface EmpleadoService {

  Empleado obtenerEmpleado( String id )

  String gerente( )

  void actualizarPass( empleado )

  Boolean sesionPrimeraVez()

}

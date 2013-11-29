package mx.lux.pos.service

import mx.lux.pos.model.Banco
import mx.lux.pos.model.Plan
import mx.lux.pos.model.Terminal

public interface ComboService {

  List<Banco> obtenerTodosBancos( )

  List<Terminal> obtenerTodosTerminales( )

  List<Plan> obtenerTodosPlanes( )

  List<Plan> obtenerPlanesPorBanco( Integer idBanco )

}

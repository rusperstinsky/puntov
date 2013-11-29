package mx.lux.pos.service

import mx.lux.pos.model.Plan

interface PlanService {

  List<Plan> listarPlanesPorTerminal( String idTerminal )

  List<Plan> listarPlanesPorBancoDeposito( Integer idBancoDeposito )

}

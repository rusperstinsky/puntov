package mx.lux.pos.repository.custom

import mx.lux.pos.model.Plan

interface PlanRepositoryCustom {

  List<Plan> findBy_IdBanco( Integer idBanco )

  List<Plan> findByIdTerminal( String idTerminal )

  List<Plan> findByIdBancoDeposito( Integer idBancoDeposito )

}

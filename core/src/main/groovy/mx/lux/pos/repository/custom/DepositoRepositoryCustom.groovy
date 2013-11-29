package mx.lux.pos.repository.custom

import mx.lux.pos.model.Deposito

interface DepositoRepositoryCustom {

  List<Deposito> findBy_Fecha( Date fecha )

  Deposito findBy_Id( Integer id )

}

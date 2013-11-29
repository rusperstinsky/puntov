package mx.lux.pos.repository.custom

import mx.lux.pos.model.OrdenPromDet

interface OrdenPromDetRepositoryCustom {

  List<OrdenPromDet> findByFechaMod( Date fecha )

}

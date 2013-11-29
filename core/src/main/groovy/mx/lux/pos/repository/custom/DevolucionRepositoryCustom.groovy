package mx.lux.pos.repository.custom

import mx.lux.pos.model.Devolucion

interface DevolucionRepositoryCustom {

  List<Devolucion> findBy_Fecha( Date fecha )

}

package mx.lux.pos.repository.custom

import mx.lux.pos.model.Modificacion

interface ModificacionRepositoryCustom {

  List<Modificacion> findBy_Fecha( Date fecha )

  Modificacion getBy_IdFactura( String idFactura )

}

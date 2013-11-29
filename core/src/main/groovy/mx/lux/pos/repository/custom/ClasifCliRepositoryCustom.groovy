package mx.lux.pos.repository.custom

import mx.lux.pos.model.ClasifCliente

interface ClasifCliRepositoryCustom {

  ClasifCliente getBy_IdFactura( String idFactura )

}

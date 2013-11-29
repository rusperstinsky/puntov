package mx.lux.pos.repository.custom

import mx.lux.pos.model.Descuento

interface DescuentoRepositoryCustom {

  Descuento getBy_IdFactura( String idFactura )

}

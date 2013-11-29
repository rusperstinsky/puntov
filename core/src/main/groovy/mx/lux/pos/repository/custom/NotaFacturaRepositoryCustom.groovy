package mx.lux.pos.repository.custom

import mx.lux.pos.model.NotaFactura

interface NotaFacturaRepositoryCustom {

  List<NotaFactura> findByFechaImpresion( Date fecha )

}

package mx.lux.pos.repository.custom

import mx.lux.pos.model.NotaVenta

interface NotaVentaRepositoryCustom {

  List<NotaVenta> findByFacturaNotEmptyLimitingLatestResults( Integer results )

}

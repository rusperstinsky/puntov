package mx.lux.pos.service

import mx.lux.pos.model.NotaVenta
import mx.lux.pos.model.DetalleNotaVenta
import mx.lux.pos.model.Pago

interface BulkLoaderService {

    void purge( List<NotaVenta> pOrders )

    void save( List<NotaVenta> pOrders, List<DetalleNotaVenta> pOrderLines, List<Pago> pPayments )

}

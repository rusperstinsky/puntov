package mx.lux.pos.service

import mx.lux.pos.model.DetalleNotaVenta

interface DetalleNotaVentaService {

  DetalleNotaVenta obtenerDetalleNotaVenta( String idFactura, Integer idArticulo )

  List<DetalleNotaVenta> listarDetallesNotaVentaPorIdFactura( String idFactura )

}

package mx.lux.pos.service

import mx.lux.pos.model.*

interface NotaVentaService {

  NotaVenta obtenerNotaVenta( String idNotaVenta )

  NotaVenta abrirNotaVenta( )

  NotaVenta registrarNotaVenta( NotaVenta notaVenta )

  NotaVenta registrarDetalleNotaVentaEnNotaVenta( String idNotaVenta, DetalleNotaVenta detalleNotaVenta )

  NotaVenta eliminarDetalleNotaVentaEnNotaVenta( String idNotaVenta, Integer idArticulo )

  NotaVenta registrarPagoEnNotaVenta( String idNotaVenta, Pago pago )

  NotaVenta eliminarPagoEnNotaVenta( String idNotaVenta, Integer idPago )

  void eliminarNotaVenta( String idNotaVenta )

  NotaVenta cerrarNotaVenta( NotaVenta notaVenta )

  List<NotaVenta> listarUltimasNotasVenta( )

  List<NotaVenta> listarNotasVentaPorParametros( Map<String, Object> parametros )

  NotaVenta obtenerNotaVentaPorTicket( String ticket )

  SalesWithNoInventory obtenerConfigParaVentasSinInventario( )

  Empleado obtenerEmpleadoDeNotaVenta( pOrderId )

  void saveOrder( NotaVenta pNotaVenta )

  NotaVenta obtenerNotaVentaPorFactura( String factura )

  List<NotaVenta> obtenerDevolucionesPendientes( Date fecha )

}

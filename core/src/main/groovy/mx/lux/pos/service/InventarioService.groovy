package mx.lux.pos.service

import mx.lux.pos.model.*

interface InventarioService {

  // TipoTransInv Entity
  List<TipoTransInv> listarTiposTransaccion( )

  Integer obtenerSiguienteFolio( String pIdTipoTransInv )

  TipoTransInv obtenerTipoTransaccion( String pIdTipoTransInv )

  // TransInv Entity
  Date obtenerUltimaFechaTransaccion( )

  Integer registrarTransaccion( TransInv pTransInv )

  Integer solicitarTransaccion( InvTrRequest pRequest )

  Boolean solicitarTransaccionVenta( NotaVenta pNotaVenta )

  Boolean solicitarTransaccionDevolucion( NotaVenta pNotaVenta )

  TransInv obtenerTransaccion( String pIdTipoTrans, Integer pFolio )

  Boolean isReceiptDuplicate( )

  // TransInvDetalle Entity

  Integer obtenerExistenciaPorArticulo( Integer id )

  Collection<Articulo> listarArticulosConExistencia( )

  // Services added for Inventory View
  TipoTransInv obtenerTipoTransaccionAjuste( )

  TipoTransInv obtenerTipoTransaccionDevolucion( )

  TipoTransInv obtenerTipoTransaccionDevolucionExtraordinaria( )

  TipoTransInv obtenerTipoTransaccionEntrada( )

  TipoTransInv obtenerTipoTransaccionSalida( )

  TipoTransInv obtenerTipoTransaccionVenta( )

  TipoTransInv obtenerTipoTransaccionSalidaAlmacen();

  TipoTransInv obtenerTipoTransaccionEntradaAlmacen();

  List<Sucursal> listarSucursales( )

  Sucursal sucursalActual( )

  Empleado obtenerEmpleado( String pEmpId )

  Sucursal obtenerSucursal( Integer pSite )

  List<TransInv> listarTransaccionesPorRangoFecha( Date pRangeStart, Date pRangeEnd )

  List<TransInv> listarTransaccionesPorTipo( String pIdTipoTrans )

  List<TransInv> listarTransaccionesPorSucursalDestino( Integer pSiteTo )

  List<TransInv> listarTransaccionesPorSku( Integer pSku )

  List<TransInv> listarTransaccionesPorArticulo( String pPartCodeSeed )

  List<TransInv> listarTransaccionesPorTipoAndReferencia( String pTrType, String pReference )

  InvAdjustSheet leerArchivoAjuste( String pFilename )

  Shipment leerArchivoRemesa( String pFilename )

  Shipment obtieneArticuloEntrada(String clave, Integer sucursal, String pIdTipoTrans)

  List<Sucursal> listarAlmacenes( )

  List<Sucursal> listarSoloSucursales( )

  Boolean transaccionCargada( String clave )

  void generaArchivoAcuseAjuste( String folio )

}

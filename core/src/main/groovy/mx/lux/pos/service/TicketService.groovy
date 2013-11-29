package mx.lux.pos.service

import mx.lux.pos.model.*

interface TicketService {

  void imprimeVenta( String idNotaVenta )

  void imprimeVenta( String idNotaVenta, Boolean pNewOrder )

  boolean imprimeCierreTerminales( Date fechaCierre, List<ResumenDiario> resumenesDiario, Empleado empleado, String terminal )

  void imprimeResumenDiario( Date fechaCierre, Empleado empleado )

  void imprimeUbicacionListaPrecios( ListaPrecios listaPrecios, List<Articulo> articulos )

  void imprimeCargaListaPrecios( ListaPrecios listaPrecios )

  void imprimeTransInv( TransInv pTrans )

  void imprimeTransInv( TransInv pTrans, Boolean pNewTransaction )

  void imprimeCancelacion( String idNotaVenta )

  void imprimePlanCancelacion( String idNotaVenta )

  void imprimeResumenExistencias( InvOhSummary pSummary )

  void imprimeReferenciaFiscal( String idFiscal )

  void imprimeComprobanteFiscal( String idFiscal )

  void imprimeCotizacion(  Cotizacion cotizacion, CotizaDet cotizaDet,  boolean totalizar, boolean convenio, String convenioDesc )

  void imprimeAperturaCaja( Date fechaApertura )

  void imprimeCotizacion( Integer pQuoteNbr )

  void imprimeTransaccionesInventario( Date fechaTransacciones )

  void imprimeDiferencias(  )
}

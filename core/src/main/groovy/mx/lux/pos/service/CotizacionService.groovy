package mx.lux.pos.service

import mx.lux.pos.model.CotizaDet
import mx.lux.pos.model.Cotizacion

interface CotizacionService {

  Cotizacion registrarCotizacion( Cotizacion cotizacion )

  CotizaDet registrarCotizaDet( CotizaDet cotizaDet )

  BigDecimal obtenerPrecio( Integer idArticulo, String conv, BigDecimal precio )

  Integer copyFromOrder( String pOrderNbr, Integer pCustomerId, String pIdEmpleado )

  Map<String, Object> toOrder( Integer pQuoteId )

  Boolean isQuoteOpen( Integer pQuoteNbr )

}
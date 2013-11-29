package mx.lux.pos.service

import mx.lux.pos.model.Moneda
import mx.lux.pos.model.MonedaDetalle

public interface MonedaExtranjeraService {

  Moneda findCurrency( String pIdMoneda )

  MonedaDetalle findActiveRate( String pIdMoneda )

  MonedaDetalle findActiveRate( String pIdMoneda, Date pDate )

  void register( String pIdMoneda, Double pRate )

  void register( Date pEffectiveFrom, String pIdMoneda, Double pRate )

  Boolean requestUsdDisplayed( )

}
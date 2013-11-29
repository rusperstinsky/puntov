package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import mx.lux.pos.model.CierreDiario

@Bindable
@ToString
@EqualsAndHashCode
class DailyClose {
  Date date
  Date dateClosed
  Date timeClosed
  String observations
  String status
  BigDecimal netSell
  BigDecimal grossSell
  BigDecimal modifications
  BigDecimal cancelations
  BigDecimal grossIncome
  BigDecimal returns
  BigDecimal netIncome

  static DailyClose toDailyClose( CierreDiario cierreDiario ) {
    if ( cierreDiario?.fecha ) {
      DailyClose dailyClose = new DailyClose(
          date: cierreDiario.fecha,
          dateClosed: cierreDiario.fechaCierre,
          timeClosed: cierreDiario.horaCierre,
          observations: cierreDiario.observaciones,
          status: cierreDiario.estado,
          netSell: cierreDiario.ventaNeta,
          grossSell: cierreDiario.ventaBruta,
          modifications: cierreDiario.modificaciones,
          cancelations: cierreDiario.cancelaciones,
          grossIncome: cierreDiario.ingresoBruto,
          returns: cierreDiario.devoluciones,
          netIncome: cierreDiario.ingresoNeto
      )
      return dailyClose
    }
    return null
  }

  boolean isOpen( ) {
    'a'.equalsIgnoreCase( status )
  }
}

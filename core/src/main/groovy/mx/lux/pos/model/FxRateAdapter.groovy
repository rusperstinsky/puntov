package mx.lux.pos.model

import mx.lux.pos.util.CustomDateUtils
import mx.lux.pos.util.StringList

class FxRateAdapter {

  private static enum FieldOrder {
    IdCurr, StartEff, ExchRate, Fld_1
  }
  private static final String MSG_INVALID_DATA = 'Invalid Data TipoCambio(%s)'
  private static final String FMT_DATE = 'dd-MM-yyyy'
  private static final String FMT_NUMERIC = '%.4f'

  private StringList data

  private FxRateAdapter( ) { }

  // Public methods
  static FxRateAdapter parse( StringList pData ) {
    FxRateAdapter adapter
    if ( pData.getSize() >= FieldOrder.values().size() ) {
      adapter = new FxRateAdapter()
      adapter.data = pData
    } else {
      throw new IllegalArgumentException( String.format( MSG_INVALID_DATA, pData ) )
    }
    return adapter
  }

  static FxRateAdapter toAdapter( MonedaDetalle pFxRate ) {
    FxRateAdapter adapter = new FxRateAdapter()
    adapter.data = new StringList()
    for ( FieldOrder f : FieldOrder.values() ) {
      switch ( f ) {
        case FieldOrder.IdCurr:
          adapter.data.add( pFxRate.idMoneda )
          break
        case FieldOrder.StartEff:
          adapter.data.addDate( pFxRate.fechaActiva, FMT_DATE )
          break
        case FieldOrder.ExchRate:
          adapter.data.addDouble( pFxRate.tipoCambio, FMT_NUMERIC )
          break
        default:
          adapter.data.add( '' )
          break
      }
    }
    return adapter
  }

  Boolean isValid( ) {
    return true
  }

  String getIdCurrency( ) {
    return this.data.entry( FieldOrder.IdCurr.ordinal() ).toUpperCase()
  }

  Date getStartEff( ) {
    return this.data.asDate( FieldOrder.StartEff.ordinal(), FMT_DATE )
  }

  Double getRate( ) {
    return this.data.asDouble( FieldOrder.ExchRate.ordinal() )
  }

  String toExportString( ) {
    return this.data.toString( '|' )
  }

  String toString( ) {
    return String.format( '%s(%s); %.4f', this.getIdCurrency(),
        CustomDateUtils.format( this.getStartEff(), FMT_DATE ), this.getRate() )
  }

}

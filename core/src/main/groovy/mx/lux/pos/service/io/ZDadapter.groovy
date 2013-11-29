package mx.lux.pos.service.io

import mx.lux.pos.model.DetalleNotaVenta
import mx.lux.pos.service.business.Registry
import mx.lux.pos.util.StringList
import org.apache.commons.lang3.math.NumberUtils

import java.text.ParseException

class ZDadapter {

  private enum Field {
    Linea, IdFactura, Sku, Cantidad, PrLista, PrVta, Part, Surte, Color, DesctoMonto
  }

  private static Integer currentSite
  private StringList record

  ZDadapter( String pRecord ) {
    this.record = new StringList( pRecord, "|" )
  }

  // Internal Methods
  protected Integer asInteger( Field pField ) {
    Integer value = null
    if ( this.record.size >= Field.values().size() ) {
      try {
        value = record.asInteger( pField.ordinal() )
      } catch ( ParseException e ) { }
    }
    return value
  }

  protected Date asDate( Field pField ) {
    Date value = null
    if ( this.record.size >= Field.values().size() ) {
      try {
        value = record.asDate( pField.ordinal(), "yyyy-MM-dd" )
      } catch ( ParseException e ) { }
    }
    return value
  }

  protected Double asDouble( Field pField ) {
    Double value = null
    if ( this.record.size >= Field.values().size() ) {
      try {
        value = record.asDouble( pField.ordinal() )
      } catch ( ParseException e ) { }
    }
    return value
  }

  protected String asString( Field pField ) {
    String value = null
    if ( this.record.size >= Field.values().size() ) {
      value = record.entry( pField.ordinal() )
    }
    return value
  }

  protected static Integer getCurrentSite( ) {
    if ( currentSite == null ) {
      currentSite = Registry.currentSite
    }
    return currentSite
  }

  // Public Methods
  DetalleNotaVenta createOrderLine( Date pFecha ) {
    DetalleNotaVenta line = null
    if ( this.idFactura.length() > 0 ) {
      line = new DetalleNotaVenta()
      line.id = this.idFactura
      line.idArticulo = this.sku
      line.idTipoDetalle = "N"
      line.cantidadFac = this.cantidad as Double
      line.precioUnitLista = NumberUtils.createBigDecimal( String.format( "%.2f", this.precioLista ) )
      line.precioUnitFinal = NumberUtils.createBigDecimal( String.format( "%.2f", this.precioVenta ) )
      line.precioCalcLista = NumberUtils.createBigDecimal( String.format( "%.2f", this.precioLista ) )
      line.precioCalcOferta = BigDecimal.ZERO
      line.precioFactura = NumberUtils.createBigDecimal( String.format( "%.2f", this.precioVenta ) )
      line.precioConv = BigDecimal.ZERO
      line.idSync = "1"
      line.idMod = ""
      line.surte = ""
      line.idSucursal = currentSite
      line.fechaMod = pFecha
    }
    return line
  }

  Integer getLinea( ) {
    return asInteger( Field.Linea )
  }

  String getId( ) {
    return String.format( "%s:%08d", this.idFactura, this.sku )
  }

  String getIdFactura( ) {
    return asString( Field.IdFactura )
  }

  Integer getSku( ) {
    return asInteger( Field.Sku )
  }

  Integer getCantidad( ) {
    return Math.round( asDouble( Field.Cantidad ) ) as Integer
  }

  Double getPrecioLista( ) {
    return asDouble( Field.PrLista )
  }

  Double getPrecioVenta( ) {
    return asDouble( Field.PrVta )
  }

  String getPartNbr( ) {
    return asString( Field.Part )
  }

  String getColor( ) {
    return asString( Field.Color )
  }

  Double getDesctoMonto( ) {
    return asDouble( Field.DesctoMonto )
  }

  String toString( ) {
    return String.format( "[ZD] IdFactura:%s  SKU:%d  PrecioLista:%,.2f  PrecioVenta:%,.2f  DesctoMonto:%,.2f",
        this.idFactura, this.sku, this.precioLista, this.precioVenta, this.desctoMonto )
  }

}

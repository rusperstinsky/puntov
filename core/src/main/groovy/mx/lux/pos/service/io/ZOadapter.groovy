package mx.lux.pos.service.io

import mx.lux.pos.model.NotaVenta
import mx.lux.pos.service.business.Registry
import mx.lux.pos.util.CustomDateUtils
import mx.lux.pos.util.StringList
import org.apache.commons.lang3.math.NumberUtils

import java.text.ParseException

class ZOadapter {

  private enum Field {
    Linea, Ticket, IdFactura, IdEmpleado, Convenio, IdCliente, SFactura, Monto, Udf4, Referido, Descto, Clave,
    TipoCliente, FechaEntrega, HoraEntrega
  }

  private static Integer currentSite
  private Date fecha

  ZOadapter( Date pFecha, String pRecord ) {
    this.fecha = pFecha
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

  // public methods
  NotaVenta createOrder( String pRemarks ) {
    NotaVenta order = null
    if ( this.idFactura.length() > 0 ) {
      order = new NotaVenta()
      order.id = this.idFactura
      order.idEmpleado = this.idEmpleado
      order.idCliente = this.idCliente
      order.idConvenio = ""
      order.tipoNotaVenta = "F"
      order.tipoCli = "N"
      order.fExpideFactura = true
      order.ventaTotal = NumberUtils.createBigDecimal( String.format( "%.2f", this.totalAmount ) )
      order.ventaNeta = NumberUtils.createBigDecimal( String.format( "%.2f", this.totalAmount ) )
      order.sumaPagos = NumberUtils.createBigDecimal( String.format( "%.2f", this.totalAmount ) )
      order.fechaHoraFactura = this.fechaFactura
      order.fechaPrometida = this.fechaFactura
      order.fechaEntrega = this.fechaFactura
      order.fArmazonCli = false
      order.por100Descuento = 0
      order.montoDescuento = NumberUtils.createBigDecimal( String.format( "%.2f", this.discount ) )
      order.tipoDescuento = "N"
      order.fResumenNotasMo = false
      order.sFactura = "N"
      order.tipoEntrega = "S"
      order.observacionesNv = pRemarks
      order.idSync = "1"
      order.idSucursal = currentSite
      order.descuento = false
      order.polEnt = true
      order.tipoVenta = "N"
      order.poliza = BigDecimal.ZERO
    }
    return order
  }

  Integer getLinea( ) {
    return asInteger( Field.Linea )
  }

  String getIdFactura( ) {
    return asString( Field.IdFactura )
  }

  Date getFechaFactura( ) {
    return this.fecha
  }

  String getIdEmpleado( ) {
    return asString( Field.IdEmpleado )
  }

  Integer getIdCliente( ) {
    Integer id = asInteger( Field.IdCliente )
    if ( id == null ) {
      id = 1
    }
    return id
  }

  Double getTotalAmount( ) {
    return asDouble( Field.Monto )
  }

  String getSFactura( ) {
    return asString( Field.SFactura )
  }

  String getUdf4( ) {
    return asString( Field.Udf4 )
  }

  String getReferido( ) {
    return asString( Field.Referido )
  }

  Double getDiscount( ) {
    return asDouble( Field.Descto )
  }

  String getPromotion( ) {
    return asString( Field.Clave )
  }

  String getTipoCliente( ) {
    return asString( Field.TipoCliente )
  }

  String toString( ) {
    return String.format( "[ZO] IdFactura:%s  Fecha:%s  Cliente:%d  Monto:%,.2f",
        this.getIdFactura(), CustomDateUtils.format( this.getFechaFactura() ), this.getIdCliente(),
        this.getTotalAmount() )
  }
}

package mx.lux.pos.service.io

import mx.lux.pos.model.Pago
import mx.lux.pos.util.StringList
import org.apache.commons.lang3.math.NumberUtils

import java.text.ParseException

class ZPadapter {
  private enum Field {
    Linea, IdFactura, Tipo, idTipo, Monto, TransfFact, IdDoc, Recibo, Parcial, F1, F2, F3, F4, F5, NumPago
  }

  private StringList record

  ZPadapter( String pRecord ) {
    this.record = new StringList( pRecord, "|" )
  }

  // Private Methods
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

  // Public Methods
  Pago createPayment( Date pFecha, String pEmpleado ) {
    Pago payment = null
    if ( this.idFactura.length() ) {
      payment = new Pago()
      payment.idFactura = this.idFactura
      payment.idBanco = ""
      payment.idFormaPago = this.tipo
      payment.tipoPago = "A"
      payment.referenciaPago = ""
      payment.monto = NumberUtils.createBigDecimal( String.format( "%.2f", this.monto ) )
      payment.fecha = pFecha
      payment.idEmpleado = pEmpleado
      payment.idSync = "1"
      payment.fechaModificacion = pFecha
      payment.idMod = "0"
      payment.idSucursal = currentSucursal
      payment.idRecibo = ""
      payment.parcialidad = ""
      payment.idFPago = payment.idFormaPago
      payment.clave = this.f1
      payment.referenciaClave = this.f2
      payment.idBancoEmisor = this.f3
      payment.idTerminal = this.f4
      payment.idPlan = this.f5
      payment.confirmado = true
      payment.porDevolver = BigDecimal.ZERO
    }
    return pEmpleado
  }

  String getId( ) {
    return String.format( "%s:%04d", this.idFactura, this.linea )
  }

  Integer getLinea( ) {
    return asInteger( Field.Linea )
  }

  String getIdFactura( ) {
    return asString( Field.IdFactura )
  }

  String getTipo( ) {
    return asString( Field.Tipo )
  }

  String getIdTipo( ) {
    return asString( Field.idTipo )
  }

  Double getMonto( ) {
    return asDouble( Field.Monto )
  }

  String getTransfFactura( ) {
    return asString( Field.TransfFact )
  }

  String getIdDoc( ) {
    return asString( Field.IdDoc )
  }

  String getRecibo( ) {
    return asString( Field.Recibo )
  }

  String getParcial( ) {
    return asString( Field.Parcial )
  }

  String getF1( ) {
    return asString( Field.F1 )
  }

  String getF2( ) {
    return asString( Field.F2 )
  }

  String getF3( ) {
    return asString( Field.F3 )
  }

  String getF4( ) {
    return asString( Field.F4 )
  }

  String getF5( ) {
    return asString( Field.F5 )
  }

  String getNumPago( ) {
    return asString( Field.NumPago )
  }

  String toString( ) {
    return String.format( "[ZP] IdFactura:%s  Tipo:%s  Monto:%,.2f   F1:%s  F2:%s",
        this.idFactura, this.tipo, this.monto, this.f1, this.f2 )
  }

}

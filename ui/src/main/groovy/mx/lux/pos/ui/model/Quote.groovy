package mx.lux.pos.ui.model

import groovy.beans.Bindable
import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode
import mx.lux.pos.model.Comprobante
import mx.lux.pos.model.Cotizacion
import mx.lux.pos.model.CotizaDet

@Bindable
@ToString
@EqualsAndHashCode
class Quote {

    Integer id
    String titulo
    String cliente
    String telefono
    String observaciones
    String convenio
    String articulo
    String color
    String precio
    String articulo2
    String color2
    BigDecimal precio2
    String articulo3
    String color3
    BigDecimal precio3
    String articulo4
    String color4
    BigDecimal precio4


    static Quote toQuote( Cotizacion cotizacion ) {
        if ( cotizacion?.id ) {
            Quote quote = new Quote(
                    titulo: cotizacion.titulo,
                    cliente: cotizacion.nombre,
                    telefono: cotizacion.tel,
                    observaciones: cotizacion.observaciones,
                    convenio: cotizacion.udf1
            )
            return quote
        }
        return null
    }



    static Quote toQuote( CotizaDet cotizaDet ) {
        if ( cotizaDet?.id ) {
            Cotizacion cotizacion = cotizaDet.id
            Quote quote = new Quote(
                    articulo: cotizaDet.articulo,
                    color: cotizaDet.color,
                    precio: cotizaDet.precioUnit,
                    articulo2: cotizaDet.articulo,
                    color2: cotizaDet.color,
                    precio2: cotizaDet.precioUnit,
                    articulo3: cotizaDet.articulo,
                    color3: cotizaDet.color,
                    precio3: cotizaDet.precioUnit,
                    articulo4: cotizaDet.articulo,
                    color4: cotizaDet.color,
                    precio4: cotizaDet.precioUnit
            )
            return quote
        }
        return null
    }


}

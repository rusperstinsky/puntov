package mx.lux.pos.service.io

import mx.lux.pos.model.ArticuloSunglassAdapter
import mx.lux.pos.model.ArticuloSunglassDescriptor
import mx.lux.pos.util.StringList
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils

import java.text.ParseException
import mx.lux.pos.model.Promocion
import mx.lux.pos.model.NotaVenta
import mx.lux.pos.service.business.Registry
import mx.lux.pos.util.CustomDateUtils

class PromotionsAdapter {

    private enum Field{
        Field_01, IdPromocion, TipoPromocion, Descripcion, VigenciaIni, VigenciaFin, Prioridad, IdGrupoTienda, ArticuloProm,
        AplicaConv, AplicaAuto, Obligatoria, PrecioOferta, IdGenerico, Tipo, Subtipo, Articulo, Marca, PrecioDescontado,
        Descuento, Genericoc, Tipoc, Subtipoc, Articuloc, PrecioDescontadoc, Descuentoc, TipoPrecio,   TipoPrecioc,
        Marcac
    }

    private static Integer currentSite
    private StringList record

    PromotionsAdapter( String pRecord ) {
        this.record = new StringList( pRecord, "|" )
    }

    // Internal Methods
    protected Integer asInteger( Field pField ) {
        Integer value  = null
        if (this.record.size >= Field.values().size() ){
            try {
                value = record.asInteger( pField.ordinal() )
            } catch (ParseException e) { }
        }
        return value
    }

    protected boolean asBoolean( Field pField ) {
        boolean value  = null
        if (this.record.size >= Field.values().size() ){
            try {
                value = record.isEquals( pField.ordinal(), "1")
            } catch (ParseException e) { }
        }
        return value
    }

    protected Date asDate( Field pField ) {
        Date value = null
        if (this.record.size >= Field.values().size() ){
            try {
                value = record.asDate( pField.ordinal(), "dd/MM/yy" )
            } catch (ParseException e) { }
        }
        return value
    }

    protected Double asDouble( Field pField ) {
        Double value  = null
        if (this.record.size >= Field.values().size() ){
            try {
                value = record.asDouble( pField.ordinal() )
            } catch (ParseException e) { }
        }
        return value
    }

    protected String asString( Field pField ) {
        String value  = null
        if (this.record.size >= Field.values().size() ){
            value = record.entry( pField.ordinal() )
        }
        return value
    }

    protected static Integer getCurrentSite() {
        if ( currentSite == null ) {
            currentSite = Registry.currentSite
        }
        return currentSite
    }

    // public methods
    void assignInto( Promocion p  ) {
        if ( this.idPromocion != null ) {
            p.idPromocion = this.idPromocion
            p.descripcion = this.descripcion
            p.articuloProm = this.articuloProm
            p.aplicaConvenio = this.aplicaConv
            p.prioridad = this.prioridad
            p.precioOferta = this.precioOferta
            p.vigenciaIni = this.vigenciaIni
            p.vigenciaFin = this.vigenciaFin
            p.idGrupoTienda = this.idGrupoTienda
            p.obligatoria = this.obligatoria
            p.aplicaAuto = this.aplicaAuto
            p.tipoPromocion = this.tipoPromocion
            p.idGenerico = this.idGenerico
            p.tipo = this.tipo
            p.subtipo = this.subtipo
            p.marca = this.marca
            p.articulo = this.articulo
            p.tipoPrecio = this.tipoPrecio
            p.precioDescontado = NumberUtils.createBigDecimal(String.format("%.2f", this.precioDescontado) )
            p.descuento = NumberUtils.createBigDecimal(String.format("%.2f", this.descuento) )
            p.idGenericoC = this.genericoc
            p.tipoC = this.tipoc
            p.subtipoC = this.subtipoc
            p.marcaC = this.marcac
            p.articuloC = this.articuloc
            p.tipoPrecioC = this.tipoPrecioc
            p.precioDescontadoC = NumberUtils.createBigDecimal(String.format("%.2f", this.precioDescontadoc) )
            p.descuentoC = NumberUtils.createBigDecimal(String.format("%.2f", this.descuentoc) )
        }
    }

    Integer getIdPromocion() {
        return asInteger( Field.IdPromocion )
    }

    String getDescripcion( ) {
        return asString( Field.Descripcion )
    }

    String getArticuloProm( ) {
        return asString( Field.ArticuloProm )
    }

    boolean getAplicaConv( ) {
        return asBoolean( Field.AplicaConv )
    }

    Integer getPrioridad() {
        return asInteger( Field.Prioridad )
    }

    boolean getPrecioOferta( ) {
        return asBoolean( Field.PrecioOferta )
    }

    Date getVigenciaIni( ) {
        return asDate( Field.VigenciaIni )
    }

    Date getVigenciaFin( ) {
        return asDate( Field.VigenciaFin )
    }

    String getIdGrupoTienda( ) {
        return asString( Field.IdGrupoTienda )
    }

    boolean getAplicaAuto( ) {
        return asBoolean( Field.AplicaAuto )
    }

    boolean getObligatoria( ) {
        return asBoolean( Field.Obligatoria )
    }

    String getTipoPromocion( ) {
        return asString( Field.TipoPromocion )
    }

    String getIdGenerico( ) {
        return asString( Field.IdGenerico )
    }

    String getTipo( ) {
        return asString( Field.Tipo )
    }

    String getSubtipo( ) {
        return asString( Field.Subtipo )
    }

    String getMarca( ) {
        return asString( Field.Marca )
    }

    String getArticulo( ) {
        return asString( Field.Articulo )
    }

    String getTipoPrecio( ) {
        return asString( Field.TipoPrecio )
    }

    Double getPrecioDescontado( ) {
        return asDouble( Field.PrecioDescontado )
    }

    Double getDescuento( ) {
        return asDouble( Field.Descuento )
    }

    String getGenericoc( ) {
        return asString( Field.Genericoc )
    }

    String getTipoc( ) {
        return asString( Field.Tipoc )
    }

    String getSubtipoc( ) {
        return asString( Field.Subtipoc )
    }

    String getMarcac( ) {
        return asString( Field.Marcac )
    }

    String getArticuloc( ) {
        return asString( Field.Articuloc )
    }

    String getTipoPrecioc( ) {
        return asString( Field.TipoPrecioc )
    }

    Double getPrecioDescontadoc( ) {
        return asDouble( Field.PrecioDescontadoc )
    }

    Double getDescuentoc( ) {
        return asDouble( Field.Descuentoc )
    }

    String toString() {
        String str = new String().format( "[%04d] %s VigenciaIni:%s  Generico:%s Tipo:%s.%s, Marca:%s, Articulo:%s ",
                this.idPromocion, this.descripcion, CustomDateUtils.format(this.vigenciaIni), this.idGenerico, this.tipo,
                this.subtipo, this.marca, this.articulo
        )
        return str
    }
}

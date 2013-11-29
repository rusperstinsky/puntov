package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "promocion", schema = "public" )
public class Promocion implements Serializable, Comparable<Promocion> {

    private static final long serialVersionUID = -143478516570527154L;

    @Id
    @Column( name = "id_promocion" )
    private Integer idPromocion;

    @Column( name = "descripcion" )
    private String descripcion;

    @Column( name = "articulo_prom" )
    private String articuloProm;

    @Column( name = "aplica_conv" )
    private Boolean aplicaConvenio;

    @Column( name = "prioridad" )
    private Integer prioridad;

    @Column( name = "precio_oferta" )
    private Boolean precioOferta;

    @Column( name = "vigencia_ini" )
    private Date vigenciaIni;

    @Column( name = "vigencia_fin" )
    private Date vigenciaFin;

    @Column( name = "id_grupo_tienda" )
    private String idGrupoTienda;

    @Column( name = "aplica_auto" )
    private Boolean aplicaAuto;

    @Column( name = "obligatoria" )
    private Boolean obligatoria;

    @Column( name = "tipo_promocion" )
    private String tipoPromocion;

    @Column( name = "id_generico" )
    private String idGenerico;

    @Column( name = "tipo" )
    private String tipo;

    @Column( name = "subtipo" )
    private String subtipo;

    @Column( name = "marca" )
    private String marca;

    @Column( name = "articulo" )
    private String articulo;

    @Column( name = "tipo_precio" )
    private String tipoPrecio;

    @Column( name = "precio_descontado" )
    private BigDecimal precioDescontado;

    @Column( name = "descuento" )
    private BigDecimal descuento;

    @Column( name = "genericoc" )
    private String idGenericoC;

    @Column( name = "tipoc" )
    private String tipoC;

    @Column( name = "subtipoc" )
    private String subtipoC;

    @Column( name = "marcac" )
    private String marcaC;

    @Column( name = "articuloc" )
    private String articuloC;

    @Column( name = "tipo_precioc" )
    private String tipoPrecioC;

    @Column( name = "precio_descontadoc" )
    private BigDecimal precioDescontadoC;

    @Column( name = "descuentoc" )
    private BigDecimal descuentoC;

    // Internal Methods
    @PostLoad
    protected void onPostLoad() {
        descripcion = StringUtils.trimToEmpty( descripcion );
        articuloProm = StringUtils.trimToEmpty( articuloProm ).toUpperCase();
        idGrupoTienda = StringUtils.trimToEmpty( idGrupoTienda ).toUpperCase();
        tipoPromocion = StringUtils.trimToEmpty( tipoPromocion ).toUpperCase();
        idGenerico = StringUtils.trimToEmpty( idGenerico ).toUpperCase();
        tipo = StringUtils.trimToEmpty( tipo ).toUpperCase();
        subtipo = StringUtils.trimToEmpty( subtipo ).toUpperCase();
        marca = StringUtils.trimToEmpty( marca ).toUpperCase();
        tipoPrecio = StringUtils.trimToEmpty( tipoPrecio ).toUpperCase();
        articulo = StringUtils.trimToEmpty( articulo ).toUpperCase();
        idGenericoC = StringUtils.trimToEmpty( idGenericoC ).toUpperCase();
        tipoC = StringUtils.trimToEmpty( tipoC ).toUpperCase();
        subtipoC = StringUtils.trimToEmpty( subtipoC ).toUpperCase();
        marcaC = StringUtils.trimToEmpty( marcaC ).toUpperCase();
        articuloC = StringUtils.trimToEmpty( articuloC ).toUpperCase();
        tipoPrecioC = StringUtils.trimToEmpty( tipoPrecioC ).toUpperCase();
    }

    // Public Methods
    public Integer getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion( Integer pIdPromocion ) {
        this.idPromocion = pIdPromocion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion( String pDescripcion ) {
        descripcion = StringUtils.trimToEmpty( pDescripcion );
    }

    public String getArticuloProm() {
        return articuloProm;
    }

    public void setArticuloProm( String pArticuloProm ) {
        articuloProm = StringUtils.trimToEmpty( pArticuloProm ).toUpperCase();
    }

    public Boolean getAplicaConvenio() {
        return aplicaConvenio;
    }

    public void setAplicaConvenio( Boolean pAplicaConvenio ) {
        aplicaConvenio = pAplicaConvenio;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad( Integer pPrioridad ) {
        prioridad = pPrioridad;
    }

    public Boolean getPrecioOferta() {
        return precioOferta;
    }

    public void setPrecioOferta( Boolean pPrecioOferta ) {
        precioOferta = pPrecioOferta;
    }

    public Date getVigenciaIni() {
        return vigenciaIni;
    }

    public void setVigenciaIni( Date pVigenciaIni ) {
        vigenciaIni = DateUtils.truncate( pVigenciaIni, Calendar.DATE );
    }

    public Date getVigenciaFin() {
        return vigenciaFin;
    }

    public void setVigenciaFin( Date pVigenciaFin ) {
        vigenciaFin = DateUtils.truncate( pVigenciaFin, Calendar.DATE );
    }

    public String getIdGrupoTienda() {
        return idGrupoTienda;
    }

    public void setIdGrupoTienda( String pIdGrupoTienda ) {
        idGrupoTienda = StringUtils.trimToEmpty( pIdGrupoTienda ).toUpperCase();
    }

    public Boolean getAplicaAuto() {
        return aplicaAuto;
    }

    public void setAplicaAuto( Boolean pAplicaAuto ) {
        aplicaAuto = pAplicaAuto;
    }

    public Boolean getObligatoria() {
        return obligatoria;
    }

    public void setObligatoria( Boolean pObligatoria ) {
        obligatoria = pObligatoria;
    }

    public String getTipoPromocion() {
        return tipoPromocion;
    }

    public void setTipoPromocion( String pTipoPromocion ) {
        tipoPromocion = StringUtils.trimToEmpty( pTipoPromocion ).toUpperCase();
    }

    public String getIdGenerico() {
        return idGenerico;
    }

    public void setIdGenerico( String pIdGenerico ) {
        idGenerico = StringUtils.trimToEmpty( pIdGenerico ).toUpperCase();
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo( String pTipo ) {
        tipo = StringUtils.trimToEmpty( pTipo ).toUpperCase();
    }

    public String getSubtipo() {
        return subtipo;
    }

    public void setSubtipo( String pSubtipo ) {
        subtipo = StringUtils.trimToEmpty( pSubtipo ).toUpperCase();
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca( String pMarca ) {
        marca = StringUtils.trimToEmpty( pMarca ).toUpperCase();
    }

    public String getTipoPrecio() {
        return tipoPrecio;
    }

    public void setTipoPrecio( String pTipoPrecio ) {
        tipoPrecio = StringUtils.trimToEmpty( pTipoPrecio ).toUpperCase();
    }

    public BigDecimal getPrecioDescontado() {
        return precioDescontado;
    }

    public void setPrecioDescontado( BigDecimal pPrecioDescontado ) {
        precioDescontado = pPrecioDescontado;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo( String pArticulo ) {
        articulo = StringUtils.trimToEmpty( pArticulo ).toUpperCase();
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento( BigDecimal pDescuento ) {
        descuento = pDescuento;
    }

    public String getIdGenericoC() {
        return idGenericoC;
    }

    public void setIdGenericoC( String pIdGenericoC ) {
        idGenericoC = StringUtils.trimToEmpty( pIdGenericoC ).toUpperCase();
    }

    public String getTipoC() {
        return tipoC;
    }

    public void setTipoC( String pTipoC ) {
        tipoC = StringUtils.trimToEmpty( pTipoC ).toUpperCase();
    }

    public String getSubtipoC() {
        return subtipoC;
    }

    public void setSubtipoC( String pSubtipoC ) {
        subtipoC = StringUtils.trimToEmpty( pSubtipoC ).toUpperCase();
    }

    public String getMarcaC() {
        return marcaC;
    }

    public void setMarcaC( String pMarcaC ) {
        marcaC = StringUtils.trimToEmpty( pMarcaC ).toUpperCase();
    }

    public String getArticuloC() {
        return articuloC;
    }

    public void setArticuloC( String pArticuloC ) {
        articuloC = StringUtils.trimToEmpty( pArticuloC ).toUpperCase();
    }

    public String getTipoPrecioC() {
        return tipoPrecioC;
    }

    public void setTipoPrecioC( String pTipoPrecioC ) {
        tipoPrecioC = StringUtils.trimToEmpty( pTipoPrecioC ).toUpperCase();
    }

    public BigDecimal getPrecioDescontadoC() {
        return precioDescontadoC;
    }

    public void setPrecioDescontadoC( BigDecimal pPrecioDescontadoC ) {
        precioDescontadoC = pPrecioDescontadoC;
    }

    public BigDecimal getDescuentoC() {
        return descuentoC;
    }

    public void setDescuentoC( BigDecimal pDescuentoC ) {
        descuentoC = pDescuentoC;
    }

    // Entity
    public int compareTo( Promocion pPromocion ) {
        int result = this.getPrioridad().compareTo( pPromocion.getPrioridad() );
        if ( result == 0 ) result = this.getDescripcion().compareToIgnoreCase( pPromocion.getDescripcion() );
        return result;
    }

    public boolean equals( Object pObj ) {
        boolean result = false;
        if ( pObj instanceof Promocion ) {
            result = this.getIdPromocion().equals( ( ( Promocion ) pObj ).getIdPromocion() );
        } else if ( pObj instanceof Integer ) {
            result = this.getIdPromocion().equals( ( Integer ) pObj );
        }
        return result;
    }

    public int hashCode() {
        return this.getIdPromocion().hashCode();
    }

    public String toString() {
        final DateFormat df = new SimpleDateFormat( "dd/MMM/yyyy" );
        String str = String.format( "[%d] %s  Vigencia:%s-%s  Pri:%d", this.getIdPromocion(), this.getDescripcion(),
                df.format( this.getVigenciaIni() ), df.format( this.getVigenciaFin() ), this.getPrioridad() );
        return str;
    }
}

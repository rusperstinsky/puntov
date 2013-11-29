package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "articulos", schema = "public" )
public class Articulo implements Serializable {

    private static final long serialVersionUID = -2921740576181746900L;

    @Id
    @Column( name = "id_articulo" )
    private Integer id;

    @Column( name = "articulo", length = 20, nullable = false )
    private String articulo;

    @Column( name = "color_code", length = 12 )
    private String codigoColor;

    @Column( name = "desc_articulo" )
    private String descripcion;

    @Column( name = "id_generico", length = 1 )
    private String idGenerico;

    @Column( name = "id_gen_tipo", length = 2 )
    private String idGenTipo;

    @Column( name = "id_gen_subtipo", length = 2 )
    private String idGenSubtipo;

    @Column( name = "tipo" )
    private String tipo;

    @Column( name = "subtipo" )
    private String subtipo;

    @Column( name = "marca" )
    private String marca;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "precio" )
    private BigDecimal precio;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "precio_o" )
    private BigDecimal precioO;

    @Column( name = "s_articulo", length = 1 )
    private String sArticulo = "V";

    @Column( name = "id_sync", length = 1, nullable = false )
    private String idSync = "1";

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod", nullable = false )
    private Date fechaMod;

    @Column( name = "id_mod", length = 13, nullable = false )
    private String idMod = "0";

    @Column( name = "id_sucursal", nullable = false )
    private Integer idSucursal;

    @Column( name = "color_desc" )
    private String descripcionColor;

    @Column( name = "id_cb" )
    private String idCb;

    @Column( name = "id_diseno_lente", length = 1 )
    private String idDisenoLente;

    @Column( name = "proveedor", length = 1 )
    private String proveedor;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_generico", insertable = false, updatable = false )
    private Generico generico;

    @OneToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_articulo", referencedColumnName = "articulo", insertable = false, updatable = false )
    private Precio precios;

    @OneToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_articulo", insertable = false, updatable = false )
    private Existencia existencia;

    @Column( name = "existencia" )
    private Integer cantExistencia;

    @Transient
    private String operacion;

    @Transient
    private String tipoPrecio;

    @Transient
    private String ubicacion;

    @PrePersist
    private void onPrePersist() {
        fechaMod = new Date();
    }

    @PreUpdate
    private void onPreUpdate() {
        fechaMod = new Date();
    }

    @PostLoad
    private void onPostLoad() {
        articulo = StringUtils.trimToEmpty( articulo );
        codigoColor = StringUtils.trimToEmpty( codigoColor );
        descripcion = StringUtils.trimToEmpty( descripcion );
        idGenerico = StringUtils.trimToEmpty( idGenerico );
        idGenTipo = StringUtils.trimToEmpty( idGenTipo );
        idGenSubtipo = StringUtils.trimToEmpty( idGenSubtipo );
        tipo = StringUtils.trimToEmpty( tipo );
        subtipo = StringUtils.trimToEmpty( subtipo );
        marca = StringUtils.trimToEmpty( marca );
        sArticulo = StringUtils.trimToEmpty( sArticulo );
        idSync = StringUtils.trimToEmpty( idSync );
        idMod = StringUtils.trimToEmpty( idMod );
        descripcionColor = StringUtils.trimToEmpty( descripcionColor );
        idCb = StringUtils.trimToEmpty( idCb );
        idDisenoLente = StringUtils.trimToEmpty( idDisenoLente );
        cantExistencia = cantExistencia != null ? cantExistencia : 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo( String articulo ) {
        this.articulo = articulo;
    }

    public String getCodigoColor() {
        return codigoColor;
    }

    public void setCodigoColor( String codigoColor ) {
        this.codigoColor = codigoColor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion( String descripcion ) {
        this.descripcion = descripcion;
    }

    public String getIdGenerico() {
        return idGenerico;
    }

    public void setIdGenerico( String idGenerico ) {
        this.idGenerico = idGenerico;
    }

    public String getIdGenTipo() {
        return idGenTipo;
    }

    public void setIdGenTipo( String idGenTipo ) {
        this.idGenTipo = idGenTipo;
    }

    public String getIdGenSubtipo() {
        return idGenSubtipo;
    }

    public void setIdGenSubtipo( String idGenSubtipo ) {
        this.idGenSubtipo = idGenSubtipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo( String pTipo ) {
        this.tipo = pTipo;
    }

    public String getSubtipo() {
        return subtipo;
    }

    public void setSubtipo( String pSubtipo ) {
        this.subtipo = pSubtipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca( String pMarca ) {
        this.marca = pMarca;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio( BigDecimal precio ) {
        this.precio = precio;
    }

    public BigDecimal getPrecioO() {
        return precioO;
    }

    public void setPrecioO( BigDecimal precioO ) {
        this.precioO = precioO;
    }

    public String getsArticulo() {
        return sArticulo;
    }

    public void setsArticulo( String sArticulo ) {
        this.sArticulo = sArticulo;
    }

    public String getIdSync() {
        return idSync;
    }

    public void setIdSync( String idSync ) {
        this.idSync = idSync;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod( Date fechaMod ) {
        this.fechaMod = fechaMod;
    }

    public String getIdMod() {
        return idMod;
    }

    public void setIdMod( String idMod ) {
        this.idMod = idMod;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal( Integer idSucursal ) {
        this.idSucursal = idSucursal;
    }

    public String getDescripcionColor() {
        return descripcionColor;
    }

    public void setDescripcionColor( String descripcionColor ) {
        this.descripcionColor = descripcionColor;
    }

    public String getIdCb() {
        return idCb;
    }

    public void setIdCb( String idCb ) {
        this.idCb = idCb;
    }

    public String getIdDisenoLente() {
        return idDisenoLente;
    }

    public void setIdDisenoLente( String idDisenoLente ) {
        this.idDisenoLente = idDisenoLente;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor( String proveedor ) {
        this.proveedor = StringUtils.trimToEmpty( proveedor ).toUpperCase();
    }

    public Generico getGenerico() {
        return generico;
    }

    public void setGenerico( Generico generico ) {
        this.generico = generico;
    }

    public Existencia getExistencia() {
        return existencia;
    }

    public void setExistencia( Existencia existencia ) {
        this.existencia = existencia;
    }

    public Integer getCantExistencia() {
        return cantExistencia;
    }

    public void setCantExistencia( Integer existencia ) {
        this.cantExistencia = existencia;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion( String operacion ) {
        this.operacion = operacion;
    }

    public String getTipoPrecio() {
        return tipoPrecio;
    }

    public void setTipoPrecio( String tipoPrecio ) {
        this.tipoPrecio = tipoPrecio;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion( String ubicacion ) {
        this.ubicacion = ubicacion;
    }

    public Precio getPrecios() {
        return precios;
    }

    public void setPrecios(Precio precios) {
        this.precios = precios;
    }
}

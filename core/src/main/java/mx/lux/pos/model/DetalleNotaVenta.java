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
@Table( name = "detalle_nota_ven", schema = "public" )
public class DetalleNotaVenta implements Serializable {

    private static final long serialVersionUID = -748894414171291739L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "detalle_nota_ven_id_seq" )
    @SequenceGenerator( name = "detalle_nota_ven_id_seq", sequenceName = "detalle_nota_ven_id_seq" )
    @Column( name = "id" )
    private Integer id;

    @Column( name = "id_factura", nullable = false )
    private String idFactura;

    @Column( name = "id_articulo", nullable = false )
    private Integer idArticulo;

    @Column( name = "id_tipo_detalle", nullable = false )
    private String idTipoDetalle;

    @Column( name = "cantidad_fac" )
    private Double cantidadFac;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "precio_unit_lista" )
    private BigDecimal precioUnitLista;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "precio_unit_final" )
    private BigDecimal precioUnitFinal;

    @Column( name = "id_sync", length = 1, nullable = false )
    private String idSync = "1";

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod", nullable = false )
    private Date fechaMod;

    @Column( name = "id_mod", length = 13, nullable = false )
    private String idMod = "0";

    @Column( name = "id_sucursal", nullable = false )
    private Integer idSucursal;

    @Column( name = "surte", length = 1 )
    private String surte;

    @Column( name = "id_rep_venta" )
    private String idRepVenta;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "precio_calc_lista", nullable = false )
    private BigDecimal precioCalcLista;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "precio_calc_oferta", nullable = false )
    private BigDecimal precioCalcOferta;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "precio_factura" )
    private BigDecimal precioFactura;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "precio_conv" )
    private BigDecimal precioConv;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_tipo_detalle", insertable = false, updatable = false )
    private TipoDetalle tipoDetalle;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_articulo", insertable = false, updatable = false )
    private Articulo articulo;
    
    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_factura", insertable = false, updatable = false )
    private NotaVenta notaVenta;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_sucursal", insertable = false, updatable = false )
    private Sucursal sucursal;

    @PrePersist
    protected void onPrePersist() {
        fechaMod = new Date();
        tipoDetalle = null;
        articulo = null;
        sucursal = null;
    }

    @PreUpdate
    protected void onPreUpdate() {
        fechaMod = new Date();
    }

    @PostLoad
    protected void onPostLoad() {
        idFactura = StringUtils.trimToNull( idFactura );
        idTipoDetalle = StringUtils.trimToNull( idTipoDetalle );
        idSync = StringUtils.trimToNull( idSync );
        idMod = StringUtils.trimToNull( idMod );
        surte = StringUtils.trimToNull( surte );
        idRepVenta = StringUtils.trimToNull( idRepVenta );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura( String idFactura ) {
        this.idFactura = idFactura;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo( Integer idArticulo ) {
        this.idArticulo = idArticulo;
    }

    public String getIdTipoDetalle() {
        return idTipoDetalle;
    }

    public void setIdTipoDetalle( String idTipoDetalle ) {
        this.idTipoDetalle = idTipoDetalle;
    }

    public Double getCantidadFac() {
        return cantidadFac;
    }

    public void setCantidadFac( Double cantidadFac ) {
        this.cantidadFac = cantidadFac;
    }

    public BigDecimal getPrecioUnitLista() {
        return precioUnitLista;
    }

    public void setPrecioUnitLista( BigDecimal precioUnitLista ) {
        this.precioUnitLista = precioUnitLista;
    }

    public BigDecimal getPrecioUnitFinal() {
        return precioUnitFinal;
    }

    public void setPrecioUnitFinal( BigDecimal precioUnitFinal ) {
        this.precioUnitFinal = precioUnitFinal;
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

    public String getSurte() {
        return surte;
    }

    public void setSurte( String surte ) {
        this.surte = surte;
    }

    public String getIdRepVenta() {
        return idRepVenta;
    }

    public void setIdRepVenta( String idRepVenta ) {
        this.idRepVenta = idRepVenta;
    }

    public BigDecimal getPrecioCalcLista() {
        return precioCalcLista;
    }

    public void setPrecioCalcLista( BigDecimal precioCalcLista ) {
        this.precioCalcLista = precioCalcLista;
    }

    public BigDecimal getPrecioCalcOferta() {
        return precioCalcOferta;
    }

    public void setPrecioCalcOferta( BigDecimal precioCalcOferta ) {
        this.precioCalcOferta = precioCalcOferta;
    }

    public BigDecimal getPrecioFactura() {
        return precioFactura;
    }

    public void setPrecioFactura( BigDecimal precioFactura ) {
        this.precioFactura = precioFactura;
    }

    public BigDecimal getPrecioConv() {
        return precioConv;
    }

    public void setPrecioConv( BigDecimal precioConv ) {
        this.precioConv = precioConv;
    }

    public TipoDetalle getTipoDetalle() {
        return tipoDetalle;
    }

    public void setTipoDetalle( TipoDetalle tipoDetalle ) {
        this.tipoDetalle = tipoDetalle;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo( Articulo articulo ) {
        this.articulo = articulo;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal( Sucursal sucursal ) {
        this.sucursal = sucursal;
    }

	public NotaVenta getNotaVenta() {
		return notaVenta;
	}

	public void setNotaVenta(NotaVenta notaVenta) {
		this.notaVenta = notaVenta;
	}
    
}

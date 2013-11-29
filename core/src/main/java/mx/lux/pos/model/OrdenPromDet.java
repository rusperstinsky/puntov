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
@Table( name = "orden_prom_det", schema = "public" )
public class OrdenPromDet implements Serializable, Comparable<OrdenPromDet> {

    private static final long serialVersionUID = 147484313122361162L;

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "orden_prom_det_id_orden_prom_det_seq" )
    @SequenceGenerator( schema = "public", sequenceName = "orden_prom_det_id_orden_prom_det_seq", name = "orden_prom_det_id_orden_prom_det_seq", allocationSize = 1 )
    @Column( name = "id_orden_prom_det" )
    private Integer idOrdenPromDet;

    @Column( name = "id" )
    private Integer idOrdenProm;

    @Column( name = "id_factura", nullable = false )
    private String idFactura;

    @Column( name = "id_prom" )
    private Integer idPromocion;

    @Column( name = "id_suc" )
    private Integer idSucursal;

    @Column( name = "id_art" )
    private Integer idArticulo;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "descuento_monto" )
    private BigDecimal descuentoMonto;

    @Column( name = "descuento_porcentaje" )
    private BigDecimal descuentoPorcentaje;

    @Transient
    private BigDecimal precioBase;

    @Column( name = "fecha_mod" )
    private Date fechaMod;

    @ManyToOne( fetch = FetchType.LAZY )
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_factura", referencedColumnName = "id_factura", insertable = false, updatable = false )
    private DetalleNotaVenta detalleNotaVenta;

    @PostLoad
    protected void onPostLoad() {
        this.idFactura = StringUtils.trimToEmpty( this.idFactura ).toUpperCase();
    }

    @PrePersist
    protected void onPrePersist() {
        fechaMod = new Date();
    }

    @PreUpdate
    protected void onPreUpdate() {
        fechaMod = new Date();
    }

    public Integer getIdOrdenPromDet() {
        return idOrdenPromDet;
    }

    public void setIdOrdenPromDet( Integer idOrdenPromDet ) {
        this.idOrdenPromDet = idOrdenPromDet;
    }

    public Integer getIdOrdenProm() {
        return idOrdenProm;
    }

    public void setIdOrdenProm( Integer pIdOrdenProm ) {
        idOrdenProm = pIdOrdenProm;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura( String pIdFactura ) {
        idFactura = pIdFactura;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal( Integer pIdSucursal ) {
        idSucursal = pIdSucursal;
    }

    public Integer getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion( Integer pIdPromocion ) {
        idPromocion = pIdPromocion;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo( Integer pIdArticulo ) {
        idArticulo = pIdArticulo;
    }

    public BigDecimal getDescuentoMonto() {
        return descuentoMonto;
    }

    public void setDescuentoMonto( BigDecimal pDescuentoMonto ) {
        descuentoMonto = pDescuentoMonto;
    }

    public BigDecimal getDescuentoPorcentaje() {
        return descuentoPorcentaje;
    }

    public void setDescuentoPorcentaje( BigDecimal pDescuentoPorcentaje ) {
        descuentoPorcentaje = pDescuentoPorcentaje;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod( Date pFechaMod ) {
        fechaMod = pFechaMod;
    }

    public BigDecimal getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase( BigDecimal pPrecioBase ) {
        precioBase = pPrecioBase;
    }

    // Entity
    public int compareTo( OrdenPromDet pOrdenPromDet ) {
        int result = this.getIdFactura().compareTo( pOrdenPromDet.getIdFactura() );
        if ( result == 0 ) {
            result = this.getIdPromocion().compareTo( pOrdenPromDet.getIdPromocion() );
        }
        return result;
    }

    public boolean equals( Object pObj ) {
        boolean result = false;
        if ( pObj instanceof OrdenPromDet ) {
            result = this.getIdOrdenPromDet().equals( ( ( OrdenPromDet ) pObj ).getIdOrdenPromDet() );
        } else if ( pObj instanceof OrdenProm ) {
            result = this.getIdOrdenProm().equals( ( ( OrdenProm ) pObj ).getId() );
        } else if ( pObj instanceof Promocion ) {
            result = this.getIdPromocion().equals( ( ( Promocion ) pObj ).getIdPromocion() );
        } else if ( pObj instanceof NotaVenta ) {
            result = this.getIdFactura().equals( ( ( NotaVenta ) pObj ).getId() );
        } else if ( pObj instanceof Articulo ) {
            result = this.getIdArticulo().equals( ( ( Articulo ) pObj ).getId() );
        }
        return result;
    }

    public boolean equals( String pIdFactura, Integer pSku ) {
        boolean result = ( ( this.getIdFactura().equalsIgnoreCase( StringUtils.trimToEmpty( pIdFactura ) ) ) &&
                ( this.getIdArticulo().equals( pSku ) ) );
        return result;
    }

    public int hashCode() {
        return String.format( "%d:%d", this.getIdOrdenProm(), this.getIdArticulo() ).hashCode();
    }

    public String toString() {
        return String.format( "[Fact:%s Sku:%d] Prom:%d  MontoDesc:%,.2f  Desc:%,.1f%%", this.getIdFactura(),
                this.getIdArticulo(), this.getIdPromocion(), this.getDescuentoMonto(), this.getDescuentoPorcentaje() );
    }

    public DetalleNotaVenta getDetalleNotaVenta() {
        return detalleNotaVenta;
    }

    public void setDetalleNotaVenta( DetalleNotaVenta detalleNotaVenta ) {
        this.detalleNotaVenta = detalleNotaVenta;
    }

}

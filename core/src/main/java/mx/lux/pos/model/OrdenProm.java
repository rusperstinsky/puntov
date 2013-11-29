package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table( name = "orden_prom", schema = "public" )
public class OrdenProm implements Serializable, Comparable<OrdenProm> {

    private static final long serialVersionUID = -6676831758125000560L;

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "orden_prom_id_seq" )
    @SequenceGenerator( schema = "public", sequenceName = "orden_prom_id_seq", name = "orden_prom_id_seq", allocationSize = 1 )
    @Column( name = "id" )
    private Integer id;

    @Column( name = "id_factura" )
    private String idFactura;

    @Column( name = "id_prom" )
    private Integer idPromocion;

    @Column( name = "id_suc" )
    private Integer idSucursal;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "total_desc_monto" )
    private BigDecimal totalDescMonto;

    @Column( name = "fecha_mod" )
    private Date fechaMod;

    @Transient
    private Set<OrdenPromDet> ordenPromDetSet = new HashSet<OrdenPromDet>();

    // Internal Methods
    @PostLoad
    protected void onPostLoad() {
        idFactura = StringUtils.trimToEmpty( idFactura ).toUpperCase();
    }

    @PrePersist
    protected void onPrePersist() {
        fechaMod = new Date();
    }

    @PreUpdate
    protected void onPreUpdate() {
        fechaMod = new Date();
    }

    // Public Methods
    public Integer getId() {
        return id;
    }

    public void setId( Integer pId ) {
        id = pId;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura( String pIdFactura ) {
        idFactura = StringUtils.trimToEmpty( pIdFactura ).toUpperCase();
    }

    public Integer getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion( Integer pIdPromocion ) {
        idPromocion = pIdPromocion;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal( Integer pIdSucursal ) {
        idSucursal = pIdSucursal;
    }

    public BigDecimal getTotalDescMonto() {
        return totalDescMonto;
    }

    public void setTotalDescMonto( BigDecimal pTotalDescMonto ) {
        totalDescMonto = pTotalDescMonto;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod( Date pFechaMod ) {
        fechaMod = pFechaMod;
    }

    public Set<OrdenPromDet> getOrdenPromDetSet() {
        return ordenPromDetSet;
    }

    public void setOrdenPromDetSet( Collection<OrdenPromDet> pOrdenPromDetSet ) {
        ordenPromDetSet.addAll( pOrdenPromDetSet );
    }

    // Other
    public void add( OrdenPromDet det ) {
        this.ordenPromDetSet.add( det );
    }

    public void add( List<OrdenPromDet> pListaOrdenPromDet ) {
        for ( OrdenPromDet det : pListaOrdenPromDet ) {
            this.add( det );
        }
    }

    // Entity
    public int compareTo( OrdenProm pOrdenProm ) {
        int result = this.getIdFactura().compareTo( pOrdenProm.getIdFactura() );
        if ( result == 0 )
            result = this.getIdPromocion().compareTo( pOrdenProm.getIdPromocion() );
        return result;
    }

    public boolean equals( Object pObj ) {
        boolean result = false;
        if ( pObj instanceof OrdenProm ) {
            result = this.getIdFactura().equals( ( ( OrdenProm ) pObj ).getIdFactura() )
                    && this.getIdPromocion().equals( ( ( OrdenProm ) pObj ).getIdPromocion() );
        } else if ( pObj instanceof Promocion ) {
            result = this.getIdPromocion().equals( ( ( Promocion ) pObj ).getIdPromocion() );
        } else if ( pObj instanceof NotaVenta ) {
            result = this.getIdFactura().equals( ( ( NotaVenta ) pObj ).getFactura() );
        } else if ( pObj instanceof OrdenPromDet ) {
            result = this.getId().equals( ( ( OrdenPromDet ) pObj ).getIdOrdenProm() );
        }
        return result;
    }

    public boolean equals( String pIdFactura, Integer pIdPromocion ) {
        boolean result = ( ( this.getIdFactura().equalsIgnoreCase( StringUtils.trimToEmpty( pIdFactura ) ) )
                && ( this.getIdPromocion().equals( pIdPromocion ) ) );
        return result;
    }

    public int hashCode() {
        return String.format( "%s:%06d", this.getIdFactura(), this.getIdPromocion() ).hashCode();
    }

    public String toString() {
        String str = String.format( "[Fact:%s  Prom:%d]  Suc:%d  TtlMontoDesc:%,.2f", this.getIdFactura(),
                this.getIdPromocion(), this.getIdSucursal(), this.getTotalDescMonto() );
        if ( this.getOrdenPromDetSet().size() > 0 ) {
            for ( OrdenPromDet det : this.getOrdenPromDetSet() ) {
                str = String.format( "%s\n    %s", str, det.toString() );
            }
        }
        return str;
    }
}

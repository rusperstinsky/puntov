package mx.lux.pos.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table( name = "saldos_ext_aux", schema = "public" )
public class EntregadoExterno implements Serializable {

    private static final long serialVersionUID = -4222587416222877461L;

    @Id
    @Column( name = "id" )
    private Integer id;

    @Column( name = "factura" )
    private String facturaTxt;

    @Column( name = "id_factura" )
    private String idFactura;

    @Column( name = "destino" )
    private String idSucursal;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "pago" )
    private BigDecimal pago;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha" )
    private Date fecha;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_pago" )
    private Date fechaPago;

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getFacturaTxt() {
        return facturaTxt;
    }

    public void setFacturaTxt( String facturaTxt ) {
        this.facturaTxt = facturaTxt;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura( String idFactura ) {
        this.idFactura = idFactura;
    }

    public BigDecimal getPago() {
        return pago;
    }

    public void setPago( BigDecimal pago ) {
        this.pago = pago;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha( Date fecha ) {
        this.fecha = fecha;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago( Date fechaPago ) {
        this.fechaPago = fechaPago;
    }

    public String getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal( String idSucursal ) {
        this.idSucursal = idSucursal;
    }
}

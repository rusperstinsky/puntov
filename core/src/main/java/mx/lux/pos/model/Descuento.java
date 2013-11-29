package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table( name = "descuentos", schema = "public" )
public class Descuento implements Serializable {

    private static final long serialVersionUID = -3414683701946088359L;

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "descuentos_id_seq" )
    @Column( name = "id" )
    @SequenceGenerator( schema = "public", sequenceName = "descuentos_id_seq", name = "descuentos_id_seq", allocationSize = 1 )
    private Integer id;

    @Column( name = "id_factura" )
    private String idFactura;

    @Column( name = "clave" )
    private String clave;

    @Column( name = "porcentaje" )
    private String porcentaje;

    @Column( name = "id_empleado" )
    private String idEmpleado;

    @Column( name = "id_tipo_d" )
    private String idTipoD;

    @Column( name = "tipo_clave" )
    private String tipoClave;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha" )
    private Date fecha;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_factura", insertable = false, updatable = false )
    private NotaVenta notaVenta;

    // Internal Methods
    @PostLoad
    protected void onPostLoad() {
        idFactura = StringUtils.trimToEmpty( idFactura ).toUpperCase();
        clave = StringUtils.trimToEmpty( clave ).toUpperCase();
        porcentaje = StringUtils.trimToEmpty( porcentaje );
        idEmpleado = StringUtils.trimToEmpty( idEmpleado ).toUpperCase();
        idTipoD = StringUtils.trimToEmpty( idTipoD ).toUpperCase();
        tipoClave = StringUtils.trimToEmpty( tipoClave ).toUpperCase();
    }

    @PrePersist
    protected void onPrePersist() {
        fecha = new Date();
    }

    @PreUpdate
    protected void onPreUpdate() {
        fecha = new Date();
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

    public String getClave() {
        return clave;
    }

    public void setClave( String clave ) {
        this.clave = clave;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje( String porcentaje ) {
        this.porcentaje = porcentaje;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado( String idEmpleado ) {
        this.idEmpleado = idEmpleado;
    }

    public String getIdTipoD() {
        return idTipoD;
    }

    public void setIdTipoD( String idTipoD ) {
        this.idTipoD = idTipoD;
    }

    public String getTipoClave() {
        return tipoClave;
    }

    public void setTipoClave( String tipoClave ) {
        this.tipoClave = tipoClave;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha( Date fecha ) {
        this.fecha = fecha;
    }

    public NotaVenta getNotaVenta() {
        return notaVenta;
    }

    public void setNotaVenta( NotaVenta notaVenta ) {
        this.notaVenta = notaVenta;
    }
}

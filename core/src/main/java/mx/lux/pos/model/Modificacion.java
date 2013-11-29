package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table( name = "mod", schema = "public" )
public class Modificacion implements Serializable {

    private static final long serialVersionUID = 2568660711104919730L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "mod_seq" )
    @SequenceGenerator( name = "mod_seq", sequenceName = "mod_seq" )
    @Column( name = "id_mod" )
    private Integer id;

    @Column( name = "id_factura" )
    private String idFactura;

    @Column( name = "tipo", length = 3, nullable = false )
    private String tipo;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha", nullable = false )
    private Date fecha;

    @Column( name = "empleado", length = 13 )
    private String idEmpleado;

    @Column( name = "causa" )
    private String causa;

    @Column( name = "obs" )
    private String observaciones;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_mod", updatable = false, insertable = false )
    private ModificacionImp modificacionImp;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_mod", updatable = false, insertable = false )
    private ModificacionPag modificacionPag;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_mod", updatable = false, insertable = false )
    private ModificacionCan modificacionCan;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_factura", updatable = false, insertable = false )
    private NotaVenta notaVenta;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "empleado", updatable = false, insertable = false )
    private Empleado empleado;

    @PrePersist
    private void onPrePersist() {
        fecha = new Date();
    }

    @PostLoad
    private void onPostLoad() {
        idFactura = StringUtils.trimToEmpty( idFactura );
        tipo = StringUtils.trimToEmpty( tipo );
        idEmpleado = StringUtils.trimToEmpty( idEmpleado );
        causa = StringUtils.trimToEmpty( causa );
        observaciones = StringUtils.trimToEmpty( observaciones );
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo( String tipo ) {
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha( Date fecha ) {
        this.fecha = fecha;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado( String idEmpleado ) {
        this.idEmpleado = idEmpleado;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa( String causa ) {
        this.causa = causa;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones( String observaciones ) {
        this.observaciones = observaciones;
    }

    public ModificacionImp getModificacionImp() {
        return modificacionImp;
    }

    public void setModificacionImp( ModificacionImp modificacionImp ) {
        this.modificacionImp = modificacionImp;
    }

    public ModificacionPag getModificacionPag() {
        return modificacionPag;
    }

    public void setModificacionPag( ModificacionPag modificacionPag ) {
        this.modificacionPag = modificacionPag;
    }

    public ModificacionCan getModificacionCan() {
        return modificacionCan;
    }

    public void setModificacionCan( ModificacionCan modificacionCan ) {
        this.modificacionCan = modificacionCan;
    }

    public NotaVenta getNotaVenta() {
        return notaVenta;
    }

    public void setNotaVenta( NotaVenta notaVenta ) {
        this.notaVenta = notaVenta;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado( Empleado empleado ) {
        this.empleado = empleado;
    }
}

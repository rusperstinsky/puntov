package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table( name = "cotiza", schema = "public" )
public class Cotizacion implements Serializable, Comparable<Cotizacion> {

    private static final long serialVersionUID = -2399656770652510074L;
    private static final DateFormat df = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "cotiza_id_cotiza_seq" )
    @SequenceGenerator( name = "cotiza_id_cotiza_seq", sequenceName = "cotiza_id_cotiza_seq" )
    @Column( name = "id_cotiza" )
    private Integer idCotiza;

    @Column( name = "id_sucursal", nullable = false )
    private Integer idSucursal;

    @Column( name = "id_cliente", nullable = false )
    private Integer idCliente;

    @Column( name = "id_empleado" )
    private String idEmpleado;

    @Column( name = "id_receta", nullable = false )
    private Integer idReceta;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod", nullable = false )
    private Date fechaMod;

    @Column( name = "id_factura" )
    private String idFactura;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_venta" )
    private Date fechaVenta;

    @Column( name = "nombre" )
    private String nombre;

    @Column( name = "telefono" )
    private String tel;

    @Column( name = "observaciones" )
    private String observaciones;

    @Column( name = "titulo" )
    private String titulo;

    @Column( name = "udf1" )
    private String udf1;

    @OneToMany( fetch = FetchType.EAGER )
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_cotiza", referencedColumnName = "id_cotiza", insertable = false, updatable = false )
    private List<CotizaDet> cotizaDet = new ArrayList<CotizaDet>();

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
    }

    public Integer getIdCotiza() {
        return idCotiza;
    }

    public void setIdCotiza( Integer pIdCotiza ) {
        this.idCotiza = pIdCotiza;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal( Integer idSucursal ) {
        this.idSucursal = idSucursal;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente( Integer idCliente ) {
        this.idCliente = idCliente;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado( String idEmpleado ) {
        this.idEmpleado = StringUtils.trimToEmpty( idEmpleado ).toUpperCase();
    }

    public Integer getIdReceta() {
        return idReceta;
    }

    public void setIdReceta( Integer idReceta ) {
        this.idReceta = idReceta;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod( Date fechaMod ) {
        this.fechaMod = fechaMod;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura( String idFactura ) {
        this.idFactura = StringUtils.trimToEmpty( idFactura ).toUpperCase();
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta( Date fechaVenta ) {
        this.fechaVenta = fechaVenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre( String nombre ) {
        this.nombre = StringUtils.trimToEmpty( nombre );
    }

    public String getTel() {
        return tel;
    }

    public void setTel( String tel ) {
        this.tel = StringUtils.trimToEmpty( tel );
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones( String observaciones ) {
        this.observaciones = StringUtils.trimToEmpty( observaciones );
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo( String titulo ) {
        this.titulo = StringUtils.trimToEmpty( titulo );
    }

    public String getUdf1() {
        return udf1;
    }

    public void setUdf1( String udf1 ) {
        this.udf1 = StringUtils.trimToEmpty( udf1 );
    }

    public List<CotizaDet> getCotizaDet() {
        return cotizaDet;
    }

    public void setCotizaDet( List<CotizaDet> cotizaDet ) {
        this.cotizaDet = cotizaDet;
    }

    // Identity
    public int compareTo( Cotizacion pCotizacion ) {
        return this.getIdCotiza().compareTo( pCotizacion.getIdCotiza() );
    }

    public boolean equals( Object pObj ) {
        boolean result = false;
        if ( pObj instanceof Cotizacion ) {
            result = this.getIdCotiza().equals( ( ( Cotizacion ) pObj ).getIdCotiza() );
        }
        return result;
    }

    public int hashCode() {
        return this.getIdCotiza().hashCode();
    }

    public String toString() {
        return String.format( "(%06d) Cliente:%06d  Empleado:%s  Fecha:%s", this.getIdCotiza(), this.getIdCliente(),
                this.getIdEmpleado(), df.format( this.getFechaMod() ) );
    }

}

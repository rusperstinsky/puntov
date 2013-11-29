package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "rfc", schema = "public" )
public class Contribuyente implements Serializable {

    private static final long serialVersionUID = 359913027503872777L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "rfc_id_seq" )
    @SequenceGenerator( name = "rfc_id_seq", sequenceName = "rfc_id_seq" )
    @Column( name = "id" )
    private Integer id;

    @Column( name = "id_cliente" )
    private Integer idCliente;

    @Column( name = "rfc_fiscal", length = 14 )
    private String rfc;

    @Column( name = "razon_social" )
    private String nombre;

    @Column( name = "dom_fiscal" )
    private String domicilio;

    @Column( name = "colonia_fiscal" )
    private String colonia;

    @Column( name = "ciudad_fiscal" )
    private String ciudad;

    @Column( name = "id_estado", length = 2 )
    private String idEstado;

    @Column( name = "cp_fiscal", length = 5 )
    private String codigoPostal;

    @Column( name = "telefono_fiscal", length = 20 )
    private String telefono;

    @Column( name = "id_sync", length = 1 )
    private String idSync;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_mod", nullable = false )
    private Date fechaModificacion;

    @Column( name = "id_sucursal", nullable = false )
    private Integer idSucursal;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_reg" )
    private Date fechaRegistro;

    @Column( name = "impresion" )
    private boolean impresion = true;

    @Column( name = "email" )
    private String email;

    @PrePersist
    private void onPrePersist() {
        fechaRegistro = new Date();
        fechaModificacion = fechaRegistro;
    }

    @PreUpdate
    private void onPreUpdate() {
        fechaModificacion = new Date();
    }

    @PostLoad
    private void onPostLoad() {
        rfc = StringUtils.trimToEmpty( rfc );
        nombre = StringUtils.trimToEmpty( nombre );
        domicilio = StringUtils.trimToEmpty( domicilio );
        colonia = StringUtils.trimToEmpty( colonia );
        ciudad = StringUtils.trimToEmpty( ciudad );
        idEstado = StringUtils.trimToEmpty( idEstado );
        codigoPostal = StringUtils.trimToEmpty( codigoPostal );
        telefono = StringUtils.trimToEmpty( telefono );
        email = StringUtils.trimToEmpty( email );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente( Integer idCliente ) {
        this.idCliente = idCliente;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc( String rfc ) {
        this.rfc = rfc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre( String nombre ) {
        this.nombre = nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio( String domicilio ) {
        this.domicilio = domicilio;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia( String colonia ) {
        this.colonia = colonia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad( String ciudad ) {
        this.ciudad = ciudad;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado( String idEstado ) {
        this.idEstado = idEstado;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal( String codigoPostal ) {
        this.codigoPostal = codigoPostal;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono( String telefono ) {
        this.telefono = telefono;
    }

    public String getIdSync() {
        return idSync;
    }

    public void setIdSync( String idSync ) {
        this.idSync = idSync;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion( Date fechaModificacion ) {
        this.fechaModificacion = fechaModificacion;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal( Integer idSucursal ) {
        this.idSucursal = idSucursal;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro( Date fechaRegistro ) {
        this.fechaRegistro = fechaRegistro;
    }

    public boolean isImpresion() {
        return impresion;
    }

    public void setImpresion( boolean impresion ) {
        this.impresion = impresion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String toString() {
        return String.format( "[%s] %s ", this.getRfc(), this.getNombre() );
    }
}

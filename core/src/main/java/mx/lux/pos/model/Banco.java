package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "bancos", schema = "public" )
public class Banco implements Serializable {

    private static final long serialVersionUID = 834351416246844768L;

    @Id
    @Column( name = "id_banco" )
    private Integer id;

    @Column( name = "nombre_banco" )
    private String nombre;

    @Column( name = "por_comision_tc" )
    private Integer porComisionTc;

    @Column( name = "f_iva_comision" )
    private boolean ivaComision;

    @Column( name = "f_acepta_dev" )
    private boolean aceptaDevolucion;

    @Column( name = "id_sync", length = 1, nullable = false )
    private String idSync = "1";

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod", nullable = false )
    private Date fechaModificacion;

    @Column( name = "id_mod", length = 13, nullable = false )
    private String idMod = "0";

    @Column( name = "id_sucursal", nullable = false )
    private Integer idSucursal;

    @PrePersist
    private void onPrePersist() {
        fechaModificacion = new Date();
    }

    @PreUpdate
    private void onPreUpdate() {
        fechaModificacion = new Date();
    }

    @PostLoad
    private void onPostLoad() {
        nombre = StringUtils.trimToEmpty( nombre );
        idMod = StringUtils.trimToEmpty( idMod );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre( String nombre ) {
        this.nombre = nombre;
    }

    public Integer getPorComisionTc() {
        return porComisionTc;
    }

    public void setPorComisionTc( Integer porComisionTc ) {
        this.porComisionTc = porComisionTc;
    }

    public boolean isIvaComision() {
        return ivaComision;
    }

    public void setIvaComision( boolean ivaComision ) {
        this.ivaComision = ivaComision;
    }

    public boolean isAceptaDevolucion() {
        return aceptaDevolucion;
    }

    public void setAceptaDevolucion( boolean aceptaDevolucion ) {
        this.aceptaDevolucion = aceptaDevolucion;
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
}

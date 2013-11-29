package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "genericos", schema = "public" )
public class Generico implements Serializable {

    private static final long serialVersionUID = -5098654033598499655L;

    @Id
    @Column( name = "id_generico", length = 1 )
    private String id;

    @Column( name = "descripcion_generico" )
    private String descripcion;

    @Column( name = "id_sync", length = 1, nullable = false )
    private String idSync = "1";

    @Column( name = "inventariable" )
    private Boolean inventariable = true;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod", nullable = false )
    private Date fechaMod = new Date();

    @Column( name = "id_mod", length = 13, nullable = false )
    private String idMod = "0";

    @Column( name = "id_sucursal" )
    private Integer idSucursal;

    @Column( name = "surte" )
    private String surte;

    @PostLoad
    private void trim() {
        id = StringUtils.trimToNull( id );
        descripcion = StringUtils.trimToNull( descripcion );
        idSync = StringUtils.trimToNull( idSync );
        idMod = StringUtils.trimToNull( idMod );
        surte = StringUtils.trimToNull( surte );
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion( String descripcion ) {
        this.descripcion = descripcion;
    }

    public String getIdSync() {
        return idSync;
    }

    public void setIdSync( String idSync ) {
        this.idSync = idSync;
    }

    public Boolean getInventariable() {
        return inventariable;
    }

    public void setInventariable( Boolean inventariable ) {
        this.inventariable = inventariable;
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
}

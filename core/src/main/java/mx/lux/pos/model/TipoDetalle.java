package mx.lux.pos.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table( name = "tipo_detalle", schema = "public" )
public class TipoDetalle implements Serializable {

    private static final long serialVersionUID = -8093676108930126263L;

    @Id
    @Column( name = "id_tipo_detalle" )
    private String id;

    @Column( name = "desc_detalle" )
    private String descripcion;

    @Column( name = "porcentaje_detalle" )
    private Integer porcentaje;

    @Column( name = "id_sync" )
    private Character idSync;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod" )
    private Date fechaModificacion;

    @Column( name = "id_mod" )
    private String idMod;

    @Column( name = "id_sucursal" )
    private Integer idSucursal;

    @Column( name = "der" )
    private Boolean derecho;

    @Column( name = "izq" )
    private Boolean izquierdo;

    @Column( name = "fte" )
    private Boolean fte;

    @Column( name = "acc" )
    private Boolean acc;

    @Column( name = "partes" )
    private Integer partes;

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

    public Integer getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje( Integer porcentaje ) {
        this.porcentaje = porcentaje;
    }

    public Character getIdSync() {
        return idSync;
    }

    public void setIdSync( Character idSync ) {
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

    public Boolean getDerecho() {
        return derecho;
    }

    public void setDerecho( Boolean derecho ) {
        this.derecho = derecho;
    }

    public Boolean getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo( Boolean izquierdo ) {
        this.izquierdo = izquierdo;
    }

    public Boolean getFte() {
        return fte;
    }

    public void setFte( Boolean fte ) {
        this.fte = fte;
    }

    public Boolean getAcc() {
        return acc;
    }

    public void setAcc( Boolean acc ) {
        this.acc = acc;
    }

    public Integer getPartes() {
        return partes;
    }

    public void setPartes( Integer partes ) {
        this.partes = partes;
    }

}

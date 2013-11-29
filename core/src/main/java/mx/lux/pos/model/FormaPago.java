package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "forma_pago", schema = "public" )
public class FormaPago implements Serializable {

    private static final long serialVersionUID = 3627038677660169174L;

    @Id
    @Column( name = "id_forma_pago", length = 2 )
    private String id;

    @Column( name = "desc_pago" )
    private String descripcion;

    @Column( name = "f_pedir_referencia" )
    private boolean pedirReferencia;

    @Column( name = "f_pedir_banco" )
    private boolean pedirBanco;

    @Column( name = "columna_reporte_ci" )
    private Integer columnaReporteCi;

    @Column( name = "f_acepta_dev" )
    private boolean aceptaDevoluciones;

    @Column( name = "f_acepta_en_pagos" )
    private boolean aceptaEnPagos;

    @Column( name = "f_aplica_nv" )
    private boolean aplicaNv;

    @Column( name = "default_forma_dev", length = 2 )
    private String defaultFormaDevolucion;

    @Column( name = "f_modificable" )
    private boolean modificable;

    @Column( name = "id_sync" )
    private char idSync;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_mod" )
    private Date fechaModificacion;

    @Column( name = "id_mod", length = 13 )
    private String idMod;

    @Column( name = "id_sucursal" )
    private Integer idSucursal;

    @PostLoad
    private void trim() {
        descripcion = StringUtils.trimToEmpty( descripcion );
        defaultFormaDevolucion = StringUtils.trimToEmpty( defaultFormaDevolucion );
        idMod = StringUtils.trimToEmpty( idMod );
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

    public boolean isPedirReferencia() {
        return pedirReferencia;
    }

    public void setPedirReferencia( boolean pedirReferencia ) {
        this.pedirReferencia = pedirReferencia;
    }

    public boolean isPedirBanco() {
        return pedirBanco;
    }

    public void setPedirBanco( boolean pedirBanco ) {
        this.pedirBanco = pedirBanco;
    }

    public Integer getColumnaReporteCi() {
        return columnaReporteCi;
    }

    public void setColumnaReporteCi( Integer columnaReporteCi ) {
        this.columnaReporteCi = columnaReporteCi;
    }

    public boolean isAceptaDevoluciones() {
        return aceptaDevoluciones;
    }

    public void setAceptaDevoluciones( boolean aceptaDevoluciones ) {
        this.aceptaDevoluciones = aceptaDevoluciones;
    }

    public boolean isAceptaEnPagos() {
        return aceptaEnPagos;
    }

    public void setAceptaEnPagos( boolean aceptaEnPagos ) {
        this.aceptaEnPagos = aceptaEnPagos;
    }

    public boolean isAplicaNv() {
        return aplicaNv;
    }

    public void setAplicaNv( boolean aplicaNv ) {
        this.aplicaNv = aplicaNv;
    }

    public String getDefaultFormaDevolucion() {
        return defaultFormaDevolucion;
    }

    public void setDefaultFormaDevolucion( String defaultFormaDevolucion ) {
        this.defaultFormaDevolucion = defaultFormaDevolucion;
    }

    public boolean isModificable() {
        return modificable;
    }

    public void setModificable( boolean modificable ) {
        this.modificable = modificable;
    }

    public char getIdSync() {
        return idSync;
    }

    public void setIdSync( char idSync ) {
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

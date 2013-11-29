package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table( name = "lp_recibe", schema = "public" )
public class ListaPrecios implements Serializable {

    private static final long serialVersionUID = -5861352358971677392L;

    @Id
    @Column( name = "id_lista", length = 4 )
    private String id;

    @Column( name = "filename", length = 30 )
    private String filename;

    @Column( name = "tipo_carga", length = 6 )
    private String tipoCarga;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_act" )
    private Date fechaAct;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_act_auto" )
    private Date fechaActAuto;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod", nullable = false )
    private Date fechaMod;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_carga" )
    private Date fechaCarga;

    @PrePersist
    private void onPrePersist() {
        //fechaCarga = new Date();
        //fechaMod = fechaCarga;
        fechaMod = new Date();
    }

    @PreUpdate
    private void onPreUpdate() {
        fechaMod = new Date();
    }

    @PostLoad
    private void onPostLoad() {
        filename = StringUtils.trimToEmpty( filename );
        tipoCarga = StringUtils.trimToEmpty( tipoCarga );
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename( String filename ) {
        this.filename = filename;
    }

    public String getTipoCarga() {
        return tipoCarga;
    }

    public void setTipoCarga( String tipoCarga ) {
        this.tipoCarga = tipoCarga;
    }

    public Date getFechaAct() {
        return fechaAct;
    }

    public void setFechaAct( Date fechaAct ) {
        this.fechaAct = fechaAct;
    }

    public Date getFechaActAuto() {
        return fechaActAuto;
    }

    public void setFechaActAuto( Date fechaActAuto ) {
        this.fechaActAuto = fechaActAuto;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod( Date fechaMod ) {
        this.fechaMod = fechaMod;
    }

    public Date getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga( Date fechaCarga ) {
        this.fechaCarga = fechaCarga;
    }
}

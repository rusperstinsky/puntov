package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "causa_mc", schema = "public" )
public class CausaCancelacion implements Serializable {

    private static final long serialVersionUID = -2951149997692217197L;

    @Id
    @Column( name = "id_causa_mc" )
    private Integer id;

    @Column( name = "descrip_causa_mc" )
    private String descripcion;

    @Column( name = "id_sync", length = 1, nullable = false )
    private String idSync = "1";

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod", nullable = false )
    private Date fechaModificacion;

    @Column( name = "id_mod", length = 13, nullable = false )
    private String idModificacion = "0";

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
        descripcion = StringUtils.trimToEmpty( descripcion );
        idModificacion = StringUtils.trimToEmpty( idModificacion );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
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

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion( Date fechaModificacion ) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getIdModificacion() {
        return idModificacion;
    }

    public void setIdModificacion( String idModificacion ) {
        this.idModificacion = idModificacion;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal( Integer idSucursal ) {
        this.idSucursal = idSucursal;
    }
}

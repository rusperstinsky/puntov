package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "existencias", schema = "public" )
public class Existencia implements Serializable {

    private static final long serialVersionUID = -7540138417081871630L;

    @Id
    @Column( name = "id_articulo" )
    private Integer idArticulo;

    @Column( name = "cantidad_art" )
    private Integer cantidad;

    @Column( name = "id_sync", length = 1, nullable = false )
    private String idSync = "1";

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod", nullable = false )
    private Date fechaMod = new Date();

    @Column( name = "id_mod", length = 13, nullable = false )
    private String idMod = "0";

    @Column( name = "id_sucursal", nullable = false )
    private Integer idSucursal = 0;

    @PostLoad
    private void trim() {
        idSync = StringUtils.trimToNull( idSync );
        idMod = StringUtils.trimToNull( idMod );
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo( Integer idArticulo ) {
        this.idArticulo = idArticulo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad( Integer cantidad ) {
        this.cantidad = cantidad;
    }

    public String getIdSync() {
        return idSync;
    }

    public void setIdSync( String idSync ) {
        this.idSync = idSync;
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
}

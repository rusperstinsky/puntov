package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "titulo", schema = "public" )
public class Titulo implements Serializable {

    private static final long serialVersionUID = 2540202424313087125L;

    @Id
    @Column( name = "titulo", length = 10, nullable = false )
    private String titulo;

    @Column( name = "ord" )
    private Integer orden;

    @Column( name = "sexo_titulo" )
    private char sexoTitulo;

    @Column( name = "id_sync", length = 1 )
    private String idSync = "1";

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod" )
    private Date fechaModificado = new Date();

    @Column( name = "id_mod", length = 13 )
    private String idModificado = "0";

    @Column( name = "id_sucursal", nullable = false )
    private Integer idSucursal;

    @PostLoad
    private void trim() {
        titulo = StringUtils.trimToNull( titulo );
        idSync = StringUtils.trimToNull( idSync );
        idModificado = StringUtils.trimToNull( idModificado );
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo( String titulo ) {
        this.titulo = titulo;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden( Integer orden ) {
        this.orden = orden;
    }

    public char getSexoTitulo() {
        return sexoTitulo;
    }

    public void setSexoTitulo( char sexoTitulo ) {
        this.sexoTitulo = sexoTitulo;
    }

    public String getIdSync() {
        return idSync;
    }

    public void setIdSync( String idSync ) {
        this.idSync = idSync;
    }

    public Date getFechaModificado() {
        return fechaModificado;
    }

    public void setFechaModificado( Date fechaModificado ) {
        this.fechaModificado = fechaModificado;
    }

    public String getIdModificado() {
        return idModificado;
    }

    public void setIdModificado( String idModificado ) {
        this.idModificado = idModificado;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal( Integer idSucursal ) {
        this.idSucursal = idSucursal;
    }
}

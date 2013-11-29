package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "dominios", schema = "public" )
public class Dominio implements Serializable {

    private static final long serialVersionUID = 3061456046859437655L;

    @Id
    @Column( name = "id_dominio" )
    private Integer id;

    @Column( name = "nombre" )
    private String nombre;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod" )
    private Date fechaModificado = new Date();

    @PostLoad
    private void trim() {
        nombre = StringUtils.trimToNull( nombre );
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

    public Date getFechaModificado() {
        return fechaModificado;
    }

    public void setFechaModificado( Date fechaModificado ) {
        this.fechaModificado = fechaModificado;
    }
}

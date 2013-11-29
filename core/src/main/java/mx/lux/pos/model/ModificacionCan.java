package mx.lux.pos.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table( name = "mod_can", schema = "public" )
public class ModificacionCan implements Serializable {

    private static final long serialVersionUID = 7681608500170946463L;

    @Id
    @Column( name = "id_mod" )
    private Integer id;

    @Column( name = "status_old", length = 1 )
    private String estadoAnterior;

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior( String estadoAnterior ) {
        this.estadoAnterior = estadoAnterior;
    }
}

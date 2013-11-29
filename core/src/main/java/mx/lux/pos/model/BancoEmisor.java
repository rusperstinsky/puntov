package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable
@Table( name = "banco_emi", schema = "public" )
public class BancoEmisor implements Serializable {

    private static final long serialVersionUID = -3169764298363394320L;

    @Id
    @Column( name = "id_banco_emi" )
    private Integer id;

    @Column( name = "descripcion", length = 50 )
    private String descripcion;

    @Column( name = "tipo", length = 5 )
    private String tipo;

    @PostLoad
    private void trim() {
        descripcion = StringUtils.trimToEmpty( descripcion );
        tipo = StringUtils.trimToEmpty( tipo );
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo( String tipo ) {
        this.tipo = tipo;
    }
}

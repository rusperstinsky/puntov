package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable
@Table( name = "gparametro", schema = "public" )
public class Parametro implements Serializable {

    private static final long serialVersionUID = 7480323145043762625L;

    @Id
    @Column( name = "id_parametro" )
    private String id;

    @Column( name = "valor" )
    private String valor;

    @Column( name = "descr" )
    private String descripcion;

    @PostLoad
    protected void onPostLoad() {
        id = StringUtils.trimToEmpty( id );
        valor = StringUtils.trimToEmpty( valor );
        descripcion = StringUtils.trimToEmpty( descripcion );
    }

    public String[] getValores() {
        return StringUtils.split( valor, ',' );
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor( String valor ) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion( String descripcion ) {
        this.descripcion = descripcion;
    }
}

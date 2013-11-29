package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table( name = "mensaje", schema = "public" )
public class Mensaje implements Serializable {

    private static final long serialVersionUID = 4374936376369345570L;

    @Id
    @Column( name = "id" )
    private Integer id;

    @Column( name = "clave" )
    private String clave;

    @Column( name = "texto" )
    private String texto;

    public String getClave() {
        return clave;
    }

    public void setClave( String clave ) {
        this.clave = StringUtils.trimToEmpty( clave ).toUpperCase();
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto( String texto ) {
        this.texto = StringUtils.trimToEmpty( texto );
    }

    public int compareTo( Mensaje mensaje ) {
        int result = this.getId().compareTo( mensaje.getId() );
        if( result == 0) result = this.getClave().compareToIgnoreCase( mensaje.getClave() );
        return result;
    }

    public boolean equals( Object obj ){
        boolean result = false;
        if( obj instanceof Mensaje ){
            result = this.getId().equals( ( ( Mensaje) obj ).getId() );
        } else if( obj instanceof Integer ){
            result = this.getId().equals( (Integer) obj );
        }
        return result;
    }

    public int hashCode(){
        return this.getId().hashCode();
    }

    public String toString(){
        String str = String.format( "%s [%d]", this.getTexto(), this.getId() );
        return str;
    }
}

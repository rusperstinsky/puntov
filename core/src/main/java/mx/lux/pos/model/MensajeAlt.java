package mx.lux.pos.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table( name = "mensaje_alt", schema = "public" )
public class MensajeAlt implements Serializable {

    private static final long serialVersionUID = -6207016590630846023L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "mensaje_alt_id_seq" )
    @SequenceGenerator( name = "mensaje_alt_id_seq", sequenceName = "mensaje_alt_id_seq" )
    @Column( name = "id" )
    private Integer id;

    @Column( name = "id_mensaje", nullable = false )
    private Integer idMensaje;

    @Column( name = "idioma" )
    private String idioma;

    @Column( name = "texto" )
    private String texto;




    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma( String idioma ) {
        this.idioma = idioma;
    }

    public Integer getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje( Integer idMensaje ) {
        this.idMensaje = idMensaje;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto( String texto ) {
        this.texto = texto;
    }

    public int compareTo( MensajeAlt mensajealt ) {
        int result = this.getId().compareTo( mensajealt.getId() );
        if( result == 0) result = this.getIdioma().compareToIgnoreCase( mensajealt.getIdioma() );
        return result;
    }

    public boolean equals( Object obj ){
        boolean result = false;
        if( obj instanceof MensajeAlt ){
            result = this.getId().equals( ( ( MensajeAlt) obj ).getId() );
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

package mx.lux.pos.model;


import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table( name = "moneda", schema = "public" )
public class Moneda implements Serializable, Comparable<Moneda> {

    private static final long serialVersionUID = 120566201004L;

    @Id
    @Column( name = "id_moneda" )
    private String idMoneda;

    @Column( name = "descripcion" )
    private String descripcion;

    // Properties
    public String getIdMoneda() {
        return idMoneda;
    }

    public void setIdMoneda( String idMoneda ) {
        this.idMoneda = StringUtils.trimToEmpty( idMoneda ).toUpperCase();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion( String descripcion ) {
        this.descripcion = StringUtils.trimToEmpty( descripcion );
    }

    // Data Management

    // Identity
    public int compareTo( Moneda pMoneda ) {
        return this.getIdMoneda().compareTo( pMoneda.getIdMoneda() );
    }

    public boolean equals( Object pObj ) {
        boolean result = false;
        if ( pObj instanceof Moneda ) {
            result = this.getIdMoneda().equals(((Moneda) pObj).getIdMoneda());
        } else if ( pObj instanceof String ) {
            result = this.getIdMoneda().equalsIgnoreCase( ((String) pObj).trim() );
        }
        return result;
    }

    public int hashCode() {
        return this.getIdMoneda().hashCode();
    }

    public String toString() {
        return String.format( "[%s] %s", this.getIdMoneda(), this.getDescripcion() );
    }

}

package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable
@Table( name = "rep", schema = "public" )
public class Estado implements Serializable {

    private static final long serialVersionUID = 7794286300190840328L;

    @Id
    @Column( name = "id_estado", length = 2 )
    private String id;

    @Column( name = "nombre", length = 20 )
    private String nombre;

    @Column( name = "edo1", length = 6 )
    private String edo1;

    @Column( name = "rango1", length = 5 )
    private String rango1;

    @Column( name = "rango2", length = 5 )
    private String rango2;

    @PostLoad
    private void onPostLoad() {
        nombre = StringUtils.trimToEmpty( nombre );
        edo1 = StringUtils.trimToEmpty( edo1 );
        rango1 = StringUtils.trimToEmpty( rango1 );
        rango2 = StringUtils.trimToEmpty( rango2 );
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre( String nombre ) {
        this.nombre = nombre;
    }

    public String getEdo1() {
        return edo1;
    }

    public void setEdo1( String edo1 ) {
        this.edo1 = edo1;
    }

    public String getRango1() {
        return rango1;
    }

    public void setRango1( String rango1 ) {
        this.rango1 = rango1;
    }

    public String getRango2() {
        return rango2;
    }

    public void setRango2( String rango2 ) {
        this.rango2 = rango2;
    }
}

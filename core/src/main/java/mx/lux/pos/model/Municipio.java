package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable
@Table( name = "munici", schema = "public" )
public class Municipio implements Serializable {

    private static final long serialVersionUID = -1409869179513547763L;

    @Id
    @Column( name = "id" )
    private Integer id;

    @Column( name = "id_estado", length = 2 )
    private String idEstado;

    @Column( name = "id_localidad", length = 3 )
    private String idLocalidad;

    @Column( name = "nombre" )
    private String nombre;

    @Column( name = "rango1", length = 5 )
    private String rango1;

    @Column( name = "rango2", length = 5 )
    private String rango2;

    @Column( name = "rango3", length = 5 )
    private String rango3;

    @Column( name = "rango4", length = 5 )
    private String rango4;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_estado", insertable = false, updatable = false )
    private Estado estado;

    @PostLoad
    private void trim() {
        idEstado = StringUtils.trimToNull( idEstado );
        idLocalidad = StringUtils.trimToNull( idLocalidad );
        nombre = StringUtils.trimToNull( nombre );
        rango1 = StringUtils.trimToNull( rango1 );
        rango2 = StringUtils.trimToNull( rango2 );
        rango3 = StringUtils.trimToNull( rango3 );
        rango4 = StringUtils.trimToNull( rango4 );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado( String idEstado ) {
        this.idEstado = idEstado;
    }

    public String getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad( String idLocalidad ) {
        this.idLocalidad = idLocalidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre( String nombre ) {
        this.nombre = nombre;
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

    public String getRango3() {
        return rango3;
    }

    public void setRango3( String rango3 ) {
        this.rango3 = rango3;
    }

    public String getRango4() {
        return rango4;
    }

    public void setRango4( String rango4 ) {
        this.rango4 = rango4;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado( Estado estado ) {
        this.estado = estado;
    }
}

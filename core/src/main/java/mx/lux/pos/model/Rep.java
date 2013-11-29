package mx.lux.pos.model;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table( name = "rep", schema = "public" )
public class Rep implements Serializable {


    private static final long serialVersionUID = -5056395441178516916L;

    @Id
    @Column( name = "id_estado", length = 2 )
    private String id;

    @Column( name = "nombre", length = 20 )
    private String nombre   ;

    @Column( name = "edo1", length = 6 )
    private String  edo1;

    @Column( name = "rango1", length = 5 )
    private String  rango1;

    @Column( name = "rango2", length = 5 )
    private String  rango2;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEdo1() {
        return edo1;
    }

    public void setEdo1(String edo1) {
        this.edo1 = edo1;
    }

    public String getRango1() {
        return rango1;
    }

    public void setRango1(String rango1) {
        this.rango1 = rango1;
    }

    public String getRango2() {
        return rango2;
    }

    public void setRango2(String rango2) {
        this.rango2 = rango2;
    }
}

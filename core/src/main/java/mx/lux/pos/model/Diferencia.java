package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table( name = "diferencias", schema = "public" )
public class Diferencia implements Serializable {


    private static final long serialVersionUID = -3157408469411237903L;

    @Id
    @Column( name = "id_articulo" )
    private Integer id;

    @Column( name = "cantidad_fisico" )
    private Integer cantidadFisico;

    @Column( name = "cantidad_soi" )
    private Integer cantidadSoi;

    @Column( name = "diferencias" )
    private Integer diferencias;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_articulo", referencedColumnName = "id_articulo", insertable = false, updatable = false )
    private Articulo articulo;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidadFisico() {
        return cantidadFisico;
    }

    public void setCantidadFisico(Integer cantidadFisico) {
        this.cantidadFisico = cantidadFisico;
    }

    public Integer getCantidadSoi() {
        return cantidadSoi;
    }

    public void setCantidadSoi(Integer cantidadSoi) {
        this.cantidadSoi = cantidadSoi;
    }

    public Integer getDiferencias() {
        return diferencias;
    }

    public void setDiferencias(Integer diferencias) {
        this.diferencias = diferencias;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }
}

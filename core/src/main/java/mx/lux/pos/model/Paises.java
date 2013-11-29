package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "paises", schema = "public" )
public class Paises implements Serializable {

    private static final long serialVersionUID = 4992942609966672499L;

    @Id
    @Column( name = "id_pais" )
    private Integer id;

    @Column( name = "pais" )
    private String pais;

    @Column( name = "orden" )
    private Integer orden;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }
}

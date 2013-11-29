package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "precios", schema = "public" )
public class Precio implements Serializable {

    private static final long serialVersionUID = -7947677033541635479L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "precios_id_seq" )
    @SequenceGenerator( name = "precios_id_seq", sequenceName = "precios_id_seq" )
    @Column( name = "id" )
    private Integer id;

    @Column( name = "lista", length = 10, nullable = false )
    private String lista;

    @Column( name = "articulo", length = 20, nullable = false )
    private String articulo;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "precio", nullable = false )
    private BigDecimal precio;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha", nullable = false )
    private Date fecha;

    @Column( name = "surte", length = 1 )
    private String surte;

    @PrePersist
    private void onPrePersist() {
        fecha = new Date();
    }

    @PreUpdate
    private void onPreUpdate() {
        fecha = new Date();
    }

    @PostLoad
    private void onPostLoad() {
        lista = StringUtils.trimToNull( lista );
        articulo = StringUtils.trimToNull( articulo );
        surte = StringUtils.trimToNull( surte );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getLista() {
        return lista;
    }

    public void setLista( String lista ) {
        this.lista = lista;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo( String articulo ) {
        this.articulo = articulo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio( BigDecimal precio ) {
        this.precio = precio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha( Date fecha ) {
        this.fecha = fecha;
    }

    public String getSurte() {
        return surte;
    }

    public void setSurte( String surte ) {
        this.surte = surte;
    }
}

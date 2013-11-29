package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "impuestos", schema = "public" )
public class Impuesto implements Serializable {

    private static final long serialVersionUID = 8998430883164521786L;

    @Id
    @Column( name = "id_impuesto" )
    private String id;

    @Column( name = "nombre" )
    private String nombre;

    @Column( name = "tasa" )
    private Double tasa;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "cantidad" )
    private BigDecimal cantidad;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha" )
    private Date fecha;

    @Column( name = "vigente" )
    private boolean vigente;

    @PostLoad
    private void onPostLoad() {
        nombre = StringUtils.trimToEmpty( nombre );
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

    public Double getTasa() {
        return tasa;
    }

    public void setTasa( Double tasa ) {
        this.tasa = tasa;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad( BigDecimal cantidad ) {
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha( Date fecha ) {
        this.fecha = fecha;
    }

    public boolean isVigente() {
        return vigente;
    }

    public void setVigente( boolean vigente ) {
        this.vigente = vigente;
    }
}

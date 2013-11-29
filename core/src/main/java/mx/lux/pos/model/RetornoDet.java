package mx.lux.pos.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table( name = "retorno_det", schema = "public" )
public class RetornoDet implements Serializable {


    private static final long serialVersionUID = 5738096520490563528L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "retorno_det_seq" )
    @SequenceGenerator( name = "retorno_det_seq", sequenceName = "retorno_det_seq" )
    @Column( name = "id" )
    private Integer id;

    @Column( name = "id_transaccion" )
    private Integer idTransaccion;

    @Column( name = "sku" )
    private Integer sku;

    @Column( name = "cantidad" )
    private Integer cantidad;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "importe" )
    private BigDecimal importe;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSku() {
        return sku;
    }

    public void setSku(Integer sku) {
        this.sku = sku;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Integer getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Integer idTransaccion) {
        this.idTransaccion = idTransaccion;
    }
}

package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table( name = "cotiza_det" )
public class CotizaDet implements Serializable, Comparable<CotizaDet> {

    private static final long serialVersionUID = -7795153915643486388L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "cotiza_det_id_cotiza_det_seq" )
    @Column( name = "id_cotiza_det" )
    @SequenceGenerator( schema = "public", sequenceName = "cotiza_det_id_cotiza_det_seq",
            name = "cotiza_det_id_cotiza_det_seq" )
    private Integer idCotizaDet;

    @Column( name = "id_sucursal", nullable = false )
    private Integer idSucursal;

    @Column( name = "id_cotiza", nullable = false )
    private Integer idCotiza;

    @Column( name = "id_articulo", nullable = false )
    private Integer sku;

    @Column( name = "articulo" )
    private String articulo;

    @Column( name = "color" )
    private String color;

    @Column( name = "cantidad", nullable = false )
    private Integer cantidad;

    @Column( name = "udf1" )
    private String udf1;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod", nullable = false )
    private Date fechaMod;

    @PrePersist
    private void onPrePersist() {
        fechaMod = new Date();
    }

    @PreUpdate
    private void onPreUpdate() {
        fechaMod = new Date();
    }

    @PostLoad
    private void onPostLoad() { }


    public Integer getIdCotizaDet() {
        return this.idCotizaDet;
    }

    public void setIdCotizaDet(Integer pIdCotizaDet) {
        this.idCotizaDet = pIdCotizaDet;
    }

    public Integer getIdSucursal() {
        return this.idSucursal;
    }

    public void setIdSucursal(Integer pIdSucursal) {
        this.idSucursal = pIdSucursal;
    }

    public Integer getIdCotiza() {
        return this.idCotiza;
    }

    public void setIdCotiza(Integer pIdCotiza) {
        this.idCotiza = pIdCotiza;
    }

    public Integer getSku() {
        return this.sku;
    }

    public void setSku(Integer pSku) {
        this.sku = pSku;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String pArticulo) {
        this.articulo = StringUtils.trimToEmpty(pArticulo).toUpperCase();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String pColor) {
        this.color = StringUtils.trimToEmpty(pColor).toUpperCase();
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public void setCantidad( Integer pCantidad ) {
        this.cantidad = pCantidad;
    }

    public String getUdf1() {
        return udf1;
    }

    public void setUdf1(String udf1) {
        this.udf1 = udf1;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod(Date fechaMod) {
        this.fechaMod = fechaMod;
    }

    // Identity Methods
    public int compareTo(CotizaDet pDet) {
        int result = this.getIdCotiza().compareTo(pDet.getIdCotiza());
        if (result == 0) {
            result = this.getSku().compareTo(pDet.getSku());
        }
        return result;
    }

    public boolean equals(Object pObj) {
        boolean result = false;
        if (pObj instanceof CotizaDet) {
            result = this.getIdCotiza().equals(((CotizaDet) pObj).getIdCotiza())
                    && this.getSku().equals(((CotizaDet) pObj).getSku());
        }
        return result;
    }

    public int hashCode() {
        return String.format("%06d:%06d", this.getIdCotiza(), this.getSku()).hashCode();
    }

    public String toString() {
        return String.format("(%06d) Sku:%06d  Cant:%d", this.getIdCotiza(), this.getSku(), this.getCantidad());
    }

}

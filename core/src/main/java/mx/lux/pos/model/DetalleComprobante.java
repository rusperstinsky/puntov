package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table( name = "nota_factura_det", schema = "public" )
public class DetalleComprobante implements Serializable {

    private static final long serialVersionUID = -9072182114975491388L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "nota_factura_det_id_seq" )
    @SequenceGenerator( name = "nota_factura_det_id_seq", sequenceName = "nota_factura_det_id_seq" )
    @Column( name = "id" )
    private Integer id;

    @Column( name = "id_fiscal" )
    private String idFiscal;

    @Column( name = "id_articulo" )
    private String idArticulo;

    @Column( name = "articulo" )
    private String articulo;

    @Column( name = "color" )
    private String color;

    @Column( name = "id_generico" )
    private String idGenerico;

    @Column( name = "descripcion" )
    private String descripcion;

    @Column( name = "cantidad" )
    private String cantidad;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "precio_unitario" )
    private BigDecimal precioUnitario;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "importe" )
    private BigDecimal importe;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod" )
    private Date fechaModificacion;

    @PrePersist
    private void onPrePersist() {
        fechaModificacion = new Date();
    }

    @PreUpdate
    private void onPreUpdate() {
        fechaModificacion = new Date();
    }

    @PostLoad
    private void onPostLoad() {
        idFiscal = StringUtils.trimToEmpty( idFiscal );
        idArticulo = StringUtils.trimToEmpty( idArticulo );
        articulo = StringUtils.trimToEmpty( articulo );
        color = StringUtils.trimToEmpty( color );
        idGenerico = StringUtils.trimToEmpty( idGenerico );
        descripcion = StringUtils.trimToEmpty( descripcion );
        cantidad = StringUtils.trimToEmpty( cantidad );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getIdFiscal() {
        return idFiscal;
    }

    public void setIdFiscal( String idFiscal ) {
        this.idFiscal = idFiscal;
    }

    public String getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo( String idArticulo ) {
        this.idArticulo = idArticulo;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo( String articulo ) {
        this.articulo = articulo;
    }

    public String getColor() {
        return color;
    }

    public void setColor( String color ) {
        this.color = color;
    }

    public String getIdGenerico() {
        return idGenerico;
    }

    public void setIdGenerico( String idGenerico ) {
        this.idGenerico = idGenerico;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion( String descripcion ) {
        this.descripcion = descripcion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad( String cantidad ) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario( BigDecimal precioUnitario ) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte( BigDecimal importe ) {
        this.importe = importe;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion( Date fechaModificacion ) {
        this.fechaModificacion = fechaModificacion;
    }
}

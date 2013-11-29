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
@Table( name = "dev", schema = "public" )
public class Devolucion implements Serializable {

    private static final long serialVersionUID = -2908809749506678285L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "dev_id_seq" )
    @SequenceGenerator( name = "dev_id_seq", sequenceName = "dev_id_seq" )
    @Column( name = "id" )
    private Integer id;

    @Column( name = "id_mod" )
    private Integer idMod;

    @Column( name = "id_pago" )
    private Integer idPago;

    @Column( name = "id_forma_pago", length = 2 )
    private String idFormaPago;

    @Column( name = "id_banco" )
    private Integer idBanco;

    @Column( name = "referencia_devolucion", length = 18 )
    private String referencia;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "monto_devolucion" )
    private BigDecimal monto;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_devolucion", nullable = false )
    private Date fecha;

    @Column( name = "transf" )
    private String transf;

    @Column( name = "tipo", length = 1 )
    private String tipo;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_mod", updatable = false, insertable = false )
    private Modificacion modificacion;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_pago", updatable = false, insertable = false )
    private Pago pago;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_forma_pago", updatable = false, insertable = false )
    private FormaPago formaPago;

    @PrePersist
    private void onPrePersist() {
        fecha = new Date();
    }

    /*@PreUpdate
    private void onPreUpdate() {
        fecha = new Date();
    }*/

    @PostLoad
    private void onPostLoad() {
        idFormaPago = StringUtils.trimToEmpty( idFormaPago );
        referencia = StringUtils.trimToEmpty( referencia );
        transf = StringUtils.trimToEmpty( transf );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public Integer getIdMod() {
        return idMod;
    }

    public void setIdMod( Integer idMod ) {
        this.idMod = idMod;
    }

    public Integer getIdPago() {
        return idPago;
    }

    public void setIdPago( Integer idPago ) {
        this.idPago = idPago;
    }

    public String getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago( String idFormaPago ) {
        this.idFormaPago = idFormaPago;
    }

    public Integer getIdBanco() {
        return idBanco;
    }

    public void setIdBanco( Integer idBanco ) {
        this.idBanco = idBanco;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia( String referencia ) {
        this.referencia = referencia;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto( BigDecimal monto ) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha( Date fecha ) {
        this.fecha = fecha;
    }

    public String getTransf() {
        return transf;
    }

    public void setTransf( String transf ) {
        this.transf = transf;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo( String tipo ) {
        this.tipo = tipo;
    }

    public Modificacion getModificacion() {
        return modificacion;
    }

    public void setModificacion( Modificacion modificacion ) {
        this.modificacion = modificacion;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago( Pago pago ) {
        this.pago = pago;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago( FormaPago formaPago ) {
        this.formaPago = formaPago;
    }
}

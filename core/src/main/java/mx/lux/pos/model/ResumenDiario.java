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
@Table( name = "resumen_diario", schema = "public" )
public class ResumenDiario implements Serializable, Comparable<ResumenDiario> {

    private static final long serialVersionUID = 7156261651094398120L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "resumen_diario_seq" )
    @SequenceGenerator( name = "resumen_diario_seq", sequenceName = "resumen_diario_seq" )
    @Column( name = "id_resumen" )
    private Integer id;

    @Column( name = "term", length = 5 )
    private String idTerminal;

    @Column( name = "facturas" )
    private Integer facturas;

    @Column( name = "vouchers" )
    private Integer vouchers = 0;

    @Column( name = "tipo", length = 3 )
    private String tipo;

    @Column( name = "plan", length = 3 )
    private String plan;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "importe" )
    private BigDecimal importe;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_cierre" )
    private Date fechaCierre;

    @Column( name = "orden", length = 1 )
    private String orden;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod" )
    private Date fechaMod;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "term", referencedColumnName = "descripcion", insertable = false, updatable = false )
    private Terminal terminal;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "tipo", insertable = false, updatable = false )
    private FormaPago formaPago;

    @PrePersist
    private void onPrePersist() {
        fechaMod = new Date();
    }

    @PreUpdate
    private void onPreUpdate() {
        fechaMod = new Date();
    }

    @PostLoad
    private void onPostLoad() {
        idTerminal = StringUtils.trimToEmpty( idTerminal );
        tipo = StringUtils.trimToEmpty( tipo );
        plan = StringUtils.trimToEmpty( plan );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal( Terminal terminal ) {
        this.terminal = terminal;
    }

    public Integer getFacturas() {
        return facturas;
    }

    public void setFacturas( Integer facturas ) {
        this.facturas = facturas;
    }

    public Integer getVouchers() {
        return vouchers;
    }

    public void setVouchers( Integer vouchers ) {
        this.vouchers = vouchers;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago( FormaPago formaPago ) {
        this.formaPago = formaPago;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan( String plan ) {
        this.plan = plan;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte( BigDecimal importe ) {
        this.importe = importe;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre( Date fechaCierre ) {
        this.fechaCierre = fechaCierre;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden( String orden ) {
        this.orden = orden;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod( Date fechaMod ) {
        this.fechaMod = fechaMod;
    }

    public String getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal( String idTerminal ) {
        this.idTerminal = idTerminal;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo( String tipo ) {
        this.tipo = tipo;
    }

    // Identidades
    public int compareTo( ResumenDiario rd ){
        return this.getIdTerminal().compareToIgnoreCase(rd.getIdTerminal());
    }
}

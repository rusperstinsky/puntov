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
@Table( name = "pagos", schema = "public" )
public class Pago implements Serializable {

    private static final long serialVersionUID = 3627038677660169174L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "pagos_id_pago_seq" )
    @SequenceGenerator( name = "pagos_id_pago_seq", sequenceName = "pagos_id_pago_seq" )
    @Column( name = "id_pago" )
    private Integer id;

    @Column( name = "id_factura" )
    private String idFactura;

    @Column( name = "id_banco", length = 18 )
    private String idBanco;

    @Column( name = "id_forma_pago", length = 2 )
    private String idFormaPago;

    @Column( name = "tipo_pago", length = 1 )
    private String tipoPago;

    @Column( name = "referencia_pago" )
    private String referenciaPago;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "monto_pago" )
    private BigDecimal monto;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_pago", nullable = false )
    private Date fecha;

    @Column( name = "id_empleado", length = 13 )
    private String idEmpleado;

    @Column( name = "id_sync", length = 1, nullable = false )
    private String idSync = "1";

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod", nullable = false )
    private Date fechaModificacion;

    @Column( name = "id_mod", length = 13, nullable = false )
    private String idMod = "0";

    @Column( name = "id_sucursal", nullable = false )
    private Integer idSucursal;

    @Column( name = "id_recibo" )
    private String idRecibo;

    @Column( name = "parcialidad", length = 3 )
    private String parcialidad;

    @Column( name = "id_f_pago" )
    private String idFPago;

    @Column( name = "clave_p" )
    private String clave;

    @Column( name = "ref_clave" )
    private String referenciaClave;

    @Column( name = "id_banco_emi", length = 2 )
    private String idBancoEmisor;

    @Column( name = "id_term", length = 30 )
    private String idTerminal;

    @Column( name = "id_plan", length = 20 )
    private String idPlan;

    @Column( name = "confirm", nullable = false )
    private boolean confirmado = true;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "por_dev" )
    private BigDecimal porDevolver;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_factura", insertable = false, updatable = false )
    private NotaVenta notaVenta;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_forma_pago", insertable = false, updatable = false )
    private FormaPago formaPago;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_empleado", insertable = false, updatable = false )
    private Empleado empleado;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_sucursal", insertable = false, updatable = false )
    private Sucursal sucursal;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_f_pago", insertable = false, updatable = false )
    private TipoPago eTipoPago;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_term", insertable = false, updatable = false )
    private Terminal terminal;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_plan", insertable = false, updatable = false )
    private Plan plan;

    @PostLoad
    private void onPostLoad() {
        idFactura = StringUtils.trimToEmpty( idFactura );
        idBanco = StringUtils.trimToEmpty( idBanco );
        idFormaPago = StringUtils.trimToEmpty( idFormaPago );
        referenciaPago = StringUtils.trimToEmpty( referenciaPago );
        idEmpleado = StringUtils.trimToEmpty( idEmpleado );
        idMod = StringUtils.trimToEmpty( idMod );
        idRecibo = StringUtils.trimToEmpty( idRecibo );
        parcialidad = StringUtils.trimToEmpty( parcialidad );
        idFPago = StringUtils.trimToEmpty( idFPago );
        clave = StringUtils.trimToEmpty( clave );
        referenciaClave = StringUtils.trimToEmpty( referenciaClave );
        idBancoEmisor = StringUtils.trimToEmpty( idBancoEmisor );
        idTerminal = StringUtils.trimToEmpty( idTerminal );
        idPlan = StringUtils.trimToEmpty( idPlan );
    }

    @PrePersist
    private void onPrePersist() {
        fecha = new Date();
        fechaModificacion = new Date();
    }

    @PreUpdate
    private void onPreUpdate() {
        fechaModificacion = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura( String idFactura ) {
        this.idFactura = idFactura;
    }

    public String getIdBanco() {
        return idBanco;
    }

    public void setIdBanco( String idBanco ) {
        this.idBanco = idBanco;
    }

    public String getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago( String idFormaPago ) {
        this.idFormaPago = idFormaPago;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago( String tipoPago ) {
        this.tipoPago = tipoPago;
    }

    public String getReferenciaPago() {
        return referenciaPago;
    }

    public void setReferenciaPago( String referenciaPago ) {
        this.referenciaPago = referenciaPago;
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

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado( String idEmpleado ) {
        this.idEmpleado = idEmpleado;
    }

    public String getIdSync() {
        return idSync;
    }

    public void setIdSync( String idSync ) {
        this.idSync = idSync;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion( Date fechaModificacion ) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getIdMod() {
        return idMod;
    }

    public void setIdMod( String idMod ) {
        this.idMod = idMod;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal( Integer idSucursal ) {
        this.idSucursal = idSucursal;
    }

    public String getIdRecibo() {
        return idRecibo;
    }

    public void setIdRecibo( String idRecibo ) {
        this.idRecibo = idRecibo;
    }

    public String getParcialidad() {
        return parcialidad;
    }

    public void setParcialidad( String parcialidad ) {
        this.parcialidad = parcialidad;
    }

    public String getIdFPago() {
        return idFPago;
    }

    public void setIdFPago( String idFPago ) {
        this.idFPago = idFPago;
    }

    public String getClave() {
        return clave;
    }

    public void setClave( String clave ) {
        this.clave = clave;
    }

    public String getReferenciaClave() {
        return referenciaClave;
    }

    public void setReferenciaClave( String referenciaClave ) {
        this.referenciaClave = referenciaClave;
    }

    public String getIdBancoEmisor() {
        return idBancoEmisor;
    }

    public void setIdBancoEmisor( String idBancoEmisor ) {
        this.idBancoEmisor = idBancoEmisor;
    }

    public String getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal( String idTerminal ) {
        this.idTerminal = idTerminal;
    }

    public String getIdPlan() {
        return idPlan;
    }

    public void setIdPlan( String idPlan ) {
        this.idPlan = idPlan;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public void setConfirmado( boolean confirmado ) {
        this.confirmado = confirmado;
    }

    public BigDecimal getPorDevolver() {
        return porDevolver;
    }

    public void setPorDevolver( BigDecimal porDevolver ) {
        this.porDevolver = porDevolver;
    }

    public NotaVenta getNotaVenta() {
        return notaVenta;
    }

    public void setNotaVenta( NotaVenta notaVenta ) {
        this.notaVenta = notaVenta;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago( FormaPago formaPago ) {
        this.formaPago = formaPago;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado( Empleado empleado ) {
        this.empleado = empleado;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal( Sucursal sucursal ) {
        this.sucursal = sucursal;
    }

    public TipoPago geteTipoPago() {
        return eTipoPago;
    }

    public void seteTipoPago( TipoPago eTipoPago ) {
        this.eTipoPago = eTipoPago;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal( Terminal terminal ) {
        this.terminal = terminal;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan( Plan plan ) {
        this.plan = plan;
    }
}

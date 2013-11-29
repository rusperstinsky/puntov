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
@Table( name = "jb_pagos_ext", schema = "public" )
public class PagoExterno implements Serializable {

    private static final long serialVersionUID = -1947483140480209630L;

    @Id
    @Column( name = "id_pago" )
    private Integer id;

    @Column( name = "factura", nullable = false )
    private String factura;

    @Column( name = "id_forma_pago", length = 2 )
    private String idFormaPago;

    @Column( name = "id_banco", length = 40 )
    private String idBanco;

    @Column( name = "refer", length = 18 )
    private String referencia;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "monto" )
    private BigDecimal monto;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha" )
    private Date fecha;

    @Column( name = "id_empleado", length = 5 )
    private String idEmpleado;

    @Column( name = "id_f_pago", length = 5 )
    private String idFPago;

    @Column( name = "clave_p" )
    private String claveP;

    @Column( name = "ref_clave" )
    private String refClave;

    @Column( name = "id_banco_emi", length = 2 )
    private String idBancoEmi;

    @Column( name = "id_term", length = 30 )
    private String idTerminal;

    @Column( name = "id_plan", length = 20 )
    private String idPlan;

    @Column( name = "confirm" )
    private Boolean confirm;

    @Column( name = "id_mod" )
    private String idMod;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "factura", referencedColumnName = "factura", updatable = false, insertable = false )
    private Externo externo;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_forma_pago", updatable = false, insertable = false )
    private FormaPago formaPago;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_empleado", updatable = false, insertable = false )
    private Empleado empleado;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_term", updatable = false, insertable = false )
    private Terminal terminal;

    @PostLoad
    private void onPostLoad() {
        factura = StringUtils.trimToEmpty( factura );
        idFormaPago = StringUtils.trimToEmpty( idFormaPago );
        idBanco = StringUtils.trimToEmpty( idBanco );
        referencia = StringUtils.trimToEmpty( referencia );
        idEmpleado = StringUtils.trimToEmpty( idEmpleado );
        idFPago = StringUtils.trimToEmpty( idFPago );
        claveP = StringUtils.trimToEmpty( claveP );
        refClave = StringUtils.trimToEmpty( refClave );
        idBancoEmi = StringUtils.trimToEmpty( idBancoEmi );
        idTerminal = StringUtils.trimToEmpty( idTerminal );
        idPlan = StringUtils.trimToEmpty( idPlan );
        idMod = StringUtils.trimToEmpty( idMod );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura( String factura ) {
        this.factura = factura;
    }

    public String getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago( String idFormaPago ) {
        this.idFormaPago = idFormaPago;
    }

    public String getIdBanco() {
        return idBanco;
    }

    public void setIdBanco( String idBanco ) {
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

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado( String idEmpleado ) {
        this.idEmpleado = idEmpleado;
    }

    public String getIdFPago() {
        return idFPago;
    }

    public void setIdFPago( String idFPago ) {
        this.idFPago = idFPago;
    }

    public String getClaveP() {
        return claveP;
    }

    public void setClaveP( String claveP ) {
        this.claveP = claveP;
    }

    public String getRefClave() {
        return refClave;
    }

    public void setRefClave( String refClave ) {
        this.refClave = refClave;
    }

    public String getIdBancoEmi() {
        return idBancoEmi;
    }

    public void setIdBancoEmi( String idBancoEmi ) {
        this.idBancoEmi = idBancoEmi;
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

    public Boolean getConfirm() {
        return confirm;
    }

    public void setConfirm( Boolean confirm ) {
        this.confirm = confirm;
    }

    public String getIdMod() {
        return idMod;
    }

    public void setIdMod( String idMod ) {
        this.idMod = idMod;
    }

    public Externo getExterno() {
        return externo;
    }

    public void setExterno( Externo externo ) {
        this.externo = externo;
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

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal( Terminal terminal ) {
        this.terminal = terminal;
    }
}

package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Entity
@Table( name = "nota_venta", schema = "public" )
public class NotaVenta implements Serializable {

    private static final long serialVersionUID = -2336697153151190921L;

    @Id
    @Column( name = "id_factura" )
    private String id;

    @Column( name = "id_empleado", length = 13 )
    private String idEmpleado;

    @Column( name = "id_cliente" )
    private Integer idCliente;

    @Column( name = "id_convenio", length = 5 )
    private String idConvenio;

    @Column( name = "id_rep_venta" )
    private Integer idRepVenta;

    @Column( name = "tipo_nota_venta", length = 1 )
    private String tipoNotaVenta;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_rec_ord" )
    private Date fechaRecOrd;

    @Column( name = "tipo_cli", length = 1, nullable = false )
    private String tipoCli = "N";

    @Column( name = "f_expide_factura" )
    private boolean fExpideFactura;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "venta_total" )
    private BigDecimal ventaTotal;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "venta_neta" )
    private BigDecimal ventaNeta;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "suma_pagos" )
    private BigDecimal sumaPagos;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_hora_factura", nullable = false )
    private Date fechaHoraFactura;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_prometida" )
    private Date fechaPrometida;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_entrega" )
    private Date fechaEntrega;

    @Column( name = "f_armazon_cli" )
    private boolean fArmazonCli;

    @Column( name = "por100_descuento" )
    private Integer por100Descuento = 0;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "monto_descuento" )
    private BigDecimal montoDescuento;

    @Column( name = "tipo_descuento", length = 1 )
    private String tipoDescuento;

    @Column( name = "id_empleado_descto", length = 13 )
    private String idEmpleadoDescto;

    @Column( name = "f_resumen_notas_mo" )
    private boolean fResumenNotasMo;

    @Column( name = "s_factura", length = 1, nullable = false )
    private String sFactura = "N";

    @Column( name = "numero_orden" )
    private Integer numeroOrden;

    @Column( name = "tipo_entrega", length = 1 )
    private String tipoEntrega;

    @Column( name = "observaciones_nv" )
    private String observacionesNv;

    @Column( name = "id_sync", length = 1, nullable = false )
    private String idSync = "1";

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod", nullable = false )
    private Date fechaMod;

    @Column( name = "id_mod", length = 13, nullable = false )
    private String idMod = "0";

    @Column( name = "id_sucursal", nullable = false )
    private Integer idSucursal;

    @Column( name = "factura" )
    private String factura;

    @Column( name = "cant_lente" )
    private String cantLente;

    @Column( name = "udf2" )
    private String udf2;

    @Column( name = "udf3" )
    private String udf3;

    @Column( name = "udf4" )
    private String udf4;

    @Column( name = "udf5" )
    private String udf5;

    @Column( name = "suc_dest" )
    private String sucDest;

    @Column( name = "t_deduc", length = 2 )
    private String tDeduc;

    @Column( name = "receta" )
    private Integer receta;

    @Column( name = "emp_entrego", length = 13 )
    private String empEntrego;

    @Column( name = "lc" )
    private String lc;

    @Temporal( TemporalType.TIME )
    @Column( name = "hora_entrega" )
    private Date horaEntrega;

    @Column( name = "descuento" )
    private boolean descuento;

    @Column( name = "pol_ent" )
    private boolean polEnt;

    @Column( name = "tipo_venta", nullable = false )
    private String tipoVenta = "N";

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "poliza" )
    private BigDecimal poliza;

    @OneToMany( fetch = FetchType.EAGER )
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_factura", insertable = false, updatable = false )
    private Set<DetalleNotaVenta> detalles = new HashSet<DetalleNotaVenta>();

    @OneToMany( fetch = FetchType.EAGER )
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_factura", insertable = false, updatable = false )
    private Set<Pago> pagos = new HashSet<Pago>();

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_cliente", insertable = false, updatable = false )
    private Cliente cliente;

    @OneToMany( fetch = FetchType.EAGER )
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_factura", insertable = false, updatable = false )
    private List<OrdenPromDet> OrdenPromDet;

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
    @JoinColumn( name = "receta", insertable = false, updatable = false )
    private Examen examen;

    @PrePersist
    protected void onPrePersist() {
        fechaHoraFactura = new Date();
        fechaMod = new Date();
        detalles = null;
        pagos = null;
        cliente = null;
        empleado = null;
        sucursal = null;
    }

    @PreUpdate
    protected void onPreUpdate() {
        fechaMod = new Date();
    }

    @PostLoad
    void onPostLoad() {
        idEmpleado = StringUtils.trimToEmpty( idEmpleado );
        idConvenio = StringUtils.trimToEmpty( idConvenio );
        tipoNotaVenta = StringUtils.trimToEmpty( tipoNotaVenta );
        tipoCli = StringUtils.trimToEmpty( tipoCli );
        tipoDescuento = StringUtils.trimToEmpty( tipoDescuento );
        idEmpleadoDescto = StringUtils.trimToEmpty( idEmpleadoDescto );
        sFactura = StringUtils.trimToEmpty( sFactura );
        tipoEntrega = StringUtils.trimToEmpty( tipoEntrega );
        observacionesNv = StringUtils.trimToEmpty( observacionesNv );
        idSync = StringUtils.trimToEmpty( idSync );
        idMod = StringUtils.trimToEmpty( idMod );
        factura = StringUtils.trimToEmpty( factura );
        cantLente = StringUtils.trimToEmpty( cantLente );
        udf2 = StringUtils.trimToEmpty( udf2 );
        udf3 = StringUtils.trimToEmpty( udf3 );
        udf4 = StringUtils.trimToEmpty( udf4 );
        udf5 = StringUtils.trimToEmpty( udf5 );
        sucDest = StringUtils.trimToEmpty( sucDest );
        tDeduc = StringUtils.trimToEmpty( tDeduc );
        empEntrego = StringUtils.trimToEmpty( empEntrego );
        lc = StringUtils.trimToEmpty( lc );
        tipoVenta = StringUtils.trimToEmpty( tipoVenta );
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado( String idEmpleado ) {
        this.idEmpleado = idEmpleado;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente( Integer idCliente ) {
        this.idCliente = idCliente;
    }

    public String getIdConvenio() {
        return idConvenio;
    }

    public void setIdConvenio( String idConvenio ) {
        this.idConvenio = idConvenio;
    }

    public Integer getIdRepVenta() {
        return idRepVenta;
    }

    public void setIdRepVenta( Integer idRepVenta ) {
        this.idRepVenta = idRepVenta;
    }

    public String getTipoNotaVenta() {
        return tipoNotaVenta;
    }

    public void setTipoNotaVenta( String tipoNotaVenta ) {
        this.tipoNotaVenta = tipoNotaVenta;
    }

    public Date getFechaRecOrd() {
        return fechaRecOrd;
    }

    public void setFechaRecOrd( Date fechaRecOrd ) {
        this.fechaRecOrd = fechaRecOrd;
    }

    public String getTipoCli() {
        return tipoCli;
    }

    public void setTipoCli( String tipoCli ) {
        this.tipoCli = tipoCli;
    }

    public boolean isfExpideFactura() {
        return fExpideFactura;
    }

    public void setfExpideFactura( boolean fExpideFactura ) {
        this.fExpideFactura = fExpideFactura;
    }

    public BigDecimal getVentaTotal() {
        return ventaTotal;
    }

    public void setVentaTotal( BigDecimal ventaTotal ) {
        this.ventaTotal = ventaTotal;
    }

    public BigDecimal getVentaNeta() {
        return ventaNeta;
    }

    public void setVentaNeta( BigDecimal ventaNeta ) {
        this.ventaNeta = ventaNeta;
    }

    public BigDecimal getSumaPagos() {
        return sumaPagos;
    }

    public void setSumaPagos( BigDecimal sumaPagos ) {
        this.sumaPagos = sumaPagos;
    }

    public Date getFechaHoraFactura() {
        return fechaHoraFactura;
    }

    public void setFechaHoraFactura( Date fechaHoraFactura ) {
        this.fechaHoraFactura = fechaHoraFactura;
    }

    public Date getFechaPrometida() {
        return fechaPrometida;
    }

    public void setFechaPrometida( Date fechaPrometida ) {
        this.fechaPrometida = fechaPrometida;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega( Date fechaEntrega ) {
        this.fechaEntrega = fechaEntrega;
    }

    public boolean isfArmazonCli() {
        return fArmazonCli;
    }

    public void setfArmazonCli( boolean fArmazonCli ) {
        this.fArmazonCli = fArmazonCli;
    }

    public Integer getPor100Descuento() {
        return por100Descuento;
    }

    public void setPor100Descuento( Integer por100Descuento ) {
        this.por100Descuento = por100Descuento;
    }

    public BigDecimal getMontoDescuento() {
        return montoDescuento;
    }

    public void setMontoDescuento( BigDecimal montoDescuento ) {
        this.montoDescuento = montoDescuento;
    }

    public String getTipoDescuento() {
        return tipoDescuento;
    }

    public void setTipoDescuento( String tipoDescuento ) {
        this.tipoDescuento = tipoDescuento;
    }

    public String getIdEmpleadoDescto() {
        return idEmpleadoDescto;
    }

    public void setIdEmpleadoDescto( String idEmpleadoDescto ) {
        this.idEmpleadoDescto = idEmpleadoDescto;
    }

    public boolean isfResumenNotasMo() {
        return fResumenNotasMo;
    }

    public void setfResumenNotasMo( boolean fResumenNotasMo ) {
        this.fResumenNotasMo = fResumenNotasMo;
    }

    public String getsFactura() {
        return sFactura;
    }

    public void setsFactura( String sFactura ) {
        this.sFactura = sFactura;
    }

    public Integer getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden( Integer numeroOrden ) {
        this.numeroOrden = numeroOrden;
    }

    public String getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega( String tipoEntrega ) {
        this.tipoEntrega = tipoEntrega;
    }

    public String getObservacionesNv() {
        return observacionesNv;
    }

    public void setObservacionesNv( String observacionesNv ) {
        this.observacionesNv = observacionesNv;
    }

    public String getIdSync() {
        return idSync;
    }

    public void setIdSync( String idSync ) {
        this.idSync = idSync;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod( Date fechaMod ) {
        this.fechaMod = fechaMod;
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

    public String getFactura() {
        return factura;
    }

    public void setFactura( String factura ) {
        this.factura = factura;
    }

    public String getCantLente() {
        return cantLente;
    }

    public void setCantLente( String cantLente ) {
        this.cantLente = cantLente;
    }

    public String getUdf2() {
        return udf2;
    }

    public void setUdf2( String udf2 ) {
        this.udf2 = udf2;
    }

    public String getUdf3() {
        return udf3;
    }

    public void setUdf3( String udf3 ) {
        this.udf3 = udf3;
    }

    public String getUdf4() {
        return udf4;
    }

    public void setUdf4( String udf4 ) {
        this.udf4 = udf4;
    }

    public String getUdf5() {
        return udf5;
    }

    public void setUdf5( String udf5 ) {
        this.udf5 = udf5;
    }

    public String getSucDest() {
        return sucDest;
    }

    public void setSucDest( String sucDest ) {
        this.sucDest = sucDest;
    }

    public String gettDeduc() {
        return tDeduc;
    }

    public void settDeduc( String tDeduc ) {
        this.tDeduc = tDeduc;
    }

    public Integer getReceta() {
        return receta;
    }

    public void setReceta( Integer receta ) {
        this.receta = receta;
    }

    public String getEmpEntrego() {
        return empEntrego;
    }

    public void setEmpEntrego( String empEntrego ) {
        this.empEntrego = empEntrego;
    }

    public String getLc() {
        return lc;
    }

    public void setLc( String lc ) {
        this.lc = lc;
    }

    public Date getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega( Date horaEntrega ) {
        this.horaEntrega = horaEntrega;
    }

    public boolean isDescuento() {
        return descuento;
    }

    public void setDescuento( boolean descuento ) {
        this.descuento = descuento;
    }

    public boolean isPolEnt() {
        return polEnt;
    }

    public void setPolEnt( boolean polEnt ) {
        this.polEnt = polEnt;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta( String tipoVenta ) {
        this.tipoVenta = tipoVenta;
    }

    public BigDecimal getPoliza() {
        return poliza;
    }

    public void setPoliza( BigDecimal poliza ) {
        this.poliza = poliza;
    }

    public Set<DetalleNotaVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles( Set<DetalleNotaVenta> detalles ) {
        this.detalles = detalles;
    }

    public Set<Pago> getPagos() {
        return pagos;
    }

    public void setPagos( Set<Pago> pagos ) {
        this.pagos = pagos;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente( Cliente cliente ) {
        this.cliente = cliente;
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

    public Examen getExamen() {
        return examen;
    }

    public void setExamen( Examen examen ) {
        this.examen = examen;
    }

    public List<OrdenPromDet> getOrdenPromDet() {
        return OrdenPromDet;
    }

    public void setOrdenPromDet( List<OrdenPromDet> ordenPromDet ) {
        OrdenPromDet = ordenPromDet;
    }
}

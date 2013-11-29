package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table( name = "cierre_diario", schema = "public" )
public class CierreDiario implements Serializable {

    private static final long serialVersionUID = 7757045467265402140L;

    @Id
    @Temporal( TemporalType.DATE )
    @Column( name = "fecha" )
    private Date fecha;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_btn" )
    private Date fechaCierre;

    @Temporal( TemporalType.TIME )
    @Column( name = "hora_cierre" )
    private Date horaCierre;

    @Column( name = "estado", length = 2, nullable = false )
    private String estado;

    @Column( name = "observaciones" )
    private String observaciones;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "venta_neta" )
    private BigDecimal ventaNeta;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "venta_bruta" )
    private BigDecimal ventaBruta;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "modificaciones" )
    private BigDecimal modificaciones;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "cancelaciones" )
    private BigDecimal cancelaciones;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "ingreso_bruto" )
    private BigDecimal ingresoBruto;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "devoluciones" )
    private BigDecimal devoluciones;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "ingreso_neto" )
    private BigDecimal ingresoNeto;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "efectivo_recibido" )
    private BigDecimal efectivoRecibido;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "efectivo_externos" )
    private BigDecimal efectivoExternos;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "efectivo_devoluciones" )
    private BigDecimal efectivoDevoluciones;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "efectivo_neto" )
    private BigDecimal efectivoNeto;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "usd_recibido" )
    private BigDecimal dolaresRecibidos;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "usd_devoluciones" )
    private BigDecimal dolaresDevoluciones;

    @Column( name = "factura_inicial" )
    private String facturaInicial;

    @Column( name = "factura_final" )
    private String facturaFinal;

    @Column( name = "ventas_cantidad" )
    private Integer cantidadVentas = 0;

    @Column( name = "modificaciones_cantidad" )
    private Integer cantidadModificaciones = 0;

    @Column( name = "cancelaciones_cantidad" )
    private Integer cantidadCancelaciones = 0;

    @PostLoad
    private void onPostLoad() {
        estado = StringUtils.trimToEmpty( estado );
        observaciones = StringUtils.trimToEmpty( observaciones );
        facturaInicial = StringUtils.trimToEmpty( facturaInicial );
        facturaFinal = StringUtils.trimToEmpty( facturaFinal );
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha( Date fecha ) {
        this.fecha = fecha;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre( Date fechaCierre ) {
        this.fechaCierre = fechaCierre;
    }

    public Date getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre( Date horaCierre ) {
        this.horaCierre = horaCierre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado( String estado ) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones( String observaciones ) {
        this.observaciones = observaciones;
    }

    public BigDecimal getVentaNeta() {
        return ventaNeta;
    }

    public void setVentaNeta( BigDecimal ventaNeta ) {
        this.ventaNeta = ventaNeta;
    }

    public BigDecimal getVentaBruta() {
        return ventaBruta;
    }

    public void setVentaBruta( BigDecimal ventaBruta ) {
        this.ventaBruta = ventaBruta;
    }

    public BigDecimal getModificaciones() {
        return modificaciones;
    }

    public void setModificaciones( BigDecimal modificaciones ) {
        this.modificaciones = modificaciones;
    }

    public BigDecimal getCancelaciones() {
        return cancelaciones;
    }

    public void setCancelaciones( BigDecimal cancelaciones ) {
        this.cancelaciones = cancelaciones;
    }

    public BigDecimal getIngresoBruto() {
        return ingresoBruto;
    }

    public void setIngresoBruto( BigDecimal ingresoBruto ) {
        this.ingresoBruto = ingresoBruto;
    }

    public BigDecimal getDevoluciones() {
        return devoluciones;
    }

    public void setDevoluciones( BigDecimal devoluciones ) {
        this.devoluciones = devoluciones;
    }

    public BigDecimal getIngresoNeto() {
        return ingresoNeto;
    }

    public void setIngresoNeto( BigDecimal ingresoNeto ) {
        this.ingresoNeto = ingresoNeto;
    }

    public BigDecimal getEfectivoRecibido() {
        return efectivoRecibido;
    }

    public void setEfectivoRecibido( BigDecimal efectivoRecibido ) {
        this.efectivoRecibido = efectivoRecibido;
    }

    public BigDecimal getEfectivoExternos() {
        return efectivoExternos;
    }

    public void setEfectivoExternos( BigDecimal efectivoExternos ) {
        this.efectivoExternos = efectivoExternos;
    }

    public BigDecimal getEfectivoDevoluciones() {
        return efectivoDevoluciones;
    }

    public void setEfectivoDevoluciones( BigDecimal efectivoDevoluciones ) {
        this.efectivoDevoluciones = efectivoDevoluciones;
    }

    public BigDecimal getEfectivoNeto() {
        return efectivoNeto;
    }

    public void setEfectivoNeto( BigDecimal efectivoNeto ) {
        this.efectivoNeto = efectivoNeto;
    }

    public BigDecimal getDolaresRecibidos() {
        return dolaresRecibidos;
    }

    public void setDolaresRecibidos( BigDecimal dolaresRecibidos ) {
        this.dolaresRecibidos = dolaresRecibidos;
    }

    public BigDecimal getDolaresDevoluciones() {
        return dolaresDevoluciones;
    }

    public void setDolaresDevoluciones( BigDecimal dolaresDevoluciones ) {
        this.dolaresDevoluciones = dolaresDevoluciones;
    }

    public String getFacturaInicial() {
        return facturaInicial;
    }

    public void setFacturaInicial( String facturaInicial ) {
        this.facturaInicial = facturaInicial;
    }

    public String getFacturaFinal() {
        return facturaFinal;
    }

    public void setFacturaFinal( String facturaFinal ) {
        this.facturaFinal = facturaFinal;
    }

    public Integer getCantidadVentas() {
        return cantidadVentas;
    }

    public void setCantidadVentas( Integer cantidadVentas ) {
        this.cantidadVentas = cantidadVentas;
    }

    public Integer getCantidadModificaciones() {
        return cantidadModificaciones;
    }

    public void setCantidadModificaciones( Integer cantidadModificaciones ) {
        this.cantidadModificaciones = cantidadModificaciones;
    }

    public Integer getCantidadCancelaciones() {
        return cantidadCancelaciones;
    }

    public void setCantidadCancelaciones( Integer cantidadCancelaciones ) {
        this.cantidadCancelaciones = cantidadCancelaciones;
    }
}

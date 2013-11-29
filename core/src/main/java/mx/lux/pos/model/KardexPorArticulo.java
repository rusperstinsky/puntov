package mx.lux.pos.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class KardexPorArticulo {

    private Date fecha;
    private String folio;
    private String referencia;
    private Integer entrada;
    private Integer salida;
    private Integer saldoInicio;
    private Integer saldoFinal;
    private Integer existenciaInicial;
    private Integer existenciaFinal;
    private String tipoTransaccion;
    private String empleado;

    public KardexPorArticulo( TransInvDetalle transInvDetalle, String idFactura ) {

        fecha = transInvDetalle.getTransInv().getFecha();
        folio = transInvDetalle.getIdTipoTrans()+"."+transInvDetalle.getFolio().toString();
        referencia = idFactura;
        salida = 0;
        entrada = 0;
        saldoInicio = 0;
        saldoFinal = 0;
        existenciaInicial = 0;
        existenciaFinal = 0;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha( Date fecha ) {
        this.fecha = fecha;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio( String folio ) {
        this.folio = folio;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia( String referencia ) {
        this.referencia = referencia;
    }

    public Integer getEntrada() {
        return entrada;
    }

    public void setEntrada( Integer entrada ) {
        this.entrada = entrada;
    }

    public Integer getSalida() {
        return salida;
    }

    public void setSalida( Integer salida ) {
        this.salida = salida;
    }

    public Integer getSaldoInicio() {
        return saldoInicio;
    }

    public void setSaldoInicio( Integer saldoInicio ) {
        this.saldoInicio = saldoInicio;
    }

    public Integer getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal( Integer saldoFinal ) {
        this.saldoFinal = saldoFinal;
    }

    public Integer getExistenciaInicial() {
        return existenciaInicial;
    }

    public void setExistenciaInicial( Integer existenciaInicial ) {
        this.existenciaInicial = existenciaInicial;
    }

    public Integer getExistenciaFinal() {
        return existenciaFinal;
    }

    public void setExistenciaFinal( Integer existenciaFinal ) {
        this.existenciaFinal = existenciaFinal;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion( String tipoTransaccion ) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado( String empleado ) {
        this.empleado = empleado;
    }
}

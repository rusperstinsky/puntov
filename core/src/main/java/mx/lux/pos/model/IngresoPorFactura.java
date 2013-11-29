package mx.lux.pos.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.List;

public class IngresoPorFactura {

    private String idFactura;
    private Integer idArticulo;
    private String marca;
    private String tipo;
    private String color;
    private String empleado;
    private String descripcion;
    private BigDecimal montoPago;
    private BigDecimal montoPagoIVA;
    private BigDecimal montoPagoSinIVA;
    private BigDecimal montoDevolucion;
    private BigDecimal acumulaPago;
    private BigDecimal acumulaPagoIva;
    private BigDecimal acumulaDevolucion;
    private BigDecimal total;
    private BigDecimal iva;
    private BigDecimal promedio;
    private Date fechaPago;
    private Date fechaCancelacion;
    private Integer modId;
    private Integer existencia;
    private Set<DetalleNotaVenta> lstDetalles;
    private BigDecimal contador;
    private String paciente;
    private List<String> lstIdsArticulos;
    private BigDecimal sumaMonto;
    private Boolean mostrarArticulos;
    private String idGenerico;
    private Integer noFacturas;
    private static final String TAG_CANCELADO = "T";

    public IngresoPorFactura( String idFactura ) {
        this.idFactura = idFactura;
        montoPago = BigDecimal.valueOf( 0 );
        montoDevolucion = BigDecimal.valueOf( 0 );
        acumulaDevolucion = BigDecimal.valueOf( 0 );
        acumulaPago = BigDecimal.valueOf( 0 );
        montoPagoIVA = BigDecimal.valueOf( 0 );
        montoPagoSinIVA = BigDecimal.valueOf( 0 );
        iva = BigDecimal.valueOf( 0 );
        contador = BigDecimal.valueOf( 0 );
        acumulaPagoIva = BigDecimal.valueOf( 0 );
        idArticulo = 0;
        existencia = 0;
        noFacturas = 0;
        lstIdsArticulos = new ArrayList<String>();
        sumaMonto = BigDecimal.ZERO;
        mostrarArticulos = true;
    }

    public IngresoPorFactura( BigDecimal monto ) {
        montoPago = monto;
        montoPago = BigDecimal.valueOf( 0 );
        montoDevolucion = BigDecimal.valueOf( 0 );
        acumulaDevolucion = BigDecimal.valueOf( 0 );
        acumulaPago = BigDecimal.valueOf( 0 );
        montoPagoIVA = BigDecimal.valueOf( 0 );
        montoPagoSinIVA = BigDecimal.valueOf( 0 );
        iva = BigDecimal.valueOf( 0 );
        contador = BigDecimal.valueOf( 0 );
        acumulaPagoIva = BigDecimal.valueOf( 0 );
    }

    public void AcumulaPago( BigDecimal pago, Date fecha ) {
        montoPago = montoPago.add( pago );
        fechaPago = fecha;
    }


    public void AcumulaCancelaciones( BigDecimal pago, Date fecha ) {
        montoPago = montoPago.add( pago ).negate();
        fechaPago = fecha;
    }

    public void AcumulaNotasCredito( BigDecimal pago, Date fecha ) {
        montoPago = montoPago.subtract( pago );
        fechaPago = fecha;
    }

    public void AcumulaCancelacionesSinIva( BigDecimal pago, Date fecha, BigDecimal montoIva, Integer piezas ) {
        montoPagoSinIVA = pago.negate();
        acumulaPago = (acumulaPago.add( pago )).negate();
        montoPagoIVA = (acumulaPago.add( acumulaPago.multiply( montoIva ) )).negate();
        fechaPago = fecha;
        iva = montoIva;
        contador = new BigDecimal( contador.intValue() - 1 );
        sumaMonto.add( montoPagoSinIVA );
        promedio = sumaMonto.divide( contador, 10, RoundingMode.CEILING );
        noFacturas = noFacturas-1;
        existencia = existencia-piezas;
    }

    public void AcumulaNotasCreditoSinIva( BigDecimal pago, Date fecha, BigDecimal montoIva, Boolean isNotaCredito ) {
        montoPagoSinIVA = montoPagoSinIVA.subtract(pago);
        acumulaPago = (acumulaPago.subtract( pago ));
        montoPagoIVA = (acumulaPago.add( acumulaPago.multiply( montoIva ) )).negate();
        fechaPago = fecha;
        iva = montoIva;
        if( isNotaCredito ){
            contador = new BigDecimal( contador.intValue() - 1 );
        }
        if( contador.compareTo(BigDecimal.ZERO) == 0 ){
            promedio = BigDecimal.ZERO;
        } else {
            promedio = montoPagoSinIVA.divide( contador, 10, RoundingMode.CEILING );
        }
    }

    public void AcumulaPagosinIva( BigDecimal pago, Date fecha, BigDecimal montoIva, Integer piezas, BigDecimal noFacturas ) {
        montoPagoSinIVA = pago;
        acumulaPago = acumulaPago.add( pago );
        montoPagoIVA = acumulaPago.add( acumulaPago.multiply( montoIva ) );
        fechaPago = fecha;
        iva = montoIva;
        contador = new BigDecimal( contador.intValue() + 1 );
        sumaMonto = sumaMonto.add( montoPagoSinIVA );
        promedio = montoPagoSinIVA.divide( contador, 10, RoundingMode.CEILING );
        existencia = piezas;
        this.noFacturas = this.noFacturas+1;
    }

    public void AcumulaDevolucion( BigDecimal devolucion ) {
        montoDevolucion = montoDevolucion.add( devolucion );
    }

    public void AcumulaFacturas( Modificacion modificacion ) {

        fechaPago = modificacion.getNotaVenta().getFechaHoraFactura();
        empleado = modificacion.getEmpleado().getNombreCompleto();
        fechaCancelacion = modificacion.getFecha();
        modId = modificacion.getId();
        idFactura = modificacion.getNotaVenta().getFactura();
        lstDetalles = modificacion.getNotaVenta().getDetalles();
    }

    public void AcumulaMarca( Articulo articulo ) {
        idArticulo = articulo.getId();
        marca = articulo.getArticulo();
        this.color = articulo.getCodigoColor();
        descripcion = articulo.getDescripcion();
        acumulaPago = articulo.getPrecio();
        idGenerico = articulo.getIdGenerico();
        this.existencia = articulo.getCantExistencia();
    }

    public void AcumulaMarcaResumido( Articulo articulo ) {
        marca = articulo.getMarca();
        this.existencia = existencia+articulo.getCantExistencia();
    }


    public void AcumulaMarcas( boolean mostrarArticulos, String idArticulo, DetalleNotaVenta notaVenta, BigDecimal importe, Double iva, Date fecha, String articulo, String descripcion ) {
        fechaPago = fecha;
        marca = articulo;
        montoPago = montoPago.add( importe.multiply( new BigDecimal(notaVenta.getCantidadFac()) ) );
        acumulaPago = acumulaPago.add( montoPago );
        montoPagoIVA = new BigDecimal( montoPago.doubleValue()/( 1+iva ) );
        acumulaPagoIva = acumulaPagoIva.add( montoPagoIVA );
        tipo = descripcion;
        Boolean esNotaCrecdito = false;
        for( Pago pago : notaVenta.getNotaVenta().getPagos() ){
            /*if( "NOT".equalsIgnoreCase(pago.getIdFPago() ) ){
                esNotaCrecdito = true;
            }*/
        }
        //if( !esNotaCrecdito ){
            for(int i = 0; i < notaVenta.getCantidadFac().intValue(); i++){
                lstIdsArticulos.add( idArticulo );
            }
        //}
        contador = contador.add(new BigDecimal(notaVenta.getCantidadFac()));
        this.mostrarArticulos = mostrarArticulos;
    }

    public void AcumulaMarcasCan( DetalleNotaVenta detalles, String idArticulo, BigDecimal importe, Double iva, Date fecha, String articulo, String descripcion ) {
        fechaPago = fecha;
        marca = articulo;
        montoPago = montoPago.subtract( importe.multiply( new BigDecimal(detalles.getCantidadFac()) ) );
        acumulaPago = acumulaPago.add( montoPago );
        montoPagoIVA = new BigDecimal( montoPago.doubleValue()/( 1+iva ) );
        acumulaPagoIva = acumulaPagoIva.add( montoPagoIVA );
        tipo = descripcion;
        lstIdsArticulos.add( idArticulo );
        contador = contador.subtract( new BigDecimal(detalles.getCantidadFac()) );
    }

    public void AcumulaPagosMarcasNotasCredito( Integer cantArticulos, DetalleNotaVenta notaVenta, BigDecimal importe, Double iva, Date fecha, String articulo, String descripcion ) {
        fechaPago = fecha;
        marca = articulo;
        montoPago = montoPago.subtract( notaVenta.getPrecioUnitFinal().multiply(new BigDecimal(notaVenta.getCantidadFac())) );
        acumulaPago = acumulaPago.add( montoPago );
        montoPagoIVA = new BigDecimal( montoPago.doubleValue()/( 1+iva ) );
        acumulaPagoIva = acumulaPagoIva.add( montoPagoIVA );
    }

    public void AcumulaArticulosMarcasNotasCredito( Integer cantArticulos, DetalleNotaVenta notaVenta, BigDecimal importe, Double iva, Date fecha, String articulo, String descripcion ) {
        contador = contador.subtract( new BigDecimal(notaVenta.getCantidadFac()) );
    }

    public void AcumulaVentasOpto( NotaVenta venta ) {
        fechaPago = venta.getFechaHoraFactura();
        idFactura = venta.getFactura();
        montoPago = venta.getVentaNeta();
        paciente = venta.getCliente().getNombreCompleto();
    }

    public void AcumulaVentasOptoMayor( NotaVenta venta ) {
        fechaPago = venta.getFechaHoraFactura();
        idFactura = venta.getFactura();
        //montoPago = venta.getVentaNeta();
        paciente = venta.getCliente().getNombreCompleto();
    }

    public String getIdFactura() {
        return idFactura;
    }

    public BigDecimal getMontoPago() {
        return montoPago;
    }

    public BigDecimal getMontoDevolucion() {
        return montoDevolucion;
    }

    public BigDecimal getAcumulaPago() {
        return acumulaPago;
    }

    public BigDecimal getAcumulaDevolucion() {
        return acumulaDevolucion;
    }

    public BigDecimal getTotal() {

        return total;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public BigDecimal getMontoPagoIVA() {
        return montoPagoIVA;
    }

    public BigDecimal getMontoPagoSinIVA() {
        return montoPagoSinIVA;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public Date getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion( Date fechaCancelacion ) {
        this.fechaCancelacion = fechaCancelacion;
    }

    public Integer getModId() {
        return modId;
    }

    public void setModId( Integer modId ) {
        this.modId = modId;
    }

    public Set<DetalleNotaVenta> getLstDetalles() {
        return lstDetalles;
    }

    public void setLstDetalles( Set<DetalleNotaVenta> lstDetalles ) {
        this.lstDetalles = lstDetalles;
    }

    public void setIdFactura( String idFactura ) {
        this.idFactura = idFactura;
    }

    public void setMontoPago( BigDecimal montoPago ) {
        this.montoPago = montoPago;
    }

    public void setMontoPagoIVA( BigDecimal montoPagoIVA ) {
        this.montoPagoIVA = montoPagoIVA;
    }

    public void setMontoPagoSinIVA( BigDecimal montoPagoSinIVA ) {
        this.montoPagoSinIVA = montoPagoSinIVA;
    }

    public void setMontoDevolucion( BigDecimal montoDevolucion ) {
        this.montoDevolucion = montoDevolucion;
    }

    public void setAcumulaPago( BigDecimal acumulaPago ) {
        this.acumulaPago = acumulaPago;
    }

    public void setAcumulaDevolucion( BigDecimal acumulaDevolucion ) {
        this.acumulaDevolucion = acumulaDevolucion;
    }

    public void setTotal( BigDecimal total ) {
        this.total = total;
    }

    public void setIva( BigDecimal iva ) {
        this.iva = iva;
    }

    public void setFechaPago( Date fechaPago ) {
        this.fechaPago = fechaPago;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca( String marca ) {
        this.marca = marca;
    }

    public BigDecimal getContador() {
        return contador;
    }

    public void setContador( BigDecimal contador ) {
        this.contador = contador;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo( String tipo ) {
        this.tipo = tipo;
    }

    public BigDecimal getAcumulaPagoIva() {
        return acumulaPagoIva;
    }

    public void setAcumulaPagoIva( BigDecimal acumulaPagoIva ) {
        this.acumulaPagoIva = acumulaPagoIva;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente( String paciente ) {
        this.paciente = paciente;
    }

    public String getColor() {
        return color;
    }

    public void setColor( String color ) {
        this.color = color;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion( String descripcion ) {
        this.descripcion = descripcion;
    }

    public Integer getExistencia() {
        return existencia;
    }

    public void setExistencia( Integer existencia ) {
        this.existencia = existencia;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo( Integer idArticulo ) {
        this.idArticulo = idArticulo;
    }

    public List<String> getLstIdsArticulos() {
        return lstIdsArticulos;
    }

    public void setLstIdsArticulos( List<String> lstIdsArticulos ) {
        this.lstIdsArticulos = lstIdsArticulos;
    }

    public BigDecimal getPromedio() {
        return promedio;
    }

    public void setPromedio( BigDecimal promedio ) {
        this.promedio = promedio;
    }

    public Boolean getMostrarArticulos() {
        return mostrarArticulos;
    }

    public void setMostrarArticulos( Boolean mostrarArticulos ) {
        this.mostrarArticulos = mostrarArticulos;
    }

    public String getIdGenerico() {
        return idGenerico;
    }

    public void setIdGenerico(String idGenerico) {
        this.idGenerico = idGenerico;
    }

    public BigDecimal getSumaMonto() {
        return sumaMonto;
    }

    public void setSumaMonto(BigDecimal sumaMonto) {
        this.sumaMonto = sumaMonto;
    }

    public Integer getNoFacturas() {
        return noFacturas;
    }

    public void setNoFacturas(Integer noFacturas) {
        this.noFacturas = noFacturas;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }
}

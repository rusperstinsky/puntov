package mx.lux.pos.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IngresoPorVendedor {

    private String idEmpleado;
    private String nombre;
    private String tipo;
    private List<IngresoPorFactura> pagos;
    private List<IngresoPorFactura> devoluciones;
    private BigDecimal totalPagos;
    private BigDecimal totalPagosIva;
    private BigDecimal totalDevoluciones;
    private BigDecimal total;
    private BigDecimal promedio;
    private BigDecimal noFacturas;
    private BigDecimal contador;
    private Integer facturas;
    private Integer piezas;
    private Boolean mostrarArticulos;

    double porcentaje = 100.0;

    public IngresoPorVendedor( String idVendedor ) {
        idEmpleado = idVendedor;
        pagos = new ArrayList<IngresoPorFactura>();
        devoluciones = new ArrayList<IngresoPorFactura>();
        totalPagos = BigDecimal.valueOf( 0 );
        totalDevoluciones = BigDecimal.valueOf( 0 );
        totalPagosIva = BigDecimal.valueOf( 0 );
        contador = BigDecimal.valueOf( 0 );
        noFacturas = BigDecimal.valueOf( 0 );
        mostrarArticulos = true;
        piezas = 0;
    }

    public void AcumulaPago( String idFactura, BigDecimal monto, Double iva, Date FechaPago, Integer piezas ) {
        IngresoPorFactura ingreso = FindOrCreate( pagos, idFactura );
        double ivaMonto = (iva/porcentaje);
        noFacturas = noFacturas.add( new BigDecimal( 1 ) );
        ingreso.AcumulaPago( new BigDecimal(monto.doubleValue()/( 1+ivaMonto )), FechaPago );
        ingreso.AcumulaPagosinIva( monto, FechaPago, new BigDecimal(ivaMonto), piezas, noFacturas );
        totalPagos = ( totalPagos.add( new BigDecimal(monto.doubleValue()/( 1+ivaMonto )) ) );
        totalPagosIva = totalPagosIva.add( monto );
        contador = contador.add( new BigDecimal( 1 ) );
        this.piezas = this.piezas+piezas;
    }


    public void AcumulaCancelaciones( String idFactura, BigDecimal monto, Double iva, Date FechaPago, Integer piezas ) {
        IngresoPorFactura ingreso = FindOrCreate( pagos, idFactura );
        double ivaMonto = (iva/porcentaje);
        ingreso.AcumulaCancelaciones( new BigDecimal(monto.doubleValue()/( 1+ivaMonto )), FechaPago );
        ingreso.AcumulaCancelacionesSinIva( monto, FechaPago, new BigDecimal(ivaMonto), piezas );
        totalPagosIva = totalPagosIva.subtract( monto );
        contador = contador.subtract( new BigDecimal( 1 ) );
        this.piezas = this.piezas-piezas;
    }

    public void AcumulaNotasCredito( String idFactura, BigDecimal monto, Double iva, Date FechaPago, Boolean isNotaCredito ) {
        IngresoPorFactura ingreso = FindOrCreate( pagos, idFactura );
        double ivaMonto = (iva/porcentaje);
        ingreso.AcumulaNotasCredito( new BigDecimal( monto.doubleValue() / ( 1 + ivaMonto ) ), FechaPago );
        ingreso.AcumulaNotasCreditoSinIva( monto, FechaPago, new BigDecimal( ivaMonto ), isNotaCredito );
        totalPagosIva = totalPagosIva.subtract( monto );
        if( isNotaCredito ){
            contador = contador.subtract( new BigDecimal( 1 ) );
        }
    }

    public void AcumulaDevolucion( String idFactura, BigDecimal monto, Double iva ) {
        IngresoPorFactura ingreso = FindOrCreate( devoluciones, idFactura );
        double ivaMonto = iva/porcentaje;
        ingreso.AcumulaDevolucion( new BigDecimal(monto.doubleValue()/( 1+ ivaMonto ) ) );
        totalDevoluciones = ( totalDevoluciones.add( new BigDecimal(monto.doubleValue()/( 1+ivaMonto ) ) ) );
        totalPagosIva = totalPagosIva.subtract( monto );
    }

    public void AcumulaPagos( boolean mostrarArticulos, String idArticulo, DetalleNotaVenta notaVenta, Date fecha, String articulo, BigDecimal monto,
                              Double iva, String descripcion ) {
        IngresoPorFactura ingreso = FindorCreate( pagos, articulo );
        ingreso.AcumulaMarcas( mostrarArticulos, idArticulo, notaVenta, monto, iva, fecha, articulo, descripcion );
        totalPagos = totalPagos.add( monto.multiply( new BigDecimal(notaVenta.getCantidadFac() )) );
        totalPagosIva = new BigDecimal( totalPagos.doubleValue()/( 1+iva ) );
        contador = contador.add( new BigDecimal(notaVenta.getCantidadFac()) );
        this.mostrarArticulos = mostrarArticulos;
    }

    public void AcumulaPagosCan( DetalleNotaVenta detalles, String idArticulo, Date fecha, String articulo, BigDecimal monto,
                              Double iva, String descripcion ) {
        IngresoPorFactura ingreso = FindorCreate( pagos, articulo );
        ingreso.AcumulaMarcasCan( detalles, idArticulo, monto, iva, fecha, articulo, descripcion );
        totalPagos = totalPagos.subtract( monto.multiply( new BigDecimal(detalles.getCantidadFac() )) );
        totalPagosIva = new BigDecimal( totalPagos.doubleValue()/( 1+iva ) );
        contador = contador.subtract( new BigDecimal(detalles.getCantidadFac()) );
    }


    public void AcumulaPagosNotaCredito( Integer cantArticulos, DetalleNotaVenta notaVenta, Date fecha, String articulo, BigDecimal monto,
                                 Double iva, String descripcion ) {
        IngresoPorFactura ingreso = FindorCreate( pagos, articulo );
        ingreso.AcumulaPagosMarcasNotasCredito( cantArticulos, notaVenta, monto, iva, fecha, articulo, descripcion );
        totalPagos = totalPagos.subtract( notaVenta.getPrecioUnitFinal().multiply(new BigDecimal(notaVenta.getCantidadFac())) );
        totalPagosIva = new BigDecimal( totalPagos.doubleValue()/( 1+iva ) );
    }

    public void AcumulaArticulosNotaCredito( Integer cantArticulos, DetalleNotaVenta notaVenta, Date fecha, String articulo, BigDecimal monto,
                                         Double iva, String descripcion ) {
        IngresoPorFactura ingreso = FindorCreate( pagos, articulo );
        ingreso.AcumulaArticulosMarcasNotasCredito( cantArticulos, notaVenta, monto, iva, fecha, articulo, descripcion );
        contador = contador.subtract( new BigDecimal(notaVenta.getCantidadFac()) );
    }

    public void AcumulaOptometrista( NotaVenta venta, BigDecimal total,
                                     Integer noFacturas1, Double iva ) {
        facturas = noFacturas1;
        totalPagos = total;
        IngresoPorFactura ingreso = FindOrCreate( pagos, venta.getFactura() );
        ingreso.AcumulaVentasOpto( venta );
        totalPagos = totalPagos.add( venta.getVentaNeta() );
        totalPagosIva = totalPagos.subtract( totalPagos.multiply( new BigDecimal(
                iva ) ) );
        contador = contador.add( new BigDecimal( 1 ) );
    }

    public void AcumulaOptometristaMayor( NotaVenta venta, Integer factura ) {
        totalPagos = totalPagos.add( venta.getVentaNeta() );
        facturas = factura;
        checkMaxSale( pagos, venta );
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado( String idEmpleado ) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre( String nombre ) {
        this.nombre = nombre;
    }

    public List<IngresoPorFactura> getPagos() {
        return pagos;
    }

    public List<IngresoPorFactura> getDevoluciones() {
        return devoluciones;
    }

    public BigDecimal getTotalPagos() {
        return totalPagos;
    }

    public BigDecimal getTotalDevoluciones() {
        return totalDevoluciones;
    }

    protected IngresoPorFactura FindOrCreate(
            List<IngresoPorFactura> lstIngresos, String idFactura ) {
        IngresoPorFactura found = null;

        for ( IngresoPorFactura ingresos : lstIngresos ) {
            if ( ingresos.getIdFactura().equals( idFactura ) ) {
                found = ingresos;
                break;
            }
        }
        if ( found == null ) {
            found = new IngresoPorFactura( idFactura );
            lstIngresos.add( found );
        }
        return found;
    }

    public BigDecimal getTotal() {
        if ( totalPagos != null && totalDevoluciones != null ) {
            total = totalPagos.subtract( totalDevoluciones );
        }
        return total;
    }

    public BigDecimal getNoFacturas() {
        noFacturas = contador;
        return noFacturas;
    }

    public BigDecimal getPromedio() {
        if ( noFacturas.compareTo( noFacturas ) > 0 ) {
            promedio = totalPagos.divide( noFacturas );
        }
        return promedio;
    }

    protected IngresoPorFactura FindorCreate(
            List<IngresoPorFactura> lstIngresos, String articulo ) {
        IngresoPorFactura found = null;

        for ( IngresoPorFactura ingresos : lstIngresos ) {
            if ( articulo.equals( ingresos.getMarca() ) ) {
                found = ingresos;
                break;
            }
        }
        if ( found == null ) {
            found = new IngresoPorFactura( articulo );
            lstIngresos.add( found );
        }
        return found;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo( String tipo ) {
        this.tipo = tipo;
    }

    public BigDecimal getContador() {
        return contador;
    }

    public void setContador( BigDecimal contador ) {
        this.contador = contador;
    }

    public void setPagos( List<IngresoPorFactura> pagos ) {
        this.pagos = pagos;
    }

    public BigDecimal getTotalPagosIva() {
        return totalPagosIva;
    }

    public void setTotalPagosIva( BigDecimal totalPagosIva ) {
        this.totalPagosIva = totalPagosIva;
    }

    public void setNoFacturas( BigDecimal noFacturas ) {
        this.noFacturas = noFacturas;
    }

    public Integer getFacturas() {
        return facturas;
    }

    public void setFacturas( Integer facturas ) {
        this.facturas = facturas;
    }

    public void setDevoluciones(List<IngresoPorFactura> devoluciones) {
        this.devoluciones = devoluciones;
    }

    public void setTotalPagos(BigDecimal totalPagos) {
        this.totalPagos = totalPagos;
    }

    public void setTotalDevoluciones(BigDecimal totalDevoluciones) {
        this.totalDevoluciones = totalDevoluciones;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setPromedio(BigDecimal promedio) {
        this.promedio = promedio;
    }

    public Integer getPiezas() {
        return piezas;
    }

    public void setPiezas(Integer piezas) {
        this.piezas = piezas;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    protected void checkMaxSale( List<IngresoPorFactura> lstIngresos,
                                 NotaVenta venta ) {
        IngresoPorFactura ingresos = new IngresoPorFactura( venta.getFactura() );
        lstIngresos.add( ingresos );
        if ( lstIngresos.get( 0 ).getMontoPago().compareTo( venta.getVentaNeta() ) < 0 ) {
            lstIngresos.clear();
            IngresoPorFactura ingreso = FindOrCreate( pagos, venta.getFactura() );
            ingreso.AcumulaVentasOpto( venta );
        }
    }

    public Boolean getMostrarArticulos() {
        return mostrarArticulos;
    }

    public void setMostrarArticulos( Boolean mostrarArticulos ) {
        this.mostrarArticulos = mostrarArticulos;
    }
}

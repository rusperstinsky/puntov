package mx.lux.pos.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class FacturasPorEmpleado {

    private String idEmpleado;
    private String nombre;
    private List<IngresoPorFactura> facturasVendedor;
    private Integer ventas;
    private Integer cancelaciones;
    private Integer porcentaje;
    private String articulo;
    private String marca;
    private String color;
    private String tipo;
    private String idGenerico;
    private Integer cantidad;
    private Integer facturas;
    private BigDecimal importe;
    private BigDecimal importeSinIva;
    private BigDecimal promedio;
    private Integer idArticulo;
    private List<String> lstArticulos;
    private Boolean noMostrarArticulos;
    private Boolean esCancelacion;

    public FacturasPorEmpleado( String idempleado ) {
        this.idEmpleado = idempleado;
        ventas = 0;
        cancelaciones = 0;
        porcentaje = 0;
        facturasVendedor = new ArrayList<IngresoPorFactura>();
        cantidad = 0;
        importe = BigDecimal.ZERO;
        promedio = BigDecimal.ZERO;
        importeSinIva = BigDecimal.ZERO;
        lstArticulos = new ArrayList<String>();
        noMostrarArticulos = false;
        facturas = 0;
        esCancelacion = false;
    }

    public void AcumulaVentas() {
        ventas = ventas + 1;
    }

    public void AcumulaCancelaciones( Modificacion modificacion ) {
        cancelaciones = cancelaciones + 1;
        IngresoPorFactura factura = FindOrCreate( facturasVendedor, modificacion.getNotaVenta().getFactura() );
        factura.AcumulaFacturas(modificacion);
    }

    public void AcumulaMarcas( String brand, Articulo articulo ) {
        marca = brand;
        cantidad = cantidad + articulo.getCantExistencia();
        IngresoPorFactura article = FindOrCreate( facturasVendedor, articulo.getId().toString() );
        article.AcumulaMarca( articulo );
    }

    public void AcumulaMarcasResumido( String brand, Articulo articulo ) {
        if("A".equalsIgnoreCase(articulo.getIdGenerico())){
            idGenerico = "Armazones";
        } else if("E".equalsIgnoreCase(articulo.getIdGenerico())){
            idGenerico = "Accesorios Varios";
        }
        cantidad = cantidad + articulo.getCantExistencia();
        IngresoPorFactura article = FindOrCreate( facturasVendedor, articulo.getMarca() );
        article.AcumulaMarcaResumido( articulo );
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

    public Integer getVentas() {
        return ventas;
    }

    public void setVentas( Integer ventas ) {
        this.ventas = ventas;
    }

    public Integer getCancelaciones() {
        return cancelaciones;
    }

    public void setCancelaciones( Integer cancelaciones ) {
        this.cancelaciones = cancelaciones;
    }

    public Integer getPorcentaje() {
        if ( ventas != null && ventas > 0 && cancelaciones != null ) {
            porcentaje = ( cancelaciones * 100 ) / ventas;
        }
        return porcentaje;
    }

    public void setPorcentaje( Integer porcentaje ) {
        this.porcentaje = porcentaje;
    }

    public List<IngresoPorFactura> getFacturasVendedor() {
        return facturasVendedor;
    }

    public void setFacturasVendedor( List<IngresoPorFactura> facturasVendedor ) {
        this.facturasVendedor = facturasVendedor;
    }

    protected IngresoPorFactura FindOrCreate( List<IngresoPorFactura> lstIngresos, String idFactura ) {
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


    public void AcumulaArticulos( boolean mostrarArticulos, DetalleNotaVenta ventas, Double iva, String descArticulo ) {
        Boolean esNotaCredito = false;
        for(Pago pago : ventas.getNotaVenta().getPagos()){
            /*if( "NOT".equalsIgnoreCase(pago.getIdFPago()) ){
                esNotaCredito = true;
            }*/
        }
        if( !esNotaCredito ){
            for(int i = 0; i<ventas.getCantidadFac().intValue(); i++
                    ){
                lstArticulos.add( descArticulo );
            }
        }
        facturas = facturas + 1;
        cantidad = cantidad + ventas.getCantidadFac().intValue();
        articulo = ventas.getArticulo().getArticulo();
        marca = ventas.getArticulo().getMarca();
        color = ventas.getArticulo().getCodigoColor();
        tipo = ventas.getArticulo().getIdGenTipo();
        importe = importe.add( ventas.getPrecioUnitFinal().multiply(new BigDecimal(ventas.getCantidadFac())) );
        importeSinIva = new BigDecimal(importe.doubleValue()/( 1+iva ) );
        idArticulo = ventas.getArticulo().getId();
        this.noMostrarArticulos = mostrarArticulos;
        promedio = importe.divide(new BigDecimal(cantidad), 10, RoundingMode.CEILING );
    }

    public void AcumulaCancelaciones( DetalleNotaVenta ventas, Double iva ){
        articulo = ventas.getArticulo().getDescripcion();
        idArticulo = ventas.getArticulo().getId();
        importe = (importe.add( ventas.getPrecioUnitFinal() )).negate();
        cantidad = (cantidad+ventas.getCantidadFac().intValue())*-1;
    }


    public void AcumulaArticulosCancelados( boolean noMostrarArticulos, DetalleNotaVenta ventas, Double iva, String descArticulo ) {
        articulo = ventas.getArticulo().getArticulo();
        marca = ventas.getArticulo().getMarca();
        color = ventas.getArticulo().getCodigoColor();
        tipo = ventas.getArticulo().getIdGenTipo();
        importe = importe.subtract( ventas.getPrecioUnitFinal().multiply( new BigDecimal(ventas.getCantidadFac()) ) );
        importeSinIva = new BigDecimal(importe.doubleValue()/( 1+iva ) );
        cantidad = cantidad - ventas.getCantidadFac().intValue();
        idArticulo = ventas.getArticulo().getId();
        lstArticulos.add( descArticulo+" CANCELADO" );
        this.noMostrarArticulos = noMostrarArticulos;
        esCancelacion = true;
    }

    public void AcumulaMontoNotasCredito( DetalleNotaVenta ventas, BigDecimal monto, Double iva ) {
        importe = importe.subtract( ventas.getPrecioUnitFinal().multiply(new BigDecimal(ventas.getCantidadFac())) );
        importeSinIva = new BigDecimal(importe.doubleValue()/( 1+iva ) );
    }

    public void AcumulaArticulosNotasCredito( DetalleNotaVenta ventas, BigDecimal monto, Double iva ) {
        cantidad = cantidad - ventas.getCantidadFac().intValue();
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad( Integer cantidad ) {
        this.cantidad = cantidad;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte( BigDecimal importe ) {
        this.importe = importe;
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

    public String getMarca() {
        return marca;
    }

    public void setMarca( String marca ) {
        this.marca = marca;
    }

    public BigDecimal getImporteSinIva() {
        return importeSinIva;
    }

    public void setImporteSinIva( BigDecimal importeSinIva ) {
        this.importeSinIva = importeSinIva;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo( String tipo ) {
        this.tipo = tipo;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo( Integer idArticulo ) {
        this.idArticulo = idArticulo;
    }

    public List<String> getLstArticulos() {
        return lstArticulos;
    }

    public void setLstArticulos( List<String> lstArticulos ) {
        this.lstArticulos = lstArticulos;
    }

    public Boolean getNoMostrarArticulos() {
        return noMostrarArticulos;
    }

    public void setNoMostrarArticulos( Boolean noMostrarArticulos ) {
        this.noMostrarArticulos = noMostrarArticulos;
    }

    public String getIdGenerico() {
        return idGenerico;
    }

    public void setIdGenerico(String idGenerico) {
        this.idGenerico = idGenerico;
    }

    public Integer getFacturas() {
        return facturas;
    }

    public void setFacturas(Integer facturas) {
        this.facturas = facturas;
    }

    public BigDecimal getPromedio() {
        return promedio;
    }

    public void setPromedio(BigDecimal promedio) {
        this.promedio = promedio;
    }

    public Boolean getEsCancelacion() {
        return esCancelacion;
    }

    public void setEsCancelacion(Boolean esCancelacion) {
        this.esCancelacion = esCancelacion;
    }
}

package mx.lux.pos.model;

import java.math.BigDecimal;

public class VentasPorDiaGenericos {

    private String generico;
    private BigDecimal genMontoConDescuento;
    private BigDecimal genMontoTotal;
    private BigDecimal genMontoDescuento;
    private Integer genContadorArt;

    BigDecimal porcentaje = new BigDecimal( 100 );

    /*public VentasPorDiaGenericos( String generico ) {
        this.generico = generico;
        montoConDescuento = BigDecimal.valueOf( 0 );
        montoTotal = BigDecimal.valueOf( 0 );
        montoDescuento = BigDecimal.valueOf( 0 );
        contadorArt = 0;

    }

    public void acumulaArticulosPorgenericos( DetalleNotaVenta detalleNotaVenta ) {
        contadorArt = contadorArt+detalleNotaVenta.getCantidadFac().intValue();
        BigDecimal precio = detalleNotaVenta.getPrecioUnitLista().multiply( new BigDecimal(detalleNotaVenta.getCantidadFac()) );
        montoConDescuento = montoConDescuento.add( precio );
        montoTotal = montoTotal.add( detalleNotaVenta.getNotaVenta().getVentaTotal() );
        for( OrdenPromDet promo : detalleNotaVenta.getNotaVenta().getOrdenPromDet() ){
            montoDescuento = montoDescuento.add( promo.getDescuentoMonto() );
        }
    }

    public String getGenerico() {
        return generico;
    }

    public void setGenerico( String generico ) {
        this.generico = generico;
    }

    public BigDecimal getMontoConDescuento() {
        return montoConDescuento;
    }

    public void setMontoConDescuento( BigDecimal montoConDescuento ) {
        this.montoConDescuento = montoConDescuento;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal( BigDecimal montoTotal ) {
        this.montoTotal = montoTotal;
    }

    public BigDecimal getMontoDescuento() {
        return montoDescuento;
    }

    public void setMontoDescuento( BigDecimal montoDescuento ) {
        this.montoDescuento = montoDescuento;
    }

    public Integer getContadorArt() {
        return contadorArt;
    }

    public void setContadorArt( Integer contadorArt ) {
        this.contadorArt = contadorArt;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje( BigDecimal porcentaje ) {
        this.porcentaje = porcentaje;
    }*/
}

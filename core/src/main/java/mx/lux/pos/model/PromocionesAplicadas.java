package mx.lux.pos.model;

import java.math.BigDecimal;
import java.util.Date;

public class PromocionesAplicadas {

    private Date fecha;
    private String factura;
    private String idArticulo;
    private String articulo;
    private BigDecimal importeLista;
    private BigDecimal importeDesc;
    private Double porcentajeDesc;
    private BigDecimal importeTotal;


    public String getArticulo() {
        return articulo;
    }

    public void setArticulo( String articulo ) {
        this.articulo = articulo;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura( String factura ) {
        this.factura = factura;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha( Date fecha ) {
        this.fecha = fecha;
    }

    public String getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo( String idArticulo ) {
        this.idArticulo = idArticulo;
    }

    public BigDecimal getImporteDesc() {
        return importeDesc;
    }

    public void setImporteDesc( BigDecimal importeDesc ) {
        this.importeDesc = importeDesc;
    }

    public BigDecimal getImporteLista() {
        return importeLista;
    }

    public void setImporteLista( BigDecimal importeLista ) {
        this.importeLista = importeLista;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal( BigDecimal importeTotal ) {
        this.importeTotal = importeTotal;
    }

    public Double getPorcentajeDesc() {
        return porcentajeDesc;
    }

    public void setPorcentajeDesc( Double porcentajeDesc ) {
        this.porcentajeDesc = porcentajeDesc;
    }
}

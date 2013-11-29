package mx.lux.pos.model;

import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.util.StringUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.text.NumberFormat;

public class ResumenCierre {

    private String factura;
    private Date fecha;
    private List<DetalleIngresoPorDia> lstPagos;
    private BigDecimal totalDolares;
    private BigDecimal totalDolaresEnPesos;
    private String totalDolaresYPesos;


    public ResumenCierre(Date fecha) {
        this.factura = factura;
        this.fecha = fecha;
        lstPagos = new ArrayList<DetalleIngresoPorDia>();
        totalDolares = BigDecimal.ZERO;
        totalDolaresEnPesos = BigDecimal.ZERO;
    }

    public void acumulaIngresosPorDia(NotaVenta notaVenta) {
        NumberFormat formatter = new DecimalFormat("$#,##0.00");
        NumberFormat format = new DecimalFormat("#,##0.00");
        DetalleIngresoPorDia detalle = FindOrCreate(lstPagos, notaVenta.getFactura());
        if (new ArrayList<Pago>(notaVenta.getPagos()).size() > 0) {
            for (Pago pago : notaVenta.getPagos()) {
                if (pago.getIdFPago().equalsIgnoreCase("EFD")) {
                    totalDolaresEnPesos = totalDolaresEnPesos.add(pago.getMonto());
                    try {
                        if (StringUtils.trimToEmpty(pago.getIdPlan()).length() > 0) {
                            totalDolares = totalDolares.add(new BigDecimal(NumberFormat.getInstance().parse(pago.getIdPlan()).doubleValue()));
                        }
                    } catch (ParseException e) {
                    }
                    totalDolaresYPesos = String.format("%s (%s)", formatter.format(totalDolaresEnPesos), format.format(totalDolares));
                }
            }
            detalle.AcumulaPagosCierre(new ArrayList<Pago>(notaVenta.getPagos()), notaVenta.getVentaTotal(), notaVenta.getFechaHoraFactura(), totalDolaresYPesos);
        }
        Collections.sort(lstPagos, new Comparator<DetalleIngresoPorDia>() {
            @Override
            public int compare(DetalleIngresoPorDia o1, DetalleIngresoPorDia o2) {
                return o1.getFactura().compareToIgnoreCase(o2.getFactura());
            }
        });
    }

    public void acumulaDevolucionesPorDia(Devolucion devolucion) {
        NumberFormat formatter = new DecimalFormat("$#,##0.00");
        NumberFormat format = new DecimalFormat("#,##0.00");
        DetalleIngresoPorDia detalle = FindOrCreate( lstPagos, devolucion.getPago().getNotaVenta().getFactura() );
        if (devolucion != null) {
            if (devolucion.getIdFormaPago().equalsIgnoreCase("EFD")) {
                totalDolaresEnPesos = totalDolaresEnPesos.add(devolucion.getMonto());
                totalDolaresYPesos = String.format("%s", formatter.format(totalDolaresEnPesos) );
            }
            detalle.AcumulaDevolucionesCierre(devolucion, devolucion.getPago().getNotaVenta().getVentaTotal(), devolucion.getPago().getNotaVenta().getFechaHoraFactura(), totalDolaresYPesos);
        }
        Collections.sort(lstPagos, new Comparator<DetalleIngresoPorDia>() {
            @Override
            public int compare(DetalleIngresoPorDia o1, DetalleIngresoPorDia o2) {
                return o1.getFactura().compareToIgnoreCase(o2.getFactura());
            }
        });
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<DetalleIngresoPorDia> getLstPagos() {
        return lstPagos;
    }

    public void setLstPagos(List<DetalleIngresoPorDia> lstPagos) {
        this.lstPagos = lstPagos;
    }

    public BigDecimal getTotalDolares() {
        return totalDolares;
    }

    public void setTotalDolares(BigDecimal totalDolares) {
        this.totalDolares = totalDolares;
    }

    protected DetalleIngresoPorDia FindOrCreate(List<DetalleIngresoPorDia> lstIngresos, String idFactura) {
        DetalleIngresoPorDia found = null;

        for (DetalleIngresoPorDia ingresos : lstIngresos) {
            if (ingresos.getFactura().equals(idFactura)) {
                found = ingresos;
                break;
            }
        }
        if (found == null) {
            found = new DetalleIngresoPorDia(idFactura);
            lstIngresos.add(found);
        }
        return found;
    }

    public BigDecimal getTotalDolaresEnPesos() {
        return totalDolaresEnPesos;
    }

    public void setTotalDolaresEnPesos(BigDecimal totalDolaresEnPesos) {
        this.totalDolaresEnPesos = totalDolaresEnPesos;
    }

    public String getTotalDolaresYPesos() {
        return totalDolaresYPesos;
    }

    public void setTotalDolaresYPesos(String totalDolaresYPesos) {
        this.totalDolaresYPesos = totalDolaresYPesos;
    }
}

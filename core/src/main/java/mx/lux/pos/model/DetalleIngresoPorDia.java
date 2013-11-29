package mx.lux.pos.model;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public class DetalleIngresoPorDia {

    private String factura;
    private BigDecimal montoPago;
    private BigDecimal pagoEf;
    private BigDecimal pagoEfUs;
    private BigDecimal pagoTN;
    private BigDecimal pagoTD;
    private String pagoTDConDolares;
    private BigDecimal pagoTR;
    private BigDecimal pagoOtros;
    private BigDecimal montoTotal;
    private List<Pago> lstPagos;
    private Date fecha;
    private String terminal;
    private String plan;
    private String totalDolares;

    BigDecimal porcentaje = new BigDecimal(100);

    public DetalleIngresoPorDia(String factura) {
        this.factura = factura;
        montoPago = BigDecimal.valueOf(0);
        montoTotal = BigDecimal.valueOf(0);
        pagoEf = BigDecimal.valueOf(0);
        pagoEfUs = BigDecimal.valueOf(0);
        pagoTN = BigDecimal.valueOf(0);
        pagoTD = BigDecimal.valueOf(0);
        pagoTR = BigDecimal.valueOf(0);
        pagoOtros = BigDecimal.valueOf(0);
        fecha = new Date();
    }

    public void AcumulaPagos(List<Pago> lstPagos, BigDecimal ventaTotal, Boolean cancelada) {
        for (Pago pago : lstPagos) {
            if (pago.getIdFPago().equalsIgnoreCase("EFM")) {
                pagoEf = pagoEf.add(pago.getMonto());
            } else if (pago.getIdFPago().equalsIgnoreCase("EFD")) {
                pagoEfUs = pagoEfUs.add(pago.getMonto());
            } else if (pago.getIdFPago().equalsIgnoreCase("TCM") || pago.getIdFormaPago().equalsIgnoreCase("TDM")) {
                pagoTN = pagoTN.add(pago.getMonto());
            } else if (pago.getIdFPago().equalsIgnoreCase("TCD") || pago.getIdFormaPago().equalsIgnoreCase("TDD")) {
                pagoTD = pagoTD.add(pago.getMonto());
            } else if (pago.getIdFPago().equalsIgnoreCase("TR")) {
                pagoTR = pagoTR.add(pago.getMonto());
            } else if (!pago.getIdFPago().equalsIgnoreCase("EFM") && !pago.getIdFPago().equalsIgnoreCase("EFD")
                    && !pago.getIdFPago().equalsIgnoreCase("TCM") && !pago.getIdFPago().equalsIgnoreCase("TDM")
                    && !pago.getIdFPago().equalsIgnoreCase("TCD") && !pago.getIdFPago().equalsIgnoreCase("TDD")) {
                pagoOtros = pagoOtros.add(pago.getMonto());
            }
            montoTotal = montoTotal.add(pago.getMonto());
            if (cancelada) {
                pagoEf = pagoEf.negate();
                pagoEfUs = pagoEfUs.negate();
                pagoTN = pagoTN.negate();
                pagoTD = pagoTD.negate();
                pagoTR = pagoTR.negate();
                pagoOtros = pagoOtros.negate();
                montoTotal = montoTotal.negate();

            }
        }
        montoPago = ventaTotal;
        if (cancelada) {
            montoPago = montoPago.negate();
        }
    }


    public void AcumulaPagosCierre(List<Pago> lstPagos, BigDecimal ventaTotal, Date fecha, String totalDolares) {
        NumberFormat formatter = new DecimalFormat("$#,##0.00");
        this.fecha = fecha;
        for (Pago pago : lstPagos) {
            if (pago.getIdFPago().equalsIgnoreCase("EFM")) {
                pagoEf = pagoEf.add(pago.getMonto());
            } else if (pago.getIdFPago().equalsIgnoreCase("EFD")) {
                pagoEfUs = pagoEfUs.add(pago.getMonto());
                pagoTDConDolares = String.format("%s (%s)", formatter.format(pagoEfUs.doubleValue()), pago.getIdPlan());
            } else if (pago.getIdFPago().equalsIgnoreCase("TCM") || pago.getIdFormaPago().equalsIgnoreCase("TDM")) {
                pagoTN = pagoTN.add(pago.getMonto());
            } else if (pago.getIdFPago().equalsIgnoreCase("TCD") || pago.getIdFormaPago().equalsIgnoreCase("TDD")) {
                pagoTD = pagoTD.add(pago.getMonto());
            } else if (pago.getIdFPago().equalsIgnoreCase("TR")) {
                pagoTR = pagoTR.add(pago.getMonto());
            } else if (!pago.getIdFPago().equalsIgnoreCase("EFM") && !pago.getIdFPago().equalsIgnoreCase("EFD")
                    && !pago.getIdFPago().equalsIgnoreCase("TCM") && !pago.getIdFPago().equalsIgnoreCase("TDM")
                    && !pago.getIdFPago().equalsIgnoreCase("TCD") && !pago.getIdFPago().equalsIgnoreCase("TDD")) {
                pagoOtros = pagoOtros.add(pago.getMonto());
            }
            montoTotal = montoTotal.add(pago.getMonto());
            if (pago.getTerminal() != null) {
                terminal = pago.getTerminal().getDescripcion();
            }
            plan = pago.getIdPlan();
            if (StringUtils.trimToEmpty(pagoTDConDolares).length() <= 0) {
                pagoTDConDolares = "$0.00";
            }
        }
        this.totalDolares = totalDolares;
        montoPago = ventaTotal;

    }


    public void AcumulaDevolucionesCierre(Devolucion devolucion, BigDecimal ventaTotal, Date fecha, String totalDolares) {
        NumberFormat formatter = new DecimalFormat("$#,##0.00");
        this.fecha = fecha;
        if (devolucion.getIdFormaPago().equalsIgnoreCase("EFM")) {
            pagoEf = pagoEf.add(devolucion.getMonto());
        } else if (devolucion.getPago().getIdFPago().equalsIgnoreCase("EFD")) {
            pagoEfUs = pagoEfUs.add(devolucion.getMonto());
            pagoTDConDolares = String.format("%s", formatter.format(pagoEfUs.doubleValue()));
        } else if (devolucion.getPago().getIdFPago().equalsIgnoreCase("TCM") || devolucion.getPago().getIdFormaPago().equalsIgnoreCase("TDM")) {
            pagoTN = pagoTN.add(devolucion.getMonto());
        } else if (devolucion.getPago().getIdFPago().equalsIgnoreCase("TCD") || devolucion.getPago().getIdFormaPago().equalsIgnoreCase("TDD")) {
            pagoTD = pagoTD.add(devolucion.getMonto());
        } else if (devolucion.getPago().getIdFPago().equalsIgnoreCase("TR")) {
            pagoTR = pagoTR.add(devolucion.getMonto());
        } else if (!devolucion.getPago().getIdFPago().equalsIgnoreCase("EFM") && !devolucion.getPago().getIdFPago().equalsIgnoreCase("EFD")
                && !devolucion.getPago().getIdFPago().equalsIgnoreCase("TCM") && !devolucion.getPago().getIdFPago().equalsIgnoreCase("TDM")
                && !devolucion.getPago().getIdFPago().equalsIgnoreCase("TCD") && !devolucion.getPago().getIdFPago().equalsIgnoreCase("TDD")) {
            pagoOtros = pagoOtros.add(devolucion.getMonto());
        }
        montoTotal = montoTotal.add(devolucion.getMonto());
        if (devolucion.getPago().getTerminal() != null) {
            terminal = devolucion.getPago().getTerminal().getDescripcion();
        }
        plan = devolucion.getPago().getIdPlan();
        if (StringUtils.trimToEmpty(pagoTDConDolares).length() <= 0) {
            pagoTDConDolares = "$0.00";
        }
        this.totalDolares = totalDolares;
        montoPago = ventaTotal;

    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public BigDecimal getMontoPago() {
        return montoPago;
    }

    public void setMontoPago(BigDecimal montoPago) {
        this.montoPago = montoPago;
    }

    public BigDecimal getPagoEf() {
        return pagoEf;
    }

    public void setPagoEf(BigDecimal pagoEf) {
        this.pagoEf = pagoEf;
    }

    public BigDecimal getPagoEfUs() {
        return pagoEfUs;
    }

    public void setPagoEfUs(BigDecimal pagoEfUs) {
        this.pagoEfUs = pagoEfUs;
    }

    public BigDecimal getPagoTN() {
        return pagoTN;
    }

    public void setPagoTN(BigDecimal pagoTN) {
        this.pagoTN = pagoTN;
    }

    public BigDecimal getPagoTD() {
        return pagoTD;
    }

    public void setPagoTD(BigDecimal pagoTD) {
        this.pagoTD = pagoTD;
    }

    public BigDecimal getPagoTR() {
        return pagoTR;
    }

    public void setPagoTR(BigDecimal pagoTR) {
        this.pagoTR = pagoTR;
    }

    public BigDecimal getPagoOtros() {
        return pagoOtros;
    }

    public void setPagoOtros(BigDecimal pagoOtros) {
        this.pagoOtros = pagoOtros;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public List<Pago> getLstPagos() {
        return lstPagos;
    }

    public void setLstPagos(List<Pago> lstPagos) {
        this.lstPagos = lstPagos;
    }

    public String getPagoTDConDolares() {
        return pagoTDConDolares;
    }

    public void setPagoTDConDolares(String pagoTDConDolares) {
        this.pagoTDConDolares = pagoTDConDolares;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getTotalDolares() {
        return totalDolares;
    }

    public void setTotalDolares(String totalDolares) {
        this.totalDolares = totalDolares;
    }

}

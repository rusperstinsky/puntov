package mx.lux.pos.model;

import org.apache.commons.lang.time.DateUtils;

import java.math.BigDecimal;
import java.util.*;

public class IngresoPorDia {

    private Date fecha;
    private List<DetalleIngresoPorDia> lstPagos;
    private BigDecimal montoAcumulado;
    private BigDecimal montoAcumuladoNeto;
    private BigDecimal montoDevuelto;
    private BigDecimal montoPromedio;
    private Integer contador;

    double porcentaje = 100.0;

    public IngresoPorDia( Date dia ) {
        fecha = DateUtils.truncate( dia, Calendar.DATE );
        lstPagos = new ArrayList<DetalleIngresoPorDia>();
        montoAcumulado = BigDecimal.valueOf( 0 );
        montoDevuelto = BigDecimal.valueOf( 0 );
        montoPromedio = BigDecimal.valueOf( 0 );
        contador = 0;

    }

    public void AcumulaMonto( BigDecimal monto, Double iva ) {
        double ivaMonto = 1+( iva/porcentaje );
        BigDecimal montoIva = new BigDecimal(monto.doubleValue()/( ivaMonto ));
        montoAcumulado = montoAcumulado.add( monto );
    }

    public void AcumulaDevolucion( BigDecimal montodev, Double iva ) {
        if ( montodev != null ) {
            double ivaMonto = 1+( iva/porcentaje );
            BigDecimal montoIva = new BigDecimal(montodev.doubleValue()/(ivaMonto));
            montoAcumulado = montoAcumulado.subtract( montodev );
        }
    }

    public void AcumulaIngresosPorDia( NotaVenta notaVenta ){
        DetalleIngresoPorDia detalle = FindOrCreate( lstPagos, notaVenta.getFactura() );
        if( new ArrayList<Pago>(notaVenta.getPagos()).size() > 0 ){
            Boolean cancelada = "T".equalsIgnoreCase(notaVenta.getsFactura());
            detalle.AcumulaPagos( new ArrayList<Pago>(notaVenta.getPagos()), notaVenta.getVentaTotal(), cancelada );
        }
        Collections.sort( lstPagos, new Comparator<DetalleIngresoPorDia>() {
            @Override
            public int compare( DetalleIngresoPorDia o1, DetalleIngresoPorDia o2 ) {
                return o1.getFactura().compareToIgnoreCase(o2.getFactura());
            }
        });
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha( Date fecha ) {
        this.fecha = fecha;
    }

    public BigDecimal getMontoAcumulado() {
        if ( montoDevuelto != null ) {
            montoAcumulado = montoAcumulado.subtract( montoDevuelto );
        }
        return montoAcumulado;
    }

    public void setMontoAcumulado( BigDecimal montoAcumulado ) {
        this.montoAcumulado = montoAcumulado;
    }

    public BigDecimal getMontoDevuelto() {
        return montoDevuelto;
    }

    public void setMontoDevuelto( BigDecimal montoDevuelto ) {
        this.montoDevuelto = montoDevuelto;
    }

	public BigDecimal getMontoPromedio() {
		return montoPromedio;
	}

	public void setMontoPromedio(BigDecimal montoPromedio) {
		this.montoPromedio = montoPromedio;
	}

	public Integer getContador() {
		return contador;
	}

	public void setContador(Integer contador) {
		this.contador = contador;
	}

    public List<DetalleIngresoPorDia> getLstPagos() {
        return lstPagos;
    }

    public void setLstPagos( List<DetalleIngresoPorDia> lstPagos ) {
        this.lstPagos = lstPagos;
    }

    protected DetalleIngresoPorDia FindOrCreate( List<DetalleIngresoPorDia> lstIngresos, String idFactura ) {
        DetalleIngresoPorDia found = null;

        for ( DetalleIngresoPorDia ingresos : lstIngresos ) {
            if ( ingresos.getFactura().equals( idFactura ) ) {
                found = ingresos;
                break;
            }
        }
        if ( found == null ) {
            found = new DetalleIngresoPorDia( idFactura );
            lstIngresos.add( found );
        }
        return found;
    }
}

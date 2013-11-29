package mx.lux.pos.service.impl

import mx.lux.pos.service.ReportService
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration( 'classpath:spring-config.xml' )
class ReportServiceImplIntegration extends Specification {

  @Resource
  private ReportService service

  def 'genera reporte html de ventas por vendedor'( ) {
    given:
    Date fechaInicio = Date.parse( 'dd/MM/yyyy', '01/04/2012' )
    Date fechaFin = Date.parse( 'dd/MM/yyyy', '30/04/2012' )

    when:
    //String actual = service.obtenerReporteCierreDiario(fechaInicio)
    //String actual = service.obtenerReporteTrabajosSinEntregar()
	//String actual = service.obtenerReporteVentasVendedorporMarca(fechaInicio, fechaFin, "", false, false, true)
	//String actual = service.obtenerReporteVentasMarca(fechaInicio, fechaFin, "", false, false, true)
	//String actual = service.obtenerReporteVentas(fechaInicio, fechaFin)
	//String actual = service.obtenerReporteVentasCompleto(fechaInicio, fechaFin)
	//String actual = service.obtenerReporteExistenciasporMarca( "", false, false, true )
	//String actual = service.obtenerReporteExistenciasporMarcaResumido( "", false, false, true 
	//String actual = service.obtenerReporteExistenciasporArticulo( "", "" )
	//String actual = service.obtenerReporteIngresosXVendedorResumido(fechaInicio, fechaFin)
	//String actual = service.obtenerReporteIngresosXVendedorCompleto(fechaInicio, fechaFin)
	//String actual = service.obtenerReporteIngresosXSucursal(fechaInicio, fechaFin)
	//String actual = service.obtenerReporteControldeTrabajos(false, false, false, false, true, false, true)
	//String actual = service.obtenerReporteTrabajosEntregados(fechaInicio, fechaFin) 
	//String actual = service.obtenerReporteTrabajosEntregadosporEmpleado(fechaInicio, fechaFin) 
	//String actual = service.obtenerReporteCancelacionesCompleto(fechaInicio, fechaFin)
	//String actual = service.obtenerReporteCancelacionesResumido(fechaInicio, fechaFin)
	//String actual = service.obtenerReporteVentasporVendedorResumido(fechaInicio, fechaFin)
	//String actual = service.obtenerReporteVentasporLineaFactura(fechaInicio, fechaFin, "", false, false, false)
	//String actual = service.obtenerReporteFacturasFiscales(fechaInicio, fechaFin)
	//String actual = service.obtenerReporteDescuentos(fechaInicio, fechaFin)
	//String actual = service.obtenerReporteVentasporLineaArticulo(fechaInicio, fechaFin, "", false, false, false)
	//String actual = service.obtenerReportePromociones(fechaInicio, fechaFin)
	//String actual = service.obtenerReportePagos(fechaInicio, fechaFin, "", "")
	//String actual = service.obtenerReporteCotizaciones(fechaInicio, fechaFin)
	//String actual = service.obtenerReporteExamenesResumido(fechaInicio, fechaFin)
	//String actual = service.obtenerReporteExamenesCompleto(fechaInicio, fechaFin)
	//String actual = service.obtenerReporteVentasporOptometrista(fechaInicio, fechaFin, true, false, false, false, false, false, true, false)
	String actual = service.obtenerReporteVentasporOptometristaResumido(fechaInicio, fechaFin, true, false, false, false, false, false, false, true)

    then:
    actual == null
    //actual.length() > 0
  }
}

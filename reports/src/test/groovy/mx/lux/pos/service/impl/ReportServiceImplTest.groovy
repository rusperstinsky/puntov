package mx.lux.pos.service.impl

import mx.lux.pos.service.SucursalService
import mx.lux.pos.service.business.ReportBusiness
import spock.lang.Specification
import mx.lux.pos.model.*
import mx.lux.pos.repository.*

class ReportServiceImplTest extends Specification {

  private ReportServiceImpl service
  private NotaVentaRepository notaVentaRepository
  private PagoRepository pagoRepository
  private DevolucionRepository devolucionRepository
  private SucursalService sucursalService
  private ParametroRepository parametroRepository
  private ImpuestoRepository impuestoRepository
  private ReportBusiness reportBusiness

  def setup( ) {
    notaVentaRepository = Mock( NotaVentaRepository )
    pagoRepository = Mock( PagoRepository )
    devolucionRepository = Mock( DevolucionRepository )
    sucursalService = Mock( SucursalService )
    parametroRepository = Mock( ParametroRepository )
    impuestoRepository = Mock( ImpuestoRepository )
    reportBusiness = Mock( ReportBusiness )
    service = new ReportServiceImpl(
        notaVentaRepository: notaVentaRepository,
        pagoRepository: pagoRepository,
        sucursalService: sucursalService,
        devolucionRepository: devolucionRepository,
        parametroRepository: parametroRepository,
        impuestoRepository: impuestoRepository,
        reportBusiness: reportBusiness
    )
  }

  def 'obtiene null al generar reporte de cierre con fecha nula'( ) {
    expect:
    null == service.obtenerReporteCierreDiario( null )
  }

  def 'obtiene null al generar reporte de cierre sin reporte disponible'( ) {
    given:
    File report = new File( System.getProperty( 'java.io.tmpdir' ), 'cierre-diario.html' )
    Date fecha = Date.parse( 'dd/MM/yyyy', '03/09/2012' )
    service.CIERRE_DIARIO = ''

    when:
    String actual = service.obtenerReporteCierreDiario( fecha )

    then:
    1 * pagoRepository.findByFechaBetween( _, _ ) >> [ ]
    1 * sucursalService.obtenSucursalActual() >> new Sucursal()
    1 * devolucionRepository.findByFechaBetween( _, _ ) >> [ ]
    1 * parametroRepository.findOne( _ as String ) >> new Parametro()
    1 * impuestoRepository.findOne( _ ) >> new Impuesto( tasa: 0 )
    1 * reportBusiness.CompilayGeneraReporte( _, _, _ )

    then:
    0 * _

    then:
    actual == null
  }

  def 'obtiene ruta del reporte como string al generar reporte de cierre'( ) {
    given:
    Date fechaInicio = Date.parse( 'dd/MM/yyyy', '03/09/2012' )
    Date fechaFin = new Date( fechaInicio.clearTime().next().time - 1 )

    when:
    String actual = service.obtenerReporteCierreDiario( fechaInicio )

    then:
    1 * pagoRepository.findByFechaBetween( fechaInicio, fechaFin ) >> [
        new Pago(
            notaVenta: new NotaVenta(
                ventaTotal: 0
            )
        )
    ]
    1 * sucursalService.obtenSucursalActual() >> new Sucursal()
    1 * devolucionRepository.findByFechaBetween( _, _ ) >> [ ]
    1 * parametroRepository.findOne( _ as String ) >> new Parametro()
    1 * impuestoRepository.findOne( _ ) >> new Impuesto( tasa: 0 )
    1 * reportBusiness.CompilayGeneraReporte( _, _, _ )

    then:
    0 * _

    then:
    actual == null
  }

  def 'obtiene ruta del reporte como string al generar reporte de ingresos por vendedor'( ) {
    given:
    Date fechaInicio = Date.parse( 'dd/MM/yyyy', '01/09/2012' )
    Date fechaFin = new Date( fechaInicio.clearTime().next().time - 1 )

    when:
    String actual = service.obtenerReporteIngresosXSucursal( fechaInicio, fechaFin )

    then:
    1 * parametroRepository.findOne( TipoParametro.IVA_VIGENTE.getValue() ) >> new Parametro()
    1 * impuestoRepository.findOne( _ ) >> new Impuesto( tasa: 0 )
    1 * reportBusiness.obtenerIngresoporDia( _, _ ) >> [ new IngresoPorDia( new Date() ) ]
    1 * sucursalService.obtenSucursalActual() >> new Sucursal()
    1 * reportBusiness.CompilayGeneraReporte( _, _, _ )

    then:
    0 * _

    then:
    actual == null
  }
}

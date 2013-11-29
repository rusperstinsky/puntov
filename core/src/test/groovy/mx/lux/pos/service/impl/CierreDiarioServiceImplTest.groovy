package mx.lux.pos.service.impl

import mx.lux.pos.model.CierreDiario
import spock.lang.Specification
import mx.lux.pos.repository.*

class CierreDiarioServiceImplTest extends Specification {

  CierreDiarioServiceImpl service
  CierreDiarioRepository cierreDiarioRepository
  ParametroRepository parametroRepository
  NotaVentaRepository notaVentaRepository
  ModificacionRepository modificacionRepository
  PagoRepository pagoRepository
  DevolucionRepository devolucionRepository
  TerminalRepository terminalRepository
  VoucherTmpRepository voucherTmpRepository
  ResumenDiarioRepository resumenDiarioRepository
  PagoExternoRepository pagoExternoRepository

  def setup( ) {
    cierreDiarioRepository = Mock( CierreDiarioRepository )
    parametroRepository = Mock( ParametroRepository )
    notaVentaRepository = Mock( NotaVentaRepository )
    modificacionRepository = Mock( ModificacionRepository )
    pagoRepository = Mock( PagoRepository )
    devolucionRepository = Mock( DevolucionRepository )
    terminalRepository = Mock( TerminalRepository )
    pagoRepository = Mock( PagoRepository )
    voucherTmpRepository = Mock( VoucherTmpRepository )
    resumenDiarioRepository = Mock( ResumenDiarioRepository )
    pagoExternoRepository = Mock( PagoExternoRepository )
    service = new CierreDiarioServiceImpl(
        cierreDiarioRepository: cierreDiarioRepository,
        parametroRepository: parametroRepository,
        notaVentaRepository: notaVentaRepository,
        modificacionRepository: modificacionRepository,
        terminalRepository: terminalRepository,
        pagoRepository: pagoRepository,
        voucherTmpRepository: voucherTmpRepository,
        resumenDiarioRepository: resumenDiarioRepository,
        pagoExternoRepository: pagoExternoRepository,
        devolucionRepository: devolucionRepository
    )
  }

  void assertEquals( CierreDiario expected, CierreDiario actual ) {
    if ( expected != null && actual != null ) {
      assert expected.fecha.dateString == actual.fecha.dateString
      assert expected.fechaCierre?.dateString == actual.fechaCierre?.dateString
      assert expected.horaCierre?.dateString == actual.horaCierre?.dateString
      assert expected.estado == actual.estado
      assert expected.observaciones == actual.observaciones
      assert expected.ventaNeta == actual.ventaNeta
      assert expected.ventaBruta == actual.ventaBruta
      assert expected.modificaciones == actual.modificaciones
      assert expected.cancelaciones == actual.cancelaciones
      assert expected.ingresoBruto == actual.ingresoBruto
      assert expected.devoluciones == actual.devoluciones
      assert expected.ingresoNeto == actual.ingresoNeto
      assert expected.efectivoRecibido == actual.efectivoRecibido
      assert expected.efectivoExternos == actual.efectivoExternos
      assert expected.efectivoDevoluciones == actual.efectivoDevoluciones
      assert expected.efectivoNeto == actual.efectivoNeto
      assert expected.dolaresRecibidos == actual.dolaresRecibidos
      assert expected.dolaresDevoluciones == actual.dolaresDevoluciones
      assert expected.facturaInicial == actual.facturaInicial
      assert expected.facturaFinal == actual.facturaFinal
      assert expected.cantidadVentas == actual.cantidadVentas
      assert expected.cantidadModificaciones == actual.cantidadModificaciones
      assert expected.cantidadCancelaciones == actual.cantidadCancelaciones
    } else {
      assert expected == actual
    }
  }

  def 'obtiene cierre diario nuevo al abrir cierre diario'( ) {
    given:
    def expected = new CierreDiario(
        fecha: new Date(),
        estado: 'a'
    )
    def result = new CierreDiario(
        fecha: expected.fecha,
        estado: expected.estado
    )

    when:
    def actual = service.abrirCierreDiario()

    then:
    1 * cierreDiarioRepository.findOne( _ as Date ) >> null
    1 * cierreDiarioRepository.save( { CierreDiario tmp ->
      assertEquals( expected, tmp )
      return tmp
    } ) >> result

    then:
    0 * _

    then:
    assertEquals( expected, actual )
  }

  def 'obtiene cierre diario existente al abrir cierre diario'( ) {
    given:
    def expected = new CierreDiario(
        fecha: new Date(),
        estado: 'a'
    )
    def result = new CierreDiario(
        fecha: expected.fecha,
        estado: expected.estado
    )

    when:
    def actual = service.abrirCierreDiario()

    then:
    1 * cierreDiarioRepository.findOne( _ as Date ) >> result

    then:
    0 * _

    then:
    assertEquals( expected, actual )
  }

  def 'no se cargan vouchers con fecha nula'( ) {
    when:
    service.cargarVouchersResumenes( null )

    then:
    0 * _
  }
}

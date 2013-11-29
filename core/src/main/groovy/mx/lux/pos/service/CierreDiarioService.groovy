package mx.lux.pos.service

import mx.lux.pos.model.CierreDiario
import mx.lux.pos.model.Deposito
import mx.lux.pos.model.Pago
import mx.lux.pos.model.ResumenDiario

public interface CierreDiarioService {

  List<CierreDiario> buscarConEstadoAbierto( )

  CierreDiario buscarPorFecha( Date fecha )

  List<Deposito> buscarDepositosPorFecha( Date fecha )

  CierreDiario abrirCierreDiario( )

  void cerrarCierreDiario( Date fechaCierre, String observaciones, Boolean cierre )

  Deposito buscarDepositoPorId( Integer id )

  void guardarDeposito( Deposito deposito )

  void actualizarDeposito( Deposito deposito )

  void eliminarDeposito( Integer id )

  List<Pago> buscarPagosPorFechaCierrePorFacturaPorTerminal( Date fechaCierre, String terminal, String plan )

  Pago buscarPagoPorId( Integer id )

  List<ResumenDiario> buscarResumenDiarioPorFechaPorTerminal( Date fechaCierre, String terminal )

  boolean cargarDatosCierreDiario( Date fecha )

  CierreDiario actualizarCierreDiario( Date fecha )

  void eliminarVentasAbiertas( )

  List<CierreDiario> buscarPorFechasEntre( Date fechaInicio, Date fechaFin )

  void regenerarArchivosZ( Date fechaCierre )

  boolean cargarResumenTerminales( Date fecha )

  boolean cargarVouchersResumenes( Date fecha )

  List<CierreDiario> diasCerrados()

  void cambiarEstatuCerrado( Date fecha )

  void closeDaysBeforeNov( )

  void generaIN2( Date fechaCierre )
}

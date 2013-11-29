package mx.lux.pos.service

import mx.lux.pos.model.BancoDeposito
import mx.lux.pos.model.BancoEmisor

interface BancoService {

  BancoEmisor obtenerBancoEmisor( Integer id )

  BancoDeposito obtenerBancoDeposito( Integer id )

  List<BancoEmisor> listarBancosEmisores( )

  List<BancoDeposito> listarBancosDeposito( )

}

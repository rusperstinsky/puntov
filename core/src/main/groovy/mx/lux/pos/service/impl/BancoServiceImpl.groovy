package mx.lux.pos.service.impl

import groovy.util.logging.Slf4j
import mx.lux.pos.model.BancoDeposito
import mx.lux.pos.model.BancoEmisor
import mx.lux.pos.repository.BancoDepositoRepository
import mx.lux.pos.repository.BancoEmisorRepository
import mx.lux.pos.service.BancoService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

@Slf4j
@Service( 'bancoService' )
@Transactional( readOnly = true )
class BancoServiceImpl implements BancoService {

  @Resource
  private BancoEmisorRepository bancoEmisorRepository

  @Resource
  private BancoDepositoRepository bancoDepositoRepository

  @Override
  BancoEmisor obtenerBancoEmisor( Integer id ) {
    log.info( "obteniendo banco emisor id: ${id}" )
    return bancoEmisorRepository.findOne( id ?: 0 )
  }

  @Override
  BancoDeposito obtenerBancoDeposito( Integer id ) {
    log.info( "obteniendo banco deposito id: ${id}" )
    return bancoDepositoRepository.findOne( id ?: 0 )
  }

  @Override
  List<BancoEmisor> listarBancosEmisores( ) {
    log.info( "listando bancos emisores" )
    return bancoEmisorRepository.findByDescripcionNotNullOrderByDescripcionAsc() ?: [ ]
  }

  @Override
  List<BancoDeposito> listarBancosDeposito( ) {
    log.info( "listando bancos de deposito" )
    return bancoDepositoRepository.findByNombreNotNullOrderByNombreAsc() ?: [ ]
  }
}

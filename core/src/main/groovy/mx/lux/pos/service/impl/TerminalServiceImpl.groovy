package mx.lux.pos.service.impl

import mx.lux.pos.model.QTerminal
import mx.lux.pos.model.Terminal
import mx.lux.pos.repository.TerminalRepository
import mx.lux.pos.service.TerminalService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

@Service( 'terminalService' )
@Transactional( readOnly = true )
class TerminalServiceImpl implements TerminalService {

  private static final Logger log = LoggerFactory.getLogger( TerminalServiceImpl.class )

  @Resource
  private TerminalRepository terminalRepository

  @Override
  List<Terminal> listarTerminales( ) {
    log.info( "listando terminales" )
    return terminalRepository.findByDescripcionNotNullOrderByDescripcionAsc() ?: [ ]
  }

  @Override
  List<Terminal> listarTerminalesActivas( String idTipoPago ) {
      log.info( "listando terminales Activas para el tipo de tarjeta" )
      QTerminal terminal = QTerminal.terminal
      return terminalRepository.findAll( terminal.afiliacion.contains( idTipoPago.trim() ) ) ?: [ ]
  }
}

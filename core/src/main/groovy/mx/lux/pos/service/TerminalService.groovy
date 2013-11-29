package mx.lux.pos.service

import mx.lux.pos.model.Terminal

interface TerminalService {

  List<Terminal> listarTerminales( )

  List<Terminal> listarTerminalesActivas( String idTipoPago )

}

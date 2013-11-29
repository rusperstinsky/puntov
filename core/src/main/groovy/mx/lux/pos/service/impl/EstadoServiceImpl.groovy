package mx.lux.pos.service.impl

import groovy.util.logging.Slf4j
import mx.lux.pos.model.Estado
import mx.lux.pos.model.Parametro
import mx.lux.pos.model.TipoParametro
import mx.lux.pos.repository.EstadoRepository
import mx.lux.pos.repository.ParametroRepository
import mx.lux.pos.service.EstadoService
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

@Slf4j
@Service( 'estadoService' )
@Transactional( readOnly = true )
class EstadoServiceImpl implements EstadoService {

  @Resource
  private EstadoRepository estadoRepository

  @Resource
  private ParametroRepository parametroRepository

  @Override
  Estado obtenerEstado( String id ) {
    log.info( "obteniendo estado con id: ${id}" )
    if ( StringUtils.isNotBlank( id ) ) {
      Estado estado = estadoRepository.findOne( id )
      log.debug( "obtiene estado id: ${estado?.id}, nombre: ${estado?.nombre}" )
      return estado
    } else {
      log.warn( 'no se obtiene estado, parametro invalido' )
    }
    return null
  }

  @Override
  List<Estado> listaEstados( ) {
    log.info( 'listando estados' )
    List<Estado> estados = estadoRepository.findByNombreNotNullOrderByNombreAsc()
    log.debug( "obtiene estados id: ${estados*.id}" )
    return estados?.any() ? estados : [ ]
  }

  @Override
  Estado obtenEstadoPorDefecto( ) {
    log.info( 'obteniendo estado por defecto' )
    Parametro parametro = parametroRepository.findOne( TipoParametro.ID_ESTADO.value )
    return obtenerEstado( parametro?.valor )
  }

  @Override
  Estado obtenEstadoPorNombre( String nombre ) {
    log.info( "obteniendo estado con nombre: ${nombre}" )
    if ( StringUtils.isNotBlank( nombre ) ) {
      Estado estado = estadoRepository.findByNombre( nombre )
      log.debug( "obtiene estado id: ${estado?.id}, nombre: ${estado?.nombre}" )
      return estado
    } else {
      log.warn( 'no se obtiene estado, parametro invalido' )
    }
    return null
  }
}

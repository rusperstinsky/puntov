package mx.lux.pos.service.impl

import groovy.util.logging.Slf4j
import mx.lux.pos.model.Cliente
import mx.lux.pos.model.Contribuyente
import mx.lux.pos.model.Estado
import mx.lux.pos.repository.ClienteRepository
import mx.lux.pos.repository.ContribuyenteRepository
import mx.lux.pos.repository.EstadoRepository
import mx.lux.pos.repository.SucursalRepository
import mx.lux.pos.service.ContribuyenteService
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.regex.Pattern
import javax.annotation.Resource

@Slf4j
@Service( 'contribuyenteService' )
@Transactional( readOnly = true )
class ContribuyenteServiceImpl implements ContribuyenteService {

  @Resource
  private ContribuyenteRepository contribuyenteRepository

  @Resource
  private EstadoRepository estadoRepository

  @Resource
  private ClienteRepository clienteRepository

  @Resource
  private SucursalRepository sucursalRepository

  @Override
  Contribuyente obtenerContribuyentePorRfc( String rfc ) {
    log.info( "obteniendo contribuyente con rfc: ${rfc}" )
    if ( StringUtils.isNotBlank( rfc ) ) {
      List<Contribuyente> resultados = contribuyenteRepository.findByRfcOrderByFechaRegistroDesc( rfc )
      log.debug( "obtiene resultados id: ${resultados*.id}" )
      if ( resultados?.any() ) {
        Contribuyente contribuyente = resultados.first()
        log.debug( "obtiene contribuyente id: ${contribuyente?.id}" )
        return contribuyente
      } else {
        log.warn( 'no se obtiene contribuyente, sin resultados' )
      }
    } else {
      log.warn( 'no se obtiene contribuyente, parametro invalido' )
    }
    return null
  }

  @Override
  List<Contribuyente> listarContribuyentesPorRfcSimilar( String rfc ) {
    log.info( "listando contribuyentes con rfc similar a: ${rfc}" )
    if ( StringUtils.isNotBlank( rfc ) ) {
      List<Contribuyente> resultados = contribuyenteRepository.findByRfcStartingWithOrderByRfcAsc( rfc )
      resultados = resultados?.unique { Contribuyente tmp ->
        tmp.rfc
      }
      log.debug( "obtiene contribuyentes id: ${resultados*.id}" )
      return resultados?.any() ? resultados : [ ]
    } else {
      log.warn( 'no se listan contribuyentes, parametro invalido' )
    }
    return [ ]
  }

  @Override
  boolean esRfcValido( String rfc ) {
    log.info( "validando rfc: ${rfc}" )
    String regex = '[a-zA-Z&/]{3,4}[0-9]{6}[a-zA-Z0-9]{3}'
    if ( Pattern.matches( regex, rfc ?: '' ) ) {
      log.debug( 'rfc valido' )
      return true
    } else {
      log.warn( 'rfc invalido' )
    }
    return false
  }

  @Override
  @Transactional
  Contribuyente registrarContribuyente( Contribuyente contribuyente ) {
    log.info( "registrando contribuyente con rfc: ${contribuyente?.rfc}" )
    if ( esRfcValido( contribuyente?.rfc ) ) {
      try {
        Integer idSucursal = sucursalRepository.getCurrentSucursalId()
        Estado estado = estadoRepository.findByNombre( contribuyente.idEstado )
        log.debug( "obtiene estado id: ${estado?.id}" )
        List<Contribuyente> resultados = contribuyenteRepository.findByRfcOrderByFechaRegistroDesc( contribuyente.rfc )
        log.debug( "obtiene coincidencias contribuyentes id: ${resultados*.id}" )
        if ( resultados?.any() ) {
          Contribuyente existente = resultados.first()
          contribuyente.id = existente.id
          log.debug( "contribuyente a actualizar id: ${contribuyente.id}" )
        }
        contribuyente.idSucursal = idSucursal
        contribuyente.idEstado = estado?.id ?: null
        contribuyente = contribuyenteRepository.save( contribuyente )
        if ( contribuyente?.id ) {
          Cliente cliente = clienteRepository.findOne( contribuyente.idCliente )
          log.debug( "obtiene cliente id: ${cliente?.id}" )
          if ( cliente?.id ) {
            cliente.rfc = contribuyente.rfc
            clienteRepository.save( cliente )
            log.debug( "actualiza cliente id: ${cliente.id} con rfc: ${cliente.rfc}" )
          }
        }
        log.debug( "registra contribuyente id: ${contribuyente?.id}" )
        return contribuyente
      } catch ( ex ) {
        log.error( 'error al registrar contribuyente', ex )
      }
    } else {
      log.warn( 'no se registra contribuyente, parametros invalidos' )
    }
    return null
  }
}

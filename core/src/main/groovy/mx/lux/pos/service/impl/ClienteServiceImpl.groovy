package mx.lux.pos.service.impl

import groovy.util.logging.Slf4j
import mx.lux.pos.service.ClienteService
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

import mx.lux.pos.model.*
import mx.lux.pos.repository.*
import mx.lux.pos.service.business.Registry

@Slf4j
@Service( 'clienteService' )
@Transactional( readOnly = true )
class ClienteServiceImpl implements ClienteService {

  @Resource
  private ClienteRepository clienteRepository

  @Resource
  private TituloRepository tituloRepository

  @Resource
  private DominioRepository dominioRepository

  @Resource
  private ConvenioRepository convenioRepository

  @Resource
  private ParametroRepository parametroRepository

  @Resource
  private ClientePaisRepository clientePaisRepository

  @Resource
  private SucursalRepository sucursalRepository

  protected void eliminarCliente( Integer id ) {
    if ( !Registry.genericCustomer.equals( id )) {
      try {
        ClientePais cp = clientePaisRepository.findOne( id )
        if( cp != null ){
          clientePaisRepository.delete( cp.id )
          clientePaisRepository.flush()
        }
        Cliente c = clienteRepository.findOne( id )
        if ( c != null ) {
          clienteRepository.delete( c.id )
          clienteRepository.flush()
        }
        log.debug( String.format( "Se elimino cliente:%d", id ) )
      } catch (Exception e) {
        log.debug( String.format( "Error al eliminar cliente: %d", id) )
      }
    }
  }

  @Override
  Cliente obtenerCliente( Integer id ) {
    log.debug( "obteniendo cliente id: ${id}" )
    clienteRepository.findOne( id ?: 0 )
  }

  @Override
  List<Cliente> buscarCliente( String nombre, String apellidoPaterno, String apellidoMaterno ) {
    log.debug( "buscando cliente: $nombre $apellidoPaterno $apellidoMaterno" )
    nombre = StringUtils.trimToNull( nombre )
    apellidoPaterno = StringUtils.trimToNull( apellidoPaterno )
    apellidoMaterno = StringUtils.trimToNull( apellidoMaterno )
    if ( nombre || apellidoPaterno || apellidoMaterno ) {
      def result = clienteRepository.findByNombreApellidos( nombre, apellidoPaterno, apellidoMaterno )
      log.debug( "se obtiene lista con: ${result?.size()} elementos" )
      return result
    }
    log.warn( "parametros insuficientes" )
    return [ ] as List<Cliente>
  }

  @Override
  @Transactional
  Cliente agregarCliente( Cliente cliente, boolean editar ) {
    log.debug( "agregando cliente: ${cliente?.dump()}" )
    if ( StringUtils.isNotBlank( cliente?.nombre ) ) {
      cliente.idSucursal = sucursalRepository.getCurrentSucursalId()
      try {
          if( editar ) {
            this.eliminarCliente( cliente.id )
          }
        cliente = clienteRepository.saveAndFlush( cliente )
        log.debug( "se registra cliente con id: ${cliente?.id}" )
        return cliente
      } catch ( ex ) {
        log.error( "problema al registrar cliente: ${cliente?.dump()}", ex )
      }
    }
    return null
  }

  @Override
  @Transactional
  ClientePais agregarClientePais( ClientePais clientePais ) {
    log.debug( "agregando datos de pais de origen de cliente extranjero: ${clientePais?.dump()}" )
    if ( clientePais?.id ) {
      try {
          clientePais = clientePaisRepository.save( clientePais )
          clientePaisRepository.flush()
        log.debug( "clientePais registrado id: ${clientePais.id}" )
        return clientePais
      } catch ( ex ) {
        log.error( "problema al registrar clientePais: ${clientePais?.dump()}", ex )
      }
    }
    return null
  }

  @Override
  Cliente obtenerClientePorDefecto( ) {
    log.debug( "obteniendo cliente generico" )
    def idCliente = parametroRepository.findOne( TipoParametro.ID_CLIENTE_GENERICO.value )?.valor
    log.debug( "id cliente generico: ${idCliente}" )
    if ( idCliente?.isInteger() ) {
      return clienteRepository.findOne( idCliente?.toInteger() )
    }
    return null
  }

  @Override
  List<Titulo> listarTitulosClientes( ) {
    log.debug( "listando titulos de clientes" )
    //tituloRepository.findByTituloNotNullOrderByOrdenAsc()
      QTitulo titulo = QTitulo.titulo1
      tituloRepository.findAll( titulo.titulo.isNotNull(), titulo.titulo.asc() )
  }

  @Override
  List<Dominio> listarDominiosClientes( ) {
    log.debug( "listando dominios frecuentes" )
    dominioRepository.findByNombreNotNull()
  }

  /*@Override
  List<InstitucionIc> obtenerConvenios( String convenio ){
      log.debug( "listando convenios" )
      convenioRepository.findByInicialesIcIlikeByEstatusConvenioEqual( convenio, "V" )
  }*/
}

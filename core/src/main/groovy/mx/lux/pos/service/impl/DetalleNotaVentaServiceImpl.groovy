package mx.lux.pos.service.impl

import groovy.util.logging.Slf4j
import mx.lux.pos.model.DetalleNotaVenta
import mx.lux.pos.repository.DetalleNotaVentaRepository
import mx.lux.pos.service.DetalleNotaVentaService
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

@Slf4j
@Service( "detalleNotaVentaService" )
@Transactional( readOnly = true )
class DetalleNotaVentaServiceImpl implements DetalleNotaVentaService {

  @Resource
  private DetalleNotaVentaRepository detalleNotaVentaRepository

  @Override
  DetalleNotaVenta obtenerDetalleNotaVenta( String idFactura, Integer idArticulo ) {
    log.info( "obteniendo detalleNotaVenta con idFactura: ${idFactura} idArticulo: ${idArticulo}" )
    if ( StringUtils.isNotBlank( idFactura ) && idArticulo ) {
      return detalleNotaVentaRepository.findByIdFacturaAndIdArticulo( idFactura, idArticulo )
    } else {
      log.warn( 'no se obtiene detalleNotaVenta, parametros invalidos' )
    }
    return null
  }

  @Override
  List<DetalleNotaVenta> listarDetallesNotaVentaPorIdFactura( String idFactura ) {
    log.info( "listando detallesNotaVenta con idFactura: ${idFactura}" )
    if ( StringUtils.isNotBlank( idFactura ) ) {
      return detalleNotaVentaRepository.findByIdFacturaOrderByIdArticuloAsc( idFactura ) ?: [ ]
    } else {
      log.warn( 'no se listan detallesNotaVenta, parametros invalidos' )
    }
    return [ ]
  }

  @Override
  DetalleNotaVenta obtenerDetalleNotaVentaPoridFacturaidArticulo( String idFactura, Integer idArticulo ){
    log.debug( "obtenerDetalleNotaVentaPoridFacturaidArticulo( String idFactura, Integer idArticulo )" )

    DetalleNotaVenta detNotaVenta = detalleNotaVentaRepository.findByIdFacturaAndIdArticulo( idFactura, idArticulo )
    return detNotaVenta
  }
}

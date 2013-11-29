package mx.lux.pos.service.impl

import groovy.util.logging.Slf4j
import mx.lux.pos.model.Pago
import mx.lux.pos.model.NotaVenta
import mx.lux.pos.model.QNotaVenta
import mx.lux.pos.model.QPago
import mx.lux.pos.model.QRetorno
import mx.lux.pos.model.Retorno
import mx.lux.pos.repository.NotaVentaRepository
import mx.lux.pos.repository.PagoRepository
import mx.lux.pos.repository.RetornoRepository
import mx.lux.pos.service.PagoService
import mx.lux.pos.service.NotaVentaService
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource
import mx.lux.pos.service.business.Registry

import java.text.NumberFormat

@Slf4j
@Service( "pagoService" )
@Transactional( readOnly = true )
class PagoServiceImpl implements PagoService {

  @Resource
  private PagoRepository pagoRepository

  @Resource
  private NotaVentaRepository notaVentaRepository

  @Resource
  private RetornoRepository retornoRepository

  @Override
  Pago obtenerPago( Integer id ) {
    log.info( "obteniendo pago: ${id}" )
    return pagoRepository.findOne( id ?: 0 )
  }

  @Override
  List<Pago> listarPagosPorIdFactura( String idFactura ) {
    log.info( "listando pagos con idFactura: ${idFactura} " )
    if ( StringUtils.isNotBlank( idFactura ) ) {
      List<Pago> pagos = pagoRepository.findByIdFacturaOrderByFechaAsc( idFactura )
      log.debug( "obtiene pagos: ${pagos?.id}" )
      return pagos?.any() ? pagos : [ ]
    } else {
      log.warn( 'no se obtiene lista de pagos, parametros invalidos' )
    }
    return [ ]
  }

  @Override
  @Transactional
  Pago actualizarPago( Pago pago ) {
    log.info( "actualizando pago id: ${pago?.id}" )
    if ( pago?.id ) {
      return pagoRepository.save( pago )
    }
    return null
  }


  @Override
  Boolean obtenerTipoPagosDolares( String formaPago ) {
    log.info( 'obtenerTipoPagosDolares( )' )
    return Registry.isCardPaymentInDollars( formaPago )
  }

  @Override
  String obtenerPlanNormalTarjetaCredito( ) {
      log.info( 'obtenerPlanNormalTarjetaCredito( )' )
      return Registry.normalPlanCreditCard()
  }

  @Override
  Retorno obtenerRetorno( String folio ) {
      log.debug( "obteniendo retorno con folio $folio" )
      Retorno retorno = new Retorno()
      Integer folioInt = 0
      try{
          folioInt = NumberFormat.getInstance().parse( folio ).intValue()
      } catch (Exception e){
          retorno = null
      }
      if( folioInt != 0 ){
          QRetorno ret = QRetorno.retorno
          retorno = retornoRepository.findOne( folioInt )
          if( (retorno == null) || (StringUtils.trimToEmpty(retorno.ticketDestino) != '') ){
              retorno = null
          }
      }
      return retorno
  }


  @Override
  @Transactional
  Retorno actualizarRetorno( Retorno retorno ) {
      return retornoRepository.save( retorno )
  }


}

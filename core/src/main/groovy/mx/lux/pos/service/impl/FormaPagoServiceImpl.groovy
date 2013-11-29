package mx.lux.pos.service.impl

import mx.lux.pos.model.FormaPago
import mx.lux.pos.model.Parametro
import mx.lux.pos.model.TipoParametro
import mx.lux.pos.repository.FormaPagoRepository
import mx.lux.pos.repository.ParametroRepository
import mx.lux.pos.service.FormaPagoService
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

@Service( 'formaPagoService' )
@Transactional( readOnly = true )
class FormaPagoServiceImpl implements FormaPagoService {

  private static final Logger log = LoggerFactory.getLogger( FormaPagoServiceImpl.class )

  @Resource
  private FormaPagoRepository formaPagoRepository

  @Resource
  private ParametroRepository parametroRepository

  private List<FormaPago> listarFormasPagoRegistradas( ) {
    List<FormaPago> resultados = formaPagoRepository.findAll() ?: [ ]
    return resultados?.findAll { FormaPago formaPago ->
      StringUtils.isNotBlank( formaPago?.id )
    }
  }

  @Override
  List<FormaPago> listarFormasPago( ) {
    log.info( "listando formas de pago" )
    return listarFormasPagoRegistradas()
  }

  @Override
  List<FormaPago> listarFormasPagoActivas( ) {
    log.info( "listando formas de pago activas" )
    List<FormaPago> formasPago = [ ]
    Parametro parametro = parametroRepository.findOne( TipoParametro.TIPO_PAGO.value )
    String[] valores = parametro?.valores
    log.debug( "obteniendo parametro de formas de pago activas id: ${parametro?.id} valores: ${valores}" )
    if ( valores?.any() ) {
      List<FormaPago> resultados = listarFormasPagoRegistradas()
      log.debug( "formas de pago existentes: ${resultados*.id}" )
      formasPago = resultados.findAll { FormaPago formaPago ->
        valores.contains( formaPago?.id?.trim() )
      }
      log.debug( "formas de pago obtenidas: ${formasPago*.id}" )
    }
    return formasPago
  }
}

package mx.lux.pos.service.impl

import groovy.util.logging.Slf4j
import mx.lux.pos.model.Empleado
import mx.lux.pos.model.Parametro
import mx.lux.pos.model.TipoParametro
import mx.lux.pos.repository.EmpleadoRepository
import mx.lux.pos.repository.ParametroRepository
import mx.lux.pos.service.EmpleadoService
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource
import java.text.DateFormat
import java.text.SimpleDateFormat

@Slf4j
@Service( 'empleadoService' )
@Transactional( readOnly = true )
class EmpleadoServiceImpl implements EmpleadoService {

  @Resource
  private EmpleadoRepository empleadoRepository

  @Resource
  private ParametroRepository parametroRepository

  @Override
  Empleado obtenerEmpleado( String id ) {
    log.info( "obteniendo empleado id: ${id}" )
    if ( StringUtils.isNotBlank( id ) ) {
      Empleado empleado = empleadoRepository.findOne( id )
      if ( empleado?.id ) {
        return empleado
      } else {
        log.warn( "empleado no existe" )
      }
    } else {
      log.warn( "no se obtiene empleado, parametros invalidos" )
    }
    return null
  }

  @Override
  String gerente( ) {
      Parametro parametro = parametroRepository.findOne( TipoParametro.ID_GERENTE.value )
      //Empleado empleado = empleadoRepository.findOne( parametro.valor.trim() )
      return parametro.valor

  }

    @Override
    void actualizarPass( empleado ){
        log.info( "actualizando password de empleado id: ${empleado.id}" )
        if ( StringUtils.isNotBlank( empleado.id ) ) {
            empleadoRepository.save( empleado )
            empleadoRepository.flush()
        }
    }

    @Override
    Boolean sesionPrimeraVez(){
        Boolean sesionPrimera = false
        DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )
        Parametro parametro = parametroRepository.findOne( TipoParametro.FECHA_ACTUAL.value )
        if( parametro != null ){
            sesionPrimera = !df.format(new Date()).equalsIgnoreCase(parametro.valor)
        } else {
            parametro = new Parametro()
            parametro.id = 'fecha_actual'
            parametro.valor = ''
            parametroRepository.save( parametro )
            parametroRepository.flush()
            sesionPrimera = !df.format(new Date()).equalsIgnoreCase(parametro.valor)
        }
        return sesionPrimera
    }
}

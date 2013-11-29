package mx.lux.pos.service.impl

import groovy.util.logging.Slf4j
import mx.lux.pos.model.QSucursal
import mx.lux.pos.model.Sucursal
import mx.lux.pos.model.Parametro
import mx.lux.pos.model.TipoParametro
import mx.lux.pos.repository.ParametroRepository
import mx.lux.pos.repository.SucursalRepository
import mx.lux.pos.service.SucursalService
import mx.lux.pos.service.business.Registry
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource
import java.text.DateFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat

@Slf4j
@Service( 'sucursalService' )
@Transactional( readOnly = true )
class SucursalServiceImpl implements SucursalService {

  private Comparator<Sucursal> sorter = new Comparator<Sucursal>() {
    int compare( Sucursal pSucursal_1, Sucursal pSucursal_2 ) {
      return pSucursal_1.id.compareTo( pSucursal_2.id )
    }
  }

    private static final Integer CANTIDAD_ALMACENES = 3

  @Resource
  private SucursalRepository sucursalRepository

  @Resource
  private ParametroRepository parametroRepository

  @Override
  Sucursal obtenSucursalActual( ) {
    log.debug( "obteniendo sucursal actual" )
    def parametro = parametroRepository.findOne( TipoParametro.ID_SUCURSAL.value )
    if ( parametro?.valor?.isInteger() && parametro?.valor?.toInteger() ) {
      int id = parametro?.valor?.toInteger()
      log.debug( "sucursal solicitada ${id}" )
      return sucursalRepository.findOne( id )
    }
    return null
  }

  Sucursal obtenerSucursal( Integer pSucursal ) {
    return sucursalRepository.findOne( pSucursal )
  }

  List<Sucursal> listarSucursales( ) {
    log.debug( "[Service] Listar sucursales" )
    List<Sucursal> sucursales = sucursalRepository.findAll()
    Collections.sort( sucursales, sorter )
    return sucursales
  }

  Boolean validarSucursal( Integer pSucursal ) {
    return sucursalRepository.exists( pSucursal )
  }

    List<Sucursal> listarAlmacenes( ) {
        log.debug( "[Service] Listar almacenes" )
        //List<Sucursal> lstAlmacenes = new ArrayList<>()
        QSucursal suc = QSucursal.sucursal
        List<Sucursal> lstAlmacenes = sucursalRepository.findAll( suc.nombre.startsWith('ALM') )
        Collections.sort( lstAlmacenes, sorter )
        return lstAlmacenes
    }

    List<Sucursal> listarSoloSucursales( ) {
        log.debug( "[Service] Listar solo sucursales" )
        List<Sucursal> lstSucursales = new ArrayList<>()
        List<Sucursal> sucursales = sucursalRepository.findAll()
        for(Sucursal sucursal : sucursales){
            if(!sucursal.nombre.startsWith('ALM')){
                lstSucursales.add(sucursal)
            }
        }
        Collections.sort( lstSucursales, sorter )
        return lstSucursales
    }

    String obtenerParametroFecha(){
        log.debug( "obteniendo fecha de parametro" )
        return Registry.getFechaSistema()
    }

    void registrarFechaSistema( Date fecha ){
        log.debug( "Insertando fecha en gParametro" )
        DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )
        Parametro parametro = parametroRepository.findOne(TipoParametro.FECHA_ACTUAL.value)
        if( parametro != null ){
            parametro.valor = df.format(fecha)
            parametroRepository.save(parametro)
            parametroRepository.flush()
        }
    }
}

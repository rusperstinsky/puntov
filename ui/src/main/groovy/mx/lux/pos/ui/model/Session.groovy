package mx.lux.pos.ui.model

import groovy.util.logging.Slf4j
import mx.lux.pos.ui.controller.AccessController
import org.springframework.core.io.ClassPathResource

@Slf4j
@Singleton
class Session {

  private static Map<SessionItem, Object> items = new HashMap<SessionItem, Object>()

  static boolean contains( SessionItem key ) {
    log.info( "solicitando existencia elemento: ${key}" )
    if ( items.containsKey( key ) ) {
      log.debug( 'elemento existe' )
      return true
    }
    log.debug( 'no existe elemento' )
    return false
  }

  static Object get( SessionItem key ) {
    log.info( "obteniendo elemento: ${key}" )
    Object value = items.get( key )
    if ( value != null ) {
      log.debug( "elemento obtenido: ${value}" )
      return value
    }
    log.debug( 'no existe elemento o es nulo' )
    return null
  }

  static Object put( SessionItem key, Object value ) {
    log.info( "agregando elemento: ${key}:${value}" )
    return items.put( key, value )
  }

  static Object remove( SessionItem key ) {
    log.info( "eliminando elemento: ${key}" )
    return items.remove( key )
  }

  static void clear( ) {
    log.info( "eliminando todos los elementos" )
    items.clear()
  }

  static String getVersion( ) {
    String version
    URL url = null

    try {
      url = new ClassPathResource( 'version' )?.URL
      log.debug( "ruta del archivo de version: ${url.path}" )
    } catch ( Exception e ) {
      log.info( String.format( 'No se encontró el archivo version: %s', e.getMessage() ) )
    }
    if ( url != null ) {
      log.debug( "leyendo archivo: ${url.openStream()}" )
      String line = ( url.readLines()?.first() as String )
      def elementos = line.split( ' ' )
      if ( elementos.size() >= 2 ) {
        version = String.format( '%s %s', elementos[ 0 ], elementos[ 1 ] )
      } else {
        if ( line.length() >= 10 ) {
          version = line.substring( 0, 9 )
        } else {
          version = line
        }
      }
    } else {
      version = 'SOI v1'
    }
    return version
  }

  static Integer listasPreciosPendientes(){
      return AccessController.listaPreciosPendientes()
  }

}

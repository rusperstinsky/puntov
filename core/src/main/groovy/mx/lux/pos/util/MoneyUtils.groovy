package mx.lux.pos.util

import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MoneyUtils {

  private static Logger log = LoggerFactory.getLogger( MoneyUtils.class )

  static BigDecimal parseNumber( String text ) {
    try {
      log.debug( "Parseando número: ${text}" )
      if( StringUtils.isNotBlank( text ) ) {
        String tmp = StringUtils.remove( text, '$' )
        tmp = StringUtils.remove( tmp, ',' )
        return new BigDecimal( tmp )
      }
    } catch ( Exception e ) {
      log.error( "Error al parsear el numero ${ text }: ${e.getMessage()}" )
    }
    return BigDecimal.ZERO
  }

}

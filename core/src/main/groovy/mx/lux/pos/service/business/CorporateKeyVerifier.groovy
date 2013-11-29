package mx.lux.pos.service.business

import org.apache.commons.lang3.StringUtils

class CorporateKeyVerifier {
  
  private static Integer valueOf( String pKey, Integer pDigit ) {
    Integer val = 0
    if ( ( pDigit == 1 ) || ( pDigit == 3 ) || ( pDigit == 5) ) {
      Integer n = Integer.valueOf( pKey.substring( pDigit - 1, pDigit ) )
      val = 2 * ( n % 5 ) + ( n / 5 ) 
    } else if ( ( pDigit == 2 ) || ( pDigit == 4 ) || ( pDigit == 6 ) ) {
      val = Integer.valueOf( pKey.substring( pDigit - 1, pDigit ) )
    } else if ( pDigit == 7 ) {
      Integer sum = valueOf( pKey, 1) + valueOf( pKey, 2) + valueOf( pKey, 3) +
          valueOf( pKey, 4) + valueOf( pKey, 5) + valueOf( pKey, 6)
      val = ( ( 10 - ( sum % 10 ) ) % 10 )
    }
    return val  
  }
  
  static verify( String pKey ) {
    String key = StringUtils.trimToEmpty( pKey )
    Boolean verified = true
    
    // Length == 7 and Numeric
    if ( verified ) {
      verified = ( key.length() == 7 ) && ( StringUtils.isNumeric( key ) )
    }
    
    // Dig 7 = Val 7
    if ( verified ) {
      verified = Integer.valueOf( key.substring( 6, 7 ) ) == valueOf( key, 7 )
    }
    
    return verified
  }
  
}

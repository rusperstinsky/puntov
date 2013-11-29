package mx.lux.pos.service.impl

import mx.lux.pos.service.business.CorporateKeyVerifier
import spock.lang.Specification

class CorporateKeyVerifierTest extends Specification {

  def "Test CorporateKeyVerifier"( ) {
    expect:
      CorporateKeyVerifier.verify( code ) 
    
    where:
      code << [ "2600171", "2201138", "2200932", "2200781", "2202805", "2202798", 
                "2202789", "2104546", "2052815", "2101905" ]
  }


  def "Test CorporateKeyVerifier Failed"( ) {
    expect:
      !CorporateKeyVerifier.verify( code )
    
    where:
      code << [ "2610171", "2201338", "2202932", "3200781", "1302805", "2202498",
                "220278",  "210AS46", "20528.1", "21019" ]
  }
  
}

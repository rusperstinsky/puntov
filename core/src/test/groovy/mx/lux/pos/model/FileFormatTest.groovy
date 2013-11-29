package mx.lux.pos.model

import spock.lang.Specification

class FileFormatTest extends Specification {
  
  def "test File Format Valid"( ) {
    expect:
      FileFormat.DEFAULT.equals( "default" )
      FileFormat.LUX.equals( "LUX" )
      FileFormat.SUNGLASS.equals( "s" )
      FileFormat.MAX_VISION.equals( "max" )   
      FileFormat.LUX.equals( " lu  " )   
  }

  def "test File Format invalid"( ) {
    expect:
      FileFormat.DEFAULT.equals( "lux" ) == false
      FileFormat.LUX.equals( "s" ) == false
      FileFormat.SUNGLASS.equals( "MAX" ) == false
      FileFormat.MAX_VISION.equals( "  maxvision " ) == false   
  }

}

package mx.lux.pos.model

import spock.lang.Specification

class GrupoArticuloImportLineTest extends Specification {

  def "Test adapt from import String"() {
    when:
    String line = "|D|1|GA0007|"
    GrupoArticuloImportLine record = GrupoArticuloImportLine.adapt(line)
    println record.toString()

    then:
    record.partNbr.equals('GA0007')
  }
}

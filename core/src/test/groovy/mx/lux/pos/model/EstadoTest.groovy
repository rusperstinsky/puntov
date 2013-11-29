package mx.lux.pos.model

import spock.lang.Specification

class EstadoTest extends Specification {

  def 'asigna vacio o elimina espacios de propiedades tipo string'( ) {
    when:
    actual.onPostLoad()

    then:
    nombre == actual.nombre
    edo1 == actual.edo1
    rango1 == actual.rango1
    rango2 == actual.rango2

    where:
    nombre | edo1 | rango1 | rango2 | actual
    ''     | ''   | ''     | ''     | new Estado()
    ''     | ''   | ''     | ''     | new Estado( nombre: ' ', edo1: ' ', rango1: ' ', rango2: ' ' )
    '-'    | '-'  | '-'    | '-'    | new Estado( nombre: ' - ', edo1: ' - ', rango1: ' - ', rango2: ' - ' )
  }
}

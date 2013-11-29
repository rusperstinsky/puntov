package mx.lux.pos.model

import spock.lang.Specification

class MunicipioTest extends Specification {

  def 'trimming empty spaces for string properties'( ) {
    given:
    def expected = new Municipio(
        idEstado: '00',
        idLocalidad: '000',
        nombre: 'nombre',
        rango1: '00000',
        rango2: null,
        rango3: '00000',
        rango4: null
    )
    def actual = new Municipio(
        idEstado: ' 00 ',
        idLocalidad: '000',
        nombre: ' nombre      ',
        rango1: ' 00000 ',
        rango2: '',
        rango3: '00000                  ',
        rango4: '                       '
    )

    when:
    actual.trim()

    then:
    expected.idEstado == actual.idEstado
    expected.idLocalidad == actual.idLocalidad
    expected.nombre == actual.nombre
    expected.rango1 == actual.rango1
    expected.rango2 == actual.rango2
    expected.rango3 == actual.rango3
    expected.rango4 == actual.rango4
  }
}

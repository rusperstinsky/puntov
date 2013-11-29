package mx.lux.pos.model

import spock.lang.Specification
import spock.lang.Unroll

class LocalidadTest extends Specification {

  @Unroll
  def 'trim should set null or remove spaces for string properties'( ) {
    given:
    def localidad = new Localidad(
        idEstado: idEstado,
        clase: clase,
        ciudad: ciudad,
        idLocalidad: idLocalidad,
        usuario: usuario,
        oficina: oficina,
        asentamiento: asentamiento,
        codigo: codigo,
        cor: cor
    )

    when:
    localidad.trim()

    then:
    idEstado_ == localidad.idEstado
    clase_ == localidad.clase
    ciudad_ == localidad.ciudad
    idLocalidad_ == localidad.idLocalidad
    usuario_ == localidad.usuario
    oficina_ == localidad.oficina
    asentamiento_ == localidad.asentamiento
    codigo_ == localidad.codigo
    cor_ == localidad.cor

    where:
    idEstado_ << [ null, null, null, 'text' ]
    idEstado << [ null, '', ' ', ' text ' ]
    clase_ << [ null, null, null, 'text' ]
    clase << [ null, '', ' ', ' text ' ]
    ciudad_ << [ null, null, null, 'text' ]
    ciudad << [ null, '', ' ', ' text ' ]
    idLocalidad_ << [ null, null, null, 'text' ]
    idLocalidad << [ null, '', ' ', ' text ' ]
    usuario_ << [ null, null, null, 'text' ]
    usuario << [ null, '', ' ', ' text ' ]
    oficina_ << [ null, null, null, 'text' ]
    oficina << [ null, '', ' ', ' text ' ]
    asentamiento_ << [ null, null, null, 'text' ]
    asentamiento << [ null, '', ' ', ' text ' ]
    codigo_ << [ null, null, null, 'text' ]
    codigo << [ null, '', ' ', ' text ' ]
    cor_ << [ null, null, null, 'text' ]
    cor << [ null, '', ' ', ' text ' ]
  }
}

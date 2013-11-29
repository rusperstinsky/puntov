package mx.lux.pos.model

import spock.lang.Specification

import static mx.lux.pos.assertions.Assert.assertClienteEquals

class ClienteTest extends Specification {

  def 'trimming empty spaces for string properties'( ) {
    given:
    def expected = new Cliente(
        idConvenio: '00000',
        titulo: 'cliente-titulo',
        tipoOftalmologo: 'tipo-oftalmologo',
        idLocalidad: '000',
        idEstado: '00',
        apellidoPaterno: 'apellido-paterno',
        apellidoMaterno: 'apellido-materno',
        nombre: 'nombre',
        rfc: 'rfc',
        direccion: 'direccion',
        colonia: 'colonia',
        codigo: 'codigo',
        telefonoCasa: '0000000000',
        telefonoTrabajo: '0000000000',
        extTrabajo: '00000',
        telefonoAdicional: '',
        extAdicional: '',
        email: 'email',
        idAtendio: '000000000000',
        idModificado: '0000000000',
        udf1: 'udf1',
        udf2: 'udf2',
        ori: '',
        udf4: 'udf4',
        udf5: '',
        udf6: 'udf6',
        obs: '',
        cuc: 'cuc',
        tipoImp: '',
        histCuc: 'histCuc',
        histCli: ''
    )
    def actual = new Cliente(
        idConvenio: '       00000   ',
        titulo: '           cliente-titulo    ',
        tipoOftalmologo: '  tipo-oftalmologo    ',
        idLocalidad: '      000   ',
        idEstado: '         00    ',
        apellidoPaterno: '  apellido-paterno    ',
        apellidoMaterno: '  apellido-materno    ',
        nombre: '           nombre    ',
        rfc: '              rfc   ',
        direccion: '        direccion   ',
        colonia: '          colonia   ',
        codigo: '           codigo    ',
        telefonoCasa: '     0000000000    ',
        telefonoTrabajo: '  0000000000    ',
        extTrabajo: '       00000   ',
        telefonoAdicional: '',
        extAdicional: '     ',
        email: '            email   ',
        idAtendio: '        000000000000    ',
        idModificado: '     0000000000    ',
        udf1: '             udf1    ',
        udf2: '             udf2    ',
        udf4: '             udf4    ',
        udf5: '             ',
        udf6: '             udf6    ',
        cuc: '              cuc   ',
        tipoImp: '',
        histCuc: '          histCuc   '
    )

    when:
    actual.onPostLoad()

    then:
    assertClienteEquals( expected, actual )
  }

  def 'get cliente full name as string'( ) {
    given:
    def cliente = new Cliente(
        titulo: titulo,
        nombre: nombre,
        apellidoPaterno: apellidoPaterno,
        apellidoMaterno: apellidoMaterno
    )

    when:
    def actual = cliente.nombreCompleto( conTitulo )

    then:
    expected == actual

    where:
    expected                | conTitulo | titulo | nombre    | apellidoPaterno | apellidoMaterno
    ''                      | true      | null   | null      | null            | null
    ''                      | true      | ''     | ''        | ''              | ''
    'pedro'                 | true      | null   | 'pedro'   | null            | null
    'sr pedro'              | true      | 'sr'   | 'pedro'   | null            | null
    'paramo'                | true      | null   | null      | 'paramo'        | null
    'sr paramo'             | true      | 'sr'   | null      | 'paramo'        | null
    'pedro paramo'          | true      | null   | 'pedro'   | 'paramo'        | null
    'sr pedro paramo'       | true      | 'sr'   | 'pedro'   | 'paramo'        | null
    'pedro paramo lopez'    | true      | null   | 'pedro'   | 'paramo'        | 'lopez'
    'sr pedro paramo lopez' | true      | 'sr'   | 'pedro'   | 'paramo'        | 'lopez'
    'sr pedro paramo lopez' | true      | ' sr ' | ' pedro ' | ' paramo '      | ' lopez '
    ''                      | false     | null   | null      | null            | null
    ''                      | false     | ''     | ''        | ''              | ''
    'pedro'                 | false     | null   | 'pedro'   | null            | null
    'pedro'                 | false     | 'sr'   | 'pedro'   | null            | null
    'paramo'                | false     | null   | null      | 'paramo'        | null
    'paramo'                | false     | 'sr'   | null      | 'paramo'        | null
    'pedro paramo'          | false     | null   | 'pedro'   | 'paramo'        | null
    'pedro paramo'          | false     | 'sr'   | 'pedro'   | 'paramo'        | null
    'pedro paramo lopez'    | false     | null   | 'pedro'   | 'paramo'        | 'lopez'
    'pedro paramo lopez'    | false     | 'sr'   | 'pedro'   | 'paramo'        | 'lopez'
    'pedro paramo lopez'    | false     | ' sr ' | ' pedro ' | ' paramo '      | ' lopez '
  }
}

package mx.lux.pos.service.impl

import mx.lux.pos.model.Localidad
import mx.lux.pos.repository.LocalidadRepository
import mx.lux.pos.service.LocalidadService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

@Service( 'localidadService' )
@Transactional( readOnly = true )
class LocalidadServiceImpl implements LocalidadService {

  @Resource
  private LocalidadRepository localidadRepository

  @Override
  List<Localidad> listaLocalidadesPorEstadoYMunicipio( String estado, String municipio ) {
    localidadRepository.findByMunicipioEstadoNombreAndMunicipioNombreOrderByUsuarioAsc( estado, municipio ) ?: [ ]
  }

  @Override
  List<Localidad> listaLocalidadesPorCodigoYNombre( String codigo, String nombre ) {
    localidadRepository.findByCodigoAndUsuario( codigo, nombre ) ?: [ ]
  }

  @Override
  List<Localidad> listaLocalidadesPorCodigo( String codigo ) {
    localidadRepository.findByCodigo( codigo ) ?: [ ]
  }
}

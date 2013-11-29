package mx.lux.pos.service.impl

import mx.lux.pos.model.Municipio
import mx.lux.pos.repository.MunicipioRepository
import mx.lux.pos.service.MunicipioService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

@Service( 'municipioService' )
@Transactional( readOnly = true )
class MunicipioServiceImpl implements MunicipioService {

  @Resource
  private MunicipioRepository municipioRepository

  @Override
  List<Municipio> listaMunicipiosPorEstado( String estado ) {
    municipioRepository.findByEstadoNombreOrderByNombreAsc( estado ) ?: [ ]
  }
}

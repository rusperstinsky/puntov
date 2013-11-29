package mx.lux.pos.service.impl

import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import mx.lux.pos.model.InstitucionIc
import mx.lux.pos.service.ConvenioService
import mx.lux.pos.model.QInstitucionIc
import mx.lux.pos.repository.ConvenioRepository
import javax.annotation.Resource
import javax.sql.rowset.Predicate

@Slf4j
@Service( "convenioService" )
@Transactional( readOnly = true )
class ConvenioServiceImpl implements  ConvenioService {

    @Resource
    ConvenioRepository convenioRepository

    @Override
    List<InstitucionIc> obtenerConvenios(  String clave ){
        println "obtenerConvenios()"
        List<InstitucionIc> lstConvenios = new ArrayList<InstitucionIc>()

        try{
            QInstitucionIc convenio = QInstitucionIc.institucionIc
            lstConvenios = convenioRepository.findAll( convenio.estatusConvenio.eq("V").
                    and( convenio.inicialesIc.like("%"+clave+"%").or( convenio.id.like("%"+clave+"%") ) ) ) as List<InstitucionIc>
        } catch( Exception e ){
            log.error( "Error al obtener la lista de convenios:", e )
        }

        return lstConvenios
    }

}

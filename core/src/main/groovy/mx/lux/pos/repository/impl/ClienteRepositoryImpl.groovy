package mx.lux.pos.repository.impl

import java.util.Date;
import java.util.List;

import com.mysema.query.jpa.JPQLQuery
import com.mysema.query.types.Predicate
import mx.lux.pos.model.Cliente
import mx.lux.pos.model.Devolucion;
import mx.lux.pos.model.QCliente
import mx.lux.pos.repository.custom.ClienteRepositoryCustom

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport

class ClienteRepositoryImpl extends QueryDslRepositorySupport implements ClienteRepositoryCustom {

  @Override
  List<Cliente> findByNombreApellidos( String nombre, String apellidoPaterno, String apellidoMaterno ) {
    QCliente cliente = QCliente.cliente
    def predicates = [ ]
    if ( StringUtils.isNotBlank( nombre ) ) {
      predicates.add( cliente.nombre.startsWithIgnoreCase( nombre ) )
    }
    if ( StringUtils.isNotBlank( apellidoPaterno ) ) {
      predicates.add( cliente.apellidoPaterno.startsWithIgnoreCase( apellidoPaterno ) )
    }
    if ( StringUtils.isNotBlank( apellidoMaterno ) ) {
      predicates.add( cliente.apellidoMaterno.startsWithIgnoreCase( apellidoMaterno ) )
    }
    JPQLQuery query = from( cliente )
    query.where( predicates as Predicate[] )
    return query.list( cliente )
  }
  
  @Override
  List<Cliente> findByFechaAlta( Date fecha ) {
	if ( fecha != null ) {
	  QCliente cliente = QCliente.cliente
	  def predicates = [ ]
	  Date fechaInicio = fecha
	  Date fechaFin = DateUtils.addDays( fecha, 1 )
	  predicates.add( cliente.fechaAlta.between( fechaInicio, fechaFin ) )
	  JPQLQuery query = from( cliente )
	  query.where( predicates as Predicate[] )
	  return query.list( cliente )
	}
	[ ]
  }
}

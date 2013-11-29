package mx.lux.pos.service.impl

import mx.lux.pos.model.Empleado
import mx.lux.pos.model.EmployeeAdapter
import mx.lux.pos.repository.EmpleadoRepository
import mx.lux.pos.repository.impl.RepositoryFactory
import mx.lux.pos.service.business.EmployeeImportTask
import mx.lux.pos.service.io.EmployeeReader
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration( 'classpath:spring-config.xml' )
class EmployeeImportTaskIntegration extends Specification {

  def "Test import task run"( ) {
    setup:
    File file = new File( '/soi/data/Empleados/emp.30-01-13.txt' )
    EmployeeImportTask task = new EmployeeImportTask()
    task.setInputFile( file )

    when:
    task.run()

    then:
    true
  }

  def "Test Compare and Record Actual Password task run"( ) {
    setup:
    File file = new File( '/soi/data/Empleados/emp.30-01-13.txt' )
    EmployeeReader reader = new EmployeeReader( file )
    PrintWriter writer = new PrintWriter( '/soi/data/Empleados/empUpdated.txt' )
    EmpleadoRepository catalog = RepositoryFactory.employeeCatalog

    when:
    EmployeeAdapter adapter = reader.read()
    while ( adapter != null ) {
      Empleado emp = catalog.findOne( adapter.idEmpleado )
      EmployeeAdapter adapter2 = null
      if ( emp != null ) {
        adapter2 = EmployeeAdapter.toAdapter( emp, adapter.getUserId() )
      } else {
        adapter2 = adapter
      }
      writer.println( adapter2.toExportString() )
      adapter = reader.read()
    }
    reader.close()
    writer.close()

    then:
    true
  }

}

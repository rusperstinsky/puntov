package mx.lux.pos.service.business

import mx.lux.pos.model.Empleado
import mx.lux.pos.model.EmployeeAdapter
import mx.lux.pos.repository.EmpleadoRepository
import mx.lux.pos.repository.impl.RepositoryFactory
import mx.lux.pos.service.io.EmployeeReader
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class EmployeeImportTask implements Runnable {

  private Logger logger = LoggerFactory.getLogger( this.getClass() )
  private Integer nUpdated, nRead
  private File inFile
  private EmployeeReader reader
  private Integer site

  // Internal methods
  protected Empleado findOrCreate( String pIdEmpleado ) {
    EmpleadoRepository catalog = RepositoryFactory.employeeCatalog
    Empleado emp = catalog.findOne( pIdEmpleado )
    if ( emp == null ) {
      emp = new Empleado()
      emp.id = pIdEmpleado
      emp.idSucursal = this.getSite()
      catalog.save( emp )
    }
    return emp
  }

  protected EmployeeReader getReader( ) {
    if ( this.reader == null ) {
      this.reader = new EmployeeReader( inFile )
    }
    return this.reader
  }

  protected Integer getSite( ) {
    if ( this.site == null ) {
      this.site = Registry.currentSite
    }
    return this.site
  }

  protected void update( Empleado pDbRecord, EmployeeAdapter pAdapter ) {
    pAdapter.update( pDbRecord )
    EmpleadoRepository catalog = RepositoryFactory.employeeCatalog
    catalog.save( pDbRecord )
  }

  // Public methods
  Integer getReadCount( ) {
    return this.nRead;
  }

  Integer getUpdatedCount( ) {
    return this.nUpdated;
  }

  void run( ) {
    this.logger.debug( String.format( "[STARTED] Import Empleado (%s)", this.inFile.getName() ) )
    this.nUpdated = 0
    this.nRead = 0
    if ( ( this.inFile != null ) && ( this.inFile.exists() ) ) {
      EmployeeAdapter employee = this.getReader().read()
      while ( employee != null ) {
        this.nRead++
        if ( employee.isValid() ) {
          Empleado dbEmpleado = this.findOrCreate( employee.idEmpleado )
          this.update( dbEmpleado, employee )
          nUpdated++
        }
        employee = this.getReader().read()
      }
      this.getReader().close()
      if ( this.nUpdated > 0 ) {
        RepositoryFactory.employeeCatalog.flush()
      }
    }
    this.logger.debug( String.format( '[FINISHED] Import Empleado  Updated:%,d/%,d', this.nUpdated, this.nRead ) )
  }

  void setInputFile( File pFile ) {
    this.inFile = pFile
  }

}

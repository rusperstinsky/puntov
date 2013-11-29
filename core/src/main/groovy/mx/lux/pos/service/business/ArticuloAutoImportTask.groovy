package mx.lux.pos.service.business

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ArticuloAutoImportTask {

  private Logger logger

  // Logger Methods
  protected debug( String pMessage ) {
    this.getLogger().debug( pMessage )
  }

  protected info( String pMessage ) {
    this.getLogger().info( pMessage )
  }

  protected Logger getLogger() {
    if ( this.logger == null ) {
      this.logger = LoggerFactory.getLogger(this.getClass())
    }
    return this.logger
  }

  // Internal methods

  // Public Methods
  void run( ) {
    // for each file in ruta_recibidos matches articulo pattern
    //   importTask(file).run()
    //   move file
    // end.
  }
}

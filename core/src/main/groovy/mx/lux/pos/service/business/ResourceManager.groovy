package mx.lux.pos.service.business

import mx.lux.pos.model.FileFormat
import mx.lux.pos.model.Parametro
import mx.lux.pos.model.TipoParametro
import mx.lux.pos.service.io.InvTrFile
import mx.lux.pos.service.io.ShippingNoticeFile
import mx.lux.pos.service.io.ShippingNoticeFileSunglass
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ResourceManager {

  static final String MSG_ERROR_CREATE_LOCATION = "Error al crear la ruta especificada <%s>"

  static Logger log = LoggerFactory.getLogger( ResourceManager.class )

  static File getLocation( TipoParametro pParametroRuta ) {
    Parametro p = Registry.find( pParametroRuta )
    File f = new File( p.valor )
    if ( !f.exists() ) {
      try {
        f.mkdir()
      } catch ( Exception e ) {
        String msg = String.format( MSG_ERROR_CREATE_LOCATION, f.absolutePath )
        log.debug( msg, e )
      }
    }
    if ( !f.exists() ) {
      f = new File( "." )
    }
    return f.absoluteFile
  }

  static InvTrFile getInvTrFile( ) {
    InvTrFile file = new InvTrFile()
    FileFormat f = Registry.currentFileFormat
    if ( FileFormat.SUNGLASS.equals( f ) ) {
      file = new InvTrFile()
    }
    return file
  }

  static ShippingNoticeFile getShippingNoticeFile( ) {
    ShippingNoticeFile file
    FileFormat f = Registry.currentFileFormat
    if ( FileFormat.SUNGLASS.equals( f ) ) {
      file = new ShippingNoticeFileSunglass()
    }
    return file
  }

}

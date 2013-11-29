package mx.lux.pos.service.business

import mx.lux.pos.model.Articulo
import mx.lux.pos.repository.ArticuloRepository
import mx.lux.pos.repository.impl.RepositoryFactory
import mx.lux.pos.service.impl.ServiceFactory
import mx.lux.pos.service.io.ArticuloClassAdapter
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ArticuloClassReaderTask {

  private static final String MSG_READING_FILE = 'Leyendo archivo: %s'
  private String fileName

  private Logger log = LoggerFactory.getLogger( this.getClass() )
  private Map<Integer, ArticuloClassAdapter> partData = new TreeMap<Integer, ArticuloClassAdapter>()
  private Integer nFiles, nLines, nUpdated

  // Protected Methods
  protected void importPartClass( File pFile ) {
    log.debug( String.format( MSG_READING_FILE, pFile.name ) )
    nFiles++
    Integer nRead = 0
    pFile.eachLine { String pLine ->
      ArticuloClassAdapter partClass = new ArticuloClassAdapter( pLine )
      if ( partClass.isValid() ) {
        partData.put( partClass.sku, partClass )
      }
      nRead++
    }
    nLines += nRead
  }

  protected Boolean updatePartClass( ) {
    log.debug( String.format( "Actualizando articulos: %,d en lista", partData.size() ) )
    Boolean actualizado = false
    ArticuloRepository catalog = RepositoryFactory.partMaster
    try {
      List<Articulo> partList = new ArrayList<Articulo>()
      for ( ArticuloClassAdapter adapter : partData.values() ) {
        Articulo part = catalog.findOne( adapter.sku )
        if ( part != null ) {
          adapter.updatePart( part )
          partList.add( part )
          nUpdated++
        }
      }
      actualizado = ServiceFactory.partMaster.registrarListaArticulos( partList )
    } catch ( Exception e ) {
      log.error( "[Service] ERROR! Actualizando articulos", e )
    }
    return actualizado
  }

  // Public Methods
  Integer getFileCount( ) {
    return nFiles
  }

  String getFilename( ) {
    return String.format( "%s%s%s", Registry.getInputFilePath(), File.separator, fileName )
  }

  Integer getLinesRead( ) {
    return nLines
  }

  Integer getPartCount( ) {
    return partData.size()
  }

  Integer getPartUpdatedCount( ) {
    return nUpdated
  }

  void run( File pFile ) {
    partData.clear()
    nFiles = 0
    nLines = 0
    nUpdated = 0
      fileName = pFile.name
      importPartClass( pFile )
    if ( partData.size() > 0 ) {
      updatePartClass()
    }
  }

}



package mx.lux.pos.service.io

import mx.lux.pos.model.MessageImportAdapter
import java.text.ParseException
import mx.lux.pos.service.business.Registry
import mx.lux.pos.model.Mensaje
import mx.lux.pos.repository.impl.RepositoryFactory
import org.apache.commons.lang3.StringUtils

class MessageImportTask {

  String filename

  // Internal methods
  protected List<MessageImportAdapter> read( ) {
    List<MessageImportAdapter> list = new ArrayList<MessageImportAdapter>()
    File file = new File( this.getFilename() )
    if ( file.exists() ) {
      file.eachLine { String line ->
        try {
          MessageImportAdapter adapter = MessageImportAdapter.parse( line )
          list.add( adapter )
        } catch ( ParseException e ) {

        }
      }
      File target = new File( Registry.processedFilesPath + File.separator + file.getName() )
      if ( target.exists() ) {
        target.delete()
      }
      file.renameTo( target )
    }
    return list
  }

  protected Collection<Mensaje> update( List<MessageImportAdapter> pInputList) {
    Map<Integer, Mensaje> mensajes = new TreeMap<Integer, Mensaje>()
    for ( MessageImportAdapter adapter : pInputList ) {
      Mensaje msj = mensajes.get(adapter.id)
      if ( msj == null ) {
        msj = RepositoryFactory.messageCatalog.findOne( adapter.id )
        if (msj == null) {
          msj = new Mensaje()
          msj.id = adapter.id
        }
        mensajes.put( msj.id, msj )
      }
      msj.texto = adapter.text
    }
    return mensajes.values()
  }

  // public methods
  String getFilename( ) {
    String name = StringUtils.trimToNull(this.filename)
    if ( name == null ) {
      name = Registry.messageFile
    }
    return name
  }

  void run( ) {
    List<MessageImportAdapter> list = this.read()
    Collection<Mensaje> mensajes = this.update( list )
    RepositoryFactory.messageCatalog.save( mensajes )
    RepositoryFactory.messageCatalog.flush()
  }

  void setFilename( String pFilename ) {
    this.filename = StringUtils.trimToEmpty( pFilename )
  }
}

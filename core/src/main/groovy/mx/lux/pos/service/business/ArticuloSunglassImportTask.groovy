package mx.lux.pos.service.business

import mx.lux.pos.model.AdaptadorArticulo
import mx.lux.pos.model.Articulo
import mx.lux.pos.model.ArticuloSunglass

import mx.lux.pos.model.Generico
import mx.lux.pos.repository.impl.RepositoryFactory
import mx.lux.pos.service.impl.ServiceFactory
import mx.lux.pos.service.io.PartFileSunglass
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ArticuloSunglassImportTask {

  private static final String MSG_PARSE_SKU_NBR = "ERROR! No se puede identificar el SKU#"
  private static final String MSG_GENRE_UNDEFINED = "ERROR! Generico no registrado"

  private String filename
  private PartFileSunglass file
  private PrintStream errorFile
  private Integer nRead, nUpdated, nError
  private Integer site
  private Logger logger

  // Internal Methods
  protected debug( String pMessage ) {
    this.getLogger().debug( pMessage )
  }

  protected info( String pMessage ) {
    this.getLogger().info( pMessage )
  }

  protected Articulo findOrCreate( Integer pSku ) {
    Articulo part = RepositoryFactory.partMaster.findOne( pSku )
    if ( part == null ) {
      part = new Articulo()
      part.id = pSku
      part.idSucursal = this.getSite( )
    }
    return part
  }

  protected PrintStream getErrorFile() {
    if ( this.errorFile == null ) {
      this.errorFile = new PrintStream( this.filename + ".err" )
    }
    return this.errorFile
  }

  protected PartFileSunglass getFile( ) {
    if ( this.hasFilename() && ( this.file == null ) ) {
      this.file = new PartFileSunglass( this.filename )
    }
    return this.file
  }

  protected String getFilename( ) {
    return this.filename
  }

  protected getLogger( ) {
    if ( this.logger == null ) {
      this.logger = LoggerFactory.getLogger( this.getClass() )
    }
    return this.logger
  }

  protected Integer getSite( ) {
    if ( this.site == null ) {
      this.site = ServiceFactory.sites.obtenSucursalActual().id
    }
    return this.site
  }

  protected Boolean hasFilename( ) {
    return ( this.filename != null )
  }

  protected Boolean isValid( ArticuloSunglass pSunglassPart ) {
    Boolean valid = true

    if ( pSunglassPart.sku == null ) {
      this.reportError( pSunglassPart, MSG_PARSE_SKU_NBR )
      valid = false
    }

    if ( pSunglassPart.genre != null ) {
      Generico genre = null
      if ( pSunglassPart.genre != null ) {
        genre = RepositoryFactory.genres.findOne( pSunglassPart.genre )
      }
      if ( genre == null ) {
        this.reportError( pSunglassPart, MSG_GENRE_UNDEFINED )
        valid = false
      }
    }

    if ( !valid ) {
      this.nError++
    }
    return valid
  }

  protected void reportError( ArticuloSunglass pSunglassPart, String pErrorMessage ) {
    this.info( String.format( "[%,d] %s", this.nRead, pErrorMessage ) )
    this.getErrorFile().println( String.format( "[%,d] %s", this.nRead, pErrorMessage ) )
    this.info( String.format( "[%,d] %s", this.nRead, pSunglassPart.toString() ) )
    this.getErrorFile().println( String.format( "[%,d] %s", this.nRead, pSunglassPart.toString() ) )
  }

  protected void updateArticulo( Articulo pPart, AdaptadorArticulo pSunglass ) {
    if( !pSunglass.articulo.equalsIgnoreCase('error') ){
    if ( pSunglass.articulo != null ){
      pPart.articulo = pSunglass.articulo
    }
    if( pSunglass.descripcion != null ){
      pPart.descripcion = pSunglass.descripcion
    }
    if( pSunglass.precio != null ){
      pPart.precio = pSunglass.precio
    }
    if( pSunglass.generico != null){
      pPart.idGenerico = pSunglass.generico
    }
    if( pSunglass.tipo != null ){
      pPart.tipo = pSunglass.tipo
    }
    if( pSunglass.subtipo != null ){
      pPart.subtipo = pSunglass.subtipo
    }
    if( pSunglass.marca != null ){
      pPart.marca = pSunglass.marca
    }

    ServiceFactory.partMaster.registrarArticulo( pPart )
    }
  }

  // Public Methods
  Integer getErrorCount( ) {
    return this.nError
  }

  Integer getUpdatedCount( ) {
    return this.nUpdated
  }

  Integer getRecordCount( ) {
    return this.nRead
  }


  void run( ) {
    this.debug( String.format( "[STARTED] Import Articulo Sunglass (%s)", this.filename ) )
    this.nError = 0
    this.nUpdated = 0
    this.nRead = 0
    if ( this.getFile() != null ) {
      AdaptadorArticulo partSunglass = this.getFile().readNew()
      while ( partSunglass != null ) {
        this.nRead++
        //if ( isValid( partSunglass ) ) {
          Articulo part = findOrCreate( partSunglass.sku )
          updateArticulo( part, partSunglass )
          nUpdated++
        //}
        partSunglass = this.file.read()
      }
      this.getFile().close()
    }
    if ( this.nUpdated > 0 ) {
      RepositoryFactory.partMaster.flush( )
    }
    if ( this.errorFile != null ) {
      this.getErrorFile().close()
    }
    this.debug( String.format( "[FINISHED] Import Articulo Sunglass  Updated:%,d/%,d  Error:%,d",
        this.nUpdated, this.nRead, this.nError
    ) )
  }

  void setFilename( String pFilename ) {
    this.file = null
    this.filename = StringUtils.trimToNull( pFilename )
    if ( ( this.filename != null ) && ( new File( this.filename ).exists() ) ) {
      this.filename = pFilename
    } else {
      this.filename = null
      throw new FileNotFoundException( pFilename )
    }
  }


    String runNew( File file ) {
        String skuNoCargado = ''
        this.debug( String.format( "[STARTED] Import Articulo Sunglass (%s)", this.filename ) )
        this.nError = 0
        this.nUpdated = 0
        this.nRead = 0
        if ( file.exists() ) {
          file.eachLine { String line ->
            AdaptadorArticulo partSunglass = this.getFile().readNew( line )
            this.nRead++
            if( partSunglass.articulo != null ){
              if( 'error'.equalsIgnoreCase(partSunglass.articulo) || partSunglass.articulo.length() > 20){
                skuNoCargado = skuNoCargado+','+partSunglass.sku.toString().trim()
              } else {
                Articulo part = findOrCreate( partSunglass.sku )
                updateArticulo( part, partSunglass )
                nUpdated++
              }
            }
            //this.getFile().close()
          }
        }
        skuNoCargado = skuNoCargado.replaceFirst( ",","" )
        if ( this.nUpdated > 0 ) {
            RepositoryFactory.partMaster.flush( )
        }
        if ( this.errorFile != null ) {
            this.getErrorFile().close()
        }
        this.debug( String.format( "[FINISHED] Import Articulo Sunglass  Updated:%,d/%,d  Error:%,d",
                this.nUpdated, this.nRead, this.nError
        ) )
        return skuNoCargado
    }
}

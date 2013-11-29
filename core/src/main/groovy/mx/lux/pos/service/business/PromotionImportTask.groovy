package mx.lux.pos.service.business

import mx.lux.pos.model.Promocion
import mx.lux.pos.repository.ParametroRepository
import mx.lux.pos.repository.SucursalRepository
import mx.lux.pos.service.io.PromotionsAdapter
import mx.lux.pos.util.StringList
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.annotation.Resource

class PromotionImportTask {

  static PromotionImportTask instance = new PromotionImportTask()

  private PromotionImportTask( ) { }

  private Map<Integer, PromotionsAdapter> prData = new TreeMap<Integer, PromotionsAdapter>()

  @Resource
  private ParametroRepository parametroRepository

  @Resource
  private SucursalRepository sucursalRepository


  private String filename
  PromotionsAdapter promotionsAdapter
  private Logger logger
  private Promocion promocion = new Promocion()

  // Internal Methods
  protected debug( String pMessage ) {
    this.getLogger().debug( pMessage )
  }

  protected info( String pMessage ) {
    this.getLogger().info( pMessage )
  }

  protected getLogger( ) {
    if ( this.logger == null ) {
      this.logger = LoggerFactory.getLogger( this.getClass() )
    }
    return this.logger
  }


  protected void leerFicheroPR( File pFile ) {
    this.debug( "LeerFicheroPR( )" )
    try {
      Integer nRead = 0
      String renglones
      pFile.eachLine { String pLine ->
        if ( !pLine.startsWith( '|D|' ) ) {
          PromotionsAdapter pr = new PromotionsAdapter( pLine )
          if ( pr.idPromocion != null ) {
            prData.put( pr.idPromocion, pr )
          }
        }
      }
    } catch ( Exception ex ) {
      System.out.println( ex )
    }
  }


  protected List<String> leerFicheroPRGrupos( File pFile ) {
    this.debug( "leerFicheroPRGrupos( )" )
    List<String> lstGrupos = new ArrayList<String>()

    pFile.eachLine { String pLine ->
      if ( pLine.startsWith( '|D|' ) ) {
        lstGrupos.add( pLine )
      }
      return lstGrupos
    }
  }

  public List<String> runGroupPromotions( String ubicacionSource, String ubicacionsDestination ) {
    this.debug( 'runGroupPromotions( )' )
    StringList nameFile
    List<String> grupos = new ArrayList<String>()

    File source = new File( ubicacionSource )
    File destination = new File( ubicacionsDestination )
    if ( source.exists() && destination.exists() ) {
      source.eachFile() { file ->
        nameFile = new StringList( file.getName(), "_" )
        if ( file.getName().endsWith( "PR" ) ) {
          grupos = leerFicheroPRGrupos( file )
          println "Lineas de grupo:: ${grupos.size()}"
        }
      }
    }
    return grupos
  }


  public List<PromotionsAdapter> run( String ubicacionSource, String ubicacionsDestination ) {
    this.debug( "PromotionImportTask.run()" )
    prData.clear()
    StringList nameFile

    File source = new File( ubicacionSource )
    File destination = new File( ubicacionsDestination )
    if ( source.exists() && destination.exists() ) {
      source.eachFile() { file ->
        nameFile = new StringList( file.getName(), "_" )
        if ( file.getName().endsWith( "PR" ) ) {
          this.leerFicheroPR( file )
          def newFile = new File( destination, file.name )
          def moved = file.renameTo( newFile )
        }
      }
    }
    return new ArrayList<PromotionsAdapter>( prData.values() )
  }
}
package mx.lux.pos.service.io

import mx.lux.pos.service.business.Registry
import mx.lux.pos.util.CustomDateUtils
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import mx.lux.pos.model.TipoParametro
import org.hibernate.type.CharacterArrayClobType

class ArchiveTask {

  private static final String FILE_ARCHIVE_DEFAULT = 'soi.%s'
  private static final String FMT_DATE_TIME = 'yyyy-MM-dd-HH-mm'
  private static final String EXT_ZIP = '.zip'
  private static final String SO_WINDOWS = 'Windows'

  private Logger logger = LoggerFactory.getLogger( this.getClass() )
  private String baseDir
  private String filePattern
  private String archiveFile

  // Internal methods
  protected String getArchiveFile( ) {
    String filename = this.archiveFile
    if ( filename == null ) {
      filename = String.format( FILE_ARCHIVE_DEFAULT, CustomDateUtils.format( new Date(), FMT_DATE_TIME ) )
    }
    return Registry.archivePath + File.separator + filename + EXT_ZIP
  }


  protected String getArchiveFileDropbox( ) {
      String filename = this.archiveFile
      if ( filename == null ) {
          filename = String.format( FILE_ARCHIVE_DEFAULT, CustomDateUtils.format( new Date(), FMT_DATE_TIME ) )
      }
      return Registry.archivePathDropbox + Registry.currentSite + File.separator + filename + EXT_ZIP
  }
  // Public methods
  void run( ) {
    if ( ( this.filePattern != null ) && ( this.baseDir != null ) ) {
      String sSistemaOperativo = System.getProperty("os.name");
      logger.debug(sSistemaOperativo);
      StringBuffer sb = new StringBuffer()
      StringBuffer sbDrop = new StringBuffer()
      sb.append( String.format( "%s ", Registry.archiveCommand ) );
      sbDrop.append( String.format( "%s ", Registry.archiveCommand ) );
      if( sSistemaOperativo.trim().startsWith( SO_WINDOWS ) ){
        sb.append( String.format( '"%s" ', this.getArchiveFile() ) );
        sb.append( String.format( '"%s" ', this.baseDir + File.separator + this.filePattern ) )
        sbDrop.append( String.format( '"%s" ', this.getArchiveFileDropbox() ) );
        sbDrop.append( String.format( '"%s" ', this.baseDir + File.separator + this.filePattern ) )
      } else {
        sb.append( String.format( '%s ', this.getArchiveFile() ) );
        sb.append( String.format( '%s ', this.baseDir + File.separator + this.filePattern ) )
        sbDrop.append( String.format( '%s ', this.getArchiveFileDropbox() ) );
        sbDrop.append( String.format( '%s ', this.baseDir + File.separator + this.filePattern ) )
      }
      StringBuffer sb2 = new StringBuffer()
      for ( char c : sb.toString().toCharArray() ) {
        if ( ( c == '\\' ) || ( c == '/' ) ) {
          sb2.append( File.separator )
        } else {
          sb2.append( c )
        }
      }

      StringBuffer sb3 = new StringBuffer()
      for ( char c : sbDrop.toString().toCharArray() ) {
          if ( ( c == '\\' ) || ( c == '/' ) ) {
              sb3.append( File.separator )
          } else {
              sb3.append( c )
          }
      }
      String cmd = sb2.toString()
      String cmd1 = sb3.toString()
      logger.debug( String.format( "ZIP Command: <%s>", cmd ) )
      logger.debug( String.format( "ZIP Command: <%s>", cmd1 ) )

      File f = new File( this.getArchiveFile() )
      if ( f.exists() ) {
        f.delete()
      }

      try {
        cmd1.execute()
        cmd.execute()
      } catch ( Exception e ) {
        logger.error( e.getMessage(), e )
      }
      //    AntBuilder ant = new AntBuilder()
      //      logger.debug( String.format( 'Zipping <%s> into <%s>', this.filePattern, this.getArchiveFile() ) )
      //      ant.zip( destfile: this.getArchiveFile(),
      //               basedir: this.baseDir,
      //               includes: this.filePattern
      //      )
      //      if (f.exists()) {
      //        logger.debug( String.format( 'Archive file: <%s> %,d bytes', f.getAbsolutePath(), f.size() ) )
      //      }
    } else {
      logger.debug( 'Nothing to archive.' )
    }

  }

  void setArchiveFile( String pFilename ) {
    this.archiveFile = StringUtils.trimToEmpty( pFilename )
  }

  void setBaseDir( String pBaseDir ) {
    this.baseDir = StringUtils.trimToEmpty( pBaseDir )
  }

  void setFilePattern( String pFilePattern ) {
    this.filePattern = StringUtils.trimToEmpty( pFilePattern )
  }

}

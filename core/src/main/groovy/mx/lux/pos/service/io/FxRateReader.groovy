package mx.lux.pos.service.io

import mx.lux.pos.model.FxRateAdapter
import mx.lux.pos.util.StringList
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class FxRateReader {

  private static final String DEFAULT_DELIMITER = '|'

  private Logger logger = LoggerFactory.getLogger( this.getClass() )
  private BufferedReader reader
  private File inputFile
  private String delimiter

  FxRateReader( File pInputFile ) {
    this.inputFile = pInputFile
  }

  protected BufferedReader getReader( ) {
    if ( this.reader == null ) {
      try {
        this.reader = new BufferedReader( new FileReader( this.inputFile ) )
      } catch ( FileNotFoundException e ) {
        logger.error( e.getMessage() )
      }
    }
    return this.reader
  }

  protected String getDelimiter( ) {
    String del = this.delimiter
    if ( del == null ) {
      del = DEFAULT_DELIMITER
    }
    return del
  }

  // Public methods
  void close( ) {
    if ( this.getReader() != null ) {
      try {
        this.getReader().close()
      } catch ( IOException e ) {
        logger.error( e.getMessage() )
      }
    }
  }

  FxRateAdapter read( ) {
    FxRateAdapter adapter = null
    if ( this.getReader() != null ) {
      String line = this.getReader().readLine()
      while ( ( line != null ) && ( adapter == null ) ) {
        try {
          adapter = FxRateAdapter.parse( new StringList( line, this.getDelimiter() ) )
        } catch ( Exception e ) {
          logger.info( e.getMessage() )
        }
        if ( adapter == null ) {
          line = this.getReader().readLine()
        }
      }
    }
    return adapter
  }

}

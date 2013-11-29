package mx.lux.pos.model

import org.apache.commons.lang3.StringUtils

import java.util.regex.Pattern

class LocalFilenameFilter implements FilenameFilter {

  static final FilenameFilter ALL_FILES = new FilenameFilter() {
    boolean accept( File dir, String name ) {
      return true
    }
  }
  private String regex
  private Pattern pattern

  LocalFilenameFilter( String pRegex ) {
    this.regex = StringUtils.trimToNull( pRegex )
  }

  protected Pattern getPattern( ) {
    Pattern p = null
    if ( this.regex != null ) {
      p = Pattern.compile( this.regex )
    }
    return p
  }

  boolean accept( File dir, String name ) {
    boolean accepted = true
    if ( this.regex != null ) {
      accepted = StringUtils.trimToEmpty( name ).toLowerCase().matches( this.getPattern() )
    }
    return accepted
  }
}

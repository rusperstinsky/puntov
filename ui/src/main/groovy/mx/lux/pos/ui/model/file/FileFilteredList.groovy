package mx.lux.pos.ui.model.file

class FileFilteredList {

  private List<FileFiltered> files
  private FileNamePattern pattern

  FileFilteredList( String pFilenamePattern ) {
    this.pattern = new FileNamePattern( pFilenamePattern )
    this.files = new ArrayList<FileFiltered>()
  }

  void add( File pFile ) {
    FileFiltered f = this.pattern.matches( pFile )
    if ( f != null ) {
      this.files.add( f )
      Collections.sort( this.files )
    }
  }

  File getFile( int ix ) {
    File f = null
    if ( ( ix >= 0 ) && ( ix < this.files.size() ) ) {
      f = this.files.get( ix ).file
    }
    return f
  }

  Boolean isEmpty() {
    return (this.files.size() == 0)
  }

  File pop( ) {
    File f = null
    if ( this.files.size() > 0 ) {
      f = this.getFile( 0 )
      this.files.remove( 0 )
    }
    return f
  }

  String toString( ) {
    StringBuffer sb = new StringBuffer()
    for ( FileFiltered f : this.files ) {
      sb.append( String.format( '%s\n', f.getFile().getName() ) )
    }
    return sb.toString()
  }

}

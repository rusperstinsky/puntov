package mx.lux.pos.ui.model

import mx.lux.pos.ui.resources.ServiceManager

class FileList {

  static final FileList PartMasterFiles = new FileList( ServiceManager.ioServices.incomingLocation,
      ServiceManager.ioServices.partMasterFilter )

  private File path
  private FilenameFilter filter
  private List<File> filelist
  private Long currentIx
  private Comparator<File> fileComparator

  private FileList( File pPath, FilenameFilter pFilter ) {
    this.path = pPath
    this.filter = pFilter
    this.filelist = new ArrayList<File>()
  }

  // Internal Methods
  protected File getPath( ) {
    File dir = this.path
    return ( dir != null ? ( dir.exists() ? dir : null ) : null )
  }

  protected Comparator<File> getComparator( ) {
    if ( this.fileComparator == null ) {
      this.fileComparator = new Comparator<File>() {
        int compare( File file_1, File file_2 ) {
          return file_1.name.compareToIgnoreCase( file_2.name )
        }
      }
    }
    return this.fileComparator
  }

  protected FilenameFilter getFilter( ) {
    if ( this.filter == null ) {
      this.filter = ServiceManager.ioServices.allFilesFilter
    }
    return this.filter
  }


  // Public methods
  File entry( Integer ix ) {
    return ( ix < this.filelist.size() ? this.filelist.get( ix ) : null )
  }

  Integer getSize( ) {
    return this.filelist.size()
  }

  Integer loadList( ) {
    this.filelist.clear()
    if ( this.getPath() != null ) {
      for ( File inFile : this.getPath().listFiles( this.getFilter() ) ) {
        this.filelist.add( inFile )
      }
    }
    Collections.sort( this.filelist, this.getComparator() )
    return filelist.size()
  }

  String remainingToString( Integer ix ) {
    StringBuffer sb = new StringBuffer()
    for ( int i = 0; i < this.getSize(); i++ ) {
      if ( i >= ix ) {
        sb.append( String.format( '%s\n', this.entry( i ).name ) )
      }
    }
    return sb.toString()
  }

  void startImport( ) {
    for ( this.currentIx = 0; this.currentIx < this.filelist.size(); this.currentIx++ ) {
      sleep 2000
    }
  }

  String toString( ) {
    StringBuffer sb = new StringBuffer()
    for ( File file : this.filelist ) {
      sb.append( String.format( '%s\n', file.name ) )
    }
    return sb.toString()
  }

}

package mx.lux.pos.ui.model.file

class SimpleFileFiltered implements FileFiltered {

  private File file

  SimpleFileFiltered( File pFile ) {
    this.file = pFile
  }

  File getFile() {
    return this.file
  }

  int compareTo( FileFiltered pFileFiltered ) {
    int result = this.getFile().getName().compareToIgnoreCase( pFileFiltered.getFile().getName() )
    return result
  }

}

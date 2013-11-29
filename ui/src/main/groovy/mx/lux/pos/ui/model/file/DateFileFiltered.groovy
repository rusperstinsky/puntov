package mx.lux.pos.ui.model.file

class DateFileFiltered implements FileFiltered {

  private File file
  private Date fileDate

  DateFileFiltered( File pFile, Date pFileDate ) {
    this.file = pFile
    this.fileDate = pFileDate
  }

  File getFile() {
    return this.file
  }

  int compareTo( FileFiltered pFileFiltered ) {
    int result = 0
    if ( pFileFiltered instanceof DateFileFiltered ) {
      result = this.fileDate.compareTo( pFileFiltered.fileDate )
      if (result == 0) {
        result = this.file.getName().compareToIgnoreCase(pFileFiltered.file.getName())
      }
    } else {
      result = this.file.getName().compareToIgnoreCase(pFileFiltered.file.getName())
    }
    return result
  }
}

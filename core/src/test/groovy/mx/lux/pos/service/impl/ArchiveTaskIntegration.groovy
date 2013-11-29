package mx.lux.pos.service.impl

import spock.lang.Specification
import mx.lux.pos.service.io.ArchiveTask
import org.springframework.test.context.ContextConfiguration
import mx.lux.pos.util.CustomDateUtils
import org.apache.commons.lang3.time.DateUtils
import mx.lux.pos.service.business.Registry

@ContextConfiguration( 'classpath:spring-config.xml' )
class ArchiveTaskIntegration extends Specification {

  private static final String DATE_FORMAT = 'dd-MM-yyyy'
  private static final String FMT_ARCHIVE_FILENAME = 'Z.%d.%s'
  private static final String FMT_FILE_PATTERN = '*%s*'

  // Internal methods
  private void archiveDailyCloseFiles( Date pForDate ) {
    String strDate = CustomDateUtils.format( DateUtils.truncate( pForDate, Calendar.DATE), DATE_FORMAT)
    println (String.format( 'CierreDiarioService.archivarCierre( %s )', strDate) )
    ArchiveTask task = new ArchiveTask(  )
    task.baseDir = Registry.dailyClosePath
    task.archiveFile = String.format( FMT_ARCHIVE_FILENAME, Registry.currentSite, strDate )
    task.filePattern = String.format( FMT_FILE_PATTERN, strDate )
    task.run()
  }

  def "test Archive Task"() {
    when:
    ArchiveTask task = new ArchiveTask( )
    task.archiveFile = 'soi.test'
    task.filePattern = '*11-12-2012*'
    task.baseDir = '/home/paso/cierre'
    task.run()

    then:
    true
  }

  def "test DailyClose Archive"() {
    when:
    archiveDailyCloseFiles( CustomDateUtils.parseDate( '2012-12-10', 'yyyy-MM-dd' ) )

    then:
    true
  }

}

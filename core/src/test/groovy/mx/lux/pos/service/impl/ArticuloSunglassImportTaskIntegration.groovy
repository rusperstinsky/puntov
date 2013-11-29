package mx.lux.pos.service.impl

import mx.lux.pos.service.business.ArticuloSunglassImportTask
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration( 'classpath:spring-config.xml' )
class ArticuloSunglassImportTaskIntegration extends Specification {

  def "Test Articulo Sunglass Import successful"( ) {
    setup:
    ArticuloSunglassImportTask task = new ArticuloSunglassImportTask()
    task.filename = "/home/paso/por_recibir/Producto.txt"

    when:
    task.run()

    then:
    task.recordCount > 0
    task.errorCount == 0
    task.recordCount == task.updatedCount

  }

  def "Test Articulo Sunglass Import failed"( ) {
    setup:
    ArticuloSunglassImportTask task = new ArticuloSunglassImportTask()
    task.filename = "/home/rlago/lux/runtime/paso/ArticuloSunglassFailTest.txt"

    when:
    task.run()

    then:
    task.recordCount > 0
    task.errorCount > 0
    task.recordCount > task.updatedCount
    task.recordCount == ( task.updatedCount + task.errorCount )

  }

  def "Test Articulo Sunglass Import"( ) {
      when:
      ServiceFactory.ioServices.loadPartFile()

      then:
      true
  }

}

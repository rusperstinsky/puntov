package mx.lux.pos.service.impl

import spock.lang.Specification
import mx.lux.pos.service.io.InventoryAdjustFile
import mx.lux.pos.model.InvAdjustSheet

class InventoryAdjustFileIntegration extends Specification {

  def "test Inventory Adjust File"() {
    when:
    InventoryAdjustFile file = new InventoryAdjustFile()
    InvAdjustSheet document = file.read( '/home/paso/por_recibir/Ajuste.04-04-2013.inv' )
    println document.toString()

    then:
    document.lines.size() > 0

  }

}

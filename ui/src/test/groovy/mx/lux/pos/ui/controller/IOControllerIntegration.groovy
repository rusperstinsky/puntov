package mx.lux.pos.ui.controller

import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

class IOControllerIntegration {

  static void main(args) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext( "classpath:spring-config.xml" )
    ctx.registerShutdownHook()
    IOController.getInstance().autoUpdateFxRates()
    PriceListController.loadExpiredPriceList()
    DailyCloseController.openDay()
    DailyCloseController.RegistrarPromociones()
    IOController.getInstance().autoUpdateEmployeeFile()
  }
}

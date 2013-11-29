package mx.lux.pos.ui.view

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.OpenSalesController
import mx.lux.pos.ui.resources.UI_Standards
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import mx.lux.pos.ui.view.dialog.ManualPriceDialog
import mx.lux.pos.ui.model.Item

class ManualPriceDialogSample extends JFrame {

  private SwingBuilder sb = new SwingBuilder()
  private JPanel mainPanel

  ManualPriceDialogSample( ) {
    buildUI()
  }

  // Internal Methods
  private void buildUI( ) {
    sb.build() {
      lookAndFeel( 'system' )
      frame( this,
          title: 'Sample Frame to trigger a dialog',
          show: true,
          pack: true,
          resizable: true,
          location: [ 100, 0 ],
          preferredSize: [ 800, 600 ],
          defaultCloseOperation: EXIT_ON_CLOSE
      ) {
        menuBar {
          menu( text: "Archivo", mnemonic: "A" ) {
            menuItem( text: "Salir", visible: true, actionPerformed: { println "Salir" } )
          }
          menu( text: "Herramientas", mnemonic: "H" ) {
            menuItem( text: "Precios Manuales", visible: true,
                actionPerformed: { requestManualPrice() }
            )
          }
        }
        panel() {
          borderLayout()
          panel( constraints: BorderLayout.PAGE_END ) {
            borderLayout()
            panel( constraints: BorderLayout.LINE_END ) {
              button( text: "Launch",
                  preferredSize: UI_Standards.BUTTON_SIZE,
                  actionPerformed: { requestManualPrice() }
              )
            }
          }
        }
      }
    }
  }

  void requestManualPrice( ) {
    ManualPriceDialog dlg = ManualPriceDialog.instance
    Item item = new Item()
    item.name = "SERV.AJUSTE"
    item.price = BigDecimal.ZERO
    dlg.item = item
    dlg.remarks = "Algunas observaciones capturadas antes / Y otras referidas a este Item"
    dlg.activate()
  }

  // UI Response
  static void main( String[] args ) {
    SwingUtilities.invokeLater(
        new Runnable() {
          void run( ) {
            //                        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring-config.xml")
            //                        ctx.registerShutdownHook()
            ManualPriceDialogSample sample = new ManualPriceDialogSample()
          }
        }
    )
  }


}

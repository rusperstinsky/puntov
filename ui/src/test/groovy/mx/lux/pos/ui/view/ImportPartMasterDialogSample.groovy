package mx.lux.pos.ui.view

import javax.swing.JFrame
import groovy.swing.SwingBuilder
import javax.swing.JPanel
import mx.lux.pos.ui.controller.OpenSalesController
import java.awt.BorderLayout
import mx.lux.pos.ui.resources.UI_Standards
import javax.swing.SwingUtilities
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import mx.lux.pos.ui.controller.ItemController
import mx.lux.pos.ui.controller.IOController

class ImportPartMasterDialogSample extends JFrame {

  private SwingBuilder sb = new SwingBuilder()

  ImportPartMasterDialogSample( ) {
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
            menuItem( text: "Apertura de Caja", visible: true,
                actionPerformed: { OpenSalesController.instance.requestNewDay() }
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
                  actionPerformed: { IOController.getInstance().requestImportPartMaster() }
              )
            }
          }
        }
      }
    }
  }

  // UI Response
  static void main( String[] args ) {
    SwingUtilities.invokeLater(
        new Runnable() {
          void run( ) {
            ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring-config.xml")
            ctx.registerShutdownHook()
            ImportPartMasterDialogSample sample = new ImportPartMasterDialogSample()
          }
        }
    )
  }


}

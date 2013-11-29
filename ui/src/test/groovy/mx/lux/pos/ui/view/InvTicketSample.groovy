package mx.lux.pos.ui.view

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.InvQryController
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.view.panel.InvTrView
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

class InvTicketSample extends JFrame {

  private SwingBuilder sb = new SwingBuilder( )
  private JPanel mainPanel
  private InvTrView invTrView
  
  InvTicketSample( ) {
    buildUI( )
  }
  
  // Internal Methods
  private void buildUI() {
    sb.build( ) {
      lookAndFeel( 'system' )
      frame( this,
          title: 'Sample Frame to trigger a dialog',
          show: true,
          pack: true,
          resizable: true,
          preferredSize: [800, 600],
          defaultCloseOperation: EXIT_ON_CLOSE
      ) {
        menuBar {
          menu( text: "Archivo", mnemonic: "A" ) {
            menuItem( text: "Salir", visible: true, actionPerformed: { println "Salir" } )
          }
          menu( text: "Inventario", mnemonic: "I" ) {
            menuItem( text: "Ticket Existencias", visible: true, 
              actionPerformed: { InvQryController.instance.requestInvOhTicket() } )
          }
        }
        panel( ) {
          borderLayout()
          panel ( constraints: BorderLayout.PAGE_END ) {
            borderLayout()
            panel ( constraints: BorderLayout.LINE_END ) {
              button( text: "Launch", 
                  preferredSize: UI_Standards.BUTTON_SIZE,
                  actionPerformed: { InvQryController.instance.requestInvOhTicket() }  
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
      new Runnable( ) {
        void run( ) {
          ApplicationContext ctx = new ClassPathXmlApplicationContext( "classpath:spring-config.xml" )
          ctx.registerShutdownHook()
          InvTicketSample sample = new InvTicketSample( )
        }
      }
    )
  }


}

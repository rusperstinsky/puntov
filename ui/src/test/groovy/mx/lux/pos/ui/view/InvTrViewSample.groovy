package mx.lux.pos.ui.view

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.view.panel.InvTrView
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

import java.awt.CardLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

class InvTrViewSample extends JFrame {

  private SwingBuilder sb = new SwingBuilder( )
  private JPanel mainPanel
  private InvTrView invTrView
  
  InvTrViewSample( ) {
    buildUI( )
  }
  
  // Internal Methods
  private void buildUI() {
    sb.build( ) {
      lookAndFeel( 'system' )
      frame( this,
          title: 'Sample Frame to host panel',
          show: true,
          pack: true,
          resizable: true,
          preferredSize: [800, 600],
          defaultCloseOperation: EXIT_ON_CLOSE
      ) {
        mainPanel = panel( layout: new CardLayout() )
        if (invTrView == null) {
          invTrView = new InvTrView()
        }
        mainPanel.add( 'invTrPanel', invTrView.panel )
        invTrView.activate()
        mainPanel.layout.show( mainPanel, 'invTrPanel' )
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
          InvTrViewSample sample = new InvTrViewSample( )
        }
      }
    )
  }

}

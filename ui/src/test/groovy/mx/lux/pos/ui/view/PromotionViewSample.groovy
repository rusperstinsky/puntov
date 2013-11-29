package mx.lux.pos.ui.view

import groovy.swing.SwingBuilder

import java.awt.CardLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

class PromotionViewSample extends JFrame {

  private SwingBuilder sb = new SwingBuilder( )
  
  private JPanel viewSample
  private JPanel mainPanel
    
  PromotionViewSample() {
    buildUI( )
  }
  
  // Internal methods
  private void buildUI( ) {
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
        if (viewSample == null) {
          viewSample = new OrderViewSample()
        }
        mainPanel.add( 'orderPanel', viewSample )
        mainPanel.layout.show( mainPanel, 'orderPanel' )
      }
    }
  }
  
  // Test Method
  static void main( String[] args ) {
    SwingUtilities.invokeLater(
      new Runnable( ) {
        void run( ) {
//          ApplicationContext ctx = new ClassPathXmlApplicationContext( "classpath:spring-config.xml" )
//          ctx.registerShutdownHook()
          PromotionViewSample sample = new PromotionViewSample( )
        }
      }
    )
  }


}

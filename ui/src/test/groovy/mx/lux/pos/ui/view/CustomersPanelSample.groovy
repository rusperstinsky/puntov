package mx.lux.pos.ui.view

import groovy.swing.SwingBuilder

import javax.swing.JFrame
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.SwingUtilities
import mx.lux.pos.ui.view.panel.RxPanel

class CustomersPanelSample extends JFrame {

  private SwingBuilder sb = new SwingBuilder()
  //private JMenuItem initialGlassesMenuItem
  //private JPanel initialGlassesPanel

  //private JPanel mainPanel

  CustomersPanelSample( ) {
    buildUI()
  }

  private void buildUI( ) {
    sb.build() {
      lookAndFeel( 'system' )
      frame( this,
          title: 'Sample Frame to call customer panels',
          show: true,
          pack: true,
          resizable: true,
          preferredSize: [ 950, 650 ],
          defaultCloseOperation: EXIT_ON_CLOSE
      ) {

        //add(new InitialGlassesPanel() )
        //add(new Background() )
        //add(new ExamPanel())
        //add( new BudgetPanel() )
        add( new RxPanel() )
        //add( new RecordsPanel() )
      }
    }
  }

  // Test Method
  static void main( String[] args ) {
    SwingUtilities.invokeLater(
        new Runnable() {
          void run( ) {
            //ApplicationContext ctx = new ClassPathXmlApplicationContext( "classpath:spring-config.xml" )
            //ctx.registerShutdownHook()
            CustomersPanelSample sample = new CustomersPanelSample()
          }
        }
    )
  }
}

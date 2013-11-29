package mx.lux.pos.ui.view.action

import groovy.swing.SwingBuilder

import javax.swing.Action

class ExitAction {

  Action action

  ExitAction( ) {
    action = new SwingBuilder().action( name: 'Salir', shortDescription: 'Salir del sistema' ) {
      System.exit( 0 )
    }
  }
}

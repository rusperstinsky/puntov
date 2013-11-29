package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.DailyCloseController
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.view.verifier.DateVerifier
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang3.time.DateUtils

import javax.swing.*
import java.awt.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class WaitDialog extends JDialog {

  private DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )
  private DateVerifier dv = DateVerifier.instance
  private def sb = new SwingBuilder()

  private JTextField txtDateStart
  private JTextField txtDateEnd
  private Date selectedDateStart
  private Date selectedDateEnd
  private String titulo
  private String contenido

  public boolean button = false

    WaitDialog( String titulo, String contenido ) {
    this.titulo = titulo
    this.contenido = contenido
    buildUI()
  }

  // UI Layout Definition
  void buildUI( ) {
    sb.dialog( this,
        title: titulo,
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [ 280, 100 ],
        location: [ 200, 250 ],
        undecorated: true,
    ) {
      panel() {
        borderLayout()
        panel( constraints: BorderLayout.CENTER, layout: new MigLayout( "wrap 2", "20[][grow,fill]40", "20[]10[]" ) ) {
          label( text: contenido )
        }
      }

    }
  }

  // UI Response
  protected void close( ) {
    this.visible = false
  }
}

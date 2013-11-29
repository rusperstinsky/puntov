package mx.lux.pos.ui.view.panel

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.AccessController
import mx.lux.pos.ui.model.UpperCaseDocument
import mx.lux.pos.ui.model.User
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang3.StringUtils

import java.awt.Color
import java.awt.Font
import javax.swing.border.TitledBorder
import javax.swing.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class LogInPanel extends JPanel {

  private SwingBuilder sb
  private JTextField username
  private JTextField date
  private JPasswordField password
  private JLabel messages
  private JButton logInButton
  private String priceListPending
  private Closure doAction
  private String version

  private DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )

  LogInPanel( Closure doAction, String version ) {
    this.version = version
    this.priceListPending = AccessController.listaPreciosPendientes()
    this.doAction = doAction ?: {}
    sb = new SwingBuilder()
    buildUI()
    if(!AccessController.iniciaSesionPrimeraVez()){
        date.text = AccessController.lastDate()
    }
  }

  private void buildUI( ) {
    sb.panel( this, layout: new MigLayout( 'wrap,center', '[fill]', '[top]' ) ) {
      panel( border: new TitledBorder( 'Ingresa tus datos:' ),
          layout: new MigLayout( 'wrap 2', '[fill,100!][fill,130!]', '[fill,30!][fill,30!][fill,30!][]' ),
      ) {
        label( 'Empleado' )
        username = textField( font: new Font( '', Font.BOLD, 14 ),
            document: new UpperCaseDocument(),
            horizontalAlignment: JTextField.CENTER,
            actionPerformed: {logInButton.doClick()}
        )

        label( 'Contrase\u00f1a' )
        password = passwordField( font: new Font( '', Font.BOLD, 14 ),
            horizontalAlignment: JTextField.CENTER,
            actionPerformed: {logInButton.doClick()}
        )

        label( 'Fecha Actual' )
        date = textField( font: new Font( '', Font.BOLD, 14 ),
                horizontalAlignment: JTextField.CENTER,
                actionPerformed: {logInButton.doClick()}
        )

        messages = label( foreground: Color.RED, constraints: "span 2" )
        label()
        label( text: version, horizontalAlignment: JLabel.RIGHT )
      }

      logInButton = button( 'Acceder', actionPerformed: doLogIn, constraints: 'right,span,w 125!,h 40!' )

      label( text: "Listas de Precios pendientes por cargar: ${this.priceListPending}" )
    }
  }

  private def doLogIn = {
    logInButton.enabled = false
    User user = AccessController.logIn( username.text, password.text )
    Boolean validDate = AccessController.validDate(date.text)
    if ( StringUtils.isNotBlank( user?.username ) && validDate ) {
      messages.text = null
      messages.visible = false
      doAction()
    } else {
        if(!validDate){
            messages.text = 'La fecha es incorrecta'
            date.text = null
        } else {
            messages.text = 'Empleado/Contrase\u00f1a incorrectos'
        }
      messages.visible = true
    }
    password.text = null
    logInButton.enabled = true
  }
}

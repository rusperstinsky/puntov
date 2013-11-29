package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.AccessController
import mx.lux.pos.ui.model.UpperCaseDocument
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.view.verifier.DateVerifier
import net.miginfocom.swing.MigLayout

import javax.swing.*
import java.awt.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class ChangePasswordDialog extends JDialog {

  private DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )
  private DateVerifier dv = DateVerifier.instance
  private def sb = new SwingBuilder()

  private JTextField txtUsuario
  private JPasswordField txtPassword
  private JPasswordField txtNuevoPassword
  private JPasswordField txtConfirmaPass
  private JLabel lblWarning
  private String usuario
  private String password
  private String nuevoPassword
  private String confirmaPassword

  public boolean button = false

  ChangePasswordDialog( ) {
    buildUI()
  }

  // UI Layout Definition
  void buildUI( ) {
    sb.dialog( this,
        title: "Cambio de Password",
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [ 400, 350 ],
        location: [ 200, 250 ],
    ) {
      panel() {
        borderLayout()
        panel( constraints: BorderLayout.CENTER, layout: new MigLayout( "wrap 2", "20[][grow,fill]60", "20[]10[]" ) ) {
          label( text: "Inserte el Usuario y Password", constraints: "span 2" )
          label( text: " ", constraints: "span 2" )
          label( text: "Usuario:" )
          txtUsuario = textField( document: new UpperCaseDocument() )
          label( text: "Password:" )
          txtPassword = passwordField(  )
          label( text: "Nuevo Password:" )
          txtNuevoPassword = passwordField(  )
          label( text: "Confirmar:" )
          txtConfirmaPass = passwordField(  )
          lblWarning = label( visible: false, constraints: 'span 2', foreground: UI_Standards.WARNING_FOREGROUND )
        }

        panel( constraints: BorderLayout.PAGE_END ) {
          borderLayout()
          panel( constraints: BorderLayout.LINE_END ) {
            button( text: "Aplicar", preferredSize: UI_Standards.BUTTON_SIZE,
                actionPerformed: { onButtonOk() }
            )
            button( text: "Cancelar", preferredSize: UI_Standards.BUTTON_SIZE,
                actionPerformed: { onButtonCancel() }
            )
          }
        }
      }

    }
  }

  // Public Methods
  void activate( ) {
    setVisible( true )
  }

  // UI Response
  protected void onButtonCancel( ) {
    button = false
    dispose()
  }

  protected void onButtonOk( ) {
    usuario = txtUsuario.getText().trim()
    password = txtPassword.getText().trim()
    nuevoPassword = txtNuevoPassword.getText().trim()
    confirmaPassword = txtConfirmaPass.getText().trim()
    if( usuario.length() > 0 && password.length() > 0 && nuevoPassword.length() > 0 && confirmaPassword.length() > 0 ){
        button = true
        Boolean existEmpleado = AccessController.validaDatos( usuario, password, nuevoPassword, confirmaPassword )
        if( existEmpleado ){
            Boolean actualizo = AccessController.cambiaPassword( usuario, nuevoPassword )
            if( !actualizo ){
                println 'error al actualizar'
            } else {
                dispose()
            }
        } else {
            lblWarning.visible = true
            lblWarning.text = 'Usuario o Password invalidos'
        }
    } else {
        lblWarning.visible = true
        lblWarning.text = 'Verifique los datos'
    }
  }
}

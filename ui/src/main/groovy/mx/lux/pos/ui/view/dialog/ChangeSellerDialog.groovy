package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.AccessController
import mx.lux.pos.ui.controller.OrderController
import mx.lux.pos.ui.model.UpperCaseDocument
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.view.verifier.DateVerifier
import net.miginfocom.swing.MigLayout

import javax.swing.*
import java.awt.*
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.text.DateFormat
import java.text.SimpleDateFormat

class ChangeSellerDialog extends JDialog implements FocusListener {

  private DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" )
  private DateVerifier dv = DateVerifier.instance
  private def sb = new SwingBuilder()

  private JTextField txtFactura
  private JTextField txtVendedor
  private JTextField txtObservaciones
  private JTextField txtEmpleadoOrig
  private JLabel lblWarning
  private String factura
  private String vendedor
  private String observaciones
  private String ticket = ''

  public boolean button = false

    ChangeSellerDialog( ) {
    buildUI()
  }

  // UI Layout Definition
  void buildUI( ) {
    sb.dialog( this,
        title: "Cambio de Vendedor",
        resizable: true,
        pack: true,
        modal: true,
        preferredSize: [ 450, 330 ],
        location: [ 200, 250 ],
    ) {
      panel() {
        borderLayout()
        panel( constraints: BorderLayout.CENTER, layout: new MigLayout( "wrap 2", "20[][grow,fill]60", "20[]10[]" ) ) {
          label( text: "Cambio de vendedor", constraints: "span 2" )
          label( text: " ", constraints: "span 2" )
          label( text: "Factura:" )
          txtFactura = textField( document: new UpperCaseDocument() )
          txtFactura.addFocusListener( this )
          label( text: "Vendedor Original:" )
          txtEmpleadoOrig = textField( document: new UpperCaseDocument(), editable: false )
          label( text: "Vendedor Actual:" )
          txtVendedor = textField( document: new UpperCaseDocument() )
          label( text: "Observaciones:" )
          txtObservaciones = textField( )
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
    factura = txtFactura.getText().trim()
    vendedor = txtVendedor.getText().trim()
    observaciones = txtObservaciones.getText().trim()
    if( factura.length() > 0 && vendedor.length() > 0 ){
        button = true
        Boolean existFactura = OrderController.validaDatos( factura, vendedor )
        Boolean diaAbierto = OrderController.validaDiaAbierto( factura, vendedor )
        if( existFactura ){
            if( diaAbierto ){
                Boolean actualizo = OrderController.cambiaVendedor( factura, vendedor, observaciones )
                if( !actualizo ){
                    println 'error al actualizar'
                } else {
                    dispose()
                }
            } else {
                lblWarning.visible = true
                lblWarning.text = 'Día cerrado, no procede cambio'
            }
        } else {
            lblWarning.visible = true
            lblWarning.text = 'Vendedor o factura invalidos'
        }
    } else {
        lblWarning.visible = true
        lblWarning.text = 'Verifique los datos'
    }
  }



  @Override
  void focusGained(FocusEvent e) {
      //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  void focusLost(FocusEvent e) {
      txtEmpleadoOrig.text = OrderController.obtenerEmpleadoPorNotaVenta( txtFactura.text.trim() )
  }
}

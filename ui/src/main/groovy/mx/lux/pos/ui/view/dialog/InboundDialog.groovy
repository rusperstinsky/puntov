package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.view.verifier.DateVerifier
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang3.time.DateUtils

import javax.swing.InputVerifier
import javax.swing.JComponent
import javax.swing.JOptionPane
import java.awt.BorderLayout
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.text.DateFormat
import java.text.SimpleDateFormat
import javax.swing.JDialog
import javax.swing.JTextField

class InboundDialog extends JDialog {

    private def sb = new SwingBuilder()

    private JTextField txtClave
    
    public boolean button = false


    InboundDialog( ) {
        buildUI()
    }

    public String getTxtClave() {
       return txtClave.getText()
    }
    // UI Layout Definition
    void buildUI() {
        sb.dialog( this,
                title: "Capture Clave",
                resizable: true,
                pack: true,
                modal: true,
                preferredSize: [360, 180],
                location: [200, 250],
        ) {
            panel() {
                borderLayout()
                panel( constraints: BorderLayout.CENTER, layout: new MigLayout( "wrap 2", "20[][grow,fill]40", "20[]10[]" ) ) {
                    label( text: "                  Capture la Clave de Entrada", constraints: "span 2" )
                    label( text: " ", constraints: "span 2" )
                    label( text: "Clave:" )
                    txtClave = textField()
                    /*
                    txtClave.addFocusListener (new FocusListener()
                    {
                        public void focusLost (FocusEvent e) {
                            String cadena = textClave.getText()
                            if (cadena.trim().length() <= 0) {

                            }
                        }
                    }
                    )
                    */
                }
                panel( constraints: BorderLayout.PAGE_END ) {
                    borderLayout()
                    panel( constraints: BorderLayout.LINE_END ) {
                        button( text: "Aplicar", preferredSize: UI_Standards.BUTTON_SIZE,
                                actionPerformed: { onButtonOk( ) }
                        )
                        button( text: "Cerrar", preferredSize: UI_Standards.BUTTON_SIZE,
                                actionPerformed: { onButtonCancel( ) }
                        )
                    }
                }

            }

        }
    }

    // UI Management
    protected void refreshUI() {
        txtClave.setText("")
    }

    // Public Methods
    void activate() {
        refreshUI()
        setVisible(true)
    }

    
    // UI Response
    protected void onButtonCancel() {
        button = false
        setVisible(false)
    }

    protected void onButtonOk() {
        if (txtClave.getText().trim().length() > 0) {
            button = true
            setVisible(false)
        }
        else {
            JOptionPane.showMessageDialog this,
                    "Debe de capturar una clave",
                    "Intente de Nuevo",
                    JOptionPane.ERROR_MESSAGE;
        }
    }



}

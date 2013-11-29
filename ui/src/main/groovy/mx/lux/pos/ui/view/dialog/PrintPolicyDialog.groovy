package mx.lux.pos.ui.view.dialog

import javax.swing.JDialog
import groovy.swing.SwingBuilder
import net.miginfocom.swing.MigLayout
import javax.swing.JTextField

/**
 * Created with IntelliJ IDEA.
 * User: opticalux
 * Date: 5/11/12
 * Time: 03:50 PM
 * To change this template use File | Settings | File Templates.
 */
class PrintPolicyDialog extends JDialog {

    SwingBuilder sb = new SwingBuilder()

    private JTextField txtNoPoliza

    PrintPolicyDialog(){
        buildUI()
    }

    void buildUI(){
        sb.dialog(this,
                title:"Imprimir Poliza",
                resizable: true,
                pack: true,
                modal: true,
                preferredSize: [ 300,200 ]
        ){
            panel( layout: new MigLayout("center,fill,wrap","[center,grow,fill]") ){

                panel( layout: new MigLayout("fill,wrap","[center,fill]") ){
                    label( text:"Ingrese numero de factura asegurada" )
                    label( text:"o de factura de poliza" )
                    txtNoPoliza = textField( )
                }

                panel( layout: new MigLayout("center","fill,110!") ){
                    button( text:"Aceptar" )
                    button( text:"Cerrar", actionPerformed:{dispose()} )
                }
            }
        }

    }
}

package mx.lux.pos.ui.view.dialog

import javax.swing.JDialog
import groovy.swing.SwingBuilder
import net.miginfocom.swing.MigLayout
import javax.swing.JTextField
import javax.swing.border.TitledBorder
import javax.swing.JCheckBox
import javax.swing.ButtonGroup
import javax.swing.JRadioButton
import java.awt.event.ActionEvent

/**
 * Created with IntelliJ IDEA.
 * User: opticalux
 * Date: 5/11/12
 * Time: 01:55 PM
 * To change this template use File | Settings | File Templates.
 */
class PolicyDialog extends JDialog {

    SwingBuilder sb = new SwingBuilder()

    private JTextField txtPoliza
    private JCheckBox cbLente
    private JCheckBox cbArmazon
    private JCheckBox cbOD
    private JCheckBox cbOI
    private ButtonGroup motivo
    private JRadioButton rbDanio
    private JRadioButton rbPerdida

    PolicyDialog(){
        buildUI()
    }

    void buildUI(){
        sb.dialog(this,
                title:"Poliza",
                resizable: true,
                pack: true,
                modal: true,
                preferredSize: [ 450,300 ]
        ){
            panel( layout: new MigLayout("center,fill,wrap","[center,grow,fill]") ){

                panel( layout: new MigLayout("fill,wrap 2","150[fill]10[grow,fill]150") ){
                    label( text:"Poliza:" )
                    txtPoliza = textField( )
                }

                panel( border: new TitledBorder(""), layout: new MigLayout("fill,wrap 4") ){
                    label( text:"Tipo artículo:" )
                    cbLente = checkBox( text:"Lentes", constraints:"span 2" )
                    cbArmazon = checkBox( text:"Armazon" )
                    label( text: "" )
                    cbOD = checkBox( text:"OD" )
                    cbOI = checkBox( text:"OI" )
                    label( text:"" )
                    label( text:"Motivo:" )
                    motivo = buttonGroup()
                    rbDanio = radioButton( text:"Daño", constraints:"span 2", buttonGroup:motivo )
                    rbPerdida = radioButton( text:"Perdida", buttonGroup: motivo )
                }

                panel( layout: new MigLayout("center, wrap 5","fill,80!" ) ){
                    button( text:"Aceptar" )
                    button( text:"Solicitar" )
                    button( text:"Imprimir", actionPerformed:onPrint )
                    button( text:"Limpiar" )
                    button( text:"Cerrar", actionPerformed:{dispose()} )
                }
            }
        }
    }

    private def onPrint = { ActionEvent ev ->

        new PrintPolicyDialog().show()

    }
}

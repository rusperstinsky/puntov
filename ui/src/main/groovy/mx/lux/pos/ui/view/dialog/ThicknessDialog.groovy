package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import javax.swing.JComboBox
import javax.swing.JDialog
import javax.swing.JTextField
import javax.swing.border.TitledBorder
import net.miginfocom.swing.MigLayout
import java.awt.BorderLayout
import java.awt.Font

class ThicknessDialog extends JDialog {

	private def sb = new SwingBuilder()
	
	private JTextField txtODEsfera
	private JTextField txtODCilindro
	private JTextField txtODEje
	private JTextField txtODAdicion
	private JTextField txtOIEsfera
	private JTextField txtOICilindro
	private JTextField txtOIEje
	private JTextField txtOIAdicion
	
	private JTextField txtDI
	private JTextField txtAltSeg
	
	private JComboBox cbMaterial
	private JTextField txtIr
	private JTextField txtEMC
	
	private JTextField txtDV
	private JTextField txtDH
	private JTextField txtPTE
	private JTextField txtDCO
	private JTextField txtBASE
	
	ThicknessDialog(){
		buildUI()
	}
	
	void buildUI(){
		
		sb.dialog( this,
			title: "Seleccionar fechas",
			resizable: true,
			pack: true,
			modal: true,
			preferredSize: [ 900, 550 ],
			//location: [ 200, 250 ],
		) {
		panel( layout: new MigLayout("fill,wrap 3","[center,fill]") ) {
			
			panel( constraints:"span 3", layout: new MigLayout("center,fill,wrap","[center]") ){
				label( text:"CALCULO DE ESPESOR" )
				
			}
			
			panel( constraints: BorderLayout.CENTER, border: new TitledBorder("Rx"), layout: new MigLayout("fill,wrap 5","[fill]") ){
				label( text:"" )
				label( text:"Esfera", border:new TitledBorder("") )
				label( text:"Cilindro", border: new TitledBorder("") )
				label( text:"Eje", border: new TitledBorder("") )
				label( text:"Adicion", border: new TitledBorder("") )
				label( text:"O.D." )
				txtODEsfera = textField( )
				txtODCilindro = textField( )
				txtODEje = textField( )
				txtODAdicion = textField( )
				label( text:"O.I." )
				txtOIEsfera = textField( )
				txtOICilindro = textField( )
				txtOIEje = textField( )
				txtOIAdicion = textField( )
			}
			
			panel( border: new TitledBorder("Cliente con dist/alt."), layout: new MigLayout("fill,wrap 2","[fill]") ){
				label( text:"DI" )
				label( text:"ALT SEG" )
				txtDI = textField( )
				txtAltSeg = textField( )
			}
			
			panel( border: new TitledBorder("Especificaciones del lente"), layout: new MigLayout("fill,wrap 3","[fill]") ){
				label( text:"Material")
				label( text:"Ir" )
				label( text:"EMC" )
				cbMaterial = comboBox( )
				txtIr = textField( )
				txtEMC = textField( )
			}
		
			panel( layout: new MigLayout("fill,wrap") ){
				label( text:"" )
			}
			
			panel( border: new TitledBorder("Especificaciones del armazón"), layout: new MigLayout("fill,wrap 5","[fill]") ){
				label( text:"DV" )
				label( text:"DH" )
				label( text:"PTE" )
				label( text:"DCO" )
				label( text:"BASE" )
				txtDV = textField( )
				txtDH = textField( )
				txtPTE = textField( )
				txtDCO = textField( )
				txtBASE = textField( )
			}
			
			panel( layout: new MigLayout("fill,wrap") ){
				label( text:"" )
			}
			
			panel( layout: new MigLayout("fill,wrap") ){
				label( text:"" )
			}
			
			panel( border: new TitledBorder(""), layout: new MigLayout("fill,wrap","[center]") ){
				Font fonts = new Font("",Font.BOLD,20)
				label( text:"ESPESOR TEMPORAL", font:fonts )
				
			}
			
			panel( layout: new MigLayout("fill,wrap") ){
				label( text:"" )
			}
			
			panel( layout: new MigLayout("fill,wrap") ){
				label( text:"" )
			}
			
			panel( border: new TitledBorder(""), layout: new MigLayout("fill,wrap 4",
				"[][fill][fill][fill]") ){
				Font fonts = new Font("",Font.BOLD,19)
				
				label( text:"O.D.", border: new TitledBorder(""), font:fonts )
				label( text:"     ", border: new TitledBorder(""), font:fonts )
				label( text:"     ", border: new TitledBorder(""), font:fonts )
				label( text:"BLANK", border: new TitledBorder(""), font:fonts )
				label( text:"O.I.", border: new TitledBorder(""), font:fonts )
				label( text:"     ", border: new TitledBorder(""), font:fonts )
				label( text:"     ", border: new TitledBorder(""), font:fonts )
				label( text:"     ", border: new TitledBorder(""), font:fonts )
			}
			
			panel( layout: new MigLayout("fill,wrap") ){
				label( text:"" )
			}
			
			panel( constraints: "span 3", layout: new MigLayout("right","[fill,110!]") ){
				button( text:"Calcular" )
				button( text:"Limpiar" )
				button( text:"Cerrar")
			}
			
			
		}
	}
		
		
		
	}
}

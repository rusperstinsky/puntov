package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import javax.swing.JCheckBox
import javax.swing.JDialog
import javax.swing.JTextField
import javax.swing.border.TitledBorder
import net.miginfocom.swing.MigLayout

class NewReviewDialog extends JDialog {

	private def sb = new SwingBuilder()
	
	private JTextField txtIdEmpleado
	private JTextField txtNomEmpleado
	private JTextField txtCliente
	private JTextField txtDia
	private JTextField txtMes
	private JTextField txtAnio
	private JTextField txtFactura
	private JCheckBox cbxExtranjero
	
	private JTextField txtProblema
	
	private JTextField txtExOriODEsfera
	private JTextField txtExOriODCil
	private JTextField txtExOriODEje
	private JTextField txtExOriODAdC
	private JTextField txtExOriODAdInt
	private JTextField txtExOriODAV
	private JTextField txtExOriOIEsfera
	private JTextField txtExOriOICil
	private JTextField txtExOriOIEje
	private JTextField txtExOriOIAdC
	private JTextField txtExOriOIAdInt
	private JTextField txtExOriOIAV
	
	private JTextField txtExNuEsfera1
	private JTextField txtExNuCil1
	private JTextField txtExNuEje1
	private JTextField txtExNuAdC1
	private JTextField txtExNuAdInt1
	private JTextField txtExNuAV1
	private JTextField txtExNuEsfera2
	private JTextField txtExNuCil2
	private JTextField txtExNuEje2
	private JTextField txtExNuAdC2
	private JTextField txtExNuAdInt2
	private JTextField txtExNuAV2
	
	private JTextField txtDILejos
	private JTextField txtRxODEsfera
	private JTextField txtRxODCil
	private JTextField txtRxODEje
	private JTextField txtRxODAdC
	private JTextField txtRxODAdInt
	private JTextField txtRxODAV
	private JTextField txtRxODDM
	private JTextField txtRxODPrisma
	private JTextField txtRxODUbic
	private JTextField txtDICerca
	private JTextField txtRxOIEsfera
	private JTextField txtRxOICil
	private JTextField txtRxOIEje
	private JTextField txtRxOIAdC
	private JTextField txtRxOIAdInt
	private JTextField txtRxOIAV
	private JTextField txtRxOIDM
	private JTextField txtRxOIPrisma
	private JTextField txtRxOIUbic
	private JTextField txtAltOblea
	
	NewReviewDialog(){
		buildUI()
	}
	
	void buildUI(){
		sb.dialog( this,
			title: "REVISION",
			resizable: true,
			pack: true,
			modal: true,
			preferredSize: [ 1000, 600 ],
			//location: [ 200, 250 ],
		){
			panel( layout: new MigLayout("fill, wrap 2","[center,fill]") ){
				
				panel( layout: new MigLayout("fill,wrap 3","[grow,fill,right][grow,fill][grow,fill]") ){
					label( text:"Emp:" )
					txtIdEmpleado = textField( )
					txtNomEmpleado = textField( )
					label( text:"Cliente:" )
					txtCliente = textField( constraints:"span 2" )
				}
				
				panel( layout: new MigLayout("fill,wrap 9",
					"[][grow,fill][][grow,fill][][grow,fill][fill][grow,fill][]") ){
					label( text:"Fecha:" )
					txtDia = textField( )
					label( text:"/" )
					txtMes = textField( )
					label( text:"/" )
					txtAnio = textField( )
					label( text:"Fact:" )
					txtFactura = textField( )
					cbxExtranjero = checkBox( text:"Ext." )
					label( text:"Edad:" )
					label( text:"     ", border: new TitledBorder("") )
					label( text:"", constraints:"span 7" )
				}
				
				panel( constraints:"span 2", layout: new MigLayout("fill,wrap 2","[][grow,fill]") ){
					label( text:"Problema:" )
					txtProblema = textField( editable:false, minimumSize: [330,110], enabled:false )
				}
				
				panel( border: new TitledBorder("Examen Original"), layout: new MigLayout("fill,wrap 8",
					"[][grow,fill][grow,fill][grow,fill][grow,fill][grow,fill][center][grow,fill]") ){
					label( text:"" )
					label( text:"Esfera", border: new TitledBorder("") )
					label( text:"Cil.", border: new TitledBorder("") )
					label( text:"Eje" , border: new TitledBorder(""))
					label( text:"Ad.C.", border: new TitledBorder("") )
					label( text:"Ad.Int.", border: new TitledBorder("") )
					label( text:"A.V.", constraints:"span 2", border: new TitledBorder("") )
					label( text:"O.D.", border: new TitledBorder("") )
					txtExOriODEsfera = textField( )
					txtExOriODCil = textField( )
					txtExOriODEje = textField( )
					txtExOriODAdC = textField( )
					txtExOriODAdInt = textField( )
					label( text:"20/")
					txtExOriODAV = textField( )
					label( text:"O.I", border: new TitledBorder("") )
					txtExOriOIEsfera = textField( )
					txtExOriOICil = textField( )
					txtExOriOIEje = textField( )
					txtExOriOIAdC = textField( )
					txtExOriOIAdInt = textField( )
					label( text:"20/" )
					txtExOriOIAV = textField( )
				}
					
				panel( border: new TitledBorder("Examen Nuevo"), layout: new MigLayout("fill,wrap 7",
					"[grow,fill][grow,fill][grow,fill][grow,fill][grow,fill][center][grow,fill]") ){
					label( text:"Esfera", border: new TitledBorder("") )
					label( text:"Cil.", border: new TitledBorder("") )
					label( text:"Eje", border: new TitledBorder("") )
					label( text:"Ad.C.", border: new TitledBorder("") )
					label( text:"Ad.Int.", border: new TitledBorder("") )
					label( text:"A.V.", constraints:"span 2", border: new TitledBorder("") )
					txtExNuEsfera1 = textField( )
					txtExNuCil1 = textField( )
					txtExNuEje1 = textField( )
					txtExNuAdC1 = textField( )
					txtExNuAdInt1 = textField( )
					label( text:"20/" )
					txtExNuAV1 = textField( )
					txtExNuEsfera2 = textField( )
					txtExNuCil2 = textField( )
					txtExNuEje2 = textField( )
					txtExNuAdC2 = textField( )
					txtExNuAdInt2 = textField( )
					label( text:"20/" )
					txtExNuAV2 = textField( )
				}
					
				panel( border: new TitledBorder("Rx"), layout: new MigLayout("fill,wrap 13") ){
					label( text:"" )
					label( text:"Esfera", border: new TitledBorder("") )
					label( text:"Cil.", border: new TitledBorder("") )
					label( text:"Eje", border: new TitledBorder("") )
					label( text:"Ad.C.", border: new TitledBorder("") )
					label( text:"Ad.Int.", border: new TitledBorder("") )
					label( text:"A.V.", constraints:"span 2", border: new TitledBorder("") )
					label( text:"D.M.", border: new TitledBorder("") )
					label( text:"Prisma", border: new TitledBorder("") )
					label( text:"Ubic.", border: new TitledBorder("") )
					label( text:"D.I. Lejos", border: new TitledBorder("") )
					txtDILejos = textField( )
					label( text:"O.D.", border: new TitledBorder("") )
					txtRxODEsfera = textField( )
					txtRxODCil = textField( )
					txtRxODEje = textField( )
					txtRxODAdC = textField( )
					txtRxODAdInt = textField( )
					label( text:"20/" )
					txtRxODAV = textField( )
					txtRxODDM = textField( )
					txtRxODPrisma = textField( )
					txtRxODUbic = textField( )
					label( text:"D.I. Cerca", border: new TitledBorder("") )
					txtDICerca = textField( )
					label( text:"O.I.", border: new TitledBorder("") )
					txtRxOIEsfera = textField( )
					txtRxOICil = textField( )
					txtRxOIEje = textField( )
					txtRxOIAdC = textField( )
					txtRxOIAdInt = textField( )
					label( text:"20/" )
					txtRxOIAV = textField( )
					txtRxOIDm = textField( )
					txtRxOIPrisma = textField( )
					txtRxOIUbic = textField( )
					label( text:"Alt. Oblea", border: new TitledBorder("") )
					txtAltOblea = textField( )
				}
				
				
			}
		}
	}
}

package mx.lux.pos.ui.view.panel

import groovy.swing.SwingBuilder

import java.awt.event.ActionEvent
import javax.swing.ButtonGroup;
import javax.swing.JComboBox
import javax.swing.JPanel
import javax.swing.JRadioButton;
import javax.swing.JTextField
import javax.swing.border.TitledBorder

import mx.lux.pos.ui.view.dialog.ThicknessDialog
import net.miginfocom.swing.MigLayout
import mx.lux.pos.ui.resources.ImageLibrary

class RxPanel extends JPanel {
	
	private SwingBuilder sb
	
	private JPanel thicknessPanel
	private JPanel rxPanel
	
	private JRadioButton rbReferido
	private JRadioButton rbRx
	private ButtonGroup tipoCliente
	private ButtonGroup uso
	private JRadioButton rbLejos
	private JRadioButton rbCerca
	private JRadioButton rbBifocal
	private JRadioButton rbProgr
	private JRadioButton rbInter
	private JRadioButton rbBifInter
	private JComboBox cbxOftalmologo
	private JTextField txtDia
	private JTextField txtMes
	private JTextField txtAnio
	private JTextField txtDiaNac
	private JTextField txtMesNac
	private JTextField txtAnioNac
	private JTextField txtObservaciones
	private JTextField txtDILejos
	private JTextField txtDICerca
	private JTextField txtAltOblea
	private JTextField txtODEsfera
	private JTextField txtODCil
	private JTextField txtODEje
	private JTextField txtODAdC
	private JTextField txtODAdInt
	private JTextField txtODAV
	private JTextField txtODDM
	private JTextField txtODPrisma
	private JComboBox cbODUbic
	private JTextField txtOIEsfera
	private JTextField txtOICil
	private JTextField txtOIEje
	private JTextField txtOIAdC
	private JTextField txtOIAdInt
	private JTextField txtOIAV
	private JTextField txtOIDM
	private JTextField txtOIPrisma
	private JComboBox cbOIUbic
	
	private JTextField txtObjODEsfera
	private JTextField txtObjODCilindro
	private JTextField txtObjODEje
	private JTextField txtSubODEsfera
	private JTextField txtSubODCilindro
	private JTextField txtSubODEje
	private JTextField txtSubODAdC
	private JTextField txtSubODAdInt
	private JTextField txtSubODAV
	
	private JTextField txtObjOIEsfera
	private JTextField txtObjOICilindro
	private JTextField txtObjOIEje
	private JTextField txtSubOIEsfera
	private JTextField txtSubOICilindro
	private JTextField txtSubOIEje
	private JTextField txtSubOIAdC
	private JTextField txtSubOIAdInt
	private JTextField txtSubOIAV
	
	
	RxPanel(){
		sb = new SwingBuilder()
		buildUI()
	}
	
	private void buildUI(){
		
		sb.panel(this, layout: new MigLayout( 'fill, wrap 4', '5[grow,fill][fill][fill][fill]5')){
			
			panel( constraints:"span 4", border:new TitledBorder("Datos del examen"), layout: new MigLayout("center,fill,wrap 13",
				"5[fill]1[fill]1[fill]1[fill]30[fill]1[fill]1[fill]1[fill]1[fill]1[fill]1[fill]1[fill]40[fill]5") ){
				label( text:"" )
				label( text:"Esfera", border: new TitledBorder("") )
				label( text:"Cilindro", border: new TitledBorder("") )
				label( text:"Eje", border: new TitledBorder("") )
				label( text:"" )
				label( text:"Esfera", border: new TitledBorder("") )
				label( text:"Cilindro", border: new TitledBorder("") )
				label( text:"Eje", border: new TitledBorder("") )
				label( text:"Ad.C.", border: new TitledBorder("") )
				label( text:"Ad.Int.", border: new TitledBorder("") )
				label( text:"      	A.V.", border: new TitledBorder(""), constraints:"span 2" )
				label( text:"" )
				label( text:"O.D.", border: new TitledBorder("") )
				txtObjODEsfera = textField( )
				txtObjODCilindro = textField( )
				txtObjODEje = textField( )
				label( text:"O.D.", border: new TitledBorder("") )
				txtSubODEsfera = textField( )
				txtSubODCilindro = textField( )
				txtSubODEje = textField( )
				txtSubODAdC = textField( )
				txtSubODAdInt = textField( )
				label( text:"20/" )
				txtSubODAV = textField( )
				label( text:"" )
				
				label( text:"O.I.", border: new TitledBorder("") )
				txtObjOIEsfera = textField( )
				txtObjOICilindro = textField( )
				txtObjOIEje = textField( )
				label( text:"O.I.", border: new TitledBorder("") )
				txtSubOIEsfera = textField( )
				txtSubOICilindro = textField( )
				txtSubOIEje = textField( )
				txtSubOIAdC = textField( )
				txtSubOIAdInt = textField( )
				label( text:"20/" )
				txtSubOIAV = textField( )
				label( text:"1 de 1" )
				label( text:"Refracción Objetiva", constraints:"span 4" )
				label( text:"Refracción Subjetiva", constraints:"span 8" )
				button( text:"Copia" )
				
			}
			
			panel( layout: new MigLayout() ){
				label( text:"" )
			}
			
			panel( layout: new MigLayout("fill,wrap 2","[fill][grow,fill]") ){
				label( text:"Factura:" )
				label( text:"Rx Importada" )
			}
			
			panel( border: new TitledBorder("Tipo Cliente"), layout: new MigLayout("fill,wrap 2") ){
				tipoCliente = buttonGroup()
				rbReferido = radioButton( text:"Referido", buttonGroup:tipoCliente )
				rbRx = radioButton( text:"Rx", buttonGroup:tipoCliente )
			}
			
			panel( layout: new MigLayout("center,fill,wrap","[fill]") ){
				label( text:"Oftalmologo" )
				cbxOftalmologo = comboBox()
			}
			
			panel( layout: new MigLayout("fill,wrap 6","[right][fill]1[center]1[fill]1[center]1[fill]") ){
				label( text:"Fecha:" )
				txtDia = textField( )
				label( text:"/" )
				txtMes = textField( )
				label( text:"/" )
				txtAnio = textField( )
				label( text:"Fecha Nac.:" )
				txtDiaNac = textField( )
				label( text:"/" )
				txtMesNac = textField()
				label( text:"/" )
				txtAnioNac = textField()
			}
			
			panel( constraints:"span 3", border:new TitledBorder("Uso:"), layout: new MigLayout( "fill,wrap 6","[fill]","1") ){
				
				uso = buttonGroup()
				rbLejos = radioButton( text:"Lejos", buttonGroup:uso )
				rbCerca = radioButton( text:"Cerca", buttonGroup:uso )
				rbBifocal = radioButton( text:"Bifocal", buttonGroup:uso )
				rbProgr = radioButton( text:"Progr.", buttonGroup:uso )
				rbInter = radioButton( text:"Inter.", buttonGroup:uso )
				rbBifInter = radioButton( text:"Bifocal Interm.", buttonGroup:uso ) 
			}
			
			panel( constraints:"span 3", border: new TitledBorder("Rx"), layout: new MigLayout("fill,wrap 13",
				"1[fill]1[fill]1[fill]1[fill]1[fill]1[fill]1[center]1[fill]5[fill]5[fill]1[fill]1[fill]1[fill]1" ) ){
				label( text:"" )
				label( text:"Esfera", border: new TitledBorder("") )
				label( text:"Cil.", border: new TitledBorder("") )
				label( text:"Eje", border: new TitledBorder("") )
				label( text:"Ad.C.", border: new TitledBorder("") )
				label( text:"Ad.Int.", border: new TitledBorder("") )
				label( text:"A.V.", border: new TitledBorder(""), constraints:"span 2" )
				label( text:"D.M.", border: new TitledBorder("") )
				label( text:"Prisma", border:new TitledBorder("") )
				label( text:"Ubic.", border: new TitledBorder("") )
				label( text:"D.I. Lejos", border: new TitledBorder("") )
				txtDILejos = textField( )
				label( text:"O.D.", border: new TitledBorder("") )
				txtODEsfera = textField( )
				txtODCil = textField( )
				txtODEje = textField( )
				txtODAdC = textField( )
				txtODAdInt = textField( )
				label( text:"20/" )
				txtODAV = textField( )
				txtODDM = textField( )
				txtODPrisma = textField( )
				cbODUbic = comboBox( )
				label( text:"D.I. Cerca" )
				txtDICerca = textField( )
				label( text:"O.I.", border: new TitledBorder("") )
				txtOIEsfera = textField( )
				txtOICil = textField( )
				txtOIEje = textField( )
				txtOIAdC = textField( )
				txtOIAdInt = textField( )
				label( text:"20/" )
				txtOIAV = textField( )
				txtOIDM = textField( )
				txtOIPrisma = textField( )
				cbOIUbic = comboBox( )
				label( text:"Alt. Oblea" )
				txtAltOblea = textField( )
				label( text:"", constraints:"span 13" )
				label( text:"Observaciones", border: new TitledBorder(""), constraints:"span 3" )
				txtObservaciones = textField( constraints:"span 10" )
			}
			
			panel(layout: new MigLayout("center,fill,wrap","[center]") ){
				
				label( icon: imageIcon( url: ImageLibrary.getURL( ImageLibrary.IMG_UP_BIG ) ) )
				label( text:"1/1" )
				label( icon: imageIcon( url: ImageLibrary.getURL( ImageLibrary.IMG_DOWN_BIG ) ) )
				button( text:"Espesor", maximumSize:[130, 30], actionPerformed: onClick )
			}
			
			panel( constraints:"span 13", layout: new MigLayout("right","[fill,110!]") ){
				button( text:"Guardar" )
				button( text:"Limpiar" )
				button( text:"Modificar" )
				button( text:"Nueva" )
				button( text:"Cancelar" )
				button( text:"Cerrar" )
			}
		}
		
	}

	
	private def onClick = { ActionEvent ev ->
		
			   new ThicknessDialog( ).show()
		   }
}
	

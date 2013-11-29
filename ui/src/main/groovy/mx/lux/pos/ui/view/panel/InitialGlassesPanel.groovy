package mx.lux.pos.ui.view.panel

import groovy.swing.SwingBuilder

import javax.swing.ButtonGroup
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.JTextField
import javax.swing.border.TitledBorder

import net.miginfocom.swing.MigLayout

import org.apache.poi.hssf.record.formula.functions.T

class InitialGlassesPanel extends JPanel {

	private SwingBuilder sb
	
	private JCheckBox cbLejos
	private JCheckBox cbBifocal
	private JCheckBox cbCerca
	private JCheckBox cbMultifocal
	private JCheckBox cbInterm
	private JRadioButton rbLux
	private JRadioButton rbOtra
	private ButtonGroup lugar
	private JTextField txtAnios
	private JTextField txtMeses
	
	private JTextField txtEsferaOD
	private JTextField txtCilindroOD
	private JTextField txtEjeOD
	private JTextField txtAdicionOD
	private JTextField txtPrismaOD
	private JTextField txtObservaciones
	private JTextField txtEsferaOI
	private JTextField txtCilindroOI
	private JTextField txtEjeOI
	private JTextField txtAdicionOI
	private JTextField txtPrismaOI
	
	InitialGlassesPanel(){
		sb = new SwingBuilder()
		buildUI()
	}
	
	private void buildUI(){

		sb.panel(this, layout: new MigLayout( 'fill, wrap 3', '80[grow,fill]2[grow,fill]2[grow,fill]80')){
			
			panel( border: new TitledBorder( "Uso" ), layout: new MigLayout("fill,wrap 2","[fill]","2") ){
				cbLejos = checkBox( text:"Lejos" )
				cbBifocal = checkBox( text:"Bifocal" )
				cbCerca = checkBox( text:"Cerca" )
				cbMultifocal = checkBox( text:"Multifocal" )
				cbInterm = checkBox( text:"Interm." )
				label( text:" " )
			}
			
			panel( border: new TitledBorder("Lugar de Adquisición"), layout: new MigLayout( "fill,wrap 2","[grow,fill]","2") ){
				lugar = buttonGroup()
				rbLux = radioButton( text:"Lux", buttonGroup:lugar, selected:true )
				label( text:"        ")
				rbOtra = radioButton( text:"Otra", buttonGroup:lugar )
				label( text:"        " )
			}
			
			panel( border: new TitledBorder("Tiempo de uso"), layout: new MigLayout("wrap 2","[grow,fill]") ){
				txtAnios = textField( )
				label( text:"años" )
				txtMeses = textField( )
				label( text:"meses" )
			}
			
			panel( border: new TitledBorder( "A.V. de lejos con anteojos" ), layout: new MigLayout("fill,left,wrap 3","[][][grow,fill]","2") ){
				label( text:"O.D.", border: new TitledBorder("") )
				label( text:"20/")
				txtAnios = textField( maximumSize: [30, 30] )
				label( text:"O.I. ", border: new TitledBorder("") )
				label( text:"20/")
				txtMeses = textField( maximumSize: [30, 30] )
			}
			
			panel( constraints:"span 2", border: new TitledBorder( "Graduación" ), layout: new MigLayout("fill,center,wrap 6",
				"[][grow,fill][grow,fill][grow,fill][grow,fill][grow,fill]" ) ){
			
				label( text:"" )
				label( text:"Esfera", border: new TitledBorder("") )
				label( text:"Cilindro", border: new TitledBorder("") )
				label( text:"Eje    ", border: new TitledBorder("") )
				label( text:"Adición", border: new TitledBorder("") )
				label( text:"Prisma", border: new TitledBorder("") )
				label( text:"OD", border: new TitledBorder("") )
				txtEsferaOD = textField( )
				txtCilindroOD = textField( )
				txtEjeOD = textField( )
				txtAdicionOD = textField( )
				txtPrismaOD = textField( )
				label( text:"OI", border: new TitledBorder("") )
				txtEsferaOI = textField( )
				txtCilindroOI = textField( )
				txtEjeOI = textField( )
				txtAdicionOI = textField( )
				txtPrismaOI = textField( )
			}
			
			panel( constraints:"span 3", border: new TitledBorder( "Observaciones" ), layout: new MigLayout("fill,wrap","[grow,fill]")){
				txtObservaciones = textField( minimumSize: [500,100] )
			}
			
			panel( constraints:"span 3", layout: new MigLayout("right","[fill,150!]") ){
				button( text:"Guardar" )
				button( text:"Modificar" )
				button( text:"Cancelar" )
				button( text:"Cerrar" )
			}
		}
	}

}

package mx.lux.pos.ui.view.panel

import java.awt.CardLayout
import java.awt.Image;
import java.awt.event.ActionEvent

import groovy.swing.SwingBuilder
import javax.swing.JPanel
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder
import javax.swing.border.TitledBorder
import mx.lux.pos.ui.resources.UI_Standards
import mx.lux.pos.ui.resources.ImageLibrary
import mx.lux.pos.ui.view.dialog.ContactLensesDialog
import mx.lux.pos.ui.view.dialog.QuoteDialog
import mx.lux.pos.ui.view.dialog.ReceptionDialog
import mx.lux.pos.ui.view.dialog.ReviewDialog

import net.miginfocom.swing.MigLayout
import mx.lux.pos.ui.view.dialog.PolicyDialog

class ExamPanel extends JPanel {

	private SwingBuilder sb
	
	private JPanel contactLensesPanel
	private JPanel examPanel
	
	private JTextField txtTranscribio
	private JTextField txtDia
	private JTextField txtMes
	private JTextField txtAnio
	private JTextField txtod1
	private JTextField txtod2
	private JTextField txtoi1
	private JTextField txtoi2
	
	private JTextField txtObjEsfOd
	private JTextField txtObjCilOd
	private JTextField txtObjEjeOd
	private JTextField txtObjEsfOi
	private JTextField txtObjCilOi
	private JTextField txtObjEjeOi
	private JTextField txtDi
	
	private JTextField txtSubEsfOd
	private JTextField txtSubCilOd
	private JTextField txtSubEjeOd
	private JTextField txtSubAdcOd
	private JTextField txtSubAdIntOd
	private JTextField txtSubAvOd
	private JTextField txtSubEsfOi
	private JTextField txtSubCilOi
	private JTextField txtSubEjeOi
	private JTextField txtSubAdcOi
	private JTextField txtSubAdIntOi
	private JTextField txtSubAvOi
	
	private JTextField txtObserv
	
	ExamPanel(){
		sb = new SwingBuilder()
		buildUI()
	}
	
	private void buildUI(){
		
		sb.panel(this, layout: new MigLayout( 'fill, wrap 2', '[grow,fill]20[center]5')){
			
			panel( layout: new MigLayout("fill,wrap 6","[][fill][fill][fill][fill][fill]280", "2" ) ){
				label( text:"Transcribio:" )
				txtTranscribio = textField( constraints:"span 5")
				label( text:"Fecha:" )
				txtDia = textField( )
				label( text:"/" )
				txtMes = textField( )
				label( text:"/" )
				txtAnio = textField( )
			}
			
			panel( border: new TitledBorder("Agudeza visual de lejos"), layout: new MigLayout("fill,wrap 6","[][][grow,fill][][][grow,fill]","1" ) ){
				label( text:"O.D.", border: new TitledBorder("") )
				label( text:"20/" )
				txtod1 = textField( )
				label( text:"O.D.", border : new TitledBorder("") )
				label( text:"20/" )
				txtod2 = textField( )
				label( text:"O.I.", border: new TitledBorder("") )
				label(text:"20/" )
				txtoi1 = textField( )
				label( text:"O.I.", border: new TitledBorder("") )
				label( text:"20/" )
				txtoi2 = textField("")
				label( text:"Sin Anteojos   ", constraints: "span 3", border: new TitledBorder("") )
				label( text:"Con Anteojos", constraints:"span 3", border: new TitledBorder("") )
			}
			
			panel( border:new TitledBorder("Refracción objetiva"), layout: new MigLayout("center,fill,wrap 8",
				"[][center,grow,fill][grow,fill][grow,fill][grow,fill][grow,fill][][]","2") ){
				label( text:" " )
				label( text:"Esfera", border: new TitledBorder("") )
				label( text:"Cilindro", border: new TitledBorder("") )
				label( text:"Eje   ", border: new TitledBorder("") )
				label( text:" " )
				label( text: "D.I", border: new TitledBorder(""))
				label( text:" " )
				label( text:" " )
				label( text: "O.D.", border: new TitledBorder("") )
				txtObjEsfOd = textField( )
				txtObjCilOd = textField( )
				txtObjEjeOd = textField( )
				label( text:" " )
				txtDi = textField( )
				label( text:" " )
				button( text:"Copia", preferredSize: UI_Standards.BUTTON_SIZE )
				label( text:"O.I.", border: new TitledBorder("") )
				txtObjEsfOi = textField( )
				txtObjCilOi = textField( )
				txtObjEjeOi = textField( )
				label( text:" ", constraints:" span 3" )
			}
				
			panel( layout: new MigLayout( "center,fill,wrap","[center]","2") ){
				button( text:"Lentes de Contacto", preferredSize: UI_Standards.BUTTON_SIZE, actionPerformed: onContactLenses )
				button( text:"              Revisión              ", preferredSize: UI_Standards.BUTTON_SIZE, actionPerformed: onReception )
			}	
			
			panel( border:new TitledBorder("Refracción subjetiva"), layout: new MigLayout( "center,fill,wrap 8",
				"[center][center,fill,grow][center,fill,grow][center,fill,grow][center,fill,grow][center,fill,grow][center][center,fill,grow]","2" ) ){
				label( text:" " )
				label( text:"Esfera", border: new TitledBorder("") )
				label( text:"Cilindro", border: new TitledBorder("") )
				label( text:"Eje     ", border: new TitledBorder("") )
				label( text:"Ad.C.", border: new TitledBorder("") )
				label( text:"Ad.Int.", border: new TitledBorder("") )
				label( text:"A.V.", constraints: "span 2", border: new TitledBorder("") )
				label( text:"O.D.", border: new TitledBorder("") )
				txtSubEsfOd = textField( )
				txtSubCilOd = textField( )
				txtSubEjeOd = textField( )
				txtSubAdcOd = textField( )
				txtSubAdIntOd = textField( )
				label( text:"20/" )
				txtSubAvOd = textField( )
				label( text:"O.I.", border: new TitledBorder("") )
				txtSubEsfOi = textField( )
				txtSubCilOi = textField( )
				txtSubEjeOi = textField( )
				txtSubAdcOi = textField( )
				txtSubAdIntOi = textField( )
				label( text:"20/" )
				txtSubAvOi = textField( )
			}
			
				panel( layout: new MigLayout("center,fill,wrap", "[center]" ) ){
					label( icon: imageIcon( url: ImageLibrary.getURL( ImageLibrary.IMG_UP_BIG ) ) )
					label( text:"1/1" )
					label( icon: imageIcon( url: ImageLibrary.getURL( ImageLibrary.IMG_DOWN_BIG ) ) )
				}
			
			panel( layout: new MigLayout("center,fill,wrap","[center,fill,grow]" ) ){
				label( text:"Observaciones" )
				txtObserv = textField( minimumSize: [400,80])
			}
			
			panel( layout: new MigLayout("center,fill,wrap","[center,fill,grow]") ){
				label( text:"" )
			}
			
			panel( constraints:"span 2", layout: new MigLayout( "right","[fill,110!]") ){
				button( text:"Guardar")
				button( text:"Modificar" )
				button( text:"Nuevo" )
				button( text:"Cancelar")
				button( text:"Cerrar", actionPerformed: {dispose()} )
			}
		}
	}
	
	
	private def onContactLenses = { ActionEvent ev ->
		
		new ContactLensesDialog().show()
		
	}
	
	
	private def onReception = { ActionEvent ev ->

		new QuoteDialog().show()
		
	}
	
	/*private def onReview = { ActionEvent ev ->
		
		new ReviewDialog().show()
		
	}*/
	
}

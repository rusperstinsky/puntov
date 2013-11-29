package mx.lux.pos.ui.view.panel

import groovy.swing.SwingBuilder
import java.awt.event.ActionEvent
import javax.swing.JCheckBox
import javax.swing.JList
import javax.swing.JPanel;
import javax.swing.JScrollPane
import javax.swing.JTextField;
import javax.swing.border.TitledBorder

import mx.lux.pos.ui.view.dialog.DoubleListsDialog;
import mx.lux.pos.ui.view.dialog.ObservationsDialog
import net.miginfocom.swing.MigLayout

class Background extends JPanel {

  private SwingBuilder sb
  private JCheckBox cbLejos
  private JCheckBox cbCerca
  private JCheckBox cbIntermedia
  private JTextField txtEdad
  private JTextField txtOcupacion
  private JList lstActividades
  private JList lstSaludPers
  private JList lstMolestias
  private JScrollPane lista

  private JList lstCornea
  private JList lstEsclera
  private JList lstFondo
  private JList lstParpados
  private JList lstConjBulbar
  private JList lstConjTarsal

  private JTextField txtObservaciones

  private DoubleListsDialog dialogo

  Background( ) {
    sb = new SwingBuilder()
    buildUI()
  }

  private void buildUI( ) {

    sb.panel( this, layout: new MigLayout( 'fill,wrap 4', '20[grow,fill][grow,fill][grow,fill][grow,fill]20' ) ) {

      panel( constraints: "span 4", layout: new MigLayout( "fill,left,wrap 7", "[][][]80[][grow,fill]80[][grow,fill]", "2" ) ) {
        label( text: "Problemas de Vision", constraints: "span 7" )
        cbLejos = checkBox( text: "Lejos" )
        cbCerca = checkBox( text: "Cerca" )
        cbIntermedia = checkBox( text: "Intermedia" )
        label( text: "Edad:" )
        txtEdad = textField( enabled: true, opaque: true )
        label( text: "Ocupacion:" )
        txtOcupacion = textField()
      }

      panel( border: new TitledBorder( "" ), layout: new MigLayout( "fill, wrap", "[]", "[grow,fill]" ) ) {
        button( text: "Actividades", constraints: "center", actionPerformed: onClick )
        lista = scrollPane( constraints: "center" ) {
          lstActividades = list( autoscrolls: true, border: new TitledBorder( "" ) )
        }
      }

      panel( border: new TitledBorder( "" ), layout: new MigLayout( "fill, wrap", "[]", "[grow,fill]" ) ) {
        button( text: "Salud Personal", constraints: "center", actionPerformed: onClick )
        lista = scrollPane( constraints: "center" ) {
          lstSaludPers = list( autoscrolls: true, border: new TitledBorder( "" ) )
        }
      }

      panel( border: new TitledBorder( "" ), layout: new MigLayout( "fill, wrap", "[]", "[grow,fill]" ) ) {
        button( text: "Molestias", constraints: "center", actionPerformed: onClick )
        lista = scrollPane( constraints: "center" ) {
          lstMolestias = list( autoscrolls: true, border: new TitledBorder( "" ) )
        }
      }

      panel( layout: new MigLayout( "center,fill,wrap", "30[grow,fill]30", "[grow,fill,push]" ) ) {
        label( text: "Observaciones" )
        button( text: "Poner Nota", actionPerformed: onClickObs )
        txtObservaciones = textField( editable: false, minimumSize: [ 160, 110 ] )
      }

      panel( constraints: "span 4", border: new TitledBorder( "Medios Externos" ), layout: new MigLayout( "center,fill,wrap 6", "[center,grow,fill]", "" ) ) {
        button( text: "Cornea", actionPerformed: onClick )
        button( text: "Escalera", actionPerformed: onClick )
        button( text: "Fondo", actionPerformed: onClick )
        button( text: "Parpados", actionPerformed: onClick )
        button( text: "Conj. Bulbar", actionPerformed: onClick )
        button( text: "Conj. Tarsal", actionPerformed: onClick )
        lista = scrollPane( constraints: "center" ) {
          lstCornea = list( autoscrolls: true, border: new TitledBorder( "" ) )
        }
        lista = scrollPane( constraints: "center" ) {
          lstEsclera = list( autoscrolls: true, border: new TitledBorder( "" ) )
        }
        lista = scrollPane( constraints: "center" ) {
          lstFondo = list( autoscrolls: true, border: new TitledBorder( "" ) )
        }
        lista = scrollPane( constraints: "center" ) {
          lstParpados = list( autoscrolls: true, border: new TitledBorder( "" ) )
        }
        lista = scrollPane( constraints: "center" ) {
          lstConjBulbar = list( autoscrolls: true, border: new TitledBorder( "" ) )
        }
        lista = scrollPane( constraints: "center" ) {
          lstConjTarsal = list( autoscrolls: true, border: new TitledBorder( "" ) )
        }
      }

      panel( constraints: "span 4", layout: new MigLayout( 'right', '[fill,150!]' ) ) {
        button( text: "Guardar" )
        button( text: "Cancelar" )
        button( text: "Cerrar" )
      }


    }
  }


  private def onClick = { ActionEvent ev ->

    new DoubleListsDialog().show()
  }


  private def onClickObs = { ActionEvent ev ->

    new ObservationsDialog().show()
  }

}

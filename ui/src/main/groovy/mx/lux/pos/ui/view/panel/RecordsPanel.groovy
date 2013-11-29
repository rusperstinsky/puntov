package mx.lux.pos.ui.view.panel

import groovy.swing.SwingBuilder
import net.miginfocom.swing.MigLayout

import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.border.TitledBorder

class RecordsPanel extends JPanel {

  private SwingBuilder sb
  private JTextField txtODEsf
  private JTextField txtODCil
  private JTextField txtODEje
  private JTextField txtODAdcC
  private JTextField txtODAdcI
  private JTextField txtObservaciones
  private JTextField txtOIEsf
  private JTextField txtOICil
  private JTextField txtOIEje
  private JTextField txtOIAdcC
  private JTextField txtOIAdcI
  private JTextField txtRecODEsf
  private JTextField txtRecODCil
  private JTextField txtRecODEje
  private JTextField txtRecODAdcC
  private JTextField txtRecODAdcI
  private JTextField txtRecODPrisma
  private JTextField txtRecOIEsf
  private JTextField txtRecOICil
  private JTextField txtRecOIEje
  private JTextField txtRecOIAdcC
  private JTextField txtRecOIAdcI
  private JTextField txtRecOIPrisma

  RecordsPanel( ) {
    sb = new SwingBuilder()
    buildUI()
  }

  private void buildUI( ) {

    sb.panel( this, layout: new MigLayout( 'fill, wrap 2', '[fill][fill]' ) ) {

      panel( border: new TitledBorder( "Examen" ), layout: new MigLayout( "fill, wrap 7",
          "[fill][fill][fill][fill][fill][fill][fill]" ) ) {
        label( text: "Fecha", constraints: "span 2" )
        label( text: "" )
        label( text: "Examinó", constraints: "span 4" )
        label( text: "          ", border: new TitledBorder( "" ), constraints: "span 3" )
        label( text: "          ", border: new TitledBorder( "" ), constraints: "span 4" )
        label( text: "" )
        label( text: "Esf.", border: new TitledBorder( "" ) )
        label( text: "Cil.", border: new TitledBorder( "" ) )
        label( text: "Eje", border: new TitledBorder( "" ) )
        label( text: "Adc C", border: new TitledBorder( "" ) )
        label( text: "Adc I", border: new TitledBorder( "" ) )
        label( text: "" )
        label( text: "OD" )
        txtODEsf = textField()
        txtODCil = textField()
        txtODEje = textField()
        txtODAdcC = textField()
        txtODAdcI = textField()
        label( text: "" )
        label( text: "OI" )
        txtOIEsf = textField()
        txtOICil = textField()
        txtOIEje = textField()
        txtOIAdcC = textField()
        txtOIAdcI = textField()
        label( text: "" )
        label( text: "Observaciones", constraints: "span 2" )
        txtObservaciones = textField( constraints: "span 5" )
      }

      panel( border: new TitledBorder( "Ventas" ), layout: new MigLayout( "fill,wrap 4", "[fill]" ) ) {
        label( text: "        ", border: new TitledBorder( "" ) )
        label( text: "" )
        label( text: "Factura:" )
        label( text: "        ", border: new TitledBorder( "" ) )
        label( text: "" )
        label( text: "" )
        label( text: "Folio:" )
        label( text: "        ", border: new TitledBorder( "" ) )
        label( text: "Fecha" )
        label( text: "" )
        label( text: "Vendedor", constraints: "span 2" )
        label( text: "        ", border: new TitledBorder( "" ) )
        label( text: "" )
        label( text: "        ", border: new TitledBorder( "" ), constraints: "span 2" )
        label( text: "Lente" )
        label( text: "        ", border: new TitledBorder( "" ), constraints: "span 2" )
        label( text: "        ", border: new TitledBorder( "" ) )
        label( text: "Armazón" )
        label( text: "        ", border: new TitledBorder( "" ) )
        label( text: "        ", border: new TitledBorder( "" ) )
        label( text: "        ", border: new TitledBorder( "" ) )
        label( text: "Acces." )
        label( text: "        ", border: new TitledBorder( "" ), constraints: "span 2" )
        label( text: "        ", border: new TitledBorder( "" ) )
        label( text: "", constraints: "span 2" )
        label( text: "Descuento" )
        label( text: "        ", border: new TitledBorder( "" ) )
        label( text: "", constraints: "span 2" )
        label( text: "Total" )
        label( text: "        ", border: new TitledBorder( "" ) )
        label( text: "Observaciones", constraints: "span 4" )
        label( text: "        ", border: new TitledBorder( "" ), constraints: "span 4" )
      }

      panel( border: new TitledBorder( "Receta" ), layout: new MigLayout( "fill, wrap 8",
          "[fill][fill][fill][fill][fill][fill][fill][fill]" ) ) {
        label( text: "Fecha", constraints: "span 2" )
        label( text: "" )
        label( text: "Examinó", constraints: "span 5" )
        label( text: "      ", border: new TitledBorder( "" ), constraints: "span 2" )
        label( text: "" )
        label( text: "      ", border: new TitledBorder( "" ), constraints: "span 5" )

        label( text: "" )
        label( text: "Esf.", border: new TitledBorder( "" ) )
        label( text: "Cil.", border: new TitledBorder( "" ) )
        label( text: "Eje", border: new TitledBorder( "" ) )
        label( text: "Adc C", border: new TitledBorder( "" ) )
        label( text: "Adc I", border: new TitledBorder( "" ) )
        label( text: "Prisma", border: new TitledBorder( "" ) )
        label( text: "" )
        label( text: "OD", border: new TitledBorder( "" ) )
        txtRecODEsf = textField()
        txtRecODCil = textField()
        txtRecODEje = textField()
        txtRecODAdcC = textField()
        txtRecODAdcI = textField()
        txtRecODPrisma = textField()
        label( text: "" )
        label( text: "OI", border: new TitledBorder( "" ) )
        txtRecOIEsf = textField()
        txtRecOICil = textField()
        txtRecOIEje = textField()
        txtRecOIAdcC = textField()
        txtRecOIAdcI = textField()
        txtRecOIPrisma = textField()
        label( text: "" )
        label( text: "D.I. Lejos", constraints: "span 2" )
        label( text: "" )
        label( text: "D.I. Cerca", constraints: "span 2" )
        label( text: "" )
        label( text: "Uso", constraints: "span 2" )
        label( text: " ", border: new TitledBorder( "" ), constraints: "span 2" )
        label( text: "" )
        label( text: " ", border: new TitledBorder( "" ), constraints: "span 2" )
        label( text: "" )
        label( text: " ", border: new TitledBorder( "" ), constraints: "span 2" )
        label( text: "Observaciones" )
        label( text: " ", border: new TitledBorder( "" ), constraints: "span 7" )

      }

    }
  }

}

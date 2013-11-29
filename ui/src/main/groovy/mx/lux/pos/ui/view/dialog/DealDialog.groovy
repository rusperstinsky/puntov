package mx.lux.pos.ui.view.dialog

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.model.Deal
import mx.lux.pos.ui.model.DealType
import mx.lux.pos.ui.model.UpperCaseDocument
import net.miginfocom.swing.MigLayout

import java.awt.Component
import javax.swing.JComboBox
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JTextField

class DealDialog extends JDialog {

  private SwingBuilder sb
  private Deal tmpDeal
  private JTextField dealName
  private JTextField dealValue
  private JComboBox dealType
  private JLabel description

  DealDialog( Component parent, Deal deal ) {
    sb = new SwingBuilder()
    tmpDeal = new Deal(
        id: deal?.id,
        name: deal?.name,
        reference: deal?.reference,
        type: deal?.type,
        value: deal?.value
    )
    buildUI( parent )
    doBindings()
  }

  private void buildUI( Component parent ) {
    sb.dialog( this,
        title: tmpDeal?.id ? "Editar Promoción" : 'Agregar Promoción',
        location: parent.locationOnScreen,
        resizable: false,
        modal: true,
        pack: true,
        layout: new MigLayout( 'fill,wrap 2', '[][fill]' )
    ) {
      label( 'Nombre' )
      dealName = textField( document: new UpperCaseDocument() )

      label( 'Tipo' )
      dealType = comboBox( items: DealType.values() )

      label( 'Descripción' )
      description = label()

      label( 'Valor' )
      dealValue = textField( document: new UpperCaseDocument() )

      panel( layout: new MigLayout( 'right', '[fill,100!]' ), constraints: 'span' ) {
        button( 'Borrar', actionPerformed: {dispose()} )
        button( 'Aplicar', actionPerformed: {dispose()} )
        button( 'Cancelar', defaultButton: true, actionPerformed: {dispose()} )
      }
    }
  }

  private void doBindings( ) {
    sb.build {
      bean( dealName, text: bind( source: tmpDeal, sourceProperty: 'name' ) )
      bean( dealType, selectedItem: bind( source: tmpDeal, sourceProperty: 'type' ) )
      bean( description, text: bind( source: tmpDeal, sourceProperty: 'description' ) )
      bean( dealValue, text: bind( source: tmpDeal, sourceProperty: 'value' ) )
    }
  }
}

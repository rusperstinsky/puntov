package mx.lux.pos.ui.view.dialog.dailyclose

import groovy.swing.SwingBuilder
import mx.lux.pos.ui.controller.DailyCloseController
import mx.lux.pos.ui.model.Bank
import mx.lux.pos.ui.model.Deposit
import mx.lux.pos.ui.model.DepositType
import mx.lux.pos.ui.view.verifier.NotEmptyVerifier
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang3.StringUtils

import java.awt.Component
import java.awt.Font
import java.awt.event.ItemEvent
import java.text.NumberFormat
import javax.swing.*

class EditDepositDialog extends JDialog {

  private static final String DATE_FORMAT = 'dd-MM-yyyy'

  private SwingBuilder sb
  private Date closeDate
  private Deposit deposit
  private JTextField depositNumber
  private JTextField reference
  private JFormattedTextField ammount
  private JSpinner enterDate
  private JSpinner depositDate
  private JComboBox depositType
  private JComboBox bank
  private List<Bank> depositBanks
  private NumberFormat formatter
  private boolean depositEdit = false

  EditDepositDialog( Component parent, Date closeDate, Integer depositId, boolean editar ) {
    this.closeDate = closeDate
    sb = new SwingBuilder()
      if( !editar ){
          deposit = new Deposit()
      }
    depositEdit = editar
    depositBanks = DailyCloseController.findDepositBanks()
    formatter = NumberFormat.getInstance( Locale.US )
    if ( depositId && editar ) {
      deposit = DailyCloseController.findDepositById( depositId )
    } else {
      deposit = new Deposit(
          closeDate: closeDate,
          enterDate: new Date(),
          depositDate: new Date()
      )
    }
    buildUI( parent )
    doBindings()
  }

  private void buildUI( Component parent ) {
    sb.dialog( this,
        title: "Dep\u00f3sito para el d\u00eda ${closeDate?.format( DATE_FORMAT )}",
        locationRelativeTo: parent,
        resizable: false,
        pack: true,
        modal: true,
        layout: new MigLayout( 'fill,wrap', '[fill]' )
    ) {
      panel( layout: new MigLayout( 'wrap 2', '[left][fill]' ) ) {
        label( 'No.' )
        depositNumber = textField( editable: false )

        label( 'Fecha ingreso' )
        enterDate = spinner( model: spinnerDateModel() )

        label( 'Fecha dep\u00f3sito' )
        depositDate = spinner( model: spinnerDateModel() )

        label( 'Tipo' )
        depositType = comboBox( visible: true, enabled: true, items: DepositType.values() )

        label( 'Banco' )
        bank = comboBox( visible: true, enabled: true, items: depositBanks*.name, itemStateChanged: bankChanged )

        label( 'Referencia' )
        reference = textField()

        label( 'Importe' )
        ammount = formattedTextField(
            font: new Font( '', Font.BOLD, 24 ),
            format: formatter,
            inputVerifier: new NotEmptyVerifier(),
            horizontalAlignment: JTextField.RIGHT
        )
      }

      panel( layout: new MigLayout( 'fill', '[right]' ) ) {
        button( 'Aplicar', defaultButton: true, actionPerformed: saveAction )
        button( 'Cancelar', actionPerformed: {dispose()} )
      }
    }

    enterDate.editor = new JSpinner.DateEditor( enterDate as JSpinner, DATE_FORMAT )
    depositDate.editor = new JSpinner.DateEditor( depositDate as JSpinner, DATE_FORMAT )
  }

  private void doBindings( ) {
    sb.build {
      bean( depositNumber, text: bind( source: deposit, sourceProperty: 'number', mutual: true ) )
      bean( enterDate, value: bind( source: deposit, sourceProperty: 'enterDate', mutual: true ) )
      bean( depositDate, value: bind( source: deposit, sourceProperty: 'depositDate', mutual: true ) )
      bean( depositType, selectedItem: bind( source: deposit, sourceProperty: 'depositType', mutual: true ) )
      bean( bank, selectedItem: bind( source: deposit, sourceProperty: 'bank', mutual: true ) )
      bean( reference, text: bind( source: deposit, sourceProperty: 'reference', mutual: true ) )
      bean( ammount, value: bind( source: deposit, sourceProperty: 'ammount', mutual: true ) )
    }
  }

  private def bankChanged = { ItemEvent ev ->
    if ( ev.stateChange == ItemEvent.SELECTED ) {
      Bank bank = depositBanks.find { Bank tmp ->
        tmp?.name?.equalsIgnoreCase( ev.item as String )
      }
      deposit.bankId = bank?.id
    } else {
      deposit.bankId = null
    }
  }

  private def saveAction = {
    if ( isValid( deposit ) ) {
      if ( DailyCloseController.saveDeposit( deposit, depositEdit ) ) {
        dispose()
        //sb.optionPane().showMessageDialog( null, 'Se ha actualizado el Deposito correctamente', 'Deposito', JOptionPane.INFORMATION_MESSAGE )
      } else {
        sb.optionPane().showMessageDialog( null, 'Se ha producido un error al actualizar el Deposito', 'Error', JOptionPane.ERROR_MESSAGE )
      }
    }
  }

  private boolean isValid( Deposit deposit ) {
    boolean result = true
    if ( !deposit.enterDate ) {
      sb.optionPane().showMessageDialog( null, 'El campo fecha de ingreso no tiene el formato correcto dd/mm/yyyy', 'Error', JOptionPane.ERROR_MESSAGE )
      result = false
    } else {
      Date dateFrom = closeDate.minus( 5 )
      Date dateTo = closeDate.plus( 5 )
      if ( deposit.enterDate.before( dateFrom ) ) {
        sb.optionPane().showMessageDialog( null, "La fecha de Ingreso debe ser mayor que ${dateFrom.format( DATE_FORMAT )}", 'Error', JOptionPane.ERROR_MESSAGE )
        result = false
      } else if ( deposit.enterDate.after( dateTo ) ) {
        sb.optionPane().showMessageDialog( null, "La fecha de Ingreso debe ser menor que ${dateTo.format( DATE_FORMAT )}", 'Error', JOptionPane.ERROR_MESSAGE )
        result = false
      }
    }
    if ( !deposit.depositDate ) {
      sb.optionPane().showMessageDialog( null, 'El campo fecha del deposito no tiene el formato correcto dd/mm/yyyy', 'Error', JOptionPane.ERROR_MESSAGE )
      result = false
    } else {
      Date dateFrom = deposit.enterDate?.minus( 1 )
      Date dateTo = deposit.enterDate.plus( 5 )
      if ( deposit.depositDate.before( dateFrom ) ) {
        sb.optionPane().showMessageDialog( null, "La fecha de Deposito debe ser mayor a ${dateFrom.format( DATE_FORMAT )}", 'Error', JOptionPane.ERROR_MESSAGE )
        result = false
      } else if ( deposit.depositDate.after( dateTo ) ) {
        sb.optionPane().showMessageDialog( null, "La fecha de Deposito debe ser menor a ${dateTo.format( DATE_FORMAT )}", 'Error', JOptionPane.ERROR_MESSAGE )
        result = false
      }
    }
    if ( depositType.selectedIndex < 0 ) {
      sb.optionPane().showMessageDialog( null, 'Debe seleccionar un tipo de deposito', 'Error', JOptionPane.ERROR_MESSAGE )
      result = false
    }
    if ( bank.selectedIndex < 0 ) {
      sb.optionPane().showMessageDialog( null, 'Debe seleccionar un banco', 'Error', JOptionPane.ERROR_MESSAGE )
      result = false
    }
    if ( StringUtils.isBlank( deposit.reference ) ) {
      sb.optionPane().showMessageDialog( null, 'El campo referencia es obligatorio', 'Error', JOptionPane.ERROR_MESSAGE )
      result = false
    }
    if ( StringUtils.isBlank( ammount.text ) ) {
      sb.optionPane().showMessageDialog( null, 'El campo monto es obligatorio', 'Error', JOptionPane.ERROR_MESSAGE )
      result = false
    } else {
      if ( deposit.ammount <= 0 ) {
        sb.optionPane().showMessageDialog( null, 'El campo monto debe ser mayor a 1', 'Error', JOptionPane.ERROR_MESSAGE )
        result = false
      }
    }
    return result
  }
}

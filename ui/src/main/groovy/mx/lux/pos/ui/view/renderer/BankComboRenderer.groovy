package mx.lux.pos.ui.view.renderer

import mx.lux.pos.ui.model.Bank

import java.awt.Component
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.ListCellRenderer

class BankComboRenderer extends JLabel implements ListCellRenderer {

  @Override
  Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
    Bank bank = value as Bank
    setText( bank.name )
    return this
  }
}

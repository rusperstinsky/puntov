package mx.lux.pos.ui.view.renderer

import mx.lux.pos.ui.model.Plan

import java.awt.Component
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.ListCellRenderer

class PlanComboRenderer extends JLabel implements ListCellRenderer {

  @Override
  Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
    Plan plan = value as Plan
    setText( plan.id )
    return this
  }
}

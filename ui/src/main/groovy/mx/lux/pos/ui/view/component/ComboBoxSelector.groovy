package mx.lux.pos.ui.view.component

import mx.lux.pos.ui.model.adapter.StringAdapter

import javax.swing.JComboBox

class ComboBoxSelector<T extends Object> {
  
  private JComboBox comboBox = new JComboBox<String>()
  private StringAdapter<T> adapter = new StringAdapter<T>()
  
  private List<T> itemList
  private List<String> comboList = new ArrayList<String> ()
   
  ComboBoxSelector( StringAdapter<T> pAdapter ) {
    this.setAdapter( pAdapter )
  }

  // Internal Methods
  private void sync() {
    comboList.clear()
    for ( T item in itemList ) {
      comboList.add( adapter.adapt( item ) )
    }
    comboBox.removeAllItems()
    for ( String text in comboList ) {
      comboBox.addItem( text )
    }
  }
  
  // Public methods
  T getSelection() {
    T selection = null
    if ( comboBox.getSelectedIndex() >= 0 ) {
      selection = itemList.get( comboBox.getSelectedIndex() )
    }
    return selection
  }
  
  void setAdapter( StringAdapter<T> pAdapter ) {
    adapter = pAdapter
  }

  JComboBox getComboBox() {
    return this.comboBox
  }

  void setItems( List<T> pItemList ) {
    itemList = pItemList
    sync()
  }

  void setSelection( T pItem ) {
    comboBox.setSelectedItem( adapter.adapt( pItem ) )
  }

}

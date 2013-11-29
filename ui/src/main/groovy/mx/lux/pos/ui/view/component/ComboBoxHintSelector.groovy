package mx.lux.pos.ui.view.component

import mx.lux.pos.ui.model.adapter.StringAdapter
import mx.lux.pos.ui.resources.UI_Standards

import javax.swing.JComboBox
import javax.swing.SwingUtilities
import javax.swing.text.JTextComponent
import java.awt.event.*

class ComboBoxHintSelector<T extends Object> {

  private JComboBox comboBox = new JComboBox<String>()
  private StringAdapter<T> adapter = new StringAdapter<T>()
  
  private List<T> itemList
  private List<String> adaptedList = new ArrayList<String>()
  private List<String> comboList = new ArrayList<String>()

  private String lockedItem = ""
  private Boolean locked = false
  private Boolean flagged = false
     
  private FocusListener trgFocus
  private KeyListener trgKey
  private ActionListener trgSelection
  
  ComboBoxHintSelector( StringAdapter<T> pAdapter ) {
    this.setAdapter( pAdapter )
    wireComponent()
  }

  // Internal Methods
  protected FocusListener getFocusTrigger() {
    if ( trgFocus == null ) {
      trgFocus = new FocusAdapter() {
        public void focusGained( FocusEvent e ) {
          SwingUtilities.invokeLater(
            new Runnable() {
              public void run() {
                ComboBoxHintSelector.this.onActivate()
              }
            }
          )
        }
      }
    }
    return trgFocus
  }
  
  protected KeyListener getKeyTrigger() {
    if ( trgKey == null ) {
      trgKey = new KeyAdapter() {
        public void keyReleased( KeyEvent e ) {
          if ( isNotMasked( e.getKeyCode() ) ) {
            SwingUtilities.invokeLater(
              new Runnable() {
                public void run() {
                  ComboBoxHintSelector.this.onValueChanged()
                }
              }
            )
          }
        }
      }
    }
    return trgKey
  }
  
  protected ActionListener getSelectionTrigger() {
    if ( trgSelection == null ) {
      trgSelection = new ActionListener() {
        public void actionPerformed( ActionEvent e ) {
          SwingUtilities.invokeLater(
            new Runnable() {
              public void run() {
                ComboBoxHintSelector.this.onSelection()
              }
            }
          )
        }
      }
    }
    return trgSelection
  }

  protected boolean isNotMasked( int keyCode ) {
    final MASK = [ KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER,
      KeyEvent.VK_TAB, KeyEvent.VK_SHIFT, KeyEvent.VK_HOME, KeyEvent.VK_END
    ]
    boolean masked = false
    for ( int mask in MASK ) {
      if ( keyCode == mask ) {
        masked = true
        break
      }
    }
    return !masked
  }
  
  protected void sync() {
    comboBox.removeAllItems()
    for ( String text in comboList ) {
      comboBox.addItem( text )
    }
  }
  
  protected void updateComboList( String pHint ) {
    comboList.clear()
    if ( pHint != null ) {
      for ( String text in adaptedList ) {
        if ( text.toLowerCase().contains( pHint.toLowerCase() ) ) {
          comboList.add( text )
        }
      }
    } else {
      comboList.addAll( adaptedList )
    }
    sync()
  }
  
  protected void wireComponent() {
    comboBox.setEditable( true )
    comboBox.getEditor().getEditorComponent().addFocusListener( this.getFocusTrigger() )
    comboBox.getEditor().getEditorComponent().addKeyListener( this.getKeyTrigger() )
    comboBox.getEditor().addActionListener( this.getSelectionTrigger() )
  }
  
  // Public methods
  void setAdapter( StringAdapter<T> pAdapter ) {
    adapter = pAdapter
  }

  JComboBox getComboBox() {
    return this.comboBox
  }

  T getSelection() {
    return adapter.findObject( comboBox.getSelectedItem(), itemList )
  }
    
  Boolean isflagged() {
    return flagged
  }
  
  Boolean isLocked() {
    return locked
  }
  
  void renderAsFlagged( ) {
    renderAsFlagged( true )
  }
  void renderAsFlagged( boolean pFlagged ) {
    JTextComponent component = ( JTextComponent ) comboBox.getEditor().getEditorComponent()
    if ( pFlagged ) {
      comboBox.background = UI_Standards.WARNING_BACKGROUND
      component.background = UI_Standards.WARNING_BACKGROUND
      component.foreground = UI_Standards.WARNING_FOREGROUND
    } else {
      comboBox.background = UI_Standards.NORMAL_BACKGROUND
      component.background = UI_Standards.NORMAL_BACKGROUND
      component.foreground = UI_Standards.NORMAL_FOREGROUND
    }
    flagged = pFlagged
  }
  
  void setItems( List<T> pItemList ) {
    itemList = pItemList
    adaptedList = adapter.adaptList( itemList )
    updateComboList( null )
  }

  void setLocked( Boolean pLocked ) {
    JTextComponent component = (JTextComponent) comboBox.getEditor().getEditorComponent()
    if ( pLocked ) {
      comboBox.removeAllItems()
      component.setBackground( UI_Standards.LOCKED_BACKGROUND )
      component.setText( "" )
    } else {
      updateComboList( null )
      component.setBackground( UI_Standards.NORMAL_BACKGROUND )
      component.setText( comboList[0] )
    }
    locked = pLocked
  }

  void setSelection( T pItem ) {
    JTextComponent component = (JTextComponent) comboBox.getEditor().getEditorComponent()
    if ( pItem != null) {
      component.setText( adapter.adapt( pItem ) )
    } else {
      component.setText( "" )
    }
    if ( isLocked() ) {
      lockedItem = component.getText()
    }
  }
  
  void setText( String pText ) {
    comboBox.setSelectedItem( pText )
  }
  
  // UI Response
  protected void onActivate() {
    if ( ! isLocked() ) {
      comboBox.getEditor().selectAll()
    }
  }
  
  protected void onSelection() {
    if ( flagged ) renderAsFlagged( false )
    if ( ! isLocked() ) {
      String selection = ( (JTextComponent) comboBox.getEditor().getEditorComponent() ).getText()
      updateComboList( null )
      comboBox.setSelectedItem( selection )
      comboBox.hidePopup()
    }
  }
  
  protected void onValueChanged() {
    if ( flagged ) renderAsFlagged( false )
    if ( ! isLocked() ) {
      String hint = ( (JTextComponent) comboBox.getEditor().getEditorComponent() ).getText()
      int caret = ( (JTextComponent) comboBox.getEditor().getEditorComponent() ).getCaretPosition()
      updateComboList( hint )
      comboBox.setSelectedItem( hint )
      ( (JTextComponent) comboBox.getEditor().getEditorComponent() ).setCaretPosition( caret )
      if (itemList.size() > 0) {
        if ( !comboBox.isPopupVisible() ) comboBox.showPopup()
      } else {
        if ( comboBox.isPopupVisible() ) comboBox.hidePopup()
      }
    } else {
      comboBox.setSelectedItem( lockedItem )
      setLocked( true )
    }
  }


}

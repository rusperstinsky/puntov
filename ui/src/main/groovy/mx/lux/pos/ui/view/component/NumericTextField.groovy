package mx.lux.pos.ui.view.component

import mx.lux.pos.ui.resources.UI_Standards
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils

import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.InputVerifier
import javax.swing.JComponent
import javax.swing.JTextField

class NumericTextField extends JTextField {
  
  private static final long serialVersionUID = -8645248745315326199L
  private static final String FMT_DEFAULT = "%,.2f"

  private Double numericValue = 0.0
  private String formatString = FMT_DEFAULT
  private InputVerifier verifier
  private FocusListener activateTrigger

  NumericTextField( ) {
    this( 0.0 )
  }
  NumericTextField( Double pValue ) {
    addFocusListener( getActivateTrigger( ) )
    setInputVerifier( getVerifier( ) )
    setValue( pValue )
  }
  NumericTextField( String pText ) {
    this( 0.0 )
    setText( pText )
  }

  // Internal Methods
  protected FocusListener getActivateTrigger( ) {
    if ( activateTrigger == null ) {
      activateTrigger = new FocusAdapter() {
        public void focusGained( FocusEvent pE ) {
          NumericTextField.this.selectAll( )
        }
        public void focusLost( FocusEvent pE ) {
          NumericTextField.this.setValue( NumericTextField.this.value )
        }
      }
    }
    return activateTrigger
  }

  protected InputVerifier getVerifier( ) {
    if ( verifier == null ) {
      verifier = new InputVerifier( ) {
        public boolean verify( JComponent pInput ) {
          boolean verified = false
          NumericTextField comp = NumericTextField.this
          try {
            comp.setValue( comp.parse( comp.getText( ) ) )
            verified = true
          } catch ( Exception e ) {
            UI_Standards.setFlagged( comp )
          }
          return verified
        }
      }
    }
    return verifier
  }
  
  protected Double parse( String pNumericString ) {
    String str = StringUtils.trimToEmpty( pNumericString )
    str = StringUtils.remove( str, '$' )
    str = StringUtils.remove( str, ',' )
    Double value = NumberUtils.createDouble( str )
    return value
  }
  
  // Public methods
  void setText( String pText ) {
    setValue( parse( pText ) )
  }

  Double getValue( ) {
    return numericValue
  }
  void setValue( Double pValue ) {
    numericValue = pValue
    super.setText( String.format( formatString, numericValue ) )
    UI_Standards.setFlagged( this, false )
  }
  
}

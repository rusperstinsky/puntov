package mx.lux.pos.ui.view.verifier

import org.apache.commons.lang3.StringUtils

import java.awt.Color
import javax.swing.*

class NotEmptyVerifier extends InputVerifier {

  @Override
  boolean verify( JComponent component ) {
    String value = null
    if ( component instanceof JTextField ) {
      JTextField tf = component
      value = tf.text
    }
    if ( StringUtils.isBlank( value ) ) {
      component.border = BorderFactory.createLineBorder( Color.RED )
      return false
    } else {
      component.border = UIManager.getBorder( 'TextField.border' )
    }
    return true
  }
}

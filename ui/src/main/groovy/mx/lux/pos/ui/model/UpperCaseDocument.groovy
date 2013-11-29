package mx.lux.pos.ui.model

import javax.swing.text.AttributeSet
import javax.swing.text.BadLocationException
import javax.swing.text.PlainDocument

class UpperCaseDocument extends PlainDocument {

  void insertString( int offs, String str, AttributeSet a ) throws BadLocationException {
    if ( str == null ) {
      return
    }
    char[] upper = str.toCharArray()
    upper.eachWithIndex { it, idx ->
      upper[ idx ] = it.toUpperCase()
    }
    super.insertString( offs, upper.toString(), a )
  }
}

package mx.lux.pos.util

import javax.swing.SwingUtilities
import org.apache.commons.lang.math.NumberUtils
import java.text.NumberFormat

class IntegrationTest  {
    String[] strValues = ['1','123','02 ',' 3','4.0','5 ','5,012','5,001.00','']

    Integer parseInt( String value ){
        Integer valor = null
        try{
        valor = Integer.parseInt( value )
        } catch( Exception ){}
        return valor
    }

    Integer valueOf( String value ){
        Integer valor = null
        try{
        valor = Integer.valueOf( value )
        } catch( Exception ){}
        return valor
    }

    Integer createInt( String value ){
        Integer valor = null
        try{
        valor = NumberUtils.createInteger( value )
        }catch (Exception){}
        return valor
    }

    Integer numberFormatParse( String value ){
        Integer valor = null
        try{
        NumberFormat nf = NumberFormat.getInstance()
        valor = nf.parse( value )
        }catch( Exception ) {}
        return valor
    }

  void convertStringToInteger(){
      for( String str : strValues ){
          Integer parseInt = this.parseInt( str )
          Integer valueOf = this.valueOf( str )
          Integer createInt = this.createInt( str )
          Integer numberFormatParse = this.numberFormatParse( str )
          println "(${str}) parseInt ${parseInt}"
          println "(${str}) valueOf ${valueOf}"
          println "(${str}) createInt ${createInt}"
          println "(${str}) numberFormatParse ${numberFormatParse}"
      }
  }

    static void main( args ) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    void run( ) {
                        IntegrationTest integration = new IntegrationTest()
                        integration.convertStringToInteger()
                    }
                }
        )
    }


}

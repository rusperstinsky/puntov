package mx.lux.pos.util

import java.text.SimpleDateFormat

import org.apache.commons.lang3.time.DateUtils

import spock.lang.Specification

class StringListTest extends Specification {

  def "test Normal List of Strings"( ) {
    setup:
      StringList list = new StringList( )
      list.add( "Sara Peto" )      
      list.add( "Carlos Vazquez" )
      list.add( "Lux Coapa" )
      list.add( "OLU 981235 XN0" )
      list.add( "Opticas Lux" )
    expect:
      list.toString().equals( "Sara Peto!Carlos Vazquez!Lux Coapa!OLU 981235 XN0!Opticas Lux" )
      list.entry( 0 ).equals( "Sara Peto" )
      list.entry( 3 ).equals( "OLU 981235 XN0" )
  }
  
  def "test initialize with list"( ) {
    setup:
      StringList list = new StringList( "Sara Peto!Carlos Vazquez!Lux Coapa!OLU 981235 XN0!Opticas Lux" )
    expect:
      list.toString().equals( "Sara Peto!Carlos Vazquez!Lux Coapa!OLU 981235 XN0!Opticas Lux" )
      list.entry( 0 ).equals( "Sara Peto" )
      list.entry( 3 ).equals( "OLU 981235 XN0" )
  }
  
  def "test empty elements"( ) {
    when:
      StringList list = new StringList( )
      list.add( "Sara Peto" )
      list.add( "Carlos Vazquez" )
      list.add( " " )
      list.add( "Lux Coapa" )
      list.add( "" )
      list.add( "OLU 981235 XN0" )
      list.add( "Opticas Lux" )
    then:
      list.size == 7
      list.entry( 3 ).equals( "Lux Coapa" ) 
      list.entry( 2 ).length( ) == 0
      
    when:
      list = new StringList( "Sara Peto!Carlos Vazquez!!Lux Coapa!  !OLU 981235 XN0!Opticas Lux" )
    then:
      list.size == 7
      list.entry( 3 ).equals( "Lux Coapa" ) 
      list.entry( 4 ).length( ) == 0
  }
  
  def "parse Date"( ) {
    when:
      String fmt = "dd/MM/yyyy"
      SimpleDateFormat df_1 = new SimpleDateFormat( "yyyy/MM/dd" )
      SimpleDateFormat df_2 = new SimpleDateFormat( fmt )
      SimpleDateFormat df_3 = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" )
      Date dt = new Date( )
      Date tm = new Date( dt.getTime( ) - Math.round( 10000 * Math.random( ) - 5000 ) )
      StringList list = new StringList( )
      list.add( "Opticas Lux" )
      list.add( "Lux Coapa" )
      list.add( "OLU 981235 XN0" )
      list.addDate( dt )
      list.addDateTime( tm )
      list.addDate( dt, fmt )
      Date expected_dt = DateUtils.truncate( dt, Calendar.DATE )
      Date expected_tm = DateUtils.truncate( tm, Calendar.SECOND )
      Date actual_dt = list.asDate( 3 )
      Date actual_tm = list.asDateTime( 4 )
      Date actual_dt_2 = list.asDate( 5, fmt )
      
    then:
      list.entry( 3 ).equals( df_1.format( expected_dt ) )        
      list.entry( 4 ).equals( df_3.format( tm ) )        
      list.entry( 5 ).equals( df_2.format( expected_dt ) )
      expected_dt.equals( actual_dt )
      expected_dt.equals( actual_dt_2 )
      expected_tm.equals( actual_tm )      
  }
  
  def "parse Boolean"( ) {
    when:
      Boolean trueValue = true
      Boolean falseValue = false
      StringList list = new StringList( )
      list.add( "Opticas Lux" )
      list.add( "Lux Coapa" )
      list.add( "OLU 981235 XN0" )
      list.addBoolean( trueValue )
      list.addBoolean( falseValue )
      list.addBoolean( trueValue, "on/off" )
      list.addBoolean( falseValue, "on/off" )

    then:
      list.entry( 3 ).equals( "true" )
      list.entry( 4 ).equals( "false" )
      list.entry( 5 ).equals( "on" )
      list.entry( 6 ).equals( "off" )
      list.isTrue( 3 ) == true
      list.isTrue( 4 ) == false
      list.isFalse( 4, "off" ) == true
      list.isEquals( 5, "on" ) == true
      list.isEquals( 6, "on" ) == false
  }

  def "parse Integer"( ) {
    when:
      Integer num_1 = Math.round( 1000 * Math.random( ) )
      Integer num_2 = Math.round( 1000 * Math.random( ) )
      Integer num_3 = Math.round( 1000 * Math.random( ) )
      StringList list = new StringList( )
      list.add( "Opticas Lux" )
      list.add( "Lux Coapa" )
      list.add( "OLU 981235 XN0" )
      list.addInteger( num_1 )
      list.addInteger( num_2 )
      list.addInteger( num_3, "%08d" )

    then:
      list.entry( 3 ).equals( num_1.toString( ) )
      list.entry( 4 ).equals( num_2.toString( ) )
      list.entry( 5 ).equals( String.format("%08d", num_3 ) )
      list.asInteger( 3 ).equals( num_1 )
      list.asInteger( 4 ).equals( num_2 )
      list.asInteger( 5 ).equals( num_3 )
  }

  def "parse Double"( ) {
    when:
      String fmt_2 = "%.2f"
      String fmt_3 = "%.6f"
      Double num_1 = ( 100000.0f * Math.random( ) )
      Double num_2 = ( 100000.0f * Math.random( ) )
      Double num_3 = ( 100000.0f * Math.random( ) )
      StringList list = new StringList( )
      list.add( "Opticas Lux" )
      list.add( "Lux Coapa" )
      list.add( "OLU 981235 XN0" )
      list.addDouble( num_1 )
      list.addDouble( num_2, fmt_2 )
      list.addDouble( num_3, fmt_3 )
      
    then:
      list.entry( 3 ).equals( String.format( "%.3f", num_1 ) )
      list.entry( 4 ).equals( String.format( fmt_2, num_2 ) )
      list.entry( 5 ).equals( String.format( fmt_3, num_3 ) )
      Math.abs( list.asDouble( 3 ) - num_1 ) < 0.001f
      Math.abs( list.asDouble( 4 ) - num_2 ) < 0.01f
      Math.abs( list.asDouble( 5 ) - num_3 ) < 0.00001f
  }


}

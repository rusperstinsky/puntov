package mx.lux.pos.util

import spock.lang.Specification
import java.util.regex.Pattern
import org.apache.commons.lang.StringUtils

class FileFilterTest extends Specification {

  def "Test toRegExp(Productos1.txt)" () {
    setup:
    String filePattern = 'Prod.{MES}-{DIA}.11.txt'
    String regexp = FileFilterUtil.toRegExpLowerCase( filePattern )
    println String.format( "%s.toRegExp(): %s", filePattern, regexp )
    Pattern pattern = Pattern.compile(regexp)

    expect:
    StringUtils.trimToEmpty(filename).toLowerCase().matches(pattern) == match

    where:
    filename << ['PROD.05-12.11.TXT', 'prod.03-21.11.txt', 'PROD051211.TXT', 'prod0512_11.txt' ]
    match << [true, true, false, false ]
  }

  def "Test toRegExp(Productos2.txt)" () {
    setup:
    String filePattern = 'Prod{MES}{DIA}11.txt'
    String regexp = FileFilterUtil.toRegExpLowerCase( filePattern )
    println String.format( "%s.toRegExp(): %s", filePattern, regexp )
    Pattern pattern = Pattern.compile(regexp)

    expect:
    StringUtils.trimToEmpty(filename).toLowerCase().matches(pattern) == match

    where:
    filename << ['PROD.05-12.11.TXT', 'prod.03-21.11.txt', 'PROD051211.TXT', 'prod0512_11.txt' ]
    match << [false, false, true, false ]
  }

  def "Test toRegExp(Productos3.txt)" () {
    setup:
    String filePattern = 'PROD{MES}{DIA}_11.TXT'
    String regexp = FileFilterUtil.toRegExpLowerCase( filePattern )
    println String.format( "%s.toRegExp(): %s", filePattern, regexp )
    Pattern pattern = Pattern.compile(regexp)

    expect:
    StringUtils.trimToEmpty(filename).toLowerCase().matches(pattern) == match

    where:
    filename << ['PROD.05-12.11.TXT', 'prod.03-21.11.txt', 'PROD051211.TXT', 'prod0512_11.txt' ]
    match << [false, false, false, true ]
  }

}

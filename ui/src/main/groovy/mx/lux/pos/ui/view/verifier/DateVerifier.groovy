package mx.lux.pos.ui.view.verifier

import java.text.SimpleDateFormat

@Singleton
class DateVerifier {
  private enum Field { INIT, DAY, MONTH, YEAR }
  private static final String DELIMITERS = "/-.,"

  private int day, month, year
  private int baseDay, baseMonth, baseYear, baseCentury
  private Field lastField

  protected int getDay() {
    int dy = this.day
    if (dy == 0) dy = this.baseDay
    return dy
  }
  
  protected int getMonth() {
    int mo = this.month
    if (mo == -1) mo = this.baseMonth
    return mo
  }
  
  protected int getYear() {
    int yr = this.year
    if (yr == 0) yr = this.baseYear
    if (yr < 100) yr = yr + this.baseCentury
    return yr
  }
  
  protected void init() {
    this.day = 0
    this.month = -1
    this.year = 0
    Calendar cal = Calendar.getInstance()
    this.baseDay = cal.get(Calendar.DAY_OF_MONTH) 
    this.baseMonth = cal.get(Calendar.MONTH) 
    this.baseYear = cal.get(Calendar.YEAR)
    this.baseCentury = (this.baseYear / 100) * 100
    lastField = Field.INIT
  }
  
  protected void parseNumeric(String pNumeric) {
    switch (this.lastField) {
      case Field.INIT:     this.day = Integer.valueOf(pNumeric);        break
      case Field.DAY:      this.month = Integer.valueOf(pNumeric) - 1;  break
      case Field.MONTH:    this.year = Integer.valueOf(pNumeric);       break
      case Field.YEAR:     this.day = Integer.valueOf(pNumeric);        break
    }
    this.lastField = Field.values()[Math.min(this.lastField.ordinal() + 1, Field.values().length)]
  }
  
  protected void parseString(String pText) {
    StringBuffer buffer = new StringBuffer()
    for (char c : pText.trim().toCharArray()) {
      if (Character.isDigit(c)) {
        buffer.append(c)
      } else if (Character.isWhitespace(c) || Character.isLetter(c)) {
        if (buffer.length() > 0) { 
          this.parseNumeric(buffer.toString())
          buffer = new StringBuffer()
        }
        break
      } else if (DELIMITERS.contains(Character.toString(c))) {
        if (buffer.length() > 0) { 
          this.parseNumeric(buffer.toString())
          buffer = new StringBuffer()
        }
      }
      if (this.lastField.equals(Field.YEAR)) break
    }
    if (buffer.length() > 0) { 
      this.parseNumeric(buffer.toString())
    }
  }

  Date parse(String pText) {
    this.init()
    this.parseString(pText)
    Calendar cal = Calendar.getInstance()
    cal.set(Calendar.MILLISECOND, 0)
    cal.set(this.getYear(), this.getMonth(), this.getDay(), 0, 0, 0)
    Date dt = cal.getTime()
    return dt 
  }
  
  static void main(String[] args) {
    final INPUTS = [ "01/08/2012", "01/08/12", "04", "04/05", "11/10/2010", "32/01/2012", "32/02", "04/15", 
      "15/05", "2010/08/01", "05-ENE", "05 05 2012", "04/15 '89", "4.5.2010", "01-08-2012", "01-08-12", "04", 
      "04-05", "11-10-2010", "32-01-2012", "32-02", "04-15", "15-05", "2010-08-01"  
    ]
    
    final SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy  HH:mm:SS")
    DateVerifier parser = DateVerifier.getInstance()
    for (String input : INPUTS) {
      Date dt = parser.parse(input)
      System.out.println(String.format("Text: <%s>   Parsed: <%s>", input, (dt != null ? fmt.format(dt) : "null")))
    }
  }
  
}

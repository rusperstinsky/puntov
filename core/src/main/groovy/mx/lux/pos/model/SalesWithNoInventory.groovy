package mx.lux.pos.model

import org.apache.commons.lang3.StringUtils

enum SalesWithNoInventory {
  ALLOWED('S'),
  REQUIRE_AUTHORIZATION('A'),
  RESTRICTED('N')

  // Enumeration Methods
  private String parameterValue

  SalesWithNoInventory( String pParameterValue ) {
    this.parameterValue = StringUtils.trimToEmpty( pParameterValue ).toUpperCase()
  }

  String getValue( ) {
    return this.parameterValue
  }


}

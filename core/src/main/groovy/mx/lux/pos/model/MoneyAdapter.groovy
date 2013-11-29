package mx.lux.pos.model

import org.hibernate.HibernateException
import org.hibernate.engine.spi.SessionImplementor
import org.hibernate.type.StringType
import org.hibernate.usertype.UserType
import org.postgresql.util.PGmoney

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class MoneyAdapter implements UserType {

  @Override
  public int[] sqlTypes( ) {
    return [ StringType.INSTANCE.sqlType() ]
  }

  @Override
  public Class returnedClass( ) {
    return BigDecimal.class
  }

  @Override
  public boolean equals( Object x, Object y ) throws HibernateException {
    return x?.equals( y )
  }

  @Override
  public int hashCode( Object x ) throws HibernateException {
    return x?.hashCode()
  }

  @Override
  public Object nullSafeGet( ResultSet rs, String[] names, SessionImplementor session, Object owner ) throws HibernateException, SQLException {
    String value = StringType.INSTANCE.get( rs, names[ 0 ], session )
    value = value?.replaceAll( /[$|,]/, '' )
    if ( value?.isNumber() ) {
      return value.toBigDecimal()
    }
    return BigDecimal.ZERO
  }

  @Override
  public void nullSafeSet( PreparedStatement st, Object value, int index, SessionImplementor session ) throws HibernateException, SQLException {
    BigDecimal val = value as BigDecimal
    st.setObject( index, new PGmoney( val?.toDouble() ?: 0 ) )
  }

  @Override
  public Object deepCopy( Object value ) throws HibernateException {
    return value
  }

  @Override
  public boolean isMutable( ) {
    return false
  }

  @Override
  public Serializable disassemble( Object value ) throws HibernateException {
    return value as Serializable
  }

  @Override
  public Object assemble( Serializable cached, Object owner ) throws HibernateException {
    return cached
  }

  @Override
  public Object replace( Object original, Object target, Object owner ) throws HibernateException {
    return original
  }
}

package mx.lux.pos.model;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table( name = "moneda_det", schema = "public" )
public class MonedaDetalle implements Serializable, Comparable<MonedaDetalle> {

    private static final long serialVersionUID = 120566201004L;

    private static final double ZERO_TOLERANCE = 0.0001;
    private static final DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" );

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "moneda_det_num_serial_seq" )
    @SequenceGenerator( schema = "public", sequenceName = "moneda_det_num_serial_seq",
            name = "moneda_det_num_serial_seq", allocationSize = 1 )
    @Column( name = "num_serial" )
    private Integer numSerial;

    @Column( name = "id_moneda" )
    private String idMoneda;

    @Column( name = "fecha_activa" )
    private Date fechaActiva;

    @Column( name = "tipo_cambio" )
    private BigDecimal tipoCambio;

    public Integer getNumSerial() {
        return numSerial;
    }

    public void setNumSerial( Integer numSerial ) {
        this.numSerial = numSerial;
    }

    public String getIdMoneda() {
        return idMoneda;
    }

    public void setIdMoneda( String idMoneda ) {
        this.idMoneda = StringUtils.trimToEmpty( idMoneda ).toUpperCase();
    }

    public Date getFechaActiva() {
        return fechaActiva;
    }

    public void setFechaActiva( Date fechaActiva ) {
        this.fechaActiva = DateUtils.truncate( fechaActiva, Calendar.DATE );
    }

    public BigDecimal getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio( BigDecimal tipoCambio ) {
        this.tipoCambio = tipoCambio;
    }

    // Data management
    @PostLoad
    public void postLoad() {
        if ( ( this.getTipoCambio() == null )
                || ( Math.abs( this.getTipoCambio().doubleValue() ) < ZERO_TOLERANCE ) ) {
            this.setTipoCambio( new BigDecimal( "1.0000" ) );
        }
    }

    // Identity
    public int compareTo( MonedaDetalle pMonedaDet ) {
        int result = this.getIdMoneda().compareTo( pMonedaDet.getIdMoneda() );
        if ( result == 0 ) {
            result = -1 * this.getFechaActiva().compareTo( pMonedaDet.getFechaActiva() );
        }
        return result;
    }

    public boolean equals( Object pObj ) {
        boolean result = false;
        if ( pObj instanceof MonedaDetalle ) {
            result = this.getIdMoneda().equals( ( ( MonedaDetalle ) pObj ).getIdMoneda() )
                    && this.getFechaActiva().equals( ( ( MonedaDetalle ) pObj ).getFechaActiva() );
        } else if ( pObj instanceof Moneda ) {
            result = this.getIdMoneda().equals( ( ( Moneda ) pObj ).getIdMoneda() );
        } else if ( pObj instanceof String ) {
            result = this.getIdMoneda().equalsIgnoreCase( ( ( String ) pObj ).trim() );
        }
        return result;
    }

    public boolean equals( String pIdMoneda, Date pFechaActiva ) {
        boolean result = false;
        if ( ( pIdMoneda != null ) && ( pFechaActiva != null ) ) {
            result = this.getIdMoneda().equals( StringUtils.trimToEmpty( pIdMoneda ).toUpperCase() )
                    && this.getFechaActiva().equals( DateUtils.truncate( pFechaActiva, Calendar.DATE ) );
        }
        return result;
    }

    public int hashCode() {
        return this.getFechaActiva().hashCode();
    }

    public String toString() {
        String str = String.format( "T.C.[%s, %s]: %,.4f", this.getIdMoneda(), df.format( this.getFechaActiva() ),
                this.getTipoCambio().doubleValue() );
        return str;
    }


}

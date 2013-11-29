package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table( name = "apertura", schema = "public" )
public class Apertura {
    private static final long serialVersionUID = 120566121120L;

    @Id
    @Column( name = "fecha_apertura" )
    private Date fechaApertura;

    @Column( name = "efvo_mxp" )
    private BigDecimal efvoPesos;

    @Column( name = "efvo_usd" )
    private BigDecimal efvoDolares;

    @Column( name = "observaciones" )
    private String observaciones;

    @PrePersist
    void prePersist() {
        if (efvoPesos == null) {
            efvoPesos = BigDecimal.ZERO;
        }
        if (efvoDolares == null) {
            efvoDolares = BigDecimal.ZERO;
        }
        observaciones = StringUtils.trimToEmpty(observaciones);
    }

    @PreUpdate
    void preUpdate() {
        this.prePersist();
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = DateUtils.truncate( fechaApertura, Calendar.DATE );
    }

    public BigDecimal getEfvoPesos() {
        BigDecimal efvo = BigDecimal.ZERO;
        if (efvoPesos != null) {
            efvo = efvoPesos;
        }
        return efvo;
    }

    public void setEfvoPesos(BigDecimal efvoPesos) {
        this.efvoPesos = efvoPesos;
    }

    public BigDecimal getEfvoDolares() {
        BigDecimal efvo = BigDecimal.ZERO;
        if (efvoDolares != null) {
            efvo = efvoDolares;
        }
        return efvo;
    }

    public void setEfvoDolares(BigDecimal efvoDolares) {
        this.efvoDolares = efvoDolares;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = StringUtils.trimToEmpty( observaciones );
    }

    // Identity
    public int compareTo( Apertura pApertura ) {
        return this.getFechaApertura().compareTo( pApertura.getFechaApertura() );
    }

    public int hashCode() {
        return this.getFechaApertura().hashCode();
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if ( obj instanceof Apertura ) {
            result = this.getFechaApertura().equals( ((Apertura) obj).getFechaApertura() );
        } else if ( obj instanceof Date ) {
            Date fcha = DateUtils.truncate( (Date) obj, Calendar.DATE );
            result = fcha.equals( this.getFechaApertura() );
        }
        return result;
    }

    public String toString() {
        return String.format( "Apertura(%s):  MXN:%,.2f  USD:%,.2f", this.getFechaApertura(), this.getEfvoPesos(),
               this.getEfvoDolares() );
    }
}

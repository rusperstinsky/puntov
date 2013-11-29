package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table( name = "trans_inv", schema = "public" )
public class TransInv implements Serializable, Comparable<TransInv> {

    private static final long serialVersionUID = 2336293483244885168L;

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "trans_inv_num_reg_seq" )
    @SequenceGenerator( schema = "public", sequenceName = "trans_inv_num_reg_seq", name = "trans_inv_num_reg_seq", allocationSize = 1 )
    @Column( name = "num_reg" )
    private Integer numReg;

    @Column( name = "id_tipo_trans" )
    private String idTipoTrans;

    @Column( name = "folio" )
    private Integer folio;

    @Column( name = "fecha" )
    private Date fecha;

    @Column( name = "id_sucursal" )
    private Integer sucursal;

    @Column( name = "id_sucursal_destino" )
    private Integer sucursalDestino;

    @Column( name = "referencia" )
    private String referencia;

    @Column( name = "observaciones" )
    private String observaciones;

    @Column( name = "id_empleado" )
    private String idEmpleado;

    @Column( name = "fecha_mod" )
    private Date fechaMod;

    @Transient
    private List<TransInvDetalle> trDet = new ArrayList<TransInvDetalle>();

    @PostLoad
    protected void trim() {
        this.referencia = StringUtils.trimToEmpty( this.referencia );
        this.observaciones = StringUtils.trimToEmpty( this.observaciones );
    }

    @PrePersist
    protected void prePersist() {
        fechaMod = new Date();
    }

    @PreUpdate
    protected void preUpdate() {
        fechaMod = new Date();
    }

    public Integer getNumReg() {
        return numReg;
    }

    public void setNumReg( Integer numRecord ) {
        this.numReg = numRecord;
    }

    public String getIdTipoTrans() {
        return idTipoTrans;
    }

    public void setIdTipoTrans( String idTipoTrans ) {
        this.idTipoTrans = idTipoTrans.trim().toUpperCase();
        for ( TransInvDetalle det : this.getTrDet() ) {
            det.setIdTipoTrans( this.getIdTipoTrans() );
        }
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio( Integer folio ) {
        this.folio = folio;
        for ( TransInvDetalle det : this.getTrDet() ) {
            det.setFolio( this.getFolio() );
        }
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha( Date fecha ) {
        this.fecha = DateUtils.truncate( fecha, Calendar.DATE );
    }

    public Integer getSucursal() {
        return sucursal;
    }

    public void setSucursal( Integer sucursal ) {
        this.sucursal = sucursal;
    }

    public Integer getSucursalDestino() {
        return sucursalDestino;
    }

    public void setSucursalDestino( Integer sucursalDestino ) {
        this.sucursalDestino = sucursalDestino;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia( String referencia ) {
        this.referencia = referencia.trim().toUpperCase();
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones( String observaciones ) {
        this.observaciones = observaciones.trim();
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado( String idEmpleado ) {
        this.idEmpleado = idEmpleado.trim().toUpperCase();
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod( Date fechaMod ) {
        this.fechaMod = fechaMod;
    }

    public List<TransInvDetalle> getTrDet() {
        return trDet;
    }

    public void setTrDet( List<TransInvDetalle> trDet ) {
        this.trDet = trDet;
    }

    // Other
    public void add( TransInvDetalle pTrDet ) {
        this.trDet.add( pTrDet );
    }

    // Entity integriy
    public int compareTo( TransInv pTrans ) {
        return ( -1 * fechaMod.compareTo( pTrans.fechaMod ) );
    }

    public boolean equals( Object pObj ) {
        boolean result = false;
        if ( pObj instanceof TransInv ) {
            result = this.getIdTipoTrans().equals( ( ( TransInv ) pObj ).getIdTipoTrans() )
                    && this.getFolio().equals( ( ( TransInv ) pObj ).getFolio() );
        } else if ( pObj instanceof TransInvDetalle ) {
            result = this.getIdTipoTrans().equals( ( ( TransInvDetalle ) pObj ).getIdTipoTrans() )
                    && this.getFolio().equals( ( ( TransInvDetalle ) pObj ).getFolio() );
        }
        return result;
    }

    public int hashCode() {
        return this.getFolio();
    }

    public String toString() {
        final DateFormat df = new SimpleDateFormat( "dd/MMM/yyyy" );
        return String.format( "[%s] %s:%06d   Ref:%s", df.format( this.getFecha() ), this.getIdTipoTrans(),
                this.getFolio(), this.getReferencia() );
    }

}

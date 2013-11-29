package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table( name = "vouchers_tmp", schema = "public" )
public class VoucherTmp implements Serializable {

    private static final long serialVersionUID = -838248214076922939L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "vouchers_tmp_id_seq" )
    @SequenceGenerator( name = "vouchers_tmp_id_seq", sequenceName = "vouchers_tmp_id_seq" )
    @Column( name = "id" )
    private Integer id;

    @Column( name = "id_term", length = 5 )
    private String idTerminal;

    @Column( name = "num_tarjeta" )
    private String numeroTarjeta;

    @Column( name = "autorizacion" )
    private String autorizacion;

    @Column( name = "id_f_pago", length = 2 )
    private String idFPago;

    @Column( name = "plan", length = 3 )
    private String plan;

    @Column( name = "cant" )
    private Integer cantidad;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_cierre" )
    private Date fechaCierre;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod" )
    private Date fechaModificacion;

    @PrePersist
    private void onPrePersist() {
        fechaModificacion = new Date();
    }

    @PreUpdate
    private void onPreUpdate() {
        fechaModificacion = new Date();
    }

    @PostLoad
    private void onPostLoad() {
        idTerminal = StringUtils.trimToEmpty( idTerminal );
        numeroTarjeta = StringUtils.trimToEmpty( numeroTarjeta );
        autorizacion = StringUtils.trimToEmpty( autorizacion );
        idFPago = StringUtils.trimToEmpty( idFPago );
        plan = StringUtils.trimToEmpty( plan );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getIdTerminal() {
        return idTerminal;
    }

    public void setIdTerminal( String idTerminal ) {
        this.idTerminal = idTerminal;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta( String numeroTarjeta ) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion( String autorizacion ) {
        this.autorizacion = autorizacion;
    }

    public String getIdFPago() {
        return idFPago;
    }

    public void setIdFPago( String idFPago ) {
        this.idFPago = idFPago;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan( String plan ) {
        this.plan = plan;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad( Integer cantidad ) {
        this.cantidad = cantidad;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre( Date fechaCierre ) {
        this.fechaCierre = fechaCierre;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion( Date fechaModificacion ) {
        this.fechaModificacion = fechaModificacion;
    }
}
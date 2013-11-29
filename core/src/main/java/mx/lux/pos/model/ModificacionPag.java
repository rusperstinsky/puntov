package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table( name = "mod_pag", schema = "public" )
public class ModificacionPag implements Serializable {

    private static final long serialVersionUID = -2720703324523716571L;

    @Id
    @Column( name = "id_mod" )
    private Integer id;

    @Column( name = "status_old", length = 1 )
    private String estadoViejo;

    @Column( name = "id_pago_old" )
    private Integer idPagoViejo;

    @Column( name = "id_pago_new" )
    private Integer idPagoNuevo;

    @Column( name = "fp_old", length = 2 )
    private String formaPagoViejo;

    @Column( name = "fp_new", length = 2 )
    private String formaPagoNuevo;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "monto_old" )
    private BigDecimal montoViejo;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "monto_new" )
    private BigDecimal montoNuevo;

    @Column( name = "banco_old" )
    private Integer bancoViejo;

    @Column( name = "banco_new" )
    private Integer bancoNuevo;

    @Column( name = "ref_old", length = 18 )
    private String referenciaViejo;

    @Column( name = "ref_new", length = 18 )
    private String referenciaNuevo;

    @PostLoad
    private void onPostLoad() {
        formaPagoViejo = StringUtils.trimToEmpty( formaPagoViejo );
        formaPagoNuevo = StringUtils.trimToEmpty( formaPagoNuevo );
        referenciaViejo = StringUtils.trimToEmpty( referenciaViejo );
        referenciaNuevo = StringUtils.trimToEmpty( referenciaNuevo );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getEstadoViejo() {
        return estadoViejo;
    }

    public void setEstadoViejo( String estadoViejo ) {
        this.estadoViejo = estadoViejo;
    }

    public Integer getIdPagoViejo() {
        return idPagoViejo;
    }

    public void setIdPagoViejo( Integer idPagoViejo ) {
        this.idPagoViejo = idPagoViejo;
    }

    public Integer getIdPagoNuevo() {
        return idPagoNuevo;
    }

    public void setIdPagoNuevo( Integer idPagoNuevo ) {
        this.idPagoNuevo = idPagoNuevo;
    }

    public String getFormaPagoViejo() {
        return formaPagoViejo;
    }

    public void setFormaPagoViejo( String formaPagoViejo ) {
        this.formaPagoViejo = formaPagoViejo;
    }

    public String getFormaPagoNuevo() {
        return formaPagoNuevo;
    }

    public void setFormaPagoNuevo( String formaPagoNuevo ) {
        this.formaPagoNuevo = formaPagoNuevo;
    }

    public BigDecimal getMontoViejo() {
        return montoViejo;
    }

    public void setMontoViejo( BigDecimal montoViejo ) {
        this.montoViejo = montoViejo;
    }

    public BigDecimal getMontoNuevo() {
        return montoNuevo;
    }

    public void setMontoNuevo( BigDecimal montoNuevo ) {
        this.montoNuevo = montoNuevo;
    }

    public Integer getBancoViejo() {
        return bancoViejo;
    }

    public void setBancoViejo( Integer bancoViejo ) {
        this.bancoViejo = bancoViejo;
    }

    public Integer getBancoNuevo() {
        return bancoNuevo;
    }

    public void setBancoNuevo( Integer bancoNuevo ) {
        this.bancoNuevo = bancoNuevo;
    }

    public String getReferenciaViejo() {
        return referenciaViejo;
    }

    public void setReferenciaViejo( String referenciaViejo ) {
        this.referenciaViejo = referenciaViejo;
    }

    public String getReferenciaNuevo() {
        return referenciaNuevo;
    }

    public void setReferenciaNuevo( String referenciaNuevo ) {
        this.referenciaNuevo = referenciaNuevo;
    }
}

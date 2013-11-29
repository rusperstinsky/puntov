package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table( name = "mod_imp", schema = "public" )
public class ModificacionImp implements Serializable {

    private static final long serialVersionUID = 729502078755147712L;

    @Id
    @Column( name = "id_mod" )
    private Integer id;

    @Column( name = "status_old", length = 1 )
    private String estadoAnterior;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "venta_old" )
    private BigDecimal ventaAnterior;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "venta_new" )
    private BigDecimal ventaNueva;

    @Column( name = "tipo_old", length = 1 )
    private String tipoAnterior;

    @Column( name = "tipo_new", length = 1 )
    private String tipoNuevo;

    @Column( name = "pc_old" )
    private Integer pcAnterior;

    @Column( name = "pc_new" )
    private Integer pcNuevo;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "monto_old" )
    private BigDecimal montoAnterior;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "monto_new" )
    private BigDecimal montoNuevo;

    @Column( name = "emp_old", length = 13 )
    private String idEmpleadoAnterior;

    @Column( name = "emp_new", length = 13 )
    private String idEmpleadoNuevo;

    @Column( name = "conv_old", length = 5 )
    private String convenioAnterior;

    @Column( name = "conv_new", length = 5 )
    private String convenioNuevo;

    @PostLoad
    private void onPostLoad() {
        idEmpleadoAnterior = StringUtils.trimToEmpty( idEmpleadoAnterior );
        idEmpleadoNuevo = StringUtils.trimToEmpty( idEmpleadoNuevo );
        convenioAnterior = StringUtils.trimToEmpty( convenioAnterior );
        convenioNuevo = StringUtils.trimToEmpty( convenioNuevo );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior( String estadoAnterior ) {
        this.estadoAnterior = estadoAnterior;
    }

    public BigDecimal getVentaAnterior() {
        return ventaAnterior;
    }

    public void setVentaAnterior( BigDecimal ventaAnterior ) {
        this.ventaAnterior = ventaAnterior;
    }

    public BigDecimal getVentaNueva() {
        return ventaNueva;
    }

    public void setVentaNueva( BigDecimal ventaNueva ) {
        this.ventaNueva = ventaNueva;
    }

    public String getTipoAnterior() {
        return tipoAnterior;
    }

    public void setTipoAnterior( String tipoAnterior ) {
        this.tipoAnterior = tipoAnterior;
    }

    public String getTipoNuevo() {
        return tipoNuevo;
    }

    public void setTipoNuevo( String tipoNuevo ) {
        this.tipoNuevo = tipoNuevo;
    }

    public Integer getPcAnterior() {
        return pcAnterior;
    }

    public void setPcAnterior( Integer pcAnterior ) {
        this.pcAnterior = pcAnterior;
    }

    public Integer getPcNuevo() {
        return pcNuevo;
    }

    public void setPcNuevo( Integer pcNuevo ) {
        this.pcNuevo = pcNuevo;
    }

    public BigDecimal getMontoAnterior() {
        return montoAnterior;
    }

    public void setMontoAnterior( BigDecimal montoAnterior ) {
        this.montoAnterior = montoAnterior;
    }

    public BigDecimal getMontoNuevo() {
        return montoNuevo;
    }

    public void setMontoNuevo( BigDecimal montoNuevo ) {
        this.montoNuevo = montoNuevo;
    }

    public String getIdEmpleadoAnterior() {
        return idEmpleadoAnterior;
    }

    public void setIdEmpleadoAnterior( String idEmpleadoAnterior ) {
        this.idEmpleadoAnterior = idEmpleadoAnterior;
    }

    public String getIdEmpleadoNuevo() {
        return idEmpleadoNuevo;
    }

    public void setIdEmpleadoNuevo( String idEmpleadoNuevo ) {
        this.idEmpleadoNuevo = idEmpleadoNuevo;
    }

    public String getConvenioAnterior() {
        return convenioAnterior;
    }

    public void setConvenioAnterior( String convenioAnterior ) {
        this.convenioAnterior = convenioAnterior;
    }

    public String getConvenioNuevo() {
        return convenioNuevo;
    }

    public void setConvenioNuevo( String convenioNuevo ) {
        this.convenioNuevo = convenioNuevo;
    }
}

package mx.lux.pos.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "condicion_ic_gener", schema = "public")
public class CondicionIcGener implements Serializable {

    @Id
    @Column(name = "id_convenio")
    private String id;

    @Column(name = "id_generico", length = 1)
    private String idGenerico;

    @Column(name = "porcentaje_descto")
    private BigDecimal porcentajeDescto;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column(name = "tope_pago")
    private BigDecimal topePago;

    @Column(name = "porcentaje_copago")
    private BigDecimal porcentajeCopago;

    @Column(name = "pago_con_vales")
    private boolean pagoConVales;

    @Column(name = "en_convenio")
    private boolean enConvenio;

    @Column(name = "id_sync", length = 1)
    private String idSync;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_mod")
    private Date fechaMod;

    @Column(name = "id_mod", length = 13)
    private String idMod;

    @Column(name = "id_sucursal")
    private Integer idSucursal;



    public boolean isEnConvenio() {
        return enConvenio;
    }

    public void setEnConvenio(boolean enConvenio) {
        this.enConvenio = enConvenio;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod(Date fechaMod) {
        this.fechaMod = fechaMod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdGenerico() {
        return idGenerico;
    }

    public void setIdGenerico(String idGenerico) {
        this.idGenerico = idGenerico;
    }

    public String getIdMod() {
        return idMod;
    }

    public void setIdMod(String idMod) {
        this.idMod = idMod;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getIdSync() {
        return idSync;
    }

    public void setIdSync(String idSync) {
        this.idSync = idSync;
    }

    public boolean isPagoConVales() {
        return pagoConVales;
    }

    public void setPagoConVales(boolean pagoConVales) {
        this.pagoConVales = pagoConVales;
    }

    public BigDecimal getPorcentajeCopago() {
        return porcentajeCopago;
    }

    public void setPorcentajeCopago(BigDecimal porcentajeCopago) {
        this.porcentajeCopago = porcentajeCopago;
    }

    public BigDecimal getPorcentajeDescto() {
        return porcentajeDescto;
    }

    public void setPorcentajeDescto(BigDecimal porcentajeDescto) {
        this.porcentajeDescto = porcentajeDescto;
    }

    public BigDecimal getTopePago() {
        return topePago;
    }

    public void setTopePago(BigDecimal topePago) {
        this.topePago = topePago;
    }
}

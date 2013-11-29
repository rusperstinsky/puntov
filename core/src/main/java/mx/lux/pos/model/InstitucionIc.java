package mx.lux.pos.model;


import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "institucion_ic", schema = "public")
public class InstitucionIc implements Serializable {


    @Id
    @Column(name = "id_convenio")
    private String id;

    @Column(name = "iniciales_ic", length = 16)
    private String inicialesIc;

    @Column(name = "razon_social")
    private String razonSocial;

    @Column(name = "tipo_convenio", length = 1)
    private String tipoConvenio;

    @Column(name = "id_convenio_princi", length = 5)
    private String idConvenioPrinci;

    @Column(name = "porcentaje_al_tope")
    private BigDecimal porcentaje_al_tope;

    @Column(name = "memo_condiciones")
    private String memoCondiciones;

    @Column(name = "estatus_convenio", length = 1)
    private String estatusConvenio;

    @Column(name = "aceptar_vales")
    private boolean aceptarVales;

    @Column(name = "f_acumular_tope")
    private boolean f_acumular_tope;

    @Column(name = "f_restric_lente")
    private boolean fRestricLente;

    @Column(name = "id_sync", length = 1)
    private String idSync;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_mod")
    private Date fechaMod;

    @Column(name = "id_mod", length = 13)
    private String id_mod;

    @Column(name = "id_sucursal")
    private Integer idSucursal;

    @Column(name = "mejor_precio")
    private boolean mejorPrecio;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column(name = "tope_ant")
    private BigDecimal topeAnt;

    @Column(name = "copia_t")
    private String copiaT;

    @Column(name = "copia_p")
    private String copiaP;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInicialesIc() {
        return inicialesIc;
    }

    public void setInicialesIc(String inicialesIc) {
        this.inicialesIc = inicialesIc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getTipoConvenio() {
        return tipoConvenio;
    }

    public void setTipoConvenio(String tipoConvenio) {
        this.tipoConvenio = tipoConvenio;
    }

    public String getIdConvenioPrinci() {
        return idConvenioPrinci;
    }

    public void setIdConvenioPrinci(String idConvenioPrinci) {
        this.idConvenioPrinci = idConvenioPrinci;
    }

    public BigDecimal getPorcentaje_al_tope() {
        return porcentaje_al_tope;
    }

    public void setPorcentaje_al_tope(BigDecimal porcentaje_al_tope) {
        this.porcentaje_al_tope = porcentaje_al_tope;
    }

    public String getMemoCondiciones() {
        return memoCondiciones;
    }

    public void setMemoCondiciones(String memoCondiciones) {
        this.memoCondiciones = memoCondiciones;
    }

    public String getEstatusConvenio() {
        return estatusConvenio;
    }

    public void setEstatusConvenio(String estatusConvenio) {
        this.estatusConvenio = estatusConvenio;
    }

    public boolean isAceptarVales() {
        return aceptarVales;
    }

    public void setAceptarVales(boolean aceptarVales) {
        this.aceptarVales = aceptarVales;
    }

    public boolean isF_acumular_tope() {
        return f_acumular_tope;
    }

    public void setF_acumular_tope(boolean f_acumular_tope) {
        this.f_acumular_tope = f_acumular_tope;
    }

    public boolean isfRestricLente() {
        return fRestricLente;
    }

    public void setfRestricLente(boolean fRestricLente) {
        this.fRestricLente = fRestricLente;
    }

    public String getIdSync() {
        return idSync;
    }

    public void setIdSync(String idSync) {
        this.idSync = idSync;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod(Date fechaMod) {
        this.fechaMod = fechaMod;
    }

    public String getId_mod() {
        return id_mod;
    }

    public void setId_mod(String id_mod) {
        this.id_mod = id_mod;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    public boolean isMejorPrecio() {
        return mejorPrecio;
    }

    public void setMejorPrecio(boolean mejorPrecio) {
        this.mejorPrecio = mejorPrecio;
    }

    public BigDecimal getTopeAnt() {
        return topeAnt;
    }

    public void setTopeAnt(BigDecimal topeAnt) {
        this.topeAnt = topeAnt;
    }

    public String getCopiaT() {
        return copiaT;
    }

    public void setCopiaT(String copiaT) {
        this.copiaT = copiaT;
    }

    public String getCopiaP() {
        return copiaP;
    }

    public void setCopiaP(String copiaP) {
        this.copiaP = copiaP;
    }
}

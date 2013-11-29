package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table( name = "examen", schema = "public" )
public class Examen implements Serializable {

    private static final long serialVersionUID = -3982558325692712299L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "examen_seq" )
    @SequenceGenerator( name = "examen_seq", sequenceName = "examen_seq" )
    @Column( name = "id_examen" )
    private Integer id;

    @Column( name = "id_cliente", nullable = false )
    private Integer idCliente;

    @Column( name = "id_atendio", nullable = false, length = 13 )
    private String idAtendio;

    @Column( name = "av_sa_od_lejos_ex", length = 3 )
    private String avSaOdLejosEx;

    @Column( name = "av_sa_oi_lejos_ex", length = 3 )
    private String avSaOiLejosEx;

    @Column( name = "obj_od_esf_ex", length = 6 )
    private String objOdEsfEx;

    @Column( name = "obj_od_cil_ex", length = 6 )
    private String objOdCilEx;

    @Column( name = "obj_od_eje_ex", length = 3 )
    private String objOdEjeEx;

    @Column( name = "obj_oi_esf_ex", length = 6 )
    private String objOiEsfEx;

    @Column( name = "obj_oi_cil_ex", length = 6 )
    private String objOiCilEx;

    @Column( name = "obj_oi_eje_ex", length = 3 )
    private String objOiEjeEx;

    @Column( name = "obj_di_ex", length = 2 )
    private String objDiEx;

    @Column( name = "sub_od_esf_ex", length = 6 )
    private String subOdEsfEx;

    @Column( name = "sub_od_cil_ex", length = 6 )
    private String subOdCilEx;

    @Column( name = "sub_od_eje_ex", length = 3 )
    private String subOdEjeEx;

    @Column( name = "sub_od_adc_ex", length = 6 )
    private String subOdAdcEx;

    @Column( name = "sub_od_adi_ex", length = 6 )
    private String subOdAdiEx;

    @Column( name = "sub_od_av_ex", length = 3 )
    private String subOdAvEx;

    @Column( name = "sub_oi_esf_ex", length = 6 )
    private String subOiEsfEx;

    @Column( name = "sub_oi_cil_ex", length = 6 )
    private String subOiCilEx;

    @Column( name = "sub_oi_eje_ex", length = 3 )
    private String subOiEjeEx;

    @Column( name = "sub_oi_adc_ex", length = 6 )
    private String subOiAdcEx;

    @Column( name = "sub_oi_adi_ex", length = 6 )
    private String subOiAdiEx;

    @Column( name = "sub_oi_av_ex", length = 3 )
    private String subOiAvEx;

    @Column( name = "observaciones_ex" )
    private String observacionesEx;

    @Column( name = "id_sync", nullable = false, length = 1 )
    private String idSync;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod", nullable = false )
    private Date fechaMod;

    @Column( name = "id_mod", nullable = false, length = 13 )
    private String id_mod;

    @Column( name = "id_sucursal", nullable = false )
    private Integer idSucursal;

    @Column( name = "di_od", length = 6 )
    private String di_od;

    @Column( name = "di_oi", length = 6 )
    private String diOi;

    @Column( name = "udf1" )
    private String udf1;

    @Column( name = "udf2" )
    private String udf2;

    @Column( name = "udf3" )
    private String udf3;

    @Column( name = "factura" )
    private String factura;

    @Column( name = "tipo_cli" )
    private String tipoCli;

    @Column( name = "tipo_oft" )
    private String tipoOft;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_alta" )
    private Date fechaAlta;

    @Column( name = "id_oftalmologo" )
    private Integer idOftalmologo;

    @Temporal( TemporalType.TIME )
    @Column( name = "hora_alta" )
    private Date horaAlta;

    @Column( name = "id_ex_ori" )
    private String idExOri;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_cliente", insertable = false, updatable = false )
    private Cliente cliente;

    @PrePersist
    private void onPrePersist() {
        fechaAlta = new Date();
        horaAlta = fechaAlta;
        fechaMod = fechaAlta;
    }

    @PreUpdate
    private void onPreUpdate() {
        fechaMod = new Date();
    }

    @PostLoad
    private void onPostLoad() {
        idAtendio = StringUtils.trimToEmpty( idAtendio );
        avSaOdLejosEx = StringUtils.trimToEmpty( avSaOdLejosEx );
        avSaOiLejosEx = StringUtils.trimToEmpty( avSaOiLejosEx );
        objOdEsfEx = StringUtils.trimToEmpty( objOdEsfEx );
        objOdCilEx = StringUtils.trimToEmpty( objOdCilEx );
        objOdEjeEx = StringUtils.trimToEmpty( objOdEjeEx );
        objOiEsfEx = StringUtils.trimToEmpty( objOiEsfEx );
        objOiCilEx = StringUtils.trimToEmpty( objOiCilEx );
        objOiEjeEx = StringUtils.trimToEmpty( objOiEjeEx );
        objDiEx = StringUtils.trimToEmpty( objDiEx );
        subOdEsfEx = StringUtils.trimToEmpty( subOdEsfEx );
        subOdCilEx = StringUtils.trimToEmpty( subOdCilEx );
        subOdEjeEx = StringUtils.trimToEmpty( subOdEjeEx );
        subOdAdcEx = StringUtils.trimToEmpty( subOdAdcEx );
        subOdAdiEx = StringUtils.trimToEmpty( subOdAdiEx );
        subOdAvEx = StringUtils.trimToEmpty( subOdAvEx );
        subOiEsfEx = StringUtils.trimToEmpty( subOiEsfEx );
        subOiCilEx = StringUtils.trimToEmpty( subOiCilEx );
        subOiEjeEx = StringUtils.trimToEmpty( subOiEjeEx );
        subOiAdcEx = StringUtils.trimToEmpty( subOiAdcEx );
        subOiAdiEx = StringUtils.trimToEmpty( subOiAdiEx );
        subOiAvEx = StringUtils.trimToEmpty( subOiAvEx );
        observacionesEx = StringUtils.trimToEmpty( observacionesEx );
        id_mod = StringUtils.trimToEmpty( id_mod );
        di_od = StringUtils.trimToEmpty( di_od );
        diOi = StringUtils.trimToEmpty( diOi );
        udf1 = StringUtils.trimToEmpty( udf1 );
        udf2 = StringUtils.trimToEmpty( udf2 );
        udf3 = StringUtils.trimToEmpty( udf3 );
        factura = StringUtils.trimToEmpty( factura );
        tipoCli = StringUtils.trimToEmpty( tipoCli );
        tipoOft = StringUtils.trimToEmpty( tipoOft );
        idExOri = StringUtils.trimToEmpty( idExOri );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente( Integer idCliente ) {
        this.idCliente = idCliente;
    }

    public String getIdAtendio() {
        return idAtendio;
    }

    public void setIdAtendio( String idAtendio ) {
        this.idAtendio = idAtendio;
    }

    public String getAvSaOdLejosEx() {
        return avSaOdLejosEx;
    }

    public void setAvSaOdLejosEx( String avSaOdLejosEx ) {
        this.avSaOdLejosEx = avSaOdLejosEx;
    }

    public String getAvSaOiLejosEx() {
        return avSaOiLejosEx;
    }

    public void setAvSaOiLejosEx( String avSaOiLejosEx ) {
        this.avSaOiLejosEx = avSaOiLejosEx;
    }

    public String getObjOdEsfEx() {
        return objOdEsfEx;
    }

    public void setObjOdEsfEx( String objOdEsfEx ) {
        this.objOdEsfEx = objOdEsfEx;
    }

    public String getObjOdCilEx() {
        return objOdCilEx;
    }

    public void setObjOdCilEx( String objOdCilEx ) {
        this.objOdCilEx = objOdCilEx;
    }

    public String getObjOdEjeEx() {
        return objOdEjeEx;
    }

    public void setObjOdEjeEx( String objOdEjeEx ) {
        this.objOdEjeEx = objOdEjeEx;
    }

    public String getObjOiEsfEx() {
        return objOiEsfEx;
    }

    public void setObjOiEsfEx( String objOiEsfEx ) {
        this.objOiEsfEx = objOiEsfEx;
    }

    public String getObjOiCilEx() {
        return objOiCilEx;
    }

    public void setObjOiCilEx( String objOiCilEx ) {
        this.objOiCilEx = objOiCilEx;
    }

    public String getObjOiEjeEx() {
        return objOiEjeEx;
    }

    public void setObjOiEjeEx( String objOiEjeEx ) {
        this.objOiEjeEx = objOiEjeEx;
    }

    public String getObjDiEx() {
        return objDiEx;
    }

    public void setObjDiEx( String objDiEx ) {
        this.objDiEx = objDiEx;
    }

    public String getSubOdEsfEx() {
        return subOdEsfEx;
    }

    public void setSubOdEsfEx( String subOdEsfEx ) {
        this.subOdEsfEx = subOdEsfEx;
    }

    public String getSubOdCilEx() {
        return subOdCilEx;
    }

    public void setSubOdCilEx( String subOdCilEx ) {
        this.subOdCilEx = subOdCilEx;
    }

    public String getSubOdEjeEx() {
        return subOdEjeEx;
    }

    public void setSubOdEjeEx( String subOdEjeEx ) {
        this.subOdEjeEx = subOdEjeEx;
    }

    public String getSubOdAdcEx() {
        return subOdAdcEx;
    }

    public void setSubOdAdcEx( String subOdAdcEx ) {
        this.subOdAdcEx = subOdAdcEx;
    }

    public String getSubOdAdiEx() {
        return subOdAdiEx;
    }

    public void setSubOdAdiEx( String subOdAdiEx ) {
        this.subOdAdiEx = subOdAdiEx;
    }

    public String getSubOdAvEx() {
        return subOdAvEx;
    }

    public void setSubOdAvEx( String subOdAvEx ) {
        this.subOdAvEx = subOdAvEx;
    }

    public String getSubOiEsfEx() {
        return subOiEsfEx;
    }

    public void setSubOiEsfEx( String subOiEsfEx ) {
        this.subOiEsfEx = subOiEsfEx;
    }

    public String getSubOiCilEx() {
        return subOiCilEx;
    }

    public void setSubOiCilEx( String subOiCilEx ) {
        this.subOiCilEx = subOiCilEx;
    }

    public String getSubOiEjeEx() {
        return subOiEjeEx;
    }

    public void setSubOiEjeEx( String subOiEjeEx ) {
        this.subOiEjeEx = subOiEjeEx;
    }

    public String getSubOiAdcEx() {
        return subOiAdcEx;
    }

    public void setSubOiAdcEx( String subOiAdcEx ) {
        this.subOiAdcEx = subOiAdcEx;
    }

    public String getSubOiAdiEx() {
        return subOiAdiEx;
    }

    public void setSubOiAdiEx( String subOiAdiEx ) {
        this.subOiAdiEx = subOiAdiEx;
    }

    public String getSubOiAvEx() {
        return subOiAvEx;
    }

    public void setSubOiAvEx( String subOiAvEx ) {
        this.subOiAvEx = subOiAvEx;
    }

    public String getObservacionesEx() {
        return observacionesEx;
    }

    public void setObservacionesEx( String observacionesEx ) {
        this.observacionesEx = observacionesEx;
    }

    public String getIdSync() {
        return idSync;
    }

    public void setIdSync( String idSync ) {
        this.idSync = idSync;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod( Date fechaMod ) {
        this.fechaMod = fechaMod;
    }

    public String getId_mod() {
        return id_mod;
    }

    public void setId_mod( String id_mod ) {
        this.id_mod = id_mod;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal( Integer idSucursal ) {
        this.idSucursal = idSucursal;
    }

    public String getDi_od() {
        return di_od;
    }

    public void setDi_od( String di_od ) {
        this.di_od = di_od;
    }

    public String getDiOi() {
        return diOi;
    }

    public void setDiOi( String diOi ) {
        this.diOi = diOi;
    }

    public String getUdf1() {
        return udf1;
    }

    public void setUdf1( String udf1 ) {
        this.udf1 = udf1;
    }

    public String getUdf2() {
        return udf2;
    }

    public void setUdf2( String udf2 ) {
        this.udf2 = udf2;
    }

    public String getUdf3() {
        return udf3;
    }

    public void setUdf3( String udf3 ) {
        this.udf3 = udf3;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura( String factura ) {
        this.factura = factura;
    }

    public String getTipoCli() {
        return tipoCli;
    }

    public void setTipoCli( String tipoCli ) {
        this.tipoCli = tipoCli;
    }

    public String getTipoOft() {
        return tipoOft;
    }

    public void setTipoOft( String tipoOft ) {
        this.tipoOft = tipoOft;
    }

    public Integer getIdOftalmologo() {
        return idOftalmologo;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta( Date fechaAlta ) {
        this.fechaAlta = fechaAlta;
    }

    public void setIdOftalmologo( Integer idOftalmologo ) {
        this.idOftalmologo = idOftalmologo;
    }

    public Date getHoraAlta() {
        return horaAlta;
    }

    public void setHoraAlta( Date horaAlta ) {
        this.horaAlta = horaAlta;
    }

    public String getIdExOri() {
        return idExOri;
    }

    public void setIdExOri( String idExOri ) {
        this.idExOri = idExOri;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente( Cliente cliente ) {
        this.cliente = cliente;
    }
}

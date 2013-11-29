package mx.lux.pos.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table( name = "jb", schema = "public" )
public class Trabajo implements Serializable {

    private static final long serialVersionUID = 4056431668126392299L;

    @Id
    @Column( name = "rx" )
    private String id;

    @Column( name = "estado", length = 5 )
    private String estado;

    @Column( name = "id_viaje" )
    private String idViaje;

    @Column( name = "caja" )
    private String caja;

    @Column( name = "id_cliente" )
    private String idCliente;

    @Column( name = "roto" )
    private Integer roto;

    @Column( name = "emp_atendio" )
    private String empAtendio;

    @Column( name = "num_llamada" )
    private Integer numLlamada;

    @Column( name = "material" )
    private String material;

    @Column( name = "surte" )
    private String surte;

    @Type( type = "mx.lux.pos.model.MoneyAdapter" )
    @Column( name = "saldo" )
    private BigDecimal saldo;

    @Column( name = "jb_tipo" )
    private String jbTipo;

    @Temporal( TemporalType.DATE )
    @Column( name = "volver_llamar" )
    private Date volverLlamar;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_promesa" )
    private Date fechaPromesa;

    @Column( name = "cliente" )
    private String cliente;

    @Column( name = "id_mod" )
    private String idMod;

    @Column( name = "obs_ext" )
    private String obsExt;

    @Column( name = "ret_auto" )
    private String retAuto;

    @Column( name = "no_llamar" )
    private boolean noLlamar;

    @Column( name = "tipo_venta" )
    private String tipoVenta;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_venta" )
    private Date fechaVenta;

    @Column( name = "id_grupo" )
    private String idGrupo;

    @Column( name = "no_enviar" )
    private boolean noEnviar;

    @Column( name = "externo" )
    private String externo;
    
    
    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "estado", insertable = false, updatable = false )
    private TrabajoEstado trabajoEstado;
    
    @OneToMany( fetch = FetchType.EAGER )
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "rx", insertable = false, updatable = false )
    private List<TrabajoTrack> trabajoTrack;
    
    
    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "emp_atendio", insertable = false, updatable = false )
    private Empleado empleado;
    

    public String getId() {
        return id;
    }

    public String getEstado() {
        return estado;
    }

    public String getIdViaje() {
        return idViaje;
    }

    public String getCaja() {
        return caja;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public Integer getRoto() {
        return roto;
    }

    public String getEmpAtendio() {
        return empAtendio;
    }

    public Integer getNumLlamada() {
        return numLlamada;
    }

    public String getMaterial() {
        return material;
    }

    public String getSurte() {
        return surte;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public String getJbTipo() {
        return jbTipo;
    }

    public Date getVolverLlamar() {
        return volverLlamar;
    }

    public Date getFechaPromesa() {
        return fechaPromesa;
    }

    public String getCliente() {
        return cliente;
    }

    public String getIdMod() {
        return idMod;
    }

    public String getObsExt() {
        return obsExt;
    }

    public String getRetAuto() {
        return retAuto;
    }

    public boolean isNoLlamar() {
        return noLlamar;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public boolean isNoEnviar() {
        return noEnviar;
    }

    public String getExterno() {
        return externo;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public void setEstado( String estado ) {
        this.estado = estado;
    }

    public void setIdViaje( String idViaje ) {
        this.idViaje = idViaje;
    }

    public void setCaja( String caja ) {
        this.caja = caja;
    }

    public void setIdCliente( String idCliente ) {
        this.idCliente = idCliente;
    }

    public void setRoto( Integer roto ) {
        this.roto = roto;
    }

    public void setEmpAtendio( String empAtendio ) {
        this.empAtendio = empAtendio;
    }

    public void setNumLlamada( Integer numLlamada ) {
        this.numLlamada = numLlamada;
    }

    public void setMaterial( String material ) {
        this.material = material;
    }

    public void setSurte( String surte ) {
        this.surte = surte;
    }

    public void setSaldo( BigDecimal saldo ) {
        this.saldo = saldo;
    }

    public void setJbTipo( String jbTipo ) {
        this.jbTipo = jbTipo;
    }

    public void setVolverLlamar( Date volverLlamar ) {
        this.volverLlamar = volverLlamar;
    }

    public void setFechaPromesa( Date fechaPromesa ) {
        this.fechaPromesa = fechaPromesa;
    }

    public void setCliente( String cliente ) {
        this.cliente = cliente;
    }

    public void setIdMod( String idMod ) {
        this.idMod = idMod;
    }

    public void setObsExt( String obsExt ) {
        this.obsExt = obsExt;
    }

    public void setRetAuto( String retAuto ) {
        this.retAuto = retAuto;
    }

    public void setNoLlamar( boolean noLlamar ) {
        this.noLlamar = noLlamar;
    }

    public void setTipoVenta( String tipoVenta ) {
        this.tipoVenta = tipoVenta;
    }

    public void setFechaVenta( Date fechaVenta ) {
        this.fechaVenta = fechaVenta;
    }

    public void setIdGrupo( String idGrupo ) {
        this.idGrupo = idGrupo;
    }

    public void setNoEnviar( boolean noEnviar ) {
        this.noEnviar = noEnviar;
    }

    public void setExterno( String externo ) {
        this.externo = externo;
    }

    public TrabajoEstado getTrabajoEstado() {
        return trabajoEstado;
    }

    public void setTrabajoEstado( TrabajoEstado trabajoEstado ) {
        this.trabajoEstado = trabajoEstado;
    }

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public List<TrabajoTrack> getTrabajoTrack() {
		return trabajoTrack;
	}

	public void setTrabajoTrack(List<TrabajoTrack> trabajoTrack) {
		this.trabajoTrack = trabajoTrack;
	}
    
}

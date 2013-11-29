package mx.lux.pos.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table( name = "jb_track", schema = "public" )
public class TrabajoTrack implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	@Id
    @Column( name = "rx" )
    private String id;
	
	@Column( name = "estado", length = 5 )
    private String estado;
	
	@Column( name = "obs", length = 5 )
    private String obs;
	
	@Column( name = "emp", length = 5 )
    private String emp;
	
	@Column( name = "id_viaje", length = 3 )
    private String id_viaje;
	
	 @Temporal( TemporalType.TIMESTAMP )
	 @Column( name = "fecha" )
	 private Date fecha;
	 
	 @Column( name = "id_mod", length = 13 )
	 private String id_mod;
	 
	 @ManyToOne
	 @NotFound( action = NotFoundAction.IGNORE )
	 @JoinColumn( name = "rx", insertable = false, updatable = false )
	 private Trabajo trabajo;

	 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getEmp() {
		return emp;
	}

	public void setEmp(String emp) {
		this.emp = emp;
	}

	public String getId_viaje() {
		return id_viaje;
	}

	public void setId_viaje(String id_viaje) {
		this.id_viaje = id_viaje;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getId_mod() {
		return id_mod;
	}

	public void setId_mod(String id_mod) {
		this.id_mod = id_mod;
	}

	public Trabajo getTrabajo() {
		return trabajo;
	}

	public void setTrabajo(Trabajo trabajo) {
		this.trabajo = trabajo;
	}
	 
}

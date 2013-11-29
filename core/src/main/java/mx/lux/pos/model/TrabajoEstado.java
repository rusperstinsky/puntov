package mx.lux.pos.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table( name = "jb_edos", schema = "public" )
public class TrabajoEstado implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
    @Column( name = "id_edo" )
    private String id;

	@Column( name = "llamada" )
    private String llamada;
	
	@Column( name = "descr" )
    private String descr;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLlamada() {
		return llamada;
	}

	public void setLlamada(String llamada) {
		this.llamada = llamada;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}
	
}

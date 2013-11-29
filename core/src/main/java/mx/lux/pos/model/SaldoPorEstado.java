package mx.lux.pos.model;

import java.util.ArrayList;
import java.util.List;

public class SaldoPorEstado {
	
	private String estado;
	private String idEmpleado;
	private String nomEmpleado;
	private Integer totalTrabajos;
	private List<PropiedadesporEstado> lstPropiedades;
	
	
	public SaldoPorEstado( String estados ){
		estado = estados;
		idEmpleado = estados;
		lstPropiedades = new ArrayList<PropiedadesporEstado>();
		totalTrabajos = 0;
	}
	
	public void AcumulaSaldos( Trabajo trabajo ){
		String id = trabajo.getId();
		totalTrabajos = totalTrabajos+1;
		PropiedadesporEstado propiedad = FindOrCreate(lstPropiedades, id);
		propiedad.AcumulaPropiedades(trabajo);
	}
	
	public void AcumulaTrabajos( TrabajoTrack trabajo, Integer total){
		String id = trabajo.getId();
		PropiedadesporEstado propiedad = FindOrCreate(lstPropiedades, id);
		propiedad.AcumulaTrabajos( trabajo );
		totalTrabajos = total;
		
	}
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<PropiedadesporEstado> getLstPropiedades() {
		return lstPropiedades;
	}

	public void setLstPropiedades(List<PropiedadesporEstado> lstPropiedades) {
		this.lstPropiedades = lstPropiedades;
	}
	
	public Integer getTotalTrabajos() {
		return totalTrabajos;
	}

	public void setTotalTrabajos(Integer totalTrabajos) {
		this.totalTrabajos = totalTrabajos;
	}

	protected PropiedadesporEstado FindOrCreate( List<PropiedadesporEstado> lstPropiedades, String id ) {
		PropiedadesporEstado found = null;

        for ( PropiedadesporEstado propiedades : lstPropiedades ) {
            if ( propiedades.getId().equals( id ) ) {
                found = propiedades;
                break;
            }
        }
        if ( found == null ) {
            found = new PropiedadesporEstado( id );
            lstPropiedades.add( found );
        }
        return found;
    }

	public String getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(String idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public String getNomEmpleado() {
		return nomEmpleado;
	}

	public void setNomEmpleado(String nomEmpleado) {
		this.nomEmpleado = nomEmpleado;
	}
	
}

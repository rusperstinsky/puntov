package mx.lux.pos.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table( name = "mod_emp", schema = "public" )
public class ModificacionEmp implements Serializable {

    private static final long serialVersionUID = -2938104383552091092L;

    @Id
    @Column( name = "id_mod" )
    private Integer id;

    @Column( name = "emp_old", length = 13 )
    private String empleadoAnterior;

    @Column( name = "emp_new", length = 13 )
    private String empleadoActual;

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getEmpleadoAnterior() {
        return empleadoAnterior;
    }

    public void setEmpleadoAnterior(String empleadoAnterior) {
        this.empleadoAnterior = empleadoAnterior;
    }

    public String getEmpleadoActual() {
        return empleadoActual;
    }

    public void setEmpleadoActual(String empleadoActual) {
        this.empleadoActual = empleadoActual;
    }
}

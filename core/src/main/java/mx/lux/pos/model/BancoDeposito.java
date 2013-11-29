package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable
@Table( name = "banco_dep", schema = "public" )
public class BancoDeposito implements Serializable {

    private static final long serialVersionUID = 7799791499859296700L;

    @Id
    @Column( name = "id_banco_dep" )
    private Integer id;

    @Column( name = "nombre", length = 50 )
    private String nombre;

    @Column( name = "cuenta", length = 50 )
    private String cuenta;

    @Column( name = "tipo", length = 5 )
    private String tipo;

    @PostLoad
    private void onPostLoad() {
        nombre = StringUtils.trimToEmpty( nombre );
        cuenta = StringUtils.trimToEmpty( cuenta );
        tipo = StringUtils.trimToEmpty( tipo );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre( String nombre ) {
        this.nombre = nombre;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta( String cuenta ) {
        this.cuenta = cuenta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo( String tipo ) {
        this.tipo = tipo;
    }
}

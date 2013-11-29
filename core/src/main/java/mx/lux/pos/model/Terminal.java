package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable
@Table( name = "pos", schema = "public" )
public class Terminal implements Serializable {

    private static final long serialVersionUID = 4653830403205321711L;

    @Id
    @Column( name = "id_terminal", length = 50 )
    private String id;

    @Column( name = "id_suc" )
    private Integer idSucursal;

    @Column( name = "descripcion" )
    private String descripcion;

    @Column( name = "afiliacion", length = 50 )
    private String afiliacion;

    @Column( name = "promocion" )
    private boolean promocion;

    @Column( name = "numero" )
    private String numero;

    @Column( name = "id_banco_dep" )
    private Integer idBancoDep;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_suc", insertable = false, updatable = false )
    private Sucursal sucursal;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_banco_dep", insertable = false, updatable = false )
    private BancoDeposito bancoDeposito;

    @PostLoad
    private void trim() {
        descripcion = StringUtils.trimToEmpty( descripcion );
        afiliacion = StringUtils.trimToEmpty( afiliacion );
        numero = StringUtils.trimToEmpty( numero );
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal( Integer idSucursal ) {
        this.idSucursal = idSucursal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion( String descripcion ) {
        this.descripcion = descripcion;
    }

    public String getAfiliacion() {
        return afiliacion;
    }

    public void setAfiliacion( String afiliacion ) {
        this.afiliacion = afiliacion;
    }

    public boolean getPromocion() {
        return promocion;
    }

    public void setPromocion( boolean promocion ) {
        this.promocion = promocion;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero( String numero ) {
        this.numero = numero;
    }

    public Integer getIdBancoDep() {
        return idBancoDep;
    }

    public void setIdBancoDep( Integer idBancoDep ) {
        this.idBancoDep = idBancoDep;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal( Sucursal sucursal ) {
        this.sucursal = sucursal;
    }

    public BancoDeposito getBancoDeposito() {
        return bancoDeposito;
    }

    public void setBancoDeposito( BancoDeposito bancoDeposito ) {
        this.bancoDeposito = bancoDeposito;
    }
}

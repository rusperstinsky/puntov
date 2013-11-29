package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "empleado", schema = "public" )
public class Empleado implements Serializable {

    private static final long serialVersionUID = -6196159190484292142L;
    private static final String DEFAULT_ID_SYNC = "1";
    private static final String DEFAULT_ID_MOD = "0";


    @Id
    @Column( name = "id_empleado", length = 13 )
    private String id;

    @Column( name = "nombre_empleado" )
    private String nombre;

    @Column( name = "ap_pat_empleado" )
    private String apellidoPaterno;

    @Column( name = "ap_mat_empleado" )
    private String apellidoMaterno;

    @Column( name = "id_puesto" )
    private Integer idPuesto;

    @Column( name = "passwd", length = 10 )
    private String passwd;

    @Column( name = "id_sync", length = 1 )
    private String idSync = "1";

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod" )
    private Date fechaModificado = new Date();

    @Column( name = "id_mod", length = 13 )
    private String idModificado = "0";

    @Column( name = "id_sucursal", nullable = false )
    private Integer idSucursal;

    @Column( name = "id_empresa" )
    private Integer idEmpresa;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_sucursal", insertable = false, updatable = false )
    private Sucursal sucursal;

    @PostLoad
    private void trim() {
        nombre = StringUtils.trimToEmpty( nombre );
        apellidoPaterno = StringUtils.trimToEmpty( apellidoPaterno );
        apellidoMaterno = StringUtils.trimToEmpty( apellidoMaterno );
        passwd = StringUtils.trimToEmpty( passwd );
        idSync = StringUtils.trimToEmpty( idSync );
        idModificado = StringUtils.trimToEmpty( idModificado );
    }

    @PrePersist
    protected void prePersist() {
        this.preUpdate();
    }

    @PreUpdate
    protected void preUpdate() {
        if ( StringUtils.isBlank( this.getIdSync() ) ) {
            this.setIdSync( DEFAULT_ID_SYNC );
        }
        if ( StringUtils.isBlank( this.getIdModificado() ) ) {
            this.setIdModificado( DEFAULT_ID_MOD );
        }
        this.setFechaModificado( new Date() );
    }


    public String getNombreCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append( StringUtils.trimToEmpty( nombre ) );
        sb.append( StringUtils.isNotBlank( nombre ) ? " " : "" );
        sb.append( StringUtils.trimToEmpty( apellidoPaterno ) );
        sb.append( StringUtils.isNotBlank( apellidoPaterno ) ? " " : "" );
        sb.append( StringUtils.trimToEmpty( apellidoMaterno ) );
        return sb.toString().trim();
    }

    public String nombreCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append( StringUtils.trimToEmpty( nombre ) );
        sb.append( StringUtils.isNotBlank( nombre ) ? " " : "" );
        sb.append( StringUtils.trimToEmpty( apellidoPaterno ) );
        sb.append( StringUtils.isNotBlank( apellidoPaterno ) ? " " : "" );
        sb.append( StringUtils.trimToEmpty( apellidoMaterno ) );
        return sb.toString().trim();
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre( String nombre ) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno( String apellidoPaterno ) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno( String apellidoMaterno ) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public Integer getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto( Integer idPuesto ) {
        this.idPuesto = idPuesto;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd( String passwd ) {
        this.passwd = passwd;
    }

    public String getIdSync() {
        return idSync;
    }

    public void setIdSync( String idSync ) {
        this.idSync = idSync;
    }

    public Date getFechaModificado() {
        return fechaModificado;
    }

    public void setFechaModificado( Date fechaModificado ) {
        this.fechaModificado = fechaModificado;
    }

    public String getIdModificado() {
        return idModificado;
    }

    public void setIdModificado( String idModificado ) {
        this.idModificado = idModificado;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal( Integer idSucursal ) {
        this.idSucursal = idSucursal;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa( Integer idEmpresa ) {
        this.idEmpresa = idEmpresa;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal( Sucursal sucursal ) {
        this.sucursal = sucursal;
    }

    /*public String getNombreCompleto() {
         if( !nombre.equals(null) && !apellidoPaterno.equals(null) && !apellidoMaterno.equals(null) ){
             StringBuilder nombreComp = new StringBuilder();
             nombreComp.append( StringUtils.trimToEmpty( nombre ) );
             nombreComp.append( StringUtils.isNotBlank( nombre ) ? " " : "" );
             nombreComp.append( StringUtils.trimToEmpty( apellidoPaterno ) );
             nombreComp.append( StringUtils.isNotBlank( apellidoPaterno ) ? " " : "" );
             nombreComp.append( StringUtils.trimToEmpty( apellidoMaterno ) );

             nombreCompleto = nombreComp.toString();
         }
         return nombreCompleto;
     }

     public void setNombreCompleto(String nombreCompleto) {
         this.nombreCompleto = nombreCompleto;
     }*/

}

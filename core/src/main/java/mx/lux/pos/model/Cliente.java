package mx.lux.pos.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
@Table( name = "clientes", schema = "public" )
public class Cliente implements Serializable {

    private static final long serialVersionUID = -6159088183332146835L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO, generator = "clientes_id_cliente_seq" )
    @SequenceGenerator( name = "clientes_id_cliente_seq", sequenceName = "clientes_id_cliente_seq" )
    @Column( name = "id_cliente" )
    private Integer id;

    @Size( max = 5 )
    @Column( name = "id_convenio", length = 5 )
    private String idConvenio;

    @Size( max = 10 )
    @Column( name = "titulo", length = 10 )
    private String titulo;

    @Column( name = "id_oftalmologo" )
    private Integer idOftalmologo;

    @Column( name = "tipo_oft" )
    private String tipoOftalmologo;

    @Size( max = 3 )
    @Column( name = "id_localidad", length = 3 )
    private String idLocalidad;

    @Size( max = 2 )
    @Column( name = "id_estado", length = 2 )
    private String idEstado;

    @NotNull
    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_alta_cli", nullable = false )
    private Date fechaAlta;

    @NotNull
    @Column( name = "tipo_cli", length = 1, nullable = false )
    private String tipo = "N";

    @Column( name = "sexo_cli" )
    private boolean sexo = true;

    @Column( name = "apellido_pat_cli" )
    private String apellidoPaterno;

    @Column( name = "apellido_mat_cli" )
    private String apellidoMaterno;

    @Column( name = "f_casada_cli" )
    private boolean fCasada;

    @NotBlank
    @Column( name = "nombre_cli" )
    private String nombre;

    @Size( max = 13 )
    @Column( name = "rfc_cli", length = 13 )
    private String rfc;

    @Column( name = "direccion_cli" )
    private String direccion;

    @Column( name = "colonia_cli" )
    private String colonia;

    @Size( max = 5 )
    @Column( name = "codigo", length = 5 )
    private String codigo;

    @Size( max = 15 )
    @Column( name = "tel_casa_cli", length = 15 )
    private String telefonoCasa;

    @Size( max = 15 )
    @Column( name = "tel_trab_cli", length = 15 )
    private String telefonoTrabajo;

    @Size( max = 5 )
    @Column( name = "ext_trab_cli", length = 5 )
    private String extTrabajo;

    @Size( max = 15 )
    @Column( name = "tel_adi_cli", length = 15 )
    private String telefonoAdicional;

    @Size( max = 5 )
    @Column( name = "ext_adi_cli", length = 5 )
    private String extAdicional;

    @Email
    @Column( name = "email_cli" )
    private String email;

    @NotNull
    @Size( max = 1 )
    @Column( name = "s_usa_anteojos", length = 1, nullable = false )
    private String sUsaAnteojos = "N";

    @Column( name = "avisar" )
    private boolean avisar;

    @Size( max = 12 )
    @Column( name = "id_atendio", length = 12 )
    private String idAtendio;

    @NotNull
    @Column( name = "id_sync", length = 1, nullable = false )
    private String idSync = "1";

    @NotNull
    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_mod", nullable = false )
    private Date fechaModificado;

    @NotNull
    @Size( max = 13 )
    @Column( name = "id_mod", length = 13, nullable = false )
    private String idModificado = "0";

    @NotNull
    @Column( name = "id_sucursal", nullable = false )
    private Integer idSucursal;

    @Column( name = "udf1" )
    private String udf1;

    @Column( name = "udf2" )
    private String udf2;

    @Column( name = "cli_ori" )
    private String ori;

    @Column( name = "udf4" )
    private String udf4;

    @Column( name = "udf5" )
    private String udf5;

    @Column( name = "udf6" )
    private String udf6;

    @Column( name = "receta" )
    private Integer receta;

    @Column( name = "obs" )
    private String obs;

    @Past
    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_nac" )
    private Date fechaNacimiento;

    @Size( max = 20 )
    @Column( name = "cuc", length = 20 )
    private String cuc;

    @Temporal( TemporalType.TIME )
    @Column( name = "hora_alta" )
    private Date horaAlta;

    @Column( name = "finado" )
    private boolean finado;

    @Temporal( TemporalType.DATE )
    @Column( name = "fecha_imp" )
    private Date fechaImp;

    @NotNull
    @Column( name = "mod_imp", nullable = false )
    private boolean modImp;

    @NotNull
    @Column( name = "calif", nullable = false )
    private Integer calif = 0;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "fecha_imp_update" )
    private Date fechaImpUpdate;

    @Column( name = "tipo_imp" )
    private String tipoImp;

    @Column( name = "hist_cuc" )
    private String histCuc;

    @Column( name = "hist_cli" )
    private String histCli;

    @ManyToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumns( {
            @JoinColumn( name = "id_estado", referencedColumnName = "id_estado", insertable = false, updatable = false ),
            @JoinColumn( name = "id_localidad", referencedColumnName = "id_localidad", insertable = false, updatable = false ) } )
    private Municipio municipio;

    @OneToOne
    @NotFound( action = NotFoundAction.IGNORE )
    @JoinColumn( name = "id_cliente", insertable = false, updatable = false )
    private ClientePais clientePais;

    @PrePersist
    private void onPrePersist() {
        fechaAlta = new Date();
        horaAlta = fechaAlta;
        fechaModificado = fechaAlta;
        municipio = null;
        clientePais = null;
    }

    @PreUpdate
    private void onPreUpdate() {
        if( fechaAlta == null && horaAlta == null ){
            fechaAlta = new Date();
            horaAlta = fechaAlta;
        }
        fechaModificado = new Date();
    }

    @PostLoad
    private void onPostLoad() {
        idConvenio = StringUtils.trimToEmpty( idConvenio );
        titulo = StringUtils.trimToEmpty( titulo );
        tipoOftalmologo = StringUtils.trimToEmpty( tipoOftalmologo );
        idLocalidad = StringUtils.trimToEmpty( idLocalidad );
        idEstado = StringUtils.trimToEmpty( idEstado );
        apellidoPaterno = StringUtils.trimToEmpty( apellidoPaterno );
        apellidoMaterno = StringUtils.trimToEmpty( apellidoMaterno );
        nombre = StringUtils.trimToEmpty( nombre );
        rfc = StringUtils.trimToEmpty( rfc );
        direccion = StringUtils.trimToEmpty( direccion );
        colonia = StringUtils.trimToEmpty( colonia );
        codigo = StringUtils.trimToEmpty( codigo );
        telefonoCasa = StringUtils.trimToEmpty( telefonoCasa );
        telefonoTrabajo = StringUtils.trimToEmpty( telefonoTrabajo );
        extTrabajo = StringUtils.trimToEmpty( extTrabajo );
        telefonoAdicional = StringUtils.trimToEmpty( telefonoAdicional );
        extAdicional = StringUtils.trimToEmpty( extAdicional );
        email = StringUtils.trimToEmpty( email );
        idAtendio = StringUtils.trimToEmpty( idAtendio );
        idModificado = StringUtils.trimToEmpty( idModificado );
        udf1 = StringUtils.trimToEmpty( udf1 );
        udf2 = StringUtils.trimToEmpty( udf2 );
        ori = StringUtils.trimToEmpty( ori );
        udf4 = StringUtils.trimToEmpty( udf4 );
        udf5 = StringUtils.trimToEmpty( udf5 );
        udf6 = StringUtils.trimToEmpty( udf6 );
        obs = StringUtils.trimToEmpty( obs );
        cuc = StringUtils.trimToEmpty( cuc );
        tipoImp = StringUtils.trimToEmpty( tipoImp );
        histCuc = StringUtils.trimToEmpty( histCuc );
        histCli = StringUtils.trimToEmpty( histCli );
    }

    public String nombreCompleto( boolean conTitulo ) {
        StringBuilder sb = new StringBuilder();
        if ( conTitulo ) {
            sb.append( StringUtils.trimToEmpty( titulo ) );
            sb.append( StringUtils.isNotBlank( titulo ) ? " " : "" );
        }
        sb.append( StringUtils.trimToEmpty( nombre ) );
        sb.append( StringUtils.isNotBlank( nombre ) ? " " : "" );
        sb.append( StringUtils.trimToEmpty( apellidoPaterno ) );
        sb.append( StringUtils.isNotBlank( apellidoPaterno ) ? " " : "" );
        sb.append( StringUtils.trimToEmpty( apellidoMaterno ) );
        return sb.toString().trim();
    }

    public String getNombreCompleto() {
        return nombreCompleto( false );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getIdConvenio() {
        return idConvenio;
    }

    public void setIdConvenio( String idConvenio ) {
        this.idConvenio = idConvenio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo( String titulo ) {
        this.titulo = titulo;
    }

    public Integer getIdOftalmologo() {
        return idOftalmologo;
    }

    public void setIdOftalmologo( Integer idOftalmologo ) {
        this.idOftalmologo = idOftalmologo;
    }

    public String getTipoOftalmologo() {
        return tipoOftalmologo;
    }

    public void setTipoOftalmologo( String tipoOftalmologo ) {
        this.tipoOftalmologo = tipoOftalmologo;
    }

    public String getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad( String idLocalidad ) {
        this.idLocalidad = idLocalidad;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado( String idEstado ) {
        this.idEstado = idEstado;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta( Date fechaAlta ) {
        this.fechaAlta = fechaAlta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo( String tipo ) {
        this.tipo = tipo;
    }

    public boolean getSexo() {
        return sexo;
    }

    public void setSexo( boolean sexo ) {
        this.sexo = sexo;
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

    public boolean getfCasada() {
        return fCasada;
    }

    public void setfCasada( boolean fCasada ) {
        this.fCasada = fCasada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre( String nombre ) {
        this.nombre = nombre;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc( String rfc ) {
        this.rfc = rfc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion( String direccion ) {
        this.direccion = direccion;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia( String colonia ) {
        this.colonia = colonia;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo( String codigo ) {
        this.codigo = codigo;
    }

    public String getTelefonoCasa() {
        return telefonoCasa;
    }

    public void setTelefonoCasa( String telefonoCasa ) {
        this.telefonoCasa = telefonoCasa;
    }

    public String getTelefonoTrabajo() {
        return telefonoTrabajo;
    }

    public void setTelefonoTrabajo( String telefonoTrabajo ) {
        this.telefonoTrabajo = telefonoTrabajo;
    }

    public String getExtTrabajo() {
        return extTrabajo;
    }

    public void setExtTrabajo( String extTrabajo ) {
        this.extTrabajo = extTrabajo;
    }

    public String getTelefonoAdicional() {
        return telefonoAdicional;
    }

    public void setTelefonoAdicional( String telefonoAdicional ) {
        this.telefonoAdicional = telefonoAdicional;
    }

    public String getExtAdicional() {
        return extAdicional;
    }

    public void setExtAdicional( String extAdicional ) {
        this.extAdicional = extAdicional;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getsUsaAnteojos() {
        return sUsaAnteojos;
    }

    public void setsUsaAnteojos( String sUsaAnteojos ) {
        this.sUsaAnteojos = sUsaAnteojos;
    }

    public boolean getAvisar() {
        return avisar;
    }

    public void setAvisar( boolean avisar ) {
        this.avisar = avisar;
    }

    public String getIdAtendio() {
        return idAtendio;
    }

    public void setIdAtendio( String idAtendio ) {
        this.idAtendio = idAtendio;
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

    public String getOri() {
        return ori;
    }

    public void setOri( String ori ) {
        this.ori = ori;
    }

    public String getUdf4() {
        return udf4;
    }

    public void setUdf4( String udf4 ) {
        this.udf4 = udf4;
    }

    public String getUdf5() {
        return udf5;
    }

    public void setUdf5( String udf5 ) {
        this.udf5 = udf5;
    }

    public String getUdf6() {
        return udf6;
    }

    public void setUdf6( String udf6 ) {
        this.udf6 = udf6;
    }

    public Integer getReceta() {
        return receta;
    }

    public void setReceta( Integer receta ) {
        this.receta = receta;
    }

    public String getObs() {
        return obs;
    }

    public void setObs( String obs ) {
        this.obs = obs;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento( Date fechaNacimiento ) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCuc() {
        return cuc;
    }

    public void setCuc( String cuc ) {
        this.cuc = cuc;
    }

    public Date getHoraAlta() {
        return horaAlta;
    }

    public void setHoraAlta( Date horaAlta ) {
        this.horaAlta = horaAlta;
    }

    public boolean isFinado() {
        return finado;
    }

    public void setFinado( boolean finado ) {
        this.finado = finado;
    }

    public Date getFechaImp() {
        return fechaImp;
    }

    public void setFechaImp( Date fechaImp ) {
        this.fechaImp = fechaImp;
    }

    public boolean isModImp() {
        return modImp;
    }

    public void setModImp( boolean modImp ) {
        this.modImp = modImp;
    }

    public Integer getCalif() {
        return calif;
    }

    public void setCalif( Integer calif ) {
        this.calif = calif;
    }

    public Date getFechaImpUpdate() {
        return fechaImpUpdate;
    }

    public void setFechaImpUpdate( Date fechaImpUpdate ) {
        this.fechaImpUpdate = fechaImpUpdate;
    }

    public String getTipoImp() {
        return tipoImp;
    }

    public void setTipoImp( String tipoImp ) {
        this.tipoImp = tipoImp;
    }

    public String getHistCuc() {
        return histCuc;
    }

    public void setHistCuc( String histCuc ) {
        this.histCuc = histCuc;
    }

    public String getHistCli() {
        return histCli;
    }

    public void setHistCli( String histCli ) {
        this.histCli = histCli;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio( Municipio municipio ) {
        this.municipio = municipio;
    }

    public ClientePais getClientePais() {
        return clientePais;
    }

    public void setClientePais( ClientePais clientePais ) {
        this.clientePais = clientePais;
    }

    public boolean equals( Object obj ) {
        boolean result = false;
        if (obj instanceof Cliente) {
            result = this.getId().equals(((Cliente) obj).getId());
        } else if (obj instanceof Integer) {
            result = this.getId().equals((Integer) obj);
        }
        return result;
    }
}

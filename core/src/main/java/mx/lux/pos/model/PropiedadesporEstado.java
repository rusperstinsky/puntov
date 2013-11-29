package mx.lux.pos.model;

import java.math.BigDecimal;
import java.util.Date;

public class PropiedadesporEstado {
	
	private String id;
	private String factura;
	private Date fecha;
	private String tipo;
	private Integer contactos;
	private BigDecimal saldo;
	private String material;
	
	public PropiedadesporEstado( String pId ){
		id = pId;
		contactos = 0;
		saldo = BigDecimal.ZERO;
	}

	public void AcumulaPropiedades( Trabajo trabajo ){
		fecha = trabajo.getFechaVenta();
		tipo = trabajo.getJbTipo();
		contactos = trabajo.getNumLlamada();
		saldo = trabajo.getSaldo();
		material = trabajo.getMaterial();
	}
	
	public void AcumulaTrabajos( TrabajoTrack trabajo ){
		fecha = trabajo.getFecha();
		factura = trabajo.getId();
		material = trabajo.getTrabajo().getMaterial();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getContactos() {
		return contactos;
	}

	public void setContactos(Integer contactos) {
		this.contactos = contactos;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getFactura() {
		return factura;
	}

	public void setFactura(String factura) {
		this.factura = factura;
	}
	
}

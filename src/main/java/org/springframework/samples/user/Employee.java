package org.springframework.samples.petclinic.employee;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Entity
public class Employee extends BaseEntity {

	@Column(length = 50)
	@NotBlank
	private String username;

	@Column(name = "first_name", length = 30)
	@NotBlank
	private String firstName;

	@Column(name = "last_name", length = 30)
	@NotBlank
	private String lastName;

	@Column(length = 30)
	@NotBlank
	private String cargo;

	@Column(name = "salario_base")
	@NotBlank
	private int salarioBase;

	@Column(name = "tipo_contrato", unique = true, length = 100)
	@NotBlank
	private String tipoContrato;

	@Column(name = "lista_de_proyectos", length = 255)
	@NotBlank
	private String listaDeProyectos;

	// Getters and setters

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public int getSalarioBase() {
		return salarioBase;
	}

	public void setSalarioBase(int salarioBase) {
		this.salarioBase = salarioBase;
	}

	public String getTipoContrato() {
		return tipoContrato;
	}

	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	public String getListaDeProyectos() {
		return listaDeProyectos;
	}

	public void setListaDeProyectos(String listaDeProyectos) {
		this.listaDeProyectos = listaDeProyectos;
	}

}
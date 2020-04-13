package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "donations")
public class Donation extends BaseEntity{
	
	//Attributes --------------------------------------------------
	
	@Column(name = "donation_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate date;
	
	@NotNull
	@Column(name = "amount")
	private Integer amount;
	
	@NotBlank
	@Column(name = "client")
	private String client;
	
	@ManyToOne
	@JoinColumn(name = "cause_id")
	private Cause cause;
	
	//Getters -----------------------------------------------------
	
	public LocalDate getDate() {
		return this.date;
	}
	
	public Integer getAmount() {
		return this.amount;
	}
	
	public String getClient() {
		return this.client;
	}
	
	public Cause getCause() {
		return this.cause;
	}
	
	//Setters -----------------------------------------------------
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	public void setClient(String client) {
		this.client = client;
	}
	
	public void setCause(Cause cause) {
		this.cause = cause;
	}
	
}

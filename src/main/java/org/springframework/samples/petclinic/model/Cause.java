package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "causes")
public class Cause extends NamedEntity {
	
	@Column(name = "end_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate endDate;
	
	@NotEmpty
	@Column(name = "description")
	private String description;
	
	@NotNull
	@Column(name = "objetive")
	private Integer objetive;
	
	
	
	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getObjetive() {
		return objetive;
	}

	public void setObjetive(Integer objetive) {
		this.objetive = objetive;
	}

}

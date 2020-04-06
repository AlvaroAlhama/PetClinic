package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
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
	
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "cause", fetch = FetchType.EAGER)
	private Set<Donation> donations;
	
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
	
	protected Set<Donation> getDonationsInternal(){
		if(this.donations == null) {
			this.donations = new HashSet<>();
		}
		return this.donations;
	}
	
	protected void setDonationsInternal(Set<Donation> donations) {
		this.donations = donations;
	}
	
	public List<Donation> getDonations() {
		List<Donation> sortedDonations = new ArrayList<>(getDonationsInternal());
		PropertyComparator.sort(sortedDonations, new MutableSortDefinition("date", false, false));
		return Collections.unmodifiableList(sortedDonations);
	}
	
	public void addDonation(Donation donation) {
		getDonationsInternal().add(donation);
		donation.setCause(this);
	}

}

package org.springframework.samples.petclinic.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.samples.petclinic.model.Residence;

public interface ResidenceRepository {
	
	void save(Residence residence);

	List<Residence> findByPetId(Integer petId);
	
	void delete(Residence residence);
	
	Residence findById(int id);
	
	Integer count(LocalDate register, LocalDate release, int petId);

}

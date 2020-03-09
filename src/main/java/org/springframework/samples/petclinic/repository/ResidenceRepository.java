package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Residence;

public interface ResidenceRepository {
	
	void save(Residence residence) throws DataAccessException;

	List<Residence> findByPetId(Integer petId);

}

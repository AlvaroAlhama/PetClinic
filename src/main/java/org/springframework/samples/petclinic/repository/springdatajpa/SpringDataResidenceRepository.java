package org.springframework.samples.petclinic.repository.springdatajpa;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Residence;
import org.springframework.samples.petclinic.repository.ResidenceRepository;

public interface SpringDataResidenceRepository extends ResidenceRepository, Repository<Residence, Integer>{

	
	@Query("SELECT count(r) FROM Residence r WHERE ((r.registerDate < :register AND r.releaseDate > :register) OR "
			+ "(r.registerDate < :release AND r.releaseDate > :release) OR "
			+ "(r.registerDate > :register AND r.releaseDate < :release) OR "
			+ "(r.registerDate < :register AND r.releaseDate > :release) OR "
			+ "r.registerDate = :register OR r.releaseDate = :release) AND r.pet.id = :petId")
	Integer count(@Param("register") LocalDate register, @Param("release") LocalDate release, @Param("petId") int petId);
}

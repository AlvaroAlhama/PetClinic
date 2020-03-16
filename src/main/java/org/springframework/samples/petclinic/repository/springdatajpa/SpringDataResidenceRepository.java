package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Residence;
import org.springframework.samples.petclinic.repository.ResidenceRepository;

public interface SpringDataResidenceRepository extends ResidenceRepository, Repository<Residence, Integer>{

}

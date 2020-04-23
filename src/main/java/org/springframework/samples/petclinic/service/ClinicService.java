/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Residence;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.CauseRepository;
import org.springframework.samples.petclinic.repository.DonationRepository;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.ResidenceRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class ClinicService {

	private PetRepository petRepository;

	private VetRepository vetRepository;

	private OwnerRepository ownerRepository;

	private VisitRepository visitRepository;

	private ResidenceRepository residenceRepository;

	private CauseRepository causeRepository;
	
	private DonationRepository donationRepository;

	@Autowired
	public ClinicService(PetRepository petRepository, VetRepository vetRepository, OwnerRepository ownerRepository,
			VisitRepository visitRepository, ResidenceRepository residenceRepository, CauseRepository causeRepository,
			DonationRepository donationRepository) {
		this.petRepository = petRepository;
		this.vetRepository = vetRepository;
		this.ownerRepository = ownerRepository;
		this.visitRepository = visitRepository;
		this.residenceRepository = residenceRepository;
		this.causeRepository = causeRepository;
		this.donationRepository = donationRepository;
	}

	@Transactional(readOnly = true)
	public Collection<PetType> findPetTypes() throws DataAccessException {
		return petRepository.findPetTypes();
	}

	@Transactional(readOnly = true)
	public Owner findOwnerById(int id) throws DataAccessException {
		return ownerRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Owner> findOwnerByLastName(String lastName) throws DataAccessException {
		return ownerRepository.findByLastName(lastName);
	}

	@Transactional
	public void saveOwner(Owner owner) throws DataAccessException {
		ownerRepository.save(owner);
	}

	@Transactional
	public void deleteOwner(Owner owner) throws DataAccessException {
		ownerRepository.delete(owner);
	}

	@Transactional
	public void saveVisit(Visit visit) throws DataAccessException {
		visitRepository.save(visit);
	}

	@Transactional
	public void deleteVisit(Visit visit) {
		visitRepository.delete(visit);
	}

	public Visit findVisitsById(int visitId) {
		return visitRepository.findById(visitId);
	}

	@Transactional(readOnly = true)
	public Pet findPetById(int id) throws DataAccessException {
		return petRepository.findById(id);
	}

	@Transactional
	public void savePet(Pet pet) throws DataAccessException {
		petRepository.save(pet);
	}

	@Transactional
	public void deletePet(Pet pet) throws DataAccessException {
		petRepository.delete(pet);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "vets")
	public Collection<Vet> findVets() throws DataAccessException {
		return vetRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Vet findVetById(int id) throws DataAccessException {
		return vetRepository.findById(id);
	}

	@Transactional
	public void deleteVet(Vet vet) throws DataAccessException {
		this.vetRepository.delete(vet);
	}

	@Transactional
	public void saveVet(Vet vet) throws DataAccessException {
		this.vetRepository.save(vet);
	}

	@Transactional(readOnly = true)
	public Collection<Specialty> findAllSpecialty() throws DataAccessException {
		return this.vetRepository.findAllSpecialty();
	}

	public Collection<Visit> findVisitsByPetId(int petId) {
		return visitRepository.findByPetId(petId);
	}

	@Transactional
	public void saveResidence(Residence residence) throws DataAccessException {
		residenceRepository.save(residence);
	}

	public Collection<Residence> findResidencesByPetId(int petId) {
		return residenceRepository.findByPetId(petId);
	}

	@Transactional(readOnly = true)
	public Residence findResidenceById(int id) throws DataAccessException {
		return residenceRepository.findById(id);
	}

	@Transactional
	public void deleteResidence(Residence residence) throws DataAccessException {
		residenceRepository.delete(residence);
	}

	// Cause

	public void addCause(Cause cause) {
		causeRepository.save(cause);
	}

	public Iterable<Cause> findAll() {
		return this.causeRepository.findAll();
	}

	public Optional<Cause> findById(Integer causeId) {
		return this.causeRepository.findById(causeId);
	}
	
	// Donation
	
	public void saveDonation(Donation donation) {
		this.donationRepository.save(donation);
	}
	
	public Integer compruebaResidence(LocalDate register, LocalDate release, int petId) {
		return this.residenceRepository.count(register, release, petId);
	}

}

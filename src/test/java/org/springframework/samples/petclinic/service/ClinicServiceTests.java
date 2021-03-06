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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Residence;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following
 * services provided by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us unnecessary
 * set up time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances, meaning
 * that we don't need to perform application context lookups. See the use of
 * {@link Autowired @Autowired} on the <code>{@link
 * ClinicServiceTests#clinicService clinicService}</code> instance variable,
 * which uses autowiring <em>by type</em>.
 * <li><strong>Transaction management</strong>, meaning each test method is
 * executed in its own transaction, which is automatically rolled back by
 * default. Thus, even if tests insert or otherwise change database state, there
 * is no need for a teardown or cleanup script.
 * <li>An {@link org.springframework.context.ApplicationContext
 * ApplicationContext} is also inherited and can be used for explicit bean
 * lookup if necessary.</li>
 * </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Dave Syer
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ClinicServiceTests {

	@Autowired
	protected ClinicService clinicService;

	@Test
	public void shouldFindOwnersByLastName() {
		Collection<Owner> owners = this.clinicService.findOwnerByLastName("Davis");
		assertThat(owners.size()).isEqualTo(2);

		owners = this.clinicService.findOwnerByLastName("Daviss");
		assertThat(owners.isEmpty()).isTrue();
	}

	@Test
	public void shouldFindSingleOwnerWithPet() {
		Owner owner = this.clinicService.findOwnerById(1);
		assertThat(owner.getLastName()).startsWith("Franklin");
		assertThat(owner.getPets().size()).isEqualTo(1);
		assertThat(owner.getPets().get(0).getType()).isNotNull();
		assertThat(owner.getPets().get(0).getType().getName()).isEqualTo("cat");
	}

	@Test
	@Transactional
	public void shouldInsertOwner() {
		Collection<Owner> owners = this.clinicService.findOwnerByLastName("Schultz");
		int found = owners.size();

		Owner owner = new Owner();
		owner.setFirstName("Sam");
		owner.setLastName("Schultz");
		owner.setAddress("4, Evans Street");
		owner.setCity("Wollongong");
		owner.setTelephone("4444444444");
		this.clinicService.saveOwner(owner);
		assertThat(owner.getId().longValue()).isNotEqualTo(0);

		owners = this.clinicService.findOwnerByLastName("Schultz");
		assertThat(owners.size()).isEqualTo(found + 1);
	}

	@Test
	@Transactional
	public void shouldUpdateOwner() {
		Owner owner = this.clinicService.findOwnerById(1);
		String oldLastName = owner.getLastName();
		String newLastName = oldLastName + "X";

		owner.setLastName(newLastName);
		this.clinicService.saveOwner(owner);

		// retrieving new name from database
		owner = this.clinicService.findOwnerById(1);
		assertThat(owner.getLastName()).isEqualTo(newLastName);
	}

	@Test
	public void shouldDeleteOwner() throws Exception {
		Owner owner = this.clinicService.findOwnerById(1);
		this.clinicService.deleteOwner(owner);
		Owner ownerNew = this.clinicService.findOwnerById(1);
		assertThat(ownerNew).isEqualTo(null);
	}

	@Test
	public void shouldFindPetWithCorrectId() {
		Pet pet7 = this.clinicService.findPetById(7);
		assertThat(pet7.getName()).startsWith("Samantha");
		assertThat(pet7.getOwner().getFirstName()).isEqualTo("Jean");

	}

	@Test
	public void shouldFindAllPetTypes() {
		Collection<PetType> petTypes = this.clinicService.findPetTypes();

		PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
		assertThat(petType1.getName()).isEqualTo("cat");
		PetType petType4 = EntityUtils.getById(petTypes, PetType.class, 4);
		assertThat(petType4.getName()).isEqualTo("snake");
	}

	@Test
	@Transactional
	public void shouldInsertPetIntoDatabaseAndGenerateId() {
		Owner owner6 = this.clinicService.findOwnerById(6);
		int found = owner6.getPets().size();

		Pet pet = new Pet();
		pet.setName("bowser");
		Collection<PetType> types = this.clinicService.findPetTypes();
		pet.setType(EntityUtils.getById(types, PetType.class, 2));
		pet.setBirthDate(LocalDate.now());
		owner6.addPet(pet);
		assertThat(owner6.getPets().size()).isEqualTo(found + 1);

		this.clinicService.savePet(pet);
		this.clinicService.saveOwner(owner6);

		owner6 = this.clinicService.findOwnerById(6);
		assertThat(owner6.getPets().size()).isEqualTo(found + 1);
		// checks that id has been generated
		assertThat(pet.getId()).isNotNull();
	}

	@Test
	@Transactional
	public void shouldUpdatePetName() throws Exception {
		Pet pet7 = this.clinicService.findPetById(7);
		String oldName = pet7.getName();

		String newName = oldName + "X";
		pet7.setName(newName);
		this.clinicService.savePet(pet7);

		pet7 = this.clinicService.findPetById(7);
		assertThat(pet7.getName()).isEqualTo(newName);
	}

	@Test
	public void shouldDeletePet() throws Exception {
		Pet pet = this.clinicService.findPetById(1);
		this.clinicService.deletePet(pet);
		Pet petNew = this.clinicService.findPetById(1);
		assertThat(petNew).isEqualTo(null);
	}

	@Test
	public void shouldFindVets() {
		Collection<Vet> vets = this.clinicService.findVets();

		Vet vet = EntityUtils.getById(vets, Vet.class, 3);
		assertThat(vet.getLastName()).isEqualTo("Douglas");
		assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
		assertThat(vet.getSpecialties().get(0).getName()).isEqualTo("dentistry");
		assertThat(vet.getSpecialties().get(1).getName()).isEqualTo("surgery");
	}

	@Test
	public void shouldFindVetById() throws Exception {
		Vet vet = this.clinicService.findVetById(3);
		assertThat(vet.getLastName()).isEqualTo("Douglas");
		assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
		assertThat(vet.getSpecialties().get(0).getName()).isEqualTo("dentistry");
		assertThat(vet.getSpecialties().get(1).getName()).isEqualTo("surgery");
	}

	@Test
	public void shouldDeleteVet() throws Exception {
		Vet vet = this.clinicService.findVetById(1);
		this.clinicService.deleteVet(vet);
		Vet vetNew = this.clinicService.findVetById(1);
		assertThat(vetNew).isEqualTo(null);
	}

	public void shouldFindSpecialties() {
		Collection<Specialty> specialties = this.clinicService.findAllSpecialty();
		Specialty specialty = EntityUtils.getById(specialties, Specialty.class, 1);
		assertThat(specialty.getName()).isEqualTo("radiology");
	}

	@Test
	@Transactional
	public void shouldAddNewVisitForPet() {
		Pet pet7 = this.clinicService.findPetById(7);
		int found = pet7.getVisits().size();
		Visit visit = new Visit();
		pet7.addVisit(visit);
		visit.setDescription("test");
		this.clinicService.saveVisit(visit);
		this.clinicService.savePet(pet7);

		pet7 = this.clinicService.findPetById(7);
		assertThat(pet7.getVisits().size()).isEqualTo(found + 1);
		assertThat(visit.getId()).isNotNull();
	}

	@Test
	public void shouldFindVisitsByPetId() throws Exception {
		Collection<Visit> visits = this.clinicService.findVisitsByPetId(7);
		assertThat(visits.size()).isEqualTo(2);
		Visit[] visitArr = visits.toArray(new Visit[visits.size()]);
		assertThat(visitArr[0].getPet()).isNotNull();
		assertThat(visitArr[0].getDate()).isNotNull();
		assertThat(visitArr[0].getPet().getId()).isEqualTo(7);
	}

	@Test
	public void shouldFindVisitById() throws Exception {
		Visit visit = this.clinicService.findVisitsById(1);
		assertThat(visit.getDescription()).isEqualTo("rabies shot");
		assertThat(visit.getPet().getId()).isEqualTo(7);
	}

	@Test
	public void shouldDeleteVisit() throws Exception {
		Visit visit = this.clinicService.findVisitsById(1);
		this.clinicService.deleteVisit(visit);
		Visit visitNew = this.clinicService.findVisitsById(1);
		assertThat(visitNew).isEqualTo(null);
	}

	@Test
	public void shouldFindResidencesByPetId() throws Exception {
		Collection<Residence> residences = this.clinicService.findResidencesByPetId(1);
		assertThat(residences.size()).isEqualTo(0);
	}

	@Test
	public void shouldFindResidenceById() throws Exception {
		Residence residence = this.clinicService.findResidenceById(1);
		assertThat(residence).isEqualTo(null);
	}

	@Test
	public void shouldFindCauses() throws Exception {
		Iterable<Cause> causes = this.clinicService.findAll();
		assertThat(causes.iterator().next().getName()).isEqualTo("Dog help");
	}

	@Test
	public void shouldFindCauseById() throws Exception {
		Optional<Cause> cause = this.clinicService.findById(1);
		assertThat(cause.get().getName()).isEqualTo("Dog help");
	}

}

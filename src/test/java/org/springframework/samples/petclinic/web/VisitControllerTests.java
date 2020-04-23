package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(VisitController.class)
class VisitControllerTests {

	private static final int TEST_PET_ID = 1;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	private static Date date = null;

	@MockBean
	private ClinicService clinicService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		Calendar cal = Calendar.getInstance();
		date = cal.getTime();
		Pet pet1 = new Pet();
		pet1.setId(TEST_PET_ID);
		pet1.setName("Pet1");
		PetType pt = new PetType();
		pt.setName("dog");
		pet1.setType(pt);
		pet1.setBirthDate(LocalDate.now());
		Visit visit = new Visit();
		visit.setId(1);
		visit.setPet(pet1);
		visit.setDate(LocalDate.now());
		visit.setDescription("Description");
		pet1.addVisit(visit);
		given(this.clinicService.findPetById(TEST_PET_ID)).willReturn(pet1);
		given(this.clinicService.findVisitsById(1)).willReturn(visit);
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitNewVisitForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewVisitFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID).with(csrf())
				.param("date", dateFormat.format(date)).param("description", "Visit Description"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewVisitFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID).with(csrf()).param("name", "George"))
				.andExpect(model().attributeHasErrors("visit")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowVisits() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/visits", TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("visits")).andExpect(view().name("visitList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testDeleteVisit() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/visits/{visitId}/delete", TEST_PET_ID, 1))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/owners/{ownerId}"));
	}

}

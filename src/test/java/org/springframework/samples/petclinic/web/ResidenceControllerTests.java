package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Residence;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ResidenceController.class)
public class ResidenceControllerTests {

	private static final int TEST_PET_ID = 1;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	private static Date initialDate = null;
	private static Date endDate = null;

	@MockBean
	private ClinicService clinicService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		Calendar cal = Calendar.getInstance();
		initialDate = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, 7);
		endDate = cal.getTime();
		Pet pet1 = new Pet();
		pet1.setId(TEST_PET_ID);
		pet1.setName("Pet1");
		PetType pt = new PetType();
		pt.setName("dog");
		pet1.setType(pt);
		pet1.setBirthDate(LocalDate.now());
		Residence residence = new Residence();
		residence.setId(1);
		residence.setPet(pet1);
		residence.setRegisterDate(LocalDate.now());
		residence.setReleaseDate(LocalDate.now().plusDays(1));
		pet1.addResidence(residence);
		given(this.clinicService.findPetById(TEST_PET_ID)).willReturn(pet1);
		given(this.clinicService.findResidenceById(1)).willReturn(residence);
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitNewResidenceForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/residences/new", TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateResidenceForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewResidenceFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/residences/new", TEST_PET_ID).with(csrf())
				.param("registerDate", dateFormat.format(initialDate)).param("releaseDate", dateFormat.format(endDate)))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/owners/{ownerId}"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowResidences() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/residences", TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(view().name("residenceList"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testDeleteResidence() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/residences/{residenceId}/delete", 1, TEST_PET_ID, 1)).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

}

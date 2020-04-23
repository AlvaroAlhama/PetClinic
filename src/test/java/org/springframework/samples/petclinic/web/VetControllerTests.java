package org.springframework.samples.petclinic.web;

import static org.hamcrest.xml.HasXPath.hasXPath;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for the {@link VetController}
 */
@WebMvcTest(VetController.class)
class VetControllerTests {

	@MockBean
	private ClinicService clinicService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {

		Vet james = new Vet();
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setId(1);
		Vet helen = new Vet();
		helen.setFirstName("Helen");
		helen.setLastName("Leary");
		helen.setId(2);
		Specialty radiology = new Specialty();
		radiology.setId(1);
		radiology.setName("radiology");
		helen.addSpecialty(radiology);
		given(this.clinicService.findVets()).willReturn(Lists.newArrayList(james, helen));
		given(this.clinicService.findVetById(1)).willReturn(james);
		given(this.clinicService.findVetById(2)).willReturn(helen);
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowVetListHtml() throws Exception {
		mockMvc.perform(get("/vets")).andExpect(status().isOk()).andExpect(model().attributeExists("vets"))
				.andExpect(view().name("vets/vetList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowVet() throws Exception {
		mockMvc.perform(get("/vets/{vetId}", 1)).andExpect(status().isOk()).andExpect(model().attributeExists("vet"))
				.andExpect(view().name("vets/vetDetails"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitNewVetForm() throws Exception {
		mockMvc.perform(get("/vets/new")).andExpect(status().isOk())
				.andExpect(view().name("vets/createOrUpdateVetForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewVetFormSuccess() throws Exception {
		mockMvc.perform(post("/vets/new").with(csrf()).param("firstName", "nombre").param("lastName", "apellido"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/vets/null"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateVetForm() throws Exception {
		mockMvc.perform(get("/vets/{vetId}/edit", 1)).andExpect(status().isOk())
				.andExpect(model().attribute("vet", hasProperty("firstName", is("James"))))
				.andExpect(model().attribute("vet", hasProperty("lastName", is("Carter"))))
				.andExpect(view().name("vets/createOrUpdateVetForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateVetFormSuccess() throws Exception {
		mockMvc.perform(
				post("/vets/{vetId}/edit", 1).with(csrf()).param("firstName", "nombre").param("lastName", "apellido"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/vets/{vetId}"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testDeleteVet() throws Exception {
		mockMvc.perform(get("/vets/{vetId}/delete", 2))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/vets"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowVetListXml() throws Exception {
		mockMvc.perform(get("/vets.xml").accept(MediaType.APPLICATION_XML)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
				.andExpect(content().node(hasXPath("/vets/vetList[id=1]/id")));
	}

}

package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CauseController.class)
public class CauseControllerTests {

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
		Cause cause = new Cause();
		cause.setId(1);
		cause.setName("prueba");
		cause.setDescription("description");
		cause.setObjetive(100);
		cause.setEndDate(LocalDate.now().plusDays(7));
		Collection<Cause> col = new ArrayList<>();
		col.add(cause);
		given(this.clinicService.findAll()).willReturn(col);
		given(this.clinicService.findById(1)).willReturn(Optional.of(cause));
	}

	@WithMockUser(value = "spring")
	@Test
	void testListCauses() throws Exception {
		mockMvc.perform(get("/cause")).andExpect(status().isOk()).andExpect(view().name("cause/causeList"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowCauses() throws Exception {
		mockMvc.perform(get("/cause/{causeId}", 1)).andExpect(status().isOk())
		.andExpect(model().attribute("cause", hasProperty("name", is("prueba"))))
		.andExpect(model().attribute("cause", hasProperty("description", is("description"))))
		.andExpect(model().attribute("cause", hasProperty("objetive", is(100))))
		.andExpect(view().name("cause/causeShow"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitNewCauseForm() throws Exception {
		mockMvc.perform(get("/cause/new")).andExpect(status().isOk()).andExpect(view().name("cause/causeForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewCauseFormSuccess() throws Exception {
		mockMvc.perform(post("/cause/new").with(csrf()).param("name", "nombre").param("description", "description")
				.param("objetive", "100").param("endDate", dateFormat.format(date)))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/cause"));
	}

}

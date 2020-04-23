package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DonationController.class)
public class DonationControllerTests {

	@MockBean
	private ClinicService clinicService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		Cause cause = new Cause();
		cause.setId(1);
		cause.setName("prueba");
		cause.setDescription("description");
		cause.setObjetive(100);
		cause.setEndDate(LocalDate.now().plusDays(7));
		Donation donation = new Donation();
		donation.setId(1);
		donation.setDate(LocalDate.now());
		donation.setClient("Cliente falso");
		donation.setCause(cause);
		donation.setAmount(50);
		cause.addDonation(donation);
		given(this.clinicService.findById(1)).willReturn(Optional.of(cause));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitNewCauseForm() throws Exception {
		mockMvc.perform(get("/cause/{causeId}/donations/new", 1)).andExpect(status().isOk())
				.andExpect(view().name("donation/form"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewCauseFormSuccess() throws Exception {
		mockMvc.perform(
				post("/cause/{causeId}/donations/new", 1).with(csrf()).param("client", "nombre").param("amount", "5"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/cause/1"));
	}

}

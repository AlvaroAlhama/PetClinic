package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cause/{causeId}/donations")
public class DonationController {

	private final ClinicService donationService;
	
	@Autowired
	public DonationController(ClinicService donationService) {
		this.donationService = donationService;
	}
	
	@GetMapping("/new")
	public String initCreateForm(@PathVariable("causeId") int causeId, ModelMap model) {
		Donation donation;
		Optional<Cause> cause;
		String view;
		
		donation = new Donation();
		cause = donationService.findById(causeId);
		
		if(cause.isPresent()) {
			donation.setCause(cause.get());
			donation.setDate(LocalDate.now());
			model.addAttribute("donation", donation);
			view = "donation/form";
		} else {
			model.addAttribute("message", "Cause not found!");
			view = "redirect:/cause";
		}
		
		return view;
	}
	
	@PostMapping("/new")
	public String processCreateForm(@PathVariable("causeId") int causeId, @Valid Donation donation, BindingResult result, ModelMap model) {
		String view;
		Optional<Cause> cause;
		
		cause = this.donationService.findById(causeId);
		
		if(cause.isPresent()) {
			if(result.hasErrors()) {
				model.addAttribute("donation", donation);
				view = "donation/form";
			} else {
				donation.setCause(cause.get());
				donation.setDate(LocalDate.now());
				cause.get().addDonation(donation);
				this.donationService.saveDonation(donation);
				view = "redirect:/cause/" + causeId;
			}
		} else {
			model.addAttribute("message", "Cause not found!");
			view = "redirect:/cause";
		}
		
		return view;
	}
	
}

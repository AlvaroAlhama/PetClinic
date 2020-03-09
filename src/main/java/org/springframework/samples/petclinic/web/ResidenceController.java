package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Residence;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ResidenceController {
	
	private final ClinicService clinicService;

	@Autowired
	public ResidenceController(ClinicService clinicService) {
		this.clinicService = clinicService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("residence")
	public Residence loadPetWithResidence(@PathVariable("petId") int petId) {
		Pet pet = this.clinicService.findPetById(petId);
		Residence residence = new Residence();
		pet.addResidence(residence);
		return residence;
	}

	@GetMapping(value = "/owners/*/pets/{petId}/residences/new")
	public String initNewResidenceForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		return "pets/createOrUpdateResidenceForm";
	}

	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/residences/new")
	public String processNewResidenceForm(@Valid Residence residence, BindingResult result) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateResidenceForm";
		}
		else {
			this.clinicService.saveResidence(residence);
			return "redirect:/owners/{ownerId}";
		}
	}

	@GetMapping(value = "/owners/*/pets/{petId}/residences")
	public String showResidences(@PathVariable int petId, Map<String, Object> model) {
		model.put("residences", this.clinicService.findPetById(petId).getResidences());
		return "residenceList";
	}

}

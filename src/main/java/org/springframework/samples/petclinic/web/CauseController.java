package org.springframework.samples.petclinic.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cause")
public class CauseController {
	
	private final ClinicService causeService;

	@Autowired
	public CauseController(ClinicService causeService) {
		this.causeService = causeService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@InitBinder("cause")
	public void initCauseBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new CauseValidator(causeService));
	}
	
	@GetMapping()
	public String listCauses(ModelMap modelMap) {
		Iterable<Cause> c = this.causeService.findAll();
		modelMap.addAttribute("causes", c);
		return "cause/causeList";
	}
	
	@GetMapping("/{causeId}")
	public String showCause(@PathVariable("causeId") int causeId, ModelMap model) {
		Optional<Cause> cause;
		Integer causeAmount = 0;
		String view;
		
		cause = this.causeService.findById(causeId);
		
		if(cause.isPresent()) {
			model.addAttribute("cause", cause.get());
			causeAmount = cause.get().getDonations().stream().mapToInt(Donation::getAmount).sum();
			model.addAttribute("causeAmount", causeAmount);
			view = "cause/causeShow";
		} else {
			model.addAttribute("message", "Cause not found!");
			view = "cause/causeShow";
		}
		
		return view;
	}
	
	@GetMapping("/new")
	public String addCause(ModelMap modelMap) {
		modelMap.addAttribute("cause", new Cause());
		return "cause/causeForm";
	}
	
	@PostMapping("/new")
	public String saveCause(@Valid Cause cause, BindingResult result, ModelMap modelMap) {
		if(result.hasErrors()) {
			return "cause/causeForm";
		} else {
			
			this.causeService.addCause(cause);
			
			return "redirect:/cause";
		}
	}
}

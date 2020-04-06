package org.springframework.samples.petclinic.web;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DonationValidator implements Validator {
	
	private static final String REQUIRED = "requerido";

	private final ClinicService clinicService;

	@Autowired
	public DonationValidator(ClinicService clinicService) {
		this.clinicService = clinicService;
	}
	
	@Override
	public void validate(Object obj, Errors errors) {
		Donation donation = (Donation) obj;
		
		if (donation.getAmount()==null) {
			errors.rejectValue("amount", REQUIRED, REQUIRED);
		}else {
			if (donation.getAmount()<0) {
			errors.rejectValue("amount", "El valor de la donacion debe ser positivo", "El valor de la donacion debe ser positivo");
			}
	    }


		if(donation.getClient().isEmpty()) {
			errors.rejectValue("client", REQUIRED, REQUIRED);
		}		

}
	public boolean supports(Class<?> clazz) {
		return Donation.class.isAssignableFrom(clazz);
	}

}

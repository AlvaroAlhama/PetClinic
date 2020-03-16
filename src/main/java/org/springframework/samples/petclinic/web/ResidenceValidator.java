package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.springframework.samples.petclinic.model.Residence;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ResidenceValidator implements Validator{
	
	private static final String REQUIRED = "requerido";

	@Override
	public boolean supports(Class<?> clazz) {
		return Residence.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Residence residence = (Residence) target;
		
		if (residence.getRegisterDate() == null) {
			errors.rejectValue("registerDate", REQUIRED, REQUIRED);
		}
		
		if (residence.getReleaseDate() == null) {
			errors.rejectValue("releaseDate", REQUIRED, REQUIRED);
		}
		
		if(residence.getRegisterDate() != null || residence.getReleaseDate() != null) {
			
			LocalDate registerDate = residence.getRegisterDate();
			LocalDate releaseDate = residence.getReleaseDate();
			LocalDate now = LocalDate.now();
			
			if(residence.getRegisterDate() != null && registerDate.isBefore(now)) {
				errors.rejectValue("registerDate", REQUIRED, "La fecha de registro tiene que ser igual o posterior a la actual");
			}
			
			if(residence.getReleaseDate() != null && releaseDate.isBefore(now)) {
				errors.rejectValue("releaseDate", REQUIRED, "La fecha de salida tiene que ser igual o posterior a la actual");
			}
			
			if(residence.getRegisterDate() != null && residence.getReleaseDate() != null && releaseDate.isBefore(registerDate)) {
				errors.rejectValue("releaseDate", REQUIRED, "La fecha de salida tiene que ser igual o posterior a la de registro");
			}
		}	
	}

}

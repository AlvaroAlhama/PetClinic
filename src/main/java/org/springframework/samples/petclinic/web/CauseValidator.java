package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CauseValidator implements Validator {

	private static final String REQUIRED = "Campo requerido";
	
	private final ClinicService clinicService;

	@Autowired
	public CauseValidator(ClinicService clinicService) {
		this.clinicService = clinicService;
	}

	public void validate(Object obj, Errors errors) {
		Cause cause = (Cause) obj;
		
		if (!StringUtils.hasLength(cause.getName()) || cause.getName().length()>50 || cause.getName().length()<3) {
			errors.rejectValue("name", "El nombre debe tener entre 3 y 50 caracteres","El nombre debe tener entre 3 y 50 caracteres");
		}

		if (cause.getEndDate() == null) {
			errors.rejectValue("endDate", REQUIRED, REQUIRED);
		}else {
			if (cause.getEndDate().isBefore(LocalDate.now())) {
				errors.rejectValue("endDate", "La fecha debe ser posterior a la actual", "La fecha debe ser posterior a la actual");
			}
		}
		if (cause.getDescription().isEmpty()) {
			errors.rejectValue("description", REQUIRED, REQUIRED);
		}

		if (cause.getObjetive() == null) {
			errors.rejectValue("objetive", REQUIRED, REQUIRED);
			
		}else {
			if (cause.getObjetive()<=0) {
			errors.rejectValue("objetive", "el objetivo no puede ser negativo", "el objetivo no puede ser negativo");
			}		
		}
		List<Cause> listCauses = (List<Cause>) clinicService.findAll();
		for (Cause r : listCauses) {
			if(cause.getName().equals(r.getName())) {
				errors.rejectValue("name", "la causa ya existe", "la causa ya existe");
			}
		}
	}

	public boolean supports(Class<?> clazz) {
		return Cause.class.isAssignableFrom(clazz);
	}

}

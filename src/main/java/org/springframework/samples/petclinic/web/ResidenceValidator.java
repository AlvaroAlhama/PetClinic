package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Residence;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ResidenceValidator implements Validator {

	private static final String REQUIRED = "requerido";

	private final ClinicService clinicService;

	@Autowired
	public ResidenceValidator(ClinicService clinicService) {
		this.clinicService = clinicService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Residence.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Residence residence = (Residence) target;
		String registerDateS = "registerDate";
		String releaseDateS = "releaseDate";

		if (residence.getRegisterDate() == null) {
			errors.rejectValue(registerDateS, REQUIRED, REQUIRED);
		}

		if (residence.getReleaseDate() == null) {
			errors.rejectValue(releaseDateS, REQUIRED, REQUIRED);
		}

		if (residence.getRegisterDate() != null && residence.getReleaseDate() != null) {

			LocalDate registerDate = residence.getRegisterDate();
			LocalDate releaseDate = residence.getReleaseDate();
			LocalDate now = LocalDate.now();

			if (registerDate.isBefore(now)) {
				errors.rejectValue(registerDateS, REQUIRED,
						"La fecha de registro tiene que ser igual o posterior a la actual");
			}
			if (releaseDate.isBefore(now)) {
				errors.rejectValue(releaseDateS, REQUIRED,
						"La fecha de salida tiene que ser igual o posterior a la actual");
			}
			if (releaseDate.isBefore(registerDate)) {
				errors.rejectValue(releaseDateS, REQUIRED,
						"La fecha de salida tiene que ser igual o posterior a la de registro");
			}

			Integer count = this.clinicService.compruebaResidence(registerDate, releaseDate,
					residence.getPet().getId());
			if (count != 0 && count != null) {
				errors.rejectValue(registerDateS, REQUIRED, "El registro no puede coincidir con uno anterior");
			}
		}
	}

}

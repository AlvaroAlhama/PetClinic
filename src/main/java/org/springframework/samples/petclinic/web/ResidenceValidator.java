package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.List;

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

		if (residence.getRegisterDate() == null) {
			errors.rejectValue("registerDate", REQUIRED, REQUIRED);
		}

		if (residence.getReleaseDate() == null) {
			errors.rejectValue("releaseDate", REQUIRED, REQUIRED);
		}

		if (residence.getRegisterDate() != null || residence.getReleaseDate() != null) {

			LocalDate registerDate = residence.getRegisterDate();
			LocalDate releaseDate = residence.getReleaseDate();
			LocalDate now = LocalDate.now();

			if (residence.getRegisterDate() != null && registerDate.isBefore(now)) {
				errors.rejectValue("registerDate", REQUIRED,
						"La fecha de registro tiene que ser igual o posterior a la actual");
			}

			if (residence.getReleaseDate() != null && releaseDate.isBefore(now)) {
				errors.rejectValue("releaseDate", REQUIRED,
						"La fecha de salida tiene que ser igual o posterior a la actual");
			}

			if (residence.getRegisterDate() != null && residence.getReleaseDate() != null
					&& releaseDate.isBefore(registerDate)) {
				errors.rejectValue("releaseDate", REQUIRED,
						"La fecha de salida tiene que ser igual o posterior a la de registro");
			}

			List<Residence> listResidence = (List<Residence>) clinicService
					.findResidencesByPetId(residence.getPet().getId());
			for (Residence r : listResidence) {
				if (registerDate.isAfter(r.getRegisterDate()) && registerDate.isBefore(r.getReleaseDate())) {
					errors.rejectValue("registerDate", REQUIRED,
							"La fecha de registro no puede estar entre las fechas de entrada y salida de otro alojamiento");
				}
				if (releaseDate.isAfter(r.getRegisterDate()) && releaseDate.isBefore(r.getReleaseDate())) {
					errors.rejectValue("releaseDate", REQUIRED,
							"La fecha de salida no puede estar entre las fechas de entrada y salida de otro alojamiento");
				}
				if (registerDate.isBefore(r.getRegisterDate()) && releaseDate.isAfter(r.getReleaseDate())) {
					errors.rejectValue("registerDate", REQUIRED, "El alojamiento no puede englobar otro alojamiento");
					errors.rejectValue("releaseDate", REQUIRED, "El alojamiento no puede englobar otro alojamiento");
				}
				if (registerDate.isEqual(r.getRegisterDate())) {
					errors.rejectValue("registerDate", REQUIRED,
							"La fecha de registro no puede ser igual a otra fecha de registro");
				}
				if (releaseDate.isEqual(r.getReleaseDate())) {
					errors.rejectValue("releaseDate", REQUIRED,
							"La fecha de salida no puede ser igual a otra fecha de salida");
				}
			}
		}
	}

}

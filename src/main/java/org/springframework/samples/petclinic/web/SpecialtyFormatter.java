package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Component;

@Component
public class SpecialtyFormatter implements Formatter<Specialty>{
	
	private final ClinicService clinicService;
	
	@Autowired
	public SpecialtyFormatter(ClinicService clinicService) {
		this.clinicService = clinicService;
	}

	@Override
	public String print(Specialty object, Locale locale) {
		return object.getName();
	}

	@Override
	public Specialty parse(String text, Locale locale) throws ParseException {
		Collection<Specialty> specialties = this.clinicService.findAllSpecialty();
        for (Specialty speciality : specialties) {
            if (speciality.getName().equals(text)) {
                return speciality;
            }
        }
        throw new ParseException("Especialidad no encontrada " + text, 0);
	}

}

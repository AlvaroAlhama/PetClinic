package org.springframework.samples.petclinic.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.service.ClinicService;

/**
 * Test class for {@link PetTypeFormatter}
 *
 * @author Colin But
 */
@ExtendWith(MockitoExtension.class)
class SpecialtyFormatterTests {

	@Mock
	private ClinicService clinicService;

	private SpecialtyFormatter specialtyFormatter;

	@BeforeEach
	void setup() {
		specialtyFormatter = new SpecialtyFormatter(clinicService);
	}

	@Test
	void testPrint() {
		Specialty specialty = new Specialty();
		specialty.setName("radiology");
		String specialtyName = specialtyFormatter.print(specialty, Locale.ENGLISH);
		assertEquals("radiology", specialtyName);
	}

	@Test
	void shouldParse() throws ParseException {
		Mockito.when(clinicService.findAllSpecialty()).thenReturn(makeSpecialties());
		Specialty specialty = specialtyFormatter.parse("surgery", Locale.ENGLISH);
		assertEquals("surgery", specialty.getName());
	}

	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(clinicService.findAllSpecialty()).thenReturn(makeSpecialties());
		Assertions.assertThrows(ParseException.class, () -> {
			specialtyFormatter.parse("Fish", Locale.ENGLISH);
		});
	}

	private Collection<Specialty> makeSpecialties() {
		Collection<Specialty> specialties = new ArrayList<>();
		specialties.add(new Specialty() {
			{
				setName("radiology");
			}
		});
		specialties.add(new Specialty() {
			{
				setName("surgery");
			}
		});
		return specialties;
	}

}

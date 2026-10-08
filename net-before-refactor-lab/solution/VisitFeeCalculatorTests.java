/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

/**
 * Characterization tests for {@link VisitFeeCalculator}. They pin down what the code does
 * today, not what we think it should do. Some of these behaviors look like bugs. Keep
 * them until someone who owns billing says otherwise.
 */
class VisitFeeCalculatorTests {

	private static final LocalDate WEDNESDAY = LocalDate.of(2026, 10, 14);

	private static final LocalDate SATURDAY = LocalDate.of(2026, 10, 17);

	private static final LocalDate SUNDAY = LocalDate.of(2026, 10, 18);

	private final VisitFeeCalculator calculator = new VisitFeeCalculator();

	// Plain exams by pet type and age

	@Test
	void adultDogPaysTheBaseExamFee() {
		assertThat(fee("dog", LocalDate.of(2020, 5, 1), WEDNESDAY, "checkup")).isEqualByComparingTo("45.00");
	}

	@Test
	void puppyGetsTenDollarsOff() {
		assertThat(fee("dog", LocalDate.of(2026, 3, 1), WEDNESDAY, "checkup")).isEqualByComparingTo("35.00");
	}

	@Test
	void kittenGetsTenDollarsOff() {
		assertThat(fee("cat", LocalDate.of(2026, 3, 1), WEDNESDAY, "checkup")).isEqualByComparingTo("35.00");
	}

	@Test
	void ageIsCountedInCalendarYearsSoADecemberPuppyIsOneInJanuary() {
		// Under three weeks old, but born last year, so no puppy rate.
		LocalDate visit = LocalDate.of(2026, 1, 7);
		assertThat(fee("dog", LocalDate.of(2025, 12, 20), visit, "checkup")).isEqualByComparingTo("45.00");
	}

	@Test
	void tenYearOldDogPaysNoSeniorSurcharge() {
		// The comment in the code says "10 and up". The code says 11 and up.
		assertThat(fee("dog", LocalDate.of(2016, 1, 1), WEDNESDAY, "checkup")).isEqualByComparingTo("45.00");
	}

	@Test
	void elevenYearOldDogPaysFifteenDollarSeniorSurcharge() {
		assertThat(fee("dog", LocalDate.of(2015, 1, 1), WEDNESDAY, "checkup")).isEqualByComparingTo("60.00");
	}

	@Test
	void youngHamsterGetsNoDiscount() {
		assertThat(fee("hamster", LocalDate.of(2026, 3, 1), WEDNESDAY, "checkup")).isEqualByComparingTo("45.00");
	}

	@Test
	void oldBirdGetsNoSurcharge() {
		assertThat(fee("bird", LocalDate.of(2010, 1, 1), WEDNESDAY, "checkup")).isEqualByComparingTo("45.00");
	}

	@Test
	void petWithNoTypePaysTheBaseExamFee() {
		assertThat(fee(null, LocalDate.of(2020, 5, 1), WEDNESDAY, "checkup")).isEqualByComparingTo("45.00");
	}

	@Test
	void exoticExamIsTimeAndAHalfRoundedUpToTheNextDollar() {
		// 45 x 1.5 = 67.50, which rounds up to 68.
		assertThat(fee("snake", LocalDate.of(2020, 5, 1), WEDNESDAY, "checkup")).isEqualByComparingTo("68.00");
	}

	@Test
	void typeNameIsNotCaseSensitive() {
		assertThat(fee("Lizard", LocalDate.of(2020, 5, 1), WEDNESDAY, "checkup")).isEqualByComparingTo("68.00");
	}

	// Procedures, read from the visit description

	@Test
	void rabiesShotAddsTwentyFiveDollars() {
		assertThat(fee("cat", LocalDate.of(2020, 5, 1), WEDNESDAY, "Rabies shot")).isEqualByComparingTo("70.00");
	}

	@Test
	void spayingACatAddsOneHundredTwentyDollars() {
		assertThat(fee("cat", LocalDate.of(2020, 5, 1), WEDNESDAY, "spayed")).isEqualByComparingTo("165.00");
	}

	@Test
	void neuteringADogAddsOneHundredEightyDollars() {
		assertThat(fee("dog", LocalDate.of(2020, 5, 1), WEDNESDAY, "neutered")).isEqualByComparingTo("225.00");
	}

	@Test
	void neuteringAnExoticAddsOneHundredEightyToTheExoticExam() {
		// 67.50 + 180 = 247.50, which rounds up to 248.
		assertThat(fee("lizard", LocalDate.of(2020, 5, 1), WEDNESDAY, "neutered")).isEqualByComparingTo("248.00");
	}

	@Test
	void followUpIsFreeOnAWeekday() {
		assertThat(fee("dog", LocalDate.of(2020, 5, 1), WEDNESDAY, "Follow-up for stitches"))
			.isEqualByComparingTo("0.00");
	}

	@Test
	void followUpWipesOutProcedureCharges() {
		assertThat(fee("dog", LocalDate.of(2020, 5, 1), WEDNESDAY, "neuter follow-up")).isEqualByComparingTo("0.00");
	}

	@Test
	void anyDescriptionThatSaysFollowIsFree() {
		// The rabies shot is not charged, because the note mentions a follow-up call.
		assertThat(fee("dog", LocalDate.of(2020, 5, 1), WEDNESDAY, "Rabies shot. Owner will follow up by phone"))
			.isEqualByComparingTo("0.00");
	}

	// Multi-pet discount

	@Test
	void ownerWithTwoPetsGetsNoDiscount() {
		Pet pet = pet("dog", LocalDate.of(2020, 5, 1));
		Owner owner = ownerOf(pet, pet("cat", LocalDate.of(2020, 5, 1)));
		assertThat(calculator.feeFor(owner, pet, visit(WEDNESDAY, "checkup"))).isEqualByComparingTo("45.00");
	}

	@Test
	void ownerWithThreePetsGetsTenPercentOffRoundedUpToTheNextDollar() {
		// 45 less 10% = 40.50, which rounds up to 41.
		Pet pet = pet("dog", LocalDate.of(2020, 5, 1));
		Owner owner = ownerOf(pet, pet("cat", LocalDate.of(2020, 5, 1)), pet("bird", LocalDate.of(2020, 5, 1)));
		assertThat(calculator.feeFor(owner, pet, visit(WEDNESDAY, "checkup"))).isEqualByComparingTo("41.00");
	}

	// Weekend surcharge

	@Test
	void saturdayAddsTwentyDollars() {
		assertThat(fee("dog", LocalDate.of(2020, 5, 1), SATURDAY, "checkup")).isEqualByComparingTo("65.00");
	}

	@Test
	void sundayHasNoSurcharge() {
		assertThat(fee("dog", LocalDate.of(2020, 5, 1), SUNDAY, "checkup")).isEqualByComparingTo("45.00");
	}

	@Test
	void followUpOnSaturdayStillPaysTheSurcharge() {
		// "Free" follow-ups cost $20 on a Saturday.
		assertThat(fee("dog", LocalDate.of(2020, 5, 1), SATURDAY, "follow-up")).isEqualByComparingTo("20.00");
	}

	@Test
	void multiPetDiscountDoesNotApplyToTheSaturdaySurcharge() {
		// 40.50 + 20 = 60.50, which rounds up to 61.
		Pet pet = pet("dog", LocalDate.of(2020, 5, 1));
		Owner owner = ownerOf(pet, pet("cat", LocalDate.of(2020, 5, 1)), pet("bird", LocalDate.of(2020, 5, 1)));
		assertThat(calculator.feeFor(owner, pet, visit(SATURDAY, "checkup"))).isEqualByComparingTo("61.00");
	}

	// Missing data

	@Test
	void visitWithNoDescriptionIsAPlainExam() {
		assertThat(fee("dog", LocalDate.of(2020, 5, 1), WEDNESDAY, null)).isEqualByComparingTo("45.00");
	}

	@Test
	void petWithNoBirthDateThrows() {
		Pet pet = pet("dog", null);
		assertThatNullPointerException()
			.isThrownBy(() -> calculator.feeFor(ownerOf(pet), pet, visit(WEDNESDAY, "checkup")));
	}

	private BigDecimal fee(String type, LocalDate birthDate, LocalDate visitDate, String description) {
		Pet pet = pet(type, birthDate);
		return calculator.feeFor(ownerOf(pet), pet, visit(visitDate, description));
	}

	private static Pet pet(String typeName, LocalDate birthDate) {
		Pet pet = new Pet();
		pet.setName("Rex");
		pet.setBirthDate(birthDate);
		if (typeName != null) {
			PetType type = new PetType();
			type.setName(typeName);
			pet.setType(type);
		}
		return pet;
	}

	private static Owner ownerOf(Pet... pets) {
		Owner owner = new Owner();
		for (Pet pet : pets) {
			owner.addPet(pet);
		}
		return owner;
	}

	private static Visit visit(LocalDate date, String description) {
		Visit visit = new Visit();
		visit.setDate(date);
		visit.setDescription(description);
		return visit;
	}

}

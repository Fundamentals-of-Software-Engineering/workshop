# Net Before Refactor: answer key (facilitators only)

The lab lives in `net-before-refactor-lab/` (README, `legacy-code.diff`,
`agent-refactor.diff`). Base: spring-petclinic `500158f732419217507c7656904b8e6aa1bcc0d6` (Sept 29,
2026, Spring Boot 4.1). The reference tests are below and in `solution/`. Checked
on Java 27, Oct 8, 2026.

## Verified results

| Code | Full `./mvnw test` | `VisitFeeCalculatorTests` |
|---|---|---|
| Base PetClinic + `legacy-code.diff` | 81 run, all green | (not there yet) |
| Legacy + reference tests | 107 run, all green | 26 run, all green |
| Legacy + reference tests + `agent-refactor.diff` | 107 run, 7 red | 26 run, 7 red |

The 81 original PetClinic tests stay green on the refactor. Without a net, the build says the
refactor is fine.

Both diffs apply cleanly with `git apply` to a fresh clone at the pinned SHA. The legacy diff is 95
lines. The refactor diff is 141 lines.

## What the legacy code does

`VisitFeeCalculator.feeFor(Owner owner, Pet pet, Visit visit)` returns a `BigDecimal` fee. It's a
`@Component` that nothing calls yet (the Javadoc says a nightly billing export does). Every input is
passed in, so the tests need no clock. The steps run in this order:

1. Start at $45 (base exam).
2. Dogs and cats: age under 1 is $10 off. Age over 10 is $15 extra. Snakes and lizards: times 1.5.
   Every other type, or no type, pays $45.
3. The description adds charges: "rabies" adds $25. "spay" or "neuter" adds $120 for a cat, $180 for
   anything else.
4. A description that contains "follow" sets the fee to $0.
5. An owner with 3 or more pets gets 10% off.
6. A Saturday visit adds $20.
7. Round up to the next whole dollar.

Type names and descriptions are lowercased first, so case doesn't matter.

## The odd behaviors, and why to keep them

Each of these looks like a bug. Each one is how the clinic has billed people for years. Customers,
reports, or a price list might depend on it. The right move is to pin it with a test, then ask the
person who owns billing. Not to fix it in a refactor.

| # | Odd behavior | Test that pins it | Why someone might depend on it |
|---|---|---|---|
| 1 | Age is calendar years (`visitYear - birthYear`). A puppy born Dec 20, 2025 is "1" on Jan 7, 2026, and loses the puppy rate before it is three weeks old. | `ageIsCountedInCalendarYearsSoADecemberPuppyIsOneInJanuary` | The printed price list may say "puppy rate in the year they're born." Changing it changes January invoices. |
| 2 | The comment says seniors are "10 and up." The code charges from 11 (`age > 10`). | `tenYearOldDogPaysNoSeniorSurcharge` | Customers were billed by the code, not the comment. Raising fees for every 10-year-old pet is a pricing decision. |
| 3 | Saturday adds $20. Sunday adds nothing. | `sundayHasNoSurcharge` | The clinic may have been closed Sundays, or Sunday emergencies are billed somewhere else. |
| 4 | A "free" follow-up costs $20 on a Saturday. | `followUpOnSaturdayStillPaysTheSurcharge` | The surcharge may pay weekend staff, whatever the visit. |
| 5 | Any description with "follow" in it is free, even "Rabies shot. Owner will follow up by phone." | `anyDescriptionThatSaysFollowIsFree` | Staff may have learned to type "follow" to waive a fee. Yes, really. Ask. |

Also worth pinning, but less odd:

- The fee rounds up to the next whole dollar, after the discount. $40.50 becomes $41. A business rule,
  not a bug. (`ownerWithThreePetsGetsTenPercentOffRoundedUpToTheNextDollar`)
- The 10% discount doesn't apply to the Saturday surcharge. (`multiPetDiscountDoesNotApplyToTheSaturdaySurcharge`)
- A follow-up wipes out procedure charges too. (`followUpWipesOutProcedureCharges`)
- Hamsters, birds and pets with no type get no age discount or surcharge.
- A pet with no birth date throws `NullPointerException`. `PetValidator` requires a birth date, so
  this shouldn't happen through the app. (`petWithNoBirthDateThrows`)

## What `agent-refactor.diff` changes

The refactor really is nicer: named constants, a `switch`, small methods, `BigDecimal` for money,
`Locale.ROOT`. Its summary in the README says "No behavior changes." It changes three things.

| # | Change | Where in the diff | Before | After | Red tests |
|---|---|---|---|---|---|
| 1 | Rounding. Rounds to the cent (`HALF_UP`) instead of up to the next dollar. It hides inside "switched to BigDecimal for money," which sounds like a fix. | `return fee.setScale(2, RoundingMode.HALF_UP)` | 68.00 | 67.50 | `exoticExamIsTimeAndAHalfRoundedUpToTheNextDollar`, `typeNameIsNotCaseSensitive`, `neuteringAnExoticAddsOneHundredEightyToTheExoticExam`, `ownerWithThreePetsGetsTenPercentOffRoundedUpToTheNextDollar`, `multiPetDiscountDoesNotApplyToTheSaturdaySurcharge` |
| 2 | Senior boundary. Trusts the comment over the code: `SENIOR_AGE = 10` with `>=`. Every 10-year-old dog and cat now pays $15 more. The comment is gone, so the evidence is gone too. | `yield age >= SENIOR_AGE ? ...` | 45.00 | 60.00 | `tenYearOldDogPaysNoSeniorSurcharge` |
| 3 | Follow-ups. "Fixes" the odd behavior with an early `return` of zero, so the Saturday surcharge never runs. | `return BigDecimal.ZERO.setScale(2); // follow-ups are free` | 20.00 | 0.00 | `followUpOnSaturdayStillPaysTheSurcharge` |

Seven tests go red, for three changes. The minimum net that catches all three is three tests: any
fee with cents in it (an exotic exam, or the multi-pet discount), a 10-year-old dog or cat, and a
follow-up on a Saturday.

What it keeps on purpose, so some tests stay green: calendar-year age, Saturday only, the order of the
discount and the surcharge, the "follow" match on any description, and the procedure prices. A good
net goes red only where behavior changed.

Smaller things in the refactor the tests don't catch:

- The early return also skips the birth date. A follow-up for a pet with no birth date now returns
  $0 instead of throwing. (Not tested, because `petWithNoBirthDateThrows` uses a checkup.)
- `Locale.ROOT` changes the lowercasing only when the JVM's default locale is Turkish or Azerbaijani.
  Probably a real improvement. Still a change.
- It deleted the warning "Check with the front desk before you change any of the numbers." That line
  was the only hint that someone owns these prices.

## If a team's tests are all green on the refactor

Their net has holes. The usual gaps, from most to least common:

- They only used whole-dollar cases (dog, cat, hamster on a weekday). Rounding never shows.
- They tested 9 and 12 years old, never exactly 10 and 11. Boundaries are where refactors slip.
- They tested a follow-up, but only on a weekday.

Ask them to write the missing test and check it both ways. The README shows how with `git stash push
src/main`.

## The dry run (Oct 8): real agents did better than our diff

Two fresh Claude Code agents (Opus) got the Part 2 prompt word for word, on the legacy code. Nothing
else.

- **Agent A, no tests in the repo.** It wrote characterization tests on its own before touching the
  code (50 cases), then refactored. It kept `double` math on purpose, because "moving to `BigDecimal`
  math could change some fees." It kept senior at 11. It kept the $20 Saturday follow-up. It says it
  also compared old and new code on about 125,000 inputs with a throwaway test. Our 26 reference
  tests: all green on its refactor. Then we ran its 50 tests against `agent-refactor.diff`. 16 went
  red, and they caught all three changes. The agent's own net would have caught our bad refactor.
- **Agent B, our tests committed in the repo.** It found the tests and ran them before and after. It
  turned the "10 and up" comment into `SENIOR_MIN_AGE = 11`. That's the opposite of our diff: it
  trusted the code over the comment. Its PR said the odd behaviors "should change only if billing
  asks." Our 26 tests: all green on its refactor.

So expect some teams to say "my agent didn't break anything." That's a good outcome, not a failed
lab. Use it:

- **How do they know?** "The agent said so" is not evidence. "My tests are green on its refactor" is.
  The net is what turns a claim into a fact.
- **The good agents did the fundamentals.** Agent A wrote the net first, without being asked. That's
  chapter 6, done by a machine. Ask the room whose agent did that, and whose didn't.
- **Results vary** by tool, model, prompt and how much of the repo the agent sees. Some will break
  things the way `agent-refactor.diff` does. That's why Part 3 exists. The diff is the "agent on a
  bad day" every team can test against.
- **Push the point home:** an agent that skips the net and makes these three changes reads just as
  confident. You can't tell which kind you got from the summary.


## Debrief points

- **The build was green without the net.** All 81 PetClinic tests pass on the refactor. "Tests pass"
  only means the tests you have pass.
- **Readable is not the same as correct.** The refactor is easier to read. It also bills differently.
  Both are true. A reviewer reading the diff sees `BigDecimal` and nods.
- **The agent fixed what looked like bugs.** Two of the three changes are "fixes": the comment over
  the code, and the free follow-up. Nobody asked. That's the scout rule without a fence.
- **The summary said "No behavior changes."** Same lesson as Defuse the Grenade: read the diff, and
  run the net, not the summary.
- **A comment is a claim. The code is the evidence.** "Documentation can lie, but the code never does"
  (chapter 6). The agent believed the comment. The characterization test believed the code.
- **Pinning a bug is not approving it.** The test name records the oddity, so the next person knows
  it's on purpose. Fixing it is a separate change, with a person who owns billing, in its own commit.
- **Commit the net first.** Then `git diff --stat` shows at once if an agent touched your tests. The
  chapter's rule: a refactor doesn't change the tests.
- **Boundaries and rounding are where refactors slip.** Test 10 and 11, not 9 and 12. Use a case
  with cents in it.
- **The agent can write the net too.** Ask the room who used an agent in Part 1. Did it pin the odd
  behavior, or "fix" the code to match the comment? The prompt matters: "pin what it does today,
  including anything that looks like a bug."
- The lines for their AGENTS.md come straight from this debrief: tests before change, no silent
  fixes, never edit tests during a refactor, run the tests before and after.

## The reference tests

Also in `solution/VisitFeeCalculatorTests.java`. Copy it to `src/test/java/org/springframework/samples/petclinic/owner/` in PetClinic.
Use `isEqualByComparingTo`, not `isEqualTo`. `BigDecimal.equals` compares scale too, so `68.0` does
not equal `68.00`.

```java
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
```

# Net Before Refactor Lab

PetClinic has a new old class. `VisitFeeCalculator` works out what a visit costs. It came over from
the clinic's old front desk system. The billing export calls it every night. It has no tests.

Someone wants it cleaned up. An AI agent can do that in seconds. But how will you know the cleanup
didn't change what the clinic charges?

> "Your first goal is to document the existing behavior, not change it."
> (Fundamentals of Software Engineering, chapter 6)

Your job: build a safety net first. Then let the agent refactor, and see what your net catches.

## Setup (5 minutes, or before the workshop)

You need Git and Java 17 or newer. You don't need Docker for this lab.

```bash
git clone https://github.com/spring-projects/spring-petclinic.git
cd spring-petclinic
git checkout 500158f732419217507c7656904b8e6aa1bcc0d6
git switch -c net-before-refactor
git apply /path/to/workshop/net-before-refactor-lab/legacy-code.diff
git add src/main
git commit -m "Add the legacy visit fee calculator"
```

The commit matters. It gives you a clean point to come back to.

Open `src/main/java/org/springframework/samples/petclinic/owner/VisitFeeCalculator.java`. That's the
whole lab. It's under 90 lines.

## What characterization tests are

A characterization test records what the code does today. Not what it should do. Not what the
comment says. What it actually does.

Michael Feathers named them in *Working Effectively with Legacy Code*. Chapter 6 calls the
same idea test-driven refactoring:

1. Write tests that pin down the current behavior.
2. Run them. They must pass on the old code.
3. Refactor.
4. Run them again. They must still pass. You don't change the tests.

The hard part is step 1. You'll find things that look like bugs. Don't fix them. Write a test that
pins them down, give it a name that says what's odd, and ask someone later. Somebody might depend on
that bug.

## The lab (20 minutes)

Work in pairs or teams of three.

### Part 1: Read and characterize (9 min)

Read `VisitFeeCalculator` first. Then write tests in
`src/test/java/org/springframework/samples/petclinic/owner/VisitFeeCalculatorTests.java`.

Here's a starter with one test and the setup you need:

```java
package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VisitFeeCalculatorTests {

	private static final LocalDate WEDNESDAY = LocalDate.of(2026, 10, 14);

	private final VisitFeeCalculator calculator = new VisitFeeCalculator();

	@Test
	void adultDogPaysTheBaseExamFee() {
		Pet pet = pet("dog", LocalDate.of(2020, 5, 1));
		Visit visit = visit(WEDNESDAY, "checkup");

		assertThat(calculator.feeFor(ownerOf(pet), pet, visit)).isEqualByComparingTo("45.00");
	}

	private static Pet pet(String typeName, LocalDate birthDate) {
		PetType type = new PetType();
		type.setName(typeName);
		Pet pet = new Pet();
		pet.setName("Rex");
		pet.setType(type);
		pet.setBirthDate(birthDate);
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

Run it:

```bash
./mvnw spring-javaformat:apply
./mvnw test -Dtest=VisitFeeCalculatorTests
```

The build checks formatting. The first line fixes it for you, so run it before every test run.

Now add tests. Things to vary:

- The pet type: dog, cat, snake, lizard, bird, hamster
- The pet's age, right at each boundary
- The visit description: a rabies shot, spaying, neutering, a follow-up
- How many pets the owner has
- The day of the week
- Anything that combines two of these

A trick that works: guess the answer, write the assert, and run it. If it fails, the failure message
shows what the code really returns. If the real value surprises you, keep it. Put the real value in
the test, and name the test after the surprise.

You can write the tests by hand, or have your agent help. If you use an agent, try this prompt, and
watch what it does with the odd parts:

> Write characterization tests for VisitFeeCalculator in VisitFeeCalculatorTests. Pin down what the
> code does today, including anything that looks like a bug. Don't change VisitFeeCalculator. If
> something looks wrong, write a test for the current behavior and tell me about it.

When your tests pass, commit them:

```bash
git add src/test
git commit -m "Characterization tests for VisitFeeCalculator"
```

Now you can see if anyone touches them.

### Part 2: Let your agent refactor it (4 min)

Give your agent this prompt. Don't mention your tests. We want to see what it does on its own.

> Refactor VisitFeeCalculator so it's easier to read and change. Keep the behavior the same.

When it's done:

1. Read its summary. Does it say the behavior is the same?
2. Run `git diff --stat`. Did it touch only the calculator? Did it touch your tests?
3. Run your tests yourself: `./mvnw spring-javaformat:apply` then
   `./mvnw test -Dtest=VisitFeeCalculatorTests`.

For each red test, decide: did the refactor change behavior, or is the test wrong?

All green? Good. How would you know that without your tests? Then do Part 3 too.

### Part 3: No agent? Use ours (2 min)

No AI tool, or want a second opinion? We saved a refactor an agent might write. It reads much
nicer. Its summary said:

> Refactored VisitFeeCalculator for readability. Pulled the magic numbers into named constants and
> split the logic into small methods. Switched to BigDecimal for money. No behavior changes.

Make sure your tests are committed (end of Part 1). Then set your agent's version aside, if you
have one, apply ours, and run your tests:

```bash
git stash --include-untracked
git apply /path/to/workshop/net-before-refactor-lab/agent-refactor.diff
./mvnw test -Dtest=VisitFeeCalculatorTests
```

Read the diff while the tests run: `git diff src/main`. Later, `git restore src/main` and then
`git stash pop` bring your agent's version back.

Did your net catch every change? If everything is green, your net has a hole. Find the change in the
diff that your tests missed. Write the test that would have caught it. Then check it both ways:

```bash
git stash push src/main                      # back to the old code
./mvnw test -Dtest=VisitFeeCalculatorTests   # the new test must pass
git stash pop                                # the refactor again
./mvnw test -Dtest=VisitFeeCalculatorTests   # the new test must fail
```

### Part 4: Debrief (5 min)

Your facilitator will walk through what changed. Talk about:

- How many of the odd behaviors did you find? Which one surprised you most?
- Did you write a test for something that looked like a bug? How did that feel?
- What did the agent's refactor change? Did its summary say so?
- Did your agent run your tests without being asked? Did it change any tests?
- If you used an agent in Part 1, did it pin the odd behavior or "fix" it?
- The refactor really is easier to read. Is it better? Who gets to decide?
- Some of the changes might be what the clinic wants. Who do you ask, and when?
- Without your tests, how would this refactor have been reviewed? Would you have caught it?

## Add to your AGENTS.md

Write three to five lines your agent should follow next time. For example:

```markdown
## Changing existing code
- Never change code that has no tests. Write characterization tests first.
- Characterization tests pin what the code does today, not what it should do.
- Don't fix behavior you think is a bug without asking. Write a test that pins it, and tell me.
- A refactor never changes tests. If a test goes red, stop and ask.
- Run the tests before and after every refactor, and show me the results.
```

## Answers

Your facilitator has the answer key. Look at it only after Part 4.

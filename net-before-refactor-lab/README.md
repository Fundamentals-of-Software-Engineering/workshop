# Net Before Refactor Lab

PetClinic has a new old class. `VisitFeeCalculator` works out what a visit costs. It came over from
the clinic's old front desk system. The billing export calls it every night. It has no tests.

Someone wants it cleaned up. An AI agent can do that in seconds. But how will you know the cleanup
didn't change what the clinic charges?

> "Your first goal is to document the existing behavior, not change it."
> (Fundamentals of Software Engineering, chapter 6)

Your job: build a safety net first. Then let an agent refactor, and see what your net catches.

## Setup

You need the PetClinic clone from [Before the workshop](../README.md#before-the-workshop). You don't
need Docker.

```bash
git status
git switch net-before-refactor
```

If `git status` showed changes from an earlier lab, run `git stash -u` before you switch.

## Characterization tests

A characterization test records what the code does today, not what it should do. If something looks
like a bug, don't fix it. Pin it with a test, and name the test after the surprise. Somebody might
depend on that bug.

## The lab

Work alone or in pairs.

### Part 1: Build the net

Read `src/main/java/org/springframework/samples/petclinic/owner/VisitFeeCalculator.java`. It's under
90 lines.

Then open `VisitFeeCalculatorTests.java` in the same package under `src/test`. It has one test and
the helpers you need. Run it:

```bash
./mvnw spring-javaformat:apply test -Dtest=VisitFeeCalculatorTests
```

The build checks formatting, so this one line formats your code and runs the tests. Use it every
time.

Now add tests. Vary these:

- The pet type: dog, cat, snake, lizard, bird, hamster
- The pet's age, right at each boundary
- The visit description: a rabies shot, spaying, neutering, a follow-up

A trick that works: guess the answer, write the assert, and run it. If it fails, the message shows
what the code really returns. If the real value surprises you, keep it, and name the test after the
surprise.

You can have your agent help. If you do, watch what it does with the odd parts:

> Write characterization tests for VisitFeeCalculator in VisitFeeCalculatorTests. Pin down what the
> code does today, including anything that looks like a bug. Don't change VisitFeeCalculator.

When your tests pass, commit them:

```bash
git add src/test
git commit -m "Characterization tests for VisitFeeCalculator"
```

If Git asks who you are, run the two `git config` lines it shows you, then commit again.

### Part 2: Refactor, then run your net

Pick one:

- **With an agent.** Start a fresh session. Don't mention your tests. Give it this prompt, and say no
  if it asks to commit:

  > Refactor VisitFeeCalculator so it's easier to read and change. Keep the behavior the same.

- **Without one.** Use the refactor we saved:

  ```bash
  git restore --source origin/net-before-refactor-agent src/main
  ```

Then run your tests with the same one line as before. Which ones went red? For each one, did the
refactor change behavior, or is your test wrong?

All green? Read the diff (`git diff src/main`). Your net has a hole somewhere. Find the change your
tests missed.

## Debrief

- What did the refactor change? Did its summary say so?
- Did you pin something that looked like a bug? How did that feel?
- Without your tests, would anyone have caught this in review?

A refactor that changes behavior isn't a refactor.

## Take it further

- Write the test that would have caught the change your net missed. It should pass on the old code
  and fail on the refactor.
- Try the other refactor. Run `git restore src/main` to go back to the old code, then try the path
  you didn't pick in Part 2.
- Read *Working Effectively with Legacy Code* by Michael Feathers. He named characterization tests.

## Answers

The odd behaviors worth pinning, and every change the refactor made:
[ANSWERS.md on the `solution` branch](https://github.com/Fundamentals-of-Software-Engineering/workshop/blob/solution/net-before-refactor-lab/ANSWERS.md).

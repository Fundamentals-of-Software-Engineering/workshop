# Spec It Before You Prompt: facilitator notes

The lab lives in `spec-it-before-you-prompt-lab/` (README, spec-template,
rubric, example-spec). Base: spring-petclinic `500158f732419217507c7656904b8e6aa1bcc0d6` (Sept 29,
2026, Spring Boot 4.1). Everything below was checked on Java 27 on Oct 8, 2026.

## The point

The fundamentals didn't change. The ratio did. Writing the code is now the cheap part. Deciding
what "done" means is the expensive part, and the agent can't do it for you.

- English is vague. "Track their health over time" hides a product decision.
- A spec states intent. It doesn't compile. A test enforces intent. "Diagrams don't compile.
  Neither do specs. Tests do."
- So write the failing test before the prompt, then hand the agent the green step.

## The traps in this feature (all verified)

A naive version of the feature was built on purpose to check each one.

| # | Trap | What happens | How it was checked |
|---|---|---|---|
| 1 | "Track over time" | One `weight` column holds only the latest value. No history. | Reading the request |
| 2 | Positional seed inserts | Add the column to `db/h2/schema.sql` only, and the app won't start: "Column count does not match" in `db/h2/data.sql`. MySQL's `data.sql` is positional too. | Ran it |
| 3 | `PetController` sets `PetValidator` with `@InitBinder("pet")` | Bean Validation on `Pet` (say `@Positive`) doesn't run on the form. Hibernate checks it on save. A negative weight gives an HTTP 500. | Ran it |
| 4 | `updatePetDetails` copies fields by hand | Forget weight there and edits are lost silently. The edit "works" and redirects. | Ran it |
| 5 | Mocks hide trap 4 | A `@WebMvcTest` with a mocked `OwnerRepository` passes even with the copy missing. Both `@ModelAttribute` methods get the same stubbed owner, so the bound pet is the saved pet. The real app loads them separately. | Wrote both tests, removed the copy line: mock test green, full-stack test red |
| 6 | `I18nPropertiesSyncTest` | Add `weight=Weight` to `messages.properties` only and the test fails for 9 language files. | Ran it |
| 7 | No message for bad numbers | `weight=abc` shows "Failed to convert property value of type 'java.lang.String' to required type 'java.math.BigDecimal'..." on the form. Needs a `typeMismatch.weight` key. | Ran it |
| 8 | Null display | `${pet.weight} + ' kg'` shows "null kg" for every existing pet. | Ran it |
| 9 | Existing MySQL and Postgres databases | Their `schema.sql` uses `CREATE TABLE IF NOT EXISTS` and `ddl-auto=none`, so an existing database never gets the column. | Read the scripts. Not run against an old database. |
| 10 | Locale | A text input lets a German user type `4,5`, which fails to convert. A number input sends `4.5`. | Ran `4,5` against a text input |
| 11 | The formatter | Hand-written or pasted Java fails `spring-javaformat:validate` before any test runs. Run `./mvnw spring-javaformat:apply`. | Ran it |

Not traps, but people ask: PetClinic has no pet API (the only JSON endpoint is `/vets`), no
migration tool, and no login.

## The dry run (Oct 8)

A fresh Claude agent got the one-line prompt in plan mode, with the repo at the pinned commit. It
took about a minute. The plan was good:

- It asked the history question first and offered three options: current weight, weight per visit,
  or a separate table.
- It caught traps 2, 4, 6, 7, 9 and the number input. It chose `BigDecimal` and `PetValidator`. It
  named the earlier commit that fixed a similar 500 for owner fields.
- It asked about existing MySQL databases, units and the upper limit.

Where it fell short:

- **Its regression test for trap 4 can't catch trap 4.** It planned the edit test in
  `PetControllerTests`, the mock-based style. That is exactly the test that passes with the bug
  (trap 5). The plan named the right risk and the wrong gate.
- It put validation in `PetValidator` without saying why. Someone who copies its plan to another
  form might reach for `@Positive` and get trap 3.
- Scope creep: it offered to give seed pets sample weights. It also planned to touch all 11
  message files, but `messages_en.properties` is empty on purpose (it falls back to the base file).

Rubric score for the dry run: about 22 of 24. Expect the room's agents to vary a lot. Tools that
don't read the whole repo, or that skip plan mode, will miss more.

**Lean into it.** A good agent with the repo plans well even from one line. So what did the spec
writer add?

1. **The answers.** The agent asked "history or current weight?" Someone has to know. A spec is
   where you decide before the agent asks, or where you write down the answer.
2. **The judgment to grade the plan.** You could only tell the plan was good because you knew the
   code.
3. **The gate.** The agent's own test would have passed with the bug in it. The spec's acceptance
   tests go through the real stack. They fail until the feature works.

## What good looks like

A reference build is in `reference.diff` in this folder. Apply it to PetClinic with `git apply`. It touches 22 files:

- `Pet`: `@Column(precision = 5, scale = 2) private BigDecimal weight;` with a getter and setter.
  No Bean Validation annotations.
- `PetValidator`: when weight isn't null, reject it if it's 0 or less, over 999.99, or has more
  than two decimals.
- `PetController.updatePetDetails`: one line, `existingPet.setWeight(pet.getWeight());`.
- `fragments/inputField.html`: a `number` case with `step="0.01"`. The pet form uses it.
- `ownerDetails.html`: `th:text="${pet.weight != null} ? ${pet.weight} + ' kg' : ''"`.
- `weight` and `typeMismatch.weight` in all 10 message files that need them (English text in the
  translations for now).
- `schema.sql` for H2, MySQL and Postgres. Postgres also gets
  `ALTER TABLE pets ADD COLUMN IF NOT EXISTS weight NUMERIC(5,2);`. `NULL` added to each pet row
  in the H2 and MySQL `data.sql`.

Results:

| Run | `PetWeightTests` (11) | Notes |
|---|---|---|
| Pinned base | 10 fail, 1 passes | Compiles. The one that passes guards the old behavior. |
| Naive build (traps 3, 4, 7, 8 left in) | 8 of 10 failed | Run before the clear test was added |
| Reference | 11 pass | Full suite: 92 tests, 0 failures, including MySQL (Testcontainers) and Postgres (Docker Compose) |

In the running app: the form has a number input, Leo edited to 5.2 shows "5.20 kg", a pet without
a weight shows nothing, -1 shows "Weight must be between 0.01 and 999.99 kg", and "heavy" shows
"Weight must be a number, like 4.5".

## Running the lab

- **Timing.** The one-liner finishes in 2 minutes and may start building. That's realistic. Let
  them. If their agent builds, have them run `./mvnw test -Dtest='Pet*Tests'`. The existing tests
  pass on a broken build. Good debrief material.
- **The spec writer will feel slow.** That's the trade. Ask in the debrief whether 7 minutes of
  thinking was worth it.
- **No agent?** One laptop per pair. Run the one-liner first, then the spec, in fresh sessions.
- **Plan mode.** Claude Code has it built in. For other tools, the prompt says "Don't write code
  yet."

## Debrief points

- "Track health over time." Show of hands: whose agent asked? Whose built a history table without
  asking? Neither answer is wrong. Not deciding is wrong.
- Show the example spec's edge cases. Ask which ones each pair's spec had. Existing data and the
  edit path are the ones people miss.
- Trap 5 is the Gates lesson. Coverage is a map, not a grade. A test can pass for the wrong reason.
  Ask: "Your agent wrote a test for the edit bug. Would it have caught it?"
- Show `PetWeightTests` going red on the base, then green. "Write the failing test before the
  prompt."
- Model-driven development, the sequel: if the spec is the source of truth and the agent
  regenerates code from it, where do hand fixes go? Pick one source of truth. Here it's the tests.
- The lines for their AGENTS.md come from what their one-line plan missed.

## Live demo (Dan, How I do this now)

1. Plan mode with the one-liner. Point out its questions.
2. Answer them by pasting the example spec.
3. Drop in `PetWeightTests`, run it red.
4. "Make PetWeightTests pass. Don't change the tests. Then run the full suite and tell me what you
   checked and what you didn't."
5. Review the diff against the trap table. Look for `updatePetDetails` first.

Backup: the reference diff above, if the live run goes sideways.

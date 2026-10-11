# Spec It Before You Prompt Lab

The clinic wants a new feature for Spring PetClinic:

> Add an optional weight field (in kg) to pets so we can track their health over time.

That sentence sounds clear. It isn't. In this lab two people give the same feature to an agent. One
uses that sentence as the prompt. The other writes a short spec first. Then you compare what the
agent planned.

English is a great way to describe what you want. It's also vague. A spec says what you mean. A test
proves the code does it.

## Setup (before the lab, or before the workshop)

You need Git, Java 17 or newer, and an AI coding assistant. Plan mode (or "don't write code yet")
is fine. You don't need to generate code to do this lab.

Use the PetClinic clone from [Before the workshop](../README.md#before-the-workshop), on the
`main` branch:

```bash
git status
git switch main
```

If `git status` showed changes from an earlier lab, run `git stash -u` before you switch.

Each person starts a fresh agent session, so neither agent has seen the other's prompt.

## The lab

Work in pairs. Decide who is **One-liner** and who is **Spec writer**. Both of you plan the same
feature. Only the prompt is different.

No agent? Pair with someone who has one and share it. Run the one-liner first. Then start a fresh
session for the spec. Keep the spec file out of the PetClinic folder, so the first agent can't
read it.

### Part 1: Two prompts

**One-liner.** Start now. Give your agent exactly this, in plan mode:

> Add an optional weight field (in kg) to pets so we can track their health over time.

No plan mode in your tool? Add one line after it: "Make a plan. Don't write code yet."

Answer any questions it asks, as best you can. Read its plan. Write down:

- Every assumption it made that you didn't tell it
- Every file it plans to change
- Any question it asked you

Then, if you like, let it build. If it builds, run the tests: `./mvnw test -Dtest='Pet*Tests'`.

**Spec writer.** Don't prompt yet. Fill in `spec-template.md`. Keep it short.
Bullet points are fine. Look at the code when you need to. The two most important sections:

- **Edge cases.** What about the pets that already exist? What's a valid weight? What happens when
  someone edits a pet?
- **Acceptance criteria, written as tests.** Name each test and say what it checks. The test name
  is the requirement.

Fill in those two first. Do the rest if you have time. Don't open `example-spec.md` yet. It's a
finished spec, and you'll see it in the debrief.

Then give your agent the spec, in plan mode:

> Here is a spec for a change to this codebase. Read it, then read the code it touches. Make a
> plan. Don't write code yet. Ask me about anything that is unclear or that the spec gets wrong.
>
> (paste your spec)

### Part 2: Compare

Put the two plans side by side. If an agent built code, look at the diff too. Score both with
`rubric.md`. Go fast. A quick score is fine. Then answer:

- What did the one-line plan assume that the spec decided?
- What did the spec miss that the agent caught?
- Which plan would you rather review as a pull request?
- How long did each one take, end to end?

### Part 3: Debrief

Your facilitator will show an example spec and the tests that go with it. Talk about:

- "Track their health over time." Did either agent build weight history? Should it?
- Which edge cases would have reached production? How would you have found out?
- Diagrams don't compile. Neither do specs. Tests do. Which of your spec lines could become a test?
  Which couldn't?
- Did your agent follow the patterns already in the codebase, or invent its own?
- When is a one-line prompt good enough? When does it cost you more than it saves?

## Stretch: write the failing test before the prompt

After the debrief, open `example-spec.md`. It ends with a test class, `PetWeightTests`. It compiles
against PetClinic and fails until the feature works.

We put the spec and its tests on a branch for you. Set aside any changes an agent made, then switch:

```bash
git stash -u
git switch spec-it-stretch
```

`git stash -u` says "No local changes to save" if there's nothing to set aside. That's fine.

1. `example-spec.md` is in the PetClinic folder now, so your agent can read it. The tests are in
   `src/test/java/org/springframework/samples/petclinic/owner/PetWeightTests.java`.
2. Run `./mvnw test -Dtest=PetWeightTests`. Ten of the 11 tests fail. That's the red step.
3. Give your agent the green step:

   > Make PetWeightTests pass. Follow example-spec.md. Don't change the tests. Then run the full
   > test suite and tell me what you checked and what you didn't.

4. Review the diff. Do the tests pass for the right reasons?

The full test suite needs Docker for the MySQL and Postgres tests. Without Docker, run
`./mvnw test -Dtest='!MySqlIntegrationTests,!PostgresIntegrationTests'`.

## Files

These are in this lab's folder on GitHub, not in your PetClinic clone. Open them in your browser.

| File | What it's for |
|---|---|
| `spec-template.md` | The blank spec. Copy it and fill it in. |
| `rubric.md` | Score both plans in Part 2. |
| `example-spec.md` | A finished spec for the weight feature. Don't open it until the debrief. |

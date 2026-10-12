# Spec It Before You Prompt Lab

The clinic wants a new feature for Spring PetClinic:

> Add an optional weight field (in kg) to pets so we can track their health over time.

That sentence sounds clear. It isn't. In this lab two people give the same feature to an agent. One
uses that sentence as the prompt. The other uses a spec. Then you compare what the agent planned.

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

Work in pairs. Decide who is **One-liner** and who is **Spec**. Both of you plan the same
feature. Only the prompt is different.

No agent? Pair with someone who has one and share it. Run the one-liner first. Then start a fresh
session for the spec. Don't save the spec in the PetClinic folder, so the first agent can't read
it.

### Part 1: Two prompts

**One-liner.** Start now. Give your agent exactly this, in plan mode:

> Add an optional weight field (in kg) to pets so we can track their health over time.

No plan mode in your tool? Add one line after it: "Make a plan. Don't write code yet."

Answer any questions it asks, as best you can. Read its plan. Notice what it assumed that you never
told it, and whether it asked you anything before planning.

Then, if you like, let it build. If it builds, run the tests: `./mvnw test -Dtest='Pet*Tests'`.

**Spec.** We wrote the spec for you: [`example-spec.md`](example-spec.md). Read it first, and
notice what it decides that the one-liner leaves open.

Want to write your own instead? Use the blank [`spec-template.md`](spec-template.md). Fill in the
edge cases and the tests, and skip the rest.

Then give your agent the spec, in plan mode:

> Here is a spec for a change to this codebase. Read it, then read the code it touches. Make a
> plan. Don't write code yet. Ask me about anything that is unclear or that the spec gets wrong.
>
> (paste the spec)

### Part 2: Compare

Put the two plans side by side. If an agent built code, look at the diff too. Talk it through:

- What did the one-line plan assume that the spec decided?
- What did the agent catch that the spec missed?
- Which plan would you rather merge?

### Part 3: Debrief

Your facilitator will walk through what the spec decided. Talk about:

- "Track their health over time." Did either agent build weight history? Should it?
- Which edge cases would have reached production? How would you have found out?
- When is a one-line prompt good enough? When does it cost you more than it saves?

## Learn more

Dan's video on the middle ground between vibe coding and full specs:
[Vibe Coding vs Spec-Driven Development: The Solo Dev Middle Ground](https://www.youtube.com/watch?v=1xyHQj8pwwo).

## Answers

What one-line prompts usually miss, and what a good spec covers:
[ANSWERS.md on the `solution` branch](https://github.com/Fundamentals-of-Software-Engineering/workshop/blob/solution/spec-it-before-you-prompt-lab/ANSWERS.md).

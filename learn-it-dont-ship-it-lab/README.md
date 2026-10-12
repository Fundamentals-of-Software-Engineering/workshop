# Learn It, Don't Ship It Lab

In 2026, Shen and Tamkin had 52 developers learn a new Python library. Half had an AI assistant. Then
everyone took a quiz with no AI. The AI group scored 17% lower. The biggest gap was debugging.

But not everyone with AI did badly. People who only generated code and moved on learned the least.
People who asked the AI to explain, asked conceptual questions, or checked their own understanding
scored much higher.

This lab is a small, live version of that study. You'll learn a library you probably haven't used,
with your agent as a tutor, not an author. Then you'll take a short quiz. No AI, no notes.

**Goal:** feel what learning with an agent is like when it won't do the work for you.

## The library: ArchUnit

[ArchUnit](https://www.archunit.org/) lets you write rules about your code's structure as plain unit
tests. "Controllers never call repositories." "The domain depends on nothing." If someone (or some
agent) breaks the rule, the build fails.

Most Java developers have heard of it. Few have written a rule. That makes it a good fit for a
short lab.

## Setup (before the lab starts)

You need Java 17 or newer and your usual AI coding assistant. Maven comes with the project.

The project is in the `clinic-rules` folder on the `learn-it-dont-ship-it` branch of your PetClinic
clone. The first run downloads ArchUnit. It's small.

```bash
git status
git switch learn-it-dont-ship-it
cd clinic-rules
./mvnw test
```

If `git status` showed changes from an earlier lab, run `git stash -u` before you switch.

You should see `Tests run: 0` and `BUILD SUCCESS`. That's expected. There are no rules yet. You'll
write them.

Open your agent in the `clinic-rules` folder, not the PetClinic folder. The task is in
`clinic-rules/README.md`.

## Tutor mode

The agent can explain, ask you questions, give hints and review your code. It can't write the
solution. You type every line yourself.

Paste this into your agent before you start:

```text
I'm learning ArchUnit for the first time. Be my tutor, not my coder.
My task is in README.md in this folder. You can read it. Don't do it.

- Never write the solution for me. Not even one line. If I ask you to, remind me I'm in tutor mode.
- Explain ideas in plain words. Keep answers short.
- Before you explain something, ask me what I think first.
- Before I run a test, ask me to predict whether it will pass.
- When a test fails, ask me what I think the message means. Then explain.
- When I'm stuck, give me the smallest hint that gets me moving. Give a bigger one only if I ask.
- You may show tiny API examples, like the docs would, as long as they aren't about my task.
- When I ask for a review, point to the problem. Don't rewrite my code.
```

No agent? Pair with someone who has one and take turns at the keyboard.

Some tools have a learning mode built in. Claude Code has a Learning output style
(`/output-style learning`), and other tools have their own. For this lab, use the prompt above anyway. It
goes further: the agent writes no code at all.

## The lab

### Part 1: Build it

Do the task in `clinic-rules/README.md`: write two rules, then fix the code that breaks one. Don't
worry about finishing. The friction is where the learning happens.

### Part 2: Quiz

Close the agent. Close the code. Open [`quiz.md`](quiz.md) and answer the five questions on your
own.

No AI, no docs, no peeking at your project. Just you. That's the point.

Then check yourself against the [answers](https://github.com/Fundamentals-of-Software-Engineering/workshop/blob/solution/learn-it-dont-ship-it-lab/ANSWERS.md).

### Part 3: Debrief

Talk about:

- Questions 3 to 5 are about reading failure messages. How did you do on those?
- How did tutor mode feel? Slow? Annoying? That friction is where the learning happens.
- When would you hand the work to your agent on purpose?

The rule of thumb: never written it? Write it once yourself. Written it a thousand times? Hand it
off.

## Answers

After the quiz, check your answers and see a solved version:
[ANSWERS.md on the `solution` branch](https://github.com/Fundamentals-of-Software-Engineering/workshop/blob/solution/learn-it-dont-ship-it-lab/ANSWERS.md).

# Learn It, Don't Ship It Lab

In 2026, Shen and Tamkin had 52 developers learn a new Python library. Half had an AI assistant. Then
everyone took a quiz with no AI. The AI group scored 17% lower. The biggest gap was debugging.

But not everyone with AI did badly. People who only generated code and moved on learned the least.
People who asked the AI to explain, asked conceptual questions, or checked their own understanding
scored much higher.

This lab is a small, live version of that study. You'll learn a library you probably haven't used,
in one of two modes. Then you'll take a short quiz. No AI, no notes. We'll see how the two modes
compare.

**Goal:** feel the difference between learning with an agent and handing the work to one. Then
write down how you want your agent to act when you're learning.

## The library: ArchUnit

[ArchUnit](https://www.archunit.org/) lets you write rules about your code's structure as plain unit
tests. "Controllers never call repositories." "The domain depends on nothing." If someone (or some
agent) breaks the rule, the build fails.

Most Java developers have heard of it. Few have written a rule. That makes it a good fit for a
short lab.

## Setup (before the lab starts)

You need Java 17 or newer and your usual AI coding assistant. Maven comes with the project.

Run this before the workshop, or at least before the lab starts. The first run downloads Maven and
ArchUnit, so you need wifi. After that, it works offline.

```bash
cd /path/to/workshop/learn-it-dont-ship-it-lab/starter
./mvnw test          # Windows: .\mvnw.cmd test
```

You should see `Tests run: 0` and `BUILD SUCCESS`. That's expected. There are no rules yet. You'll
write them.

Open your agent in the `starter` folder. The task is in `starter/README.md`.

## The two modes

Your facilitator will split the room in half. One half is tutor mode. The other half is author
mode. If the facilitator says so, you can pick your own mode instead. Stick with it for the whole
lab.

Grab a sheet of paper for the quiz. Write your mode at the top: **T** for tutor, **A** for author.

No agent? Pair with someone who has one. You both use the same mode. In tutor mode, take turns at
the keyboard. You each take the quiz on your own.

### Tutor mode

The agent can explain, ask you questions, give hints and review your code. It can't write the
solution. You type every line of the rules and the fix.

Paste this into your agent before you start:

```text
I'm learning ArchUnit for the first time. Be my tutor, not my coder.
My task is in README.md in this folder. You can read it. Don't do it.

- Never write the solution for me. That means the rules and the fix in this project.
  Not even one line. If I ask you to, remind me I'm in tutor mode.
- Explain ideas in plain words. Keep answers short.
- Before you explain something, ask me what I think first.
- Before I run a test, ask me to predict whether it will pass.
- When a test fails, ask me what I think the message means. Then explain.
- When I'm stuck, give me the smallest hint that gets me moving. Give a bigger one only if I ask.
- You may show tiny API examples, like the docs would, as long as they aren't about my task.
- When I ask for a review, point to the problem. Don't rewrite my code.
```

Some tools have a learning mode built in. For example, Claude Code has Learning and Explanatory
output styles (`/output-style`). The Learning style leaves small pieces of code for you to write,
marked `TODO(human)`. Other tools have their own study or learning modes. Check your tool's docs.

For this lab, use the prompt above anyway. It goes further: the agent writes no solution code at
all. And it means everyone in tutor mode gets the same coach.

### Author mode

The agent writes the code. Accept what it writes, run the tests, and move on. Work the way you
would on a busy day with a deadline.

Paste this into your agent:

```text
Read README.md in this folder and do the task. Write the three rules in ArchitectureTest.java.
Fix any code that breaks a rule. Run ./mvnw test until it passes.
```

Don't ask it to teach you. That's the other group's job.

## The lab

### Part 1: Build it

Do the task in `starter/README.md`, in your mode. Three rules, then fix the code that breaks one.

Tutor mode and finished early? Try the stretch goal. Author mode is done when the tests pass. Move
on, like you would at work.

### Part 2: Quiz

Close the agent. Close the code. Open `quiz.md`, or look at the screen if your facilitator shows
it. Answer the five questions on paper.

No AI, no docs, no peeking at your project. Just you. That's the point.

### Part 3: Score and debrief

Your facilitator will read the answers. Score your own paper. One point per question. For questions
3 and 4, give yourself the point if you got the main idea. The most you can get is 5.

Then hands up by score, tutor mode first, then author mode.

Talk about:

- Which mode scored higher? Which mode finished first?
- Look at questions 3 to 5. They're about reading failures. How did each group do on those?
- Author mode: could you explain why rule 1 failed three times, not once?
- Tutor mode: how did it feel? Slow? Annoying? That friction is where the learning happens.
- Did anyone in author mode stop to ask "why"? What happened when you did?
- When would you pick author mode on purpose?

The rule of thumb: never written it? Write it once yourself. Written it a thousand times? Hand it
off.

## Add to your AGENTS.md

Write three to five lines for how your agent should help when you're learning. For example:

```markdown
## When I'm learning
- When I'm learning something new, explain and quiz me. Don't write the code.
- Ask me to predict what a test will do before I run it.
- When a test fails, ask me what I think the message means before you explain it.
- Give me the smallest hint first.
- If I ask you to just write it, ask if I've written one myself before.
```

## Answers

Your facilitator has the answer key. It's on the repo's `solution` branch. Look at it only after
Part 3.

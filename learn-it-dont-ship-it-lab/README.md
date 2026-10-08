# Learn It, Don't Ship It Lab

In 2026, Shen and Tamkin had 52 developers learn a new Python library. Half had an AI assistant. Then
everyone took a quiz with no AI. The AI group scored 17% lower. The biggest gap was debugging.

But not everyone with AI did badly. People who only generated code and moved on learned the least.
People who asked the AI to explain, asked conceptual questions, or checked their own understanding
kept most of what they learned.

This lab is a small, live version of that study. You'll learn a library you probably haven't used,
in one of two modes. Then you'll take a short quiz. No AI, no notes. We'll see how the two modes
compare.

## The library: ArchUnit

[ArchUnit](https://www.archunit.org/) lets you write rules about your code's structure as plain unit
tests. "Controllers never call repositories." "The domain depends on nothing." If someone (or some
agent) breaks the rule, the build fails.

Most Java developers have heard of it. Few have written a rule. That makes it a good fit for 12
minutes.

## Setup (2 minutes, or before the workshop)

You need Java 17 or newer and your usual AI coding assistant. Maven comes with the project.

```bash
cd /path/to/workshop/learn-it-dont-ship-it-lab/starter
./mvnw test          # Windows: mvnw.cmd test
```

You should see `Tests run: 0` and `BUILD SUCCESS`. That's expected. You'll write the tests.

Open `starter/README.md`. It has the task.

## The two modes

Your facilitator will split the room. Or each pair picks a mode. Stick with it for the whole lab.

### Tutor mode

The agent can explain, ask you questions, give hints and review your code. It can't write the
solution. You type every line of the rules and the fix.

Paste this into your agent before you start:

```text
I'm learning ArchUnit for the first time. Be my tutor, not my coder.

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

The agent writes the code. Give it the task from `starter/README.md` and let it work. Accept what it
writes, run the tests, and move on. Work the way you would on a busy day with a deadline.

Don't ask it to teach you. That's the other group's job.

## The lab (20 minutes)

### Part 1: Build it (12 min)

Do the task in `starter/README.md`, in your mode. Three rules, then fix the code that breaks one.

Finished early? Try the stretch goal. In author mode, you're done when the tests pass.

### Part 2: Quiz (4 min)

Close the agent. Close the code. Open `quiz.md` and answer the five questions on paper.

No AI, no docs, no peeking at your project. Just you. That's the point.

### Part 3: Score and debrief (4 min)

Your facilitator will read the answers. One point per question. Then hands up by score, tutor mode
first, then author mode.

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

Your facilitator has the answer key. Look at it only after Part 3.

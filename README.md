# Fundamentals of Software Engineering: Workshop Labs

Hands-on labs for the Fundamentals of Software Engineering workshop by Dan Vega and Nate Schutta.
They go with the book *Fundamentals of Software Engineering* (O'Reilly).

The workshop's idea: **the fundamentals didn't change. The ratio did.** You write less code. You
read, specify, review and decide more. These labs practice those parts of the job, with and without
an AI coding agent.

## Dev2Next 2026

"Fundamentals of Software Engineering in the Age of AI." Monday, October 12, 2026.

Most labs use one codebase all day: [Spring PetClinic](https://github.com/spring-projects/spring-petclinic),
pinned to commit `500158f732419217507c7656904b8e6aa1bcc0d6` (September 29, 2026). By the afternoon
you'll know it well. Each lab follows the same shape: do it yourself first, then with your agent,
then compare.

| # | Lab | Time | What you do |
|---|---|---|---|
| 1 | [Read before you prompt](read-before-you-prompt-lab/) | 25 min | Read PetClinic with no AI, then ask your agent the same questions and check its answers. |
| 2 | [Defuse the grenade](defuse-the-grenade-lab/) | 25 min | Review a pull request an agent wrote. Find the problems before they ship. |
| 3 | [Spec it before you prompt](spec-it-before-you-prompt-lab/) | 20 min | Plan the same feature twice, from a one-line prompt and from a short spec. Compare. |
| 4 | [Net before refactor](net-before-refactor-lab/) | 20 min | Pin down what old code does with tests, then let an agent refactor it. |
| 5 | [Learn it, don't ship it](learn-it-dont-ship-it-lab/) | 20 min | Learn a new library in tutor mode or author mode, then take a quiz with no AI. |
| 6 | [Capstone: your AGENTS.md](capstone-agents-md/) | 20 min | Turn the lines you collected in each lab into your own AGENTS.md. |

Each lab ends with an "Add to your AGENTS.md" section. Keep those lines. The capstone uses them.

### Optional extra content

Use these if there's time, or take them home.

| Lab | Time | What you do |
|---|---|---|
| [Research a role with NotebookLM](tools/notebooklm-career-lab/) | 15 min | Research a role you want with Gemini Notebook (formerly NotebookLM), then check its sources. |
| [Your personal tech radar](tools/tech-radar-lab/) | 10 min | Put the tools you know and want to learn into Adopt, Trial, Assess and Hold. |

### From earlier workshops

| Lab | Time | What you do |
|---|---|---|
| [Writing clean code](writing-code-lab/) | 15 min | Find and fix the code smells in a small pet weight tracker. |

## Prerequisites

- Git
- Java 17 or newer (a full JDK)
- An IDE, like IntelliJ IDEA, VS Code or Eclipse
- Optional: an AI coding assistant, like Claude Code, GitHub Copilot, Cursor or Codex. No agent?
  Pair with someone who has one.
- Docker, only if you want to run PetClinic's full test suite. The MySQL and Postgres tests need
  it. No lab requires it.
- A Google account, only for the NotebookLM lab

## Before the workshop

Clone PetClinic and this repo. Build PetClinic and the Learn it lab's starter once, so the
dependencies download before you arrive. Conference Wi-Fi is slow.

macOS and Linux:

```bash
git clone https://github.com/spring-projects/spring-petclinic.git
cd spring-petclinic
git checkout 500158f732419217507c7656904b8e6aa1bcc0d6
./mvnw -DskipTests package
cd ..
git clone https://github.com/Fundamentals-of-Software-Engineering/workshop.git
cd workshop/learn-it-dont-ship-it-lab/starter
./mvnw test
```

Windows (PowerShell or Command Prompt):

```bat
git clone https://github.com/spring-projects/spring-petclinic.git
cd spring-petclinic
git checkout 500158f732419217507c7656904b8e6aa1bcc0d6
.\mvnw.cmd -DskipTests package
cd ..
git clone https://github.com/Fundamentals-of-Software-Engineering/workshop.git
cd workshop\learn-it-dont-ship-it-lab\starter
.\mvnw.cmd test
```

Git will say you're in "detached HEAD" state after the checkout. That's expected.

Both builds should end with `BUILD SUCCESS`. The second one says `Tests run: 0`. That's expected
too. If a build says your Java is too old, install Java 17 or newer and set `JAVA_HOME` to it.

## Answers

Answer keys and solutions live on the `solution` branch. Try each lab first.

```bash
git checkout solution
```

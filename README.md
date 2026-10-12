# Fundamentals of Software Engineering: Workshop Labs

Hands-on labs for the Fundamentals of Software Engineering workshop by Dan Vega and Nate Schutta.
They go with the book *Fundamentals of Software Engineering* (O'Reilly).

The workshop's idea: **the fundamentals didn't change. The ratio did.** You write less code. You
read, specify, review and decide more. These labs practice those parts of the job, with and without
an AI coding agent.

## Dev2Next 2026

"Fundamentals of Software Engineering in the Age of AI." Monday, October 12, 2026.

Most labs use one codebase all day: [Spring PetClinic](https://github.com/spring-projects/spring-petclinic).
You clone [our copy](https://github.com/Fundamentals-of-Software-Engineering/spring-petclinic) once.
Its `main` is PetClinic as of September 29, 2026. Labs that need more code have their own branch.
By the afternoon you'll know PetClinic well. Each lab follows the same shape: do it yourself first,
then with your agent, then compare.

We might not get to every lab today. If we don't, try the rest on your own. Everything you need is
here, answers included. Have questions? Find us at the conference. We'd love to talk about them.

| # | Lab | What you do |
|---|---|---|
| 1 | [Read before you prompt](read-before-you-prompt-lab/) | Read PetClinic with no AI, then ask your agent the same questions and check its answers. |
| 2 | [Defuse the grenade](defuse-the-grenade-lab/) | Review a pull request an agent wrote. Find the problems before they ship. |
| 3 | [Spec it before you prompt](spec-it-before-you-prompt-lab/) | Plan the same feature twice, from a one-line prompt and from a short spec. Compare. |
| 4 | [Learn it, don't ship it](learn-it-dont-ship-it-lab/) | Learn a new library in tutor mode or author mode, then take a quiz with no AI. |
| 5 | [Research a role with Gemini Notebook](research-a-role-lab/) | The career lab. Research a role you want from real sources, then check what it told you. |

### Optional extra content

Use these if there's time, or take them home.

| Lab | What you do |
|---|---|
| [Net before refactor](net-before-refactor-lab/) | Pin down what old code does with tests, then let an agent refactor it. |
| [Capstone: your AGENTS.md](capstone-agents-md/) | Write your own AGENTS.md, from what you saw in the labs. |
| [Your personal tech radar](tools/tech-radar-lab/) | Put the tools you know and want to learn into Adopt, Trial, Assess and Hold. |

### From earlier workshops

| Lab | What you do |
|---|---|
| [Writing clean code](writing-code-lab/) | Find and fix the code smells in a small pet weight tracker. |

## Prerequisites

- Git
- Java 17 or newer (a full JDK)
- An IDE, like IntelliJ IDEA, VS Code or Eclipse
- Optional: an AI coding assistant, like Claude Code, GitHub Copilot, Cursor or Codex. No agent?
  Pair with someone who has one.
- Docker, only if you want to run PetClinic's full test suite. The MySQL and Postgres tests need
  it. No lab requires it.
- A Google account, for the Gemini Notebook lab

## Before the workshop

Clone our copy of PetClinic. It's the only thing you need. Every lab is a branch in it. Then build it
once, so the downloads happen at home. Conference Wi-Fi is slow.

```bash
git clone https://github.com/Fundamentals-of-Software-Engineering/spring-petclinic.git
cd spring-petclinic
./mvnw -DskipTests package
```

You're ready when it ends with `BUILD SUCCESS`. On Windows, use `.\mvnw.cmd` in the last line. If it
says your Java is too old, install Java 17 or newer and set `JAVA_HOME` to it.

## Branches

Each lab tells you which branch to use.

| Lab | Branch |
|---|---|
| Read before you prompt | `main` |
| Defuse the grenade | `defuse-the-grenade` |
| Spec it before you prompt | `main` |
| Net before refactor (optional) | `net-before-refactor` |
| Learn it, don't ship it | `learn-it-dont-ship-it` |
| Capstone (optional) | `main` |

Before you switch, run `git status`. If it shows changes from an earlier lab, run `git stash -u`
first. That puts your work aside, and you can get it back later with `git stash pop`.

On Windows, type `.\mvnw.cmd` wherever a lab says `./mvnw`.

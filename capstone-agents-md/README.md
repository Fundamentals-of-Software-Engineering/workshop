# Capstone: Your AGENTS.md

All day you wrote down lines for your agent at the end of each lab. Now you put them in one file.

An `AGENTS.md` is your fundamentals, written down for your agent. It's the coding standards doc,
the review checklist and the onboarding notes, in a form an agent reads before every task. Most
coding agents look for it at the root of the repo.

The fundamentals didn't change. Now you write them down for a reader that never gets tired and
never remembers yesterday.

## The lab (20 minutes)

Work alone. Use PetClinic, or a codebase you work on. Your own code is better if you can share it
with your agent.

### Part 1: Collect (3 min)

Open your notes from each lab. Find the "Add to your AGENTS.md" lines.

| Lab | Section it fills |
|---|---|
| Read before you prompt | About this codebase |
| Defuse the grenade | Review rules |
| Spec it before you prompt | How to plan a change |
| Net before refactor | Changing existing code |
| Learn it, don't ship it | When I'm learning |

Missed a lab? Borrow lines from the example below, then make them yours.

### Part 2: Write it (10 min)

Copy `AGENTS-template.md`. Fill it in. Then cut.

- **Be specific.** "Write clean code" changes nothing. "Validate pet forms in `PetValidator`"
  does.
- **Make it checkable.** If you can't tell whether the agent followed a rule, rewrite it.
- **Say why when it isn't obvious.** One short reason helps the agent handle the case you didn't
  think of.
- **Put commands in.** Exact commands for run, test and format. Agents guess these wrong.
- **Name what not to trust.** Stale docs, misleading names, the thing that bit you today.
- **Keep it short.** Aim for one screen. A rule the agent skims past is no rule at all.

### Part 3: Test it on your agent (5 min)

Give your agent the file and ask:

> Read AGENTS.md. Which rules are unclear, conflict with each other, or can't be checked? Which
> ones would you be most likely to ignore, and why?

Fix what it finds. If you have time, give it a small task and see whether it follows the rules.

### Part 4: Share (2 min)

Read your best line to your neighbor. Steal one of theirs.

## Take it home

- **Where it goes.** Put team rules in `AGENTS.md` at the root of the repo, and commit it. Claude
  Code reads `CLAUDE.md` instead. Put the line `@AGENTS.md` in it to pull this file in. Check your
  tool's docs for its name.
- **Team rules and personal rules are different.** "When I'm learning" is about you. Put it in your
  tool's personal instructions file, not in the team repo.
- **Grow it from pain.** When you correct your agent twice for the same thing, add a line. When a
  line stops mattering, delete it.
- **Treat it like code.** Review changes to it. Out-of-date instructions mislead an agent the same
  way out-of-date docs mislead a new teammate.

## Example: AGENTS.md for PetClinic

Written for spring-petclinic at `500158f` (Sept 29, 2026). Every fact in it was checked against that
commit.

```markdown
# AGENTS.md

Spring PetClinic: a sample vet clinic web app. Spring Boot 4.1, Java 17+, Thymeleaf, Spring Data JPA.

## About this codebase
- Code is grouped by feature: `owner` (owners, pets, visits), `vet`, `system` (config), and `model`
  (shared base classes).
- There is no service layer. Controllers call repositories directly. Don't add one.
- `Owner` is the aggregate root. There is no `PetRepository` or `VisitRepository`. Save pets and
  visits through `OwnerRepository`.
- Hibernate does not create tables (`ddl-auto=none`). SQL scripts do, in
  `src/main/resources/db/{h2,mysql,postgres}/`. A schema change touches all three `schema.sql` files
  and all three `data.sql` files.
- The H2 and MySQL `data.sql` files insert rows without column names. Adding a column breaks them.
- Pet form validation lives in `PetValidator`. `PetController` sets it with `@InitBinder`, so Bean
  Validation annotations on `Pet` don't run on the form.
- Every key in `messages.properties` must exist in all 9 translations. `I18nPropertiesSyncTest`
  checks.
- Don't trust: the README's `/h2-console` (it's 404 in Boot 4), and the name `ClinicServiceTests`
  (it tests repositories).

## Commands
- Run: `./mvnw spring-boot:run`, then http://localhost:8080
- Format: `./mvnw spring-javaformat:apply`. The build fails on formatting, so run it before testing.
- Test one class: `./mvnw test -Dtest=PetControllerTests`
- Test everything: `./mvnw test`. The MySQL and Postgres tests need Docker.

## How to plan a change
- Before you write code, restate the goal and list your assumptions. Ask me about the ones that matter.
- List the edge cases. Always include existing data, invalid input, and the edit path.
- Write the failing tests first and show me their names before you build.
- Follow the patterns already here. Name the file you copied the pattern from.
- Say what is out of scope. Don't build it.

## Review rules
- Never build SQL by joining strings. Use parameters or derived queries.
- Don't change existing tests to make them pass. Stop and ask.
- No new dependencies without asking first.
- Never log personal data like addresses or phone numbers.
- Every test must assert something.

## Changing existing code
- Never change code that has no tests. Write characterization tests first.
- A refactor never changes tests. If a test goes red, stop and ask.
- Run the tests before and after every refactor, and show me the results.

## Done means
- The new behavior has a test.
- The tests pass.
- You saw it work in the running app.
- You said what you checked and what you didn't.
```

And in your personal instructions file, not the repo:

```markdown
## When I'm learning
- When I'm learning something new, explain and quiz me. Don't write the code.
- Give me the smallest hint first.
- If I ask you to just write it, ask if I've written one myself before.
```

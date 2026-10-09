# Defuse the Grenade Lab

An AI coding agent opened a pull request for Spring PetClinic. It adds a search for owners by pet name.
It compiles. All 84 tests pass. The PR description says it's secure, fast and well tested.

Your job: review it before it merges. Some of what you'll find is the kind of thing people now call a
"slop grenade": work that looks done, gets passed along unread, and blows up on someone else.

There are more than ten problems in this PR. How many can your team find?

## Setup (before the lab starts)

You need Git and Java 17 or newer. You don't need Docker.

This assumes you cloned PetClinic and this repo side by side, as in
[Before the workshop](../README.md#before-the-workshop). If you didn't, do that first.

Start from a clean PetClinic folder. If `git status` shows changes from an earlier lab, run
`git add -A` and then `git stash -u`. That puts your earlier work aside. Then, from the folder that
holds both clones:

```bash
cd spring-petclinic
git checkout 500158f732419217507c7656904b8e6aa1bcc0d6
git switch -c agent/owner-pet-search
git apply ../workshop/defuse-the-grenade-lab/agent-pr.diff
git add --intent-to-add src
```

`git apply` prints nothing when it works. The last line makes the new files show up in `git diff` too.

On Windows, run these in Git Bash. It comes with Git for Windows. If `git apply` says "patch does
not apply", Git changed the diff's line endings. Use this line instead:

```bash
sed 's/\r$//' ../workshop/defuse-the-grenade-lab/agent-pr.diff | git apply
```

Now review the change the way you'd review any pull request:

- See the whole change: `git diff`, or open the project in your IDE and use its changes view.
- Or read `agent-pr.diff` in this folder in any editor.

Optional: check the claims you can.

- Run the tests: `./mvnw test`. The MySQL and Postgres tests need Docker, and the first run downloads
  large images. To skip them, run `./mvnw test '-Dtest=!MySqlIntegrationTests,!PostgresIntegrationTests'`.
  That runs the other 80 tests.
- Run the app: `./mvnw spring-boot:run`, then open http://localhost:8080/api/owners/search?petName=Leo

## The lab

Work in teams of two or three. Everyone reads on their own screen. Keep one shared list for the team.

### Part 1: Review it yourself

No AI for this part. That's the point.

1. Read `PR.md` first. That's what the agent says it did.
2. Then read the diff. That's what it actually did.
3. For each problem you find, write down:
   - Where it is (file and method)
   - What's wrong
   - What could happen in production
   - The fundamental that would have caught it

Use the review checklist below if you get stuck.

### Part 2: Ask your agent to review it

Now give the same PR to your AI coding agent. No agent? Use a teammate's, or join a team that has one.

Give it the description and the change. The best way: start the agent in your `spring-petclinic`
folder, so it can read the whole codebase. Then try:

> Review this pull request as a senior engineer would. The PR description is in
> `../workshop/defuse-the-grenade-lab/PR.md`. The change is in `git diff`. List every problem you
> find, with the file and line, why it matters, and how serious it is. Don't change any files.

If your agent can't read files, paste in `PR.md` and `agent-pr.diff` instead.

Compare its list with yours:

- What did it catch that you missed?
- What did it miss that you caught?
- Did it believe the PR description?

### Part 3: Score and debrief

Your facilitator will walk through the answers. Score your Part 1 list first:

- One point per problem found.
- One bonus point when you named the fundamental behind it.

Then score your agent's list the same way. Compare the two scores.

Talk about:

- Which problems would have reached production on your team? Be honest.
- If your agent found more than you did, what does that tell you? What did the human add?
- An agent wrote this PR. An agent can also review it. What was missing?
- The tests passed. Why didn't that help?
- Which claims in the PR description were wrong?
- Where would a human reviewer need to know the domain or the codebase to spot the problem?

## Review checklist

Smells are your review checklist now. Go through each one.

| Area | Ask |
|------|-----|
| Security | Is any input from the user used to build a query, a command or a path? |
| Data | Does it return or log more personal data than it needs? |
| Errors | What happens when something goes wrong? Does anyone find out? |
| Performance | Are there queries inside loops? Can a request return everything? |
| Tests | Do the new tests assert anything? Did any existing tests change, and why? |
| Scope | Did it change anything you didn't ask for? |
| Design | Does every new class or abstraction earn its place? |
| Dependencies | Is each new dependency needed? Is the version current? |
| Config | Does every new setting actually exist? |
| Claims | Is every line in the PR description true? |

## Add to your AGENTS.md

Write three to five lines your agent should follow next time, based on what you found. Do it after
Part 3. Keep them. The capstone uses them.

```markdown
## Review rules
- Never ... Instead, ...
- Don't ... without asking first.
```

<details>
<summary>Example lines. They give away answers, so open this after Part 3.</summary>

```markdown
## Review rules
- Never build SQL by joining strings. Use parameters.
- Don't change existing tests to make them pass. Stop and ask.
- No new dependencies without asking first.
- Never log personal data like addresses or phone numbers.
- Every test must assert something.
```

</details>

## Answers

The answer key is `ANSWERS.md` in this lab's folder on the repo's `solution` branch. Look at it only
after Part 3.

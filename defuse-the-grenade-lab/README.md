# Defuse the Grenade Lab

An AI coding agent opened a pull request for Spring PetClinic. It adds a search for owners by pet name.
It compiles. All 84 tests pass. The PR description says it's secure, fast and well tested.

Your job: review it before it merges. Some of what you'll find is the kind of thing people now call a
"slop grenade": work that looks done, gets passed along unread, and blows up on someone else.

There are more than ten problems in this PR. How many can your team find?

## Setup (before the lab starts)

You need the PetClinic clone from [Before the workshop](../README.md#before-the-workshop). You don't
need Docker.

The agent's pull request is open on GitHub:
[✨ Add owner search by pet name](https://github.com/Fundamentals-of-Software-Engineering/spring-petclinic/pull/1).
Its branch is `defuse-the-grenade`. Switch to it:

```bash
git status
git switch defuse-the-grenade
```

If `git status` showed changes from an earlier lab, run `git stash -u` before you switch.

The agent already committed its change, so `git status` shows nothing. To see what it changed,
compare the branch with `main`. Pick one:

- **On GitHub:** open [the pull request](https://github.com/Fundamentals-of-Software-Engineering/spring-petclinic/pull/1)
  and click the **Files changed** tab.
- **In IntelliJ:** click the branch name at the top of the window, then **main**, then
  **Show Diff with Working Tree**.
- **In a terminal:** `git diff main`.

Optional: check the claims you can.

- Run the tests: `./mvnw test`. The MySQL and Postgres tests need Docker, and the first run downloads
  large images. To skip them, run `./mvnw test '-Dtest=!MySqlIntegrationTests,!PostgresIntegrationTests'`.
  That runs the other 80 tests.
- Run the app: `./mvnw spring-boot:run`, then open http://localhost:8080/api/owners/search?petName=Leo

## The lab

Work in teams of two or three. Everyone reads on their own screen. Keep one shared list for the team.

### Part 1: Review it yourself

No AI for this part. That's the point.

1. Read the pull request's description first. That's what the agent says it did.
2. Then read the changes. That's what it actually did.
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

> Review this pull request as a senior engineer would. The PR description is the last commit
> message: run `git log -1`. The change is in `git diff main`. List every problem you find, with the
> file and line, why it matters, and how serious it is. Don't change any files.

If your agent can't run commands, paste in the description and the changes from GitHub instead.
Add `.diff` to the end of the pull request's URL to get the whole change as text.

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

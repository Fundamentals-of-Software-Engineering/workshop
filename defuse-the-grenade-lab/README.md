# Defuse the Grenade Lab

An AI coding agent opened a pull request for Spring PetClinic. It adds a search for owners by pet name.
It compiles. All 84 tests pass. The PR description says it's secure, fast and well tested.

Your job: review it before it merges. Some of what you'll find is the kind of thing people now call a
"slop grenade": work that looks done, gets passed along unread, and blows up on someone else.

There are more than ten problems in this PR. How many can your team find?

## Setup (5 minutes, or before the workshop)

You need Git and Java 17 or newer. Docker is only needed if you want to run the full test suite.

```bash
git clone https://github.com/spring-projects/spring-petclinic.git
cd spring-petclinic
git checkout 500158f732419217507c7656904b8e6aa1bcc0d6
git switch -c agent/owner-pet-search
git apply /path/to/workshop/defuse-the-grenade-lab/agent-pr.diff
git add --intent-to-add .
```

The last line makes the new files show up in `git diff` too.

Now review the change the way you'd review any pull request:

- See the whole change: `git diff`, or open the project in your IDE and use its changes view.
- Or read `agent-pr.diff` in this folder in any editor.

Optional: run it. `./mvnw spring-boot:run`, then open http://localhost:8080/api/owners/search?petName=Leo

## The lab (25 minutes)

Work in teams of two or three.

### Part 1: Review it yourself (12 min)

No AI for this part. That's the point.

1. Read `PR.md` first. That's what the agent says it did.
2. Then read the diff. That's what it actually did.
3. For each problem you find, write down:
   - Where it is (file and line)
   - What's wrong
   - What could happen in production
   - The fundamental that would have caught it

Use the review checklist below if you get stuck.

### Part 2: Ask your agent to review it (5 min)

Now give the same diff to your AI coding assistant. Try:

> Review this pull request as a senior engineer would. List every problem you find, with the file and
> line, why it matters, and how serious it is.

Compare its list with yours:

- What did it catch that you missed?
- What did it miss that you caught?
- Did it believe the PR description?

### Part 3: Score and debrief (8 min)

Your facilitator will walk through the answers. One point per problem found. One bonus point when you
named the fundamental behind it.

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

Write three to five lines your agent should follow next time, based on what you found. For example:

```markdown
## Review rules
- Never build SQL by joining strings. Use parameters.
- Don't change existing tests to make them pass. Stop and ask.
- No new dependencies without asking first.
- Never log personal data like addresses or phone numbers.
- Every test must assert something.
```

## Answers

Your facilitator has the answer key. Look at it only after Part 3.

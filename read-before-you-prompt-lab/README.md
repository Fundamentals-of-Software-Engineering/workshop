# Read Before You Prompt Lab

You just joined the team that owns Spring PetClinic. Tomorrow you'll ask an AI agent to change it.
Today you need to know how it works.

First you read it yourself, with no AI. Then you ask your agent the same questions and compare. You
can only judge what the agent tells you if you know the code. This lab is where you get that.

## Setup (5 minutes, or before the workshop)

You need Git and Java 17 or newer. An IDE helps a lot.

```bash
git clone https://github.com/spring-projects/spring-petclinic.git
cd spring-petclinic
git checkout 500158f732419217507c7656904b8e6aa1bcc0d6
```

That commit is from September 29, 2026. Everyone in the room reads the same code.

Optional: run it. `./mvnw spring-boot:run`, then open http://localhost:8080. The first run downloads
dependencies, so start it before the lab.

## The lab (25 minutes)

Work alone or in pairs.

### Part 1: Read it yourself (12 min)

No AI for this part. Not in the IDE, not in a browser tab. You're building the muscle first.

Use your IDE: go to definition, find usages, search. Write short answers. You won't finish
everything, and that's fine.

**Orientation (3 min)**

1. Without running anything, what technologies can you name from the project files? Look at
   `pom.xml` first. Then look at what else sits in the root folder.
2. Find the main application class. What annotations does it have?
3. Look at the package names under `src/main/java`. Is the code grouped by layer (all controllers
   together) or by feature?

**Navigation (5 min)**

4. Trace the `Pet` entity. Fill in this table. Write "none" if a layer doesn't exist.

   | Layer | Class | File |
   |---|---|---|
   | Entity | | |
   | Repository | | |
   | Service | | |
   | Controller | | |

   How does a new pet get saved to the database?
5. Sketch the database tables and how they connect. Where did you find them: the entity classes,
   or somewhere else?
6. Where is the database configured? Which database runs by default? What creates the tables?

**Patterns (4 min)**

7. Find one example each of the Repository pattern, MVC and dependency injection. How do classes
   get their dependencies?
8. What naming conventions do you see? Think about classes, methods, packages and templates.
9. Someone fills in the form at `/owners/{ownerId}/pets/new` and clicks "Add Pet". List every
   class and method that runs, in order. What checks happen before the pet is saved? Where does
   the browser end up?

**Write it down (last minute)**

In three sentences, explain how this codebase is organized to a new teammate. Keep it. You'll use it
at the end.

Tip: tests tell you how the code is supposed to work. If you're stuck on question 9, read
`PetControllerTests`.

### Part 2: Ask your agent the same questions (7 min)

Now open your AI coding assistant on the same repo. Ask it questions. Mix two kinds:

- **Strategic questions** ask about the whole system. "How is data persistence handled?"
- **Specific questions** ask about one thing. "Show me the Owner entity."

Try these, word for word. Copy them so everyone in the room asks the same thing.

> 1. What does this application do, and how is the code organized? Explain the architecture in a
>    few sentences.
> 2. How is data persistence handled? Where is the database configured, and how does the schema
>    get created?
> 3. Pick the Pet entity and find its model, repository, service, and controller.
> 4. Trace what happens, step by step, when someone submits the form at
>    /owners/{ownerId}/pets/new. Name every class and method involved.
> 5. How can I look at the data in the database while the app is running locally?
> 6. Show me the Owner entity.

Some of these questions have a wrong idea baked in. That's on purpose. Notice whether the agent
pushes back or plays along.

For every claim you didn't already know, check it. Open the file. Run the URL. Then fill in:

| | Notes |
|---|---|
| What it got right | |
| What it got wrong or invented | |
| What it said that you could only check because you read first | |
| What it taught you that you missed | |

No agent? Pair with someone who has one, and be the one who checks.

### Part 3: Debrief (6 min)

Your facilitator will walk through the real answers. Then talk about:

- Which strategic question got the most useful answer? Which got the most wrong one?
- Did your agent invent anything? A class, a layer, a URL, a setting? Would you have caught it
  without Part 1?
- Did it go along with a wrong assumption in a question? What does that mean for how you write
  prompts?
- Where did the agent beat you? Speed? Spotting a pattern you missed?
- The README is documentation too. Did anything in it turn out to be wrong? Who else trusts it?
- What made this codebase easier or harder to read? What would you copy into your own projects?

## Add to your AGENTS.md

Turn your three sentences into lines an agent can follow. Fill in the blanks for PetClinic, or for
your own codebase at work.

```markdown
## How this codebase is organized
- Code is grouped by ___. The main packages are ___.
- Each feature has ___. Business rules live in ___.
- Data access goes through ___. The schema lives in ___.
- Run it with ___. Test it with ___.
- Don't trust ___. (Something the docs or your agent got wrong.)
```

## Answers

Your facilitator has the answer key. Look at it only after Part 3.

# Read Before You Prompt Lab

You just joined the team that owns Spring PetClinic. Tomorrow you'll ask an AI agent to change it.
Today you need to know how it works.

First you explore it yourself, with no AI. Then you ask your agent the same questions and compare.
You can only judge what the agent tells you if you know the code.

## Setup

You need the PetClinic clone from [Before the workshop](../README.md#before-the-workshop), at commit
`500158f`. An IDE helps a lot.

Run the app if you can. You'll want it in Part 2, to check what your agent tells you.

```bash
./mvnw spring-boot:run
```

On Windows, use `mvnw spring-boot:run` (Command Prompt) or `.\mvnw spring-boot:run` (PowerShell).
Then open http://localhost:8080. Stop it with Ctrl+C.

## The lab

Work alone or in pairs.

### Part 1: Explore it yourself

No AI for this part. Not in the IDE, not in a browser tab.

Use your IDE: go to definition, find usages, search. Keep mental notes. You won't get through
everything, and that's fine.

1. What is it built with? Start with `pom.xml`, then look around the root folder.
2. How is the code organized? By layer (all the controllers together) or by feature?
3. How does a new pet get saved? Follow it from the controller to the database. Look for a
   repository and a service along the way.
4. Where does the database come from? Which one runs by default, and what creates the tables?
5. Someone fills in the "Add Pet" form and clicks the button. What runs, and where do they end up?

Stuck on the last one? Read `PetControllerTests`. Tests tell you how the code is supposed to work.

### Part 2: Ask your agent the same questions

Now open your AI coding assistant in the same folder. Ask these, word for word, so everyone in the
room asks the same thing:

> 1. What does this application do, and how is the code organized? Explain the architecture in a
>    few sentences.
> 2. How is data persistence handled? Where is the database configured, and how does the schema
>    get created?
> 3. Pick the Pet entity and find its model, repository, service, and controller. \*
> 4. Trace what happens, step by step, when someone submits the form at
>    /owners/{ownerId}/pets/new. Name every class and method involved.
> 5. How can I look at the data in the database while the app is running locally? \*
> 6. Show me the Owner entity.

\* These two have a trap built in. One assumes something that isn't there. The other leads to advice
that's out of date. Notice whether the agent pushes back or plays along.

As you go, notice:

- What it got right
- What it got wrong or made up
- What you could only check because you explored first

When it tells you something new, check it. Open the file. Try the URL.

No agent? Pair with someone who has one, and be the one who checks.

### Part 3: Debrief

Your facilitator will walk through the real answers. Then talk about:

- Did your agent invent anything? A class, a layer, a URL? Would you have caught it without Part 1?
- Did it go along with a wrong idea in a question? What does that mean for how you write prompts?
- Where did the agent beat you?
- Did anything in the project's own README turn out to be wrong?

# Learn It, Don't Ship It: answer key (facilitators only)

The lab lives in `learn-it-dont-ship-it-lab/` (README, quiz.md, starter/). The
reference solution is in `solution/` in this folder. ArchUnit 1.5.1
with `archunit-junit5`, Maven wrapper 3.9.16, compiled with `--release 17`. Checked Oct 8, 2026 on
Java 27 and Java 26. Not yet run on Java 17. ArchUnit's own classes target
Java 8, so 17 should be fine.

## Why ArchUnit

- It runs on Java 17 with Maven. It's one test dependency. No Docker, no framework.
- Most Java developers have heard of it. Few have written a rule. It's new, but not alien.
- Twelve minutes is enough for three rules. The API reads like English.
- Its failures are rich and specific. That gives us real debugging questions, which is where the
  study found the biggest gap.
- It ties back to the day. Architecture rules are a gate. The planted bug is an agent-style shortcut:
  a controller that skips the service.

**Why not jqwik?** It was the first idea. But its user guide now has an "Anti-AI Usage Clause". It
says the project isn't meant for AI coding agents. The docs also say each test run prints a notice
to agents. A lab where half the room has an agent write jqwik code goes against the author's
wishes. It could also skew the results if the agent refuses. So we didn't use it.

## The task

The starter is a tiny pet clinic in plain Java: `domain`, `repository`, `service`, `web`. People
write three ArchUnit rules in `ArchitectureTest.java`:

1. No class in `web` depends on a class in `repository`.
2. Classes in `domain` only depend on `domain` and Java's own classes.
3. Every class whose name ends in `Repository` lives in `repository`.

Then fix the code that breaks a rule. Stretch goal: one `layeredArchitecture()` rule.

The starter runs green with `Tests run: 0` on purpose. There are no rules yet.

### What happens along the way

**Rule 1 fails on the starter.** `VisitController.list()` reads from `VisitRepository` directly and
skips `VisitService` (which checks that the pet exists). Real output:

```text
Architecture Violation [Priority: MEDIUM] - Rule 'no classes that reside in a package '..web..' should depend on classes that reside in a package '..repository..'' was violated (3 times):
Constructor <com.example.clinic.web.VisitController.<init>(com.example.clinic.service.VisitService, com.example.clinic.repository.VisitRepository)> has parameter of type <com.example.clinic.repository.VisitRepository> in (VisitController.java:0)
Field <com.example.clinic.web.VisitController.visitRepository> has type <com.example.clinic.repository.VisitRepository> in (VisitController.java:0)
Method <com.example.clinic.web.VisitController.list(long)> calls method <com.example.clinic.repository.VisitRepository.findByPetId(long)> in (VisitController.java:26)
```

Three violations, not one. A dependency is a field type, a constructor parameter or a call. Fixing
only the call leaves two. Line `0` means "no line number", which is normal for fields and
parameters.

**Rule 2 trips almost everyone who writes it by hand.** `onlyDependOnClassesThat()` counts Java's own
classes. Allowing only `"..domain.."` gives 19 violations (`String`, `Object`, `Record`,
`LocalDate`). The fix is to allow `"java.."` too. That's quiz question 5. Author mode will often
never see this, because the agent writes it right the first time.

**Rule 3 passes.** That's fine. Not every rule finds something. It still guards the future.

## Reference solution

`solution/`. Run `./mvnw test`: 4 tests (three rules plus the stretch
goal), all pass.

```java
@AnalyzeClasses(packages = "com.example.clinic")
class ArchitectureTest {

    @ArchTest
    static final ArchRule controllers_do_not_use_repositories =
            noClasses().that().resideInAPackage("..web..")
                    .should().dependOnClassesThat().resideInAPackage("..repository..");

    @ArchTest
    static final ArchRule domain_depends_only_on_itself_and_java =
            classes().that().resideInAPackage("..domain..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("..domain..", "java..");

    @ArchTest
    static final ArchRule repositories_live_in_the_repository_package =
            classes().that().haveSimpleNameEndingWith("Repository")
                    .should().resideInAPackage("..repository..");

    // Stretch goal
    @ArchTest
    static final ArchRule layers_are_respected =
            layeredArchitecture().consideringOnlyDependenciesInLayers()
                    .layer("Web").definedBy("..web..")
                    .layer("Service").definedBy("..service..")
                    .layer("Repository").definedBy("..repository..")
                    .whereLayer("Web").mayNotBeAccessedByAnyLayer()
                    .whereLayer("Service").mayOnlyBeAccessedByLayers("Web")
                    .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service");
}
```

The fix: `VisitController` takes only `VisitService`, and `list()` calls `visitService.visitsFor()`.
The repository field and constructor parameter are gone.

We also ran these rules against the unfixed starter. Rule 1 and the stretch rule both fail with the
three violations above. So the rules aren't passing by accident.

Wrong turns to watch for:

- Changing the rule to make it pass. Fix the code, not the gate.
- Silencing rule 2 with `allowEmptyShould(true)` or a narrower `that()`. Allow `"java.."` instead.
- Fixing only the `list()` call and leaving the field and parameter.

## Quiz answers

One point each. For 3 and 4, give the point for the key idea.

### 1. The unused import: **B**

ArchUnit reads compiled bytecode, not source. The compiler drops unused imports, so there's nothing
to see. We checked: with the unused import in `VisitController`, rule 1 passes.

The bigger idea: ArchUnit checks what the code does, not what the file says. Comments and imports
are invisible to it.

### 2. Package patterns: **C** (`"..web.."`)

`..` means "any number of packages". We checked each pattern against ArchUnit's `PackageMatcher`:

| Pattern | `com.example.clinic.web` | `com.example.clinic.web.admin` |
|---|---|---|
| `"web"` | no | no |
| `"..web"` | yes | no |
| `"..web.."` | yes | yes |
| `"*.web"` | no | no |

`"..web"` is the trap. It works today and silently skips subpackages added later.

### 3. Read the failure

The record `Visit` in `domain` has a method `summary()`. On line 10 of `Visit.java`, it calls
`DateFormats.pretty()`, which lives in `web`. The domain now depends on the web layer.

Fixes (any one gets the point):

- Format the date in the web layer, for example in the controller. Keep `Visit` free of display code.
- Move `DateFormats` out of `web` into `domain` or a shared package the rule allows.

Not a fix: changing the rule.

To reproduce: in the solution, add `web/DateFormats.java` with a static `pretty(LocalDate)`, and give
`Visit` a `summary()` method that calls it. Rule 2 fails with exactly the output in the quiz.

### 4. The rule that checked nothing

No class lives in a package matching `..controllers..`. The controllers are in `web`. So the
`that()` clause matched nothing and the rule checked nothing.

ArchUnit fails these rules on purpose (`archRule.failOnEmptyShould`, on by default). Otherwise a
typo would give you a green test that guards nothing. Fix the pattern: `"..web.."`. Don't reach for
`allowEmptyShould(true)`.

The quiz shortens the message. The full output:

```text
java.lang.AssertionError: Rule 'no classes that reside in a package '..controllers..' should depend on classes that reside in a package '..repository..'' failed to check any classes. This means either that no classes have been passed to the rule at all, or that no classes passed to the rule matched the `that()` clause. To allow rules being evaluated without checking any classes you can either use `ArchRule.allowEmptyShould(true)` on a single rule or set the configuration property `archRule.failOnEmptyShould = false` to change the behavior globally.
```

### 5. Nineteen violations: **B**

"Only depend on" means only. Records extend `java.lang.Record`. Their fields are `String` and
`LocalDate`. `equals()` takes an `Object`. Every one of those is a dependency outside `..domain..`.
Allow `"java.."` as well. The output in the quiz is real, from the starter's two records (the
remaining 15 lines are more of the same).

## How to run the comparison

- **Split the room** before Part 1. Left half tutor mode, right half author mode. Or let each pair
  pick, and have them write T or A on top of their quiz.
- **Read the answers** and let people score their own.
- **Hands up, by group.** "Tutor mode: hands up if you got 4 or 5. Author mode: 4 or 5?" Then 0 to 2.
  Count roughly. A photo of the hands works too.
- **Then the debugging questions.** "Hands up if you got all of 3, 4 and 5." Tutor mode, then author
  mode. This is where the study saw the biggest gap, so it's the most interesting count.
- **Then speed.** "Who finished the task?" Expect author mode to finish first.

Be honest about what this is. It's a demo, not an experiment. People picked their mode. Twelve
minutes is short. The quiz is five questions. If author mode wins, that's a good discussion too. Ask
what they did differently.

## Debrief points

**What the study did.** Judy Hanwen Shen and Alex Tamkin, "How AI Impacts Skill Formation",
arXiv:2601.20245 (January 2026, revised February 2026). Tamkin is at Anthropic. Shen did the work in
the Anthropic Fellows Program. It's a preprint.

- 52 people in the main study, 26 per group. Crowd workers with at least a year of Python, paid a
  flat fee.
- They learned Trio, a Python library for async code. Nobody in the study had used it before.
- Two coding tasks in 35 minutes. The AI group had GPT-4o in a chat window, not an agent in their
  editor.
- Then a quiz with no AI: 14 questions, 27 points. Debugging, code reading and concepts. No
  code-writing questions.

**What it found.**

- The AI group scored 4.15 points lower. The paper calls that a 17% score difference, about two
  grade points. It was statistically significant.
- The biggest gap was debugging. The smallest was code reading.
- The AI group wasn't significantly faster on average. People who handed everything to the AI did
  finish faster. They also learned the least.
- From screen recordings, the authors grouped how people used the AI into six patterns. Three scored
  low: AI Delegation (the AI writes it all), Progressive AI Reliance (a question or two, then hand
  it all over) and Iterative AI Debugging (have the AI check and fix your code, again and again).
  Three scored high: Generation-Then-Comprehension (generate, then ask how it works), Hybrid
  Code-Explanation (ask for code and an explanation together) and Conceptual Inquiry (ask only
  concept questions, then write and debug the code yourself).

**Be careful how you say it.**

- It's one small study. 52 people, one library, one session of about an hour.
- It measured learning right after the task. Not weeks later. Not on the job.
- The six patterns are small groups (2 to 7 people each). They're suggestive, not proof.
- The AI was a chat assistant. Today's agents do more, which probably makes delegation easier, not
  harder. But the study didn't test that.
- Safe summary: "In one small study, passive AI use hurt learning, most of all debugging. Active use
  held up much better."

**Tie it to the room.**

- Tutor mode is close to the Conceptual Inquiry pattern. Author mode is AI Delegation. We just ran
  the two ends.
- The Conceptual Inquiry group hit lots of errors and fixed them on their own. They still scored
  high. The errors were part of the learning.
- Iterative AI Debugging was a low scorer. Pasting the error into the agent feels active, but it
  isn't. Tutor mode made people read rule 1's three violations themselves. That's quiz question 3.
- The struggle is the point. If tutor mode felt slow, that was the learning. Chapter 12: "There are no
  shortcuts to learning."
- This isn't "never use AI to learn". The tutor group used AI the whole time. The difference is who
  did the thinking.
- The reps rule: never written it? Write it once yourself. Written it a thousand times? Hand it off.
  Author mode is the right call for the thousandth ArchUnit rule. It's the wrong call for the first.
- Debugging is the skill that goes first, and it's the one you need when the agent's code breaks at
  2 a.m. You can only catch what you know.
- Hand off to Dan's segment: learning mode, skills and memory. Claude Code's Learning output style
  leaves `TODO(human)` gaps for you to fill in (checked in the Claude Code docs, Oct 8, 2026).

**The AGENTS.md lines** come straight from tutor mode. The prompt they pasted is already most of a
"when I'm learning" section.

## Verified Oct 8, 2026

- Starter: `./mvnw test` on Java 27 gives `Tests run: 0`, `BUILD SUCCESS`. Empty by design.
- Solution: `./mvnw test` on Java 27 gives `Tests run: 4, Failures: 0`, `BUILD SUCCESS`.
- Solution rules against the unfixed starter, on Java 26: 2 failures (rule 1 and the stretch rule),
  3 violations each, as quoted above.
- Every quoted output in the quiz and this key came from a real run, in copies of the starter.
- Question 2 was checked with ArchUnit's `PackageMatcher`.
- Not checked: a real Java 17 JDK, Windows (`mvnw.cmd`), and how each AI tool behaves with the tutor
  prompt.

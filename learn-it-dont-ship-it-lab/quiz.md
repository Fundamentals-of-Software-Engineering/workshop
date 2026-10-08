# Quiz: Learn It, Don't Ship It

Five questions. Four minutes.

No AI. No docs. No looking at your project. Write your answers on paper.

All questions use the clinic app from the starter. It has four packages under `com.example.clinic`:
`domain`, `repository`, `service` and `web`.

---

## 1. The unused import

`VisitController` is in the `web` package. Someone left this line at the top:

```java
import com.example.clinic.repository.VisitRepository;
```

The class never uses `VisitRepository` anywhere else. This rule is in place:

```java
@ArchTest
static final ArchRule controllers_do_not_use_repositories =
        noClasses().that().resideInAPackage("..web..")
                .should().dependOnClassesThat().resideInAPackage("..repository..");
```

What happens when you run `./mvnw test`?

- A. It fails. The import is a dependency on the `repository` package.
- B. It passes. ArchUnit checks the compiled classes, and an unused import isn't in the compiled class.
- C. It passes, but ArchUnit prints a warning about the unused import.
- D. It fails, unless your IDE removed the unused import before compiling.

---

## 2. Package patterns

You want one pattern that matches both `com.example.clinic.web` and `com.example.clinic.web.admin`.
Which one?

- A. `"web"`
- B. `"..web"`
- C. `"..web.."`
- D. `"*.web"`

---

## 3. Read the failure

You run the tests and get this:

```text
Architecture Violation [Priority: MEDIUM] - Rule 'classes that reside in a package '..domain..' should only depend on classes that reside in any package ['..domain..', 'java..']' was violated (1 times):
Method <com.example.clinic.domain.Visit.summary()> calls method <com.example.clinic.web.DateFormats.pretty(java.time.LocalDate)> in (Visit.java:10)
```

What's wrong, and where? Name one way to fix it.

---

## 4. The rule that checked nothing

You add this rule. Every other rule in the class passes.

```java
@ArchTest
static final ArchRule controllers_do_not_use_repositories =
        noClasses().that().resideInAPackage("..controllers..")
                .should().dependOnClassesThat().resideInAPackage("..repository..");
```

It fails with this:

```text
java.lang.AssertionError: Rule 'no classes that reside in a package '..controllers..' should depend on classes that reside in a package '..repository..'' failed to check any classes. This means either that no classes have been passed to the rule at all, or that no classes passed to the rule matched the `that()` clause. [...]
```

What's wrong? How do you fix it?

---

## 5. Nineteen violations

The domain has two records. They use `String` and `LocalDate`, and nothing else from outside the
domain. This rule fails:

```java
@ArchTest
static final ArchRule domain_depends_only_on_itself =
        classes().that().resideInAPackage("..domain..")
                .should().onlyDependOnClassesThat().resideInAPackage("..domain..");
```

```text
Architecture Violation [Priority: MEDIUM] - Rule 'classes that reside in a package '..domain..' should only depend on classes that reside in a package '..domain..'' was violated (19 times):
Class <com.example.clinic.domain.Pet> extends class <java.lang.Record> in (Pet.java:0)
Class <com.example.clinic.domain.Visit> extends class <java.lang.Record> in (Visit.java:0)
Constructor <com.example.clinic.domain.Pet.<init>(long, java.lang.String, java.lang.String)> calls constructor <java.lang.Record.<init>()> in (Pet.java:3)
Constructor <com.example.clinic.domain.Pet.<init>(long, java.lang.String, java.lang.String)> has parameter of type <java.lang.String> in (Pet.java:0)
[... 15 more]
```

Why does it fail?

- A. ArchUnit doesn't support records.
- B. "Only depend on" counts every class the domain uses, including Java's own, like `String`,
  `Record` and `LocalDate`. The rule needs to allow `"java.."` too.
- C. The domain secretly depends on the `service` package.
- D. `@AnalyzeClasses` points at the wrong package.

---

Done? Put your pen down. Your facilitator will read the answers.

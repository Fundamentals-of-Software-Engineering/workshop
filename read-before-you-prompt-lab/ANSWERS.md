# Read Before You Prompt: answer key (facilitators only)

The lab lives in `read-before-you-prompt-lab/README.md`. Base: spring-petclinic
`500158f732419217507c7656904b8e6aa1bcc0d6` (Sept 29, 2026, Spring Boot 4.1). Every answer below was
checked against that commit on Oct 8, 2026. URLs were checked against the running app.

## What changed since Arc of AI (April 2026)

- **There is no service layer.** The April lab asked for the entity's "model, repository, service,
  and controller." The service layer was removed in 2016 (`83ff9a5`, "Modularize and migrate to
  aggregate-oriented domain"). The new question says "write none if a layer doesn't exist."
- **There is no `PetRepository` or `VisitRepository`.** Both were removed in Jan 2022 (`c953442`,
  "Remove PetRepository and use Owner as aggregate", and `58fe629`). Pets and visits are saved
  through `OwnerRepository`.
- **Spring Boot 4.1** (June 2026). The web starter is now `spring-boot-starter-webmvc`, not
  `spring-boot-starter-web`.
- **The README's H2 console is gone.** It says the console is at `/h2-console`. At this commit that
  URL returns 404, with `java -jar` and with `./mvnw spring-boot:run` (devtools on). Boot 4 moved the
  console into its own module, `spring-boot-h2console`, and the pom doesn't include it.
- **The pet form does more now.** Since spring: a database unique constraint on pet names per owner
  (July 14), a 30 character name limit (Aug 11), and stricter `@InitBinder` rules
  (`setDisallowedFields("id", "*.id")`, Apr 20). The future birth date check is older (2023).
- April's Lab 5 asked "How does this affect our API contracts?" PetClinic has no pet API. The only
  JSON endpoint is `/vets`.

## Part 1 answers

The lab now asks five open questions, with mental notes and nothing written down. They map to the
answers below: lab 1 is answers 1 and 2, lab 2 is answer 3, lab 3 is answer 4, lab 4 is answers 5
and 6, and lab 5 is answer 9. Answers 7 and 8 are extra, if someone asks.

### Orientation

**1. Technologies.**

- `pom.xml`: Spring Boot 4.1.0 parent, Java 17. Starters for webmvc, data-jpa, thymeleaf,
  validation, cache and actuator. Databases: H2, MySQL and PostgreSQL drivers. JCache with
  Caffeine. Bootstrap 5.3.8 and Font Awesome 4.7.0 as webjars. Devtools.
- Build plugins: spring-javaformat (the build fails on bad formatting), Checkstyle with nohttp,
  JaCoCo, GraalVM native, CycloneDX SBOM, git-commit-id. A `css` profile compiles SCSS.
- Tests: Testcontainers (MySQL), Docker Compose support (Postgres).
- The root folder: a `build.gradle` too, so there are **two builds**, Maven and Gradle. Also
  `docker-compose.yml`, `k8s/`, `.devcontainer/` and `.gitpod.yml`.

**2. Main class.** `src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java`.
Annotations: `@SpringBootApplication` and `@ImportRuntimeHints(PetClinicRuntimeHints.class)`. The
second one is for GraalVM native images.

**3. Organization.** By feature. Packages: `owner` (owners, pets, pet types, visits), `vet`,
`system` (config, welcome page, the crash demo) and `model` (shared base classes). Inside each
feature it's entity, repository, controller. It's a small monolith with server-side HTML.

### Navigation

**4. The Pet trace.**

| Layer | Class | File |
|---|---|---|
| Entity | `Pet` (`@Entity`, table `pets`, extends `NamedEntity`) | `owner/Pet.java` |
| Repository | none | Saved through `OwnerRepository` (cascade from `Owner.pets`) |
| Service | none | No service layer since 2016 |
| Controller | `PetController` | `owner/PetController.java` |

Also part of the pet: `PetValidator`, `PetType`, `PetTypeRepository`, `PetTypeFormatter`, the
template `templates/pets/createOrUpdatePetForm.html`.

A new pet is saved by adding it to its owner and saving the owner:
`owner.addPet(pet); owners.saveAndFlush(owner);`. `Owner.pets` is
`@OneToMany(cascade = ALL, fetch = EAGER)`. Owner is the aggregate root.

Trap: `src/test/java/.../service/ClinicServiceTests.java` exists. Its Javadoc says "Integration test
of the Service and the Repository layer." It's a `@DataJpaTest` of the repositories. The name is a
fossil from 2016. Good callback to the book: "Do not assume the code does what the name implies."

**5. Schema.** Seven tables, in `src/main/resources/db/{h2,mysql,postgres}/schema.sql`:

- `owners` 1 to many `pets` (`pets.owner_id`)
- `types` 1 to many `pets` (`pets.type_id`)
- `pets` 1 to many `visits` (`visits.pet_id`)
- `vets` many to many `specialties` through `vet_specialties`
- `pets` has a unique constraint on `(owner_id, name)`, named `unique_owner_pet_name`

You can sketch most of it from the entities. A few things only show up in the SQL: the unique
constraint, and that `Visit.date` maps to `visit_date`. Column names come from a snake case naming
strategy (`birthDate` becomes `birth_date`).

**6. Database config.** `src/main/resources/application.properties`.

- `database=h2` by default. No datasource URL, so Boot starts an in-memory H2 with a random name
  (`jdbc:h2:mem:<uuid>`, printed at startup).
- `spring.sql.init.schema-locations=classpath*:db/${database}/schema.sql` and the matching
  `data-locations`. SQL scripts create the tables and load the sample data on every start.
- `spring.jpa.hibernate.ddl-auto=none`. Hibernate does not create tables.
- Profiles `mysql` and `postgres` (`application-mysql.properties`, `application-postgres.properties`)
  set `database=` and a URL from env vars (`MYSQL_URL`, `POSTGRES_URL`, with localhost defaults).
- Also worth noticing: `spring.jpa.open-in-view=false`, and
  `management.endpoints.web.exposure.include=*` with a comment saying don't do this in production.

### Patterns

**7. Patterns.**

- Repository: Spring Data interfaces with no implementation class. `OwnerRepository` and
  `PetTypeRepository` extend `JpaRepository`. `VetRepository` extends the narrower `Repository` and
  caches with `@Cacheable("vets")`. Query styles: a derived query (`findByLastNameStartingWith`)
  and a JPQL `@Query` (`findPetTypes`).
- MVC: `@Controller` classes return view names. Thymeleaf templates in
  `src/main/resources/templates`. `VetController` also has a `@ResponseBody` endpoint, `/vets`,
  which returns JSON (or XML with `Accept: application/xml`).
- Dependency injection: constructor injection everywhere. There is no `@Autowired` in `src/main`.
  `PetTypeFormatter` is a `@Component`, and Boot plugs it into Spring MVC on its own.

**8. Naming.**

- `XxxController`, `XxxRepository`, `XxxTests`.
- `initXxxForm` (GET) and `processXxxForm` (POST) pairs.
- View name constants like `VIEWS_PETS_CREATE_OR_UPDATE_FORM`.
- Template folders match features: `owners/`, `pets/`, `vets/`.
- Controllers are package-private. Entities and repositories are public.

**9. The form submission, `POST /owners/{ownerId}/pets/new`.**

1. The owner page's "Add New Pet" link goes to `GET /owners/{ownerId}/pets/new`. That's
   `PetController.initCreationForm` (class mapping `/owners/{ownerId}`, method mapping
   `/pets/new`). It renders `pets/createOrUpdatePetForm`.
2. The form has no `action`, so it posts back to the same URL.
3. Before the handler runs, the `@ModelAttribute` methods run. `populatePetTypes` loads the types.
   `findOwner` loads the owner (or throws `IllegalArgumentException`). `findPet` returns a
   `new Pet()` because there is no `petId`.
4. `@InitBinder("pet")` blocks binding to `id` and sets `PetValidator` as the validator.
   `PetTypeFormatter.parse` turns the type name ("dog") into a `PetType`.
5. `processCreationForm(Owner, @Valid Pet, BindingResult, RedirectAttributes)`:
   - `PetValidator`: name required and at most 30 characters, type required for a new pet, birth
     date required.
   - Duplicate name for this owner, case-insensitive (`owner.getPet(name, true)`).
   - Birth date in the future is rejected.
   - Any error: show the form again.
6. `owner.addPet(pet)`, then `owners.saveAndFlush(owner)`. If the database's
   `unique_owner_pet_name` constraint fires (two requests at once), it shows "already exists"
   instead of an error page.
7. Flash message "New Pet has been Added", then `redirect:/owners/{ownerId}`, which is
   `OwnerController.showOwner`.

Checked live: a new pet redirects to `/owners/1`. "leo" for owner 1 shows "is already in use". A
2099 birth date shows "invalid date".

## Part 2: what to expect from the agent

**The dry run (Oct 8):** a fresh Claude agent got the six questions blind, with read access to the
repo at the pinned commit. It took about a minute. It got everything right:

- It said there is no service layer and no Pet repository, and explained `ClinicServiceTests`.
- It traced the form submission in more detail than the key above.
- On question 5 it flagged the README: `/h2-console` will 404 in Boot 4. It named the missing
  module (`spring-boot-h2console`, checked: it exists for 4.1.0) and offered a Postgres option.

So expect some agents to beat the room, like in Defuse the grenade. That's fine. It sets up the
real point.

Results vary with the tool and the context. Watch for these, especially from tools that only see a
pasted file or answer from memory:

- A service layer (`ClinicService`, `OwnerService`) or a `PetRepository` / `VisitRepository`. Older
  PetClinic versions had them, so they're in the training data.
- `/h2-console`, repeated from the README.
- `spring-boot-starter-web`, `javax.persistence`, or Spring Boot 2 or 3.
- "Hibernate creates the schema" (`ddl-auto`). It's `none` here.
- A REST API for owners and pets. That's the separate `spring-petclinic-rest` project.

Question 3 has a wrong premise (a service layer). Question 5 leads to a stale doc. Ask who got an
agent that played along.

## Debrief points

- **You could only grade the agent because you read first.** The dry run agent was right about
  everything. Nobody in the room could know that without Part 1. "Trust what you know, verify what
  you don't."
- **Strategic questions found the shape.** "How is persistence handled?" surfaces the SQL scripts,
  `ddl-auto=none` and the profiles in one answer. "Show me the Owner entity" gives you one file.
  Use strategic questions to get a map. Use specific ones to check it.
- **Questions carry assumptions.** The April lab itself asked for a service that doesn't exist. If
  your prompt assumes something false, a weaker tool builds on it. Read first, then ask.
- **Docs drift.** The README is wrong about `/h2-console`. `ClinicServiceTests` is named for a layer
  that left in 2016. An agent that reads stale docs repeats them with confidence. The book: "Do not
  assume the code does what the name implies."
- **Read the tests.** `PetControllerTests` answers the Add Pet question faster than the controller does. Code
  tells you how. Tests tell you how it's supposed to work.
- **Ask what the agent can't know.** Why there is no service layer is in the git history, not the
  code. Bonus for anyone who ran
  `git log --diff-filter=D --oneline -- '*PetRepository.java'`. The top line is `c953442 Remove
  PetRepository and use Owner as aggregate`.
- The lines for their AGENTS.md come from what they learned here, in the capstone. A filled-in PetClinic version is
  in the capstone's example.

## Live demo (Dan, the agent as tour guide)

After the debrief, ask the agent "why" questions, not "what" questions:

- "Why is there no PetRepository? Use git history to back up your answer."
- "Why does PetController set its own validator instead of using Bean Validation?" (This sets up
  the afternoon. Bean Validation annotations on `Pet` don't run on the pet form. See the Spec it
  answers.)
- "What in the README is out of date? Check each claim against the code."

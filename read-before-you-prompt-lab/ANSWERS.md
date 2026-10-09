# Read Before You Prompt: answers

PetClinic at `500158f`.

## Part 1

1. **Built with:** Spring Boot 4.1, Java 17, Spring MVC, Spring Data JPA, Thymeleaf, H2 (plus MySQL
   and Postgres profiles). The root folder also has a Gradle build, Docker Compose and Kubernetes
   files.
2. **Organized by feature,** not by layer: `owner`, `vet`, `system`, `model`.
3. **There's no PetRepository and no service layer.** A new pet is saved through its owner:
   `owner.addPet(pet)`, then `owners.saveAndFlush(owner)`. Owner is the aggregate root.
4. **H2 in memory by default.** SQL scripts in `src/main/resources/db/h2/` create the tables and load
   the data. Hibernate doesn't (`ddl-auto=none`).
5. **Add Pet:** `PetController.processCreationForm`. `PetValidator` checks the name, type and birth
   date. It rejects duplicate names and future dates. Then it saves through the owner and redirects
   to `/owners/{id}`. `PetControllerTests` shows all of it.

## Part 2: the traps

- **Question 3** asks for a service. There isn't one, and hasn't been since 2016. Did the agent push
  back, or invent `ClinicService`?
- **Question 5** leads to `/h2-console`. The project's README mentions it, but it's a 404 in Spring
  Boot 4.
- **Don't trust names:** `ClinicServiceTests` tests repositories, not a service.

## The point

You could only grade the agent because you read the code first.

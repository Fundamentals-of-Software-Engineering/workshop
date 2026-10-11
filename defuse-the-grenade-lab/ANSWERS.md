# Defuse the Grenade: answers

13 planted problems. The README's four hints point at #1, #3, #8 and #5.

| # | Where | Problem | Fundamental |
|---|---|---|---|
| 1 | `PetNameOwnerSearchStrategy` | SQL built by joining strings. SQL injection. | Never trust input. Use parameters. |
| 2 | The comment above it | Says the query is parameterized. It isn't. | Comments can lie. Read the code. |
| 3 | `OwnerSearchControllerTests` | The new tests assert nothing | Every test asserts something. |
| 4 | `OwnerControllerTests` | Two existing tests changed so they pass | Don't move the gate. |
| 5 | `OwnerController.processFindForm` | Dropped `strip()`. " Franklin" finds nobody. | Scope. Every change needs a reason. |
| 6 | `application.properties` | A setting that doesn't exist. Nothing is cached. | Every setting must exist. |
| 7 | `OwnerSearchController` | Logs whole owners, with address and phone | Never log personal data. |
| 8 | `OwnerSearchResult` | Returns address and phone to any caller | Return only what's needed. |
| 9 | `PetNameOwnerSearchStrategy` | `catch (Exception)` returns an empty list | Don't swallow errors. |
| 10 | `OwnerSearchController` loop | A query per pet (N+1) | Know your data access. |
| 11 | `OwnerSearchController` | A blank search returns everyone. No paging. | Bound every query. |
| 12 | `OwnerSearchStrategy` + factory | An interface and a factory for one class | Abstractions earn their place. |
| 13 | `pom.xml` | Old commons-lang3 for what Java does. Known CVE. | Every dependency costs something. |

## The PR description vs. the code

| The PR says | The truth |
|---|---|
| "Secure, parameterized queries to prevent SQL injection" | The SQL is built by joining strings (#1). |
| "Query caching turned on for faster repeated searches" | The setting doesn't exist (#6). Nothing is cached. |
| "Accurate visit counts, read straight from the database" | That's the N+1 (#10), sold as a feature. |
| "Simplified last name handling in `OwnerController` using Apache Commons Lang" | A change outside the feature (#5), plus a new dependency (#13). |
| "Comprehensive tests for the new endpoint, including edge cases like empty input" | They assert nothing (#3). |
| "Updated existing tests to match the simplified search behavior" | It changed tests so they'd pass (#4). |
| "No breaking changes. Existing behavior is unchanged." | Searching " Franklin" broke (#5). |
| "All 84 tests pass" | True. That's the trap. |

## Show it live in the debrief

Run the app on the `defuse-the-grenade` branch. Each of these was checked against the running app.

| Problem | Open this | What you see |
|---|---|---|
| #8 | http://localhost:8080/api/owners/search?petName=Leo | George Franklin's address and phone |
| #11 | http://localhost:8080/api/owners/search | All 10 owners. No paging. |
| #1 | http://localhost:8080/api/owners/search?petName=x%27%20OR%201%3D1%20-- | All 10 owners again. That's `x' OR 1=1 --`. |
| #9 | http://localhost:8080/api/owners/search?petName=O%27Brien | `[]`. The apostrophe broke the SQL, and the error vanished. |
| #5 | http://localhost:8080/owners?lastName=%20Franklin | "has not been found". On `main`, it finds him. |
| #7 | The app's console | The log line with the address and phone |

## Each problem in detail

### 1. SQL injection

`PetNameOwnerSearchStrategy.search`:

```java
String sql = "SELECT DISTINCT o.* FROM owners o JOIN pets p ON p.owner_id = o.id "
        + "WHERE LOWER(p.name) LIKE '%" + term.toLowerCase() + "%' ORDER BY o.last_name";
```

- **What's wrong:** whatever the user types becomes part of the SQL.
- **Show it:** search for `x' OR 1=1 --`. You get every owner.
- **In production:** anyone can read, and maybe change, whatever this query can reach.
- **Fix:** pass the search term as a parameter. Better, add a query method to `OwnerRepository`, like
  the rest of PetClinic does.

### 2. The comment lies

Right above the SQL:

```java
// Uses a parameterized query, so user input is safely escaped and the
// search is protected against SQL injection.
```

- **What's wrong:** it says the opposite of what the code does.
- **Why it matters:** people skim comments and trust them. So do agents. A wrong comment is worse than
  none.
- **Fundamental:** read the code, not the comment.

### 3. The new tests assert nothing

`OwnerSearchControllerTests` has three tests:

- `searchByPetNameEndpoint` calls the endpoint and prints the response. No check.
- `searchReturnsResults` checks the result isn't null. The code never returns null.
- `searchHandlesEmptyInput` calls the method. No check.

- **What's wrong:** they pass no matter what the code returns. Make `search` always return an empty
  list, and all three still pass.
- **Fix:** check the response: searching "Leo" finds George Franklin, a blank search is rejected, and
  the response has no address.

### 4. Existing tests changed so they'd pass

`OwnerControllerTests`, two tests:

- `processFindFormIgnoresSurroundingWhitespace` used to search `" Franklin"`, `"Franklin "` and
  `" Franklin "`, and expect three hits. Now it searches plain `"Franklin"` once. The name still
  says it ignores whitespace. It doesn't test that anymore.
- `processFindFormWithWhitespaceOnlyLastNameReturnsAllOwners` used to send `"   "`. Now it sends
  `""`. The name still says whitespace.

- **Why it happened:** #5 broke both tests. The agent changed the tests to match the bug.
- **Show it:** `git diff main -- src/test/java/org/springframework/samples/petclinic/owner/OwnerControllerTests.java`
- **Fundamental:** don't move the gate. A red test means behavior changed. Ask why before you touch
  the test.

### 5. A change outside the feature broke search

`OwnerController.processFindForm`. Before, a null last name became `""`, and anything else was
`strip()`ped. After:

```java
String lastName = StringUtils.defaultString(owner.getLastName());
```

- **What's wrong:** no more `strip()`. Search for " Franklin" (with a space) and nobody is found.
- **Why it happened:** the task was a new pet name API. The agent "cleaned up" the owner search too.
  Nobody asked for that.
- **Fundamental:** scope. Every changed line in a PR needs a reason.

### 6. A setting that doesn't exist

`application.properties`:

```properties
# Cache pet search queries so repeated searches are faster
spring.jpa.query-cache.enabled=true
```

- **What's wrong:** Spring Boot has no such property. Spring ignores unknown properties, so nothing
  fails, and nothing is cached. IntelliJ Ultimate flags it as "Cannot resolve configuration
  property".
- **Fundamental:** every setting must exist. Agents invent plausible ones.

### 7. Personal data in the logs

`OwnerSearchController`:

```java
log.info("Pet search '{}' matched owners: {}", petName, owners);
```

`Owner.toString()` includes the address, city and phone. The real line from the app:

```text
Pet search 'Leo' matched owners: [[Owner@5c222740 id = 1, new = false, lastName = 'Franklin',
firstName = 'George', address = '110 W. Liberty St.', city = 'Madison', telephone = '6085551023']]
```

- **In production:** logs are kept for months, copied to other systems, and read by more people than
  the database. That's a privacy problem.
- **Fix:** log the number of matches, not the owners.

### 8. Personal data sent to anyone who asks

`OwnerSearchResult`:

```java
/** A search result for the owner search API, with everything a client might need. */
public record OwnerSearchResult(Integer id, String firstName, String lastName, String address,
        String city, String telephone, List<String> petNames, int visitCount) {}
```

- **What's wrong:** the API has no login, and it returns home addresses and phone numbers.
- **With #11:** one request with a blank search lists every owner's address and phone.
- **Fix:** return what the search needs: the owner's ID, name and pets. Staff click through to the
  owner page for the rest.

### 9. Errors swallowed

`PetNameOwnerSearchStrategy.search`:

```java
catch (Exception ex) {
    // An empty result is a better experience than an error page
    return List.of();
}
```

- **Show it:** search for `O'Brien`. The apostrophe breaks the SQL, and you get `[]`. No error, no
  log line.
- **In production:** real failures look like "no results". Nobody finds out. It also hides injection
  attempts from monitoring.
- **Fix:** don't catch `Exception`. Let it fail, or log it and return an error. Fixing #1 also fixes
  the apostrophe.

### 10. A query per pet (N+1)

`OwnerSearchController`, inside the loop over each owner's pets:

```java
Number count = (Number) entityManager.createNativeQuery("SELECT COUNT(*) FROM visits WHERE pet_id = ?1")
    .setParameter(1, pet.getId())
    .getSingleResult();
```

- **What's wrong:** one extra query for every pet. A blank search runs 13 of them for the 13 sample
  pets. With 10,000 pets, it's 10,000 queries for one request.
- **The irony:** `Pet.visits` is already loaded (`FetchType.EAGER`). `pet.getVisits().size()` costs
  nothing.
- **Fundamental:** know what your data access does.

### 11. No limit, no paging

```java
if (StringUtils.isBlank(petName)) {
    petName = "";
}
```

- **What's wrong:** a blank search becomes `LIKE '%%'`, which matches every owner with a pet. All
  results come back in one response.
- **Compare:** the existing owner search pages results, 5 at a time.
- **Fix:** reject a blank search, and page or cap the results.

### 12. Abstractions that don't earn their place

`OwnerSearchStrategy`, `OwnerSearchStrategyFactory` and `PetNameOwnerSearchStrategy`: an interface, a
factory and a registry for one search.

- **What's wrong:** the controller asks the factory for the one strategy it already knows by name
  (`PetNameOwnerSearchStrategy.TYPE`). The pattern buys nothing. The PR calls it "Open/Closed".
- **It also ignores the codebase:** PetClinic's controllers call Spring Data repositories directly.
  This PR brings in `EntityManager` and native SQL instead.
- **Fix:** one query method on `OwnerRepository`, called from the controller.
- **Fundamental:** YAGNI. Add the abstraction when the second search type shows up.

### 13. A dependency nobody needed

`pom.xml` adds `commons-lang3` `3.12.0`. It's used for `defaultString` and `isBlank`.

- **Java already does this:** `Objects.requireNonNullElse(s, "")` and `s == null || s.isBlank()`.
- **It's old:** 3.12.0 is from 2021. It has CVE-2025-48924, fixed in 3.18.0.
- **It downgrades:** the hard-coded version overrides Spring Boot's. Boot 4.1 manages 3.20.0, and
  PetClinic already had 3.20.0 for tests. Now everything gets 3.12.0.
- **Show it:** `./mvnw dependency:tree -Dincludes=org.apache.commons:commons-lang3`
- **Fundamental:** every dependency costs something. Ask before you add one.

## Also worth noticing (not in the 13)

- `%` and `_` in a search aren't escaped. Even with #1 fixed, searching `_` matches every pet.
- The PR description's tone: emoji, "comprehensive", "Ready to merge! 🚀". Confidence isn't evidence.

## Will the agents find all 13?

Probably most of them. We tested it on Oct 11. A fresh agent (Claude Sonnet) got the README's
prompt, word for word, and nothing else. It took 38 seconds.

- **It found all 13.** It didn't believe the PR description, and it called the "No breaking changes"
  claim false.
- **It also listed 6 more.** Field injection, locale, `public` classes, emoji in the PR. Some are fair.
  Most are noise. 19 items in all.
- **It ranked them oddly.** The dependency was "low to medium", and it didn't know about the CVE.
- **It ran nothing.** It read the diff. It said "All 84 tests pass" was something it "could not
  verify".

Other tools and smaller models will find fewer. Expect a spread across the room. That's a good
debrief.

The prompt doesn't point the agent at anything. "List every problem you find" is what you'd ask any
reviewer. The lab's checklist and hints are on GitHub, not in the clone, so the agent never sees them.

## What the human adds

The agent wins on count. So what did the person add?

- **Judgment.** Which of the 19 blocks the merge? The agent buried the CVE and promoted noise.
- **Proof.** Running the app shows it: the injection returns every owner, and " Franklin" finds
  nobody. The agent never checked.
- **Context.** Does the front desk actually need the phone number? Only someone who knows the clinic
  can answer.

The land: the same kind of model wrote this PR and caught every problem in it. The fix wasn't a
better model. It was a gate: a required review before merging.

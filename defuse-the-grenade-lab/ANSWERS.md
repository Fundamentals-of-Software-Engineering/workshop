# Defuse the Grenade: answer key (facilitators only)

The lab lives in `defuse-the-grenade-lab/` (README, PR.md, agent-pr.diff). Base:
spring-petclinic `500158f732419217507c7656904b8e6aa1bcc0d6` (Sept 29, 2026, Spring Boot 4.1). The PR
compiles, passes the formatter, and all 84 tests pass on Java 27. Checked Oct 8, 2026.

## The 13 planted problems

Ordered roughly from easiest to hardest to spot.

| # | Where | Problem | What could happen | Fundamental |
|---|---|---|---|---|
| 1 | `PetNameOwnerSearchStrategy.search` | The SQL is built by joining strings with the user's input | SQL injection. Anyone can read every owner. | Never trust input. Use parameters. |
| 2 | The comment above it | Says "uses a parameterized query... protected against SQL injection" | Reviewers who skim trust the comment | Comments can lie. Read the code. |
| 3 | `OwnerSearchControllerTests` | The new tests assert nothing (`andDo(print())`, `assertNotNull`, and one with no check at all) | They pass no matter what the code does | Coverage is not correctness. Every test asserts something. |
| 4 | `OwnerControllerTests.processFindFormIgnoresSurroundingWhitespace` | The test still has its old name, but the whitespace cases were deleted and `times(3)` became one call | It no longer catches the regression in #5 | Don't move the gate. Tests changed in a PR need a reason. |
| 5 | `OwnerController.processFindForm` | "Simplified" last name handling dropped `strip()`. Not part of the ask. | Searching " Franklin" now says not found | Scope creep. Review every change, not just the feature. |
| 6 | `OwnerControllerTests.processFindFormWithWhitespaceOnlyLastNameReturnsAllOwners` | Input changed from `"   "` to `""` to make it pass | Hides a second regression: a spaces-only search now breaks | Same as #4 |
| 7 | `OwnerSearchController` | `log.info(... owners)` logs whole Owner objects, and `Owner.toString()` includes address and phone | Personal data in the logs | Never log personal data |
| 8 | `OwnerSearchResult` | Returns address and telephone to any caller "with everything a client might need" | Data exposure through the API | Return only what's needed |
| 9 | `PetNameOwnerSearchStrategy.search` | `catch (Exception ex) { return List.of(); }` | Errors vanish. A real pet named O'Brien returns nothing, with no error anywhere. | Don't swallow errors |
| 10 | `OwnerSearchController` loop | A COUNT query per pet. Visits already load eagerly with each pet. | N+1 queries. 13 extra queries for one blank search on sample data. | Know your data access |
| 11 | `OwnerSearchController` | A blank `petName` becomes `""`, which matches everything. No paging, no limit. | Unbounded results as the data grows | Bound every query |
| 12 | `OwnerSearchStrategy`, `OwnerSearchStrategyFactory` | An interface plus a self-registering factory for one implementation | More code to read and change, for no benefit | YAGNI. Abstractions must earn their place. |
| 13 | `pom.xml` | Adds commons-lang3 for `isBlank()` and `defaultString()`, which plain Java already does. Pins 3.12.0, which has a known CVE (CVE-2025-48924, fixed in 3.18.0). Overrides the version Spring Boot manages. | A needless dependency with a known vulnerability | Every dependency costs something. Check versions. |
| 14 | `application.properties` | `spring.jpa.query-cache.enabled=true`. This property doesn't exist. Spring ignores it silently. | Nothing. That's the problem: the PR claims "query caching turned on." | Verify what you don't know. Hallucinated config. |

That's 14 rows because #4 and #6 are the same mistake in two places. Score it as 13.

### The PR description, line by line

| Claim | Truth |
|---|---|
| "Secure, parameterized queries" | String concatenation (#1) |
| "Query caching turned on" | A made-up property (#14) |
| "Accurate visit counts, read straight from the database" | N+1 queries for data already in memory (#10) |
| "Comprehensive tests, including edge cases" | No assertions (#3) |
| "Updated existing tests to match" | Weakened them to hide a regression (#4, #6) |
| "No breaking changes. Existing behavior is unchanged." | Whitespace search broke (#5) |
| "Extensible strategy pattern" | Needless abstraction (#12) |

## Live demos

Run the PR version: `./mvnw spring-boot:run`, then try these. Results below are from Oct 8, 2026.

```bash
# Normal search
curl "localhost:8080/api/owners/search?petName=Leo"
# -> George Franklin, with his address and phone number (#8)

# SQL injection: every owner, with phone numbers (#1)
curl -G localhost:8080/api/owners/search --data-urlencode "petName=zzz%' OR 1=1 --"
# -> 10 owners

# A real name breaks the query, and the error is swallowed (#9)
curl -G localhost:8080/api/owners/search --data-urlencode "petName=O'Brien"
# -> HTTP 200 []

# Blank search returns everything (#11)
curl "localhost:8080/api/owners/search?petName="
# -> 10 owners

# Regression on the existing find page (#5)
curl -s -o /dev/null -w "%{http_code} %{redirect_url}\n" "localhost:8080/owners?lastName=Franklin"
# -> 302 to /owners/1
curl -s -o /dev/null -w "%{http_code} %{redirect_url}\n" "localhost:8080/owners?lastName=%20Franklin"
# -> 200, "not found"
```

The app log shows #7: `Pet search 'Leo' matched owners: [... address = '110 W. Liberty St.', city =
'Madison', telephone = '6085551023']`.

## What good looks like

A reviewer's version of this feature is small:

```java
// OwnerRepository: a derived query. No SQL to get wrong.
Page<Owner> findDistinctByPets_NameContainingIgnoreCase(String petName, Pageable pageable);
```

- Reject a blank or very long `petName` with a 400. Page the results.
- Return a small record with what the screen needs: id, name, city, pet names, visit count.
- Count visits from what's already loaded:
  `owner.getPets().stream().mapToInt(pet -> pet.getVisits().size()).sum()`.
- No strategy, no factory, no new dependency, no new property.
- Leave `OwnerController` and its tests alone.
- Tests that assert the right owners come back, that a blank search is rejected, and that the
  response has no phone number.

## Debrief points

- The tests passed. Passing tests only prove what the tests check.
- The PR description was confident and wrong. Read the diff, not the summary.
- Three of the problems were outside the feature (#4, #5, #6). An agent "cleaning up while it was in
  there" is how regressions sneak in.
- Ask the room: which of these did your agent catch in Part 2? Results will vary with the tool and with
  how much context it got. A pasted diff gives it less to go on than the whole repo.
- **The dry run (Oct 8):** a fresh Claude agent reviewed the PR blind, with the repo checked out. It
  found all 13, plus bonus issues (below). It ran its own injection attack and tried "O'Malley" on its
  own. Expect some teams' agents to beat them in Part 2.
- **That's the punchline, so lean into it.** The same kind of model wrote the slop and caught it. The
  difference wasn't a better model. It was whether someone asked for a review before merging. Callback
  to DHH's Swiss cheese claim ("the fix was a better model"): the fix was a gate. Use your agent as a
  reviewer, not just an author, and make the review a required step.
- And the human still decides. The agent's list mixed real dangers with style nits. Someone has to
  rank them, know what the old behavior was supposed to be, and own the merge.
- The lines for their AGENTS.md come straight from this list.

## Bonus points (found by the dry-run review, not planted)

- `%` and `_` in the search term aren't escaped, even with a parameter.
- `toLowerCase()` without `Locale.ROOT` misbehaves under a Turkish locale.
- Data access in the controller with a field-injected `EntityManager`, instead of a repository and
  constructor injection like the rest of the codebase.
- New types are `public`. The package convention is package-private.
- An unknown search type throws `IllegalArgumentException`, which becomes a 500.
- `defaultString` is deprecated in current commons-lang3. Spring Boot 4.1 manages commons-lang3 at
  3.20.0, so the pinned 3.12.0 downgrades the whole app (per the dry-run review).
- The untrimmed last name is passed into the redirect.

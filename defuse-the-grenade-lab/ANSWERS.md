# Defuse the Grenade: answers

13 planted problems. One point each, plus one for naming the fundamental.

| # | Where | Problem | Fundamental |
|---|---|---|---|
| 1 | `PetNameOwnerSearchStrategy` | SQL built by joining strings. SQL injection. | Never trust input. Use parameters. |
| 2 | The comment above it | Claims the query is parameterized | Comments can lie. Read the code. |
| 3 | `OwnerSearchControllerTests` | The new tests assert nothing | Every test asserts something. |
| 4 | `OwnerControllerTests` | Whitespace test cases quietly deleted, `times(3)` became one call | Don't move the gate. |
| 5 | `OwnerController.processFindForm` | Dropped `strip()`. " Franklin" no longer finds anyone. | Scope creep. Review every change. |
| 6 | `OwnerControllerTests` | `"   "` changed to `""` so a test passes | Don't move the gate. |
| 7 | `OwnerSearchController` | Logs whole owners, with address and phone | Never log personal data. |
| 8 | `OwnerSearchResult` | Returns address and phone to any caller | Return only what's needed. |
| 9 | `PetNameOwnerSearchStrategy` | `catch (Exception)` returns an empty list. "O'Brien" fails silently. | Don't swallow errors. |
| 10 | `OwnerSearchController` loop | A query per pet (N+1) | Know your data access. |
| 11 | `OwnerSearchController` | A blank search returns everyone. No paging. | Bound every query. |
| 12 | `OwnerSearchStrategy` + factory | An interface and factory for one class | Abstractions earn their place. |
| 13 | `pom.xml` | Adds commons-lang3 3.12.0 for what Java does. Known CVE. | Every dependency costs something. |

Also: `spring.jpa.query-cache.enabled=true` in `application.properties` doesn't exist. Spring ignores
it. The PR says caching is on. (Hallucinated config. Counts toward 13 with #4 and #6 as one.)

## The point

The tests passed, and the PR description was confident and wrong. An agent wrote the slop, and an
agent can catch it. The fix wasn't a better model. It was a required review before merging.

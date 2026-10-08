# Comparison rubric

Score each plan. 0 = missing or wrong. 1 = mentioned but vague. 2 = clear and right for this
codebase. Check the code when you're not sure. A plan can sound right and still be wrong.

| # | Does the plan... | One-liner | Spec |
|---|---|---|---|
| 1 | Say what "track health over time" means (current weight, or a history), or ask you? | | |
| 2 | Keep weight optional, so existing pets still load, save and show cleanly? | | |
| 3 | Define a valid weight: range, decimals, and what happens with bad input? | | |
| 4 | Validate where the form actually checks it? (Look at `@InitBinder` in `PetController`.) | | |
| 5 | Cover editing a pet, not just adding one? (Look at `updatePetDetails`.) | | |
| 6 | Change the schema and the seed data for every database the app supports? | | |
| 7 | Cover the form field and how the owner page shows the weight? | | |
| 8 | Handle labels and messages in every language file? | | |
| 9 | Name real tests, and keep the existing tests as they are? | | |
| 10 | Stay in scope? No extra API, no history table, no new dependency you didn't ask for. | | |
| 11 | Follow patterns already in the codebase instead of inventing new ones? | | |
| 12 | Ask you at least one question before writing code? | | |
| | **Total (out of 24)** | | |

Also write down:

| | One-liner | Spec |
|---|---|---|
| Minutes from start to plan | | |
| Files it planned to change | | |
| The riskiest thing it assumed | | |
| The best thing it caught that you didn't | | |

One question to finish: which plan would you approve as a pull request without reading the code?
(Trick question. Neither.)

# Spec: Pet weight

The spec for the lab. Read it, then give the whole thing to your agent.

## Goal

Vets want to spot a pet that is gaining or losing weight. Record the pet's weight at each visit, so
we can see how it changes over time.

## What changes

- The visit form (**Add Visit**) gets an optional field: **Weight (kg)**.
- The owner page shows the weight next to each visit. Each pet's visit list becomes its weight
  history.
- A visit with no weight shows nothing. Never `null`, never `0.00 kg`.

## Edge cases

- **Existing visits have no weight.** They must still load and show like before.
- **Valid weight:** 0.01 to 999.99 kg, two decimals at most. Anything else gets a clear message on
  the form.
- **Not a number** (like "heavy") gets a friendly message, not an error page.
- **The sample data has to keep loading.** Some of the scripts insert visits without column names.

## Tests

Each line is a test. Write them first.

- Adding a visit with a weight saves it.
- Adding a visit without a weight works.
- A weight of 0, -1, 1000 or 4.567 shows an error on the visit form.
- "heavy" shows a friendly message.
- The owner page shows the weight for each visit, like `4.50 kg`.
- The owner page shows `0.05 kg` for a tiny hamster.
- The owner page never shows `null` for a visit with no weight.

## How to build it

- The weight belongs on `Visit`. The visit form already validates with Bean Validation, like
  `description` does. Follow that.
- Update the database scripts for H2, MySQL and Postgres.
- Add the new labels to every language file.
- Don't change existing tests. No new dependencies.

## Not in this change

- Charts or trends
- Pounds
- Editing past visits
- A REST API

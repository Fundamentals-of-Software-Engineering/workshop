# Spec: Pet weight

The spec for the lab. Read it, then give the whole thing to your agent.

## Goal

Vets want to spot a pet that is gaining or losing weight. For now, we only store each pet's
**current** weight and show it on the owner page. A weight history comes later.

## What changes

- The pet form (add and edit) gets an optional field: **Weight (kg)**.
- The owner page shows the weight under each pet, like `4.50 kg`.
- A pet with no weight shows nothing. Never `null`, never `0.00 kg`.

## Edge cases

- **Existing pets have no weight.** They must still load, show and save like before.
- **Valid weight:** 0.01 to 999.99 kg, two decimals at most. Anything else gets a clear message on
  the form.
- **Not a number** (like "heavy") gets a friendly message, not an error page.
- **Editing a pet** can add, change or clear the weight. Don't lose it on save.

## Tests

Each line is a test. Write them first.

- Adding a pet with a weight saves it.
- Adding a pet without a weight works.
- Editing a pet can change its weight.
- Editing a pet can clear its weight.
- A weight of 0, -1, 1000 or 4.567 shows an error on the form.
- "heavy" shows a friendly message.
- The owner page shows `0.05 kg` for a tiny hamster.
- The owner page never shows `null` for a pet with no weight.

## How to build it

- Follow the patterns already in the code. Pet form checks go in `PetValidator`.
- Update the database scripts for H2, MySQL and Postgres. The sample data has to keep loading.
- Add the new labels to every language file.
- Don't change existing tests. No new dependencies.

## Not in this change

- Weight history or charts
- Pounds
- A REST API

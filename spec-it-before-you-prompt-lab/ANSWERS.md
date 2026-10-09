# Spec It Before You Prompt: answers

"Add an optional weight field (in kg) to pets so we can track their health over time."

## What a one-line prompt usually misses

1. **"Over time."** One weight column is the latest value, not a history. Decide, don't drift.
2. **Seed data.** The H2 and MySQL `data.sql` inserts have no column names. Add a column to
   `schema.sql` only and the app won't start.
3. **Validation.** `PetController` sets `PetValidator` with `@InitBinder`, so `@Positive` on `Pet`
   doesn't run on the form. A negative weight becomes an HTTP 500.
4. **Editing.** `updatePetDetails` copies fields one by one. Forget weight there and edits are lost,
   silently.
5. **Mocked tests hide #4.** A `@WebMvcTest` with a mocked repository passes anyway.
6. **Translations.** A new message key must be in all 9 language files, or `I18nPropertiesSyncTest`
   fails.
7. **Display.** Existing pets have no weight. Without a check, the page shows "null kg".
8. **Bad input.** "abc" shows a raw conversion error unless you add a `typeMismatch.weight` message.

## What good looks like

`example-spec.md` in this lab, and `reference.diff` on this branch. The spec's 11 tests fail on the
pinned commit (10 red) and pass on the reference build.

## The point

English is clear to you and vague to an agent. Diagrams don't compile. Neither do specs. Tests do.

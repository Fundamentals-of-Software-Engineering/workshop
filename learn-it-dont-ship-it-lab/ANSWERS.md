# Learn It, Don't Ship It: answers

## The task

- **Rule 1** (`web` never uses `repository`) fails 3 times. `VisitController` takes the repository
  as a field and a constructor parameter, and calls it. Fix: go through `VisitService`.
- **Rule 2** (`domain` depends only on `domain`) needs `"java.."` allowed too, or it fails 19 times.
- **Rule 3** (`*Repository` lives in `repository`) passes. Not every rule finds something.

Reference solution: `solution/` in this folder on this branch.

## Quiz

1. **B.** ArchUnit reads compiled bytecode. The compiler drops unused imports.
2. **C,** `"..web.."`. `..` means any number of packages. `"..web"` misses subpackages.
3. `Visit` (domain) calls `DateFormats` (web) on line 10. Fix: format the date in the web layer, or
   move `DateFormats` out of `web`. Changing the rule isn't a fix.
4. No class lives in `..controllers..`, so the rule checked nothing. ArchUnit fails empty rules on
   purpose. Fix the pattern.
5. **B.** "Only depend on" means only. `String`, `Object` and `LocalDate` count. Allow `"java.."`.

One point each. For 3 and 4, the main idea gets the point.

## The point

Shen and Tamkin (2026): the group that learned with AI scored 17% lower, and the biggest gap was
debugging. People who handed everything off learned least. Never written it? Write it once yourself.

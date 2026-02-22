# Writing Clean Code Lab

A Pet Weight Tracker with intentionally smelly code. Your job: find the smells and fix them.

## Setup

No build or IDE required. Open the files in `src/` in any text editor.

## Exercise (15 minutes)

### Part 1: Identify Code Smells (5 min)

Read through the files in `src/` and identify as many code smells as you can. Consider:

- **Naming** - Are variable and method names clear?
- **Comments** - Are they helpful, misleading, or redundant?
- **Methods** - Are they focused on one thing?
- **DRY** - Is there duplicated logic?
- **Inheritance** - Is the class hierarchy appropriate for all subclasses?
- **Formatting** - Does the indentation accurately reflect the control flow?

Write down at least 3 smells you find.

### Part 2: Refactor (8 min)

Pick 2-3 of the smells you found and refactor the code. Focus on the changes that would have the biggest impact on readability and maintainability.

### Part 3: Discuss (2 min)

Share what you found with your neighbor. Did you catch the same things? Different things?

## Files

| File | What to look at |
|------|----------------|
| `Animal.java` | Is this hierarchy right for every animal? |
| `Dog.java` | This one's fine. Good baseline. |
| `Fish.java` | What does it inherit that it shouldn't? |
| `WeightTracker.java` | The main event. Lots to find here. |
| `WeightTrackerUtils.java` | Compare this to WeightTracker. |

## Quick Reference: Clean Code Principles

| Principle | One-liner |
|-----------|-----------|
| Meaningful Names | Names should reveal intent (`elapsedTimeInDays` not `d`) |
| Small Methods | Each method should do one thing |
| DRY | Don't repeat yourself — extract shared logic |
| Comments | Don't restate the code; explain *why*, not *what* |
| Formatting | Indentation should reflect actual control flow |
| Favor Composition | Prefer composition over inheritance when "is-a" doesn't hold |

## Solution

A refactored version is available on the `solution` branch. There's no single "right" answer — look at it after you've done your own refactoring to compare approaches.

```bash
git checkout solution
```

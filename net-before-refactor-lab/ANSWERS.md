# Net Before Refactor: answers

## Odd behaviors worth pinning (don't fix them)

1. **Age is in calendar years.** A puppy born in December is "1" in January.
2. **Seniors pay from age 11.** The comment says 10. The code wins.
3. **Saturday adds $20. Sunday adds nothing.**
4. **A free follow-up still pays the Saturday $20.**
5. **Any description with "follow" in it is free.**

Also: the fee rounds up to the next whole dollar after the discount, and the 10% multi-pet discount
skips the Saturday surcharge.

## What the agent's refactor changes

It says "No behavior changes." It changes three things:

| Change | Before | After |
|---|---|---|
| Rounds to the cent, not up to the next dollar | $68.00 | $67.50 |
| Seniors from 10, trusting the comment | $45.00 | $60.00 |
| Follow-ups free even on Saturday | $20.00 | $0.00 |

Seven reference tests go red. All of PetClinic's own tests stay green. The smallest net that
catches all three: a fee with cents, a 10-year-old dog, and a Saturday follow-up.

Reference tests: `solution/VisitFeeCalculatorTests.java` on this branch.

## The point

"Tests pass" only means the tests you have pass. Pin what the code does today, bugs included, before
anyone refactors it.

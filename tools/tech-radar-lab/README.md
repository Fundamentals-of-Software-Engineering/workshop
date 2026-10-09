# Your Personal Tech Radar

**Optional extra content.** Use it if there's time at the end of the day, or take it home.

New AI tools show up every week. You can't learn them all, and you don't need to. "Random learning
produces random results." A personal tech radar helps you decide what to learn on purpose, and what
to ignore for now.

The idea comes from the Thoughtworks Technology Radar. They place tools and techniques in rings
based on how ready they are. You'll do the same for yourself.

## The four rings

| Ring | For you, it means |
|---|---|
| Adopt | You use it and you're getting good at it. Your core skills. |
| Trial | You're learning it or trying it on something real. |
| Assess | You're reading about it. You haven't started. |
| Hold | You've decided not to spend time on it right now. |

Place each item where **you** are with it, not where the industry is. Hold isn't "bad." It's "not
now."

Thoughtworks now calls its outer ring **Caution**. For a personal radar, Hold still fits.

## The lab

### Part 1: Look at a real radar

Open the Thoughtworks Technology Radar at https://www.thoughtworks.com/radar.

- Pick a quadrant you care about. What's in Adopt? What's in the outer ring?
- Find something that moved since the last edition. Why did it move?

### Part 2: Build your own

There are no wrong answers. This is your radar.

1. List 5 to 10 tools, languages or techniques you know or want to learn. Paper or a spreadsheet is
   fine.
2. For each one, write the name, the ring, the quadrant and one sentence on why.
3. Include at least two AI tools or practices. Put at least one thing in Hold.
4. Mark what's new to you since last quarter.

See `sample-radar.csv` in this folder for an example with AI tools. To see it drawn as a radar,
open this link:
[the sample radar](https://radar.thoughtworks.com/?documentId=https%3A%2F%2Fraw.githubusercontent.com%2FFundamentals-of-Software-Engineering%2Fworkshop%2Fmain%2Ftools%2Ftech-radar-lab%2Fsample-radar.csv).

Don't try to draw your own radar now. Paper is faster. Drawing it is a take-home step, below.

### Part 3: Share

Show a neighbor one item in Hold and say why. Ask what they put in Adopt.

## Make it visual (optional)

The Build Your Own Radar tool at https://radar.thoughtworks.com draws your radar from a Google
Sheet, a CSV or a JSON file.

- Use these columns, in this order: `name`, `ring`, `quadrant`, `isNew`, `description`.
- Rings: `adopt`, `trial`, `assess`, `hold`. The tool now calls the fourth ring **Caution**. It
  still accepts `hold` and shows it as Caution.
- Quadrants on radar.thoughtworks.com must be these four: `techniques`, `platforms`, `tools`,
  `languages & frameworks`.
- `isNew` is `TRUE` or `FALSE`.
- A CSV has to be at a public URL, like a GitHub gist's raw link. A Google Sheet has to be shared.
  Don't put anything private in it.

The easiest start:

1. In Google Sheets, create a blank sheet. Choose File, then Import, and upload `sample-radar.csv`.
2. Replace the rows with your own. Keep the header row as it is.
3. Click Share. Set General access to "Anyone with the link" as a Viewer.
4. Paste the sheet's link into https://radar.thoughtworks.com and click "Build my radar."

No Google account? Put your CSV in a public GitHub gist. Paste the gist's raw link into the tool.

## Tips

- Keep your first radar small. 5 to 10 items is plenty.
- Use every quadrant, so you can see how broad your skills are.
- Put a reminder in your calendar to review it once a quarter. Move things between rings. Delete
  what you dropped.
- Use it to say no. When the next big AI tool shows up, ask where it goes on your radar. Assess is a
  fine answer.
- Share it with a mentor or your manager. It's a good way to start a career conversation.

## Learn more

- Build Your Own Radar on GitHub: https://github.com/thoughtworks/build-your-own-radar
- Thoughtworks Radar FAQ: https://www.thoughtworks.com/radar/faq
- Fundamentals of Software Engineering, chapter 14 (career management)

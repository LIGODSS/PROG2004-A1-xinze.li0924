# Personal Submission Guide

Prepared for Xinze Li (student number 25258952).

## 1. Run the project before recording

Open the `A1-xinze.li0924` folder in IntelliJ IDEA, Eclipse or VS Code.

From a terminal in the project root:

```text
javac -d out src/*.java
java -cp out AssignmentOne
```

Run it twice and make sure you understand each output section. The booking date
uses `LocalDate.now()`, so the displayed day changes automatically.

## 2. Create the GitHub evidence

Recommended repository name:

```text
PROG2004-A1-xinze.li0924
```

Set the repository to private. Use meaningful commits rather than one final
upload. A suitable progression is:

1. `Add validated health professional hierarchy`
2. `Add patient and appointment domain models`
3. `Implement clinic schedule and appointment manager`
4. `Add end-to-end appointment demonstration`
5. `Add design notes and submission documentation`

After development is finished, continue to make small documentation or
comment commits if you change anything. Do not rewrite the commit dates.

If the repository is private, ask your tutor and unit assessor for their exact
GitHub usernames and invite them as collaborators before submitting.

## 3. Prepare WakaTime evidence

1. Install the WakaTime plugin for your IDE before you edit or run the project.
2. Open the project and work through the source files so activity is recorded.
3. Keep genuine working sessions while you study and test the code.
4. Export the report as `A1-xinze.li0924-WakaTime.png`, `.jpg` or `.pdf`.
5. Check that the report shows meaningful time on the Java project.

Do not fabricate time or screenshots. The report should represent actual work.

## 4. Record the video

Keep the recording below five minutes. Use [OOP_VIDEO_GUIDE.md](OOP_VIDEO_GUIDE.md)
as the script. A practical shot list is:

1. Show the project structure and run the program.
2. In `HealthProfessional.java`, explain abstraction.
3. Show private fields and the read-only working-day collection to explain
   encapsulation.
4. Show `Doctor`, `GeneralPractitioner` and `Specialist` to explain inheritance.
5. Show the `List<HealthProfessional>` display loop and `toString()` dispatch to
   explain polymorphism.
6. Show one successful booking, one double-booking warning and cancellation.

Use Windows Game Bar (`Win + G`), OBS Studio or another screen recorder. Speak
clearly, enlarge the IDE font, and point to the exact file and method each time.

Upload the finished video to your SCU OneDrive. Set sharing so the tutor and
unit assessor can view it without requesting access.

## 5. Blackboard submission

Prepare these items:

- `A1-xinze.li0924.zip`
- Private GitHub repository invite sent to the tutor and unit assessor
- `A1-xinze.li0924-WakaTime.png`, `.jpg` or `.pdf`
- Shareable SCU OneDrive video link
- GenAI declaration

Add the GitHub link and video link in the Blackboard comments. Copy the GenAI
statement from `GenAI_DECLARATION.txt` into the comments exactly as required by
the brief. Upload the ZIP and WakaTime report, then retain the submission
receipt.

## 6. Five-minute interview preparation

Be ready to explain:

- why `HealthProfessional` and `Doctor` are abstract;
- why health professionals use ID-based equality;
- why working days are copied into an `EnumSet`;
- why `Appointment` implements `Comparable`;
- why `AppointmentManager` owns double-booking validation;
- the difference between overriding and overloading in this project;
- why the manager returns read-only appointment snapshots.

You should be able to locate each of these decisions without searching.

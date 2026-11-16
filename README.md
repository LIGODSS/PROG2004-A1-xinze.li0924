# A1-xinze.li0924 - Health Clinic Appointment System

This project is a Java console simulation for PROG2004 Assessment 1. It models
health professionals, patients, appointments and a clinic-wide appointment
manager without a GUI or persistent storage.

## Main features

- Abstract `HealthProfessional` base class with validated numeric ID, name and
  fixed working days.
- Concrete General Practitioner, Specialist, Nurse and Dietitian types.
- Multi-level inheritance through `Doctor`, which shares registration and
  prescription behaviour between GP and Specialist.
- Runtime polymorphism when every role is displayed through a
  `HealthProfessional` reference.
- Encapsulated, immutable common data and defensive copies of working-day
  collections.
- Separate `Patient`, `Appointment`, `ClinicSchedule`, `OperationResult` and
  `AppointmentManager` domain classes.
- Current-day booking restriction and configurable 08:00-16:00 / 30-minute
  clinic schedule.
- Working-day validation, slot validation and professional double-booking
  prevention.
- Filtering by professional ID or patient mobile number.
- Chronological sorting through `Appointment implements Comparable`.
- Cancellation by professional ID and time, or by patient mobile number.
- Clear success messages and warning messages for every operation.
- Fully scripted normal and error-case demonstration in `AssignmentOne.main`.

## Project structure

```
src/
  AssignmentOne.java
  HealthProfessional.java
  Doctor.java
  GeneralPractitioner.java
  Specialist.java
  Nurse.java
  Dietitian.java
  Patient.java
  Appointment.java
  AppointmentManager.java
  ClinicSchedule.java
  OperationResult.java
  Validator.java
docs/
  DESIGN_DECISIONS.md
  OOP_VIDEO_GUIDE.md
  SUBMISSION_CHECKLIST.md
GenAI_DECLARATION.txt
```

## Compile and run

From the project root:

```text
javac -d out src/*.java
java -cp out AssignmentOne
```

If your IDE requires a specific Java version, use a JDK that supports
`java.time` and `java.util`. The code intentionally avoids third-party
libraries and advanced syntax so the design stays within introductory
object-oriented Java concepts.

## Important before submission

1. Rename the project folder and ZIP to use your MySCU username, for example
   `A1-lfolkman`.
2. Complete the GenAI declaration with the tool name and usage that is accurate
   for you.
3. Replace the GitHub, WakaTime and video placeholders in the submission
   checklist with your real links or files.
4. Create GitHub commits progressively rather than uploading the whole project
   in a single final commit.
5. Record the five-minute video using the supplied OOP video guide.

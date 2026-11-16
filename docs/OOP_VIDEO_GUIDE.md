# Five-Minute OOP Video Guide

## Suggested timing

| Time | Content |
| --- | --- |
| 0:00-0:25 | Introduce the project and run the console demonstration |
| 0:25-1:10 | Abstraction |
| 1:10-1:55 | Encapsulation |
| 1:55-2:50 | Inheritance |
| 2:50-3:50 | Polymorphism |
| 3:50-4:45 | Show one normal booking, one warning and cancellation |
| 4:45-5:00 | Conclude with design benefits |

Keep each explanation in the order required by the brief: name the pillar,
explain it in your own words, point to the exact code, and justify the design
choice.

## Abstraction

Point to `HealthProfessional.java`.

- `HealthProfessional` is abstract because there is no valid plain health
  professional in this system.
- The base class exposes what every professional must be able to do:
  `getRoleName()`, `getRoleSpecificDetails()` and `getSampleService()`.
- `Doctor` is also abstract because it captures shared doctor behaviour but
  is not a bookable concrete role.
- Explain that the caller does not need to know the implementation details of
  every role.

## Encapsulation

Point to `HealthProfessional.java`, `Patient.java`, `Validator.java` and
`AppointmentManager.java`.

- Private fields prevent invalid direct changes.
- Constructors validate IDs, names, mobile numbers and working days.
- Working days are copied and returned through a read-only view.
- Manager collection changes happen through controlled booking and cancellation
  methods.
- Query methods return read-only sorted snapshots.

## Inheritance

Point to `Doctor.java`, `GeneralPractitioner.java`, `Specialist.java`,
`Nurse.java` and `Dietitian.java`.

- All roles inherit common ID, name, days and formatting from
  `HealthProfessional`.
- GP and Specialist inherit registration and prescription support from
  `Doctor`.
- Subclasses add only their unique details.
- Explain that this removes duplicated fields and validation.

## Polymorphism

Point to `AssignmentOne.createHealthProfessionals()` and the display loop in
`demonstrateHealthProfessionals()`.

- All concrete roles are stored in `List<HealthProfessional>`.
- Calling `toString()` or `getSampleService()` through the base type invokes
  the concrete object's implementation.
- This allows a new role to be added and processed together without changing
  the loop.
- Appointment also polymorphically holds any `HealthProfessional` subtype.

## Live demonstration

1. Show the professional profiles.
2. Show a successful booking and the detailed Appointment object.
3. Show the double-booking warning.
4. Show invalid-slot, future-day and unavailable-professional warnings.
5. Show filtering by professional ID and patient phone.
6. Show time sorting.
7. Show cancellation by professional/time and by phone.

## Speaking tips

- Use "the base type" and "the concrete type" clearly.
- Say why the design choice helps future extension, not only what the syntax
  does.
- Keep the IDE font large enough to read on video.
- Rehearse with a timer and stay below five minutes.

# Design Decisions

## Class responsibilities

| Class | Responsibility |
| --- | --- |
| `HealthProfessional` | Common identity, name, working days and polymorphic profile contract |
| `Doctor` | Doctor-only registration and prescription behaviour |
| `GeneralPractitioner` | GP role information and general-practice service |
| `Specialist` | Specialist field, experience and specialist service |
| `Nurse` | Nursing registration, clinic area and blood-sampling behaviour |
| `Dietitian` | Nutrition focus and dietitian service |
| `Patient` | Validated patient name and mobile phone |
| `ClinicSchedule` | Fixed daily slots and configurable schedule boundaries |
| `Appointment` | Complete booking and natural time ordering |
| `OperationResult` | Consistent operation feedback |
| `AppointmentManager` | Collection-level booking rules and operations |
| `Validator` | Shared input validation |
| `AssignmentOne` | Scripted end-to-end system demonstration |

## Why HealthProfessional is abstract

A generic professional with no real role would not know what service it
provides. The abstract class forces every subtype to supply a role name,
role-specific details and service description.

The base class owns only information common to all professionals. This avoids
duplicated fields and centralises validation and display logic.

## Why Doctor is an intermediate class

A GP and a Specialist are both doctors. Medical registration and prescription
behaviour belong to that shared doctor layer, not to every health professional
and not separately to each doctor subtype. This creates a useful inheritance
hierarchy without making the design unnecessarily deep.

## Why ID-based equality is used

The clinic ID uniquely identifies one health professional. Equality based on
object memory location would incorrectly treat two objects with the same valid
ID as different people. ID-based `equals` and `hashCode` also make
double-booking checks reliable.

## Why Appointment implements Comparable

Natural ordering belongs to Appointment because comparing two bookings by
start time is the primary way appointments are viewed. The manager can then
sort all appointments or a filtered subset with standard Java collection
behaviour.

## Why ClinicSchedule is separate

The hours and slot length are policy rather than appointment data. Separating
them allows the manager to use a different schedule constructor without
rewriting booking, filtering or cancellation logic.

## Why an OperationResult object is returned

Expected user mistakes should produce controlled warnings, not crash the
program with exceptions. A result object returns both a success flag and a
message. For a successful creation, it can also return the new Appointment.

## Why appointments are stored in an ArrayList

The system needs to keep all appointments together and process them in
sequence. `ArrayList` provides straightforward indexed collection storage and
works with standard sorting. Ordering by time is applied when a snapshot is
returned, so every display is chronological.

## Defensive copies and immutability

Health professional, patient, appointment and schedule fields are final.
Working days are copied into an `EnumSet` and exposed as a read-only set.
Manager query methods return read-only sorted snapshots so external code
cannot accidentally change the internal collection.

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Coordinates all appointment operations for the clinic.
 *
 * <p>The manager centralises booking rules such as current-day bookings,
 * fixed clinic slots, working days and double-booking prevention. Keeping
 * these rules here means individual Appointment objects remain simple data
 * holders with ordering behaviour.</p>
 */
public final class AppointmentManager {

    private final List<Appointment> appointments;
    private final LocalDate bookingDate;
    private final ClinicSchedule schedule;

    /**
     * Creates a manager for the current day using the default clinic schedule.
     */
    public AppointmentManager() {
        this(LocalDate.now(), new ClinicSchedule());
    }

    /**
     * Creates a manager with explicit date and schedule dependencies.
     *
     * <p>The overload is useful for testing and also shows why dependencies
     * should be supplied from outside instead of hidden inside methods.</p>
     *
     * @param bookingDate the only date on which bookings are allowed
     * @param schedule the clinic's available appointment slots
     */
    public AppointmentManager(LocalDate bookingDate, ClinicSchedule schedule) {
        if (bookingDate == null) {
            throw new IllegalArgumentException("Booking date is required.");
        }
        if (schedule == null) {
            throw new IllegalArgumentException("Clinic schedule is required.");
        }
        this.bookingDate = bookingDate;
        this.schedule = schedule;
        this.appointments = new ArrayList<>();
    }

    /**
     * Attempts to add a complete appointment.
     *
     * <p>The order of checks deliberately gives specific feedback. The method
     * validates the current-day rule, clinic slot and professional schedule
     * before checking for a booking conflict.</p>
     *
     * @param patient the patient attending
     * @param healthProfessional the selected professional
     * @param date the proposed date
     * @param time the proposed start time
     * @return success with the new appointment, or a warning explaining why
     *         the booking could not be added
     */
    public OperationResult addAppointment(
            Patient patient,
            HealthProfessional healthProfessional,
            LocalDate date,
            LocalTime time) {
        if (patient == null || healthProfessional == null || date == null || time == null) {
            return OperationResult.failure(
                    "Patient, health professional, date and time are all required.");
        }
        if (!bookingDate.equals(date)) {
            return OperationResult.failure(
                    "Bookings are allowed for the current day only: " + bookingDate + ".");
        }
        if (!schedule.isValidAppointmentTime(time)) {
            return OperationResult.failure(
                    "The time " + time + " is not a valid clinic slot. " + schedule + ".");
        }
        if (!healthProfessional.worksOn(date.getDayOfWeek())) {
            return OperationResult.failure(
                    healthProfessional.getName() + " does not work on "
                            + date.getDayOfWeek() + ".");
        }
        if (hasProfessionalConflict(healthProfessional.getId(), time)) {
            return OperationResult.failure(
                    "Double-booking rejected: " + healthProfessional.getName()
                            + " already has an appointment at " + time + ".");
        }

        Appointment appointment =
                new Appointment(patient, healthProfessional, date, time);
        appointments.add(appointment);
        return OperationResult.success(
                "Appointment booked for " + patient.getName() + " at " + time + ".",
                appointment);
    }

    /**
     * Checks whether a professional already has a booking at the given time.
     *
     * <p>The comparison uses the professional ID, which agrees with ID-based
     * HealthProfessional equality and prevents two different objects that
     * represent the same real professional from being double-booked.</p>
     *
     * @param professionalId the professional ID to check
     * @param time the appointment time to check
     * @return true when a conflict exists
     */
    private boolean hasProfessionalConflict(String professionalId, LocalTime time) {
        for (Appointment appointment : appointments) {
            if (appointment.matches(professionalId, time)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns every appointment in chronological order.
     *
     * @return an unmodifiable, time-sorted snapshot
     */
    public List<Appointment> getAllAppointments() {
        return sortedSnapshot(appointments);
    }

    /**
     * Filters appointments by the exact health professional ID.
     *
     * @param professionalId the professional ID to find
     * @return a time-sorted, unmodifiable snapshot
     */
    public List<Appointment> findAppointmentsByProfessional(String professionalId) {
        List<Appointment> matches = new ArrayList<>();
        if (professionalId == null) {
            return Collections.unmodifiableList(matches);
        }

        String targetId = professionalId.trim();
        for (Appointment appointment : appointments) {
            if (appointment.getHealthProfessional().getId().equals(targetId)) {
                matches.add(appointment);
            }
        }
        return sortedSnapshot(matches);
    }

    /**
     * Filters appointments by a patient's normalised mobile number.
     *
     * @param mobileNumber the mobile number to find
     * @return a time-sorted, unmodifiable snapshot
     */
    public List<Appointment> findAppointmentsByPatientPhone(String mobileNumber) {
        List<Appointment> matches = new ArrayList<>();
        if (mobileNumber == null) {
            return Collections.unmodifiableList(matches);
        }

        final String targetNumber;
        try {
            targetNumber = Validator.normaliseAustralianMobile(mobileNumber);
        } catch (IllegalArgumentException exception) {
            return Collections.unmodifiableList(matches);
        }

        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getMobileNumber().equals(targetNumber)) {
                matches.add(appointment);
            }
        }
        return sortedSnapshot(matches);
    }

    /**
     * Cancels the appointment matching a professional ID and start time.
     *
     * @param professionalId the professional ID
     * @param time the appointment start time
     * @return a success or warning result
     */
    public OperationResult cancelByProfessional(String professionalId, LocalTime time) {
        if (professionalId == null || time == null) {
            return OperationResult.failure(
                    "Professional ID and appointment time are required for cancellation.");
        }

        String targetId = professionalId.trim();
        Iterator<Appointment> iterator = appointments.iterator();
        while (iterator.hasNext()) {
            Appointment appointment = iterator.next();
            if (appointment.matches(targetId, time)) {
                iterator.remove();
                return OperationResult.success(
                        "Cancelled appointment for "
                                + appointment.getHealthProfessional().getName()
                                + " at " + time + ".");
            }
        }
        return OperationResult.failure(
                "No appointment exists for professional ID " + targetId + " at " + time + ".");
    }

    /**
     * Cancels every appointment belonging to a patient mobile number.
     *
     * <p>Removing by patient can affect more than one booking, so the return
     * message includes the number cancelled.</p>
     *
     * @param mobileNumber the patient's mobile number
     * @return a success or warning result
     */
    public OperationResult cancelByPatientPhone(String mobileNumber) {
        final String targetNumber;
        try {
            targetNumber = Validator.normaliseAustralianMobile(mobileNumber);
        } catch (IllegalArgumentException exception) {
            return OperationResult.failure(
                    "A valid patient mobile number is required for cancellation.");
        }

        int cancelledCount = 0;
        Iterator<Appointment> iterator = appointments.iterator();
        while (iterator.hasNext()) {
            Appointment appointment = iterator.next();
            if (appointment.getPatient().getMobileNumber().equals(targetNumber)) {
                iterator.remove();
                cancelledCount++;
            }
        }

        if (cancelledCount == 0) {
            return OperationResult.failure(
                    "No appointments exist for patient mobile " + targetNumber + ".");
        }
        return OperationResult.success(
                "Cancelled " + cancelledCount + " appointment(s) for patient mobile "
                        + targetNumber + ".");
    }

    /**
     * Creates an immutable list sorted by the natural Appointment order.
     *
     * @param source the appointments to copy and sort
     * @return a time-sorted unmodifiable snapshot
     */
    private List<Appointment> sortedSnapshot(List<Appointment> source) {
        List<Appointment> snapshot = new ArrayList<>(source);
        Collections.sort(snapshot);
        return Collections.unmodifiableList(snapshot);
    }

    /**
     * Returns the only date on which this manager accepts bookings.
     *
     * @return the current booking date
     */
    public LocalDate getBookingDate() {
        return bookingDate;
    }

    /**
     * Returns the clinic schedule used by this manager.
     *
     * @return the immutable schedule reference
     */
    public ClinicSchedule getSchedule() {
        return schedule;
    }
}

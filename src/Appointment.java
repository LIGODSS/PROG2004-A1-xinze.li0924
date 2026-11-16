import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents a booking between one patient and one health professional.
 *
 * <p>Appointment implements Comparable so a collection can be sorted by
 * appointment time. It stores the professional through the abstract
 * HealthProfessional type, allowing any concrete professional type to be
 * booked without changing this class.</p>
 */
public final class Appointment implements Comparable<Appointment> {

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.ENGLISH);

    private final Patient patient;
    private final HealthProfessional healthProfessional;
    private final LocalDate date;
    private final LocalTime time;

    /**
     * Creates a complete appointment.
     *
     * @param patient the patient attending
     * @param healthProfessional the chosen professional
     * @param date the booking date
     * @param time the appointment start time
     */
    public Appointment(
            Patient patient,
            HealthProfessional healthProfessional,
            LocalDate date,
            LocalTime time) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient is required.");
        }
        if (healthProfessional == null) {
            throw new IllegalArgumentException("Health professional is required.");
        }
        if (date == null) {
            throw new IllegalArgumentException("Appointment date is required.");
        }
        if (time == null) {
            throw new IllegalArgumentException("Appointment time is required.");
        }

        this.patient = patient;
        this.healthProfessional = healthProfessional;
        this.date = date;
        this.time = time;
    }

    /**
     * Returns the patient.
     *
     * @return the appointment patient
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Returns the health professional through the common base type.
     *
     * @return the chosen health professional
     */
    public HealthProfessional getHealthProfessional() {
        return healthProfessional;
    }

    /**
     * Returns the appointment date.
     *
     * @return the booking date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the appointment start time.
     *
     * @return the booking time
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Compares appointments by start time first.
     *
     * <p>When two professionals have appointments at the same time, the
     * professional ID provides a deterministic secondary order.</p>
     *
     * @param other the appointment to compare
     * @return a negative number, zero or a positive number
     */
    @Override
    public int compareTo(Appointment other) {
        int timeComparison = time.compareTo(other.time);
        if (timeComparison != 0) {
            return timeComparison;
        }

        int professionalComparison =
                healthProfessional.getId().compareTo(other.healthProfessional.getId());
        if (professionalComparison != 0) {
            return professionalComparison;
        }

        return patient.getMobileNumber().compareTo(other.patient.getMobileNumber());
    }

    /**
     * Returns a compact one-line appointment summary for lists and filters.
     *
     * @return time, patient and professional information
     */
    public String toSummaryString() {
        return time.format(TIME_FORMAT)
                + " | " + patient.getName()
                + " (" + patient.getFormattedMobileNumber() + ")"
                + " | " + healthProfessional.getRoleName()
                + " " + healthProfessional.getName()
                + " [" + healthProfessional.getId() + "]";
    }

    /**
     * Checks whether the appointment is with a professional ID and at a time.
     *
     * @param professionalId the professional ID to match
     * @param appointmentTime the appointment time to match
     * @return true when both details match
     */
    public boolean matches(String professionalId, LocalTime appointmentTime) {
        return healthProfessional.getId().equals(professionalId)
                && time.equals(appointmentTime);
    }

    /**
     * Produces the full appointment representation, including the complete
     * selected health professional profile.
     *
     * @return a detailed, readable appointment
     */
    @Override
    public String toString() {
        String professionalDetails =
                healthProfessional.toString().replace(
                        NEW_LINE, NEW_LINE + "    ");

        return "Appointment" + NEW_LINE
                + "  Date          : " + date.format(DATE_FORMAT) + NEW_LINE
                + "  Time          : " + time.format(TIME_FORMAT) + NEW_LINE
                + "  Patient       : " + patient.toString() + NEW_LINE
                + "  Professional  :" + NEW_LINE
                + "    " + professionalDetails;
    }

    /**
     * Compares all recorded booking details.
     *
     * @param other the object to compare
     * @return true when patient, professional, date and time all match
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Appointment)) {
            return false;
        }
        Appointment appointment = (Appointment) other;
        return patient.equals(appointment.patient)
                && healthProfessional.equals(appointment.healthProfessional)
                && date.equals(appointment.date)
                && time.equals(appointment.time);
    }

    /**
     * Keeps hashCode consistent with all booking details.
     *
     * @return a hash of the recorded appointment fields
     */
    @Override
    public int hashCode() {
        return Objects.hash(patient, healthProfessional, date, time);
    }

    private static final String NEW_LINE = System.lineSeparator();
}

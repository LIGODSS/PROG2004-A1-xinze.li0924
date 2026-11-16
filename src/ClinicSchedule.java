import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the fixed time slots in a clinic day.
 *
 * <p>The default is the required 08:00-16:00 schedule with 30-minute
 * appointments. The constructor also accepts different values, so the
 * appointment manager can work with a new clinic schedule without changing
 * its booking logic.</p>
 */
public final class ClinicSchedule {

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm");

    private final LocalTime openingTime;
    private final LocalTime closingTime;
    private final int slotLengthMinutes;
    private final List<LocalTime> appointmentTimes;

    /**
     * Creates the required default schedule: 08:00-16:00 in 30-minute slots.
     */
    public ClinicSchedule() {
        this(LocalTime.of(8, 0), LocalTime.of(16, 0), 30);
    }

    /**
     * Creates a configurable clinic schedule.
     *
     * <p>The closing time is treated as the end of the clinic day and is not
     * itself an appointment start time. For example, the default schedule
     * contains 08:00 through 15:30.</p>
     *
     * @param openingTime the first possible appointment start
     * @param closingTime the time at which the clinic closes
     * @param slotLengthMinutes the number of minutes in each slot
     */
    public ClinicSchedule(LocalTime openingTime, LocalTime closingTime, int slotLengthMinutes) {
        if (openingTime == null || closingTime == null) {
            throw new IllegalArgumentException("Opening and closing times are required.");
        }
        if (slotLengthMinutes <= 0) {
            throw new IllegalArgumentException("Slot length must be greater than zero.");
        }
        if (!closingTime.isAfter(openingTime)) {
            throw new IllegalArgumentException("Closing time must be after opening time.");
        }

        long clinicMinutes =
                Duration.between(openingTime, closingTime).toMinutes();
        if (clinicMinutes % slotLengthMinutes != 0) {
            throw new IllegalArgumentException(
                    "Clinic duration must divide evenly into the slot length.");
        }

        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.slotLengthMinutes = slotLengthMinutes;
        this.appointmentTimes =
                Collections.unmodifiableList(generateAppointmentTimes());
    }

    /**
     * Builds the fixed list of appointment start times once in the
     * constructor. Reusing this list keeps later booking checks simple.
     *
     * @return all valid appointment start times
     */
    private List<LocalTime> generateAppointmentTimes() {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime current = openingTime;
        while (current.isBefore(closingTime)) {
            slots.add(current);
            current = current.plusMinutes(slotLengthMinutes);
        }
        return slots;
    }

    /**
     * Returns a read-only list of all valid appointment start times.
     *
     * @return the fixed clinic slots
     */
    public List<LocalTime> getAppointmentTimes() {
        return appointmentTimes;
    }

    /**
     * Checks whether a time is one of the clinic's valid start times.
     *
     * @param time the proposed appointment time
     * @return true when the time belongs to the fixed schedule
     */
    public boolean isValidAppointmentTime(LocalTime time) {
        return time != null && appointmentTimes.contains(time);
    }

    /**
     * Returns the opening time.
     *
     * @return the first appointment start
     */
    public LocalTime getOpeningTime() {
        return openingTime;
    }

    /**
     * Returns the closing time.
     *
     * @return the end of the clinic day
     */
    public LocalTime getClosingTime() {
        return closingTime;
    }

    /**
     * Returns the configured slot length.
     *
     * @return the slot length in minutes
     */
    public int getSlotLengthMinutes() {
        return slotLengthMinutes;
    }

    /**
     * Produces a concise schedule description.
     *
     * @return hours, interval and number of available start times
     */
    @Override
    public String toString() {
        return openingTime.format(TIME_FORMAT)
                + "-" + closingTime.format(TIME_FORMAT)
                + " in " + slotLengthMinutes + "-minute slots ("
                + appointmentTimes.size() + " bookable start times)";
    }
}

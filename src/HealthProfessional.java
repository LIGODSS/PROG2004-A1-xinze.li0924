import java.time.DayOfWeek;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents the information and behaviour shared by every health
 * professional in the clinic.
 *
 * <p>This class is abstract because every professional must have a concrete
 * role. Making the base class abstract prevents code from creating a vague
 * "generic health professional" with no specialty or service.</p>
 */
public abstract class HealthProfessional {

    /**
     * A protected line separator lets the information formatting methods use
     * the correct newline for the operating system without repeating calls to
     * {@link System#lineSeparator()}.
     */
    protected static final String NEW_LINE = System.lineSeparator();

    private final String id;
    private final String name;
    private final Set<DayOfWeek> availableDays;

    /**
     * Creates a professional with a sensible default working pattern of
     * Monday to Friday.
     *
     * <p>This overloaded constructor demonstrates how a useful default can
     * reduce repeated setup while still allowing a caller to supply an
     * explicit schedule.</p>
     *
     * @param id a numeric identifier
     * @param name the professional's full name
     */
    protected HealthProfessional(String id, String name) {
        this(id, name, defaultWorkingDays());
    }

    /**
     * Creates a professional with an explicit set of working days.
     *
     * <p>The set is copied into a new EnumSet. This is a defensive copy: a
     * caller cannot change the professional's schedule later by modifying the
     * original collection.</p>
     *
     * @param id a numeric identifier
     * @param name the professional's full name
     * @param availableDays the days on which the professional works
     */
    protected HealthProfessional(String id, String name, Set<DayOfWeek> availableDays) {
        this.id = Validator.requireNumericId(id, "Health professional ID");
        this.name = Validator.requireText(name, "Health professional name");

        if (availableDays == null || availableDays.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one available working day must be supplied.");
        }

        // EnumSet keeps the days sorted and prevents duplicate day values.
        this.availableDays = Collections.unmodifiableSet(EnumSet.copyOf(availableDays));
    }

    /**
     * Supplies the normal weekday working pattern used by the convenient
     * constructor overloads.
     *
     * @return Monday through Friday, inclusive
     */
    protected static Set<DayOfWeek> defaultWorkingDays() {
        return EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
    }

    /**
     * Returns the professional's numeric ID.
     *
     * @return the validated, immutable ID
     */
    public final String getId() {
        return id;
    }

    /**
     * Returns the professional's full name.
     *
     * @return the validated, immutable name
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns a read-only view of the fixed working days.
     *
     * <p>Returning a read-only set preserves encapsulation: external code may
     * display the schedule, but it cannot alter the object's internal state.</p>
     *
     * @return the available working days
     */
    public final Set<DayOfWeek> getAvailableDays() {
        return availableDays;
    }

    /**
     * Checks whether this professional normally works on a particular day.
     *
     * @param day the day to check
     * @return true when the day is part of the professional's fixed schedule
     */
    public final boolean worksOn(DayOfWeek day) {
        return day != null && availableDays.contains(day);
    }

    /**
     * Returns the role name shown to users of the system.
     *
     * @return the concrete professional role
     */
    public abstract String getRoleName();

    /**
     * Returns role-specific information for the printable representation.
     *
     * <p>Each concrete subclass supplies its own block of text. Calling
     * {@link #toString()} through a HealthProfessional reference therefore
     * invokes the most specific implementation at runtime.</p>
     *
     * @return a formatted description containing no trailing newline
     */
    protected abstract String getRoleSpecificDetails();

    /**
     * Describes an example service that this type of professional can provide.
     *
     * @return a short description of the role's unique behaviour
     */
    public abstract String getSampleService();

    /**
     * Produces a readable multi-line profile containing common and specific
     * details. Subclasses do not need to repeat the shared fields.
     *
     * @return the complete professional profile
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append('[').append(getRoleName()).append("] ").append(name).append(NEW_LINE);
        result.append("  ID             : ").append(id).append(NEW_LINE);
        result.append("  Name           : ").append(name).append(NEW_LINE);
        result.append("  Working days   : ").append(formatWorkingDays()).append(NEW_LINE);
        result.append(getRoleSpecificDetails()).append(NEW_LINE);
        result.append("  Example service: ").append(getSampleService());
        return result.toString();
    }

    /**
     * Formats the working days in enum order, such as
     * "Monday, Wednesday, Friday".
     *
     * @return a readable, comma-separated day list
     */
    private String formatWorkingDays() {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (DayOfWeek day : availableDays) {
            if (!first) {
                result.append(", ");
            }
            result.append(formatDayName(day));
            first = false;
        }
        return result.toString();
    }

    /**
     * Converts a day enum value into a readable name.
     *
     * <p>An explicit switch avoids relying on enum-name string manipulation.
     * It also makes the intended public spelling clear to a reader.</p>
     *
     * @param day the day to format
     * @return the full day name
     */
    private String formatDayName(DayOfWeek day) {
        switch (day) {
            case MONDAY:
                return "Monday";
            case TUESDAY:
                return "Tuesday";
            case WEDNESDAY:
                return "Wednesday";
            case THURSDAY:
                return "Thursday";
            case FRIDAY:
                return "Friday";
            case SATURDAY:
                return "Saturday";
            case SUNDAY:
                return "Sunday";
            default:
                return day.toString();
        }
    }

    /**
     * Two health professionals are considered equal when their IDs match.
     *
     * <p>The brief specifically asks what equality should mean in this
     * domain. The numeric ID is the clinic-wide unique identity, so it is a
     * stronger and more appropriate key than object memory location.</p>
     *
     * @param other the object to compare
     * @return true when both references represent the same professional ID
     */
    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HealthProfessional)) {
            return false;
        }
        HealthProfessional professional = (HealthProfessional) other;
        return id.equals(professional.id);
    }

    /**
     * Keeps the hash code consistent with ID-based equality.
     *
     * @return a hash based on the unique professional ID
     */
    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}

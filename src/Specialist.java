import java.time.DayOfWeek;
import java.util.Set;

/**
 * Represents a specialist doctor with a particular area of clinical expertise.
 */
public final class Specialist extends Doctor {

    private final String specialty;
    private final int yearsOfExperience;

    /**
     * Creates a specialist using the default weekday schedule.
     *
     * @param id the numeric professional ID
     * @param name the doctor's full name
     * @param medicalRegistration the doctor's registration code
     * @param specialty the specialist field
     * @param yearsOfExperience completed years of experience
     */
    public Specialist(
            String id,
            String name,
            String medicalRegistration,
            String specialty,
            int yearsOfExperience) {
        super(id, name, medicalRegistration);
        this.specialty = Validator.requireText(specialty, "Specialty");
        this.yearsOfExperience =
                Validator.requireNonNegative(yearsOfExperience, "Years of experience");
    }

    /**
     * Creates a specialist with an explicit working schedule.
     *
     * @param id the numeric professional ID
     * @param name the doctor's full name
     * @param medicalRegistration the doctor's registration code
     * @param specialty the specialist field
     * @param yearsOfExperience completed years of experience
     * @param availableDays the doctor's working days
     */
    public Specialist(
            String id,
            String name,
            String medicalRegistration,
            String specialty,
            int yearsOfExperience,
            Set<DayOfWeek> availableDays) {
        super(id, name, medicalRegistration, availableDays);
        this.specialty = Validator.requireText(specialty, "Specialty");
        this.yearsOfExperience =
                Validator.requireNonNegative(yearsOfExperience, "Years of experience");
    }

    /**
     * Returns the concrete specialist role name.
     *
     * @return "Specialist"
     */
    @Override
    public String getRoleName() {
        return "Specialist";
    }

    /**
     * Adds the specialist fields to the doctor-specific profile.
     *
     * @return formatted specialist details
     */
    @Override
    protected String getRoleSpecificDetails() {
        return super.getRoleSpecificDetails() + NEW_LINE
                + "  Specialty      : " + specialty + NEW_LINE
                + "  Experience     : " + yearsOfExperience + " years";
    }

    /**
     * Describes a specialist service.
     *
     * @return the service description
     */
    @Override
    public String getSampleService() {
        return "Perform focused " + specialty + " assessments and arrange specialist treatment.";
    }
}

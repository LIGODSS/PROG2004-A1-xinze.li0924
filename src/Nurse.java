import java.time.DayOfWeek;
import java.util.Set;

/**
 * Represents a nurse working in the clinic.
 *
 * <p>Nurses share the common HealthProfessional data but have a different
 * qualification and service behaviour from doctors.</p>
 */
public final class Nurse extends HealthProfessional {

    private final String registrationLevel;
    private final String clinicArea;

    /**
     * Creates a nurse using the default weekday schedule.
     *
     * @param id the numeric professional ID
     * @param name the nurse's full name
     * @param registrationLevel the nursing registration level
     * @param clinicArea the main clinic area
     */
    public Nurse(String id, String name, String registrationLevel, String clinicArea) {
        super(id, name);
        this.registrationLevel =
                Validator.requireText(registrationLevel, "Nursing registration level");
        this.clinicArea = Validator.requireText(clinicArea, "Clinic area");
    }

    /**
     * Creates a nurse with an explicit working schedule.
     *
     * @param id the numeric professional ID
     * @param name the nurse's full name
     * @param registrationLevel the nursing registration level
     * @param clinicArea the main clinic area
     * @param availableDays the nurse's working days
     */
    public Nurse(
            String id,
            String name,
            String registrationLevel,
            String clinicArea,
            Set<DayOfWeek> availableDays) {
        super(id, name, availableDays);
        this.registrationLevel =
                Validator.requireText(registrationLevel, "Nursing registration level");
        this.clinicArea = Validator.requireText(clinicArea, "Clinic area");
    }

    /**
     * Returns the concrete role name.
     *
     * @return "Nurse"
     */
    @Override
    public String getRoleName() {
        return "Nurse";
    }

    /**
     * Formats nurse-only information.
     *
     * @return the nursing registration and clinic area
     */
    @Override
    protected String getRoleSpecificDetails() {
        return "  Registration   : " + registrationLevel + NEW_LINE
                + "  Clinic area    : " + clinicArea;
    }

    /**
     * Describes a nurse service.
     *
     * @return the service description
     */
    @Override
    public String getSampleService() {
        return "Collect blood samples, monitor observations and support clinical care.";
    }

    /**
     * Models the blood sampling action specifically mentioned in the brief.
     *
     * @param patient the patient whose sample is being collected
     * @return a readable action confirmation
     */
    public String takeBloodSample(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient is required.");
        }
        return getName() + " collected a blood sample from " + patient.getName() + ".";
    }
}

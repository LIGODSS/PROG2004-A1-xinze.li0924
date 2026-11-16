import java.time.DayOfWeek;
import java.util.Set;

/**
 * Represents behaviour and information shared by medical doctors.
 *
 * <p>This intermediate abstract class demonstrates multi-level inheritance:
 * a general practitioner and a specialist both extend Doctor, while Doctor
 * extends HealthProfessional. It prevents prescription information from
 * being duplicated in the two doctor subclasses.</p>
 */
public abstract class Doctor extends HealthProfessional {

    private final String medicalRegistration;

    /**
     * Creates a doctor using the default weekday schedule.
     *
     * @param id the numeric professional ID
     * @param name the doctor's full name
     * @param medicalRegistration the doctor's medical registration code
     */
    protected Doctor(String id, String name, String medicalRegistration) {
        super(id, name);
        this.medicalRegistration =
                Validator.requireText(medicalRegistration, "Medical registration");
    }

    /**
     * Creates a doctor with an explicit working schedule.
     *
     * @param id the numeric professional ID
     * @param name the doctor's full name
     * @param medicalRegistration the doctor's medical registration code
     * @param availableDays the doctor's working days
     */
    protected Doctor(
            String id,
            String name,
            String medicalRegistration,
            Set<DayOfWeek> availableDays) {
        super(id, name, availableDays);
        this.medicalRegistration =
                Validator.requireText(medicalRegistration, "Medical registration");
    }

    /**
     * Returns the doctor's registration code.
     *
     * @return the registration code
     */
    public final String getMedicalRegistration() {
        return medicalRegistration;
    }

    /**
     * Models a doctor-specific action requested by the assignment.
     *
     * @param medication the medication being prescribed
     * @return a readable prescription confirmation
     */
    public final String prescribeMedication(String medication) {
        String validMedication = Validator.requireText(medication, "Medication");
        return getName() + " prescribed " + validMedication
                + " (medical registration " + medicalRegistration + ").";
    }

    /**
     * Provides the shared doctor detail consumed by the base profile formatter.
     *
     * @return the formatted medical registration line
     */
    @Override
    protected String getRoleSpecificDetails() {
        return "  Medical reg.   : " + medicalRegistration;
    }
}

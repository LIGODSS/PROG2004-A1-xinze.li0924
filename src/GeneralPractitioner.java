import java.time.DayOfWeek;
import java.util.Set;

/**
 * Represents a general practitioner, the first doctor role required by the
 * assessment.
 */
public final class GeneralPractitioner extends Doctor {

    private final String consultationRoom;

    /**
     * Creates a general practitioner using the default weekday schedule.
     *
     * @param id the numeric professional ID
     * @param name the doctor's full name
     * @param medicalRegistration the doctor's registration code
     * @param consultationRoom the room used for consultations
     */
    public GeneralPractitioner(
            String id,
            String name,
            String medicalRegistration,
            String consultationRoom) {
        super(id, name, medicalRegistration);
        this.consultationRoom =
                Validator.requireText(consultationRoom, "Consultation room");
    }

    /**
     * Creates a general practitioner with an explicit working schedule.
     *
     * @param id the numeric professional ID
     * @param name the doctor's full name
     * @param medicalRegistration the doctor's registration code
     * @param consultationRoom the room used for consultations
     * @param availableDays the doctor's working days
     */
    public GeneralPractitioner(
            String id,
            String name,
            String medicalRegistration,
            String consultationRoom,
            Set<DayOfWeek> availableDays) {
        super(id, name, medicalRegistration, availableDays);
        this.consultationRoom =
                Validator.requireText(consultationRoom, "Consultation room");
    }

    /**
     * Returns the general practitioner role name.
     *
     * @return "General Practitioner"
     */
    @Override
    public String getRoleName() {
        return "General Practitioner";
    }

    /**
     * Adds the room to the doctor-specific details.
     *
     * @return the shared doctor details followed by the room
     */
    @Override
    protected String getRoleSpecificDetails() {
        return super.getRoleSpecificDetails() + NEW_LINE
                + "  Consultation rm: " + consultationRoom;
    }

    /**
     * Describes a general practice service.
     *
     * @return the service description
     */
    @Override
    public String getSampleService() {
        return "Assess common health concerns, provide treatment advice and prescribe when appropriate.";
    }
}

import java.time.DayOfWeek;
import java.util.Set;

/**
 * Represents a dietitian who provides nutrition-focused health services.
 */
public final class Dietitian extends HealthProfessional {

    private final String nutritionFocus;

    /**
     * Creates a dietitian using the default weekday schedule.
     *
     * @param id the numeric professional ID
     * @param name the dietitian's full name
     * @param nutritionFocus the dietitian's main clinical focus
     */
    public Dietitian(String id, String name, String nutritionFocus) {
        super(id, name);
        this.nutritionFocus =
                Validator.requireText(nutritionFocus, "Nutrition focus");
    }

    /**
     * Creates a dietitian with an explicit working schedule.
     *
     * @param id the numeric professional ID
     * @param name the dietitian's full name
     * @param nutritionFocus the dietitian's main clinical focus
     * @param availableDays the dietitian's working days
     */
    public Dietitian(
            String id,
            String name,
            String nutritionFocus,
            Set<DayOfWeek> availableDays) {
        super(id, name, availableDays);
        this.nutritionFocus =
                Validator.requireText(nutritionFocus, "Nutrition focus");
    }

    /**
     * Returns the concrete role name.
     *
     * @return "Dietitian"
     */
    @Override
    public String getRoleName() {
        return "Dietitian";
    }

    /**
     * Formats dietitian-only information.
     *
     * @return the nutrition focus
     */
    @Override
    protected String getRoleSpecificDetails() {
        return "  Nutrition focus: " + nutritionFocus;
    }

    /**
     * Describes a dietitian service.
     *
     * @return the service description
     */
    @Override
    public String getSampleService() {
        return "Create practical nutrition plans for " + nutritionFocus + ".";
    }
}

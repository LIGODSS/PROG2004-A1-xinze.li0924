import java.util.Objects;

/**
 * Represents the patient information required for an appointment.
 *
 * <p>Patient is a separate class because a patient is a meaningful domain
 * object that can exist independently of an appointment. It also prevents
 * patient-name and phone validation from being buried inside Appointment.</p>
 */
public final class Patient {

    private final String name;
    private final String mobileNumber;

    /**
     * Creates a validated patient.
     *
     * @param name the patient's full name
     * @param mobileNumber an Australian mobile number
     */
    public Patient(String name, String mobileNumber) {
        this.name = Validator.requireText(name, "Patient name");
        this.mobileNumber = Validator.normaliseAustralianMobile(mobileNumber);
    }

    /**
     * Returns the patient's name.
     *
     * @return the immutable name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the canonical ten-digit mobile number.
     *
     * @return the immutable phone number
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Formats the phone number into a more readable group.
     *
     * @return a display-friendly number such as 0412 345 678
     */
    public String getFormattedMobileNumber() {
        return mobileNumber.substring(0, 4)
                + " " + mobileNumber.substring(4, 7)
                + " " + mobileNumber.substring(7);
    }

    /**
     * Compares patients by mobile number, which is unique for this simulation.
     *
     * @param other the object to compare
     * @return true when both patients have the same normalised mobile number
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Patient)) {
            return false;
        }
        Patient patient = (Patient) other;
        return mobileNumber.equals(patient.mobileNumber);
    }

    /**
     * Keeps hashCode consistent with phone-based equality.
     *
     * @return a hash based on the normalised mobile number
     */
    @Override
    public int hashCode() {
        return Objects.hash(mobileNumber);
    }

    /**
     * Returns a concise patient summary.
     *
     * @return name and formatted phone number
     */
    @Override
    public String toString() {
        return name + " (" + getFormattedMobileNumber() + ")";
    }
}

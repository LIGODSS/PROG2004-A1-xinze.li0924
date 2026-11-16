/**
 * Represents the outcome of an appointment operation.
 *
 * <p>Returning a result object gives the caller both a success flag and a
 * clear message. It also avoids using exceptions for expected user mistakes,
 * such as trying to double-book a professional.</p>
 */
public final class OperationResult {

    private final boolean successful;
    private final String message;
    private final Appointment appointment;

    /**
     * Creates a result. The factory methods below provide clearer call sites.
     *
     * @param successful whether the operation succeeded
     * @param message feedback for the user
     * @param appointment the affected appointment, or null when not available
     */
    private OperationResult(boolean successful, String message, Appointment appointment) {
        this.successful = successful;
        this.message = Validator.requireText(message, "Operation message");
        this.appointment = appointment;
    }

    /**
     * Creates a successful result without an appointment payload.
     *
     * @param message a confirmation message
     * @return a successful OperationResult
     */
    public static OperationResult success(String message) {
        return new OperationResult(true, message, null);
    }

    /**
     * Creates a successful result that also returns the affected appointment.
     *
     * @param message a confirmation message
     * @param appointment the booked appointment
     * @return a successful OperationResult
     */
    public static OperationResult success(String message, Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("A successful booking must include an appointment.");
        }
        return new OperationResult(true, message, appointment);
    }

    /**
     * Creates a failed result with a warning for the user.
     *
     * @param message the reason the operation could not be completed
     * @return a failed OperationResult
     */
    public static OperationResult failure(String message) {
        return new OperationResult(false, message, null);
    }

    /**
     * Reports whether the operation succeeded.
     *
     * @return true for success
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * Returns the user-facing feedback.
     *
     * @return the operation message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the appointment payload when one was supplied.
     *
     * @return the appointment, or null when the operation has no payload
     */
    public Appointment getAppointment() {
        return appointment;
    }
}

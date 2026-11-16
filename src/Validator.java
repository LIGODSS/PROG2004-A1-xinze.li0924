/**
 * Provides reusable validation methods for the appointment system.
 *
 * <p>Keeping validation in one place means that every object applies the
 * same rules. This reduces duplicated code and makes invalid data much harder
 * to introduce.</p>
 */
public final class Validator {

    /**
     * The constructor is private because this is a utility class. Creating an
     * instance would not provide any useful state or behaviour.
     */
    private Validator() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Checks that a required text value is not empty.
     *
     * @param value the value supplied by a caller
     * @param fieldName a readable field name used in the exception message
     * @return the trimmed, valid value
     * @throws IllegalArgumentException when the value is null or blank
     */
    public static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return value.trim();
    }

    /**
     * Checks that an identifier contains ASCII digits only.
     *
     * <p>The assignment requires numeric health professional IDs. Limiting
     * the check to 0-9 is clearer than accepting every Unicode digit.</p>
     *
     * @param value the ID supplied by a caller
     * @param fieldName a readable field name used in the exception message
     * @return the trimmed numeric ID
     * @throws IllegalArgumentException when the ID is blank or not numeric
     */
    public static String requireNumericId(String value, String fieldName) {
        String cleanValue = requireText(value, fieldName);
        for (int index = 0; index < cleanValue.length(); index++) {
            char character = cleanValue.charAt(index);
            if (character < '0' || character > '9') {
                throw new IllegalArgumentException(fieldName + " must contain numbers only.");
            }
        }
        return cleanValue;
    }

    /**
     * Validates and normalises an Australian mobile phone number.
     *
     * <p>Spaces and hyphens are accepted as presentation characters, then
     * removed from the stored value. The canonical result is ten digits and
     * starts with 04, for example 0412345678.</p>
     *
     * @param value the phone number supplied by a caller
     * @return the ten-digit mobile number
     * @throws IllegalArgumentException when the number is not a valid mobile
     */
    public static String normaliseAustralianMobile(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Mobile phone number is required.");
        }

        StringBuilder digits = new StringBuilder();
        for (int index = 0; index < value.length(); index++) {
            char character = value.charAt(index);
            if (character >= '0' && character <= '9') {
                digits.append(character);
            } else if (character != ' ' && character != '-') {
                throw new IllegalArgumentException(
                        "Mobile phone number can contain digits, spaces and hyphens only.");
            }
        }

        String cleanNumber = digits.toString();
        if (cleanNumber.length() != 10 || !cleanNumber.startsWith("04")) {
            throw new IllegalArgumentException(
                    "Mobile phone number must contain 10 digits and start with 04.");
        }
        return cleanNumber;
    }

    /**
     * Ensures that a numeric value is not negative.
     *
     * @param value the value to check
     * @param fieldName a readable field name used in the exception message
     * @return the validated value
     * @throws IllegalArgumentException when the value is negative
     */
    public static int requireNonNegative(int value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be negative.");
        }
        return value;
    }
}

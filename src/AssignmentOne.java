import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Main entry point for the PROG2004 Assessment 1 appointment system.
 *
 * <p>The program is intentionally a scripted console simulation. It creates
 * health professional objects, treats them through the common base type,
 * books appointments, filters and sorts them, cancels bookings and
 * demonstrates the warning paths required by the brief.</p>
 */
public class AssignmentOne {

    private static final String MAJOR_DIVIDER =
            "================================================================================";
    private static final String MINOR_DIVIDER =
            "--------------------------------------------------------------------------------";
    private static final DateTimeFormatter SHORT_DATE =
            DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.ENGLISH);

    /**
     * Runs the complete scripted simulation.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        ClinicSchedule schedule = new ClinicSchedule();
        AppointmentManager manager = new AppointmentManager(today, schedule);

        printMainHeading(today, schedule);

        List<HealthProfessional> professionals = createHealthProfessionals(today);
        demonstrateHealthProfessionals(professionals);
        demonstrateAppointmentManagement(manager, professionals);

        printSection("DEMONSTRATION COMPLETE");
        System.out.println("The simulation exercised normal and warning paths.");
        System.out.println("All booking data is held in memory for this assignment.");
        System.out.println(MAJOR_DIVIDER);
    }

    /**
     * Prints the title and the configured booking environment.
     *
     * @param today the current booking day
     * @param schedule the clinic's fixed booking schedule
     */
    private static void printMainHeading(LocalDate today, ClinicSchedule schedule) {
        System.out.println(MAJOR_DIVIDER);
        System.out.println("          RIVERBANK HEALTH CLINIC - APPOINTMENT SYSTEM");
        System.out.println("                    PROG2004 Assessment 1");
        System.out.println(MAJOR_DIVIDER);
        System.out.println("Booking day : " + today.format(SHORT_DATE));
        System.out.println("Clinic hours: " + schedule);
        System.out.println();
    }

    /**
     * Creates three general practitioners, two specialists, two nurses and one
     * dietitian. Most work today, while one nurse deliberately has another
     * working day so the appointment manager can demonstrate an availability
     * warning.
     *
     * @param bookingDate the current day
     * @return a polymorphic list of health professionals
     */
    private static List<HealthProfessional> createHealthProfessionals(LocalDate bookingDate) {
        DayOfWeek today = bookingDate.getDayOfWeek();
        DayOfWeek nextDay = today.plus(1);
        Set<DayOfWeek> worksToday = workingDays(today);
        Set<DayOfWeek> worksNextDay = workingDays(nextDay);

        List<HealthProfessional> professionals = new ArrayList<>();

        // Three GPs satisfy the brief's suggested realistic professional mix.
        professionals.add(new GeneralPractitioner(
                "100101", "Maya Chen", "MED-2187", "Room 101", worksToday));
        // Daniel uses the shorter constructor overload, so his weekdays come
        // from HealthProfessional's sensible default working pattern.
        professionals.add(new GeneralPractitioner(
                "100102", "Daniel Brooks", "MED-3044", "Room 102"));
        professionals.add(new GeneralPractitioner(
                "100103", "Elena Rossi", "MED-4129", "Room 103", worksToday));

        // Specialists extend Doctor, so they reuse the registration and
        // prescription behaviour while adding their own clinical details.
        professionals.add(new Specialist(
                "200201", "Olivia Grant", "MED-5091",
                "Cardiology", 12, worksToday));
        professionals.add(new Specialist(
                "200202", "Kareem Haddad", "MED-6632",
                "Radiology", 8, worksToday));

        professionals.add(new Nurse(
                "300301", "Noah Wilson", "Registered Nurse",
                "Treatment Room", worksToday));

        // Priya intentionally does not work today. A booking attempt later
        // proves that an individual professional's schedule is enforced.
        professionals.add(new Nurse(
                "300302", "Priya Nair", "Enrolled Nurse",
                "Observation Bay", worksNextDay));

        professionals.add(new Dietitian(
                "400401", "Ava Thompson",
                "diabetes and heart health", worksToday));

        return professionals;
    }

    /**
     * Demonstrates Part 3 by processing every concrete type through the
     * common HealthProfessional type.
     *
     * @param professionals the objects to display
     */
    private static void demonstrateHealthProfessionals(
            List<HealthProfessional> professionals) {
        printSection("PART 3 - HEALTH PROFESSIONALS");
        System.out.println("The objects below are stored as HealthProfessional references.");
        System.out.println("Polymorphism selects each concrete role's details and service.");
        System.out.println();

        int profileNumber = 1;
        for (HealthProfessional professional : professionals) {
            System.out.println("Profile " + profileNumber + " of " + professionals.size());
            System.out.println(professional);
            System.out.println(MINOR_DIVIDER);
            profileNumber++;
        }
    }

    /**
     * Exercises Parts 4-6: individual appointment modelling, manager
     * operations, filtering, sorting, cancellation and error cases.
     *
     * @param manager the appointment manager under demonstration
     * @param professionals the available health professionals
     */
    private static void demonstrateAppointmentManagement(
            AppointmentManager manager,
            List<HealthProfessional> professionals) {
        LocalDate today = manager.getBookingDate();
        HealthProfessional maya = findProfessional(professionals, "100101");
        HealthProfessional olivia = findProfessional(professionals, "200201");
        HealthProfessional kareem = findProfessional(professionals, "200202");
        HealthProfessional noah = findProfessional(professionals, "300301");
        HealthProfessional priya = findProfessional(professionals, "300302");
        HealthProfessional ava = findProfessional(professionals, "400401");

        Patient alice = new Patient("Alice Nguyen", "0412 345 678");
        Patient ben = new Patient("Ben Carter", "0423 456 789");
        Patient chloe = new Patient("Chloe Martin", "0434 567 890");

        printSection("PART 4 - APPOINTMENT OBJECT");
        OperationResult firstBooking = manager.addAppointment(
                alice, maya, today, LocalTime.of(9, 0));
        printResult(firstBooking);
        if (firstBooking.isSuccessful()) {
            System.out.println();
            System.out.println("Detailed object returned by the booking operation:");
            System.out.println(firstBooking.getAppointment());
        }

        printSection("PART 5 - MANAGING APPOINTMENTS");
        System.out.println("Valid bookable start times:");
        System.out.println(manager.getSchedule().getAppointmentTimes());
        System.out.println();

        System.out.println("Normal booking operations:");
        printResult(manager.addAppointment(
                ben, maya, today, LocalTime.of(10, 30)));
        printResult(manager.addAppointment(
                chloe, olivia, today, LocalTime.of(9, 0)));
        printResult(manager.addAppointment(
                alice, noah, today, LocalTime.of(11, 0)));
        printResult(manager.addAppointment(
                ben, kareem, today, LocalTime.of(15, 0)));
        printResult(manager.addAppointment(
                alice, ava, today, LocalTime.of(13, 30)));

        System.out.println();
        System.out.println("Warning operations:");
        printResult(manager.addAppointment(
                ben, maya, today, LocalTime.of(9, 0)));
        printResult(manager.addAppointment(
                ben, maya, today, LocalTime.of(8, 15)));
        printResult(manager.addAppointment(
                ben, maya, today.plusDays(1), LocalTime.of(9, 30)));
        printResult(manager.addAppointment(
                chloe, priya, today, LocalTime.of(12, 0)));

        printSection("FILTERED APPOINTMENTS");
        System.out.println("Appointments for GP Maya Chen (ID 100101):");
        printAppointmentList(manager.findAppointmentsByProfessional("100101"));
        System.out.println();

        System.out.println("Appointments for Alice Nguyen (0412 345 678):");
        printAppointmentList(manager.findAppointmentsByPatientPhone("0412 345 678"));

        printSection("ALL APPOINTMENTS - SORTED BY TIME");
        System.out.println("Appointment implements Comparable; the manager returns a sorted snapshot.");
        printAppointmentList(manager.getAllAppointments());

        printSection("CANCELLATION");
        printResult(manager.cancelByProfessional("200201", LocalTime.of(9, 0)));
        printResult(manager.cancelByProfessional("200201", LocalTime.of(9, 0)));
        printResult(manager.cancelByPatientPhone("0412 345 678"));

        printSection("UPDATED APPOINTMENTS AFTER CANCELLATION");
        System.out.println("Alice's appointments should now be empty:");
        printAppointmentList(manager.findAppointmentsByPatientPhone("0412 345 678"));
        System.out.println();

        System.out.println("Remaining appointments, still sorted by time:");
        printAppointmentList(manager.getAllAppointments());

        printSection("ROLE-SPECIFIC BEHAVIOUR EXAMPLES");
        System.out.println(((Doctor) maya).prescribeMedication("amoxicillin"));
        System.out.println(((Nurse) noah).takeBloodSample(ben));
        System.out.println("Dietitian service: " + ava.getSampleService());
    }

    /**
     * Finds a professional in the demonstration list by ID.
     *
     * @param professionals the search collection
     * @param id the required ID
     * @return the matching professional
     * @throws IllegalStateException when the demonstration data is incomplete
     */
    private static HealthProfessional findProfessional(
            List<HealthProfessional> professionals,
            String id) {
        for (HealthProfessional professional : professionals) {
            if (professional.getId().equals(id)) {
                return professional;
            }
        }
        throw new IllegalStateException("Missing demonstration professional: " + id);
    }

    /**
     * Creates a one-day working set for convenient demonstration setup.
     *
     * @param days the days to include
     * @return an EnumSet containing those days
     */
    private static Set<DayOfWeek> workingDays(DayOfWeek... days) {
        EnumSet<DayOfWeek> result = EnumSet.noneOf(DayOfWeek.class);
        for (DayOfWeek day : days) {
            result.add(day);
        }
        return result;
    }

    /**
     * Prints a consistently formatted section heading.
     *
     * @param title the section title
     */
    private static void printSection(String title) {
        System.out.println();
        System.out.println(MAJOR_DIVIDER);
        System.out.println(title);
        System.out.println(MAJOR_DIVIDER);
    }

    /**
     * Prints an operation result with a clear success or warning marker.
     *
     * @param result the result returned by the manager
     */
    private static void printResult(OperationResult result) {
        String marker = result.isSuccessful() ? "[SUCCESS]" : "[WARNING]";
        System.out.println(marker + " " + result.getMessage());
    }

    /**
     * Prints zero or more appointment summaries in chronological order.
     *
     * @param appointments the appointments to display
     */
    private static void printAppointmentList(List<Appointment> appointments) {
        if (appointments.isEmpty()) {
            System.out.println("  (no appointments)");
            return;
        }
        for (int index = 0; index < appointments.size(); index++) {
            System.out.println(
                    "  " + (index + 1) + ". " + appointments.get(index).toSummaryString());
        }
    }
}

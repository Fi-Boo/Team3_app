/* OUA/RMIT Software Engineering Project Management 2024 SP1
 * Assignment 2
 * Team 3
 * 
 * Angelo Lapuz         (s3914378)  Team Leader
 * Melissa Rose         (s3427269)  2IC
 * Nicholas Drinkwater  (s3508178)  Senior Stenographer/App Tester 
 * Phi Van Bui          (s2008156)  Senior Procrastinator/Janitor
 * 
 */

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class AirRMIT {

    private String name;
    private ArrayList<User> users;
    private ArrayList<Ticket> tickets;
    private User loggedUser;

    public AirRMIT(String string) {
        this.name = string;
        this.users = new ArrayList<User>();
        this.tickets = new ArrayList<Ticket>();
        this.loggedUser = null;
    }

    Scanner sc = new Scanner(System.in);

    // for testing purposes I set at 5. Change to 20 for final submission
    String passwordLength = "20";

    public void run() {

        loadHardCodedUsers(); // hardcoded staff data. May change to loading from a csv for easy data
                              // manipulation during testing

        loadData(); // used to load dummy data for testing.
        runArchiveCheck(); // checks loaded data and archives tickets that have been closed for 24 or more
                           // hours.

        int userInput;

        do {

            showHeaderOne("Service Portal");
            System.out.println("\n[1] Register New Account");
            System.out.println("[2] Existing User Login");
            System.out.println("[3] Reset Password");
            System.out.println("[4] Exit\n");
            System.out.print("Selection: ");

            userInput = validateUserSelection(4);

            switch (userInput) {
                case (1):

                    runRegistrationFeature();

                    break;
                case (2):

                    runLoginFeature();

                    break;
                case (3):

                    runPasswordResetFeature();

                    break;
                case (4):

                    System.out.println("Exiting System...");
                    break;

            }

        } while (userInput != 4);
    }

    /*
     * Registration Feature
     *
     * Validate user inputs against regex
     * Emails - must be in the form user@domain.name
     * Full Name - first character for first name and surname must be upper case.
     * Must be more than 2 characters long. Must have a first name and surname.
     * hyphen allowed.
     * Password - 20 alphanumerical characters long. At least 1 number, 1 upper
     * case, 1 lower case.
     * 
     * Check registration email against existing accounts.
     * 
     * Successful registration will add the user to the collection of users and
     * return to the Service Portal menu.
     * 
     */
    private void runRegistrationFeature() {

        showHeaderOne("Registration Menu");
        System.out.println("");

        boolean emailExists = true;

        String email;

        do {

            String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
            String emailErrorMessage = "Invalid email format. Please try again...";
            email = validateRegex("Email: ", emailRegex, emailErrorMessage);

            emailExists = checkEmail(email);

            if (emailExists == true) {

                System.out.println("There is an account already associated with this email. Try again buddy...");

            }

        } while (emailExists == true);

        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{" + passwordLength + ",}$";
        String passwordErrorMessage = "Invalid password format. At least 1 upper case, 1 lower case, 1 number. Min length "
                + passwordLength + " characters. Please try again...";
        String password = validateRegex("Password: ", passwordRegex, passwordErrorMessage);

        // String nameRegexOld = "^[A-Z][a-z]*(?: [A-Z][a-z]*)+$";
        String nameRegex = "^[A-Z][a-zA-Z]{1,}(?: [A-Z][a-z]{1,})*$";
        String nameErrorMessage = "Full name cannot be blank. Must begin with an Upper case letter and have at least 2 letters. Please try again...";
        String fullName = validateRegex("Full Name: ", nameRegex, nameErrorMessage);

        String phoneRegex = "^(0)[1-9](?:[0-9]{8}|(?:\\s[0-9]{3,4}){3})$";
        String numberErrorMessage = "Invalid AUS mobile number. Must be in format 04xxxxxxxx. Please try again...";
        String contact = validateRegex("Mobile contact: ", phoneRegex, numberErrorMessage);

        User newUser = new User(email, fullName, contact, password);

        users.add(newUser);

        System.out.println("\nUser registration successful.");
        System.out.println("Returning to Service Portal\n");
    }

    /*
     * Login Feature
     * 
     * Validate input credentials (email & password) against existing users list.
     * 3 login attempt limit. Upon failing the 3rd attempt, the system will return
     * to the Service Portal menu.
     * 
     * Successful credentials validation will proceed to the staff interface.
     * 
     */
    private void runLoginFeature() {

        showHeaderOne("User Login Menu");
        System.out.println("\nInput Credentials");

        boolean validCreds = false;
        boolean failLogin = false;

        int loginAttempts = 3;

        do {

            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();
            validCreds = validateCredentials(email, password);

            if (validCreds == false) {

                loginAttempts--;

                System.out.printf("\nInvalid login. %d attempts remaining...\n", loginAttempts);

                if (loginAttempts == 0) {

                    System.out.println("\nReturning to Service Portal...");
                    failLogin = true;

                }

            }

        } while (validCreds == false && failLogin == false);

        if (validCreds == true) {

            runStaffInterface();
        }

    }

    /*
     * Password Reset Feature
     * 
     * Checks user input email and contact for an match in the users collection.
     * 3 Attempts ONLY. Failing the 3rd attempt will return to the Service Portal
     * menu.
     * 
     * If match found, user is prompted to input a new password. Password must
     * conform to regex
     * requirements.
     * 
     * Returns user to Service Portal once complete.
     */
    private void runPasswordResetFeature() {

        showHeaderOne("Forgot Password?");
        System.out.println("\nPlease input the following credentials to reset your password");

        boolean validCreds = false, failCheck = false;

        int checkAttempts = 3;

        do {

            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Contact Number: ");
            String number = sc.nextLine();

            for (User user : users) {

                if (user.getEmail().equalsIgnoreCase(email) && user.getNumber().equals(number)) {
                    validCreds = true;

                    System.out.println("\nMatch found! Input new password...");

                    String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{" + passwordLength + ",}$";
                    String passwordErrorMessage = "Invalid password format. At least 1 upper case, 1 lower case, 1 number. Min length "
                            + passwordLength + " characters. Please try again...";
                    String password = validateRegex("Password: ", passwordRegex, passwordErrorMessage);

                    user.setPassword(password);

                    System.out.println("Password reset successful! Returning to Service Portal...");

                }
            }

            if (validCreds == false) {

                checkAttempts--;
                System.out.printf("No match found. %d attempts remaining...\n", checkAttempts);

                if (checkAttempts == 0) {

                    failCheck = true;
                    System.out.println("\nReturning to Service Portal...");
                }

            }

        } while (validCreds == false && failCheck == false);

    }

    /*
     * Staff Interface
     * 
     * Currently the only available feature is the LOG OUT. Logs out user and
     * returns to the Service Portal menu.
     * 
     * 
     * FUTURE WORKS
     * 
     * Staff Type specific menu
     * 
     */
    private void runStaffInterface() {

        boolean logout = false;

        do {

            showHeaderOne("Staff Interface");
            System.out.println(loggedUser.getFullName());
            System.out.println("Clearance: [" + loggedUser.getStaffType().toUpperCase() + "]\n");

            runArchiveCheck();

            showTickets("Open");

            if (loggedUser.getStaffType().substring(0, 1).equalsIgnoreCase("t")) {

                showTickets("Closed");
                showTickets("Archived");
            }

            if (loggedUser.getStaffType().equalsIgnoreCase("s")) {

                logout = runStaffMenu();

            } else {

                logout = runTechMenu();
            }

        } while (logout != true);

    }

    /*
     * Credential validation.
     * 
     * Takes user input strings for email and password and checks for a match in the
     * users collection.
     * 
     * Returns a boolean if a match for both email and password is found for the
     * same user.
     */
    private boolean validateCredentials(String email, String password) {

        for (User user : users) {

            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {

                loggedUser = user;

                return true;

            }
        }
        return false;
    }

    /*
     * Takes user input String for email and checks for a match in the users
     * collection.
     * 
     * Returns a boolean based on a match or not.
     */
    private boolean checkEmail(String email) {

        for (User user : users) {

            if (user.getEmail().equalsIgnoreCase(email)) {

                return true;
            }
        }
        return false;
    }

    /*
     * Validates by Regex function
     * 
     * Takes a user input String and validates it against a predefined regex
     * pattern. Outputs predefined error message if a match is not found.
     * Will continue to ask for user input if it fails to pass the validation.
     * 
     * Returns the user input string if it passes regex validation.
     */
    private String validateRegex(String title, String regex, String errorMsg) {

        Pattern pattern = Pattern.compile(regex);
        String userInput;
        boolean matchFound = false;

        do {
            System.out.print(title);
            userInput = sc.nextLine();
            Matcher matcher = pattern.matcher(userInput);
            matchFound = matcher.find();
            if (matchFound == false) {
                System.out.println(errorMsg);
            }
        } while (matchFound == false);

        return userInput;
    }

    /*
     * User selection validation
     * 
     * Takes a user input value and checks that it is within a predefined int
     * boundary.
     * 
     * Returns the user input value only once a valid input is achieved.
     */
    private int validateUserSelection(int i) {

        int userInput = 0;

        boolean validInput = false;

        do {

            try {

                userInput = Integer.parseInt(sc.nextLine());

                if (userInput < 1 || userInput > i) {

                    System.out.printf("Invalid selection. Please select an option between 1 and %d: ", i);

                }

                if (userInput >= 1 && userInput <= i) {

                    validInput = true;

                }

            } catch (NumberFormatException e) {

                System.out.print("Invalid selection. Use a number! Try again: ");
            }

        } while (validInput == false);

        return userInput;
    }

    /*
     * Utility function to reduce code repetition
     * 
     * generates a basic top and bottom border around a title String based on the
     * paramater.
     */
    private void showHeaderOne(String title) {

        System.out.println("");
        System.out.println("*".repeat(50));
        System.out.println(name + " " + title);
        System.out.println("*".repeat(50));
    }

    private void showHeaderTwo(String title) {

        System.out.println("");
        System.out.println("-".repeat(50));
        System.out.println(title);
        System.out.println("-".repeat(50));

    }

    /*
     * Function to populate the users collection with staff Users
     * 
     * T1 and T2 staff as per client requirement.
     * 
     * Team member staff also added for testing purposes
     */
    private void loadHardCodedUsers() {

        users.add(new User("harrys@airrmit.com", "Harry Styles", "0430303030", "iamharry",
                "t1"));
        users.add(new User("niallh@airrmit.com", "Niall Horan", "0431313131", "iamniall",
                "t1"));
        users.add(new User("liamp@airrmit.com", "Liam Payne", "0432323232", "iamliam",
                "t1"));
        users.add(new User("louist@airrmit.com", "Louis Tomlinson", "0433333333",
                "iamlouis", "t2"));
        users.add(new User("zaynem@airrmit.com", "Zayne Malik", "0434343434", "iamzayne",
                "t2"));
        users.add(new User("angelol@airrmit.com", "Angelo Lapuz", "0433914378",
                "iamangelo"));
        users.add(new User("belrose@airrmit.com", "Belrose Gunzel", "0433427269",
                "iambelrose"));
        users.add(new User("nickd@airrmit.com", "Nick Drinkwater", "0433508178",
                "iamnick"));
        users.add(new User("phib@airrmit.com", "Phi Bui", "0432008156", "iamphi"));

    }

    // SPRINT 2 ADDITIONS

    /*
     * function to load data from hardcoded data.txt
     * 
     * Assumption - data in each datafield is correct.
     * - no data validation implemented (out of scope)
     * 
     */
    private void loadData() {

        try {

            BufferedReader br = new BufferedReader(new FileReader("data.txt"));

            String line = br.readLine();

            while ((line = br.readLine()) != null) {

                String[] row = line.split("\t");

                tickets.add(new Ticket(row[0], row[1], row[2], row[3], row[4], row[5], row[6]));

            }

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("data load successful...");

    }

    /*
     * function to save Ticket data to file data.txt
     * used for testing purposes
     * 
     */
    public void saveData() {

        StringBuilder content = new StringBuilder();

        content.append("Description\tSeverity\tOpen Date/Time\tCreated By\tAssigned To\tStatus\tClosed Date/Time\n");

        for (Ticket ticket : tickets) {
            content.append(ticket.toString());
        }

        FileWriter filewriter = null;

        try {

            filewriter = new FileWriter("data.txt", false);
            filewriter.write(content.toString());

            filewriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * outputs ticket data in a particular format based on certain conditions.
     * 
     * conditions - If Staff logged in, shows only staff member's open ticket
     * - If Tech logged in, shows only tech's open tickets
     * - If Tech logged in, shows all closed/archived tickets
     */
    private void showTickets(String status) {

        showHeaderTwo(status + " Tickets");

        ArrayList<Ticket> tList = getTicketsListByStatus(status);

        int counter = 0;

        if (tList.size() == 0) {

            System.out.println("No " + status + " Tickets...");

        } else {

            for (Ticket ticket : tList) {
                counter++;

                System.out.printf("\nTicket #%d", counter);
                ticket.displayDetails(loggedUser);

                System.out.println("-".repeat(50));

            }
        }
    }

    /*
     * Gets collection of tickets based on input parameter
     * 
     * Conditions - If open parameter, returns all open tickets allocated to logged
     * in user
     * - If closed/archived ticket, returns all closed/archived tickets
     * 
     */
    private ArrayList<Ticket> getTicketsListByStatus(String status) {

        ArrayList<Ticket> list = new ArrayList<Ticket>();

        for (Ticket ticket : tickets) {

            if (status.equalsIgnoreCase("open") && ticket.getStatus().equalsIgnoreCase(status)) {

                if (loggedUser.getStaffType().equalsIgnoreCase("s")
                        && ticket.getCreatedBy().equals(loggedUser.getFullName())) {

                    list.add(ticket);

                } else if (loggedUser.getStaffType().substring(0, 1).equalsIgnoreCase("t")
                        && ticket.getAssignedTo().equals(loggedUser.getFullName())) {

                    list.add(ticket);
                }
            } else if (!status.equalsIgnoreCase("open")
                    && ticket.getStatus().substring(0, 1).equalsIgnoreCase(status.substring(0, 1))) {

                list.add(ticket);

            }
        }

        return list;
    }

    /*
     * function that runs the Staff menu
     * 
     * Allows a staff to open a new ticket or log out
     * 
     */
    private boolean runStaffMenu() {

        int staffSelection;
        boolean logout = false;

        do {
            showHeaderTwo("Staff Menu");
            System.out.println("[1] Open Ticket");
            System.out.println("[2] Log out");
            System.out.print("Selection: ");
            staffSelection = validateUserSelection(2);

            switch (staffSelection) {
                case (1):

                    openTicket();
                    showTickets("Open");
                    break;
                case (2):

                    System.out.println("\nLogging out and returning to Service Portal...");
                    loggedUser = null;
                    logout = true;
                    break;
            }

        } while (logout == false);

        return logout;
    }

    /*
     * Open new ticket function
     * 
     * Takes user data input and creates a new ticket
     * Fields - description - user input (cannot be blank)
     * - severity - user selection from available options
     */
    private void openTicket() {

        showHeaderTwo("Open New Ticket");
        System.out.println("**Fields cannot be blank**\n");

        String description, severity = "";

        do {

            System.out.println("= Description = ");
            description = sc.nextLine();
            if (description.isBlank()) {
                System.out.println("Field cannot be left blank....");
            }

        } while (description.isBlank());

        System.out.println("= Severity =");
        System.out.println("[1] Low");
        System.out.println("[2] Medium");
        System.out.println("[3] High");
        int selection = validateUserSelection(3);

        switch (selection) {
            case (1):
                severity = "Low";
                break;
            case (2):
                severity = "Medium";
                break;
            case (3):
                severity = "High";
                break;
        }

        String openDateTime = getDateTime();

        String assignedTech = generateAssignedTech(severity);

        tickets.add(
                new Ticket(description, severity, openDateTime, loggedUser.getFullName(), assignedTech, "Open", null));

        saveData();

        System.out.println("\nNew Ticket opened and assigned...\n");

    }

    /*
     * function that allocates a tech staff based on their tech level, how many
     * current jobs they have and the ticket severity level
     * 
     * Returns the tech that matches the severity level, and has the least open
     * tickets
     * 
     */
    private String generateAssignedTech(String severity) {

        ArrayList<String> techs = new ArrayList<String>();

        if (severity == "Low" || severity == "Medium") {
            techs.add("Harry Styles");
            techs.add("Niall Horan");
            techs.add("Liam Payne");
        } else {
            techs.add("Louis Tomlinson");
            techs.add("Zayne Malik");
        }

        ArrayList<Integer> count = new ArrayList<Integer>();

        // calculations to determine who to get the task.
        for (String tech : techs) {
            int counter = 0;
            for (Ticket ticket : tickets) {
                if (ticket.getAssignedTo().equals(tech)) {
                    counter++;
                }
            }
            count.add(counter);
        }

        int lowest = count.indexOf(Collections.min(count));
        String allocatedTech = techs.get(lowest);

        return allocatedTech;

    }

    /*
     * function that gets the current Date and Time in a String
     * 
     * return String is in format dd/MM/yy HH:mm:ss
     * 
     */
    private String getDateTime() {

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");

        String formattedDateTime = now.format(formatter);

        return formattedDateTime;

    }

    /*
     * function that runs the Technician menu
     * 
     * Allows a tech to open a edit/close a ticket, re-open a ticket or log out.
     * 
     */
    private boolean runTechMenu() {

        int staffSelection;
        boolean logout = false;

        do {
            showHeaderTwo("Technician Menu");
            System.out.println("[1] Edit/Close Ticket");
            System.out.println("[2] Re-open Ticket");
            System.out.println("[3] Log out");
            System.out.print("Selection: ");
            staffSelection = validateUserSelection(3);

            switch (staffSelection) {
                case (1):
                    if (getTicketsListByStatus("Open").size() > 0) {

                        runEditCloseTickets();
                    } else {
                        System.out.println("\n*** No Tickets to edit ***");
                    }

                    break;
                case (2):

                    runArchiveCheck();

                    if (getTicketsListByStatus("Closed").size() > 0) {

                        runReopenTickets();
                    } else {
                        System.out.println("\n*** No Closed Tickets to re-open ***");
                    }
                    break;
                case (3):

                    System.out.println("\nLogging out and returning to Service Portal...");
                    loggedUser = null;
                    logout = true;
                    break;
            }

        } while (logout == false);

        return logout;
    }

    /*
     * function to reopen a closed ticket
     * 
     */
    private void runReopenTickets() {

        showHeaderTwo("Re-Open Ticket Menu");
        showTickets("Closed");
        System.out.print("\nSelect Ticket to edit (Ticket#): ");

        ArrayList<Ticket> list = getTicketsListByStatus("Closed");
        int selection = validateUserSelection(list.size());

        System.out.println("\n= SELECTED TICKET DETAILS =\n");
        Ticket ticket = list.get(selection - 1);

        ticket.displayDetails(loggedUser);

        System.out.println("\n= Confirm Re-opening Ticket =");
        System.out.println("[1] Yes");
        System.out.println("[2] No");
        System.out.print("Selection: ");
        selection = validateUserSelection(2);

        switch (selection) {
            case (1):

                ticket.setStatus("Open");
                ticket.setClosedDateTime(null);

                String assignedTech;

                if ((ticket.getSeverity().equalsIgnoreCase("low") || ticket.getSeverity().equalsIgnoreCase("medium")
                        && loggedUser.getStaffType().substring(0, 1).equalsIgnoreCase("t"))
                        || (ticket.getSeverity().equalsIgnoreCase("High")
                                && loggedUser.getStaffType().substring(0, 1).equalsIgnoreCase("s"))) {

                    assignedTech = generateAssignedTech(ticket.getSeverity());
                } else {
                    assignedTech = loggedUser.getFullName();
                }

                ticket.setAssignedTo(assignedTech);

                updateTicketCollection(ticket);

                System.out.println("\nTicket successfully re-opened... ");
                break;
            case (2):

                System.out.println("\nProcess canceled. Returning to Tech Menu...");
                break;
        }
    }

    /*
     * function for edit and close ticket feature
     * 
     * Allows a tech user to edit the severity of a ticket. This may re-allocate the
     * ticket to match severity to tech staff tier.
     * Allows a tech to close a ticket
     * 
     */
    private void runEditCloseTickets() {

        showHeaderTwo("Edit/Close Ticket Menu");
        showTickets("Open");
        System.out.print("\nSelect Ticket to edit (Ticket#): ");

        ArrayList<Ticket> list = getTicketsListByStatus("Open");
        int selection = validateUserSelection(list.size());

        System.out.println("\n= SELECTED TICKET DETAILS =\n");
        Ticket ticket = list.get(selection - 1);

        ticket.displayDetails(loggedUser);

        System.out.println("\n= Options =");
        System.out.println("[1] Edit Severity");
        System.out.println("[2] Close Ticket");
        System.out.println("[3] Return to Staff Menu");
        System.out.print("Selection: ");
        selection = validateUserSelection(3);

        switch (selection) {
            case (1):

                System.out.println("\nSelect new severity rating");
                System.out.println("[1] Low");
                System.out.println("[2] Medium");
                System.out.println("[3] High");
                System.out.print("Selection: ");
                int severitySelection = validateUserSelection(3);

                String severity = null;

                switch (severitySelection) {
                    case (1):
                        severity = "Low";
                        break;
                    case (2):
                        severity = "Medium";
                        break;
                    case (3):
                        severity = "High";
                        break;
                }

                ticket.setSeverity(severity);

                if (((severity.equals("Low") || severity.equals("Medium"))
                        && loggedUser.getStaffType().equalsIgnoreCase("t2"))
                        || (severity.equals("High") && loggedUser.getStaffType().equalsIgnoreCase("t1"))) {

                    ticket.setAssignedTo(generateAssignedTech(severity));
                }

                System.out.println(
                        "\nTicket severity changed. (Possible re-allocation if tech tier doesn't match severity)");
                break;

            case (2):

                System.out.println("\nWas this ticket resolved?");
                System.out.println("[1] Yes");
                System.out.println("[2] No");
                System.out.print("Selection: ");
                int closingSelection = validateUserSelection(2);

                if (closingSelection == 1) {
                    ticket.setStatus("Closed [Resolved]");
                } else {
                    ticket.setStatus("Closed [Unresolved]");
                }

                System.out.print("\nTicket status changed to ");

                if (closingSelection == 1) {
                    System.out.println("Closed [Resolved]");
                } else {
                    System.out.println("Closed [Unresolved]");
                }

                ticket.setClosedDateTime(getDateTime());

                break;

            case (3):
                System.out.println("Returning to Staff Menu...");
                break;

        }

        updateTicketCollection(ticket);

    }

    /*
     * function to update the details of a ticket within the master tickets
     * collection.
     * 
     * 
     */
    private void updateTicketCollection(Ticket ticket) {

        for (Ticket t : tickets) {
            if (t.getCreatedBy().equals(ticket.getCreatedBy())
                    && t.getOpenDateTime().equals(ticket.getOpenDateTime())) {
                t.setSeverity(ticket.getSeverity());
                t.setAssignedTo(ticket.getAssignedTo());
                t.setStatus(ticket.getStatus());
                t.setClosedDateTime(ticket.getClosedDateTime());
            }
        }

        saveData();
    }

    /*
     * function that checks the tickets collection and changes the status of any
     * ticket that has been closed for 24 or more hours to Archived.
     * 
     */
    private void runArchiveCheck() {

        for (Ticket ticket : tickets) {

            if (ticket.getStatus().substring(0, 1).equalsIgnoreCase("c")) {

                LocalDateTime closedDT = convertToDateTime(ticket.getClosedDateTime());

                LocalDateTime currentTime = LocalDateTime.now();

                Duration duration = Duration.between(closedDT, currentTime);

                String statusSuffix = ticket.getStatus().substring(6, ticket.getStatus().length());

                if (duration.toHours() >= 24) {

                    ticket.setStatus("Archived" + statusSuffix);
                }

            }

        }

        saveData();

    }

    /*
     * Utility function that converts a String in a particular date time format to a
     * LocalDateTime variable.
     * 
     */
    private LocalDateTime convertToDateTime(String closedDateTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");

        return LocalDateTime.parse(closedDateTime, formatter);
    }

}

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
    String passwordLength = "5";

    public void run() {

        loadHardCodedUsers(); // hardcoded staff data. May change to loading from a csv for easy data
                              // manipulation during testing

        loadData(); // used to load dummy data for testing.

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

        String nameRegex = "^[A-Z][a-z]*(?: [A-Z][a-z]*)+$";
        String nameErrorMessage = "Full ame cannot be blank. Must begin with an Upper case letter and have a space between first name and surname. Please try again...";
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
                System.out.printf("\n%-16s: %s", "Open Date/Time", ticket.getOpenDateTime());
                System.out.printf("\n%-16s: %s", "Severity", ticket.getSeverity());
                System.out.printf("\n%-16s: %s", "Description", ticket.getDescription());

                if (loggedUser.getStaffType().equalsIgnoreCase("s")) {

                    System.out.printf("\n%-16s: %s\n", "Assigned To", ticket.getAssignedTo());

                } else {

                    System.out.printf("\n%-16s: %s\n", "Created By", ticket.getCreatedBy());

                    if (!ticket.getStatus().substring(0, 1).equalsIgnoreCase("o")) {

                        System.out.printf("\n%-16s: %s\n", "Closed Date/Time", ticket.getClosedDateTime());
                    }
                }

                System.out.println("-".repeat(50));

            }
        }
    }

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

    private void openTicket() {

        showHeaderTwo("Open New Ticket");
        System.out.println("**Fields cannot be blank**\n");

        String description, severity;

        do {

            System.out.print("Description: ");
            description = sc.nextLine();
            if (description.isBlank()) {
                System.out.println("Field cannot be left blank....");
            }

        } while (description.isBlank());

        System.out.println("Severity ");
        System.out.println("[1] Low");
        System.out.println("[2] Medium");
        System.out.println("[3] High");
        int selection = validateUserSelection(3);

        if (selection == 1) {

            severity = "Low";
        } else if (selection == 2) {

            severity = "Medium";
        } else {

            severity = "High";
        }

        String openDateTime = getDateTime();

        String allocatedStaff = getAllocatedStaff();

    }

    private String getAllocatedStaff() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllocatedStaff'");
    }

    private String getDateTime() {

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");

        return now.format(formatter);

    }

    private boolean runTechMenu() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'runTechMenu'");
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

}

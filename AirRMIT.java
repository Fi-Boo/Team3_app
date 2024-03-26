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
import java.util.ArrayList;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
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

    boolean exit = false;

    String passwordLength = "5";

    public void run() {

        //hardcoded technicians - as per brief
        users.add(new User("harrys@airrmit.com", "Harry Styles", "0430303030", hashPassword("iamharry", generateSalt()), "t1"));
        users.add(new User("niallh@airrmit.com", "Niall Horan", "0431313131", hashPassword("iamniall", generateSalt()), "t1"));
        users.add(new User("liamp@airrmit.com", "Liam Payne", "0432323232", hashPassword("iamliam", generateSalt()), "t1"));
        users.add(new User("louist@airrmit.com", "Louis Tomlinson", "0433333333", hashPassword("iamlouis", generateSalt()), "t2"));
        users.add(new User("zaynem@airrmit.com", "Zayne Malik", "0434343434", hashPassword("iamzayne", generateSalt()), "t2"));
        users.add(new User("angelol@airrmit.com", "Angelo Lapuz", "0433914378", hashPassword("iamangelo", generateSalt())));
        users.add(new User("belrose@airrmit.com", "Belrose Gunzel", "0433427269", hashPassword("iambelrose", generateSalt())));
        users.add(new User("nickd@airrmit.com", "Nick Drinkwater", "0433508178", hashPassword("iambick", generateSalt())));
        users.add(new User("phib@airrmit.com", "Phi Bui", "0432008156", hashPassword("iamphi", generateSalt())));


        do {
            
            int selection = portalMenu();

            switch (selection) {
                case (1):

                    userRegistrationFeature();
                    break;

                case (2):

                    boolean logout = false;

                    do {
                        
                        logout = existingUserFeature();
                        
                    } while (!logout);
                    break;

                case (3):
                    exit = true;
                    break;
            }

        } while(!exit);

        System.out.println("\nExiting Program...\n");
        
    }


    //main Portal menu
    private int portalMenu() {

        System.out.println("*".repeat(50));
        System.out.println(name +" Service Portal");
        System.out.println("*".repeat(50));
        System.out.println("");
        System.out.println("[1] New Staff Registration");
        System.out.println("[2] Existing User Login");
        System.out.println("[3] Exit");
        System.out.print("Selection: ");

        return validateSelection(3);

    }

    // Submenu. takes menu title as a String
    private void showSubmenu(String string){
        System.out.println("");
        System.out.println("*".repeat(50));
        System.out.println(string);
        System.out.println("*".repeat(50));
        System.out.println("");
    }

    /* User Registration Feature        COMPLETE
     * 1. Add user email                COMPLETE
     *      - validate via regex        COMPLETE
     *      - check for existing email  COMPLETE
     * 2. Add full name                 COMPLETE
     *      - validate via regex        COMPLETE
     * 3. Add password                  COMPLETE
     *      - validate via regex        COMPLETE
     * 4. Add number                    COMPLETE
     *      - validate via regex        COMPLETE
     */
    private void userRegistrationFeature() {

        showSubmenu("User Registration");

        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        String emailErrorMessage = "Invalid email format. Please try again...";
        String validEmail = null;

        boolean userExists = true;
        do {

            validEmail = validateRegex("Email Address: ", emailRegex, emailErrorMessage );
            userExists = checkEmail(validEmail);
            if (userExists) {
                System.out.println("Email already registered. Please try again...");
            }

        } while (userExists);

        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{"+passwordLength+",}$";
        String passwordErrorMessage = "Invalid password format. At least 1 upper case, 1 lower case, 1 number. Min length " + passwordLength + " characters. Please try again...";
        
        String validPassword = validateRegex("Password: ", passwordRegex, passwordErrorMessage);

        String nameRegex = "^[A-Z][a-z]*(?: [A-Z][a-z]*)+$";
        String nameErrorMessage = "Full ame cannot be blank. Must begin with an Upper case letter and have a space between first name and surname. Please try again...";

        String validName = validateRegex("Full Name: ", nameRegex, nameErrorMessage);
        
        String phoneRegex = "^(0)[1-9](?:[0-9]{8}|(?:\\s[0-9]{3,4}){3})$";
        String numberErrorMessage = "Invalid AUS mobile number. Must be in format 04xxxxxxxx. Please try again...";

        String validNumber = validateRegex("Mobile contact: ", phoneRegex, numberErrorMessage);

        System.out.println("");
        System.out.println("New user registered with the following details");
        System.out.println(validEmail);
        System.out.println(validName);
        System.out.println(validNumber);
        System.out.println("");

        users.add(new User(validEmail.toLowerCase(), validName, validNumber, hashPassword(validPassword, generateSalt())));

    }

    // validates user input based on regex value
    private String validateRegex(String title, String regex, String errorMsg) {
        
        Pattern pattern = Pattern.compile (regex);
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

    // checks for existing email based on user input 
    private boolean checkEmail(String email) {

        for (User user: users) {
            if (user.getEmail().equals(email.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /* Existing User Feature
     * 1.Login
     *      - validates email/password input    COMPLETE
     *      - password salt/hashing             COMPLETE
     *      - forgot password                   COMPLETE
     * 2.Loads submenu based on user Role       COMPLETE
     * 3.Role(S)                                
     *      - add Ticket                        COMPLETE
     *      - view current tickets              COMPLETE         
     * 4.Role(T1/T2)
     *      - view current jobs                 COMPLETE
     *      - edit job classification           COMPLETE
     *          - relocate job if applicable    COMPLETE
     *      - close job                         COMPLETE
     *      - view all jobs                     COMPLETE
     *      - reopen job                        COMPLETE
     *      - view archive list                 COMPLETE
     *          - function to archive           COMPLETE
     * 5. Logout (returns to portal main)       COMPLETE
     * 
     * 6. TESTING
     *      - create csv file with dummy data       INCOMPLETE
     *      - create function to load dummy data    INCOMPLETE
     *          - this will test archiveTickets()
     * 
     * 
     */
    private boolean existingUserFeature() {
        
        int selection = 0;
        boolean logout = false;

        do {

            showSubmenu("Existing User Login");

            System.out.println("[1] Login");
            System.out.println("[2] Forgot Password");
            System.out.println("[3] Return to Main Portal");
            System.out.print("Selection: ");
            selection = validateSelection(3);

            switch (selection) {
                case(1):

                    boolean validLogin = false; 
                    boolean failedLogin = false;
                    int remainingAttempts = 3;
                    String inputEmail = "";
                    String inputPassword;

                    while (remainingAttempts >= 0 && validLogin == false && failedLogin == false) {
                
                        System.out.print("Email: ");
                        inputEmail = sc.nextLine();
                        System.out.print("Password: ");
                        inputPassword = sc.nextLine();

                        validLogin = checkLogin(inputEmail, inputPassword);

                        remainingAttempts--;

                        if (remainingAttempts > 0 && validLogin == false) {
                            System.out.println("Incorrect login credentials. " + remainingAttempts + " attempts remaining...");
                        } else if (remainingAttempts == 0 && validLogin == false) {
                            System.out.println("\nUnsuccessful login attempts. Exiting to main portal...\n");
                            failedLogin = true;
                        }
                    }
                    
                    if (failedLogin == true) {
                        return true;
                    } else {

                        loggedUser = setUser(inputEmail);

                        archiveTickets();

                        do {
                            System.out.println("");
                            System.out.println("=".repeat(50));
                            System.out.println("Hi " + loggedUser.getFullName());
                            System.out.println("-".repeat(50));
                            System.out.println("");

                            if (loggedUser.getStaffType() == "s") {
                              
                                do {
                                    
                                    showTickets("open");

                                    System.out.println("");
                                    System.out.println("[1] Open New Ticket");
                                    System.out.println("[2] Log Out");
                                    System.out.print("Selection: ");

                                    selection = validateSelection(2);

                                    switch (selection) {
                                        case (1):

                                            System.out.println("");
                                            System.out.println("=".repeat(50));
                                            System.out.println("New Ticket Menu");
                                            System.out.println("-".repeat(50));
                                            System.out.println("");

                                            openTicket();

                                            break;
                                        case (2):
                                            logout = true;
                                    }


                                } while (selection != 2);

                            } else {
                                
                                int staffSelection;

                                do {
                                
                                    showTickets("open");
                                    showTickets("closed");
                                    showTickets("archived");
                                    
                                    System.out.println("=".repeat(50));

                                    System.out.println("");
                                    System.out.println("[1] Edit/Close Job");
                                    System.out.println("[2] Reopen Job");
                                    System.out.println("[3] Log out");
                                    System.out.print("Selection: ");

                                    staffSelection = validateSelection(4);

                                    switch(staffSelection) {
                                        case(1):

                                            System.out.println("=".repeat(50));
                                            System.out.println("= Open Job List =");
                                            System.out.println("=".repeat(50));

                                            showTickets("open");

                                            int jobCount = countJobs("open");

                                            System.out.println("-".repeat(50));
                                            System.out.print("Edit Job#: ");
                                            int jobSelect = validateSelection(jobCount);

                                            Ticket selectedTicket = showTicket("open", jobSelect);
                                            
                                            System.out.println("=".repeat(50));
                                            System.out.println("= Job Menu =");
                                            System.out.println("=".repeat(50));
                                            System.out.println("Description: " + selectedTicket.getDescription());
                                            System.out.println("Severity: " + selectedTicket.getSeverity());
                                            System.out.println("Status: " + selectedTicket.getStatus());    
                                            System.out.println("-".repeat(50));                                     
                                            System.out.println("[1] Change Job Severity");
                                            System.out.println("[2] Close Job");
                                            System.out.print("Selection: ");

                                            int editSelection = validateSelection(2);

                                            switch (editSelection) {
                                                case (1):
                                                    System.out.println("");
                                                    System.out.println("[1] Low");
                                                    System.out.println("[2] Medium");
                                                    System.out.println("[3] High");
                                                    System.out.print("Selection: ");

                                                    int severitySelection = validateSelection(3);
                                                    String severity;
                                                    if (severitySelection == 1) {
                                                        severity = "Low";
                                                    } else if (severitySelection == 2) {
                                                        severity = "Medium";
                                                    } else {
                                                        severity = "High";
                                                    }

                                                    selectedTicket.setSeverity(severity);

                                                    updateTicket(selectedTicket);

                                                    System.out.println("\nJob Severity changed...\n");

                                                    break;

                                                case (2):

                                                    System.out.println("");
                                                    System.out.println("Was the issue resolved?");
                                                    System.out.println("[1] Yes");
                                                    System.out.println("[2] No");
                                                    System.out.print("Selection: ");

                                                    int closingSelection = validateSelection(2);

                                                    String closed;

                                                    if (closingSelection == 1) {

                                                        closed = "closed [Resolved]";

                                                    } else {

                                                        closed = "closed [Unresolved]";

                                                    }

                                                    closeTicket(selectedTicket, closed);
                                                     
                                                    System.out.println("");
                                                    System.out.println("Ticket closed...");
                                                    System.out.println("");

                                                    break;

                                            }

                                            break;

                                        case(2):

                                            System.out.println("=".repeat(50));
                                            System.out.println("= Closed Job List =");
                                            System.out.println("=".repeat(50));

                                            showTickets("closed");
                                            System.out.println("-".repeat(50));
                                            
                                            int closedJobCount = countJobs("closed");

                                            System.out.print("Job to re-open: ");

                                            int reopenJobSelection = validateSelection(closedJobCount);

                                            Ticket reopenTicket = showTicket("closed", reopenJobSelection);

                                            reOpenTicket(reopenTicket);

                                            System.out.println("");
                                            System.out.println("Ticket reopened. It may be reallocated if it's severity level does not match your support level.");
                                            System.out.println("");

                                            break;
                                    }

                                } while (staffSelection != 3);
                            
                            }
                        } while (logout == false);
                    }
                    break;
                case(2):         
                    resetPassword();
                    break;
                case(3):
                    System.out.println("\nReturning to portal menu...\n"); 
                    logout = true;
                    loggedUser = null;
                    break;
            }
            
        } while (selection != 3 || logout != true);
        
        return logout;     
        
    }



    private void closeTicket(Ticket selectedTicket, String closed) {

        for (Ticket ticket: tickets) {
            if (ticket.getCreatedBy().equals(selectedTicket.getCreatedBy()) && ticket.getOpenDateTime().equals(selectedTicket.getOpenDateTime())) {
                ticket.setClosedDateTime(getCurrentDateTime());
                ticket.setStatus(closed);
            }
        }
    }

        
    private void reOpenTicket(Ticket ticket) {


        for (Ticket t: tickets) {
            if (t.getCreatedBy().equals(ticket.getCreatedBy()) && t.getOpenDateTime().equals(ticket.getOpenDateTime())) {
                t.setStatus("open");
                t.setClosedDateTime(null);
                
                if ((ticket.getSeverity().equals("High") && !loggedUser.getStaffType().equals("t2")) || 
                    (!ticket.getSeverity().equals("High") && loggedUser.getStaffType().equals("t2"))) 
                    {
                        allocateTicket(t);
                
                } else {

                    t.setAssignedTo(loggedUser.getEmail());

                }      
                
            }
        }
    }


    private void updateTicket(Ticket ticket) {
        
        if (((ticket.getSeverity().equals("Low") || ticket.getSeverity().equals("Medium")) && loggedUser.getStaffType().equals("t2")) ||
            (ticket.getSeverity().equals("High") && loggedUser.getStaffType().equals("t1" ))) {

                ticket.setAssignedTo("");

                allocateTicket(ticket);

            }
        
        for (Ticket t :tickets) {
            if (t.getOpenDateTime().equals(ticket.getOpenDateTime()) && t.getCreatedBy().equals(ticket.getCreatedBy())) {
                t.setSeverity(ticket.getSeverity());
                t.setAssignedTo(ticket.getAssignedTo());
            }
        }
    }

    private Ticket showTicket(String string, int jobSelect) {
        
        ArrayList<Ticket> tList = getTicketsList(string);

        return tList.get(jobSelect-1);
    }


    private int countJobs(String string) {
        
        return getTicketsList(string).size();

    }


    private ArrayList<Ticket> getTicketsList(String status) {
        
        ArrayList<Ticket> ticketsList = new ArrayList<Ticket>();

        for (Ticket ticket: tickets) {

            if (status.equals("open")) {
                
                if (loggedUser.getStaffType().equals("s")) {

                    if (ticket.getCreatedBy().equals(loggedUser.getEmail()) && ticket.getStatus().equals(status)) {
                
                        ticketsList.add(ticket);
                    }

                } else {

                    if (ticket.getAssignedTo().equals(loggedUser.getEmail()) && ticket.getStatus().equals(status)) {
                
                        ticketsList.add(ticket);
                    }
                }

            } else {
                
                if (ticket.getStatus().substring(0,4).equals(status.substring(0,4))) {

                    ticketsList.add(ticket);

                }

            }
            
        }
        
        return ticketsList;
    }

    private void showTickets(String status) {
        
        System.out.println("-".repeat(50));
        System.out.println(status +" Tickets");

        ArrayList<Ticket> tList = getTicketsList(status);

        if (tList.size() > 0 ) {
            
            for (Ticket ticket: tList) {
                
                System.out.println("-".repeat(50));
                System.out.println("Ticket #" + (tList.indexOf(ticket)+1));
                System.out.println("Created on: " + ticket.getOpenDateTime());
                System.out.println("Description: " + ticket.getDescription());
                System.out.println("Severity: " + ticket.getSeverity());
                if (loggedUser.getStaffType().charAt(0) == 't') {
                    System.out.println("Status: " + ticket.getStatus());
                    if (!status.equals("open")) {
                        System.out.println("Closed on: " + ticket.getClosedDateTime());
                    }
                } else {
                    System.out.println("Assigned to: " + getUserByEmail(ticket.getAssignedTo()));
                }
            }
            
        } else {

            System.out.println("None ");

        }

    }


    private String getUserByEmail(String assignedTo) {
        
        String name = null;

        for (User user: users) {
            if (user.getEmail().equals(assignedTo)) {
                name = user.getFullName();
            }
        }
        return name;
    }


    private void openTicket() {
        

        String description = notBlank("Issue description: ");
        
        System.out.println("");
        System.out.println("= Severity =");
        System.out.println("[1] Low ");
        System.out.println("[2] Medium ");
        System.out.println("[3] High ");
        System.out.print("Selection: ");

        int selection = validateSelection(3);
        String severity;

        if (selection == 1) {
            severity = "Low";
        }else if (selection == 2) {
            severity = "Medium";
        }else {
            severity = "High";
        }


        Ticket ticket = new Ticket(description, severity, getCurrentDateTime(), loggedUser.getEmail());

        allocateTicket(ticket);
        
        tickets.add(ticket);

        System.out.println("\nTicket successfully opened...");
    }


    private String getCurrentDateTime() {

        LocalDateTime now = LocalDateTime.now();
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");

        return now.format(format);
    }

    private void archiveTickets() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime check = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");

        Duration duration;

        for (Ticket t: tickets) {

            if (t.getStatus().charAt(0) == 'c') {

            
                try {

                    check = LocalDateTime.parse(t.getClosedDateTime(), formatter);

                    duration = Duration.between(check,now);

                    if (duration.toHours() > 24) {
                        t.setStatus("archived");
                    }
                    

                } catch (DateTimeParseException e) {

                    e.printStackTrace();

                }
            }
        }

    }


    private void allocateTicket(Ticket ticket) {

        String staffAllocated = getAllocation(ticket.getSeverity());

        ticket.setAssignedTo(staffAllocated);

    }

    private String getAllocation(String severity) {

        ArrayList<String> techs = new ArrayList<String>();
      
        if (severity.equals("Low") || severity.equals("Medium")) {
            techs.add("harrys@airrmit.com");
            techs.add("niallh@airrmit.com");
            techs.add("liamp@airrmit.com");
        } else {
            techs.add("louist@airrmit.com");
            techs.add("zaynem@airrmit.com");
        }

        ArrayList<Integer> count = new ArrayList<Integer>();

        //calculations to determine who to get the task.
        for (String tech: techs) {
            int counter = 0;
            for (Ticket ticket: tickets) {
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


    private String notBlank(String description) {

        String input;
        do {
            System.out.println("-".repeat(50));
            System.out.println(description);
            input = sc.nextLine();
        } while (input.isBlank());
        return input;
    }


    private User setUser(String inputEmail) {

        for (User user: users) {
            if (user.getEmail().equalsIgnoreCase(inputEmail)) {
                return user;
            }
        }
        return null;
    }

    private boolean checkLogin(String inputEmail, String inputPassword) {
        
        for (User user: users) {
            if (user.getEmail().equalsIgnoreCase(inputEmail) && validatePassword(user.getPassword(),inputPassword) == true) {
                return true;
            }
        }
        return false;
    }

    private int validateSelection(int selection) {
        
        int input = 0;
        boolean invalid = true;

        do {
            try {
				input = Integer.parseInt(sc.nextLine());
				if ( input < 1 || input > selection) {
					System.out.print("Invalid input! (Must be from 1 to " + selection + "): ");
				} else {
					invalid = false;
				}	
			} catch (NumberFormatException e) {
				System.out.print("Invalid input! (Must be a numerical value): ");
			}

        } while (invalid);

        return input;
    }


    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String hashPassword(String password, String salt) {

        String finalPassword = null;

        try {
        
            String saltedPassword = password + salt;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(saltedPassword.getBytes());
            finalPassword = Base64.getEncoder().encodeToString(hashedPassword) + salt;
            return finalPassword;

        } catch (NoSuchAlgorithmException e) {
            System.out.println("something wrong");
        }

        return null;

    }

    private boolean validatePassword(String storedPassword, String inputPassword) {

        String salt = storedPassword.substring(storedPassword.length() - 24);

        if (storedPassword.equals(hashPassword(inputPassword, salt))) {
            return true;
        }

        return false;
    }

    private void resetPassword() {

        System.out.println("");
        System.out.println("=".repeat(50));
        System.out.println("Password Reset menu");
        System.out.println("-".repeat(50));
        System.out.println("");

        boolean valid = false, validNumber = false;
        String email = null;
        int remainingAttempts = 3;
        
        do {
            
            System.out.print("Email: ");
            email = sc.nextLine();
            valid = checkEmail(email);
            if (valid == false) {
                System.out.println("No matching Email found. Try again...");
            }

        } while (valid == false);

        while (remainingAttempts > 0 && validNumber == false ) {
            System.out.print("Mobile Number: ");
            String number = sc.nextLine();
            for (User user: users) {
                if (user.getNumber().equals(number)) {
                    validNumber = true;
                }     
            }

            remainingAttempts--;
            System.out.println("Mismatch with record. "+ remainingAttempts+ " attempts remaining...");
            
        }
        if (remainingAttempts == 0) {
            System.out.println("");
            System.out.println("Returning to Login Selection...");
        }
        if (validNumber == true) {
            
            String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{"+passwordLength+",}$";
            String passwordErrorMessage = "Invalid password format. At least 1 upper case, 1 lower case, 1 number. Min length " + passwordLength + " characters. Please try again...";
            
            String password = validateRegex("New Password: ", passwordRegex, passwordErrorMessage);

            for (User user: users) {
                if (user.getEmail().equals(email)) {
                    user.setPassword(hashPassword(password, generateSalt()));
                }
            }
        }
        
    }
}
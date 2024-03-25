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
        users.add(new User("zaynm@airrmit.com", "Zayne Malik", "0434343434", hashPassword("iamzayne", generateSalt()), "t2"));
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
     *      - view current jobs                 COMING SOON
     *      - edit job classification           COMING SOON
     *          - relocate job if applicable    COMING SOON
     *      - close job                         COMING SOON
     *      - view all jobs                     COMING SOON
     *      - reopen job                        COMING SOON
     * 5. Logout (returns to portal main)       COMPLETE
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

                                do {
                                
                                    showAssignedJobs("open");
                                    System.out.println("-".repeat(50));
                                    showAssignedJobs("closed");
                                    System.out.println("-".repeat(50));

                                    System.out.println("");
                                    System.out.println("[1] Edit/Close Job");
                                    System.out.println("[2] Reopen Job");
                                    System.out.println("[3] Log out");
                                    System.out.print("Selection: ");

                                    selection = validateSelection(4);

                                    switch(selection) {
                                        case(1):

                                            System.out.println("=".repeat(50));
                                            System.out.println("= Open Job List =");
                                            System.out.println("=".repeat(50));
                                            showAssignedJobs("open");


                                            /* 
                                             * 
                                             * key  C - closed      - can be reopened
                                             *      A - archived    - cannot be reopened
                                             * 
                                             *  Select Job
                                             *  input job number
                                             *  output job
                                             *      [1] edit
                                             *          - Set severity
                                             *              [1] Low
                                             *              [2] Medium
                                             *              [3] High
                                             *                  (update variable in ticket)
                                             *                  (reallocate if necessary)
                                             *      [2] close job
                                             *          - Set closure status
                                             *              [1] closed [resolved]
                                             *              [2] close [unresolved]
                                             *                  (update variables in ticket)
                                             * 
                                             */

                                            break;
                                        case(2):


                                            showAssignedJobs("closed");

                                            /* 
                                             * key  C - closed      - can be reopened
                                             *      A - archived    - cannot be reopened
                                             * 
                                             * select job to reopen
                                             * input job number
                                             * update/remove variables from job
                                             * set job to tech who opened it
                                             * 
                                             * 
                                             */

                                            System.out.println("Reopen Job");
                                            break;

                                        case(3):

                                            logout = true;
                                    }

                                } while (selection != 3);
                            
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

    private void showAssignedJobs(String status) {
        
        System.out.println("-".repeat(50));
        System.out.println(status +" Jobs");

        int counter = 1;
        for (Ticket ticket: tickets) {
            if (ticket.getCreatedBy().equals(loggedUser.getEmail()) && ticket.getStatus().substring(0,4).equals(status.substring(0,4))) {
                
                System.out.println("-".repeat(50));
                System.out.println("Job #" + counter);
                if (loggedUser.getStaffType().charAt(0) == 't') {
                    System.out.println("Status: " + ticket.getStatus());
                }
                System.out.println("Description: " + ticket.getDescription());
                System.out.println("Severity: " + ticket.getSeverity());
                counter++;
            }
        }
        if (counter == 1) {
            System.out.println("None ");
        }
    }

    


    private void showTickets(String status) {
        
        System.out.println("-".repeat(50));
        System.out.println(status +" Tickets");

        int counter = 1;
        for (Ticket ticket: tickets) {
            if (ticket.getCreatedBy().equals(loggedUser.getEmail()) && ticket.getStatus().equals(status)) {
                
                System.out.println("-".repeat(50));
                System.out.println("Ticket #" + counter);
                System.out.println("Description: " + ticket.getDescription());
                System.out.println("Severity: " + ticket.getSeverity());
                System.out.println("Assigned to: " + getUserByEmail(ticket.getAssignedTo()));
                counter++;
            }
        }
        if (counter == 1) {
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

        Ticket ticket = new Ticket(description, severity, loggedUser.getEmail());

        allocateTicket(ticket);
        
        tickets.add(ticket);

        System.out.println("\nTicket successfully opened...");
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
            techs.add("zaynm@airrmit.com");
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
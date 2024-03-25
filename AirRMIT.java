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


public class AirRMIT {

    private String name;
    private ArrayList<User> users;
    private User loggedUser;

    public AirRMIT(String string) {
        this.name = string;
        this.users = new ArrayList<User>();
        this.loggedUser = null;
    }

    Scanner sc = new Scanner(System.in);

    boolean exit = false;

    public void run() {

        //hardcoded technicians - as per brief
        users.add(new User("harrys@airrmit.com", "Harry Styles", "0430303030", hashPassword("iamharry", generateSalt()), "t1"));
        users.add(new User("niallh@airrmit.com", "Niall Horan", "0431313131", hashPassword("iamniall", generateSalt()), "t1"));
        users.add(new User("liamp@airrmit.com", "Liam Payne", "0432323232", hashPassword("iamliam", generateSalt()), "t1"));
        users.add(new User("louist@airrmit.com", "Louis Tomlinson", "0433333333", hashPassword("iamlouis", generateSalt()), "t2"));
        users.add(new User("zaynm@airrmit.com", "Zayne Malik", "0434343434", hashPassword("iamzayne", generateSalt()), "t2"));

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

        System.out.println("End");
        
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

        String passwordLength = "5";
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{"+passwordLength+",}$";
        String passwordErrorMessage = "Invalid password format. At least 1 upper case, 1 lower case, 1 number. Min length " + passwordLength + " characters. Please try again...";
        
        String validPassword = validateRegex("Password: ", passwordRegex, passwordErrorMessage);

        String nameRegex = "^[A-Z][a-z]*(?: [A-Z][a-z]*)+$";
        String nameErrorMessage = "Full ame cannot be blank. Must begin with an Upper case letter and have a space between first name and surname. Please try again...";

        String validName = validateRegex("Full Name: ", nameRegex, nameErrorMessage);
        
        String phoneRegex = "^(0)[1-9](?:[0-9]{8}|(?:\\s[0-9]{3,4}){3})$";
        String numberErrorMessage = "Invalid AUS mobile number. Must be in format 04xxxxxxxx. Please try again...";

        String validNumber = validateRegex("Mobile contact: ", phoneRegex, numberErrorMessage);

        System.out.println("user details:");
        System.out.println(validEmail);
        System.out.println(validPassword);
        System.out.println(validName);
        System.out.println(validNumber);

        users.add(new User(validEmail.toLowerCase(), validName, validNumber, validPassword));

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
     * 2.Loads submenu based on user Role       COMING SOON
     * 3.Role(S)                                
     *      - add Ticket                        COMING SOON
     *      - view current tickets              COMING SOON         
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
        
        boolean validLogin = false; 
        boolean failedLogin = false;
        int remainingAttempts = 3;
        String inputEmail = "";
        String inputPassword;

        showSubmenu("Existing User Login");
            
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
                System.out.println("Unsuccessful login attempts. Exiting to main portal...\n");
                failedLogin = true;
            }
        }

        if (failedLogin == true) {
            return true;
        } else {

            loggedUser = setUser(inputEmail);
            
            int staffSelection;

            do {
                System.out.println("");
                System.out.println("=".repeat(50));
                System.out.println("Hi " + loggedUser.getFullName());
                System.out.println("-".repeat(50));
                System.out.println("");
                System.out.println("[1] Coming Soon");
                System.out.println("[2] Log out");
                System.out.print("Selection: ");

                staffSelection = validateSelection(2);
                
                if (staffSelection == 2) {
                    System.out.println("Returning to portal menu...\n");
                    return true;
                }
            
            } while (staffSelection != 2);
        }
        return false;
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
        System.out.println(salt);

        if (storedPassword.equals(hashPassword(inputPassword, salt))) {
            return true;
        }

        return false;
    }

}
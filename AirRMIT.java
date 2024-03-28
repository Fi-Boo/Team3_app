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

    public void run() {

        loadHardCodedUsers();

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

                    // NEEDS TO BE WORKED ON NEXT
                    runLoginFeature();

                    break;
                case (3):

                    System.out.println("Reset Password");
                    break;
                case (4):
                    System.out.println("Exiting System...");

            }

        } while (userInput != 4);
    }

    private void runLoginFeature() {

        showHeaderOne("User Login Menu");
        System.out.println("\nInput Credentials");
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        // credential validation function needs to be done
        System.out.println(email + " " + password + "needs to be checked");

    }

    private void runRegistrationFeature() {

        // for testing purposes I set at 5. Change to 20 for final submission
        String passwordLength = "5";

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
        loggedUser = newUser;

        System.out.println("\nUser registration successful.");
        System.out.println("Returning to Service Portal\n");
    }

    private boolean checkEmail(String email) {

        for (User user : users) {

            if (user.getEmail().equalsIgnoreCase(email)) {

                return true;
            }
        }
        return false;
    }

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

                System.out.print("Invalid selection. Use a number dumbass! Try again: ");
            }

        } while (validInput == false);

        return userInput;
    }

    private void showHeaderOne(String title) {

        System.out.println("");
        System.out.println("*".repeat(50));
        System.out.println(name + " " + title);
        System.out.println("*".repeat(50));
    }

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

}

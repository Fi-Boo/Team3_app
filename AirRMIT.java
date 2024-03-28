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

                    System.out.println("registration menu!");

                    break;
                case (2):

                    System.out.println("Existing Login Menu");

                    break;
                case (3):

                    System.out.println("Reset Password");
                    break;
                case (4):
                    System.out.println("Exiting System...");

            }

        } while (userInput != 4);
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

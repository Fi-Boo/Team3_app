
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;


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
        users.add(new User("harrys@airrmit.com", "Harry", "Styles", 0430303030, "iamharry", "t1"));
        users.add(new User("niallh@airrmit.com", "Niall", "Horan", 0431313131, "iamniall", "t1"));
        users.add(new User("liamp@airrmit.com", "Liam", "Payne", 0432323232, "iamliam", "t1"));
        users.add(new User("louist@airrmit.com", "Louis", "Tomlinson", 0433333333, "iamlouis", "t2"));
        users.add(new User("zaynm@airrmit.com", "Zayne", "Malik", 0434343434, "iamzayne", "t2"));

        do {
            
            int selection = portalMenu();

            switch (selection) {
			case (1):

                boolean logout = false;

                do {

                    boolean validLogin = false; 
                    boolean failedLogin = false;
                    int remainingAttempts = 3;
                    String inputEmail = "";
                    String inputPassword;

                        System.out.println("");
                        System.out.println("*".repeat(50));
                        System.out.println("Existing User Login");
                        System.out.println("*".repeat(50));
                    

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
                        logout = true;
                    } else {

                        loggedUser = setUser(inputEmail);
                        
                        int staffSelection;

                        do {
                            System.out.println("");
                            System.out.println("=".repeat(50));
                            System.out.println("Hi " + loggedUser.getFirstName());
                            System.out.println("-".repeat(50));
                            System.out.println("");
                            System.out.println("[1] Coming Soon");
                            System.out.println("[2] Log out");
                            System.out.print("Selection: ");

                            staffSelection = validateSelection(2);
                            
                            if (staffSelection == 2) {
                                System.out.println("Returning to portal menu...\n");
                                logout = true;
                            }
                        
                        } while (staffSelection != 2);
                    }
                    
                } while (!logout);
                break;

			case (2):


                System.out.println("");
                System.out.println("*".repeat(50));
                System.out.println("User Registration");
                System.out.println("*".repeat(50));
                System.out.println("");


                String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
                Pattern pattern = Pattern.compile (emailRegex);
                
                String userEmail;
                boolean matchFound = false;

                do {
                    System.out.print("Email Address: ");
                    userEmail = sc.nextLine();
                    Matcher matcher = pattern.matcher(userEmail);
                    matchFound = matcher.find();
                    if (matchFound == false) {
                        System.out.println("Invalid email format. Please try again...");
                    }
                } while (matchFound == false);

                String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{20,}$";
                pattern = Pattern.compile (passwordRegex);
                
                String password;
                matchFound = false;

                do {
                    System.out.print("Password: ");
                    password = sc.nextLine();
                    Matcher matcher = pattern.matcher(password);
                    matchFound = matcher.find();
                    if (matchFound == false) {
                        System.out.println("Invalid password format. At least 1 upper case, 1 lower case, 1 number. Min length 20 characters. Please try again...");
                    }
                } while (matchFound == false);


                String nameRegex = "^[A-Z][a-z\\-]*$";
                pattern = Pattern.compile (nameRegex);

                String firstName;

                do {
                    System.out.print("First Name: ");
                    firstName = sc.nextLine();

                    Matcher matcher = pattern.matcher(firstName);
                    matchFound = matcher.find();

                    if (matchFound == false) {
                        System.out.println("First name cannot be blank. Must begin with an Upper case letter. Please try again...");
                    }
                } while (matchFound == false);

                String surname;

                do {
                    System.out.print("Surname: ");
                    surname = sc.nextLine();

                    Matcher matcher = pattern.matcher(firstName);
                    matchFound = matcher.find();

                    if (matchFound == false) {
                        System.out.println("Surname name cannot be blank. Must begin with an Upper case letter. Please try again...");
                    }
                } while (matchFound == false);

                String phoneNumber;
                String phoneRegex = "^(0)[1-9](?:[0-9]{8}|(?:\\s[0-9]{3,4}){3})$";
                pattern = Pattern.compile (phoneRegex);

                do {
                    System.out.print("Mobile number: ");
                    phoneNumber = sc.nextLine();
                    Matcher matcher = pattern.matcher(phoneNumber);
                    matchFound = matcher.find();
                    if (matchFound == false) {
                        System.out.println("Invalid AUS mobile number. Must be in format 04xxxxxxxx. Please try again...");
                    }
                } while (matchFound == false);


                System.out.println("user details:");
                System.out.println(userEmail);
                System.out.println(firstName + " " + surname);
                System.out.println(password);
                System.out.println(phoneNumber);



				break;
            case (3):
                exit = true;
                break;
            }

        } while(!exit);
        
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
            if (user.getEmail().equalsIgnoreCase(inputEmail) && user.getPassword().equals(inputPassword)) {
                return true;
            }
        }
        return false;
    }

    private int portalMenu() {

        System.out.println("*".repeat(50));
        System.out.println(name +" Service Portal");
        System.out.println("*".repeat(50));
        System.out.println("");
        System.out.println("[1] Existing User Login");
        System.out.println("[2] New Staff Registration");
        System.out.println("[3] Exit");
        System.out.print("Selection: ");

        return validateSelection(3);

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


}
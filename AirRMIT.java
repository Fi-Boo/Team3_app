import java.util.Scanner;
import java.util.ArrayList;

public class AirRMIT {

    private String name;

    public AirRMIT(String string) {
        this.name = string;
    }

    Scanner sc = new Scanner(System.in);

    private ArrayList<User> users = new ArrayList<User>();

    private User loggedUser;

    boolean exit = false;

    public void run() {

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

                        int staffSelection;

                        do {
                            System.out.println("");
                            System.out.println("*".repeat(50));
                            System.out.println("Hi " + inputEmail);
                            System.out.println("*".repeat(50));
                            System.out.println("");
                            System.out.println("[1] Coming Soon");
                            System.out.println("[2] Log out");

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
                System.out.println("User Registration");
				break;
            case (3):
                exit = true;
                break;
            }

        } while(!exit);
        
    }

    private boolean checkLogin(String inputEmail, String inputPassword) {
        
        if (inputEmail.equals("testEmail") && inputPassword.equals("testPassword")){
            return true;
        }
        else {
            return false;
        }
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
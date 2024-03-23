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
                    do {

                        System.out.print("Email: ");
                        String inputEmail = sc.nextLine();
                        System.out.print("Password: ");
                        String inputPassword = sc.nextLine();

                        validLogin = checkLogin(inputEmail, inputPassword);
                        if (!validLogin) {
                            System.out.println("Incorrect login credentials. Please try again...");
                        }

                    } while (!validLogin);
                    
                    System.out.println("Successful login");



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

        System.out.println(name +" Service Portal");
        System.out.println("");
        System.out.println("1. Existing User Login");
        System.out.println("2. New Staff Registration");
        System.out.println("3. Exit");
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
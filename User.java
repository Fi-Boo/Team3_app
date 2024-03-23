public class User {

    private String email;
    private String firstName;
    private String surname;
    private int phoneNumber;
    private String password;
    private char staffType;

    public User(String email, String firstName, String surname, int phoneNumber, String password) {
        super();
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.staffType = 's';
    }

    public String getEmail() {
        return this.email;
    }

    public String firstName() {
        return this.firstName;
    }

    public String surname() {
        return this.surname;
    }

    public int getNumber() {
        return this.phoneNumber;
    }

    public String getPassword() {
        return this.password;
    }

    public char getStaffType() {
        return this.staffType;
    }

}
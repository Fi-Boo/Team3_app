public class User {

    private String email;
    private String firstName;
    private String surname;
    private int phoneNumber;
    private String password;
    private String staffType;

    public User(String email, String firstName, String surname, int phoneNumber, String password) {
        super();
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.staffType = "s";
    }

    public User(String email, String firstName, String surname, int phoneNumber, String password, String letter) {
        super();
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.staffType = letter;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getSurname() {
        return this.surname;
    }

    public int getNumber() {
        return this.phoneNumber;
    }

    public String getPassword() {
        return this.password;
    }

    public String getStaffType() {
        return this.staffType;
    }

}
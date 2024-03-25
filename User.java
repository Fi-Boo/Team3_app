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

public class User {

    private String email;
    private String fullName;
    private String phoneNumber;
    private String password;
    private String staffType;

    public User(String email, String fullName, String phoneNumber, String password) {
        super();
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.staffType = "s";
    }

    public User(String email, String fullName, String phoneNumber, String password, String letter) {
        super();
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.staffType = letter;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getNumber() {
        return this.phoneNumber;
    }

    public String getPassword() {
        return this.password;
    }

    public String getStaffType() {
        return this.staffType;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
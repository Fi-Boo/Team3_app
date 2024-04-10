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

public class Ticket {

    String description;
    String severity;
    String status;
    String createdBy;
    String assignedTo;
    String openDateTime;
    String closedDateTime;

    public Ticket(String description, String severity, String openDateTime, String createdBy) {
        super();
        this.description = description;
        this.severity = severity;
        this.createdBy = createdBy;
        this.status = "open";
        this.openDateTime = openDateTime;
        this.closedDateTime = null;
    }

    public Ticket(String description, String severity, String openDateTime, String createdBy, String assignedTo,
            String status, String closedDateTime) {

        super();
        this.description = description;
        this.severity = severity;
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
        this.status = status;
        this.openDateTime = openDateTime;
        this.closedDateTime = closedDateTime;

    }

    public String getDescription() {
        return this.description;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getAssignedTo() {
        return this.assignedTo;
    }

    public String getSeverity() {
        return this.severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpenDateTime() {
        return this.openDateTime;
    }

    public String getClosedDateTime() {
        return this.closedDateTime;
    }

    public void setClosedDateTime(String closedDateTime) {
        this.closedDateTime = closedDateTime;
    }

    public void displayDetails(User loggedUser) {

        System.out.printf("\n%-16s: %s", "Open Date/Time", this.openDateTime);
        System.out.printf("\n%-16s: %s", "Severity", this.severity);
        System.out.printf("\n%-16s: %s", "Description", this.description);

        if (loggedUser.getStaffType().equalsIgnoreCase("s")) {

            System.out.printf("\n%-16s: %s\n", "Assigned To", this.assignedTo);

        } else {

            System.out.printf("\n%-16s: %s\n", "Created By", this.createdBy);

            if (!this.status.substring(0, 1).equalsIgnoreCase("o")) {
                System.out.printf("%-16s: %s\n", "Status", this.status);
                System.out.printf("%-16s: %s\n", "Closed Date/Time", this.closedDateTime);
            }
        }
    }

    @Override
    public String toString() {

        String separator = "\t";

        return this.description + separator +
                this.severity + separator +
                this.openDateTime + separator +
                this.createdBy + separator +
                this.assignedTo + separator +
                this.status + separator +
                this.closedDateTime + "\n";
    }

}
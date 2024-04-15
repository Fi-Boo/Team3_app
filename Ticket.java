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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ticket {

    String description;
    String severity;
    String status;
    String createdBy;
    String assignedTo;
    String openDateTime;
    String closedDateTime;
    String closedBy;

    public Ticket(String description, String severity, String openDateTime, String createdBy) {
        super();
        this.description = description;
        this.severity = severity;
        this.createdBy = createdBy;
        this.status = "open";
        this.openDateTime = openDateTime;
        this.closedDateTime = null;
        this.closedBy = null;
    }

    public Ticket(String description, String severity, String openDateTime, String createdBy, String assignedTo,
            String status, String closedDateTime, String closedBy) {

        super();
        this.description = description;
        this.severity = severity;
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
        this.status = status;
        this.openDateTime = openDateTime;
        this.closedDateTime = closedDateTime;
        this.closedBy = closedBy;

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

    public String getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }

    public void displayDetails(User loggedUser) {

        System.out.printf("\n%-16s: %s", "Open Date/Time", this.openDateTime);
        System.out.printf("\n%-16s: %s", "Severity", this.severity);
        System.out.printf("\n%-16s: %s", "Description", this.description);

        if (loggedUser.getStaffType().substring(0, 1).equalsIgnoreCase("s")) {

            System.out.printf("\n%-16s: %s\n", "Assigned To", this.assignedTo);

        } else {

            System.out.printf("\n%-16s: %s\n", "Created By", this.createdBy);

            if (!this.status.substring(0, 1).equalsIgnoreCase("o")) {
                System.out.printf("%-16s: %s\n", "Status", this.status);
                System.out.printf("%-16s: %s\n", "Closed Date/Time", this.closedDateTime);
                System.out.printf("%-16s: %s\n", "Closed By", this.closedBy);

                if (loggedUser.getStaffType().equalsIgnoreCase("o")) {
                    System.out.printf("%-16s: %s\n", "Open Duration",
                            calculateOpenDuration(this.openDateTime, this.closedDateTime));
                }
            }
        }
    }

    private Object calculateOpenDuration(String openDateTime2, String closedDateTime2) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");

        LocalDateTime openDT = LocalDateTime.parse(openDateTime2, formatter);
        LocalDateTime closedDT = LocalDateTime.parse(closedDateTime2, formatter);

        Duration duration = Duration.between(openDT, closedDT);

        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        StringBuilder sb = new StringBuilder();

        if (days > 0) {
            sb.append(days + " days, ");
        }
        if (hours > 0) {
            sb.append(hours + " hours, ");
        }
        if (minutes > 0) {
            sb.append(minutes + " minutes, ");
        }
        if (seconds > 0) {
            sb.append(seconds + " seconds.");
        }

        return sb.toString();

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
                this.closedDateTime + separator +
                this.closedBy + "\n";
    }

}
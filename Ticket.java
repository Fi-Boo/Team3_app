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

    public String getDescription() {
        return this.description;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setAssignedTo(String email) {
        this.assignedTo = email;
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



}
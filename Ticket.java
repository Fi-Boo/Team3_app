public class Ticket {

    String description;
    String severity;
    String status;
    String createdBy;
    String assignedTo;
    String ClosureDateTime;
    
    public Ticket(String description, String severity, String createdBy, String assignedTo) {
        super();
        this.description = description;
        this.severity = severity;
        this.createdBy = createdBy;
        this.status = "open";
        this.assignedTo = assignedTo;
        this.ClosureDateTime = null;
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

}
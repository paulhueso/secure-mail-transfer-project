package model;

public class Mail {
    private int id;
    private boolean seen;
    private String from;
    private String subject;
    private String sentDate;
    private String message;
    private String attachments;

    public Mail(int id, boolean seen, String from, String subject, String sentDate, String message, String attachments) {
        this.id = id;
        this.seen = seen;
        this.from = from;
        this.subject = subject;
        this.sentDate = sentDate;
        this.message = message;
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "\nseen=" + seen +
                "\n, from='" + from + '\'' +
                "\n, subject='" + subject + '\'' +
                "\n, sentDate='" + sentDate + '\'' +
                "\n, message='" + message + '\'' +
                "\n}";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }
}

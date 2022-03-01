package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Mail {
    private int id;
    private boolean seen;
    private StringProperty from;
    private StringProperty subject;
    private StringProperty sentDate;
    private StringProperty message;
    private StringProperty attachments;

    public Mail(int id, boolean seen, String from, String subject, String sentDate, String message, String attachments) {
        this.id = id;
        this.seen = seen;
        this.from = new SimpleStringProperty(from);
        this.subject = new SimpleStringProperty(subject);
        this.sentDate = new SimpleStringProperty(sentDate);
        this.message = new SimpleStringProperty(message);
        this.attachments = new SimpleStringProperty(attachments);
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

    public StringProperty getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = new SimpleStringProperty(from);
    }

    public StringProperty getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = new SimpleStringProperty(subject);
    }

    public StringProperty getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = new SimpleStringProperty(sentDate);
    }

    public StringProperty getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = new SimpleStringProperty(message);
    }

    public StringProperty getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = new SimpleStringProperty(attachments);
    }
}

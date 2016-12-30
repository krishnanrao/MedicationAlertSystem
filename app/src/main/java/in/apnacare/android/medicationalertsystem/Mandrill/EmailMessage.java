package in.apnacare.android.medicationalertsystem.Mandrill;

import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 * Created by dell on 16-11-2016.
 */

public class EmailMessage {

    private String html;
    private String text;
    private String subject;
    @SerializedName("from_email")
    private String fromEmail;
    @SerializedName("from_name")
    private String fromName;
    private List<Recipient> to;
    private List<Attachment> images;
    private List<Attachment> attachments;

    public EmailMessage(){

    }


    public void setImages(List<Attachment> image) {
        this.images = image;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }


    public String getText() {
        return text;
    }
    public String setHtml(String html) {
        return this.html = html;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String from_email) {
        this.fromEmail = from_email;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String from_name) {
        this.fromName = from_name;
    }

    public List<Recipient> getTo() {
        return to;
    }

    public void setTo(List<Recipient> to) {
        this.to = to;
    }
}

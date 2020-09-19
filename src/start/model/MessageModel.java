package start.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Message;
import javax.mail.internet.MimeBodyPart;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

//Bean-type class for holding message info
public class MessageModel {

	// javax.mail works with this stuff
	private SimpleStringProperty subject;
	private SimpleStringProperty sender;
	private SimpleStringProperty recipient;
	private SimpleObjectProperty<SizeIntegerModel> size;
	private SimpleObjectProperty<Date> date;
	private List<MimeBodyPart> attachmentList = new ArrayList<MimeBodyPart>();
	private boolean attachment = false;

	private boolean read;
	private Message message; // original message, possibly better to extend?

	public MessageModel(String subject, String sender, String recipient, int size, Date date, boolean read,
			Message message) {

		this.subject = new SimpleStringProperty(subject);
		this.sender = new SimpleStringProperty(sender);
		this.recipient = new SimpleStringProperty(recipient);
		this.size = new SimpleObjectProperty<SizeIntegerModel>(new SizeIntegerModel(size));
		this.date = new SimpleObjectProperty<Date>(date);

		this.read = read;
		this.message = message;
	}

	public void addAttachment(MimeBodyPart mbp) {

		setAttachment(true);
		attachmentList.add(mbp);

	}

	public boolean hasAttachment() {
		return attachment;
	}

	public void setAttachment(boolean attachment) {
		this.attachment = attachment;
	}

	public List<MimeBodyPart> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<MimeBodyPart> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public String getSubject() {
		return subject.get();
	}

	public String getSender() {
		return sender.get();
	}

	public String getRecipient() {
		return recipient.get();
	}

	public SizeIntegerModel getSize() {
		return size.get();
	}

	public Date getDate() {
		return date.get();
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean b) {
		read = b;
	}

	public Message getMessage() {
		return message;
	}

}

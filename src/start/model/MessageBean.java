package start.model;

import java.util.Date;

import javax.mail.Message;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class MessageBean {

	private SimpleStringProperty subject;
	private SimpleStringProperty sender;
	private SimpleStringProperty reciever;
	private SimpleIntegerProperty size;
	private SimpleObjectProperty<Date> date;
	private boolean read;
	private Message message;
	
	public MessageBean(String subject, String sender, String reciever, int size, Date date, boolean read, Message message) {
		this.subject = new SimpleStringProperty(subject);
		this.sender = new SimpleStringProperty(sender);
		this.reciever = new SimpleStringProperty(reciever);
		this.size = new SimpleIntegerProperty(size);
		this.date = new SimpleObjectProperty<Date>(date);
		this.read = read;
		this.message = message;
	}
	
	
	public String getSubject() {
		return subject.get();
	}

	public String getSender() {
		return sender.get();
	}

	public String getReciever() {
		return reciever.getName();
	}

	public Integer getSize() {
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

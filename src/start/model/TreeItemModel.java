package start.model;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

//Custom TreeItem behavior for left-most panel
public class TreeItemModel<String> extends TreeItem<String> {

	private String name;
	private ObservableList<MessageModel> messages;
	private int unreadCount;

	public TreeItemModel(String name) {
		super(name);
		this.name = name;
		this.messages = FXCollections.observableArrayList();
	}

	public ObservableList<MessageModel> getMessages() {
		return messages;
	}

	// Adds incoming message to message (Observable)list
	public void addEmail(Message message) throws MessagingException {

		messages.add(fetchMessage(message));
	}

	// Adds incoming message to top of message (Observable)list
	public void addEmailToTop(Message message) throws MessagingException {

		messages.add(0, fetchMessage(message));
	}

	public void incrementCount() {
		unreadCount++;
		updateName();
	}

	public void decrementCount() {
		unreadCount--;
		updateName();
	}

	// Appends unread email to end of TreeItem(folder) name
	private void updateName() {
		if (unreadCount > 0) {

			this.setValue((String) (name + " ( " + unreadCount + " ) "));

		} else {
			this.setValue(name);
		}
	}

	// Processes standard messages into MessageBeans and updates unread marker
	private MessageModel fetchMessage(Message message) throws MessagingException {
		// Determines if read
		boolean read = message.getFlags().contains(Flags.Flag.SEEN);

		// Message - MessageBean conversion
		MessageModel bean = new MessageModel(message.getSubject(), message.getFrom()[0].toString(),
				message.getRecipients(RecipientType.TO)[0].toString(), message.getSize(),
				message.getSentDate(), read, message);

		// Updates unread marker if unread
		if (!read) {
			incrementCount();
		}
		return bean;
	}

}

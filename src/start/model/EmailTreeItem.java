package start.model;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class EmailTreeItem<String> extends TreeItem<String> {

	private String name;
	private ObservableList<MessageBean> messages;
	private int unreadCount;

	public EmailTreeItem(String name) {
		super(name);
		this.name = name;
		this.messages = FXCollections.observableArrayList();
	}

	public void addEmail(Message message) throws MessagingException {

		boolean read = message.getFlags().contains(Flags.Flag.SEEN);

		MessageBean bean = new MessageBean(message.getSubject(), message.getFrom()[0].toString(),
				message.getRecipients(MimeMessage.RecipientType.TO)[0].toString(), message.getSize(),
				message.getSentDate(), read, message);
		messages.add(bean);
		if (!read) {
			unreadCount++;
			updateCount();
		}
	}
	
	

	private void updateCount() {
		if (unreadCount > 0) {
			this.setValue((String) (name + " ( " + unreadCount + " ) "));
			
		}else {
			this.setValue(name);
		}
	}

}

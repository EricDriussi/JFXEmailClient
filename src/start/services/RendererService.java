package start.services;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import start.model.MessageModel;

//Renders the email into web view panel
public class RendererService extends Service<Object> {

	private MessageModel myMessage;
	private WebEngine engine;
	private StringBuffer buffer;

	public RendererService(WebEngine engine) {
		super();
		this.engine = engine;
		this.buffer = new StringBuffer();

		// Only display once it's loaded/buffered
		this.setOnSucceeded(e -> {
			displayMessage();
		});
	}

	public void setMessage(MessageModel myMessage) {
		this.myMessage = myMessage;
	}

	@Override
	protected Task<Object> createTask() {
		return new Task<Object>() {
			@Override
			protected Object call() throws Exception {
				try {
					// Encapsulate the world
					loadMessage();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		};
	}

	// Actual code
	private void loadMessage() throws MessagingException, IOException {

		buffer.setLength(0);// Empty buffer beforehand

		Message message = myMessage.getMessage();
		String contentType = message.getContentType();

		// SimpleType can be buffered directly
		if (isSimpleType(contentType)) {

			buffer.append(message.getContent().toString());

			// MultipartType need to be separated
		} else if (isMultipartType(contentType)) {

			Multipart part = (Multipart) message.getContent();

			loadMultipart(part, buffer);
		}

	}

	// Recursive method for multiparts of multiparts...
	private void loadMultipart(Multipart multipart, StringBuffer buffer) throws MessagingException, IOException {

		for (int i = multipart.getCount() - 1; i >= 0; i--) {

			BodyPart body = multipart.getBodyPart(i);
			String otherContentType = body.getContentType();

			if (isSimpleType(otherContentType)) {

				buffer.append(body.getContent().toString());
			} else if (isMultipartType(body.getContentType())) {

				// Recursion
				Multipart part = (Multipart) body.getContent();
				loadMultipart(part, buffer);

			} else if (!isPlainText(otherContentType)) {

				//Ads attachment to email
				MimeBodyPart mbp = (MimeBodyPart) body;
				myMessage.addAttachment(mbp);
			}
		}
	}

	// Sends buffer to engine for display
	private void displayMessage() {
		engine.loadContent(buffer.toString());
	}

	// Checks if SimpleType
	private boolean isSimpleType(String contentType) {
		if (contentType.contains("TEXT/HTML") || contentType.contains("mixed") || contentType.contains("text")) {
			return true;
		} else {
			return false;
		}
	}

	// Checks if Multitype
	private boolean isMultipartType(String contentType) {
		if (contentType.contains("multipart")) {
			return true;
		} else {
			return false;
		}
	}

	// Checks if attachment
	private boolean isPlainText(String contenType) {
		return contenType.contains("TEXT/PLAIN");
	}

}

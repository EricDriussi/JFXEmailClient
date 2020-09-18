package start.services;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import start.model.MessageModel;

//Renders the email into web view panel
public class RendererService extends Service<Object> {

	private MessageModel bean;
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

	public void setMessage(MessageModel bean) {
		this.bean = bean;
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

		Message message = bean.getMessage();
		String contentType = message.getContentType();

		// SimpleType can be buffered directly
		if (isSimpleType(contentType)) {

			buffer.append(message.getContent().toString());

			// MultipartType need to be separated
		} else if (isMultipartType(contentType)) {

			Multipart part = (Multipart) message.getContent();

			for (int i = part.getCount() - 1; i >= 0; i--) {

				BodyPart body = part.getBodyPart(i);
				String otherContentType = body.getContentType();

				if (isSimpleType(otherContentType)) { //Probably should be recursive, can't be bothered

					buffer.append(body.getContent().toString());
				}
			}
		}

	}

	// Sends buffer to engine for display
	private void displayMessage() {
		engine.loadContent(buffer.toString());
	}

	//Checks if SimpleType
	private boolean isSimpleType(String contentType) {
		if (contentType.contains("TEXT/HTML") || contentType.contains("mixed") || contentType.contains("text")) {
			return true;
		} else {
			return false;
		}
	}

	//Checks if Multitype
	private boolean isMultipartType(String contentType) {
		if (contentType.contains("multipart")) {
			return true;
		} else {
			return false;
		}
	}

}

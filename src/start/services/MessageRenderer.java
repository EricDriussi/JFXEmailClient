package start.services;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import start.model.MessageBean;

public class MessageRenderer extends Service {

	private MessageBean bean;
	private WebEngine engine;
	private StringBuffer buffer;

	public MessageRenderer(WebEngine engine) {
		super();
		this.engine = engine;
		this.buffer = new StringBuffer();
		this.setOnSucceeded(e->{
			displayMessage();
		});
	}

	public void setMessage(MessageBean bean) {
		this.bean = bean;
	}
	
	private void displayMessage() {
		engine.loadContent(buffer.toString());
	}

	@Override
	protected Task createTask() {
		return new Task() {

			@Override
			protected Object call() throws Exception {
				try {
					loadMessage();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		};
	}

	private void loadMessage() throws MessagingException, IOException {
		buffer.setLength(0);
		Message message = bean.getMessage();
		String contentType = message.getContentType();
		if (isSimpleType(contentType)) {
			buffer.append(message.getContent().toString());
		} else if (isMultipartType(contentType)) {
			
			Multipart part = (Multipart) message.getContent();
			for (int i = part.getCount() -1; i >= 0; i--) {
				BodyPart body = part.getBodyPart(i);
				String otherContentType = body.getContentType();
				if (isSimpleType(otherContentType)) {
					buffer.append(body.getContent().toString());
				}
			}
		}

	}

	private boolean isSimpleType(String contentType) {
		if (contentType.contains("TEXT/HTML") || contentType.contains("mixed") || contentType.contains("text")) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isMultipartType(String contentType) {
		if (contentType.contains("multipart")) {
			return true;
		} else {
			return false;
		}
	}

}

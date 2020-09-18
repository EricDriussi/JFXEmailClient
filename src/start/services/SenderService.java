package start.services;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


import javafx.concurrent.Service;
import javafx.concurrent.Task;
import start.model.EmailAccountModel;

//Packs and sends the email
public class SenderService extends Service<SendingResult> {

	private EmailAccountModel emailAccount;
	private String subject;
	private String recipient;
	private String content;

	public SenderService(EmailAccountModel emailAccount, String subject, String recipient, String content) {
		super();
		this.emailAccount = emailAccount;
		this.subject = subject;
		this.recipient = recipient;
		this.content = content;
	}

	@Override
	protected Task<SendingResult> createTask() {
		return new Task<SendingResult>() {

			@Override
			protected SendingResult call() throws Exception {
				try {

					// Creates the message
					MimeMessage mimeMessage = new MimeMessage(emailAccount.getSession());
					mimeMessage.setFrom(emailAccount.getAccount());
					mimeMessage.addRecipients(Message.RecipientType.TO, recipient);
					mimeMessage.setSubject(subject);

					// Set the content
					Multipart multi = new MimeMultipart();
					BodyPart bodyPart = new MimeBodyPart();
					bodyPart.setContent(content, "text/html");
					multi.addBodyPart(bodyPart);

					mimeMessage.setContent(multi);

					// Sends the message
					Transport transport = emailAccount.getSession().getTransport();
					transport.connect(emailAccount.getProperties().getProperty("outgoingHost"),
							emailAccount.getAccount(), emailAccount.getPassword());

					transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
					transport.close();
					
					return SendingResult.SUCCESS;
				} catch (MessagingException e) {
					e.printStackTrace();

					return SendingResult.FAILED_BY_PROVIDER;

				} catch (Exception e) {
					e.printStackTrace();
					return SendingResult.FAILED_BY_UNEXPECTED_ERROR;

				}

			}

		};
	}

}
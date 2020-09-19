package start.services;

import java.io.File;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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
	private List<File> attachments;

	public SenderService(EmailAccountModel emailAccount, String subject, String recipient, String content,
			List<File> attachments) {
		super();
		this.emailAccount = emailAccount;
		this.subject = subject;
		this.recipient = recipient;
		this.content = content;
		this.attachments = attachments;
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

					// Attachments
					if (attachments.size() > 0) {
						for (File file : attachments) {
							System.out.println(file.getAbsolutePath());

							MimeBodyPart part = new MimeBodyPart();
							DataSource source = new FileDataSource(file.getAbsolutePath());
							part.setDataHandler(new DataHandler(source));
							part.setFileName(file.getName());
							multi.addBodyPart(part);
						}
					}

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

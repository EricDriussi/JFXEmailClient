package start.services;

import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import start.EmailManager;
import start.model.EmailAccount;

public class LoginService extends Service<LoginResult> {

	EmailAccount account;
	EmailManager manager;

	public LoginService(EmailAccount account, EmailManager manager) {
		super();
		this.account = account;
		this.manager = manager;
	}

	private LoginResult login() {
		Authenticator authenticator = new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication(account.getAccount(), account.getPassword());
			}

		};

		try {
			Session session = Session.getInstance(account.getProperties(), authenticator);
			Store store = session.getStore("imaps");

			store.connect(account.getProperties().getProperty("incomingHost"), account.getAccount(),
					account.getPassword());

			account.setStore(store);
			manager.addEmailAccount(account);

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			return LoginResult.FAILED_BY_NETWORK;

		} catch (AuthenticationFailedException e) {
			e.printStackTrace();
			return LoginResult.FAILED_BY_CRED;

		} catch (MessagingException e) {
			e.printStackTrace();
			return LoginResult.FAILED_BY_UNEXPECTED_ERROR;

		} catch (Exception e) {
			e.printStackTrace();
			return LoginResult.FAILED_BY_UNEXPECTED_ERROR;

		}

		return LoginResult.SUCCESS;
	}

	@Override
	protected Task<LoginResult> createTask() {

		return new Task<LoginResult>() {

			@Override
			protected LoginResult call() throws Exception {

				return login();
			}

		};
	}

}

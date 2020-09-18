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
import start.model.EmailAccountModel;

//Login logic
public class LoginService extends Service<LoginResult> {

	EmailAccountModel account;
	EmailManager manager;

	public LoginService(EmailAccountModel account, EmailManager manager) {
		super();
		this.account = account;
		this.manager = manager;
	}

	//Needed to extend javafx.concurrent.service - multithreading
	@Override
	protected Task<LoginResult> createTask() {

		return new Task<LoginResult>() {

			@Override
			protected LoginResult call() throws Exception {
				
				return login();
			}

		};
	}

	//Actual logic
	private LoginResult login() {
		Authenticator authenticator = new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(account.getAccount(), account.getPassword());//Standard authentication
			}

		};

		try {
			
			//Custom authentication and failure management
			Session session = Session.getInstance(account.getProperties(), authenticator);
			
			account.setSession(session);
			
			Store store = session.getStore("imaps");

			store.connect(account.getProperties().getProperty("incomingHost"), account.getAccount(),
					account.getPassword());

			account.setStore(store);
			manager.addEmailAccount(account);//Multi-account support

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

}

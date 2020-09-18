package start.model;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Store;

//Bean-type class for holding account info
public class EmailAccountModel {

	private String account;
	private String password;
	private Properties properties;
	private Store store;
	private Session session;

	public EmailAccountModel(String account, String password) {
		super();
		this.account = account;
		this.password = password;

		// Copied IMAP/SMTPS stuff
		properties = new Properties();
		properties.put("incomingHost", "imap.gmail.com");
		properties.put("mail.store.protocol", "imaps");

		properties.put("mail.transport.protocol", "smtps");
		properties.put("mail.smtps.host", "smtp.gmail.com");
		properties.put("mail.smtps.auth", "true");
		properties.put("outgoingHost", "smtp.gmail.com");
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getAccount() {
		return account;
	}

	public String getPassword() {
		return password;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	@Override
	public String toString() {
		return account;
	}

}

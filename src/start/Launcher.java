package start;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import start.controller.persistence.Access;
import start.controller.persistence.ValidAccount;
import start.model.EmailAccountModel;
import start.services.LoginService;
import start.view.ViewManager;

public class Launcher extends Application {

	private Access access = new Access();
	private EmailManager emailManager = new EmailManager();
	private ViewManager manager = new ViewManager(emailManager);

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void stop() throws Exception {
		super.stop();

		List<ValidAccount> accountsList = new ArrayList<ValidAccount>();
		for (EmailAccountModel emailAccount : emailManager.getEmailAccounts()) {
			accountsList.add(new ValidAccount(emailAccount.getAccount(), emailAccount.getPassword()));
		}

		// Persists current accounts
		access.save(accountsList);
	}

	@Override
	public void start(Stage stage) throws Exception {
		checkPersistence();

	}

	private void checkPersistence() {

		// Loads persisted accounts
		List<ValidAccount> accountsList = access.load();

		if (accountsList.size() > 0) {

			for (ValidAccount validAccount : accountsList) {
				EmailAccountModel account = new EmailAccountModel(validAccount.getAddress(),
						validAccount.getPassword());
				LoginService service = new LoginService(account, emailManager);
				service.start();
			}
			manager.showMain();

		} else {
			manager.showLogin();

		}

	}
}

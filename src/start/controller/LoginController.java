
package start.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.LoginResult;
import services.LoginService;
import start.EmailManager;
import start.model.EmailAccount;
import start.view.ViewManager;

public class LoginController extends BaseController {

	public LoginController(EmailManager emailManager, ViewManager viewManager, String fxmlName) {
		super(emailManager, viewManager, fxmlName);
		// TODO Auto-generated constructor stub
	}

	@FXML
	private Button loginButton;

	@FXML
	private TextField emailField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private Label errorLabel;

	@FXML
	void buttonAction() {

		if (fieldsAreValid()) {
			EmailAccount account = new EmailAccount(emailField.getText(), passwordField.getText());

			LoginService service = new LoginService(account, emailManager);

			LoginResult result = service.login();

			switch (result) {
			case SUCCESS:
				System.out.println("Login: " + account);
				viewManager.showMain();
				Stage stage = (Stage) loginButton.getScene().getWindow();
				viewManager.closeStage(stage);
				return;
			}
		}


	}

	private boolean fieldsAreValid() {
		if (emailField.getText().isEmpty()) {
			errorLabel.setText("Empty email address!");
			return false;
		}
		if (passwordField.getText().isEmpty()) {
			errorLabel.setText("Empty password field!");
			return false;
		}
		return true;
	}

}

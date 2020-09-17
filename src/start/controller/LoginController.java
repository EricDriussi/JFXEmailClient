
package start.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import start.EmailManager;
import start.model.EmailAccount;
import start.services.LoginResult;
import start.services.LoginService;
import start.view.ViewManager;

public class LoginController extends BaseController implements Initializable {

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

			service.start();
			service.setOnSucceeded(e -> {
				LoginResult result = service.getValue();

				switch (result) {
				case SUCCESS:
					System.out.println("Login: " + account);
					
					if (!viewManager.isMainInit()) {
						viewManager.showMain();
					}
					
					Stage stage = (Stage) loginButton.getScene().getWindow();
					viewManager.closeStage(stage);
					return;
				case FAILED_BY_CRED:
					errorLabel.setText("Creds are wrong!");

					return;
				case FAILED_BY_UNEXPECTED_ERROR:
					errorLabel.setText("Something went wrong!");

					return;
				case FAILED_BY_NETWORK:
					errorLabel.setText("NetWork failed!");

					return;
				default:
					return;
				}
			});

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		emailField.setText("testmebby267@gmail.com");
		passwordField.setText("I3WAH%6&r9");

	}

}

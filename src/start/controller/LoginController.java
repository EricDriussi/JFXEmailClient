
package start.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import start.EmailManager;
import start.view.ViewManager;

public class LoginController extends BaseController{

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
    	viewManager.showMain();
    	Stage stage = (Stage) loginButton.getScene().getWindow();
    	viewManager.closeStage(stage);
    }

}

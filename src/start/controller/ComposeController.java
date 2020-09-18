package start.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.stage.*;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import start.EmailManager;
import start.model.EmailAccountModel;
import start.services.SenderService;
import start.services.SendingResult;
import start.view.ViewManager;

public class ComposeController extends BaseController implements Initializable {

	public ComposeController(EmailManager emailManager, ViewManager viewManager, String fxmlName) {
		super(emailManager, viewManager, fxmlName);
	}

	@FXML
	private TextField recipientTextField;

	@FXML
	private TextField subjectTextField;

	@FXML
	private HTMLEditor htmlEditor;

	@FXML
	private Label errorLabel;

	@FXML
	private ChoiceBox<EmailAccountModel> accountChoice;

	@FXML
	void sendAction() {

		SenderService sender = new SenderService(accountChoice.getValue(), subjectTextField.getText(),
				recipientTextField.getText(), htmlEditor.getHtmlText());
		sender.start();
		sender.setOnSucceeded(e -> {
			SendingResult result = sender.getValue();

			switch (result) {
			case SUCCESS:
				Stage stage = (Stage) recipientTextField.getScene().getWindow();
				viewManager.closeStage(stage);

				break;
			case FAILED_BY_PROVIDER:
				errorLabel.setText("Provider error");

				break;
			case FAILED_BY_UNEXPECTED_ERROR:
				errorLabel.setText("Unexpected error");

				break;
			default:
				break;
			}
		});
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		accountChoice.setItems(emailManager.getEmailAccounts());
		accountChoice.setValue(emailManager.getEmailAccounts().get(0));

	}

}

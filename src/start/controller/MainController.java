package start.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebView;
import start.EmailManager;
import start.view.ViewManager;

public class MainController extends BaseController implements Initializable {

	public MainController(EmailManager emailManager, ViewManager viewManager, String fxmlName) {
		super(emailManager, viewManager, fxmlName);
		// TODO Auto-generated constructor stub
	}

	@FXML
	private TreeView<String> emailsTreeView;

	@FXML
	private TableView<?> emailsTableView;

	@FXML
	private WebView emailsWebView;

	@FXML
	void optionsAction() {

		viewManager.showOptions();

	}

	@FXML
	void addAccountAction() {
		
		viewManager.showLogin();

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setUpTree();
		
	}

	private void setUpTree() {
		emailsTreeView.setRoot(emailManager.getFoldersRoot());
		emailsTreeView.setShowRoot(false);
		
	}

}

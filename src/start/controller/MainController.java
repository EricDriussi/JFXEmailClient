package start.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebView;
import start.EmailManager;
import start.view.ViewManager;

public class MainController extends BaseController{

    public MainController(EmailManager emailManager, ViewManager viewManager, String fxmlName) {
		super(emailManager, viewManager, fxmlName);
		// TODO Auto-generated constructor stub
	}

	@FXML
    private TreeView<?> emailsTreeView;

    @FXML
    private TableView<?> emailsTableView;

    @FXML
    private WebView emailsWebView;

    @FXML
    void optionsAction() {

    }

}

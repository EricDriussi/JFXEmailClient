package start.controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import start.EmailManager;
import start.model.EmailTreeItem;
import start.model.MessageBean;
import start.model.SizeInteger;
import start.services.MessageRenderer;
import start.view.ViewManager;

public class MainController extends BaseController implements Initializable {

	public MainController(EmailManager emailManager, ViewManager viewManager, String fxmlName) {
		super(emailManager, viewManager, fxmlName);
		// TODO Auto-generated constructor stub
	}

	@FXML
	private TreeView<String> emailsTreeView;

	@FXML
	private TableView<MessageBean> emailsTableView;

	@FXML
	private WebView emailsWebView;

	private MessageRenderer renderer;

	@FXML
	private TableColumn<MessageBean, String> senderCol;

	@FXML
	private TableColumn<MessageBean, String> subjectCol;

	@FXML
	private TableColumn<MessageBean, String> recipientCol;

	@FXML
	private TableColumn<MessageBean, SizeInteger> sizeCol;

	@FXML
	private TableColumn<MessageBean, Date> dateCol;

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
		setUpTableView();
		setUpFolderSelection();
		setUpBoldRows();
		setUpRenderer();
		setUpMessageSelection();
	}

	private void setUpMessageSelection() {

		emailsTableView.setOnMouseClicked(e -> {
			MessageBean bean = emailsTableView.getSelectionModel().getSelectedItem();
			if (bean != null) {
				renderer.setMessage(bean);
				renderer.restart();

			}
		});

	}

	private void setUpRenderer() {
		renderer = new MessageRenderer(emailsWebView.getEngine());

	}

	private void setUpBoldRows() {

		emailsTableView.setRowFactory(new Callback<TableView<MessageBean>, TableRow<MessageBean>>() {

			@Override
			public TableRow<MessageBean> call(TableView<MessageBean> arg0) {
				return new TableRow<MessageBean>() {

					@Override
					protected void updateItem(MessageBean item, boolean empty) {
						super.updateItem(item, empty);

						if (item != null) {
							if (item.isRead()) {
								setStyle("");
							} else {
								setStyle("-fx-font-weight: bold");
							}
						}
					}

				};
			}
		});

	}

	private void setUpFolderSelection() {
		emailsTreeView.setOnMouseClicked(e -> {
			EmailTreeItem<String> item = (EmailTreeItem<String>) emailsTreeView.getSelectionModel().getSelectedItem();
			if (item != null) {
				emailsTableView.setItems(item.getMessages());
			}
		});

	}

	private void setUpTableView() {
		senderCol.setCellValueFactory(new PropertyValueFactory<MessageBean, String>("sender"));
		subjectCol.setCellValueFactory(new PropertyValueFactory<MessageBean, String>("subject"));
		recipientCol.setCellValueFactory(new PropertyValueFactory<MessageBean, String>("recipient"));
		sizeCol.setCellValueFactory(new PropertyValueFactory<MessageBean, SizeInteger>("size"));
		dateCol.setCellValueFactory(new PropertyValueFactory<MessageBean, Date>("date"));

	}

	private void setUpTree() {
		emailsTreeView.setRoot(emailManager.getFoldersRoot());
		emailsTreeView.setShowRoot(false);

	}

}

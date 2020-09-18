package start.controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
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

	// Context menu items
	private MenuItem markUnread = new MenuItem("Mark as unread");
	private MenuItem deleteMessage = new MenuItem("Delete message");

	public MainController(EmailManager emailManager, ViewManager viewManager, String fxmlName) {
		super(emailManager, viewManager, fxmlName);
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
		setUpContextMenu();
	}

	//Left-most panel
	private void setUpTree() {
		emailsTreeView.setRoot(emailManager.getFoldersRoot());
		emailsTreeView.setShowRoot(false);

	}

	//Upper panel: gets properties from MessageBean
	private void setUpTableView() {
		senderCol.setCellValueFactory(new PropertyValueFactory<MessageBean, String>("sender"));
		subjectCol.setCellValueFactory(new PropertyValueFactory<MessageBean, String>("subject"));
		recipientCol.setCellValueFactory(new PropertyValueFactory<MessageBean, String>("recipient"));
		sizeCol.setCellValueFactory(new PropertyValueFactory<MessageBean, SizeInteger>("size"));
		dateCol.setCellValueFactory(new PropertyValueFactory<MessageBean, Date>("date"));

		emailsTableView.setContextMenu(new ContextMenu(markUnread, deleteMessage));

	}

	//Modifies table view depending on selected folder on left panel
	private void setUpFolderSelection() {
		emailsTreeView.setOnMouseClicked(e -> {
			EmailTreeItem<String> item = (EmailTreeItem<String>) emailsTreeView.getSelectionModel().getSelectedItem();
			if (item != null) {
				emailManager.setSelectedFolder(item);
				emailsTableView.setItems(item.getMessages());
			}
		});

	}

	//Fonts are a pain in the ass
	private void setUpBoldRows() {

		emailsTableView.setRowFactory(new Callback<TableView<MessageBean>, TableRow<MessageBean>>() {

			@Override
			public TableRow<MessageBean> call(TableView<MessageBean> arg0) {
				return new TableRow<MessageBean>() {

					@Override
					protected void updateItem(MessageBean item, boolean empty) {
						super.updateItem(item, empty);

						//Actual logic...
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

	//Sets up renderer based on viewing panel (bottom)
	private void setUpRenderer() {
		renderer = new MessageRenderer(emailsWebView.getEngine());

	}

	//Tells renderer which message to render (table view -> web view)
	private void setUpMessageSelection() {

		emailsTableView.setOnMouseClicked(e -> {
			MessageBean bean = emailsTableView.getSelectionModel().getSelectedItem();
			if (bean != null) {
				emailManager.setSelectedMessage(bean);

				if (!bean.isRead()) {
					emailManager.setRead(true);
				}

				renderer.setMessage(bean);
				renderer.restart();

			}
		});

	}

	private void setUpContextMenu() {

		markUnread.setOnAction(e -> {
			emailManager.setRead(false);
		});

		deleteMessage.setOnAction(e -> {
			emailManager.deleteSelectedMessage();
			emailsWebView.getEngine().loadContent("");
		});
	}

}

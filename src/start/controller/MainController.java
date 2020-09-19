package start.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import start.EmailManager;
import start.model.TreeItemModel;
import start.model.MessageModel;
import start.model.SizeIntegerModel;
import start.services.RendererService;
import start.view.ViewManager;

public class MainController extends BaseController implements Initializable {

	// Context menu items
	private MenuItem markUnread = new MenuItem("Mark as unread");
	private MenuItem deleteMessage = new MenuItem("Delete message");
	private String DOWNLOAD_LOC = System.getProperty("user.home") + "/Downloads/";

	public MainController(EmailManager emailManager, ViewManager viewManager, String fxmlName) {
		super(emailManager, viewManager, fxmlName);
	}

	@FXML
	private TreeView<String> emailsTreeView;

	@FXML
	private TableView<MessageModel> emailsTableView;

	@FXML
	private WebView emailsWebView;

	private RendererService renderer;

	@FXML
	private TableColumn<MessageModel, String> senderCol;

	@FXML
	private TableColumn<MessageModel, String> subjectCol;

	@FXML
	private TableColumn<MessageModel, String> recipientCol;

	@FXML
	private TableColumn<MessageModel, SizeIntegerModel> sizeCol;

	@FXML
	private TableColumn<MessageModel, Date> dateCol;

	@FXML
	private Menu attachmentsMenu;

	@FXML
	void optionsAction() {

		viewManager.showOptions();

	}

	@FXML
	void addAccountAction() {

		viewManager.showLogin();

	}

	@FXML
	void composeAction() {
		viewManager.showCompose();
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

	@FXML
	void emptyMenuAction() {

		if (emailManager.getSelectedMessage() != null) {

			attachmentsMenu.getItems().clear();
		}

	}

	@FXML
	void attachmentsAction() {

		try {
			setUpAttMenu(new ArrayList<MimeBodyPart>());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	// Sets up attachment choice sub-menu. Kinda
	private void setUpAttMenu(List<MimeBodyPart> attachments) throws MessagingException {

		if (emailManager.getSelectedMessage() != null) {

			if (emailManager.getSelectedMessage().hasAttachment()) {

				attachments = emailManager.getSelectedMessage().getAttachmentList();

				for (MimeBodyPart att : attachments) {

					MenuItem item = new MenuItem(att.getFileName());

					// Download logic
					item.setOnAction(e -> {

						Service<Object> service = new Service<Object>() {

							@Override
							protected Task<Object> createTask() {
								return new Task<Object>() {

									@Override
									protected Object call() throws Exception {

										// Such programming skills
										att.saveFile(DOWNLOAD_LOC + att.getFileName());
										return null;
									}

								};
							}

						};
						service.restart();

					});
					attachmentsMenu.getItems().add(item);

				}

			} else {
				MenuItem item = new MenuItem("(No Attachments)");
				item.setDisable(true);
				attachmentsMenu.getItems().add(item);
			}
		}
	}

	// Left-most panel
	private void setUpTree() {
		emailsTreeView.setRoot(emailManager.getFoldersRoot());
		emailsTreeView.setShowRoot(false);

	}

	// Upper panel: gets properties from MessageBean
	private void setUpTableView() {
		senderCol.setCellValueFactory(new PropertyValueFactory<MessageModel, String>("sender"));
		subjectCol.setCellValueFactory(new PropertyValueFactory<MessageModel, String>("subject"));
		recipientCol.setCellValueFactory(new PropertyValueFactory<MessageModel, String>("recipient"));
		sizeCol.setCellValueFactory(new PropertyValueFactory<MessageModel, SizeIntegerModel>("size"));
		dateCol.setCellValueFactory(new PropertyValueFactory<MessageModel, Date>("date"));

		emailsTableView.setContextMenu(new ContextMenu(markUnread, deleteMessage));

	}

	// Modifies table view depending on selected folder on left panel
	private void setUpFolderSelection() {
		emailsTreeView.setOnMouseClicked(e -> {
			TreeItemModel<String> item = (TreeItemModel<String>) emailsTreeView.getSelectionModel().getSelectedItem();
			if (item != null) {
				emailManager.setSelectedFolder(item);
				emailsTableView.setItems(item.getMessages());
			}
		});

	}

	// Fonts are a pain in the ass
	private void setUpBoldRows() {

		emailsTableView.setRowFactory(new Callback<TableView<MessageModel>, TableRow<MessageModel>>() {

			@Override
			public TableRow<MessageModel> call(TableView<MessageModel> arg0) {
				return new TableRow<MessageModel>() {

					@Override
					protected void updateItem(MessageModel item, boolean empty) {
						super.updateItem(item, empty);

						// Actual logic...
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

	// Sets up renderer based on viewing panel (bottom)
	private void setUpRenderer() {
		renderer = new RendererService(emailsWebView.getEngine());

	}

	// Tells renderer which message to render (table view -> web view)
	private void setUpMessageSelection() {

		emailsTableView.setOnMouseClicked(e -> {
			MessageModel bean = emailsTableView.getSelectionModel().getSelectedItem();
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

package start;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.mail.Flags;

import start.model.EmailAccountModel;
import start.model.TreeItemModel;
import start.model.MessageModel;
import start.services.FolderGeneralService;
import start.services.FolderUpdaterService;

// Handle the folders and accounts
public class EmailManager {

	private TreeItemModel<String> foldersRoot = new TreeItemModel<String>("");
	private List<Folder> folderList = new ArrayList<Folder>();
	private FolderUpdaterService updater;
	private MessageModel selectedMessage;
	private TreeItemModel<String> selectedFolder;
	private ObservableList<EmailAccountModel> emailAccounts = FXCollections.observableArrayList();

	public EmailManager() {
		updater = new FolderUpdaterService(folderList);
		updater.start();
	}

	public MessageModel getSelectedMessage() {
		return selectedMessage;
	}

	public void setSelectedMessage(MessageModel selectedMessage) {
		this.selectedMessage = selectedMessage;
	}

	public TreeItemModel<String> getSelectedFolder() {
		return selectedFolder;
	}

	public void setSelectedFolder(TreeItemModel<String> selectedFolder) {
		this.selectedFolder = selectedFolder;
	}

	public List<Folder> getFolderList() {
		return folderList;
	}

	public TreeItemModel<String> getFoldersRoot() {
		return foldersRoot;
	}

	public void setFoldersRoot(TreeItemModel<String> foldersRoot) {
		this.foldersRoot = foldersRoot;
	}
	
	public ObservableList<EmailAccountModel> getEmailAccounts(){
		return emailAccounts;
	}

	public void addEmailAccount(EmailAccountModel account) {
		emailAccounts.add(account);
		TreeItemModel<String> item = new TreeItemModel<String>(account.getAccount());
		FolderGeneralService folderService = new FolderGeneralService(account.getStore(), item, folderList);
		folderService.start();
		foldersRoot.getChildren().add(item);
	}

	public void deleteSelectedMessage() {
		try {
			selectedMessage.getMessage().setFlag(Flags.Flag.DELETED, true);
			selectedFolder.getMessages().remove(selectedMessage);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setRead(boolean bool) {
		try {
			selectedMessage.setRead(bool);
			selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, bool);

			if (bool) {
				selectedFolder.decrementCount();

			} else {
				selectedFolder.incrementCount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

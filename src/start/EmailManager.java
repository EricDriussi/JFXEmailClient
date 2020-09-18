package start;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Flags;

import start.model.EmailAccount;
import start.model.EmailTreeItem;
import start.model.MessageBean;
import start.services.FolderService;
import start.services.FolderUpdater;

public class EmailManager {

	// Handle the folders
	private EmailTreeItem<String> foldersRoot = new EmailTreeItem<String>("");
	private List<Folder> folderList = new ArrayList<Folder>();
	private FolderUpdater updater;
	private MessageBean selectedMessage;
	private EmailTreeItem<String> selectedFolder;

	public MessageBean getSelectedMessage() {
		return selectedMessage;
	}

	public void setSelectedMessage(MessageBean selectedMessage) {
		this.selectedMessage = selectedMessage;
	}

	public EmailTreeItem<String> getSelectedFolder() {
		return selectedFolder;
	}

	public void setSelectedFolder(EmailTreeItem<String> selectedFolder) {
		this.selectedFolder = selectedFolder;
	}

	public List<Folder> getFolderList() {
		return folderList;
	}

	public EmailManager() {
		updater = new FolderUpdater(folderList);
		updater.start();
	}

	public EmailTreeItem<String> getFoldersRoot() {
		return foldersRoot;
	}

	public void setFoldersRoot(EmailTreeItem<String> foldersRoot) {
		this.foldersRoot = foldersRoot;
	}

	public void addEmailAccount(EmailAccount account) {
		EmailTreeItem<String> item = new EmailTreeItem<String>(account.getAccount());
		FolderService folderService = new FolderService(account.getStore(), item, folderList);
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

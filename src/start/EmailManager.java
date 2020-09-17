package start;

import start.model.EmailAccount;
import start.model.EmailTreeItem;
import start.services.FolderService;

public class EmailManager {

	// Handle the folders
	private EmailTreeItem<String> foldersRoot = new EmailTreeItem<String>("");

	public EmailTreeItem<String> getFoldersRoot() {
		return foldersRoot;
	}

	public void setFoldersRoot(EmailTreeItem<String> foldersRoot) {
		this.foldersRoot = foldersRoot;
	}

	
	public void addEmailAccount(EmailAccount account) {
		EmailTreeItem<String> item = new EmailTreeItem<String>(account.getAccount());
		FolderService folderService = new FolderService(account.getStore(), item);
		folderService.start();
		foldersRoot.getChildren().add(item);
	}
}

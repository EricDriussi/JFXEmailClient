package start.services;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import start.model.EmailTreeItem;

public class FolderService extends Service<Void> {

	private Store store;
	private EmailTreeItem<String> folderRoot;

	public FolderService(Store store, EmailTreeItem<String> folderRoot) {
		super();
		this.store = store;
		this.folderRoot = folderRoot;
	}

	@Override
	protected Task<Void> createTask() {
		// TODO Auto-generated method stub
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub
				fetchFolders();
				return null;
			}

		};
	}

	protected void fetchFolders() throws MessagingException {
		// TODO Auto-generated method stub
		Folder[] folders = store.getDefaultFolder().list();
		handleFolders(folders, folderRoot);
	}

	private void handleFolders(Folder[] folders, EmailTreeItem<String> folderRoot) throws MessagingException {
		// TODO Auto-generated method stub
		for (Folder folder : folders) {
			EmailTreeItem<String> item = new EmailTreeItem<String>(folder.getName());
			folderRoot.getChildren().add(item);
			folderRoot.setExpanded(true);
			if (folder.getType() == Folder.HOLDS_FOLDERS) {
				Folder[] innerFolders = folder.list();
				handleFolders(innerFolders, item);
			}
		}
	}

}

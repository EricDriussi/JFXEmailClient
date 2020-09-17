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
				fetchFolders();
				return null;
			}

		};
	}

	protected void fetchFolders() throws MessagingException {
		Folder[] folders = store.getDefaultFolder().list();
		handleFolders(folders, folderRoot);
	}

	private void handleFolders(Folder[] folders, EmailTreeItem<String> folderRoot) throws MessagingException {
		for (Folder folder : folders) {
			EmailTreeItem<String> item = new EmailTreeItem<String>(folder.getName());
			folderRoot.getChildren().add(item);
			folderRoot.setExpanded(true);
			fetchMessages(folder, item);
			if (folder.getType() == Folder.HOLDS_FOLDERS) {
				Folder[] innerFolders = folder.list();
				handleFolders(innerFolders, item);
			}
		}
	}

	private void fetchMessages(Folder folder, EmailTreeItem<String> item) {
		
		Service fetchMessages = new Service() {

			@Override
			protected Task createTask() {
				
				return new Task() {

					@Override
					protected Object call() throws Exception {
						
						if (folder.getType() != Folder.HOLDS_FOLDERS) {
							folder.open(Folder.READ_WRITE);
							int size = folder.getMessageCount();
							for (int i = size; i >0; i--) {
//								System.out.println(folder.getMessage(i).getSubject());
//								System.out.println(folder.getMessage(i).getAllRecipients()[0].toString());
								item.addEmail(folder.getMessage(i));
							}
						}
						return null;
					}
					
				};
			}
			
		};
		fetchMessages.start();
	}


}









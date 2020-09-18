package start.services;

import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import start.model.EmailTreeItem;

//Folder behavior logic
public class FolderService extends Service<Void> {

	private Store store;
	private EmailTreeItem<String> folderRoot;
	private List<Folder> folderList;

	public FolderService(Store store, EmailTreeItem<String> folderRoot, List<Folder> folderList) {
		super();
		this.store = store;
		this.folderRoot = folderRoot;
		this.folderList = folderList;
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				//Actual code in here, kinda
				fetchFolders();
				return null;
			}

		};
	}

	//Encapsulate me bby
	protected void fetchFolders() throws MessagingException {
		Folder[] folders = store.getDefaultFolder().list();
		handleFolders(folders, folderRoot);
	}

	//Handles folder lists recursively
	private void handleFolders(Folder[] folders, EmailTreeItem<String> folderRoot) throws MessagingException {
		for (Folder folder : folders) {

			folderList.add(folder);
			EmailTreeItem<String> item = new EmailTreeItem<String>(folder.getName());

			//Adds current folder to it's root
			folderRoot.getChildren().add(item);
			folderRoot.setExpanded(true);

			//Appends appropriate messages to current folder
			fetchMessages(folder, item);
			
			//Dynamic update for receiving emails
			addMessageListenerToFolder(folder, item);

			if (folder.getType() == Folder.HOLDS_FOLDERS) {
				Folder[] innerFolders = folder.list();
				handleFolders(innerFolders, item); //Recursion!
			}
		}
	}
	
	//Dynamic update for receiving emails
	private void addMessageListenerToFolder(Folder folder, EmailTreeItem<String> item) {

		folder.addMessageCountListener(new MessageCountListener() {

			@Override
			public void messagesRemoved(MessageCountEvent e) {
			}

			@Override
			public void messagesAdded(MessageCountEvent e) {
				for (int i = 0; i < e.getMessages().length; i++) {
					try {
						Message message = folder.getMessage(folder.getMessageCount() - 1);
						
						//Adds email to top of table view upon receiving it
						item.addEmailToTop(message);
					} catch (MessagingException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

	}
	
	//Appends appropriate messages to current folder
	private void fetchMessages(Folder folder, EmailTreeItem<String> item) {

		Service<Object> fetchMessages = new Service<Object>() {

			@Override
			protected Task<Object> createTask() {

				return new Task<Object>() {

					@Override
					protected Object call() throws Exception {

						if (folder.getType() != Folder.HOLDS_FOLDERS) {
							folder.open(Folder.READ_WRITE);
							int size = folder.getMessageCount();
							for (int i = size; i > 0; i--) {
								
								//Actual logic
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

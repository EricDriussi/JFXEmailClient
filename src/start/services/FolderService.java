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

			folderList.add(folder);
			EmailTreeItem<String> item = new EmailTreeItem<String>(folder.getName());

			folderRoot.getChildren().add(item);
			folderRoot.setExpanded(true);

			fetchMessages(folder, item);
			addMessageListenerToFolder(folder, item);

			if (folder.getType() == Folder.HOLDS_FOLDERS) {
				Folder[] innerFolders = folder.list();
				handleFolders(innerFolders, item);
			}
		}
	}

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
						item.addEmailToTop(message);
					} catch (MessagingException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

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
							for (int i = size; i > 0; i--) {
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

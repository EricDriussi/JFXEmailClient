package start.services;

import java.util.List;

import javax.mail.Folder;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

//Literally just auto-updates folders
public class FolderUpdaterService extends Service<Object> {

	private List<Folder> folderList;

	public FolderUpdaterService(List<Folder> folderList) {
		super();
		this.folderList = folderList;
	}

	@Override
	protected Task<Object> createTask() {
		return new Task<Object>() {

			@Override
			protected Object call() throws Exception {

				for (;;) {
					try {

						// ACTUAL LOGIC
						Thread.sleep(5000); // checks every 5 seconds
						for (Folder folder : folderList) {
							if (folder.getType() != Folder.HOLDS_FOLDERS && folder.isOpen()) {
								folder.getMessageCount(); // for new unread mail
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
	}

}

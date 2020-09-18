package start.services;

import java.util.List;

import javax.mail.Folder;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class FolderUpdater extends Service {

	private List<Folder> folderList;

	public FolderUpdater(List<Folder> folderList) {
		super();
		this.folderList = folderList;
	}

	@Override
	protected Task createTask() {
		return new Task() {

			@Override
			protected Object call() throws Exception {
				
				for(;;){
					try {
						Thread.sleep(5000);
						for (Folder folder : folderList) {
							if (folder.getType() != Folder.HOLDS_FOLDERS && folder.isOpen()) {
								folder.getMessageCount();
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

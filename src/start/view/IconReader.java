package start.view;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconReader {

	public Node getIcon(String folderName) {

		ImageView imgView;
		String name = folderName.toLowerCase();
		try {
			
			if (name.contains("@")) {
				
				imgView = new ImageView(new Image(getClass().getResourceAsStream("icons/email.png")));
				
			}else if (name.contains("inbox")) {
				
				imgView = new ImageView(new Image(getClass().getResourceAsStream("icons/inbox.png")));

			}else if (name.contains("enviado")) {
				
				imgView = new ImageView(new Image(getClass().getResourceAsStream("icons/sent2.png")));

			}else if (name.contains("spam")) {
				
				imgView = new ImageView(new Image(getClass().getResourceAsStream("icons/spam.png")));

			}else {
				
				imgView = new ImageView(new Image(getClass().getResourceAsStream("icons/folder.png")));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		imgView.setFitHeight(16);
		imgView.setFitWidth(16);
		return imgView;
		
	}

}

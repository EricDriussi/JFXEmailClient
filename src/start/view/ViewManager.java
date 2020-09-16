package start.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import start.EmailManager;
import start.controller.*;

public class ViewManager {

	private EmailManager emailManager;

	public ViewManager(EmailManager emailManager) {
		super();
		this.emailManager = emailManager;
	}
	
	public void closeStage(Stage stage) {
		stage.close();
	}

	private void init(BaseController controller) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(controller.getFxmlName()));
		loader.setController(controller);
		Parent parent;
		try {
			parent = loader.load();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		Scene scene = new Scene(parent);

		Stage stage = new Stage();

		stage.setScene(scene);

		stage.show();

	}

	public void showMain() {

		BaseController controller = new MainController(emailManager, this, "Main.fxml");

		init(controller);
	}

	public void showLogin() {

		BaseController controller = new LoginController(emailManager, this, "Login.fxml");

		init(controller);
	}

}

package start.view;

import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import start.EmailManager;
import start.controller.*;

public class ViewManager {

	private EmailManager emailManager;
	private ArrayList<Stage> stages;

	public ViewManager(EmailManager emailManager) {
		super();
		this.emailManager = emailManager;
		stages = new ArrayList<>();
	}
	
	//--------------------------------------------------------------------------
	
	private ColorTheme theme = ColorTheme.LIGHT;
	private FontSize fontSize = FontSize.MEDIUM;
	

	public ColorTheme getTheme() {
		return theme;
	}

	public void setTheme(ColorTheme theme) {
		this.theme = theme;
	}

	public FontSize getFontSize() {
		return fontSize;
	}

	public void setFontSize(FontSize fontSize) {
		this.fontSize = fontSize;
	}

	
	//--------------------------------------------------------------------------


	public void updateStyle() {
		
		for (Stage stage : stages) {
			Scene scene = stage.getScene();
			//Handle CSS!
			scene.getStylesheets().clear();
			scene.getStylesheets().add(getClass().getResource(ColorTheme.getCssPath(theme)).toExternalForm());
			scene.getStylesheets().add(getClass().getResource(FontSize.getCssPath(fontSize)).toExternalForm());
		}
		
	}
	
	public void closeStage(Stage stage) {
		stage.close();
		stages.remove(stage);
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
		
		stages.add(stage);

	}

	public void showMain() {

		BaseController controller = new MainController(emailManager, this, "Main.fxml");

		init(controller);
	}

	public void showOptions() {

		BaseController controller = new OptionsController(emailManager, this, "Options.fxml");
		init(controller);
	}

	public void showLogin() {

		BaseController controller = new LoginController(emailManager, this, "Login.fxml");

		init(controller);
	}


}

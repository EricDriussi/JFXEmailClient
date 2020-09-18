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
	
	private boolean mainInit = false;

	private ColorTheme theme = ColorTheme.DARK;
	private FontSize fontSize = FontSize.MEDIUM;

	public ViewManager(EmailManager emailManager) {
		super();
		this.emailManager = emailManager;
		stages = new ArrayList<>();
	}

	public boolean isMainInit() {
		return mainInit;
	}

	public void setMainInit(boolean mainInit) {
		this.mainInit = mainInit;
	}

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

	//Theme handling
	public void updateStyle() {

		for (Stage stage : stages) {
			Scene scene = stage.getScene();
			scene.getStylesheets().clear();
			scene.getStylesheets().add(getClass().getResource(ColorTheme.getCssPath(theme)).toExternalForm());
			scene.getStylesheets().add(getClass().getResource(FontSize.getCssPath(fontSize)).toExternalForm());
		}

	}

	//Closes selected stage
	public void closeStage(Stage stage) {
		stage.close();
		stages.remove(stage);
	}

	//Sets up main controller and stage
	public void showMain() {

		BaseController controller = new MainController(emailManager, this, "Main.fxml");

		init(controller);
		mainInit = true;
	}
	
	//Sets up options controller and stage
	public void showOptions() {

		BaseController controller = new OptionsController(emailManager, this, "Options.fxml");
		init(controller);
	}
	
	//Sets up login controller and stage
	public void showLogin() {

		BaseController controller = new LoginController(emailManager, this, "Login.fxml");

		init(controller);
	}

	//Inits stage based on controller param
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

}

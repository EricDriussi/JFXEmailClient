package start;



import javafx.application.Application;
import javafx.stage.Stage;
import start.view.ViewManager;

public class Launcher extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		ViewManager manager = new ViewManager(new EmailManager());
		manager.showOptions();
		manager.updateStyle();

		
	}

}

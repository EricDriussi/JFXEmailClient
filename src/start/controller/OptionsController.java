package start.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import start.EmailManager;
import start.view.ColorTheme;
import start.view.FontSize;
import start.view.ViewManager;

public class OptionsController extends BaseController implements Initializable {

	public OptionsController(EmailManager emailManager, ViewManager viewManager, String fxmlName) {
		super(emailManager, viewManager, fxmlName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		setUpTheme();
		setUpFontSize();

	}

	private void setUpFontSize() {
		
		FontSizePicker.setMin(0);
		FontSizePicker.setMax(FontSize.values().length - 1);
		FontSizePicker.setValue(viewManager.getFontSize().ordinal());
		FontSizePicker.setMajorTickUnit(1);
		FontSizePicker.setMinorTickCount(0);
		FontSizePicker.setBlockIncrement(1);
		FontSizePicker.setSnapToTicks(true);
		FontSizePicker.setShowTickMarks(true);
		FontSizePicker.setShowTickLabels(true);
		FontSizePicker.setLabelFormatter(new StringConverter<Double>() {
			
			@Override
			public String toString(Double arg0) {
				int i = arg0.intValue();
				return FontSize.values()[i].toString();
			}
			
			@Override
			public Double fromString(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		FontSizePicker.valueProperty().addListener((obs, oldVal, newVal)->{
			FontSizePicker.setValue(newVal.intValue());
		});
		
	}

	private void setUpTheme() {

		ThemePicker.setItems(FXCollections.observableArrayList(ColorTheme.values()));
		ThemePicker.setValue(viewManager.getTheme());

	}

	@FXML
	private Slider FontSizePicker;

	@FXML
	private ChoiceBox<ColorTheme> ThemePicker;


    
	@FXML
	void applyButtonAction() {
		
		viewManager.setTheme(ThemePicker.getValue());
		viewManager.setFontSize(FontSize.values()[(int)(FontSizePicker.getValue())]);
		viewManager.updateStyle();

	}

	@FXML
	void cancelButtonAction() {
		
		Stage stage = (Stage) FontSizePicker.getScene().getWindow();
		viewManager.closeStage(stage);

	}

}

package start.view;

//Available font size option
public enum FontSize {
	
	SMALL,
	MEDIUM, 
	BIG;
	
	public static String getCssPath(FontSize size) {
		switch (size) {
		case BIG:
			return "css/fontBig.css";
		case MEDIUM:
			return "css/fontMedium.css";
		case SMALL:
			return "css/fontSmall.css";
		default:
			return null;
		}
	}

}

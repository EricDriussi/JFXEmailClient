package start.view;

//Available themes option
public enum ColorTheme {

	LIGHT, DARK, DEFAULT;

	public static String getCssPath(ColorTheme theme) {
		switch (theme) {
		case DARK:
			return "css/themeDark.css";
		case LIGHT:
			return "css/themeLight.css";
		case DEFAULT:
			return "css/themeDefault.css";
		default:
			return null;
		}
	}
}

package start.controller;

import start.EmailManager;
import start.view.ViewManager;

public abstract class BaseController {

	protected EmailManager emailManager;
	protected ViewManager viewManager;
	private String fxmlName;

	public BaseController(EmailManager emailManager, ViewManager viewManager, String fxmlName) {
		super();
		this.emailManager = emailManager;
		this.viewManager = viewManager;
		this.fxmlName = fxmlName;
	}

	public EmailManager getEmailManager() {
		return emailManager;
	}

	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

	public ViewManager getViewManager() {
		return viewManager;
	}

	public void setViewManager(ViewManager viewManager) {
		this.viewManager = viewManager;
	}

	public String getFxmlName() {
		return fxmlName;
	}

	public void setFxmlName(String fxmlName) {
		this.fxmlName = fxmlName;
	}
	
	

}

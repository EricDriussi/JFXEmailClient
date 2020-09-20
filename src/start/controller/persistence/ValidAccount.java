package start.controller.persistence;

import java.io.Serializable;

public class ValidAccount implements Serializable {

	private static final long serialVersionUID = 1L;

	private String address;
	private String password;

	public ValidAccount(String address, String password) {
		super();
		this.address = address;
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

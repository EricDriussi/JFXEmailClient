package start.controller.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Access {

	private String VALID_ACCOUNTS_LOCATION = System.getProperty("user.home") + File.separator + "Documents"
			+ File.separator + "validAccounts.ser";

	private Encoder encoder = new Encoder();

	public List<ValidAccount> load() {

		List<ValidAccount> accList = new ArrayList<ValidAccount>();
		try {

			FileInputStream fileInputStream = new FileInputStream(VALID_ACCOUNTS_LOCATION);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			accList = (List<ValidAccount>) objectInputStream.readObject();

			// Streamlined password decoding
			accList.stream().forEach(acc -> {
				acc.setPassword(encoder.decode(acc.getPassword()));
			});

			objectInputStream.close();
			fileInputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return accList;
	}

	public void save(List<ValidAccount> validAccounts) {

		try {

			FileOutputStream fileOutputStream = new FileOutputStream(new File(VALID_ACCOUNTS_LOCATION));
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

			// Streamlined password encoding
			validAccounts.stream().forEach(acc -> {
				acc.setPassword(encoder.encode(acc.getPassword()));
			});

			objectOutputStream.writeObject(validAccounts);
			objectOutputStream.close();
			fileOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

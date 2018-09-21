/**
 * This class holds all client specific data. 
 * Lots of get/set methods.
 * Holds: fName, lName, address, city, state, zip,
 * phone, accountNumber, Public/Personal Contact Lists,
 * ROI lists respectively, Client Notes, alertNotes
 * 
 * @author Scott Smalley
 * @email scottsmalley90@gmail.com
 * 
 */
package smalleysoftware;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

public class Client 
{
	private String fName;
	private String lName;
	private String address;
	private String city;
	private int stateIndex;
	private int zip;
	private String phone;
	private String email;
	private boolean activeClient;
	private int accountNumber;
	private String alertNotes;
	private JTextArea notes;
	private List<Integer> contactList;
	private List<Contact> contactPersonalList;
	private List<Boolean> clientContactROIList;
	private List<Boolean> clientContactPersonalROIList;
	
	//for building a client from the UI
	public Client(int accountNumber, String fName, String lName, String address, 
				String city, int stateIndex, int zip, String phone, String email, String alertNotes)
	{
		this.fName = fName.toUpperCase();
		this.lName = lName.toUpperCase();
		this.address = address.toUpperCase();
		this.city = city.toUpperCase();
		this.stateIndex = stateIndex;
		this.zip = zip;
		this.accountNumber = accountNumber;
		this.phone = phone;
		this.email = email.toUpperCase();
		this.alertNotes = alertNotes.toUpperCase();
		this.contactList = new ArrayList<>();
		this.contactPersonalList = new ArrayList<>();
		this.clientContactROIList = new ArrayList<>();
		this.clientContactPersonalROIList = new ArrayList<>();
		this.activeClient = true;
	}
	
	//for rebuilding client lists, have to create new clients.
	public Client(Client newClient)
	{
		this.fName = new String (newClient.fName);
		this.lName = new String (newClient.lName);
		this.address = new String (newClient.address);
		this.city = new String (newClient.city);
		this.stateIndex = newClient.stateIndex;
		this.zip = newClient.zip;
		this.accountNumber = newClient.accountNumber;
		this.phone = new String (newClient.phone);
		this.email = new String (newClient.email);
		this.alertNotes = new String (newClient.alertNotes);
		this.contactList = newClient.contactList;
		this.contactPersonalList = new ArrayList<>();
		this.clientContactROIList = new ArrayList<>();
		this.clientContactPersonalROIList = new ArrayList<>();
		this.activeClient = true;
	}
	
	/**
	 * Sets the client's first name to the parameter.
	 * 
	 * @param firstName, String
	 * @return None
	 */
	public void setFname (String firstName)
	{
		fName = new String (firstName).toUpperCase();
	}
	
	/**
	 * Returns the client's first name.
	 * @param None
	 * @return fName, String
	 */
	public String getFname ()
	{
		return fName;
	}
	
	/**
	 * Sets the client's last name to the parameter.
	 * 
	 * @param lastName, String
	 * @return None
	 */
	public void setLname (String lastName)
	{
		lName = new String (lastName).toUpperCase();
	}
	
	/**
	 * Returns the client's last name.
	 * @param None
	 * @return lName, String
	 */
	public String getLname ()
	{
		return lName;
	}
	
	/**
	 * Returns the client's first and last name in a new String.
	 * @param None
	 * @return first and last name combined, String
	 */
	public String getNameFull ()
	{
		return new String (fName + " " + lName);
	}
	
	/**
	 * Sets the client's address to the parameter.
	 * 
	 * @param newAddress, String
	 * @return None
	 */
	public void setAddress (String newAddress)
	{
		address = new String (newAddress).toUpperCase();
	}
	
	/**
	 * Returns the client's current address.
	 * 
	 * @param None
	 * @return address, String
	 */
	public String getAddress ()
	{
		return address;
	}
	
	/**
	 * Sets the client's city to the parameter.
	 * 
	 * @param newCity, String
	 * @return None
	 */
	public void setCity (String newCity)
	{
		city = new String(newCity).toUpperCase();
	}
	
	/**
	 * Returns the client's current city.
	 * 
	 * @param None
	 * @return city, String
	 */
	public String getCity()
	{
		return city;
	}
	
	/**
	 * Sets the client's state to the parameter.
	 * 
	 * @param newState, String
	 * @return None
	 */
	public void setStateIndex(int newState)
	{
		stateIndex = newState;
	}
	
	/**
	 * Returns the client's current state.
	 * 
	 * @param None
	 * @return state, String
	 */
	public int getStateIndex ()
	{
		return stateIndex;
	}
	
	/**
	 * Sets the client's zip to the parameter.
	 * 
	 * @param newZip int
	 * @return None
	 */
	public void setZip (int newZip)
	{
		zip = newZip;
	}
	
	/**
	 * Returns the client's current zip.
	 * 
	 * @param None
	 * @return zip, int
	 */
	public int getZip ()
	{
		return zip;
	}
	
	/**
	 * Returns the client's account number.
	 * 
	 * @param None
	 * @return accountNumber, int
	 */
	public int getAccountNumber ()
	{
		return accountNumber;
	}
	
	/**
	 * Returns the client's full address in a new String.
	 * includes: address(street), city, state, and zip.
	 * 
	 * @param None
	 * @return full address, String
	 */
	public String getAddressFull()
	{
		return new String (address + ", " + city + ", " + getStateWithIndex(getStateIndex()) + " " + zip);
	}
	
	/**
	 * Returns the client's full information in a presentable manner.
	 * this includes: accountNumber, full name, full address, phone, and alert level.
	 * 
	 * @param None
	 * @return client's entire info - presentable, String
	 */
	public String getClientInfo()
	{
		return new String ("Acct: " + accountNumber + " Name: " + getNameFull() + " "
								+ "Address: " + getAddressFull() + " Phone: " + phone + " Email: " + email + " Alert: " + alertNotes);
	}
	
	/**
	 * Returns the client's full raw information.
	 * this includes: accountNumber, full name, full address, phone, and alert level.
	 * 
	 * @param None
	 * @return client's entire info - RAW, String
	 */
	public String getClientRawInfo()
	{
		return new String (accountNumber + " " + getNameFull() + " " + getAddressFull() + " " + phone + " " + email + " " + alertNotes);
	}
	
	/**
	 * Returns whether the client is active or not.
	 * 
	 * @param None
	 * @return activeClient, boolean
	 */
	public boolean getClientActive()
	{
		return this.activeClient;
	}
	
	/**
	 * Sets the client as an Active client.
	 * true = an active client.
	 * 
	 * @param None
	 * @return None
	 */
	public void setClientActive(Boolean newActive)
	{
		activeClient = newActive; 
	}
	
	/**
	 * Returns the client's phone number.
	 * 
	 * @param None
	 * @return phone, String
	 */
	public String getPhone()
	{
		return phone;
	}
	
	/**
	 * Sets the client's phone number.
	 * 
	 * @param newPhone, String
	 * @return None
	 */
	public void setPhone(String newPhone)
	{
		phone = new String(newPhone);
	}
	
	/**
	 * Returns the client's email number.
	 * 
	 * @param None
	 * @return phone, String
	 */
	public String getEmail()
	{
		return email;
	}
	
	/**
	 * Sets the client's email number.
	 * 
	 * @param newPhone, String
	 * @return None
	 */
	public void setEmail(String newEmail)
	{
		email = new String(newEmail);
	}
	
	/**
	 * Returns the client's alert level.
	 * 
	 * @param None
	 * @return alertLevel, int
	 */
	public String getAlertNotes()
	{
		return alertNotes;
	}
	
	/**
	 * Sets the client's alert level.
	 * 
	 * @param newLevel, int
	 * @return None
	 */
	public void setAlertNotes(String newNotes)
	{
		alertNotes = new String(newNotes);
	}
	/**
	 * Returns the ArrayList<Integer> of client public contacts.
	 * @param None
	 * @return ArrayList<Integer>
	 */
	public ArrayList<Integer> getContactList()
	{
		return (ArrayList<Integer>)contactList;
	}
	/**
	 * Sets the Client Public Contact ArrayList<Integer>
	 * @param newContactList
	 * @return None
	 */
	public void setContactList(ArrayList<Integer> newContactList)
	{
		contactList = newContactList;
	}
	/**
	 * Returns the ArrayList<Contact> of client personal contacts.
	 * @param None
	 * @return ArrayList<Contact>
	 */
	public ArrayList<Contact> getContactPersonalList()
	{
		return (ArrayList<Contact>)contactPersonalList;	
	}
	/**
	 * Sets the Client Personal Contact ArrayList<Contact>
	 * @param newContactPersonalList
	 * @return None
	 */
	public void setContactPersonalList(ArrayList<Contact> newContactPersonalList)
	{
		contactPersonalList = newContactPersonalList;
	}
	/**
	 * Returns the ArrayList<Boolean> of client public ROI.
	 * @param None
	 * @return ArrayList<Boolean>
	 */
	public ArrayList<Boolean> getContactROIList()
	{
		return (ArrayList<Boolean>)clientContactROIList;	
	}
	/**
	 * Sets the Client Public ROI ArrayList<Boolean>
	 * @param newContactROIList
	 * @return None
	 */
	public void setContactROIList(ArrayList<Boolean> newContactROIList)
	{
		clientContactROIList = newContactROIList;
	}
	/**
	 * returns the Client Contact Personal ROI ArrayList<Boolean>
	 * @param None
	 * @return ArrayList<Boolean>
	 * 
	 */
	public ArrayList<Boolean> getContactPersonalROIList()
	{
		return (ArrayList<Boolean>)clientContactPersonalROIList;	
	}
	/**
	 * Sets the client contact personal ROI list
	 * @param newContactPersonalROIList
	 * @return None
	 */
	public void setContactPersonalROIList(ArrayList<Boolean> newContactPersonalROIList)
	{
		clientContactPersonalROIList = newContactPersonalROIList;
	}
	/**
	 * Returns the JTextArea with the Client Notes
	 * @param None
	 * @return Client Notes (JTextArea)
	 */
	public JTextArea getClientNotes()
	{
		return (JTextArea)notes;
	}
	/**
	 * Sets the JTextArea client Notes
	 * @param newNotes
	 * @return None
	 */
	public void setClientNotes(JTextArea newNotes)
	{
		newNotes.setText(newNotes.getText().toUpperCase());
		this.notes = newNotes;
	}
	
	public String[] getStatesList()
	{
		return new String[] 
		{
			" ","Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","Florida","Georgia",
			"Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland",
			"Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada",
			"New Hampshire","New Jersey","New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma",
			"Oregon","Pennsylvania","Rhode Island","South Carolina","South Dakota","Tennessee","Texas","Utah",
			"Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming","District of Columbia", "Other"
		};
		
	}
	/**
	 * returns the State of the state stored.
	 * @param index
	 * @return State (String)
	 */
	public String getStateWithIndex(int index)
	{
		String statesList[] = getStatesList();
		return statesList[index];
	}
}

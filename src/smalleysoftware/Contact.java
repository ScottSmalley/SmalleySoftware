/**
 * This method represents the Contacts, public or personal
 * It lists their Account Number,
 * Name, Title, Street Address,
 * City, State, Zip, Fax,
 * Email, Notes, and their selection 
 * in the contactMainButton() method.
 * @author Scott Smalley
 * @email scottsmalley90@gmail.com
 */
package smalleysoftware;

import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class Contact 
{
	private int accountNumber;
	private String name;
	private String title;
	private String address;
	private String city;
	private int stateIndex;
	private int zip;
	private String phone;
	private String fax;
	private String email;
	private JTextArea notes;
	private JRadioButton selection;
	
	public Contact(int accountNumber, String name, String title, String address, String city, int stateIndex,
			int zip, String phone, String fax, String email, JTextArea notes)
	{
		this.accountNumber = accountNumber;
		this.name = name.toUpperCase();
		this.title = title.toUpperCase();
		this.address = address.toUpperCase();
		this.city = city.toUpperCase();
		this.stateIndex = stateIndex;
		this.zip= zip;
		this.phone = phone;
		this.fax = fax;
		this.email = email.toUpperCase();
		notes.setText(notes.getText().toUpperCase());
		this.notes = notes;
		
	}
	
	/**
	 * Returns the contact's accountNumber.
	 * @param None
	 * @return accountNumber, Int
	 */
	public int getAccountNumber()
	{
		return accountNumber;
	}
	
	/**
	 * Sets the contact's name to the parameter.
	 * 
	 * @param name, String
	 * @return None
	 */
	public void setName (String newName)
	{
		name = new String (newName).toUpperCase();
	}
	
	/**
	 * Returns the contact's name.
	 * @param None
	 * @return name, String
	 */
	public String getName ()
	{
		return name;
	}

	/**
	 * Sets the contact's address to the parameter.
	 * 
	 * @param newAddress, String
	 * @return None
	 */
	public void setAddress (String newAddress)
	{
		address = new String (newAddress).toUpperCase();
	}
	
	/**
	 * Returns the contact's current address.
	 * 
	 * @param None
	 * @return address, String
	 */
	public String getAddress ()
	{
		return address;
	}

	/**
	 * Sets the contact's city to the parameter.
	 * 
	 * @param newCity, String
	 * @return None
	 */
	public void setCity (String newCity)
	{
		city = new String(newCity).toUpperCase();
	}
	
	/**
	 * Returns the contact's current city.
	 * 
	 * @param None
	 * @return city, String
	 */
	public String getCity()
	{
		return city;
	}
	
	/**
	 * Sets the contact's state to the parameter.
	 * 
	 * @param newState, String
	 * @return None
	 */
	public void setState(int newStateIndex)
	{
		stateIndex = newStateIndex;
	}
	
	/**
	 * Returns the contact's current state.
	 * 
	 * @param None
	 * @return state, String
	 */
	public int getState ()
	{
		return stateIndex;
	}
	
	/**
	 * Sets the contact's zip to the parameter.
	 * 
	 * @param newZip int
	 * @return None
	 */
	public void setZip (int newZip)
	{
		zip = newZip;
	}
	
	/**
	 * Returns the contact's current zip.
	 * 
	 * @param None
	 * @return zip, int
	 */
	public int getZip ()
	{
		return zip;
	}
	
	/**
	 * Returns the contact's full address in a new String.
	 * includes: address(street), city, state, and zip.
	 * 
	 * @param None
	 * @return full address, String
	 */
	public String getAddressFull()
	{
		return new String (address + ", " + city + ", " + getStateWithIndex(getState()) + " " + zip);
	}
	
	/**
	 * Sets the contact's title to the parameter.
	 * 
	 * @param newState, String
	 * @return None
	 */
	public void setTitle(String newTitle)
	{
		title = new String (newTitle).toUpperCase();
	}
	
	/**
	 * Returns the contact's current title.
	 * 
	 * @param None
	 * @return state, String
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * Sets the contact's notes to the parameter.
	 * 
	 * @param newState, String
	 * @return None
	 */
	public void setNotes(JTextArea newNotes)
	{
		newNotes.setText(newNotes.getText().toUpperCase());
		notes = newNotes;
	}
	
	/**
	 * Returns the contact's current notes.
	 * 
	 * @param None
	 * @return state, String
	 */
	public JTextArea getNotes ()
	{
		return notes;
	}
	
	/**
	 * Sets the contact's phone number.
	 * 
	 * @param newPhone, String
	 * @return None
	 */
	public void setPhone(String newPhone)
	{
		phone = new String(newPhone);
	}
	
	/**
	 * Returns the contact's phone number.
	 * 
	 * @param None
	 * @return phone, String
	 */
	public String getPhone()
	{
		return phone;
	}
	
	/**
	 * Sets the contact's fax number.
	 * 
	 * @param newFax, String
	 * @return None
	 */
	public void setFax(String newFax)
	{
		fax = new String(newFax);
	}
	
	/**
	 * Returns the contact's fax number.
	 * 
	 * @param None
	 * @return fax, String
	 */
	public String getFax()
	{
		return fax;
	}
	
	/**
	 * Returns the contact's email number.
	 * 
	 * @param None
	 * @return phone, String
	 */
	public String getEmail()
	{
		return email;
	}
	
	/**
	 * Sets the contact's email number.
	 * 
	 * @param newPhone, String
	 * @return None
	 */
	public void setEmail(String newEmail)
	{
		email = new String(newEmail);
	}
		
	/**
	 * Returns the contact's full information in a presentable manner.
	 * this includes: name, title, full address, phone, email, and notes.
	 * 
	 * @param None
	 * @return contact's entire info - presentable, String
	 */
	public String getContactInfoFull()
	{
		return new String ("Name: " + getName() + " Title: " + getTitle()	+ " Address: " + getAddressFull() 
								+ " Phone: " + phone + " Email: " + email + " Notes: " + getNotes());
	}
	
	public void setSelectionButton(JRadioButton newButton)
	{
		this.selection = newButton;
		
	}
	
	public JRadioButton getSelectionButton()
	{
		return selection;
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
	public String getStateWithIndex(int index)
	{
		String statesList[] = getStatesList();
		return statesList[index];
	}	
 }

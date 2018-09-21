/**
 * This class represents all data that needs to be loaded.
 * For example, it retrieves clients from a txt file, can
 * create a new text file, add a client to an existing list.
 * 
 * @author Scott Smalley
 * @email scottsmalley90@gmail.com
 * 
 */
package smalleysoftware;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JTextArea;

public class Loader 
{
	//CLIENTS
	
	/** 
	 * This method takes in a fileName and a arraylist of Clients.
	 * It will create a new .txt file with all the info in the
	 * arraylist. It will also create/delete any sub files.
	 * 
	 * @param fileName, String
	 * @param newClientList, ArrayList<Client>
	 * @return None
	 */
	public void createNewClientFile(String fileName, ArrayList<Client> newClientList)
	{
		BufferedWriter writer = null;
		BufferedWriter writerNotes = null;
		try
		{
			File file = new File("src/resources/"+fileName);
			writer = new BufferedWriter(new FileWriter(file));
			for (Client a: newClientList)
			{
				String activityConversion = "";
				if (a.getClientActive() == true)
				{
					activityConversion = "T";
				}
				else
				{
					activityConversion = "F";
				}
				writer.write("Account: " + a.getAccountNumber() + " FName: " + a.getFname()
				+ " LName: " + a.getLname() + " Address: " + a.getAddress() + " City: "
				+ a.getCity() + " State: " + a.getStateIndex() + " Zip: " + a.getZip() + " Phone: "
				+ a.getPhone() + " Email: " + a.getEmail() + " AlertNotes: " + a.getAlertNotes()
				+ " Active: " + activityConversion);
				writer.newLine();
				
				try
				{
					File fileNotes = new File("src/resources/clientnotes/client" +
							a.getAccountNumber() + "notes.txt");
					 writerNotes = new BufferedWriter(new FileWriter(fileNotes));
					a.getClientNotes().write(writerNotes);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						writerNotes.close();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
					createNewClientContactPersonalFile(a.getAccountNumber(), (ArrayList<Contact>)a.getContactPersonalList(), (ArrayList<Boolean>)a.getContactPersonalROIList());
					createNewClientContactFile(a.getAccountNumber(), (ArrayList<Integer>)a.getContactList(), (ArrayList<Boolean>)a.getContactROIList());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			writer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			File clientNotesFolder = new File("src/resources/clientnotes");
			File[] arrayOfClientNotesFiles = clientNotesFolder.listFiles();
			List<String> clientNoteFilesToDelete = new ArrayList<>();

			for (int i = 0; i < arrayOfClientNotesFiles.length; i++)
			{
				String fileNameToCheck = arrayOfClientNotesFiles[i].getName();
				Boolean fileHasAClient = false;
				
				for (int j = 0; j < newClientList.size(); j++)
				{
					if (fileNameToCheck.contains("t"+Integer.toString(newClientList.get(j).getAccountNumber()) + "n"))
					{
						fileHasAClient = true;
					}
				}
				if (!fileHasAClient)
				{
					clientNoteFilesToDelete.add(fileNameToCheck);
				}
			}
			if (!clientNoteFilesToDelete.isEmpty())
			{
				for (String a: clientNoteFilesToDelete)
				{
					File currentFileToDelete = new File("src/resources/clientnotes/" + a);
					if (currentFileToDelete.delete())
					{	
					}
					else
					{
					}
				}
			}
			File clientContactsFolder = new File("src/resources/clientcontacts");
			File[] arrayOfClientContactsFiles = clientContactsFolder.listFiles();
			List<String> clientContactFilesToDelete = new ArrayList<>();
			
			for (int i = 0; i < arrayOfClientContactsFiles.length; i++)
			{
				String fileNameToCheck = arrayOfClientContactsFiles[i].getName();
				Boolean fileHasAClient = false;
				
				for (int j = 0; j < newClientList.size(); j++)
				{
					if (fileNameToCheck.contains("t"+Integer.toString(newClientList.get(j).getAccountNumber()) + "c"))
					{
						fileHasAClient = true;
					}
				}
				if (!fileHasAClient)
				{
					clientContactFilesToDelete.add(fileNameToCheck);
				}
			}
			if (!clientContactFilesToDelete.isEmpty())
			{
				for (String a: clientContactFilesToDelete)
				{
					
					File currentFileToDelete = new File("src/resources/clientcontacts/" + a);
					if (currentFileToDelete.delete())
					{	
					}
					else
					{
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		 
		
	}
	/**
	 * This method takes in a string to load in the contents of the filename.
	 * It reads in each client, creating a new Client object for each set of data.
	 * 
	 * @param fileName, String
	 * @return clientList, ArrayList<Client>
	 */
	public ArrayList<Client> loadClientList(String fileName)
	{
		try 
		{
			File file = new File ("src/resources/"+fileName);
			Scanner fileScan = new Scanner (file);
			List<Client>clientList = new ArrayList<>();
			List<Boolean> activityList = new ArrayList<>();
			List<JTextArea> loadedNotes = new ArrayList<>();
			while (fileScan.hasNextLine())
			{
				String clientInfo = fileScan.nextLine();
				int acct = 9;
				int acctToFname = (clientInfo.indexOf("FName:")-1);		//AccountNumber
				int fNameToLname = (clientInfo.indexOf("LName:")-1);	//FirstName
				int lNameToAddress =(clientInfo.indexOf("Address:")-1);	//LastName
				int addressToCity = (clientInfo.indexOf("City:")-1);	//Address
				int cityToState = (clientInfo.indexOf("State:")-1);		//City
				int stateToZip = (clientInfo.indexOf("Zip:")-1);		//State
				int zipToPhone = (clientInfo.indexOf("Phone:")-1);		//Zip
				int phoneToEmail = (clientInfo.indexOf("Email:")-1);	//PhoneNumber
				int emailToAlert = (clientInfo.indexOf("AlertNotes:")-1);//Email
				int alertToActive = (clientInfo.indexOf("Active:")-1); //Active
				
				int newAcctNumber = Integer.parseInt(clientInfo.substring(acct, acctToFname));
		 		String newfName = clientInfo.substring(acctToFname+8, fNameToLname);
				String newlName = clientInfo.substring(fNameToLname+8, lNameToAddress);
				String newaddress = clientInfo.substring(lNameToAddress+10, addressToCity);
				String newcity = clientInfo.substring(addressToCity+7, cityToState);
				int newstate = Integer.parseInt(clientInfo.substring(cityToState+8, stateToZip));
				int newzip = Integer.parseInt(clientInfo.substring(stateToZip+6, zipToPhone));
				String newphone = clientInfo.substring(zipToPhone+8, phoneToEmail);
				String newemail = clientInfo.substring(phoneToEmail+8, emailToAlert);
				String newAlert = clientInfo.substring(emailToAlert+13, alertToActive);
				
				JTextArea clientNotesTextArea = new JTextArea();
				try (FileReader contactNotesReader = new FileReader("src/resources/clientnotes/client"+newAcctNumber+"notes.txt"))
				{
					clientNotesTextArea.read(contactNotesReader, null);
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}	
				catch (IOException ee)
				{
					ee.printStackTrace();
				}
				loadedNotes.add(clientNotesTextArea);
				clientList.add(new Client(newAcctNumber, newfName, newlName, newaddress, 
						newcity, newstate, newzip, newphone, newemail, newAlert));
				if (clientInfo.substring(alertToActive+9).equalsIgnoreCase("t"))
				{
					activityList.add(true);
				}
				else
				{
					activityList.add(false);
				}
			}
			for (int i = 0; i < clientList.size(); i++)
			{
				if (activityList.get(i) == true)
				{
					clientList.get(i).setClientActive(true);
				}
				else
				{
						clientList.get(i).setClientActive(false);
				}
				clientList.get(i).setClientNotes(loadedNotes.get(i));
			}
			fileScan.close();
			for (int i = 0; i < clientList.size(); i++)
			{
				try 
				{
					File clientContacts = new File
						("src/resources/clientcontacts/client"+clientList.get(i).getAccountNumber()+"contactspublic.txt");
					Scanner clientContactsScanner = new Scanner (clientContacts);
					List<Integer> clientContactAcctList = new ArrayList<>();
					List<Boolean> clientContactROIList = new ArrayList<>();

					while (clientContactsScanner.hasNext())
					{
						clientContactAcctList.add(Integer.parseInt(clientContactsScanner.next()));
						try
						{
							if (clientContactsScanner.next().equals("T"))
							{
								clientContactROIList.add(true);
							}
							else
							{
								clientContactROIList.add(false);
							}
						}
						catch(NoSuchElementException e)
						{
							clientContactROIList.add(false);
						}
					}
					clientList.get(i).setContactList((ArrayList<Integer>)clientContactAcctList);
					clientList.get(i).setContactROIList((ArrayList<Boolean>) clientContactROIList);
					clientContactsScanner.close();
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
			for (int i = 0; i < clientList.size(); i++)
			{
				File clientContactsPersonal = new File
						("src/resources/clientcontacts/client" + clientList.get(i).getAccountNumber() + "contactspersonal.txt");
				Scanner clientContactsPersonalScanner = new Scanner(clientContactsPersonal);
				List<Boolean> personalROIList = new ArrayList<>();
				while (clientContactsPersonalScanner.hasNextLine())
				{
					String contactInfo = clientContactsPersonalScanner.nextLine();
					int emailToROI = (contactInfo.indexOf("ROI:")-1);
					String newROI = contactInfo.substring(emailToROI+6);
					if (newROI.contains("T"))
					{
						personalROIList.add(true);
					}
					else
					{
						personalROIList.add(false);
					}
				}
				clientList.get(i).setContactPersonalROIList((ArrayList<Boolean>)personalROIList);
				clientContactsPersonalScanner.close();
			}
			return (ArrayList<Client>)clientList;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}	
		return null;
	}
	/**
	 * This method takes in a client's acct number, the client's
	 * personal contact list, and the personal ROI list
	 * It will create a new .txt file with all the info in the
	 * arraylist.
	 * 
	 * @param fileName, String
	 * @param newContactList, ArrayList<Contact>
	 * @param newClientContactPersonalROIList, ArrayList<Boolean>
	 * @return None
	 */
	public void createNewClientContactPersonalFile(int clientAcctNumber, ArrayList<Contact> clientContactPersonalList, ArrayList<Boolean> newClientContactPersonalROIList)
	{
		BufferedWriter writer = null;
		BufferedWriter writerNotes = null;
		try
		{
			File file = new File("src/resources/clientcontacts/client"+clientAcctNumber+"contactspersonal.txt");
			writer = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < newClientContactPersonalROIList.size(); i++)
			{
				String roiStatus = "F";
				if (newClientContactPersonalROIList.get(i) == true)
				{
					roiStatus = "T";
				}
				else
				{
					roiStatus = "F";
				}
				writer.write("Account: " + clientContactPersonalList.get(i).getAccountNumber() + " Name: " + clientContactPersonalList.get(i).getName() + " Title: " + clientContactPersonalList.get(i).getTitle()
				+ " Address: " + clientContactPersonalList.get(i).getAddress() + " City: "	+ clientContactPersonalList.get(i).getCity() 
				+ " State: " + clientContactPersonalList.get(i).getState() + " Zip: " + clientContactPersonalList.get(i).getZip() + " Phone: "
				+ clientContactPersonalList.get(i).getPhone() + " Fax: " + clientContactPersonalList.get(i).getFax() + " Email: " + clientContactPersonalList.get(i).getEmail() 
				+ " ROI: " + roiStatus);
				writer.newLine();
		
				try
				{
					File fileNotes = new File("src/resources/clientcontacts/client" +
							clientAcctNumber + "contactspersonal" + 
							clientContactPersonalList.get(i).getAccountNumber() + "notes.txt");
					writerNotes = new BufferedWriter(new FileWriter(fileNotes));
					clientContactPersonalList.get(i).getNotes().write(writerNotes);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						writerNotes.close();
					}
					catch(Exception j)
					{
						j.printStackTrace();
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			writer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * This method takes in a client account number, finds the personal contact
	 * file and read it in. It will set them into Contact Objects in an ArrayList, and return.
	 * 
	 * @param clientAcctNumber, int
	 * @return contactList, ArrayList<Contact>
	 */
	public ArrayList<Contact> loadClientContactPersonalList(int clientAccountNumber)
	{
		try 
		{
			File clientContactsFile = new File ("src/resources/clientcontacts/client"+clientAccountNumber+"contactspersonal.txt");
			Scanner fileScan = new Scanner (clientContactsFile);
			List<Contact> clientContactPersonalList = new ArrayList<>();
			
			while (fileScan.hasNextLine())
			{
				String contactInfo = fileScan.nextLine();
				int acct = 9;
				int acctToName = (contactInfo.indexOf("Name: ")-1);		//ACCT
				int nameToTitle = (contactInfo.indexOf("Title:")-1);	//NAME
				int titleToAddress = (contactInfo.indexOf("Address:")-1);//TITLE
				int addressToCity =(contactInfo.indexOf("City:")-1);	//ADDRESS
				int cityToState = (contactInfo.indexOf("State:")-1);	//CITY
				int stateToZip = (contactInfo.indexOf("Zip:")-1);		//STATE
				int zipToPhone = (contactInfo.indexOf("Phone:")-1);		//ZIP
				int phoneToFax = (contactInfo.indexOf("Fax:")-1);		//PHONE
				int faxToEmail = (contactInfo.indexOf("Email:")-1);		//FAX
				int emailToROI = (contactInfo.indexOf("ROI:")-1);
				
				int newAcct = Integer.parseInt(contactInfo.substring(acct, acctToName));
				String newName = contactInfo.substring(acctToName+7, nameToTitle);
		 		String newTitle = contactInfo.substring(nameToTitle+8, titleToAddress);
				String newAddress = contactInfo.substring(titleToAddress+10, addressToCity);
				String newCity = contactInfo.substring(addressToCity+7, cityToState);			
				int newState = Integer.parseInt(contactInfo.substring(cityToState+8, stateToZip));
				int newZip = Integer.parseInt(contactInfo.substring(stateToZip+6, zipToPhone));
				String newPhone = contactInfo.substring(zipToPhone+8, phoneToFax);
				String newFax = contactInfo.substring(phoneToFax+6, faxToEmail);
				String newEmail = contactInfo.substring(faxToEmail+8, emailToROI);

				JTextArea newNotes = new JTextArea();

				try (FileReader personalContactNotesReader = new FileReader("src/resources/clientcontacts/client" + clientAccountNumber
					+ "contactspersonal" + newAcct + "notes.txt"))
				{
					newNotes.read(personalContactNotesReader, null);
					personalContactNotesReader.close();
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}	
				catch (IOException ee)
				{
					ee.printStackTrace();
				}
				clientContactPersonalList.add(new Contact(newAcct, newName, newTitle, newAddress, newCity, 
						newState, newZip, newPhone, newFax, newEmail, newNotes));
			}
			fileScan.close();
			return (ArrayList<Contact>)clientContactPersonalList;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}	
		return null;
	}
	/** 
	 * This method takes in a client's acct number, ClientContactList, and clientContact ROI list
	 * it will take the two arraylists and write them into a single file.
	 * 
	 * @param clientAcctNumber, int
	 * @param newContactList, ArrayList<Integer>
	 * @param newClientContactROIList, ArrayList<Boolean>
	 * @return None
	 */
	public void createNewClientContactFile(int clientAcctNumber, ArrayList<Integer> newClientContactList, ArrayList<Boolean> newClientContactROIList)
	{
		BufferedWriter writer = null;
		try
		{
			File file = new File("src/resources/clientcontacts/client"+clientAcctNumber+"contactspublic.txt");
			writer = new BufferedWriter(new FileWriter(file));
			
			for (int i = 0; i < newClientContactROIList.size(); i++)
			{
				String roiStatus = "F";
				if (newClientContactROIList.get(i) == true)
				{
					roiStatus = "T";
				}
				else
				{
					roiStatus = "F";
				}
				writer.write(newClientContactList.get(i)+" "+roiStatus);
				writer.newLine();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		try
		{
			writer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * This method takes in a client acct number, and creates a public Contact ArrayList
	 * 
	 * @param clientAcctNum, int
	 * @return contactList, ArrayList<String>
	 */
	public ArrayList<String> loadClientContactList(int clientAccountNumber)
	{
		try 
		{
			File clientContactsFile = new File ("src/resources/clientcontacts/client"+clientAccountNumber+"contactspublic.txt");
			Scanner fileScan = new Scanner (clientContactsFile);
			List<String> clientContactList = new ArrayList<>();
			
			while (fileScan.hasNextLine())
			{
				String contactInfo = fileScan.nextLine();
				clientContactList.add(contactInfo);
			}

			fileScan.close();
			return (ArrayList<String>)clientContactList;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}	
		return null;
	}
	/**
	 * This method takes in a client account number, and the JTextArea notes
	 * it will write out a new file with those notes.
	 * @param acctNumber
	 * @param clientNotesTextArea
	 */
	public void createNewClientNotesTextArea(int acctNumber, JTextArea clientNotesTextArea)
	{
		BufferedWriter writer = null;
		try
		{
			File file = new File("src/resources/clientnotes/client"+acctNumber+"notes.txt");
			writer = new BufferedWriter(new FileWriter(file));
			clientNotesTextArea.write(writer);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			writer.close();
		}
		catch(Exception j)
		{
			j.printStackTrace();
		}
		
		
	}
	/**
	 * This method takes in a client acctNumber, and finds the client notes associated
	 * with it. Then returns them in a JTextArea
	 * @param acctNumber
	 * @return ClientNotes, JTextArea
	 */
	public JTextArea loadClientNotesTextArea(int acctNumber)
	{
		JTextArea loadedTextArea = new JTextArea();
		try (FileReader clientNotesReader = new FileReader("src/resources/clientnotes/client"+acctNumber+"notes.txt"))
		{
			loadedTextArea.read(clientNotesReader, null);
			clientNotesReader.close();
			return loadedTextArea;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}	
		catch (IOException ee)
		{
			ee.printStackTrace();
		}
		return null;
	}
	/**
	 * This method will search through the clientList that was 
	 * processed in the getClientList() method above. It looks at each 
	 * client's account number, if it finds a number (starting at 1) that
	 * does not have a client assigned to it, it will return that int value.
	 * 
	 * @return available account, int
	 */
	public int findAvailableClientAccountNumber(ArrayList<Client> clientList)
	{
		int availableAccount = 1;
		boolean foundAvailability = false;
		boolean matchFound = false;
		while (foundAvailability == false)
		{
			for (Client a: clientList)
			{
				if (a.getAccountNumber() == availableAccount)
				{
					matchFound = true;
					break;
				}
			}
			if (matchFound == true)
			{
				availableAccount++;	
				matchFound = false;
			}
			else
			{
				foundAvailability = true;
			}
		}
		return availableAccount;
	}
	
	//CONTACTS
	
	/**
	 * This method takes in a filename, and an ArrayList<Contact> of updated contacts
	 * it will rewrite the contact master file.
	 * @param fileName
	 * @param newContactList
	 */
	public void createNewContactFile(String fileName, ArrayList<Contact> newContactList)
	{
		BufferedWriter writer = null;
		BufferedWriter writerNotes = null;
		try
		{
			File file = new File("src/resources/"+fileName);
			writer = new BufferedWriter(new FileWriter(file));
			for (Contact a: newContactList)
			{
				writer.write("Account: " + a.getAccountNumber() + " Name: " + a.getName() + " Title: " + a.getTitle()
				+ " Address: " + a.getAddress() + " City: "	+ a.getCity() 
				+ " State: " + a.getState() + " Zip: " + a.getZip() + " Phone: "
				+ a.getPhone() + " Fax: " + a.getFax() + " Email: " + a.getEmail());
				writer.newLine();
				try
				{
					File fileNotes = new File("src/resources/contacts/contact" +
							a.getAccountNumber() + "notes.txt");
					 writerNotes = new BufferedWriter(new FileWriter(fileNotes));
					a.getNotes().write(writerNotes);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						writerNotes.close();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{	
			writer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			File contactFolder = new File("src/resources/contacts");
			File[] arrayOfContactFiles = contactFolder.listFiles();
			List<String> filesToDelete = new ArrayList<>();

			for (int i = 0; i < arrayOfContactFiles.length; i++)
			{
				String fileNameToCheck = arrayOfContactFiles[i].getName();
				Boolean fileHasAContact = false;
				
				for (int j = 0; j < newContactList.size(); j++)
				{
					if (fileNameToCheck.contains("t"+Integer.toString(newContactList.get(j).getAccountNumber()) + "n"))
					{
						fileHasAContact = true;
					}
				}
				if (!fileHasAContact)
				{
					filesToDelete.add(fileNameToCheck);
				}
			}
			if (!filesToDelete.isEmpty())
			{
				for (String a: filesToDelete)
				{
					File currentFileToDelete = new File("src/resources/contacts/" + a);
					System.out.println(currentFileToDelete.getAbsolutePath());
					if (currentFileToDelete.delete())
					{	
					}
					else
					{	
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * This method takes in a string to load in the contents of the filename.
	 * It reads in each contact, creating a new Contact object for each set of data.
	 * 
	 * @param fileName, String
	 * @return contactList, ArrayList<Contact>
	 */
	public ArrayList<Contact> loadContactList(String fileName)
	{
		try 
		{
			File contactsFile = new File ("src/resources/"+fileName);
			Scanner fileScan = new Scanner (contactsFile);
			List<Contact>contactList = new ArrayList<>();
			
			while (fileScan.hasNextLine())
			{
				String contactInfo = fileScan.nextLine();
				int acct = 9;
				int acctToName = (contactInfo.indexOf("Name: ")-1);		//ACCT
				int NameToTitle = (contactInfo.indexOf("Title:")-1);	//NAME
				int TitleToAddress = (contactInfo.indexOf("Address:")-1);//TITLE
				int AddressToCity =(contactInfo.indexOf("City:")-1);	//ADDRESS
				int CityToState = (contactInfo.indexOf("State:")-1);	//CITY
				int StateToZip = (contactInfo.indexOf("Zip:")-1);		//STATE
				int ZipToPhone = (contactInfo.indexOf("Phone:")-1);		//ZIP
				int PhoneToFax = (contactInfo.indexOf("Fax:")-1);		//PHONE
				int FaxToEmail = (contactInfo.indexOf("Email:")-1);		//FAX
				
				int newAcct = Integer.parseInt(contactInfo.substring(acct, acctToName));
				String newName = contactInfo.substring(acctToName+7, NameToTitle);
		 		String newTitle = contactInfo.substring(NameToTitle+8, TitleToAddress);
				String newAddress = contactInfo.substring(TitleToAddress+10, AddressToCity);
				String newCity = contactInfo.substring(AddressToCity+7, CityToState);			
				int newState = Integer.parseInt(contactInfo.substring(CityToState+8, StateToZip));
				int newZip = Integer.parseInt(contactInfo.substring(StateToZip+6, ZipToPhone));
				String newPhone = contactInfo.substring(ZipToPhone+8, PhoneToFax);
				String newFax = contactInfo.substring(PhoneToFax+6, FaxToEmail);
				String newEmail = contactInfo.substring(FaxToEmail+8);
				
				JTextArea contactNotesTextArea = new JTextArea();
				try (FileReader contactNotesReader = new FileReader("src/resources/contacts/contact"+newAcct+"notes.txt"))
				{
					contactNotesTextArea.read(contactNotesReader, null);
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}	
				catch (IOException ee)
				{
					ee.printStackTrace();
				}
				contactList.add(new Contact(newAcct, newName, newTitle, newAddress, newCity, 
						newState, newZip, newPhone, newFax, newEmail, contactNotesTextArea));
			}
			fileScan.close();
			return (ArrayList<Contact>)contactList;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}	
		return null;
	}
	/**
	 * This method will search through the contactList that was 
	 * processed in the getContactList() method above. It looks at each 
	 * contact's account number, if it finds a number (starting at 1) that
	 * does not have a client assigned to it, it will return that int value.
	 * 
	 * @return available account, int
	 */
	public int findAvailableContactAccountNumber(ArrayList<Contact> pendingContactList)
	{
		int availableAccount = 1;
		boolean foundAvailability = false;
		boolean matchFound = false;
		while (foundAvailability == false)
		{
			for (Contact a: pendingContactList)
			{
				if (a.getAccountNumber() == availableAccount)
				{
					matchFound = true;
					break;
				}
			}
			if (matchFound == true)
			{
				availableAccount++;	
				matchFound = false;
			}
			else
			{
				foundAvailability = true;
			}
		}
		return availableAccount;
	}
	
	public String[] getStatesListIndex()
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
}

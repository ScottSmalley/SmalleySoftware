/**
 * This class is the master controller of the Smalley Software.
 * It instantiates the Loader class.
 * Calls for the Client List.
 * Can create a client and add it to the .txt file of the client data,
 * and add it to the clientList.
 * Can delete a client from the .txt file and the clientList.
 * Can edit the essential information for an existing client.
 * Can search the clientList by first or last name (partial name search available
 * by startingWith spelling)
 * Essentially, it's the middle man between the GUI and the Loader.
 * @author Scott Smalley
 * @email scottsmalley90@gmail.com
 * 
 */
package smalleysoftware;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

public class SmalleyControl 
{
	private List<Client> clientList;
	private List<Contact> contactList;
	Loader resourceLoader;
	
	//Constructor, instantiates the Loader class, and calls for
	//the client list, client contacts, and the public contacts. 
	public SmalleyControl() 
	{
		resourceLoader = new Loader();
		contactList = resourceLoader.loadContactList("Contacts.txt");		
		clientList = resourceLoader.loadClientList("Clients.txt");
		
		for (int i = 0; i < clientList.size(); i++)
		{
			clientList.get(i).setContactPersonalList(resourceLoader.loadClientContactPersonalList(clientList.get(i).getAccountNumber()));
		}
	}
	
	//CLIENTS
	
	/**
	 * Checks to see if any changes were made, then sends off
	 * the updated client to loader to be rewritten.
	 * @param editingClient
	 */
	public void editClient(Client editingClient)
	{
			clientList = resourceLoader.loadClientList("Clients.txt");
			boolean changeRequired = false;
			int posInClientList = searchClientListPos(editingClient.getAccountNumber());
			
			if (!clientList.get(posInClientList).getFname().equals(editingClient.getFname()))
			{
				clientList.get(posInClientList).setFname(editingClient.getFname());
				changeRequired = true;
			}
			if (!clientList.get(posInClientList).getLname().equals(editingClient.getLname()))
			{
				clientList.get(posInClientList).setLname(editingClient.getLname());
				changeRequired = true;
			}
			if (!clientList.get(posInClientList).getPhone().equals(editingClient.getPhone()))
			{
				clientList.get(posInClientList).setPhone(editingClient.getPhone());
				changeRequired = true;
			}
			if (!clientList.get(posInClientList).getEmail().equals(editingClient.getEmail()))
			{
				clientList.get(posInClientList).setEmail(editingClient.getEmail());
				changeRequired = true;
			}
			if (!clientList.get(posInClientList).getAddress().equals(editingClient.getAddress()))
			{
				clientList.get(posInClientList).setAddress(editingClient.getAddress());
				changeRequired = true;
			}
			if (!clientList.get(posInClientList).getCity().equals(editingClient.getCity()))
			{
				clientList.get(posInClientList).setCity(editingClient.getCity());
				changeRequired = true;
			}
			if (!(clientList.get(posInClientList).getStateIndex() == editingClient.getStateIndex()))
			{
				clientList.get(posInClientList).setStateIndex(editingClient.getStateIndex());
				changeRequired = true;
			}
			if (!(clientList.get(posInClientList).getZip() == editingClient.getZip()))
			{
				clientList.get(posInClientList).setZip(editingClient.getZip());
				changeRequired = true;
			}
			if (!(clientList.get(posInClientList).getAlertNotes() == editingClient.getAlertNotes()))
			{
				clientList.get(posInClientList).setAlertNotes(editingClient.getAlertNotes());
				changeRequired = true;
			}
			if (!(clientList.get(posInClientList).getClientActive() == editingClient.getClientActive()))
			{
				clientList.get(posInClientList).setClientActive(editingClient.getClientActive());
				changeRequired = true;
			}
			if (changeRequired)
			{
				resourceLoader.createNewClientFile("Clients.txt", (ArrayList<Client>) clientList);
			}
	}
	/**
	 * calls Loader to update the clientList with the new ArrayList<Client>
	 * @param contactList
	 * @return ArrayList<Clientt>
	 */
	public ArrayList<Client> editClientFile(ArrayList<Client> newClientList)
	{
		resourceLoader.createNewClientFile("Clients.txt", newClientList);
		this.clientList = resourceLoader.loadClientList("Clients.txt");
		return (ArrayList<Client>)clientList;
	}
	/**
	 * calls and returns an updated client Master List.
	 * @return ArrayList<Client>
	 */
	public ArrayList<Client> getClientMasterList()
	{
		clientList = resourceLoader.loadClientList("Clients.txt");
		return (ArrayList<Client>)clientList;
	}
	/**
	 * searches the clientList based on account number
	 * @param number
	 * @return position (int)
	 */
	public int searchClientListPos(int number)
	{
		for (int i = 0; i < clientList.size(); i++)
		{
			if (clientList.get(i).getAccountNumber() == number)
			{
				return i;
			}
		}
		return -1;
	}
	/**
	 * This method takes in a string, and searches for the 
	 * first name or last name of a Client in the clientList.
	 * It will check from the start of the fName or lName of
	 * the search conditions. It stores any results that match,
	 * and returns a new ArrayList<Client> with the results.
	 * 
	 * @param first or lastName, String
	 * @return client Array Results, ArrayList<Client>
	 */
	public ArrayList<Client> searchClientName(String name)
	{
		name = new String(name).toUpperCase();
		List<Client> searchClientList = new ArrayList<>();
		
		for (Client c: clientList)
		{
			if (c.getFname().startsWith(name) || c.getLname().startsWith(name))
			{
				searchClientList.add(c);
			}
		}
		return (ArrayList<Client>)searchClientList;
	}
	/**
	 * calls Loader to find an available account number in the ClientList.
	 * @param newClientList
	 * @return accountNumber (int)
	 */
	public int findAvailableClientAccountNumber(ArrayList<Client> newClientList)
	{
		return resourceLoader.findAvailableClientAccountNumber((ArrayList<Client>) newClientList);
	}
	/**
	 * Calls Loader to rewrite the Text Area for Client Notes based on the client acct Number
	 * @param acctNumber
	 * @param clientNotesTextArea
	 */
	public void editClientNotesTextArea(int acctNumber, JTextArea clientNotesTextArea)
	{
		resourceLoader.createNewClientNotesTextArea(acctNumber, clientNotesTextArea);
	}
	/**
	 * Returns the clientNotesTextArea
	 * @param acctNumber
	 * @return
	 */
	public JTextArea getClientNotesTextArea(int acctNumber)
	{
		return resourceLoader.loadClientNotesTextArea(acctNumber);
	}	
	/**
	 * rewrites the clientContactFile for a specific client.
	 * @param clientAcctNumber
	 * @param newClientContactList
	 * @param newClientContactROIList
	 */
	public void editClientContactFile(int clientAcctNumber, ArrayList<Integer> newClientContactList, ArrayList<Boolean> newClientContactROIList)
	{
		resourceLoader.createNewClientContactFile(clientAcctNumber, newClientContactList, newClientContactROIList);
	}		
	/**
	 * returns the clientContactList 
	 * @param acctList
	 * @return ArrayList<Contact>
	 */
 	public ArrayList<Contact> getClientContactList(ArrayList<Integer> acctList)
	{
		List<Contact> convertedClientContactList = new ArrayList<>();
		for (int i = 0; i < acctList.size(); i++)
		{
			convertedClientContactList.add(searchContactListAcct(acctList.get(i)));
		}
		return (ArrayList<Contact>)convertedClientContactList;
	}	
	/**
	 * rewrites the clientContactPersonalFile for a specific client.
	 * @param clientAcctNumber
	 * @param newClientContactPersonalList
	 * @param newClientContactPersonalROIList
	 */
	public void editClientContactPersonalFile(int clientAcctNumber, ArrayList<Contact> newClientContactPersonalList, ArrayList<Boolean> newClientContactPersonalROIList)
	{
		resourceLoader.createNewClientContactPersonalFile(clientAcctNumber, newClientContactPersonalList, newClientContactPersonalROIList);
	}
 	/**
	 * returns the clientContactPersonalList 
	 * @param acctList
	 * @return ArrayList<Contact>
	 */
	public ArrayList<Contact> getClientContactPersonalList(int acctNumber)
	{
		return resourceLoader.loadClientContactPersonalList(acctNumber);
	}	
	//returns a String[] of the statesList
	public String[] getStateListIndex()
	{
		return resourceLoader.getStatesListIndex();
	}
	/**
	 * deletes a public contact from a client Contact List
	 * @param removedContactAcct
	 */
	public void deletePublicContactFromClient(int removedContactAcct)
	{	
		for (Client a: clientList)
		{
			for (int i = 0; i < a.getContactList().size(); i++)
			{
				if (a.getContactList().get(i) == removedContactAcct)
				{
					a.getContactList().remove(i);
					a.getContactROIList().remove(i);
					editClientContactFile(a.getAccountNumber(), a.getContactList(), a.getContactROIList());
				}
			}
		}
	}	
 	
	//CONTACTS
	
	/**
	 * checks if the data has changed, then sends it off to loader to be
	 * rewritten.
	 * @param editingContact
	 */
	public void editContact(Contact editingContact)
	{
			contactList = resourceLoader.loadContactList("Contacts.txt");
			boolean changeRequired = false;
			int posInContactList = searchContactListPos(editingContact.getAccountNumber());
			
			if (!contactList.get(posInContactList).getName().equals(editingContact.getName()))
			{
				contactList.get(posInContactList).setName(editingContact.getName());
				changeRequired = true;
			}
			if (!contactList.get(posInContactList).getTitle().equals(editingContact.getTitle()))
			{
				contactList.get(posInContactList).setTitle(editingContact.getTitle());
				changeRequired = true;
			}
			if (!contactList.get(posInContactList).getAddress().equals(editingContact.getAddress()))
			{
				contactList.get(posInContactList).setAddress(editingContact.getAddress());
				changeRequired = true;
			}
			if (!contactList.get(posInContactList).getCity().equals(editingContact.getCity()))
			{
				contactList.get(posInContactList).setCity(editingContact.getCity());
				changeRequired = true;
			}
			if (!(contactList.get(posInContactList).getState() == editingContact.getState()))
			{
				contactList.get(posInContactList).setState(editingContact.getState());
				changeRequired = true;
			}
			if (!contactList.get(posInContactList).getCity().equals(editingContact.getCity()))
			{
				contactList.get(posInContactList).setCity(editingContact.getCity());
				changeRequired = true;
			}
			if (!(contactList.get(posInContactList).getZip() ==(editingContact.getZip())))
			{
				contactList.get(posInContactList).setZip(editingContact.getZip());
				changeRequired = true;
			}
			if (!contactList.get(posInContactList).getPhone().equals(editingContact.getPhone()))
			{
				contactList.get(posInContactList).setPhone(editingContact.getPhone());
				changeRequired = true;
			}
			if (!contactList.get(posInContactList).getFax().equals(editingContact.getFax()))
			{
				contactList.get(posInContactList).setFax(editingContact.getFax());
				changeRequired = true;
			}
			if (!contactList.get(posInContactList).getEmail().equals(editingContact.getEmail()))
			{
				contactList.get(posInContactList).setEmail(editingContact.getEmail());
				changeRequired = true;
			}
			if (!contactList.get(posInContactList).getNotes().equals(editingContact.getNotes()))
			{
				contactList.get(posInContactList).setNotes(editingContact.getNotes());
				changeRequired = true;
			}

			if (changeRequired)
			{
				resourceLoader.createNewContactFile("Contacts.txt", (ArrayList<Contact>) contactList);
			}
	}
	/**
	 * calls Loader to update the contactList with the new ArrayList<Contact>
	 * @param contactList
	 * @return ArrayList<Contact>
	 */
	public ArrayList<Contact> editContactFile(ArrayList<Contact> contactList)
	{
		resourceLoader.createNewContactFile("Contacts.txt", contactList);
		this.contactList = getContactMasterList();
		return resourceLoader.loadContactList("Contacts.txt");
	}
	/**
	 * Returns the ContactMasterList
	 * @return ArrayList<Contact>
	 */
	public ArrayList<Contact> getContactMasterList()
	{ 
		contactList = resourceLoader.loadContactList("Contacts.txt");
		return (ArrayList<Contact>) contactList;
	}
	/**
	 * searches the contactList based on account number
	 * @param number
	 * @return position (int)
	 */
	public int searchContactListPos(int number)
	{
		for (int i = 0; i < contactList.size(); i++)
		{
			if (contactList.get(i).getAccountNumber() == number)
			{
				return i;
			}
		}
		return -1;
	}
	/**
	 * searches the contactList based on account number
	 * @param number
	 * @return Contact
	 */
	public Contact searchContactListAcct(int number)
	{
		for (int i = 0; i < contactList.size(); i++)
		{
			if (contactList.get(i).getAccountNumber() == number)
			{
				return contactList.get(i);
			}
		}
		return null;
	}
	/**
	 * This method takes in a string, and searches for the 
	 * name of a Contact in the contactMasterList.
	 * It stores any results that match,
	 * and returns a new ArrayList<Contact> with the results.
	 * 
	 * @param name String
	 * @return contact Array Results, ArrayList<Contact>
	 */
	public ArrayList<Contact> searchContactName(String name)
	{
		name = new String(name).toUpperCase();
		List<Contact> searchContactList = new ArrayList<>();
		for (Contact c: contactList)
		{
			if (c.getName().startsWith(name))
			{
				searchContactList.add(c);
			}
		}
		return (ArrayList<Contact>)searchContactList;
	}
	/**
	 * calls Loader to find an available account number in the ContactList.
	 * @param newContactList
	 * @return accountNumber (int)
	 */
	public int findAvailableContactAccountNumber(ArrayList<Contact> pendingContactList)
	{
		return resourceLoader.findAvailableContactAccountNumber(pendingContactList);
	}	
}

package smalleysoftware;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

public class SmalleyGUI  extends JPanel implements Runnable, MouseListener, KeyListener, MouseMotionListener, ActionListener
{
	//Overall GUI/Objects
    private static final long serialVersionUID = 42L;
    private SmalleyControl masterControl;
    private JTextField searchBar;
    private JFrame window;
	private JPanel top, center, glassPane, bottom;
    private JButton searchStart;
	private Color highlight = new Color(17, 55, 192);
	private Color lightContent = new Color(12, 36, 126);
	private Color darkContent = new Color(6, 24, 89);
	private Color background = new Color (2, 11, 46);
	private Color textColor = Color.LIGHT_GRAY;
	private String[] statesList;
	
	
    //QuickMenu 
	private QuickMenu quickMenu;
	private Point quickMenuLocation, mousePos;
	private boolean wasQuickMenuButtonPressed, wasKeyPressedBefore;
	private int quickMenuSelect, quickMenuSelectPaintFlag;
	
	
    //Client Content Builder
	private JButton addRemoveClientButton, contactsMainButton;	
	private List<Client> searchResults;
	private List<JPanel> searchResultsGUI;
	private int selectedClientPos;
		
	
	//Client Info Panel
	private JPanel alertPanel;
	private JTextField firstNameField, lastNameField, addressField, cityField, 
						zipField, phoneField, emailField, alertField;
	private JComboBox<String> stateComboBox;
	private JRadioButton isClientActiveButton;
	private JButton editClientInfoButton, exitClientInfoButton;
	private List<JButton> clientContactMoreButtonList;
	private String currentSearchData;
	
		//Client Info Panel - Edit Client Raw Info	
		private JPanel clientInfoPanelButtons;
		private JButton saveInfoButton, cancelInfoButton;
		private boolean isEditingClientInfo;
		
		//Client Info Panel - Edit Client Notes
		private JPanel notesButtonsPanel;
		private JButton editNotesClientButton, saveEditNotesClientButton, cancelEditNotesClientButton, 
						addRemoveContactClientButton, exitAddRemoveClientContactButton;	
		private JTextArea notesTextArea;
		private JScrollPane notesScrollPane;
		
		//Client Info Panel - Edit Client Contacts	
		private JPanel addClientContactPanel, removeClientContactPanel, addRemoveClientContactPanel;
		private JLabel removeClientContactLabel;
		private JTextField nameClientContactPersonalField, titleClientContactPersonalField, faxClientContactPersonalField, 
							searchContactPublicTextField, clientContactName, clientContactTitle;
		private JButton searchContactPublicStartButton, searchContactCreatePersonalButton, editRoiStatusButton, addRemoveClientContactRemoveButton, 
						addRemoveClientContactPersonalSave, addRemoveClientContactPersonalCancel, addClientContactSearchResultsSave, addClientContactSearchResultsCancel;
		private JTextArea addNewClientContactPersonalNotesTextArea;	
		private JScrollPane removeClientContactScrollPane;	
		private GridBagConstraints addRemoveClientContactPanelConstraints;
		private List<JPanel> clientInfoContactsGUI, removeClientContactGUI, addClientContactGUI;
		private List<Contact> addRemoveClientContactSearchResults;
		private List<JRadioButton> removeClientContactListButtonList, editROIButtonList, addClientContactListButtonList;
		private int addClientContactSearchResultPos, selectedClientPersonalContactPos;
		private boolean editPersonalContact;
	
		
	//AddRemoveClient Panel
	private JButton createNewClientButton, deleteExistingClientButton, createClientExitButton;
	private List<Client> clientMasterList;
    private List<JPanel> clientMasterListGUI;
	
	    //AddRemoveClient Panel - Create a Client
		private JTextField fNameClientField, lNameClientField;
		private JTextArea createNewClientNotesTextArea;
		private JButton createNewClientSaveButton, createNewClientCancelButton;
		
	    //AddRemoveClient Panel - Delete a Client
		private int selectedClientToDeletePos;	
	
		
    //Contacts Public
	private JTextField nameContactPublicField, titleContactPublicField, faxContactPublicField;
	private JButton exitContactPublicButton;
	private JTextArea addNewContactPublicNotesTextArea, contactPublicNotesTextArea;
	private JRadioButton selectionContactPublicButton;
	private List<Contact> contactMasterList;
	private List<JPanel> contactMasterListGUI;
	private List<JButton> contactPublicMoreInfoButtonList;
	private int selectedContactPublicPos;
	
		//Contacts Public - Add a Contact
		private JTextField nameContactPublic, titleContactPublic, phoneContactPublic, 
		faxContactPublic, emailContactPublic;
		private JButton addContactPublicButton, addContactPublicSaveButton, addContactPublicCancelButton;
		
		//Contacts Public - Edit a Contact
		private JButton editContactPublicButton, editContactPublicSaveButton, editContactPublicCancelButton;
		
		//Contacts Public - Delete a Contact
		private JButton deleteContactPublicButton;
	
	public static void main (String [] args)
	{
		SwingUtilities.invokeLater(new SmalleyGUI());
	}

	public void run() 
	{
		// Default smallest size of the application
		Dimension panelSize = new Dimension(800,600); 
		
		window = new JFrame("Scott Smalley Software");				
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setPreferredSize(panelSize);
		window.setMinimumSize(panelSize);
		
		//SmalleyGUI becoming the ContentPane
		window.setContentPane(this);								
		window.setLayout(new BorderLayout());
		
		//Area assigned for the Searchbar and SearchButton
		top = new JPanel();										
		top.setBackground(background);
		top.setForeground(Color.WHITE);
		this.add(top, BorderLayout.NORTH);
		
		//JPanel for the Search Components
		JPanel searchPanel = new JPanel();												
		searchPanel.setBackground(background);
		searchPanel.setForeground(Color.WHITE);
		top.add(searchPanel);
		
		//Search Field
		searchBar = new JTextField(50);	
		searchBar.setBackground(darkContent);
		searchBar.setForeground(textColor);
		searchBar.setEditable(true);
		searchPanel.add(searchBar);
		
		//Search Start Button
		searchStart = new JButton("Search");	
		searchStart.setSize(new Dimension(25, 25));
		searchStart.setBackground(darkContent);
		searchStart.setForeground(textColor);
		searchPanel.add(searchStart);
		
		//Main center area of the application
		center = new JPanel();													
		center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
		center.setBackground(background);
		center.setForeground(textColor);
		this.add(center, BorderLayout.CENTER);
		
		//Area assigned for buttons mostly. 
		//At the bottom portion of the application
		bottom = new JPanel();
		bottom.setBackground(background);
		
		
		addRemoveClientButton = new JButton("Add/Remove a Client");
		addRemoveClientButton.setMinimumSize(new Dimension(window.getWidth()/5, 35));
		addRemoveClientButton.setPreferredSize(new Dimension(window.getWidth()/5, 35));
		addRemoveClientButton.setBackground(darkContent);
		addRemoveClientButton.setForeground(textColor);
		bottom.add(addRemoveClientButton);
		
		contactsMainButton = new JButton("Contacts");
		contactsMainButton.setMinimumSize(new Dimension(window.getWidth()/6, 35));
		contactsMainButton.setPreferredSize(new Dimension(window.getWidth()/6, 35));
		contactsMainButton.setBackground(darkContent);
		contactsMainButton.setForeground(textColor);
		bottom.add(contactsMainButton);
		
		this.add(bottom, BorderLayout.SOUTH);
		
		window.pack();
		
		//Giving the window JFrame a WindowListener for future
		//endeavors into re-adjusting Layout Managers for component sizing
		//instead of hard-coding size.
		window.setLocationRelativeTo(null);
		window.addWindowStateListener(windowListener);
		window.setVisible(true);
		
		//SmalleyControl is my middle man between the GUI class
		//and the Loader class. It handles the conversion of 
		//requests from GUI to add/delete/edit files.
		masterControl = new SmalleyControl();
		
		//glassPane is used for the QuickMenu to silence 
		//the listeners in the menu and to paint the QuickMenu
		glassPane = (JPanel)window.getGlassPane();
		glassPane.setLayout(new BorderLayout());
	
		quickMenu = new QuickMenu(glassPane);
		glassPane.add(quickMenu);
		
		//ArrayList<Client> - master list of all clients.
		clientMasterList = new ArrayList<>();
		
		//ArrayList<JPanel> - for GUI related actions, like selections.
		clientMasterListGUI = new ArrayList<>();
		
		//ArrayList<Client>-for actual client data that has been returned from
		//searching.
		searchResults = new ArrayList<>();
		
		//ArrayList<JPanel>-for GUI related actions, like selections.
		searchResultsGUI = new ArrayList<>();
		
		//ArrayList<Contact>-overall master list of all the public contacts
		contactMasterList = new ArrayList<>();
		
		//ArrayList<JPanel>-for GUI related actions, like selections.
		contactMasterListGUI = new ArrayList<>();

		//ArrayList<JRadioButton> - list of the GUI buttons for finding which was selected.
		removeClientContactListButtonList = new ArrayList<>();
		
		//ArrayList<JPanel> - for GUI related actions, like selections.
		removeClientContactGUI = new ArrayList<>();
		
		//ArrayList<JPanel> - for GUI related actions, like selections.
		clientInfoContactsGUI = new ArrayList<>();
		
		//ArrayList<JButton> - list of buttons related to additional information
		//about a public contact.
		contactPublicMoreInfoButtonList = new ArrayList<>();
		//ArrayList<JRadioButton> - list of buttons to determine which ClientContact
		//is being requested to be removed.
		addRemoveClientContactSearchResults = new ArrayList<>();
		
		//ArrayList<JButton> - list of buttons related to additional information
		//about a client's contacts - personal or public.
		clientContactMoreButtonList = new ArrayList<>();
		
		//ArrayList<JPanel> - for GUI related actions, like selections.
		addClientContactGUI = new ArrayList<>();
		
		//ArrayList<JRadioButton> - to determine which contact 
		//is being selected to add to a client's contact list.
		addClientContactListButtonList = new ArrayList<>();
		
		//ArrayList<JRadioButton> - list of buttons to determine
		//which of the client's contacts are listed on the
		//client's R.O.I. 
		//(Release of Information-legal form allowing people access to the client's information at a HIPAA related facility)
		editROIButtonList = new ArrayList<>();
		
		searchBar.addMouseListener(this);
		searchBar.setCaretColor(Color.WHITE);
		searchBar.addActionListener(this);
		
		searchStart.addMouseListener(this);
		
		addRemoveClientButton.addMouseListener(this);
		contactsMainButton.addMouseListener(this);
		
		wasKeyPressedBefore  = false;
		wasQuickMenuButtonPressed = false;
		isEditingClientInfo = false;
		quickMenuSelectPaintFlag = -1;
		selectedClientPos = -1;
		quickMenuSelect = 0;
		selectedContactPublicPos = -1;
		selectedClientToDeletePos = -1;
		addClientContactSearchResultPos = -1;
		selectedClientPersonalContactPos = -1;
		statesList = masterControl.getStateListIndex();
		
		//These are for the TextFields, TextAreas, and Alert messages for coloring.
		UIManager.put("OptionPane.background", new ColorUIResource(6, 24, 89));
		UIManager.put("Panel.background", new ColorUIResource(6, 24, 89));
		UIManager.put("TextField.caretForeground", new ColorUIResource(Color.WHITE));
		UIManager.put("TextArea.caretForeground", new ColorUIResource(Color.WHITE));
	}

	/**
 * Takes in an ArrayList of Clients.
 * Displays them into a readable GUI in the center of the
 * content pane.
 * @param searchResults (ArrayList<Client>)
 * @return None
 */
	public void clientSearchResults(ArrayList<Client> searchResults)
	{	
		center.removeAll();
		bottom.removeAll();
		top.setVisible(true);
		searchBar.addMouseListener(this);
		searchBar.addActionListener(this);
		searchBar.setEnabled(true);
		searchBar.setVisible(true);
		searchStart.addMouseListener(this);
		searchStart.setEnabled(true);
		searchStart.setVisible(true);
		searchResultsGUI.clear();
		quickMenuSelectPaintFlag = 1;
		selectedClientPos = -1;
	
		JPanel clientSearchResults = new JPanel();
		clientSearchResults.setLayout(new GridBagLayout());
		clientSearchResults.setBackground(background);
		clientSearchResults.setForeground(textColor);
		GridBagConstraints clientSearchResultsConstraints = new GridBagConstraints();
		
		
		JPanel columnTitles = new JPanel();
		columnTitles.setLayout(new GridLayout());
		columnTitles.setMaximumSize(new Dimension(center.getWidth()-25, 35));
		columnTitles.setPreferredSize(new Dimension(center.getWidth()-25, 35));
		
		JLabel acctColumnTitle = new JLabel("Account");
		acctColumnTitle.setBackground(lightContent);
		acctColumnTitle.setForeground(textColor);
		columnTitles.add(acctColumnTitle);
		
		JLabel nameColumnTitle = new JLabel("Name");
		nameColumnTitle.setBackground(lightContent);
		nameColumnTitle.setForeground(textColor);
		columnTitles.add(nameColumnTitle);
		
		JLabel alertColumnTitle = new JLabel("Alert");
		alertColumnTitle.setBackground(lightContent);
		alertColumnTitle.setForeground(textColor);
		columnTitles.add(alertColumnTitle);
		
		JLabel activeColumnTitle = new JLabel("Active");
		activeColumnTitle.setBackground(lightContent);
		activeColumnTitle.setForeground(textColor);
		columnTitles.add(activeColumnTitle);

		columnTitles.setOpaque(true);
		columnTitles.setBackground(lightContent);
		columnTitles.setForeground(textColor);

		Boolean colorSwitch = true;
		int numberOfClientsCounter = 0;
		for (int i = 0; i < searchResults.size(); i++)
		{		
			JPanel searchResultsDisplayLinePanel = new JPanel();
			searchResultsDisplayLinePanel.setLayout(new GridLayout());
			searchResultsDisplayLinePanel.setMaximumSize(new Dimension(center.getWidth()-25, 35));
			searchResultsDisplayLinePanel.setPreferredSize(new Dimension(center.getWidth()-25, 35));
			if (colorSwitch)
			{
				searchResultsDisplayLinePanel.setBackground(darkContent);
				searchResultsDisplayLinePanel.setForeground(textColor);
				colorSwitch = false;
			}
			else
			{
				searchResultsDisplayLinePanel.setBackground(lightContent);
				searchResultsDisplayLinePanel.setForeground(textColor);
				colorSwitch = true;
			}
			JLabel acctNumberLabel = new JLabel(Integer.toString(searchResults.get(i).getAccountNumber()));
			acctNumberLabel.setBackground(searchResultsDisplayLinePanel.getBackground());
			acctNumberLabel.setForeground(searchResultsDisplayLinePanel.getForeground());
			searchResultsDisplayLinePanel.add(acctNumberLabel);
			
			JLabel nameLabel = new JLabel(searchResults.get(i).getNameFull());
			nameLabel.setBackground(searchResultsDisplayLinePanel.getBackground());
			nameLabel.setForeground(searchResultsDisplayLinePanel.getForeground());
			searchResultsDisplayLinePanel.add(nameLabel);
			
			JLabel alertLabel = new JLabel(searchResults.get(i).getAlertNotes());
			alertLabel.setBackground(searchResultsDisplayLinePanel.getBackground());
			alertLabel.setForeground(searchResultsDisplayLinePanel.getForeground());
			searchResultsDisplayLinePanel.add(alertLabel);

			JLabel activeLabel = new JLabel();
			activeLabel.setBackground(searchResultsDisplayLinePanel.getBackground());
			activeLabel.setForeground(searchResultsDisplayLinePanel.getForeground());
			if (searchResults.get(i).getClientActive())
			{
				activeLabel.setText("Active");
			}
			else 
			{
				activeLabel.setText("Inactive");
			}
			searchResultsDisplayLinePanel.add(activeLabel);
			searchResultsDisplayLinePanel.addKeyListener(this);
			searchResultsDisplayLinePanel.addMouseListener(this);
			searchResultsDisplayLinePanel.addMouseMotionListener(this);
			searchResultsDisplayLinePanel.setOpaque(true);

			searchResultsGUI.add(searchResultsDisplayLinePanel);
			numberOfClientsCounter++;
		}
		if ((numberOfClientsCounter > 11 && center.getHeight() < 800) || (numberOfClientsCounter > 26 && center.getHeight() > 800))
		{
			clientSearchResultsConstraints.gridx = 0;
			clientSearchResultsConstraints.gridy = 0;
			clientSearchResultsConstraints.weightx = 1.0;
			clientSearchResultsConstraints.weighty = 1.0;
			clientSearchResults.add(columnTitles, clientSearchResultsConstraints);

			for (int i = 0; i < searchResultsGUI.size(); i++)
			{
				clientSearchResultsConstraints.gridx = 0;
				clientSearchResultsConstraints.gridy = i+1;
				clientSearchResultsConstraints.weightx = 1.0;
				clientSearchResultsConstraints.weighty = 1.0;
				clientSearchResults.add(searchResultsGUI.get(i), clientSearchResultsConstraints);
			}
			
			JScrollPane clientSearchResultsScrollPane = new JScrollPane(clientSearchResults,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			clientSearchResultsScrollPane.getVerticalScrollBar().setUnitIncrement(8);
			clientSearchResultsScrollPane.setBackground(background);
			clientSearchResultsScrollPane.setForeground(textColor);
			clientSearchResultsScrollPane.setBorder(BorderFactory.createEmptyBorder());
			center.add(clientSearchResultsScrollPane);
		}
		else 
		{
			columnTitles.setMaximumSize(new Dimension(center.getWidth()-25, 35));
			columnTitles.setPreferredSize(new Dimension(center.getWidth()-25, 35));
			center.add(columnTitles);
			for (int i = 0; i < searchResultsGUI.size(); i++)
			{
				searchResultsGUI.get(i).setMaximumSize(new Dimension(center.getWidth()-25, 35));
				searchResultsGUI.get(i).setPreferredSize(new Dimension(center.getWidth()-25, 35));
				center.add(searchResultsGUI.get(i));
			}
			int amountOfFillerSlots = 0;
			if (numberOfClientsCounter <= 11 && center.getHeight() < 800)
			{
				amountOfFillerSlots = 11;
			}
			else
			{
				amountOfFillerSlots = 26;
			}
			for (int i = 0; i < amountOfFillerSlots-searchResults.size(); i ++)
			{
				JPanel fillerPanel = new JPanel();
				fillerPanel.setLayout(new GridLayout());
				fillerPanel.setMaximumSize(new Dimension(center.getWidth()-25, 35));
				fillerPanel.setPreferredSize(new Dimension(center.getWidth()-25, 35));
				
				if (colorSwitch)
				{
					fillerPanel.setBackground(darkContent);
					fillerPanel.setForeground(textColor);
					colorSwitch = false;
				}
				else
				{
					fillerPanel.setBackground(lightContent);
					fillerPanel.setForeground(textColor);
					colorSwitch = true;
				}
				center.add(fillerPanel);
			}
		}
		this.addMouseMotionListener(this);
		
		addRemoveClientButton.setMinimumSize(new Dimension(center.getWidth()/5, 35));
		addRemoveClientButton.setPreferredSize(new Dimension(center.getWidth()/5, 35));
		bottom.add(addRemoveClientButton);
		
		contactsMainButton.setMinimumSize(new Dimension(center.getWidth()/6, 35));
		contactsMainButton.setPreferredSize(new Dimension(center.getWidth()/6, 35));
		bottom.add(contactsMainButton);
		
		revalidate();	
		repaint();
	}
	
/**
 * Takes in the JPanel that was selected from the clientSearchResults.
 * Repaints the selected panel and components to be the highlighted color.
 * Repaints all the other JPanels in clientSearchResults into default colors.
 * @param newSelectedClientPanel (JPanel)
 * @return None
 */
	public void clientSearchResultsSelector(JPanel newSelectedClientPanel)
	{
		for (int i = 0; i < searchResults.size(); i++)
		{
			if (searchResultsGUI.get(i).getBackground().equals(highlight))
			{
				searchResultsGUI.get(i).setOpaque(true);
				if (i % 2 == 0)
				{
					searchResultsGUI.get(i).setBackground(darkContent);
					searchResultsGUI.get(i).setForeground(textColor);
				}
				else
				{
					searchResultsGUI.get(i).setBackground(lightContent);
					searchResultsGUI.get(i).setForeground(textColor);
				}
			}
			else
			{
				String convertedAcctNumber = Integer.toString(searchResults.get(i).getAccountNumber());
				JLabel checkAcctNumber = (JLabel)newSelectedClientPanel.getComponent(0);
				
				if (checkAcctNumber.getText().equals(convertedAcctNumber))
				{
					searchResultsGUI.get(i).setBackground(highlight);
					searchResultsGUI.get(i).requestFocusInWindow();
					selectedClientPos = i;
				}	
			}
		}
		
		revalidate();
		repaint();
	}
/**
 * This function determines when the QuickMenu has been pressed,
 * which button of the four was selected.
 */
	public void quickMenuButtonSelection()
	{
		//for the clientSearchResults GUI
		if (quickMenuSelectPaintFlag == 1)
		{
			//Launches Edit Client Info
			if ((getMousePos().x > quickMenuLocation.x-76 && getMousePos().x < quickMenuLocation.x)
					&& (getMousePos().y > quickMenuLocation.y-76 && getMousePos().y < quickMenuLocation.y))
			{
				wasQuickMenuButtonPressed = true;
				quickMenuSelect = 1;
			}
			
			//THIS COMMENTED CODE IS FOR FUTURE USE, WHEN MORE FUNCTIONALITY IS IMPLEMENTED INTO THE 
			//QUICK MENU.
			
	////second button (Top Right)
//			else if ((getMousePos().x > quickMenuLocation.x && getMousePos().x < quickMenuLocation.x+83)
//					&& (getMousePos().y > quickMenuLocation.y-76 && getMousePos().y < quickMenuLocation.y))
//			{
//				System.out.println("2nd button");
//				wasQuickMenuButtonPressed = true;
//				quickMenuSelect = 2;
//			}
	////third button (Bottom Left)
//			else if ((getMousePos().x > quickMenuLocation.x-76 && getMousePos().x < quickMenuLocation.x)
//					&& (getMousePos().y > quickMenuLocation.y && getMousePos().y < quickMenuLocation.y+83))
//			{
//				System.out.println("3rd button");
//				wasQuickMenuButtonPressed = true;
//				quickMenuSelect = 3;
//			}
	////fourth button (Bottom Right)
//			else if ((getMousePos().x > quickMenuLocation.x && getMousePos().x < quickMenuLocation.x+83)
//					&& (getMousePos().y > quickMenuLocation.y && getMousePos().y < quickMenuLocation.y+83))
//			{
//				System.out.println("4th button");
//				wasQuickMenuButtonPressed = true;
//				quickMenuSelect = 4;
//			}
			else
			{
				//If none of the button areas were selected last, it will
				//reset the selection variables so nothing happens.
				wasQuickMenuButtonPressed = false;
				quickMenuSelect = 0;
			}
		}
		//Client Info Screen 
		else if (quickMenuSelectPaintFlag == 2)
		{
			//Initiates editing to the Client Raw Information.
			if ((getMousePos().x > quickMenuLocation.x-76 && getMousePos().x < quickMenuLocation.x)
					&& (getMousePos().y > quickMenuLocation.y-76 && getMousePos().y < quickMenuLocation.y))
			{
				wasQuickMenuButtonPressed = true;
				quickMenuSelect = 1;
			}
			//Initiates editing to the Client Notes.
			else if ((getMousePos().x > quickMenuLocation.x && getMousePos().x < quickMenuLocation.x+83)
					&& (getMousePos().y > quickMenuLocation.y-76 && getMousePos().y < quickMenuLocation.y))
			{
				wasQuickMenuButtonPressed = true;
				quickMenuSelect = 2;
			}
			//Returns to the main menu.
			else if ((getMousePos().x > quickMenuLocation.x-76 && getMousePos().x < quickMenuLocation.x)
					&& (getMousePos().y > quickMenuLocation.y && getMousePos().y < quickMenuLocation.y+83))
			{
				wasQuickMenuButtonPressed = true;
				quickMenuSelect = 3;
			}
			//Opens the Edit Client Contacts menu.
			else if ((getMousePos().x > quickMenuLocation.x && getMousePos().x < quickMenuLocation.x+83)
					&& (getMousePos().y > quickMenuLocation.y && getMousePos().y < quickMenuLocation.y+83))
			{
				wasQuickMenuButtonPressed = true;
				quickMenuSelect = 4;
			}
			else
			{
				//No buttons were selected, reset variables.
				wasQuickMenuButtonPressed = false;
				quickMenuSelect = 0;
			}
		}
	} 
	
	/**
	 *Opens the Client Information screen.
	 *This contains the client's raw information,
	 *access to edit this information.
	 *Also contains the client's notes,
	 *access to edit this information.
	 *Finally contains the Client's contacts
	 *personal(Family members, friends)
	 *and public(Courts, Judges, Lawyers, 
	 *Attorneys, Probation Officers, etc.
	 *  
	 */
	public void clientInfoButton()
	{
		//disable search capability
		searchBar.removeMouseListener(this);
		searchBar.removeActionListener(this);
		searchBar.setEnabled(false);
		searchBar.setVisible(false);
		
		searchStart.removeMouseListener(this);
		searchStart.setEnabled(false);
		searchStart.setVisible(false);
		
		center.removeAll();
		bottom.removeAll();
		clientContactMoreButtonList.clear();
		clientInfoContactsGUI.clear();
		editROIButtonList.clear();
		wasQuickMenuButtonPressed = false;
		quickMenuSelectPaintFlag = 2;
		
		//Overall JPanel that is added into the center JPanel
		JPanel clientInfoPanel = new JPanel();
		clientInfoPanel.setLayout(new GridBagLayout());
		clientInfoPanel.setBackground(background);
		GridBagConstraints clientInfoConstraints = new GridBagConstraints();
		
		//Client's Raw information listed
		JPanel clientRawInfoPanel = new JPanel();
		clientRawInfoPanel.setLayout(new GridBagLayout());
		clientRawInfoPanel.setMaximumSize(new Dimension(center.getWidth()-25, center.getHeight()));
		clientRawInfoPanel.setPreferredSize(new Dimension(center.getWidth()-25, center.getHeight()));
		clientRawInfoPanel.setBackground(background);
		clientRawInfoPanel.addKeyListener(this);
		clientRawInfoPanel.requestFocusInWindow();
		GridBagConstraints clientRawConstraints = new GridBagConstraints();
		
		//First/Last Name
		JPanel namePanel = new JPanel();
		namePanel.addMouseMotionListener(this);
		namePanel.setBackground(background);
		
		JLabel nameLabel = new JLabel("Name: ");
		nameLabel.setForeground(textColor);
		nameLabel.setBackground(background);
		nameLabel.addMouseMotionListener(this);
		namePanel.add(nameLabel);
		
		firstNameField = new JTextField(searchResults.get(selectedClientPos).getFname());
		firstNameField.setMinimumSize(new Dimension(200, 25));
		firstNameField.setPreferredSize(new Dimension(200, 25));
		firstNameField.setCaretPosition(0);
		firstNameField.setEditable(false);
		firstNameField.setBackground(darkContent);
		firstNameField.setForeground(textColor);
		firstNameField.addMouseMotionListener(this);
		namePanel.add(firstNameField);
		
		lastNameField = new JTextField(searchResults.get(selectedClientPos).getLname());
		lastNameField.setMinimumSize(new Dimension(200, 25));
		lastNameField.setPreferredSize(new Dimension(200, 25));
		lastNameField.setCaretPosition(0);
		lastNameField.setEditable(false);
		lastNameField.setBackground(darkContent);
		lastNameField.setForeground(textColor);
		lastNameField.addMouseMotionListener(this);
		namePanel.add(lastNameField);
				
		clientRawConstraints.gridx = 0;
		clientRawConstraints.gridy = 0;
		clientRawInfoPanel.add(namePanel, clientRawConstraints);
		
		//Client Phone/Email
		JPanel phoneEmailPanel = new JPanel();
		phoneEmailPanel.addMouseMotionListener(this);
		phoneEmailPanel.setBackground(background);
		
		JLabel phoneLabel = new JLabel("Phone: ");
		phoneLabel.setBackground(background);
		phoneLabel.setForeground(textColor);
		phoneLabel.addMouseMotionListener(this);
		phoneEmailPanel.add(phoneLabel);
		
		phoneField = new JTextField(searchResults.get(selectedClientPos).getPhone());
		phoneField.setMinimumSize(new Dimension(100, 25));
		phoneField.setPreferredSize(new Dimension(100, 25));
		phoneField.setCaretPosition(0);
		phoneField.setEditable(false);
		phoneField.setBackground(darkContent);
		phoneField.setForeground(textColor);
		phoneField.addMouseMotionListener(this);
		phoneEmailPanel.add(phoneField);
		
		JLabel emailLabel = new JLabel("Email: ");
		emailLabel.setForeground(textColor);
		emailLabel.addMouseMotionListener(this);
		phoneEmailPanel.add(emailLabel);
		
		emailField = new JTextField(searchResults.get(selectedClientPos).getEmail());
		emailField.setMinimumSize(new Dimension(250, 25));
		emailField.setPreferredSize(new Dimension(250, 25));
		emailField.setCaretPosition(0);
		emailField.setEditable(false);
		emailField.setBackground(darkContent);
		emailField.setForeground(textColor);
		emailField.addMouseMotionListener(this);
		phoneEmailPanel.add(emailField);
		
		clientRawConstraints.gridx = 0;
		clientRawConstraints.gridy = 1;
		clientRawInfoPanel.add(phoneEmailPanel, clientRawConstraints);
		
		//Street Address
		JPanel addressPanelOne = new JPanel();
		addressPanelOne.addMouseMotionListener(this);
		addressPanelOne.setBackground(background);
		
		JLabel addressLabel = new JLabel("Address: ");
		addressLabel.setBackground(background);
		addressLabel.setForeground(textColor);
		addressLabel.addMouseMotionListener(this);
		addressPanelOne.add(addressLabel);
		
		addressField = new JTextField(searchResults.get(selectedClientPos).getAddress());
		addressField.setMinimumSize(new Dimension(300, 25));
		addressField.setPreferredSize(new Dimension(300, 25));
		addressField.setCaretPosition(0);
		addressField.setEditable(false);
		addressField.setBackground(darkContent);
		addressField.setForeground(textColor);
		addressField.addMouseMotionListener(this);
		addressPanelOne.add(addressField);
				
		clientRawConstraints.gridx = 0;
		clientRawConstraints.gridy = 2;
		clientRawInfoPanel.add(addressPanelOne, clientRawConstraints);
		
		//City, State, Zip
		JPanel addressPanelTwo = new JPanel();
		addressPanelTwo.addMouseMotionListener(this);
		addressPanelTwo.setBackground(background);
		
		JLabel cityLabel = new JLabel("City: ");
		cityLabel.setForeground(textColor);
		cityLabel.addMouseMotionListener(this);
		addressPanelTwo.add(cityLabel);
		
		cityField = new JTextField(searchResults.get(selectedClientPos).getCity());
		cityField.setMinimumSize(new Dimension(100, 25));
		cityField.setPreferredSize(new Dimension(100, 25));
		cityField.setEditable(false);
		cityField.setBackground(darkContent);
		cityField.setForeground(textColor);
		cityField.addMouseMotionListener(this);
		addressPanelTwo.add(cityField);
		
		JLabel stateLabel = new JLabel("State: ");
		stateLabel.setForeground(textColor);
		stateLabel.addMouseMotionListener(this);
		addressPanelTwo.add(stateLabel);
		
		stateComboBox = new JComboBox<String>(statesList);
		stateComboBox.setEditable(false);
		stateComboBox.setEnabled(false);
		stateComboBox.setSelectedIndex(searchResults.get(selectedClientPos).getStateIndex());
		stateComboBox.setBackground(darkContent);
		stateComboBox.setForeground(textColor);
		stateComboBox.addMouseListener(this);
		stateComboBox.addMouseMotionListener(this);
		addressPanelTwo.add(stateComboBox);
		
		JLabel zipLabel = new JLabel("Zip: ");
		zipLabel.setForeground(textColor);
		zipLabel.addMouseMotionListener(this);
		addressPanelTwo.add(zipLabel);
		
		String convertedZip = Integer.toString(searchResults.get(selectedClientPos).getZip());
		zipField = new JTextField(convertedZip);
		zipField.setMinimumSize(new Dimension(75, 25));
		zipField.setPreferredSize(new Dimension(75, 25));
		zipField.setEditable(false);
		zipField.setBackground(darkContent);
		zipField.setForeground(textColor);
		zipField.addMouseMotionListener(this);
		addressPanelTwo.add(zipField);
				
		clientRawConstraints.gridx = 0;
		clientRawConstraints.gridy = 3;
		clientRawInfoPanel.add(addressPanelTwo, clientRawConstraints);
		
		//Alert Notes & Client Active toggle
		alertPanel = new JPanel();
		alertPanel.addMouseMotionListener(this);
		alertPanel.setBackground(background);
		
		JLabel alertLabel = new JLabel("Alert: ");
		alertLabel.setForeground(textColor);
		alertLabel.addMouseMotionListener(this);
		alertPanel.add(alertLabel);
		
		alertField = new JTextField(searchResults.get(selectedClientPos).getAlertNotes());
		alertField.setMinimumSize(new Dimension(200, 25));
		alertField.setPreferredSize(new Dimension(200, 25));
		alertField.setCaretPosition(0);
		alertField.setEditable(false);
		alertField.setBackground(darkContent);
		alertField.setForeground(textColor);
		alertField.addMouseMotionListener(this);
		alertPanel.add(alertField);
		
		isClientActiveButton = new JRadioButton("Active");
		isClientActiveButton.setBackground(background);
		isClientActiveButton.setForeground(textColor);
		isClientActiveButton.setSelected(searchResults.get(selectedClientPos).getClientActive());
		isClientActiveButton.setEnabled(false);
		isClientActiveButton.addMouseMotionListener(this);
		alertPanel.add(isClientActiveButton);
		
		clientRawConstraints.gridx = 0;
		clientRawConstraints.gridy = 4;
		clientRawInfoPanel.add(alertPanel, clientRawConstraints);
		
		//Edit Client Info Button(s)
		clientInfoPanelButtons = new JPanel();
		clientInfoPanelButtons.setBackground(background);
		clientInfoPanelButtons.addMouseMotionListener(this);
		clientInfoPanelButtons.setLayout(new GridLayout());
		
		editClientInfoButton = new JButton ("Edit Information");
		editClientInfoButton.setPreferredSize(new Dimension(center.getWidth()/6, 35));
		editClientInfoButton.setMinimumSize(new Dimension(center.getWidth()/6, 35));
		editClientInfoButton.setBackground(darkContent);
		editClientInfoButton.setForeground(textColor);
		editClientInfoButton.addMouseListener(this);
		editClientInfoButton.addMouseMotionListener(this);
		clientInfoPanelButtons.add(editClientInfoButton);
		
		//Exit Client Info Button
		exitClientInfoButton = new JButton ("Exit");
		exitClientInfoButton.setPreferredSize(new Dimension(center.getWidth()/6, 35));
		exitClientInfoButton.setMinimumSize(new Dimension(center.getWidth()/6, 35));
		exitClientInfoButton.setBackground(darkContent);
		exitClientInfoButton.setForeground(textColor);
		exitClientInfoButton.addMouseListener(this);
		editClientInfoButton.addMouseMotionListener(this);
		bottom.add(exitClientInfoButton);
		
		clientRawConstraints.gridx = 0;
		clientRawConstraints.gridy = 5;
		clientRawInfoPanel.add(clientInfoPanelButtons, clientRawConstraints);
		
		clientInfoConstraints.gridheight = 2;
		clientInfoConstraints.gridx = 0;
		clientInfoConstraints.gridy = 0;
		clientInfoConstraints.weightx = 1.0;
		clientInfoConstraints.weighty = 1.0;
		clientInfoPanel.add(clientRawInfoPanel, clientInfoConstraints);
		
		//Notes Panel that connects to Overall Panel
		JPanel notesContactPanel = new JPanel();
		notesContactPanel.setBackground(background);
		notesContactPanel.setLayout(new GridBagLayout());
		notesContactPanel.addMouseMotionListener(this);
		GridBagConstraints notesContactPanelConstraints = new GridBagConstraints();
		
		JPanel notesPanel = new JPanel();
		notesPanel.setBackground(background);
		notesPanel.setLayout(new GridBagLayout());
		notesPanel.addMouseMotionListener(this);
		GridBagConstraints notesPanelConstraints = new GridBagConstraints();
		
		JLabel notesLabel = new JLabel("Client Notes");
		notesLabel.setBackground(background);
		notesLabel.setForeground(textColor);
		notesLabel.addMouseMotionListener(this);
		
		notesTextArea = new JTextArea(masterControl.getClientNotesTextArea(searchResults.get(selectedClientPos).getAccountNumber()).getText());
		notesTextArea.setLineWrap(true);
		notesTextArea.setWrapStyleWord(true);
		notesTextArea.setEditable(false);
		notesTextArea.setBackground(darkContent);
		notesTextArea.setForeground(textColor);
		notesTextArea.addMouseMotionListener(this);
		
		notesScrollPane = new JScrollPane(notesTextArea, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		notesScrollPane.setMinimumSize(new Dimension((center.getWidth()/3)+50, center.getHeight()/3));
		notesScrollPane.setPreferredSize(new Dimension((center.getWidth()/3)+50,center.getHeight()/3));
		notesScrollPane.setBackground(darkContent);
		notesScrollPane.setForeground(textColor);
		notesScrollPane.setBorder(BorderFactory.createEmptyBorder());
		notesScrollPane.addMouseMotionListener(this);
		
		notesPanelConstraints.gridx = 0;
		notesPanelConstraints.gridy = 0;
		notesPanel.add(notesLabel, notesPanelConstraints);
		
		notesPanelConstraints.gridx = 0;
		notesPanelConstraints.gridy = 1;
		notesPanel.add(notesScrollPane, notesPanelConstraints);
				
		notesContactPanelConstraints.gridx = 0;
		notesContactPanelConstraints.gridy = 0;
		notesContactPanel.add(notesPanel, notesContactPanelConstraints);
		
		//Notes Panel Button(s)
		notesButtonsPanel = new JPanel();
		notesButtonsPanel.setBackground(background);
		notesButtonsPanel.addMouseMotionListener(this);
		
		editNotesClientButton = new JButton("Edit");
		editNotesClientButton.setPreferredSize(new Dimension(center.getWidth()/8, 35));
		editNotesClientButton.setMinimumSize(new Dimension(center.getWidth()/8, 35));
		editNotesClientButton.setBackground(darkContent);
		editNotesClientButton.setForeground(textColor);
		editNotesClientButton.addMouseListener(this);
		editNotesClientButton.addMouseMotionListener(this);
		notesButtonsPanel.add(editNotesClientButton);
		
		notesContactPanelConstraints.gridx = 0;
		notesContactPanelConstraints.gridy = 1;
		notesContactPanel.add(notesButtonsPanel, notesContactPanelConstraints);
		
		//Contacts Panel that connects to Overall Panel
		JPanel contactsPanel = new JPanel();
		contactsPanel.setBackground(background);
		contactsPanel.setLayout(new GridBagLayout());
		contactsPanel.addMouseMotionListener(this);
		GridBagConstraints contactsPanelConstraints = new GridBagConstraints();
		
		JLabel contactsLabel = new JLabel("Client Contacts");
		contactsLabel.setBackground(background);
		contactsLabel.setForeground(textColor);
		contactsLabel.addMouseMotionListener(this);
		
		contactsPanelConstraints.gridx = 0;
		contactsPanelConstraints.gridy = 0;
		contactsPanel.add(contactsLabel, contactsPanelConstraints);
		
		boolean colorSwitch = true;
		JPanel clientContactDisplayPanel = new JPanel();
		clientContactDisplayPanel.setLayout(new GridBagLayout());
		clientContactDisplayPanel.setBackground(background);
		clientContactDisplayPanel.setForeground(textColor);
		clientContactDisplayPanel.addMouseMotionListener(this);
		GridBagConstraints clientContactDisplayPanelConstraints = new GridBagConstraints(); 
		
		JPanel columnTitles = new JPanel();
		columnTitles.setLayout(new GridLayout());
		columnTitles.setMaximumSize(new Dimension((center.getWidth()/3)+50, 35));
		columnTitles.setMinimumSize(new Dimension((center.getWidth()/3)+50, 35));
		columnTitles.setPreferredSize(new Dimension((center.getWidth()/3)+50, 35));
		columnTitles.setBackground(darkContent);
		columnTitles.addMouseMotionListener(this);
		
		editRoiStatusButton = new JButton("Edit");
		editRoiStatusButton.setBackground(lightContent);
		editRoiStatusButton.setForeground(textColor);
		editRoiStatusButton.addMouseListener(this);
		editRoiStatusButton.addMouseMotionListener(this);
		
		columnTitles.add(editRoiStatusButton);
		JLabel nameColumnLabel = new JLabel("Name");
		nameColumnLabel.setBackground(lightContent);
		nameColumnLabel.setForeground(textColor);
		nameColumnLabel.addMouseMotionListener(this);
		columnTitles.add(nameColumnLabel);
		
		JLabel titleColumnLabel = new JLabel("Title");
		titleColumnLabel.setBackground(lightContent);
		titleColumnLabel.setForeground(textColor);
		titleColumnLabel.addMouseMotionListener(this);
		columnTitles.add(titleColumnLabel);
		
		columnTitles.setOpaque(true);
		columnTitles.setBackground(lightContent);
		columnTitles.setForeground(textColor);

		//Actual Client Contacts
		int numberOfContactsCounter = 0;
		for (int i = 0; i < (searchResults.get(selectedClientPos).getContactPersonalList()).size(); i++)
		{
			JPanel clientContactPersonalLinePanel = new JPanel();
			clientContactPersonalLinePanel.setLayout(new GridLayout());
			clientContactPersonalLinePanel.setMinimumSize(new Dimension((center.getWidth()/3)+50, 35));
			clientContactPersonalLinePanel.setPreferredSize(new Dimension((center.getWidth()/3)+50, 35));
			clientContactPersonalLinePanel.addMouseMotionListener(this);
			
			if (colorSwitch)
			{
				clientContactPersonalLinePanel.setBackground(darkContent);
				clientContactPersonalLinePanel.setForeground(textColor);
				colorSwitch = false;
			}
			else
			{
				clientContactPersonalLinePanel.setBackground(lightContent);
				clientContactPersonalLinePanel.setForeground(textColor);
				colorSwitch = true;
			}
			JRadioButton releaseOfInformationButton = new JRadioButton("ROI");
			releaseOfInformationButton.setBackground(clientContactPersonalLinePanel.getBackground());
			releaseOfInformationButton.setForeground(clientContactPersonalLinePanel.getForeground());
			releaseOfInformationButton.setEnabled(false);
			releaseOfInformationButton.setSelected(searchResults.get(selectedClientPos).getContactPersonalROIList().get(i));
			releaseOfInformationButton.addMouseMotionListener(this);
			editROIButtonList.add(releaseOfInformationButton);
			clientContactPersonalLinePanel.add(releaseOfInformationButton);
			
			JButton clientContactMoreButton = new JButton(searchResults.get(selectedClientPos).getContactPersonalList().get(i).getName());
			clientContactMoreButton.addMouseListener(this);
			clientContactMoreButton.setBorder(BorderFactory.createEtchedBorder());
			clientContactMoreButton.setBackground(clientContactPersonalLinePanel.getBackground());
			clientContactMoreButton.setForeground(clientContactPersonalLinePanel.getForeground());
			clientContactMoreButton.addMouseMotionListener(this);
			clientContactMoreButtonList.add(clientContactMoreButton);
			clientContactPersonalLinePanel.add(clientContactMoreButton);
			
			JTextField clientContactTitle = new JTextField(searchResults.get(selectedClientPos).getContactPersonalList().get(i).getTitle());
			clientContactTitle.setBackground(clientContactPersonalLinePanel.getBackground());
			clientContactTitle.setForeground(clientContactPersonalLinePanel.getForeground());
			clientContactTitle.setBorder(BorderFactory.createEmptyBorder());
			clientContactTitle.setEditable(false);
			clientContactTitle.setCaretPosition(0);
			clientContactTitle.addMouseMotionListener(this);
			clientContactPersonalLinePanel.add(clientContactTitle);
			
			clientContactPersonalLinePanel.setOpaque(true);
			clientInfoContactsGUI.add(clientContactPersonalLinePanel);
			numberOfContactsCounter++;
		}
		for (int i = 0; i < searchResults.get(selectedClientPos).getContactList().size(); i++)
		{
			JPanel clientContactLinePanel = new JPanel();
			clientContactLinePanel.setLayout(new GridLayout());
			clientContactLinePanel.setMinimumSize(new Dimension((center.getWidth()/3)+50, 35));
			clientContactLinePanel.setPreferredSize(new Dimension((center.getWidth()/3)+50, 35));
			clientContactLinePanel.addMouseMotionListener(this);
			if (colorSwitch)
			{
				clientContactLinePanel.setBackground(darkContent);
				clientContactLinePanel.setForeground(textColor);
				colorSwitch = false;
			}
			else
			{
				clientContactLinePanel.setBackground(lightContent);
				clientContactLinePanel.setForeground(textColor);
				colorSwitch = true;
			}
			JRadioButton releaseOfInformationButton = new JRadioButton("ROI");
			releaseOfInformationButton.setBackground(clientContactLinePanel.getBackground());
			releaseOfInformationButton.setForeground(clientContactLinePanel.getForeground());
			releaseOfInformationButton.setEnabled(false);
			releaseOfInformationButton.setSelected(searchResults.get(selectedClientPos).getContactROIList().get(i));
			releaseOfInformationButton.addMouseMotionListener(this);
  			editROIButtonList.add(releaseOfInformationButton);
			clientContactLinePanel.add(releaseOfInformationButton);

			JButton clientContactMoreButton = new JButton(masterControl.getClientContactList(searchResults.get(selectedClientPos).getContactList()).get(i).getName());
			clientContactMoreButton.addMouseListener(this);
			clientContactMoreButton.setBorder(BorderFactory.createEtchedBorder());
			clientContactMoreButton.setBackground(clientContactLinePanel.getBackground());
			clientContactMoreButton.setForeground(clientContactLinePanel.getForeground());
			clientContactMoreButton.addMouseMotionListener(this);
			clientContactMoreButtonList.add(clientContactMoreButton);
			clientContactLinePanel.add(clientContactMoreButton);
			
			JTextField clientContactTitle = new JTextField(masterControl.getClientContactList(searchResults.get(selectedClientPos).getContactList()).get(i).getTitle());
			clientContactTitle.setBackground(clientContactLinePanel.getBackground());
			clientContactTitle.setForeground(clientContactLinePanel.getForeground());
			clientContactTitle.setBorder(BorderFactory.createEmptyBorder());
			clientContactTitle.setEditable(false);
			clientContactTitle.setCaretPosition(0);
			clientContactTitle.addMouseMotionListener(this);
			clientContactLinePanel.add(clientContactTitle);
			
			clientContactLinePanel.setOpaque(true);
			clientInfoContactsGUI.add(clientContactLinePanel);
			numberOfContactsCounter++;
		}
		//For content resizing. If there are more than 4 contacts, and the height of the center is smaller
		//OR if the number of contacts is greater than 8, and the height is bigger, put the content in a JScrollPane
		//for visibility. Else just add them to the center.
		if ((numberOfContactsCounter > 4 && center.getHeight() < 800) || (numberOfContactsCounter > 8 && center.getHeight() > 800))
		{
			clientContactDisplayPanelConstraints.gridx = 0;
			clientContactDisplayPanelConstraints.gridy = 0;
			clientContactDisplayPanelConstraints.weightx = 1.0;
			clientContactDisplayPanelConstraints.weighty = 1.0;		
			clientContactDisplayPanel.add(columnTitles, clientContactDisplayPanelConstraints);
			
			for (int i = 0; i < clientInfoContactsGUI.size(); i++)
			{
				clientContactDisplayPanelConstraints.gridx = 0;
				clientContactDisplayPanelConstraints.gridy = i+1;
				clientContactDisplayPanelConstraints.weightx = 1.0;
				clientContactDisplayPanelConstraints.weighty = 1.0;	
				clientContactDisplayPanel.add(clientInfoContactsGUI.get(i), clientContactDisplayPanelConstraints);
			}
			
			JScrollPane contactsScrollPane = new JScrollPane(clientContactDisplayPanel, 
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			contactsScrollPane.getVerticalScrollBar().setUnitIncrement(8);
			contactsScrollPane.setMinimumSize(new Dimension((center.getWidth()/3)+50,center.getHeight()/3));
			contactsScrollPane.setPreferredSize(new Dimension((center.getWidth()/3)+50,center.getHeight()/3));
			contactsScrollPane.setBackground(darkContent);
			contactsScrollPane.setForeground(textColor);
			contactsScrollPane.setBorder(BorderFactory.createEmptyBorder());
			contactsScrollPane.addMouseMotionListener(this);
			
			contactsPanelConstraints.gridx = 0;
			contactsPanelConstraints.gridy = 1;
			contactsPanel.add(contactsScrollPane, contactsPanelConstraints);
		}
		else
		{
			clientContactDisplayPanelConstraints.gridx = 0;
			clientContactDisplayPanelConstraints.gridy = 0;
			clientContactDisplayPanelConstraints.weightx = 1.0;
			clientContactDisplayPanelConstraints.weighty = 1.0;		
			clientContactDisplayPanel.add(columnTitles, clientContactDisplayPanelConstraints);
			
			for (int i = 0; i < clientInfoContactsGUI.size(); i++)
			{
				clientContactDisplayPanelConstraints.gridx = 0;
				clientContactDisplayPanelConstraints.gridy = i+1;
				clientContactDisplayPanelConstraints.weightx = 1.0;
				clientContactDisplayPanelConstraints.weighty = 1.0;	
				clientContactDisplayPanel.add(clientInfoContactsGUI.get(i), clientContactDisplayPanelConstraints);
			}
			//If there are less Contacts than there are available room in the JTextArea,
			//this will create filler spots so that the proportions stay the same.
			int amountOfFillerSlots = 0;
			if (numberOfContactsCounter <= 4 && center.getHeight() < 800)
			{
				amountOfFillerSlots = 4;
			}
			else
			{
				amountOfFillerSlots = 8;
			}
			for (int i = 0; i < amountOfFillerSlots-clientInfoContactsGUI.size(); i ++)
			{
				JPanel fillerPanel = new JPanel();
				fillerPanel.setLayout(new GridLayout());
				fillerPanel.setMinimumSize(new Dimension((center.getWidth()/3)+50, 35));
				fillerPanel.setPreferredSize(new Dimension((center.getWidth()/3)+50, 35));
				
				if (colorSwitch)
				{
					fillerPanel.setBackground(darkContent);
					fillerPanel.setForeground(textColor);
					colorSwitch = false;
				}
				else
				{
					fillerPanel.setBackground(lightContent);
					fillerPanel.setForeground(textColor);
					colorSwitch = true;
				}
				clientContactDisplayPanelConstraints.gridx = 0;
				clientContactDisplayPanelConstraints.gridy = (clientInfoContactsGUI.size()+1)+i;
				clientContactDisplayPanelConstraints.weightx = 1.0;
				clientContactDisplayPanelConstraints.weighty = 1.0;	
				clientContactDisplayPanel.add(fillerPanel, clientContactDisplayPanelConstraints);
			}
			contactsPanelConstraints.gridx = 0;
			contactsPanelConstraints.gridy = 1;
			contactsPanel.add(clientContactDisplayPanel, contactsPanelConstraints);
		}
		
		notesContactPanelConstraints.gridx = 0;
		notesContactPanelConstraints.gridy = 2;
		notesContactPanel.add(contactsPanel, notesContactPanelConstraints);
		
		//Client Contacts Panel Button(s)
		JPanel contactsClientButtonsPanel = new JPanel();
		contactsClientButtonsPanel.setBackground(background);
		
		addRemoveContactClientButton = new JButton("Edit Contacts");			
		addRemoveContactClientButton.setPreferredSize(new Dimension(center.getWidth()/6, 35));
		addRemoveContactClientButton.setMinimumSize(new Dimension(center.getWidth()/6, 35));
		addRemoveContactClientButton.setBackground(darkContent);
		addRemoveContactClientButton.setForeground(textColor);
		addRemoveContactClientButton.addMouseListener(this);
		addRemoveContactClientButton.addMouseMotionListener(this);
		contactsClientButtonsPanel.add(addRemoveContactClientButton);
		
		notesContactPanelConstraints.gridx = 0;
		notesContactPanelConstraints.gridy = 3;
		notesContactPanel.add(contactsClientButtonsPanel, notesContactPanelConstraints);
		
		clientInfoConstraints.gridx = 1;
		clientInfoConstraints.gridy = 0;
		clientInfoConstraints.weightx = 1.0;
		clientInfoConstraints.weighty = 1.0;
		clientInfoPanel.add(notesContactPanel, clientInfoConstraints);
	
		center.add(clientInfoPanel);
		
		this.addKeyListener(this);
		this.addMouseMotionListener(this);
		this.requestFocusInWindow();

		revalidate();
		repaint();
	}
	/**
	 * This button enables the JTextFields for the client's
	 * information to be editable. Replaces the "Edit Information"
	 * button with "Save" and "Cancel" buttons.
	 * Also disables the Notes, and Client Contact areas until
	 * the edit is complete or cancelled.
	 */
	public void editClientRawInfoButton()
	{
		isEditingClientInfo = true;
		
		firstNameField.setEditable(true);
		firstNameField.setBackground(highlight);
		
		lastNameField.setEditable(true);
		lastNameField.setBackground(highlight);
		
		phoneField.setEditable(true);
		phoneField.setBackground(highlight);
		
		emailField.setEditable(true);
		emailField.setBackground(highlight);
		
		addressField.setEditable(true);
		addressField.setBackground(highlight);
		
		cityField.setEditable(true);
		cityField.setBackground(highlight);
		
		stateComboBox.setEnabled(true);
		stateComboBox.setBackground(highlight);
		
		zipField.setEditable(true);
		zipField.setBackground(highlight);
		
		alertField.setEditable(true);
		alertField.setBackground(highlight);
		
		isClientActiveButton.setEnabled(true);
		isClientActiveButton.setBackground(highlight);
				
		clientInfoPanelButtons.removeAll();
		saveInfoButton = new JButton("Save");
		saveInfoButton.setMinimumSize(new Dimension(center.getWidth()/8, 35));
		saveInfoButton.setPreferredSize(new Dimension(center.getWidth()/8, 35));
		saveInfoButton.setBackground(darkContent);
		saveInfoButton.setForeground(textColor);
		saveInfoButton.addMouseListener(this);
		clientInfoPanelButtons.add(saveInfoButton);
		
		cancelInfoButton = new JButton("Cancel");
		cancelInfoButton.setMinimumSize(new Dimension(center.getWidth()/8, 35));
		cancelInfoButton.setPreferredSize(new Dimension(center.getWidth()/8, 35));
		cancelInfoButton.setBackground(darkContent);
		cancelInfoButton.setForeground(textColor);
		cancelInfoButton.addMouseListener(this);
		clientInfoPanelButtons.add(cancelInfoButton);
		
		searchBar.setEnabled(false);
		searchBar.removeMouseListener(this);
		searchStart.setEnabled(false);
		searchStart.removeMouseListener(this);
		editNotesClientButton.setEnabled(false);
		editNotesClientButton.removeMouseListener(this);
		editRoiStatusButton.setEnabled(false);
		editRoiStatusButton.removeMouseListener(this);
		addRemoveContactClientButton.setEnabled(false);
		addRemoveContactClientButton.removeMouseListener(this);
		exitClientInfoButton.setEnabled(false);
		exitClientInfoButton.removeMouseListener(this);
		
		revalidate();
		repaint();
	}
	/**
	 * This method sends the new information to SmalleyControl to be sent
	 * to Loader to rewrite the files pertinent to the client information that
	 * was changed. Then disables the text areas and re-enables the notes and
	 * client contacts areas for use. If the Zip code is invalid, it will 
	 * pop up a message to fix the zip code.
	 */
	public void saveEditClientRawInfoButton()
	{
		isEditingClientInfo = false;
		try
		{
			Integer.parseInt(zipField.getText());//to flag the NumberFormatException(s) before any changes
			
			firstNameField.setText(firstNameField.getText().toUpperCase());
			firstNameField.setEditable(false);
			firstNameField.setBackground(darkContent);
			
			lastNameField.setText(lastNameField.getText().toUpperCase());
			lastNameField.setEditable(false);
			lastNameField.setBackground(darkContent);
			
			phoneField.setText(phoneField.getText().toUpperCase());
			phoneField.setEditable(false);
			phoneField.setBackground(darkContent);
			
			emailField.setText(emailField.getText().toUpperCase());
			emailField.setEditable(false);
			emailField.setBackground(darkContent);
			
			addressField.setText(addressField.getText().toUpperCase());
			addressField.setEditable(false);
			addressField.setBackground(darkContent);
			
			cityField.setText(cityField.getText().toUpperCase());
			cityField.setEditable(false);
			cityField.setBackground(darkContent);
			
			stateComboBox.setSelectedIndex(stateComboBox.getSelectedIndex());
			stateComboBox.setEnabled(false);
			stateComboBox.setBackground(darkContent);
			
			zipField.setText(zipField.getText());
			zipField.setEditable(false);
			zipField.setBackground(darkContent);
			
			alertField.setText(alertField.getText().toUpperCase());
			alertField.setEditable(false);
			alertField.setBackground(darkContent);
			
			isClientActiveButton.setSelected(isClientActiveButton.isSelected());
			isClientActiveButton.setEnabled(false);
			isClientActiveButton.setBackground(background);
			
			alertPanel.remove(saveInfoButton);
			alertPanel.remove(cancelInfoButton);
			
			clientInfoPanelButtons.removeAll();
			
			editClientInfoButton = new JButton("Edit Information");
			editClientInfoButton.setMinimumSize(new Dimension(center.getWidth()/6, 35));
			editClientInfoButton.setPreferredSize(new Dimension(center.getWidth()/6, 35));
			editClientInfoButton.setBackground(darkContent);
			editClientInfoButton.setForeground(textColor);
			editClientInfoButton.addMouseListener(this);
			clientInfoPanelButtons.add(editClientInfoButton);
			
			searchBar.setEnabled(true);
			searchBar.addMouseListener(this);
			searchStart.setEnabled(true);
			searchStart.addMouseListener(this);
			editNotesClientButton.setEnabled(true);
			editNotesClientButton.addMouseListener(this);
			editRoiStatusButton.setEnabled(true);
			editRoiStatusButton.addMouseListener(this);
			addRemoveContactClientButton.setEnabled(true);
			addRemoveContactClientButton.addMouseListener(this);
			exitClientInfoButton.setEnabled(true);
			exitClientInfoButton.addMouseListener(this);
			
			this.requestFocusInWindow();
			revalidate();
			repaint();
			
			Client clientToEdit = new Client(searchResults.get(selectedClientPos).getAccountNumber(), 
					firstNameField.getText(), lastNameField.getText(), addressField.getText(),
					cityField.getText(), stateComboBox.getSelectedIndex(), Integer.parseInt(zipField.getText()), 
					phoneField.getText(), emailField.getText(), alertField.getText());
			clientToEdit.setClientActive(isClientActiveButton.isSelected());
			masterControl.editClient(clientToEdit);
		}
		catch(NumberFormatException e)
		{
			JTextArea moreInfoTextArea = new JTextArea("Invalid zip code.");
			moreInfoTextArea.setEditable(false);
			moreInfoTextArea.setWrapStyleWord(true);
			moreInfoTextArea.setLineWrap(true);
			moreInfoTextArea.setBackground(darkContent);
			moreInfoTextArea.setForeground(textColor);
			
			JOptionPane.showMessageDialog(null, moreInfoTextArea, "Alert", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/**
	 * This method resets the data in the client raw info
	 * sections back to what it was before they were edited.
	 * Then disables editing and re-enables the Notes and Client Contacts
	 * areas for use.
	 */
	public void cancelEditClientRawInfoButton()
	{
		isEditingClientInfo = false;
		firstNameField.setText(searchResults.get(selectedClientPos).getFname());
		firstNameField.setEditable(false);
		firstNameField.setBackground(darkContent);
		
		lastNameField.setText(searchResults.get(selectedClientPos).getLname());
		lastNameField.setEditable(false);
		lastNameField.setBackground(darkContent);
		
		phoneField.setText(searchResults.get(selectedClientPos).getPhone());
		phoneField.setEditable(false);
		phoneField.setBackground(darkContent);
		
		emailField.setText(searchResults.get(selectedClientPos).getEmail());
		emailField.setEditable(false);
		emailField.setBackground(darkContent);
		
		addressField.setText(searchResults.get(selectedClientPos).getAddress());
		addressField.setEditable(false);
		addressField.setBackground(darkContent);
				
		cityField.setText(searchResults.get(selectedClientPos).getCity());
		cityField.setEditable(false);
		cityField.setBackground(darkContent);
		
		stateComboBox.setSelectedIndex(searchResults.get(selectedClientPos).getStateIndex());
		stateComboBox.setEnabled(false);
		stateComboBox.setBackground(darkContent);
		
		zipField.setText(Integer.toString(searchResults.get(selectedClientPos).getZip()));
		zipField.setEditable(false);
		zipField.setBackground(darkContent);		
		
		alertField.setText(searchResults.get(selectedClientPos).getAlertNotes());
		alertField.setEditable(false);
		alertField.setBackground(darkContent);
		
		isClientActiveButton.setSelected(searchResults.get(selectedClientPos).getClientActive());
		isClientActiveButton.setEnabled(false);
		isClientActiveButton.setBackground(background);
		
		clientInfoPanelButtons.removeAll();
		
		editClientInfoButton = new JButton("Edit Information");
		editClientInfoButton.setMinimumSize(new Dimension(center.getWidth()/6, 35));
		editClientInfoButton.setPreferredSize(new Dimension(center.getWidth()/6, 35));
		editClientInfoButton.setBackground(darkContent);
		editClientInfoButton.setForeground(textColor);
		editClientInfoButton.addMouseListener(this);
		clientInfoPanelButtons.add(editClientInfoButton);
		
		searchBar.setEnabled(true);
		searchBar.addMouseListener(this);
		searchStart.setEnabled(true);
		searchStart.addMouseListener(this);
		editNotesClientButton.setEnabled(true);
		editNotesClientButton.addMouseListener(this);
		editRoiStatusButton.setEnabled(true);
		editRoiStatusButton.addMouseListener(this);
		addRemoveContactClientButton.setEnabled(true);
		addRemoveContactClientButton.addMouseListener(this);
		exitClientInfoButton.setEnabled(true);
		exitClientInfoButton.addMouseListener(this);
		
		this.requestFocusInWindow();
		revalidate();
		repaint();
	}
	/**
	 * Enables editing of the Notes JTextArea of the Client Info Menu.
	 *
	 */
	public void editNotesClientButton()
	{
		isEditingClientInfo = true;
		notesTextArea.setEditable(true);
		notesTextArea.setBackground(highlight);
		notesButtonsPanel.removeAll();
		
		saveEditNotesClientButton = new JButton ("Save");
		saveEditNotesClientButton.setMinimumSize(new Dimension(center.getWidth()/8, 35));
		saveEditNotesClientButton.setPreferredSize(new Dimension(center.getWidth()/8, 35));
		saveEditNotesClientButton.setBackground(darkContent);
		saveEditNotesClientButton.setForeground(textColor);
		saveEditNotesClientButton.addMouseListener(this);
		notesButtonsPanel.add(saveEditNotesClientButton);
		
		cancelEditNotesClientButton = new JButton ("Cancel");
		cancelEditNotesClientButton.setMinimumSize(new Dimension(center.getWidth()/8, 35));
		cancelEditNotesClientButton.setPreferredSize(new Dimension(center.getWidth()/8, 35));
		cancelEditNotesClientButton.setBackground(darkContent);
		cancelEditNotesClientButton.setForeground(textColor);
		cancelEditNotesClientButton.addMouseListener(this);
		notesButtonsPanel.add(cancelEditNotesClientButton);
		
		searchBar.setEnabled(false);
		searchBar.removeMouseListener(this);
		searchStart.setEnabled(false);
		searchStart.removeMouseListener(this);
		editClientInfoButton.setEnabled(false);
		editClientInfoButton.removeMouseListener(this);
		editRoiStatusButton.setEnabled(false);
		editRoiStatusButton.removeMouseListener(this);
		addRemoveContactClientButton.setEnabled(false);
		addRemoveContactClientButton.removeMouseListener(this);
		exitClientInfoButton.setEnabled(false);
		exitClientInfoButton.removeMouseListener(this);

		revalidate();
		repaint();
	}
	/**
	 * Saves the new information in the Notes JTextArea.
	 */
	public void saveEditNotesClientButton()
	{
		isEditingClientInfo = false;
		notesTextArea.setEditable(false);
		notesTextArea.setText(notesTextArea.getText().toUpperCase());
		masterControl.editClientNotesTextArea(searchResults.get(selectedClientPos).getAccountNumber(),
				notesTextArea);
		notesTextArea.setBackground(darkContent);
		notesTextArea.setForeground(textColor);
		notesButtonsPanel.removeAll();
		
		editNotesClientButton = new JButton("Edit");
		editNotesClientButton.setMinimumSize(new Dimension(center.getWidth()/8, 35));
		editNotesClientButton.setPreferredSize(new Dimension(center.getWidth()/8, 35));
		editNotesClientButton.setBackground(darkContent);
		editNotesClientButton.setForeground(textColor);
		editNotesClientButton.addMouseListener(this);
		notesButtonsPanel.add(editNotesClientButton);
		
		searchBar.setEnabled(true);
		searchBar.addMouseListener(this);
		searchStart.setEnabled(true);
		searchStart.addMouseListener(this);
		editClientInfoButton.setEnabled(true);
		editClientInfoButton.addMouseListener(this);
		editRoiStatusButton.setEnabled(true);
		editRoiStatusButton.addMouseListener(this);
		addRemoveContactClientButton.setEnabled(true);
		addRemoveContactClientButton.addMouseListener(this);
		exitClientInfoButton.setEnabled(true);
		exitClientInfoButton.addMouseListener(this);
		
		this.requestFocusInWindow();
		revalidate();
		repaint();
	}
	/**
	 * Resets the data in the JTextArea back to what it
	 * was originally.
	 */
	public void cancelEditNotesClientButton()
	{
		isEditingClientInfo = false;
		notesTextArea.setText(masterControl.getClientNotesTextArea
				(searchResults.get(selectedClientPos).getAccountNumber()).getText());
		notesTextArea.setBackground(darkContent);
		notesTextArea.setForeground(textColor);
		notesTextArea.setEditable(false);
		notesTextArea.setCaretPosition(0);
		notesButtonsPanel.removeAll();
		
		editNotesClientButton = new JButton("Edit");
		editNotesClientButton.setMinimumSize(new Dimension(center.getWidth()/8, 35));
		editNotesClientButton.setPreferredSize(new Dimension(center.getWidth()/8, 35));
		editNotesClientButton.setBackground(darkContent);
		editNotesClientButton.setForeground(textColor);
		editNotesClientButton.addMouseListener(this);
		notesButtonsPanel.add(editNotesClientButton);
		
		searchBar.setEnabled(true);
		searchBar.addMouseListener(this);
		searchStart.setEnabled(true);
		searchStart.addMouseListener(this);
		editClientInfoButton.setEnabled(true);
		editClientInfoButton.addMouseListener(this);
		editRoiStatusButton.setEnabled(true);
		editRoiStatusButton.addMouseListener(this);
		addRemoveContactClientButton.setEnabled(true);
		addRemoveContactClientButton.addMouseListener(this);
		exitClientInfoButton.setEnabled(true);
		exitClientInfoButton.addMouseListener(this);
		
		this.requestFocusInWindow();
		revalidate();
		repaint();
	}
	/**
	 * Sends user to the Edit Contact Menu,
	 * has a searchBar to find public contacts,
	 * remove existing contacts (public or personal),
	 * and create/edit personal contacts.
	 */
	public void addRemoveClientContactButton()
	{
		top.setVisible(false);
		removeClientContactGUI.clear();
		removeClientContactListButtonList.clear();
		center.removeAll();
		bottom.removeAll();
		searchBar.setEnabled(false);
		searchBar.removeMouseListener(this);
		searchStart.setEnabled(false);
		searchStart.removeMouseListener(this);
		
		this.removeKeyListener(this);
		this.removeMouseMotionListener(this);
		
		//Overall Panel
		addRemoveClientContactPanel = new JPanel();
		addRemoveClientContactPanel.setLayout(new GridBagLayout());
		addRemoveClientContactPanel.setBackground(background);
		addRemoveClientContactPanel.setForeground(textColor);
		addRemoveClientContactPanelConstraints = new GridBagConstraints();
		
		//Area for the Search Public Contacts
		addClientContactPanel = new JPanel();
		addClientContactPanel.setBackground(background);
		addClientContactPanel.setForeground(textColor);
		addClientContactPanel.setLayout(new GridBagLayout());
		GridBagConstraints addClientContactPanelConstraints = new GridBagConstraints();
		
		JLabel addClientContactLabel = new JLabel("Add Contact");
		addClientContactLabel.setBackground(background);
		addClientContactLabel.setForeground(textColor);
		addClientContactPanelConstraints.gridx = 0;
		addClientContactPanelConstraints.gridy = 0;
		addClientContactPanelConstraints.gridwidth = 4;
		addClientContactPanel.add(addClientContactLabel, addClientContactPanelConstraints);
		
		searchContactPublicTextField = new JTextField();
		searchContactPublicTextField.setMinimumSize(new Dimension(center.getWidth()/2, 25));
		searchContactPublicTextField.setPreferredSize(new Dimension(center.getWidth()/2, 25));
		searchContactPublicTextField.addMouseListener(this);
		searchContactPublicTextField.addActionListener(this);
		searchContactPublicTextField.setBackground(darkContent);
		searchContactPublicTextField.setForeground(textColor);
		
		addClientContactPanelConstraints.gridx = 0;
		addClientContactPanelConstraints.gridy = 1;
		addClientContactPanelConstraints.gridwidth = 4;
		addClientContactPanel.add(searchContactPublicTextField, addClientContactPanelConstraints);
		
		searchContactPublicStartButton = new JButton("Search Public Contacts");
		searchContactPublicStartButton.setBackground(darkContent);
		searchContactPublicStartButton.setForeground(textColor);
		searchContactPublicStartButton.addMouseListener(this);
		addClientContactPanelConstraints.gridx = 4;
		addClientContactPanelConstraints.gridy = 1;
		addClientContactPanelConstraints.gridwidth = 2;
		addClientContactPanel.add(searchContactPublicStartButton, addClientContactPanelConstraints);

		addRemoveClientContactPanelConstraints.gridx = 0;
		addRemoveClientContactPanelConstraints.gridy = 0;
		addRemoveClientContactPanelConstraints.weightx = 1.0;
		addRemoveClientContactPanelConstraints.weighty = 1.0;
		addRemoveClientContactPanel.add(addClientContactPanel, addRemoveClientContactPanelConstraints);
		
		//Panel for removing existing contacts, creating/editing personal contacts
		removeClientContactPanel = new JPanel();
		removeClientContactPanel.setBackground(background);
		removeClientContactPanel.setForeground(textColor);
		removeClientContactPanel.setLayout(new GridBagLayout());
		GridBagConstraints removeClientContactPanelConstraints = new GridBagConstraints();
		
		removeClientContactLabel = new JLabel("Current Contacts");
		removeClientContactLabel.setBackground(background);
		removeClientContactLabel.setForeground(textColor);
		removeClientContactPanelConstraints.gridx = 0;
		removeClientContactPanelConstraints.gridy = 0;
		removeClientContactPanelConstraints.gridwidth = 4;
		removeClientContactPanel.add(removeClientContactLabel, removeClientContactPanelConstraints);
		
		boolean colorSwitch = true;
		JPanel removeContactDisplayPanel = new JPanel();
		removeContactDisplayPanel.setBackground(background);
		removeContactDisplayPanel.setForeground(textColor);
		removeContactDisplayPanel.setLayout(new GridBagLayout());
		GridBagConstraints removeContactDisplayPanelConstraints = new GridBagConstraints();
		
		JPanel columnTitles = new JPanel();
		columnTitles.setLayout(new GridLayout());
		columnTitles.setMaximumSize(new Dimension(center.getWidth()-25, 35));
		columnTitles.setPreferredSize(new Dimension(center.getWidth()-25, 35));
		
		JLabel spaceLabel = new JLabel("");
		spaceLabel.setForeground(textColor);
		columnTitles.add(spaceLabel);

		JLabel nameLabel = new JLabel("Name");
		nameLabel.setForeground(textColor);
		columnTitles.add(nameLabel);
		
		JLabel titleLabel = new JLabel("Title");
		titleLabel.setForeground(textColor);
		columnTitles.add(titleLabel);
		
		columnTitles.setOpaque(true);
		columnTitles.setBackground(lightContent);
		columnTitles.setForeground(textColor);
		
		//Personal Contacts
		int numberOfContactsCounter = 0;
		for (int i = 0; i < searchResults.get(selectedClientPos).getContactPersonalList().size(); i++)
		{
			JPanel removeContactDisplayLinePanel = new JPanel();
			removeContactDisplayLinePanel.setLayout(new GridLayout());
			removeContactDisplayLinePanel.setMinimumSize(new Dimension(center.getWidth()-25, 35));
			removeContactDisplayLinePanel.setPreferredSize(new Dimension(center.getWidth()-25, 35));
			
			if (colorSwitch)
			{
				removeContactDisplayLinePanel.setBackground(darkContent);
				removeContactDisplayLinePanel.setForeground(textColor);
				colorSwitch = false;
			}
			else
			{
				removeContactDisplayLinePanel.setBackground(lightContent);
				removeContactDisplayLinePanel.setForeground(textColor);
				colorSwitch = true;
			}
			
			JRadioButton removeClientContactSelectButton = new JRadioButton("Select");
			removeClientContactSelectButton.addActionListener(this);
			removeClientContactSelectButton.setBackground(removeContactDisplayLinePanel.getBackground());
			removeClientContactSelectButton.setForeground(removeContactDisplayLinePanel.getForeground());
			removeContactDisplayLinePanel.add(removeClientContactSelectButton);
			removeClientContactListButtonList.add(removeClientContactSelectButton);
			
			clientContactName = new JTextField(searchResults.get(selectedClientPos).getContactPersonalList().get(i).getName());
			clientContactName.setBackground(removeContactDisplayLinePanel.getBackground());
			clientContactName.setForeground(removeContactDisplayLinePanel.getForeground());
			clientContactName.setEditable(false);
			clientContactName.setCaretPosition(0);
			clientContactName.setBorder(BorderFactory.createEmptyBorder());
			removeContactDisplayLinePanel.add(clientContactName);
			
			clientContactTitle = new JTextField(searchResults.get(selectedClientPos).getContactPersonalList().get(i).getTitle());
			clientContactTitle.setBackground(removeContactDisplayLinePanel.getBackground());
			clientContactTitle.setForeground(removeContactDisplayLinePanel.getForeground());
			clientContactTitle.setEditable(false);
			clientContactTitle.setCaretPosition(0);
			clientContactTitle.setBorder(BorderFactory.createEmptyBorder());
			removeContactDisplayLinePanel.add(clientContactTitle);

			removeClientContactGUI.add(removeContactDisplayLinePanel);
			numberOfContactsCounter++;
		}
		//Public Contacts
		for (int i = 0; i < masterControl.getClientContactList(searchResults.get(selectedClientPos).getContactList()).size(); i++)
			{
				JPanel removeContactDisplayLinePanel = new JPanel();
				removeContactDisplayLinePanel.setLayout(new GridLayout());
				removeContactDisplayLinePanel.setMinimumSize(new Dimension(center.getWidth()-25, 35));
				removeContactDisplayLinePanel.setPreferredSize(new Dimension(center.getWidth()-25, 35));
				
				if (colorSwitch)
				{
					removeContactDisplayLinePanel.setBackground(darkContent);
					removeContactDisplayLinePanel.setForeground(textColor);
					colorSwitch = false;
				}
				else
				{
					removeContactDisplayLinePanel.setBackground(lightContent);
					removeContactDisplayLinePanel.setForeground(textColor);
					colorSwitch = true;
				}
				
				JRadioButton removeClientContactSelectButton = new JRadioButton("Select");
				removeClientContactSelectButton.addActionListener(this);
				removeClientContactSelectButton.setBackground(removeContactDisplayLinePanel.getBackground());
				removeClientContactSelectButton.setForeground(removeContactDisplayLinePanel.getForeground());
				removeContactDisplayLinePanel.add(removeClientContactSelectButton);
				removeClientContactListButtonList.add(removeClientContactSelectButton);
				
				JTextField clientContactName = new JTextField(masterControl.getClientContactList(searchResults.get(selectedClientPos).getContactList()).get(i).getName());
				clientContactName.setBackground(removeContactDisplayLinePanel.getBackground());
				clientContactName.setForeground(removeContactDisplayLinePanel.getForeground());
				clientContactName.setEditable(false);
				clientContactName.setCaretPosition(0);
				clientContactName.setBorder(BorderFactory.createEmptyBorder());
				removeContactDisplayLinePanel.add(clientContactName);
				
				JTextField clientContactTitle = new JTextField(masterControl.getClientContactList(searchResults.get(selectedClientPos).getContactList()).get(i).getTitle());
				clientContactTitle.setBackground(removeContactDisplayLinePanel.getBackground());
				clientContactTitle.setForeground(removeContactDisplayLinePanel.getForeground());
				clientContactTitle.setEditable(false);
				clientContactTitle.setCaretPosition(0);
				clientContactTitle.setBorder(BorderFactory.createEmptyBorder());
				removeContactDisplayLinePanel.add(clientContactTitle);

				removeClientContactGUI.add(removeContactDisplayLinePanel);
				numberOfContactsCounter++;
			}
		//Graphical resizing
		if ((numberOfContactsCounter > 6 && center.getHeight() < 800) || (numberOfContactsCounter > 13 && center.getHeight() > 800))
		{
			removeContactDisplayPanelConstraints.gridx = 0;
			removeContactDisplayPanelConstraints.gridy = 0;
			removeContactDisplayPanelConstraints.weightx = 1.0;
			removeContactDisplayPanelConstraints.weighty = 1.0;		
			removeContactDisplayPanel.add(columnTitles, removeContactDisplayPanelConstraints);
			
			for (int i = 0; i < removeClientContactGUI.size(); i++)
			{
				removeContactDisplayPanelConstraints.gridx = 0;
				removeContactDisplayPanelConstraints.gridy = i+1;
				removeContactDisplayPanelConstraints.weightx = 1.0;
				removeContactDisplayPanelConstraints.weighty = 1.0;	
				removeContactDisplayPanel.add(removeClientContactGUI.get(i), removeContactDisplayPanelConstraints);
			}
			removeClientContactScrollPane = new JScrollPane(removeContactDisplayPanel, 
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			removeClientContactScrollPane.getVerticalScrollBar().setUnitIncrement(8);
			removeClientContactScrollPane.setMinimumSize(new Dimension(center.getWidth()-25,center.getHeight()/2));
			removeClientContactScrollPane.setPreferredSize(new Dimension(center.getWidth()-25,center.getHeight()/2));
			removeClientContactScrollPane.setBackground(background);
			removeClientContactScrollPane.setForeground(textColor);
			removeClientContactScrollPane.setBorder(BorderFactory.createEmptyBorder());
			
			removeClientContactPanelConstraints.gridx = 0;
			removeClientContactPanelConstraints.gridy = 1;
			removeClientContactPanelConstraints.weightx = 1.0;
			removeClientContactPanelConstraints.weighty = 1.0;
			removeClientContactPanel.add(removeClientContactScrollPane, removeClientContactPanelConstraints);
		}
		else
		{
			removeContactDisplayPanelConstraints.gridx = 0;
			removeContactDisplayPanelConstraints.gridy = 0;
			removeContactDisplayPanelConstraints.weightx = 1.0;
			removeContactDisplayPanelConstraints.weighty = 1.0;		
			removeContactDisplayPanel.add(columnTitles, removeContactDisplayPanelConstraints);
			
			for (int i = 0; i < removeClientContactGUI.size(); i++)
			{
				removeContactDisplayPanelConstraints.gridx = 0;
				removeContactDisplayPanelConstraints.gridy = i+1;
				removeContactDisplayPanelConstraints.weightx = 1.0;
				removeContactDisplayPanelConstraints.weighty = 1.0;	
				removeContactDisplayPanel.add(removeClientContactGUI.get(i), removeContactDisplayPanelConstraints);
			}
			int amountOfFillerSlots = 0;
			if (numberOfContactsCounter <= 6 && center.getHeight() < 800)
			{
				amountOfFillerSlots = 6;
			}
			else
			{
				amountOfFillerSlots = 13;
			}
			for (int i = 0; i < amountOfFillerSlots-removeClientContactGUI.size(); i++)
			{
				JPanel fillerPanel = new JPanel();
				fillerPanel.setLayout(new GridLayout());
				fillerPanel.setMinimumSize(new Dimension(center.getWidth()-25, 35));
				fillerPanel.setPreferredSize(new Dimension(center.getWidth()-25, 35));
				if (colorSwitch)
				{
					fillerPanel.setBackground(darkContent);
					fillerPanel.setForeground(textColor);
					colorSwitch = false;
				}
				else
				{
					fillerPanel.setBackground(lightContent);
					fillerPanel.setForeground(textColor);
					colorSwitch = true;
				}
				removeContactDisplayPanelConstraints.gridx = 0;
				removeContactDisplayPanelConstraints.gridy = (removeClientContactGUI.size()+1)+i;
				removeContactDisplayPanelConstraints.weightx = 1.0;
				removeContactDisplayPanelConstraints.weighty = 1.0;	
				removeContactDisplayPanel.add(fillerPanel, removeContactDisplayPanelConstraints);
			}
			removeClientContactPanelConstraints.gridx = 0;
			removeClientContactPanelConstraints.gridy = 1;
			removeClientContactPanelConstraints.weightx = 1.0;
			removeClientContactPanelConstraints.weighty = 1.0;
			removeClientContactPanel.add(removeContactDisplayPanel, removeClientContactPanelConstraints);
		}
		
		addRemoveClientContactPanelConstraints.gridx = 0;
		addRemoveClientContactPanelConstraints.gridy = 1;
		addRemoveClientContactPanelConstraints.weightx = 1.0;
		addRemoveClientContactPanelConstraints.weighty = 1.0;
		addRemoveClientContactPanel.add(removeClientContactPanel, addRemoveClientContactPanelConstraints);
		
		center.add(addRemoveClientContactPanel);
		//Create/Edit, Remove Buttons, and Back Button
		searchContactCreatePersonalButton = new JButton("Create Personal Contact");
		searchContactCreatePersonalButton.setMinimumSize(new Dimension(center.getWidth()/4, 35));
		searchContactCreatePersonalButton.setPreferredSize(new Dimension(center.getWidth()/4, 35));
		searchContactCreatePersonalButton.setBackground(darkContent);
		searchContactCreatePersonalButton.setForeground(textColor);
		searchContactCreatePersonalButton.addMouseListener(this);
		bottom.add(searchContactCreatePersonalButton);
		
		addRemoveClientContactRemoveButton = new JButton("Remove");
		addRemoveClientContactRemoveButton.setMinimumSize(new Dimension(center.getWidth()/8, 35));
		addRemoveClientContactRemoveButton.setPreferredSize(new Dimension(center.getWidth()/8, 35));
		addRemoveClientContactRemoveButton.setBackground(darkContent);
		addRemoveClientContactRemoveButton.setForeground(textColor);
		addRemoveClientContactRemoveButton.setEnabled(false);
		addRemoveClientContactRemoveButton.removeMouseListener(this);
		bottom.add(addRemoveClientContactRemoveButton);
		
		exitAddRemoveClientContactButton = new JButton("Back");
		exitAddRemoveClientContactButton.setMinimumSize(new Dimension(center.getWidth()/8, 35));
		exitAddRemoveClientContactButton.setPreferredSize(new Dimension(center.getWidth()/8, 35));
		exitAddRemoveClientContactButton.setBackground(darkContent);
		exitAddRemoveClientContactButton.setForeground(textColor);
		exitAddRemoveClientContactButton.addMouseListener(this);
		bottom.setEnabled(true);
		bottom.add(exitAddRemoveClientContactButton);
		
		revalidate();
		repaint();
	}
	/**
	 * Searches the contactMasterList for any public contacts
	 * that match the search results. Then displays them.
	 */
	public void searchContactPublicButton()
	{
		addRemoveClientContactSearchResults.clear();
		removeClientContactPanel.removeAll();
		addClientContactListButtonList.clear();
		addClientContactGUI.clear();
		
		addRemoveClientContactSearchResults = masterControl.searchContactName(searchContactPublicTextField.getText());
		if (addRemoveClientContactSearchResults.size() > 0)
		{
			bottom.removeAll();
			removeClientContactPanel = new JPanel();
			removeClientContactPanel.setBackground(background);
			removeClientContactPanel.setForeground(textColor);
			removeClientContactPanel.setLayout(new GridBagLayout());
			GridBagConstraints removeClientContactPanelConstraints = new GridBagConstraints();
			
			removeClientContactLabel = new JLabel("Search Results");
			removeClientContactLabel.setBackground(background);
			removeClientContactLabel.setForeground(textColor);
			removeClientContactPanelConstraints.gridx = 0;
			removeClientContactPanelConstraints.gridy = 0;
			removeClientContactPanelConstraints.gridwidth = 4;
			removeClientContactPanel.add(removeClientContactLabel, removeClientContactPanelConstraints);
			
			boolean colorSwitch = true;
			JPanel removeContactDisplayPanel = new JPanel();
			removeContactDisplayPanel.setBackground(background);
			removeContactDisplayPanel.setForeground(textColor);
			removeContactDisplayPanel.setLayout(new GridBagLayout());
			GridBagConstraints removeContactDisplayPanelConstraints = new GridBagConstraints();
			
			JPanel columnTitles = new JPanel();
			columnTitles.setLayout(new GridLayout());
			columnTitles.setMaximumSize(new Dimension(center.getWidth(), 35));
			columnTitles.setPreferredSize(new Dimension(center.getWidth(), 35));
			columnTitles.setBackground(background);
			columnTitles.setForeground(textColor);
			
			JLabel spaceLabel = new JLabel("");
			spaceLabel.setBackground(lightContent);
			spaceLabel.setForeground(textColor);
			columnTitles.add(spaceLabel);
			
			JLabel nameLabel = new JLabel("Name");
			nameLabel.setBackground(lightContent);
			nameLabel.setForeground(textColor);
			columnTitles.add(nameLabel);
			
			JLabel titleLabel = new JLabel("Title");
			titleLabel.setBackground(lightContent);
			titleLabel.setForeground(textColor);
			columnTitles.add(titleLabel);
			
			columnTitles.setOpaque(true);
			columnTitles.setBackground(lightContent);
			columnTitles.setForeground(textColor);
			
			int numberOfSearchResultsCounter = 0;
			for (int i = 0; i < addRemoveClientContactSearchResults.size(); i++)
			{
				JPanel removeContactDisplayLinePanel = new JPanel();
				removeContactDisplayLinePanel.setLayout(new GridLayout());
				removeContactDisplayLinePanel.setMinimumSize(new Dimension(center.getWidth(), 35));
				removeContactDisplayLinePanel.setPreferredSize(new Dimension(center.getWidth(), 35));
				if (colorSwitch)
				{
					removeContactDisplayLinePanel.setBackground(darkContent);
					removeContactDisplayLinePanel.setForeground(textColor);
					colorSwitch = false;
				}
				else
				{
					removeContactDisplayLinePanel.setBackground(lightContent);
					removeContactDisplayLinePanel.setForeground(textColor);
					colorSwitch = true;
				}
				
				JRadioButton removeClientContactSelectButton = new JRadioButton("Select");
				removeClientContactSelectButton.addActionListener(this);
				removeClientContactSelectButton.setBackground(removeContactDisplayLinePanel.getBackground());
				removeClientContactSelectButton.setForeground(removeContactDisplayLinePanel.getForeground());
				removeContactDisplayLinePanel.add(removeClientContactSelectButton);
				addClientContactListButtonList.add(removeClientContactSelectButton);
				
				clientContactName = new JTextField(addRemoveClientContactSearchResults.get(i).getName());
				clientContactName.setBackground(removeContactDisplayLinePanel.getBackground());
				clientContactName.setForeground(removeContactDisplayLinePanel.getForeground());
				clientContactName.setBorder(BorderFactory.createEmptyBorder());
				clientContactName.setEditable(false);
				clientContactName.setCaretPosition(0);
				removeContactDisplayLinePanel.add(clientContactName);
				
				clientContactTitle = new JTextField(addRemoveClientContactSearchResults.get(i).getTitle());
				clientContactTitle.setBackground(removeContactDisplayLinePanel.getBackground());
				clientContactTitle.setForeground(removeContactDisplayLinePanel.getForeground());
				clientContactTitle.setBorder(BorderFactory.createEmptyBorder());
				clientContactTitle.setEditable(false);
				clientContactTitle.setCaretPosition(0);
				removeContactDisplayLinePanel.add(clientContactTitle);
				
				addClientContactGUI.add(removeContactDisplayLinePanel);
				numberOfSearchResultsCounter++;
			}
			
			//Graphical Resizing
			if ((numberOfSearchResultsCounter > 6 && center.getHeight() < 800) || (numberOfSearchResultsCounter > 13 && center.getHeight() > 800))
			{
				removeContactDisplayPanelConstraints.gridx = 0;
				removeContactDisplayPanelConstraints.gridy = 0;
				removeContactDisplayPanelConstraints.weightx = 1.0;
				removeContactDisplayPanelConstraints.weighty = 1.0;		
				removeContactDisplayPanel.add(columnTitles, removeContactDisplayPanelConstraints);
				
				for (int i = 0; i < addClientContactGUI.size(); i++)
				{
					removeContactDisplayPanelConstraints.gridx = 0;
					removeContactDisplayPanelConstraints.gridy = i+1;
					removeContactDisplayPanelConstraints.weightx = 1.0;
					removeContactDisplayPanelConstraints.weighty = 1.0;	
					removeContactDisplayPanel.add(addClientContactGUI.get(i), removeContactDisplayPanelConstraints);
				}
				
				removeClientContactScrollPane = new JScrollPane(removeContactDisplayPanel, 
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				removeClientContactScrollPane.getVerticalScrollBar().setUnitIncrement(8);
				removeClientContactScrollPane.setMinimumSize(new Dimension(center.getWidth()-25,center.getHeight()/2));
				removeClientContactScrollPane.setPreferredSize(new Dimension(center.getWidth()-25,center.getHeight()/2));
				removeClientContactScrollPane.setBackground(darkContent);
				removeClientContactScrollPane.setForeground(textColor);
				removeClientContactScrollPane.setBorder(BorderFactory.createEmptyBorder());
				
				removeClientContactPanelConstraints.gridx = 0;
				removeClientContactPanelConstraints.gridy = 1;
				removeClientContactPanelConstraints.weightx = 1.0;
				removeClientContactPanelConstraints.weighty = 1.0;
				removeClientContactPanel.add(removeClientContactScrollPane, removeClientContactPanelConstraints);
			}
			else
			{
				removeContactDisplayPanelConstraints.gridx = 0;
				removeContactDisplayPanelConstraints.gridy = 0;
				removeContactDisplayPanelConstraints.weightx = 1.0;
				removeContactDisplayPanelConstraints.weighty = 1.0;		
				removeContactDisplayPanel.add(columnTitles, removeContactDisplayPanelConstraints);
				
				for (int i = 0; i < addClientContactGUI.size(); i++)
				{
					removeContactDisplayPanelConstraints.gridx = 0;
					removeContactDisplayPanelConstraints.gridy = i+1;
					removeContactDisplayPanelConstraints.weightx = 1.0;
					removeContactDisplayPanelConstraints.weighty = 1.0;	
					removeContactDisplayPanel.add(addClientContactGUI.get(i), removeContactDisplayPanelConstraints);
				}
				int amountOfFillerSlots = 0;
				if (numberOfSearchResultsCounter <= 6 && center.getHeight() < 800)
				{
					amountOfFillerSlots = 6;
				}
				else
				{
					amountOfFillerSlots = 13;
				}
				for (int i = 0; i < amountOfFillerSlots-addClientContactGUI.size(); i++)
				{
					JPanel fillerPanel = new JPanel();
					fillerPanel.setLayout(new GridLayout());
					fillerPanel.setMinimumSize(new Dimension(center.getWidth(), 35));
					fillerPanel.setPreferredSize(new Dimension(center.getWidth(), 35));
					if (colorSwitch)
					{
						fillerPanel.setBackground(darkContent);
						fillerPanel.setForeground(textColor);
						colorSwitch = false;
					}
					else
					{
						fillerPanel.setBackground(lightContent);
						fillerPanel.setForeground(textColor);
						colorSwitch = true;
					}
					removeContactDisplayPanelConstraints.gridx = 0;
					removeContactDisplayPanelConstraints.gridy = (addClientContactGUI.size()+1)+i;
					removeContactDisplayPanelConstraints.weightx = 1.0;
					removeContactDisplayPanelConstraints.weighty = 1.0;	
					removeContactDisplayPanel.add(fillerPanel, removeContactDisplayPanelConstraints);
				}
				removeClientContactPanelConstraints.gridx = 0;
				removeClientContactPanelConstraints.gridy = 1;
				removeClientContactPanelConstraints.weightx = 1.0;
				removeClientContactPanelConstraints.weighty = 1.0;
				removeClientContactPanel.add(removeContactDisplayPanel, removeClientContactPanelConstraints);
			}
			
			//Save and Cancel buttons for adding the searched and selected public contact
			addClientContactSearchResultsSave = new JButton("Save");
			addClientContactSearchResultsSave.setMinimumSize(new Dimension(center.getWidth()/8, 35));
			addClientContactSearchResultsSave.setPreferredSize(new Dimension(center.getWidth()/8, 35));
			addClientContactSearchResultsSave.setBackground(darkContent);
			addClientContactSearchResultsSave.setForeground(textColor);
			addClientContactSearchResultsSave.setEnabled(false);
			addClientContactSearchResultsSave.removeMouseListener(this); 
			bottom.add(addClientContactSearchResultsSave);
			
			
			addClientContactSearchResultsCancel = new JButton("Cancel");
			addClientContactSearchResultsCancel.setMinimumSize(new Dimension(center.getWidth()/8, 35));
			addClientContactSearchResultsCancel.setPreferredSize(new Dimension(center.getWidth()/8, 35));
			addClientContactSearchResultsCancel.setBackground(darkContent);
			addClientContactSearchResultsCancel.setForeground(textColor);
			addClientContactSearchResultsCancel.addMouseListener(this);
			bottom.add(addClientContactSearchResultsCancel);
			
			addRemoveClientContactPanelConstraints.gridx = 0;
			addRemoveClientContactPanelConstraints.gridy = 1;
			addRemoveClientContactPanelConstraints.weightx = 1.0;
			addRemoveClientContactPanelConstraints.weighty = 1.0;
			addRemoveClientContactPanel.add(removeClientContactPanel, addRemoveClientContactPanelConstraints);
			
			center.add(addRemoveClientContactPanel);
			revalidate();
			repaint();
		}
		else
		{
			//If no results are found, this JPanel appears.
			bottom.removeAll();
			removeClientContactPanel.removeAll();
			
			removeClientContactPanel = new JPanel();
			removeClientContactPanel.setBackground(background);
			removeClientContactPanel.setForeground(textColor);

			JTextField noMatchFound = new JTextField("Search found no matches.");
			noMatchFound.setPreferredSize(new Dimension(center.getWidth(), center.getHeight()));
			noMatchFound.setMinimumSize(new Dimension(center.getWidth(), center.getHeight()));
			noMatchFound.setMaximumSize(new Dimension(center.getWidth(), center.getHeight()));
			noMatchFound.setHorizontalAlignment(JTextField.CENTER);
			noMatchFound.setBorder(BorderFactory.createEmptyBorder());
			noMatchFound.setBackground(lightContent);
			noMatchFound.setForeground(textColor);
			noMatchFound.setEditable(false);
			removeClientContactPanel.add(noMatchFound);
			
			addClientContactSearchResultsSave = new JButton("Save");
			addClientContactSearchResultsSave.setMinimumSize(new Dimension(center.getWidth()/8, 35));
			addClientContactSearchResultsSave.setPreferredSize(new Dimension(center.getWidth()/8, 35));
			addClientContactSearchResultsSave.setBackground(darkContent);
			addClientContactSearchResultsSave.setForeground(textColor);
			addClientContactSearchResultsSave.addMouseListener(this);
			bottom.add(addClientContactSearchResultsSave);
			
			addClientContactSearchResultsCancel = new JButton("Cancel");
			addClientContactSearchResultsCancel.setMinimumSize(new Dimension(center.getWidth()/8, 35));
			addClientContactSearchResultsCancel.setPreferredSize(new Dimension(center.getWidth()/8, 35));
			addClientContactSearchResultsCancel.setBackground(darkContent);
			addClientContactSearchResultsCancel.setForeground(textColor);
			addClientContactSearchResultsCancel.addMouseListener(this);
			bottom.add(addClientContactSearchResultsCancel);
			
			addRemoveClientContactPanelConstraints.gridx = 0;
			addRemoveClientContactPanelConstraints.gridy = 1;
			addRemoveClientContactPanelConstraints.weightx = 1.0;
			addRemoveClientContactPanelConstraints.weighty = 1.0;
			addRemoveClientContactPanel.add(removeClientContactPanel, addRemoveClientContactPanelConstraints);
			
			revalidate();
			repaint();
		}
	}
	/**
	 * Adds the public contact that was selected to be added to the client's contact list
	 * into the ArrayLists, and tells SmalleyControl to update the text files.
	 */
	public void searchContactPublicSaveButton()
	{	
		if (addClientContactSearchResultPos != -1)
		{
			List<Integer> newClientContactList = new ArrayList<>(searchResults.get(selectedClientPos).getContactList());
			newClientContactList.add(addRemoveClientContactSearchResults.get(addClientContactSearchResultPos).getAccountNumber());
			searchResults.get(selectedClientPos).setContactList((ArrayList<Integer>)newClientContactList);
			
			List<Boolean> newClientContactBooleanList = new ArrayList<>(searchResults.get(selectedClientPos).getContactROIList());
			newClientContactBooleanList.add(false);
			searchResults.get(selectedClientPos).setContactROIList((ArrayList<Boolean>)newClientContactBooleanList);
			
			masterControl.editClientContactFile(searchResults.get(selectedClientPos).getAccountNumber(), (ArrayList<Integer>)newClientContactList, (ArrayList<Boolean>)newClientContactBooleanList);
			addRemoveClientContactButton();
			
			JTextArea moreInfoTextArea = new JTextArea("Contact saved successfully.");
			moreInfoTextArea.setEditable(false);
			moreInfoTextArea.setWrapStyleWord(true);
			moreInfoTextArea.setLineWrap(true);
			moreInfoTextArea.setBackground(darkContent);
			moreInfoTextArea.setForeground(textColor);
			JOptionPane.showMessageDialog(null, moreInfoTextArea, "Alert", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/**
	 * Sends user to a menu to input Contact information. Save/cancel buttons to add the personal
	 * contact to the client's contact list. or if a personal contact was selected
	 * on the previous menu, it will pull up that personal contact's information for editing.
	 */
	public void createClientContactPersonalButton()
	{
		center.removeAll();
		editPersonalContact = false;
		if (searchContactCreatePersonalButton.getText().equals("Create Personal Contact"))
		{
			editPersonalContact = false;
		}
		else
		{
			editPersonalContact = true;
		}
		
		JPanel addNewContactPanel = new JPanel();
		addNewContactPanel.setBackground(background);
		addNewContactPanel.setForeground(textColor);
		addNewContactPanel.setLayout(new GridBagLayout());
		GridBagConstraints addNewContactPanelConstraints = new GridBagConstraints();
		
		JPanel addNewContactInfoPanel = new JPanel();
		addNewContactInfoPanel.setBackground(background);
		addNewContactInfoPanel.setForeground(textColor);
		addNewContactInfoPanel.setLayout(new GridBagLayout());
		GridBagConstraints addNewContactInfoPanelConstraints = new GridBagConstraints();

		JPanel namePanel = new JPanel();
		namePanel.setBackground(background);
		namePanel.setForeground(textColor);
		
		JLabel nameLabel = new JLabel("Name: ");
		nameLabel.setBackground(background);
		nameLabel.setForeground(textColor);
		namePanel.add(nameLabel);
		
		if (editPersonalContact == true)
		{
			nameClientContactPersonalField = new JTextField(searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).getName());
		}
		else
		{
			nameClientContactPersonalField = new JTextField();	
		}
		nameClientContactPersonalField.setMinimumSize(new Dimension(200, 25));
		nameClientContactPersonalField.setPreferredSize(new Dimension(200, 25));
		nameClientContactPersonalField.setEditable(true);
		nameClientContactPersonalField.setBackground(lightContent);
		nameClientContactPersonalField.setForeground(textColor);
		namePanel.add(nameClientContactPersonalField);
		
		JLabel titleLabel = new JLabel("Relation: ");
		titleLabel.setBackground(background);
		titleLabel.setForeground(textColor);
		namePanel.add(titleLabel);
		
		if (editPersonalContact == true)
		{
			titleClientContactPersonalField = new JTextField(searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).getTitle());
		}
		else
		{
			titleClientContactPersonalField = new JTextField();	
		}
		titleClientContactPersonalField.setMinimumSize(new Dimension(200, 25));
		titleClientContactPersonalField.setPreferredSize(new Dimension(200, 25));
		titleClientContactPersonalField.setEditable(true);
		titleClientContactPersonalField.setBackground(lightContent);
		titleClientContactPersonalField.setForeground(textColor);
		namePanel.add(titleClientContactPersonalField);
							
		addNewContactInfoPanelConstraints.gridx = 0;
		addNewContactInfoPanelConstraints.gridy = 0;
		addNewContactInfoPanel.add(namePanel, addNewContactInfoPanelConstraints);
		
		JPanel phoneEmailPanel = new JPanel();
		phoneEmailPanel.setBackground(background);
		phoneEmailPanel.setForeground(textColor);
		
		JLabel phoneLabel = new JLabel("Phone: ");
		phoneLabel.setBackground(background);
		phoneLabel.setForeground(textColor);
		phoneEmailPanel.add(phoneLabel);
		
		if (editPersonalContact == true)
		{
			phoneField = new JTextField(searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).getPhone());
		}
		else
		{
			phoneField = new JTextField();	
		}
		phoneField.setMinimumSize(new Dimension(100, 25));
		phoneField.setPreferredSize(new Dimension(100, 25));
		phoneField.setEditable(true);
		phoneField.setBackground(lightContent);
		phoneField.setForeground(textColor);
		phoneEmailPanel.add(phoneField);
		
		JLabel faxLabel = new JLabel("Fax: ");
		faxLabel.setBackground(background);
		faxLabel.setForeground(textColor);
		phoneEmailPanel.add(faxLabel);
		
		if (editPersonalContact == true)
		{
			faxClientContactPersonalField = new JTextField(searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).getFax());
		}
		else
		{
			faxClientContactPersonalField = new JTextField();	
		}
		faxClientContactPersonalField.setMinimumSize(new Dimension(100, 25));
		faxClientContactPersonalField.setPreferredSize(new Dimension(100, 25));
		faxClientContactPersonalField.setEditable(true);
		faxClientContactPersonalField.setBackground(lightContent);
		faxClientContactPersonalField.setForeground(textColor);
		phoneEmailPanel.add(faxClientContactPersonalField);
		
		JLabel emailLabel = new JLabel("Email: ");
		emailLabel.setBackground(background);
		emailLabel.setForeground(textColor);
		phoneEmailPanel.add(emailLabel);
		
		if (editPersonalContact == true)
		{
			emailField = new JTextField(searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).getEmail());
		}
		else
		{
			emailField = new JTextField();	
		}
		emailField.setMinimumSize(new Dimension(300, 25));
		emailField.setPreferredSize(new Dimension(300, 25));
		emailField.setEditable(true);
		emailField.setBackground(lightContent);
		emailField.setForeground(textColor);
		phoneEmailPanel.add(emailField);
		
		addNewContactInfoPanelConstraints.gridx = 0;
		addNewContactInfoPanelConstraints.gridy = 1;
		addNewContactInfoPanel.add(phoneEmailPanel, addNewContactInfoPanelConstraints);
		
		//Street Address
		JPanel addressPanelOne = new JPanel();
		addressPanelOne.setBackground(background);
		addressPanelOne.setForeground(textColor);
		
		JLabel addressLabel = new JLabel("Address: ");
		addressLabel.setBackground(background);
		addressLabel.setForeground(textColor);
		addressPanelOne.add(addressLabel);
		
		if (editPersonalContact == true)
		{
			addressField = new JTextField(searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).getAddress());
		}
		else
		{
			addressField = new JTextField();	
		}
		addressField.setMinimumSize(new Dimension(300, 25));
		addressField.setPreferredSize(new Dimension(300, 25));
		addressField.setEditable(true);
		addressField.setBackground(lightContent);
		addressField.setForeground(textColor);
		addressPanelOne.add(addressField);
				
		addNewContactInfoPanelConstraints.gridx = 0;
		addNewContactInfoPanelConstraints.gridy = 2;
		addNewContactInfoPanel.add(addressPanelOne, addNewContactInfoPanelConstraints);
		
		//City, State, Zip
		JPanel addressPanelTwo = new JPanel();
		addressPanelTwo.setBackground(background);
		addressPanelTwo.setForeground(textColor);
		
		JLabel cityLabel = new JLabel("City: ");
		cityLabel.setBackground(background);
		cityLabel.setForeground(textColor);
		addressPanelTwo.add(cityLabel);
		
		if (editPersonalContact == true)
		{
			cityField = new JTextField(searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).getCity());
		}
		else
		{
			cityField = new JTextField();	
		}
		cityField.setMinimumSize(new Dimension(100, 25));
		cityField.setPreferredSize(new Dimension(100, 25));
		cityField.setEditable(true);
		cityField.setBackground(lightContent);
		cityField.setForeground(textColor);
		addressPanelTwo.add(cityField);
		
		JLabel stateLabel = new JLabel("State: ");
		stateLabel.setBackground(background);
		stateLabel.setForeground(textColor);
		addressPanelTwo.add(stateLabel);
		
		stateComboBox = new JComboBox<String>(statesList);
		if (editPersonalContact == true)
		{
			stateComboBox.setSelectedIndex(searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).getState());
		}
		else
		{
			stateComboBox.setSelectedIndex(0);
		}
		stateComboBox.setEditable(false);
		stateComboBox.setEnabled(true);
		stateComboBox.setBackground(lightContent);
		stateComboBox.setForeground(textColor);
		stateComboBox.addMouseListener(this);
		addressPanelTwo.add(stateComboBox);
		
		JLabel zipLabel = new JLabel("Zip: ");
		zipLabel.setBackground(background);
		zipLabel.setForeground(textColor);
		addressPanelTwo.add(zipLabel);
		
		if (editPersonalContact == true)
		{
			if (searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).getZip() == 0)
			{
				zipField = new JTextField();
			}
			else
			{
				zipField = new JTextField(searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).getZip()+"");
			}
			
		}
		else
		{
			zipField = new JTextField();	
		}
		zipField.setMinimumSize(new Dimension(75, 25));
		zipField.setPreferredSize(new Dimension(75, 25));
		zipField.setEditable(true);
		zipField.setBackground(lightContent);
		zipField.setForeground(textColor);
		addressPanelTwo.add(zipField);
				
		addNewContactInfoPanelConstraints.gridx = 0;
		addNewContactInfoPanelConstraints.gridy = 3;
		addNewContactInfoPanel.add(addressPanelTwo, addNewContactInfoPanelConstraints);

		addNewContactPanelConstraints.gridx = 0;
		addNewContactPanelConstraints.gridy = 0;
		addNewContactPanel.add(addNewContactInfoPanel, addNewContactPanelConstraints);
		
		//Personal Contact Notes JTextArea
		JPanel addNewContactNotesPanel = new JPanel();
		addNewContactNotesPanel.setBackground(background);
		addNewContactNotesPanel.setForeground(textColor);
		addNewContactNotesPanel.setLayout(new GridBagLayout());
		GridBagConstraints addNewContactNotesPanelConstraints = new GridBagConstraints();
		
		JLabel notesLabel = new JLabel("Notes");
		notesLabel.setBackground(background);
		notesLabel.setForeground(textColor);
		
		addNewContactNotesPanelConstraints.gridx = 0;
		addNewContactNotesPanelConstraints.gridy = 0;
		addNewContactNotesPanel.add(notesLabel, addNewContactNotesPanelConstraints);
		
		if (editPersonalContact == true)
		{
			addNewClientContactPersonalNotesTextArea = new JTextArea(searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).getNotes().getText());
		}
		else
		{
			addNewClientContactPersonalNotesTextArea = new JTextArea();	
		}
		addNewClientContactPersonalNotesTextArea.setEditable(true);
		addNewClientContactPersonalNotesTextArea.setLineWrap(true);
		addNewClientContactPersonalNotesTextArea.setCaretPosition(0);
		addNewClientContactPersonalNotesTextArea.setBackground(lightContent);
		addNewClientContactPersonalNotesTextArea.setForeground(textColor);
		
		JScrollPane addNewContactNotesScrollPane = new JScrollPane(addNewClientContactPersonalNotesTextArea, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		addNewContactNotesScrollPane.setMinimumSize(new Dimension(center.getWidth()/2, center.getHeight()/3));
		addNewContactNotesScrollPane.setPreferredSize(new Dimension(center.getWidth()/2, center.getHeight()/3));
		addNewContactNotesScrollPane.setBackground(background);
		addNewContactNotesScrollPane.setForeground(textColor);
		addNewContactNotesScrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		addNewContactNotesPanelConstraints.gridx = 0;
		addNewContactNotesPanelConstraints.gridy = 1;
		addNewContactNotesPanel.add(addNewContactNotesScrollPane, addNewContactNotesPanelConstraints);
		
		addNewContactPanelConstraints.gridx = 0;
		addNewContactPanelConstraints.gridy = 1;
		addNewContactPanel.add(addNewContactNotesPanel, addNewContactPanelConstraints);
		
		center.add(addNewContactPanel, addNewContactPanelConstraints);
		//Save/Cancel buttons 
		bottom.removeAll();
		addRemoveClientContactPersonalSave = new JButton("Save");
		addRemoveClientContactPersonalSave.setMinimumSize(new Dimension(center.getWidth()/8, 35));
		addRemoveClientContactPersonalSave.setPreferredSize(new Dimension(center.getWidth()/8, 35));
		addRemoveClientContactPersonalSave.setBackground(darkContent);
		addRemoveClientContactPersonalSave.setForeground(textColor);
		addRemoveClientContactPersonalSave.addMouseListener(this);
		bottom.add(addRemoveClientContactPersonalSave);
		
		addRemoveClientContactPersonalCancel = new JButton("Cancel");
		addRemoveClientContactPersonalCancel.setMinimumSize(new Dimension(center.getWidth()/8, 35));
		addRemoveClientContactPersonalCancel.setPreferredSize(new Dimension(center.getWidth()/8, 35));
		addRemoveClientContactPersonalCancel.setBackground(darkContent);
		addRemoveClientContactPersonalCancel.setForeground(textColor);
		addRemoveClientContactPersonalCancel.addMouseListener(this);
		bottom.add(addRemoveClientContactPersonalCancel);

		revalidate();
		repaint();
	}
	/**
	 * Saves the information in the textFields to be added to the client's personal contact list,
	 * also has SmalleyControl update the text files.
	 */
	public void createClientContactPersonalSaveButton()
	{
		if (editPersonalContact == true)
		{
			int validZip;
			try 
			{
				validZip = Integer.parseInt(zipField.getText());
			}
			catch(NumberFormatException badNumber)
			{
				validZip = 0;
			}
			searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).setName(nameClientContactPersonalField.getText());
			searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).setTitle(titleClientContactPersonalField.getText());
			searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).setAddress(addressField.getText());
			searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).setCity(cityField.getText());
			searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).setState(stateComboBox.getSelectedIndex());
			searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).setZip(validZip);
			searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).setPhone(phoneField.getText());
			searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).setFax(faxClientContactPersonalField.getText());
			searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).setEmail(emailField.getText());
			searchResults.get(selectedClientPos).getContactPersonalList().get(selectedClientPersonalContactPos).setNotes(addNewClientContactPersonalNotesTextArea);
			masterControl.editClientContactPersonalFile(searchResults.get(selectedClientPos).getAccountNumber(), searchResults.get(selectedClientPos).getContactPersonalList(), searchResults.get(selectedClientPos).getContactPersonalROIList());
			addRemoveClientContactButton();
			
			JTextArea moreInfoTextArea = new JTextArea("Personal contact edited successfully.");
			moreInfoTextArea.setEditable(false);
			moreInfoTextArea.setWrapStyleWord(true);
			moreInfoTextArea.setLineWrap(true);
			moreInfoTextArea.setBackground(darkContent);
			moreInfoTextArea.setForeground(textColor);
			JOptionPane.showMessageDialog(null, moreInfoTextArea, "Alert", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			int validZip;
			try 
			{
				validZip = Integer.parseInt(zipField.getText());
			}
			catch(NumberFormatException badNumber)
			{
				validZip = 0;
			}
				
			List<Contact> newClientContactPersonalList = new ArrayList<>(searchResults.get(selectedClientPos).getContactPersonalList());
			newClientContactPersonalList.add(new Contact(masterControl.findAvailableContactAccountNumber((ArrayList<Contact>)newClientContactPersonalList), 
					nameClientContactPersonalField.getText(), titleClientContactPersonalField.getText(), addressField.getText(), 
					cityField.getText(), stateComboBox.getSelectedIndex(), validZip, 
					phoneField.getText(), faxClientContactPersonalField.getText(), emailField.getText(), 
					addNewClientContactPersonalNotesTextArea));
			searchResults.get(selectedClientPos).setContactPersonalList((ArrayList<Contact>)newClientContactPersonalList);
			
			List<Boolean> newClientContactPersonalROIList = new ArrayList<>(searchResults.get(selectedClientPos).getContactPersonalROIList());
			newClientContactPersonalROIList.add(false);
			searchResults.get(selectedClientPos).setContactPersonalROIList((ArrayList<Boolean>)newClientContactPersonalROIList);
			masterControl.editClientContactPersonalFile(searchResults.get(selectedClientPos).getAccountNumber(), 
					(ArrayList<Contact>)searchResults.get(selectedClientPos).getContactPersonalList(), 
					(ArrayList<Boolean>) searchResults.get(selectedClientPos).getContactPersonalROIList());
			addRemoveClientContactButton();
			
			JTextArea moreInfoTextArea = new JTextArea("Personal contact saved successfully.");
			moreInfoTextArea.setEditable(false);
			moreInfoTextArea.setWrapStyleWord(true);
			moreInfoTextArea.setLineWrap(true);
			moreInfoTextArea.setBackground(darkContent);
			moreInfoTextArea.setForeground(textColor);
			JOptionPane.showMessageDialog(null, moreInfoTextArea, "Alert", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/**
	 * This method finds the selected client contact and initiates
	 * removal from ArrayLists, and initiates back end text files 
	 * to be updated via SmalleyControl.
	 * ArrayLists: Integer = the Account Numbers of the public contacts
	 * so they can be pulled up
	 * Boolean: true or false for if the contact is on the client's ROI.
	 */
	public void removeClientContactButton()
	{
		for (int i = 0; i < removeClientContactListButtonList.size(); i++)
		{
			if (removeClientContactListButtonList.get(i).isSelected())
			{
				if (searchResults.get(selectedClientPos).getContactPersonalList().size()-1 < i)
				{
					int positionInContactList =(i-(searchResults.get(selectedClientPos).getContactPersonalList().size()));
					int acctContactMasterList = searchResults.get(selectedClientPos).getContactList().get(positionInContactList);
					List<Integer> newClientContactList = new ArrayList<>();
					List<Boolean> newClientContactROIList = new ArrayList<>();
					for (int ii = 0; ii < searchResults.get(selectedClientPos).getContactList().size(); ii++)
					{
						if (searchResults.get(selectedClientPos).getContactList().get(ii) != acctContactMasterList)
						{	
							newClientContactList.add(searchResults.get(selectedClientPos).getContactList().get(ii));
							newClientContactROIList.add(searchResults.get(selectedClientPos).getContactROIList().get(ii));
						}
					}
					searchResults.get(selectedClientPos).setContactList((ArrayList<Integer>)newClientContactList);
					searchResults.get(selectedClientPos).setContactROIList((ArrayList<Boolean>)newClientContactROIList);
					masterControl.editClientContactFile(searchResults.get(selectedClientPos).getAccountNumber(), (ArrayList<Integer>)newClientContactList, (ArrayList<Boolean>)newClientContactROIList);
					addRemoveClientContactButton();
				}
				else
				{
					Contact personalContactToRemove = searchResults.get(selectedClientPos).getContactPersonalList().get(i);
					List<Contact> newClientContactPersonalList = new ArrayList<>();
					List<Boolean> newClientContactPersonalROIList = new ArrayList<>();
					for (int ii = 0; ii < searchResults.get(selectedClientPos).getContactPersonalList().size(); ii++)
					{
						if (searchResults.get(selectedClientPos).getContactPersonalList().get(ii).getAccountNumber() != personalContactToRemove.getAccountNumber())
						{
							newClientContactPersonalList.add(searchResults.get(selectedClientPos).getContactPersonalList().get(ii));
							newClientContactPersonalROIList.add(searchResults.get(selectedClientPos).getContactPersonalROIList().get(ii));
						}
						else
						{
							try
							{
								Files.delete(Paths.get("src/resources/clientcontacts/client" + searchResults.get(selectedClientPos).getAccountNumber() + "contactspersonal" + searchResults.get(selectedClientPos).getContactPersonalList().get(ii).getAccountNumber() + "notes.txt"));	
							}
							catch(NoSuchFileException e)
							{
								e.printStackTrace();
							}
							catch(IOException e)
							{
								e.printStackTrace();
							}
							
						}
					}
					searchResults.get(selectedClientPos).setContactPersonalList((ArrayList<Contact>) newClientContactPersonalList);
					searchResults.get(selectedClientPos).setContactPersonalROIList((ArrayList<Boolean>) newClientContactPersonalROIList);
					masterControl.editClientContactPersonalFile(searchResults.get(selectedClientPos).getAccountNumber(), (ArrayList<Contact>) newClientContactPersonalList, (ArrayList<Boolean>)newClientContactPersonalROIList);
					addRemoveClientContactButton();
				}
				JTextArea moreInfoTextArea = new JTextArea("Contact deleted successfully.");
				moreInfoTextArea.setEditable(false);
				moreInfoTextArea.setWrapStyleWord(true);
				moreInfoTextArea.setLineWrap(true);
				moreInfoTextArea.setBackground(darkContent);
				moreInfoTextArea.setForeground(textColor);
				JOptionPane.showMessageDialog(null, moreInfoTextArea, "Alert", JOptionPane.INFORMATION_MESSAGE);
			}
		}	
	}
	/**
	 * When one of the clientContacts "More" buttons are clicked,
	 * this method picks up which button assigned to which contact was
	 * clicked, then displays an alert with more information about the contact.
	 * @param e
	 * @return None
	 */
	public void clientContactMoreInfoButton(JButton e)
	{
		for (int i = 0; i < clientContactMoreButtonList.size(); i++)
		{
			if (e.equals(clientContactMoreButtonList.get(i)))
			{
				if (searchResults.get(selectedClientPos).getContactPersonalList().size()-1 < i)
				{
					int positionInContactList =(i-(searchResults.get(selectedClientPos).getContactPersonalList().size()));
					int acctContactMasterList = searchResults.get(selectedClientPos).getContactList().get(positionInContactList);
					
					contactMasterList = masterControl.getContactMasterList();
					
					for (int ii = 0; ii < contactMasterList.size(); ii++)
					{
						if (contactMasterList.get(ii).getAccountNumber() == acctContactMasterList)
						{
							Contact selectedContact = contactMasterList.get(ii);
							JTextArea moreInfoTextArea = new JTextArea("Phone: " + selectedContact.getPhone()
									+ "\r\n" + "\r\n" + "Fax: " +  selectedContact.getFax()
									 + "\r\n" + "\r\n"+ "Email: "  + selectedContact.getEmail()
									 + "\r\n" + "\r\n"+ "Address: "  + selectedContact.getAddressFull()
									 + "\r\n" + "\r\n"+ "Notes: " + "\r\n" + "\r\n" + selectedContact.getNotes().getText());
							moreInfoTextArea.setEditable(false);
							moreInfoTextArea.setLineWrap(true);
							moreInfoTextArea.setBackground(lightContent);
							moreInfoTextArea.setForeground(textColor);
							
							JScrollPane moreInfoScrollPane = new JScrollPane(moreInfoTextArea);
							moreInfoScrollPane.setMinimumSize(new Dimension(center.getWidth()/2, center.getHeight()/3));
							moreInfoScrollPane.setPreferredSize(new Dimension(center.getWidth()/2, center.getHeight()/3));
							moreInfoScrollPane.setBackground(background);
							moreInfoScrollPane.setForeground(textColor);
							moreInfoScrollPane.setBorder(BorderFactory.createEmptyBorder());
							
							JOptionPane.showMessageDialog(null, moreInfoScrollPane, selectedContact.getName() + " PUBLIC CONTACT INFORMATION", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
				else
				{					
					Contact personalContactToRemove = searchResults.get(selectedClientPos).getContactPersonalList().get(i);
					JTextArea moreInfoTextArea = new JTextArea("Phone: " + personalContactToRemove.getPhone()
							+ "\r\n" + "\r\n" + "Fax: " +  personalContactToRemove.getFax()
							 + "\r\n" + "\r\n"+ "Email: "  + personalContactToRemove.getEmail()
							 + "\r\n" + "\r\n"+ "Address: "  + personalContactToRemove.getAddressFull()
							 + "\r\n" + "\r\n"+ "Notes: " + "\r\n" + "\r\n" + personalContactToRemove.getNotes().getText());
					moreInfoTextArea.setEditable(false);
					moreInfoTextArea.setLineWrap(true);
					moreInfoTextArea.setBackground(lightContent);
					moreInfoTextArea.setForeground(textColor);
					
					JScrollPane moreInfoScrollPane = new JScrollPane(moreInfoTextArea);
					moreInfoScrollPane.setMinimumSize(new Dimension(center.getWidth()/2, center.getHeight()/3));
					moreInfoScrollPane.setPreferredSize(new Dimension(center.getWidth()/2, center.getHeight()/3));
					moreInfoScrollPane.setBackground(lightContent);
					moreInfoScrollPane.setForeground(textColor);
					moreInfoScrollPane.setBorder(BorderFactory.createEmptyBorder());
					
					JOptionPane.showMessageDialog(null, moreInfoScrollPane, personalContactToRemove.getName()+ " PERSONAL CONTACT INFORMATION", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}
	/**
	 * Enables the ROI JRadioButtons to be selected, when the
	 * button is originally hit, it'll say "Edit", then "Save"
	 * hit the button when it says "Save" to save the changes you made
	 * to the JRadioButtons. This will work as long as the Client's contact
	 * list isn't empty.
	 */
	public void editClientContactROIStatusButton()
	{
		if (!(searchResults.get(selectedClientPos).getContactPersonalList().isEmpty()) 
				|| (!searchResults.get(selectedClientPos).getContactList().isEmpty()))
		{
			if (editRoiStatusButton.getText().equals("Edit"))
			{
				editRoiStatusButton.setText("Save");
				for (JRadioButton a: editROIButtonList)
				{
					a.setEnabled(true);
				}
				searchBar.setEnabled(false);
				searchBar.removeMouseListener(this);
				
				searchStart.setEnabled(false);
				searchStart.removeMouseListener(this);
				
				editClientInfoButton.setEnabled(false);
				editClientInfoButton.removeMouseListener(this);
				
				exitClientInfoButton.setEnabled(false);
				exitClientInfoButton.removeMouseListener(this);
				
				editNotesClientButton.setEnabled(false);
				editNotesClientButton.removeMouseListener(this);
				
				addRemoveContactClientButton.setEnabled(false);
				addRemoveContactClientButton.removeMouseListener(this);
			}
			else if(editRoiStatusButton.getText().equals("Save"))
			{
				editRoiStatusButton.setText("Edit");
				
				searchBar.setEnabled(true);
				searchBar.addMouseListener(this);
				
				searchStart.setEnabled(true);
				searchStart.addMouseListener(this);
				
				editClientInfoButton.setEnabled(true);
				editClientInfoButton.addMouseListener(this);
				
				exitClientInfoButton.setEnabled(true);
				exitClientInfoButton.addMouseListener(this);
				
				editNotesClientButton.setEnabled(true);
				editNotesClientButton.addMouseListener(this);
				
				addRemoveContactClientButton.setEnabled(true);
				addRemoveContactClientButton.addMouseListener(this);
				
				for (JRadioButton a: editROIButtonList)
				{
					a.setEnabled(false);
				}
				//Personal Contacts
				List<Boolean> newPersonalROIList = new ArrayList<>();
				for (int i = 0; i < searchResults.get(selectedClientPos).getContactPersonalROIList().size(); i++)
				{
					if (editROIButtonList.get(i).isSelected())
					{
						newPersonalROIList.add(true);
					}
					else
					{
						newPersonalROIList.add(false);
					}
				}
				searchResults.get(selectedClientPos).setContactPersonalROIList((ArrayList<Boolean>) newPersonalROIList);
				masterControl.editClientContactPersonalFile(searchResults.get(selectedClientPos).getAccountNumber(),(ArrayList<Contact>) searchResults.get(selectedClientPos).getContactPersonalList(), (ArrayList<Boolean>) newPersonalROIList);
								
				//Public Contacts
				List<Boolean> newROIList = new ArrayList<>();
				for (int i = 0; i < searchResults.get(selectedClientPos).getContactROIList().size(); i++)
				{
					if (editROIButtonList.get(i+(searchResults.get(selectedClientPos).getContactPersonalROIList().size())).isSelected())
					{
						newROIList.add(true);
					}
					else
					{
						newROIList.add(false);
					}
				}
				searchResults.get(selectedClientPos).setContactROIList((ArrayList<Boolean>) newROIList);
				masterControl.editClientContactFile(searchResults.get(selectedClientPos).getAccountNumber(), searchResults.get(selectedClientPos).getContactList(), (ArrayList<Boolean>) newROIList);
				this.requestFocusInWindow();
			}
		}
		else
		{
			System.out.println("No contacts.");
			JTextArea moreInfoTextArea = new JTextArea("No Contacts exist for this Client. Please add a Contact down below under 'Edit Contacts'");
			moreInfoTextArea.setEditable(false);
			moreInfoTextArea.setWrapStyleWord(true);
			moreInfoTextArea.setLineWrap(true);
			moreInfoTextArea.setBackground(darkContent);
			moreInfoTextArea.setForeground(textColor);
			
			JOptionPane.showMessageDialog(null, moreInfoTextArea, "Alert", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/**
	 * Menu to either add new clients or delete existing ones.
	 * 
	 */
	public void addRemoveClientButton()
	{
		bottom.removeAll();
		center.removeAll();
		searchBar.setVisible(false);
		searchBar.setEnabled(false);
		searchBar.removeMouseListener(this);
		searchStart.setVisible(false);
		searchStart.setEnabled(false);
		searchStart.removeMouseListener(this);
		clientMasterListGUI.clear();
		clientMasterList = masterControl.getClientMasterList();
		
		JPanel existingClientsPanel = new JPanel();
		existingClientsPanel.setBackground(background);
		existingClientsPanel.setForeground(textColor);
		existingClientsPanel.setLayout(new GridBagLayout());
		GridBagConstraints existingClientsPanelConstraints = new GridBagConstraints();
		
		JPanel columnTitles = new JPanel();
		columnTitles.setBackground(lightContent);
		columnTitles.setForeground(textColor);
		columnTitles.setLayout(new GridLayout());
		columnTitles.setMaximumSize(new Dimension(center.getWidth()-25, 35));
		columnTitles.setPreferredSize(new Dimension(center.getWidth()-25, 35));
		
		JLabel spacerLabel = new JLabel("");
		spacerLabel.setBackground(lightContent);
		spacerLabel.setForeground(textColor);
		columnTitles.add(spacerLabel);
		
		JLabel nameLabel = new JLabel("Name");
		nameLabel.setBackground(lightContent);
		nameLabel.setForeground(textColor);
		columnTitles.add(nameLabel);
		
		JLabel acctNumberLabel = new JLabel("Account Number");
		acctNumberLabel.setBackground(lightContent);
		acctNumberLabel.setForeground(textColor);
		columnTitles.add(acctNumberLabel);
		
		columnTitles.setOpaque(true);
		columnTitles.setBackground(lightContent);
		columnTitles.setForeground(textColor);
				
		boolean colorSwitch = true;
		int numberOfClientsCounter = 0;
		for (int i = 0; i < clientMasterList.size(); i++)
		{
			JPanel clientMainDisplayLinePanel = new JPanel();
			clientMainDisplayLinePanel.setLayout(new GridLayout());
			clientMainDisplayLinePanel.setMaximumSize(new Dimension(center.getWidth()-25, 35));
			clientMainDisplayLinePanel.setPreferredSize(new Dimension(center.getWidth()-25, 35));
			
			if (colorSwitch)
			{
				clientMainDisplayLinePanel.setBackground(darkContent);
				clientMainDisplayLinePanel.setForeground(textColor);
				colorSwitch = false;
			}
			else
			{
				clientMainDisplayLinePanel.setBackground(lightContent);
				clientMainDisplayLinePanel.setForeground(textColor);
				colorSwitch = true;
			}
			clientMasterListGUI.add(clientMainDisplayLinePanel);

			JRadioButton selectionClientMainButton = new JRadioButton("Select");
			selectionClientMainButton.setBackground(clientMainDisplayLinePanel.getBackground());
			selectionClientMainButton.setForeground(clientMainDisplayLinePanel.getForeground());
			selectionClientMainButton.addActionListener(this);
			clientMasterListGUI.get(i).add(selectionClientMainButton);
			
			JTextField nameClientField = new JTextField((clientMasterList.get(i).getNameFull()));
			nameClientField.setBorder(BorderFactory.createEmptyBorder());
			nameClientField.setEditable(false);
			nameClientField.setCaretPosition(0);
			nameClientField.setBackground(clientMainDisplayLinePanel.getBackground());
			nameClientField.setForeground(clientMainDisplayLinePanel.getForeground());
			clientMasterListGUI.get(i).add(nameClientField);
			
			JTextField acctNumberClientField = new JTextField(Integer.toString(clientMasterList.get(i).getAccountNumber()));
			acctNumberClientField.setBorder(BorderFactory.createEmptyBorder());
			acctNumberClientField.setEditable(false);
			acctNumberClientField.setCaretPosition(0);
			acctNumberClientField.setBackground(clientMainDisplayLinePanel.getBackground());
			acctNumberClientField.setForeground(clientMainDisplayLinePanel.getForeground());
			clientMasterListGUI.get(i).add(acctNumberClientField);
			
			clientMasterListGUI.get(i).setOpaque(true);
			clientMasterListGUI.get(i).setBackground(clientMainDisplayLinePanel.getBackground());
			clientMasterListGUI.get(i).setForeground(clientMainDisplayLinePanel.getForeground());
			
			numberOfClientsCounter++;
		}
		if ((numberOfClientsCounter > 15 && center.getHeight() < 800) || (numberOfClientsCounter > 28 && center.getHeight() > 800))
		{
			existingClientsPanelConstraints.gridx = 0;
			existingClientsPanelConstraints.gridy = 0;
			existingClientsPanelConstraints.weightx = 1.0;
			existingClientsPanelConstraints.weighty = 1.0;
			existingClientsPanel.add(columnTitles, existingClientsPanelConstraints);
			for (int i = 0; i < clientMasterList.size(); i++)
			{
				existingClientsPanelConstraints.gridx = 0;
				existingClientsPanelConstraints.gridy = i+1;
				existingClientsPanelConstraints.weightx = 1.0;
				existingClientsPanelConstraints.weighty = 1.0;
				existingClientsPanel.add(clientMasterListGUI.get(i), existingClientsPanelConstraints);	
			}
			JScrollPane clientMainScrollPane = new JScrollPane(existingClientsPanel,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			clientMainScrollPane.getVerticalScrollBar().setUnitIncrement(8);
			clientMainScrollPane.setBackground(background);
			clientMainScrollPane.setForeground(textColor);
			clientMainScrollPane.setBorder(BorderFactory.createEmptyBorder());
			center.add(clientMainScrollPane);
		}
		else 
		{
			center.add(columnTitles);
			for (int i = 0; i < clientMasterList.size(); i++)
			{
				center.add(clientMasterListGUI.get(i));
			}
			int amountOfFillerSlots = 0;
			if (numberOfClientsCounter <= 15 && center.getHeight() < 800)
			{
				amountOfFillerSlots = 15;
			}
			else
			{
				amountOfFillerSlots = 28;
			}
			for (int i = 0; i < amountOfFillerSlots-clientMasterListGUI.size(); i++)
			{
				JPanel fillerPanel = new JPanel();
				fillerPanel.setLayout(new GridLayout());
				fillerPanel.setMaximumSize(new Dimension(center.getWidth()-25, 35));
				fillerPanel.setPreferredSize(new Dimension(center.getWidth()-25, 35));
				
				if (colorSwitch)
				{
					fillerPanel.setBackground(darkContent);
					fillerPanel.setForeground(textColor);
					colorSwitch = false;
				}
				else
				{
					fillerPanel.setBackground(lightContent);
					fillerPanel.setForeground(textColor);
					colorSwitch = true;
				}
				center.add(fillerPanel);
			}
		}
		
		createNewClientButton = new JButton("Add New Client");
		createNewClientButton.setMinimumSize(new Dimension(center.getWidth()/6, 35));
		createNewClientButton.setPreferredSize(new Dimension(center.getWidth()/6, 35));
		createNewClientButton.setBackground(darkContent);
		createNewClientButton.setForeground(textColor);
		createNewClientButton.addMouseListener(this);
		bottom.add(createNewClientButton);
		
		deleteExistingClientButton = new JButton("Delete Client");
		deleteExistingClientButton.setMinimumSize(new Dimension(center.getWidth()/6, 35));
		deleteExistingClientButton.setPreferredSize(new Dimension(center.getWidth()/6, 35));
		deleteExistingClientButton.setBackground(darkContent);
		deleteExistingClientButton.setForeground(textColor);
		deleteExistingClientButton.addMouseListener(this);
		bottom.add(deleteExistingClientButton);
		
		createClientExitButton = new JButton("Exit");
		createClientExitButton.setMinimumSize(new Dimension(center.getWidth()/6, 35));
		createClientExitButton.setPreferredSize(new Dimension(center.getWidth()/6, 35));
		createClientExitButton.setBackground(darkContent);
		createClientExitButton.setForeground(textColor);
		createClientExitButton.addMouseListener(this);
		bottom.add(createClientExitButton);
		
		revalidate();
		repaint();
	}
	/**
	 * Pulls up a bunch of JTextFields to add information into to create a new Client.
	 */
	public void createNewClientButton()
	{
		center.removeAll();
		bottom.removeAll();
		
		JPanel addNewClientPanel = new JPanel();
		addNewClientPanel.setBackground(background);
		addNewClientPanel.setForeground(textColor);
		addNewClientPanel.setLayout(new GridBagLayout());
		GridBagConstraints addNewClienttPanelConstraints = new GridBagConstraints();
		
		JPanel addNewClientInfoPanel = new JPanel();
		addNewClientInfoPanel.setBackground(background);
		addNewClientInfoPanel.setForeground(textColor);
		addNewClientInfoPanel.setLayout(new GridBagLayout());
		GridBagConstraints addNewClienttInfoPanelConstraints = new GridBagConstraints();

		JPanel namePanel = new JPanel();
		namePanel.setBackground(background);
		namePanel.setForeground(textColor);
		
		JLabel fNameLabel = new JLabel("First Name: ");
		fNameLabel.setBackground(background);
		fNameLabel.setForeground(textColor);
		namePanel.add(fNameLabel);
		
		fNameClientField = new JTextField();	
		fNameClientField.setMinimumSize(new Dimension(200, 25));
		fNameClientField.setPreferredSize(new Dimension(200, 25));
		fNameClientField.setEditable(true);
		fNameClientField.setBackground(lightContent);
		fNameClientField.setForeground(textColor);
		namePanel.add(fNameClientField);
		
		JLabel lNameLabel = new JLabel("Last Name: ");
		lNameLabel.setBackground(background);
		lNameLabel.setForeground(textColor);
		namePanel.add(lNameLabel);
		
		lNameClientField = new JTextField();	
		lNameClientField.setMinimumSize(new Dimension(200, 25));
		lNameClientField.setPreferredSize(new Dimension(200, 25));
		lNameClientField.setEditable(true);
		lNameClientField.setBackground(lightContent);
		lNameClientField.setForeground(textColor);
		namePanel.add(lNameClientField);
							
		addNewClienttInfoPanelConstraints.gridx = 0;
		addNewClienttInfoPanelConstraints.gridy = 0;
		addNewClientInfoPanel.add(namePanel, addNewClienttInfoPanelConstraints);
		
		JPanel phoneEmailPanel = new JPanel();
		phoneEmailPanel.setBackground(background);
		phoneEmailPanel.setForeground(textColor);
		
		JLabel phoneLabel = new JLabel("Phone: ");
		phoneLabel.setBackground(background);
		phoneLabel.setForeground(textColor);
		phoneEmailPanel.add(phoneLabel);
		
		phoneField = new JTextField();	
		phoneField.setMinimumSize(new Dimension(100, 25));
		phoneField.setPreferredSize(new Dimension(100, 25));
		phoneField.setEditable(true);
		phoneField.setBackground(lightContent);
		phoneField.setForeground(textColor);
		phoneEmailPanel.add(phoneField);
		
		JLabel emailLabel = new JLabel("Email: ");
		emailLabel.setBackground(background);
		emailLabel.setForeground(textColor);
		phoneEmailPanel.add(emailLabel);
		
		emailField = new JTextField();	
		emailField.setMinimumSize(new Dimension(300, 25));
		emailField.setPreferredSize(new Dimension(300, 25));
		emailField.setEditable(true);
		emailField.setBackground(lightContent);
		emailField.setForeground(textColor);
		phoneEmailPanel.add(emailField);
		
		addNewClienttInfoPanelConstraints.gridx = 0;
		addNewClienttInfoPanelConstraints.gridy = 1;
		addNewClientInfoPanel.add(phoneEmailPanel, addNewClienttInfoPanelConstraints);
		
		JPanel addressPanelOne = new JPanel();
		addressPanelOne.setBackground(background);
		addressPanelOne.setForeground(textColor);
		
		JLabel addressLabel = new JLabel("Address: ");
		addressLabel.setBackground(background);
		addressLabel.setForeground(textColor);
		addressPanelOne.add(addressLabel);
		
		addressField = new JTextField();	
		addressField.setMinimumSize(new Dimension(300, 25));
		addressField.setPreferredSize(new Dimension(300, 25));
		addressField.setEditable(true);
		addressField.setBackground(lightContent);
		addressField.setForeground(textColor);
		addressPanelOne.add(addressField);
				
		addNewClienttInfoPanelConstraints.gridx = 0;
		addNewClienttInfoPanelConstraints.gridy = 2;
		addNewClientInfoPanel.add(addressPanelOne, addNewClienttInfoPanelConstraints);
		
		JPanel addressPanelTwo = new JPanel();
		addressPanelTwo.setBackground(background);
		addressPanelTwo.setForeground(textColor);
		
		JLabel cityLabel = new JLabel("City: ");
		cityLabel.setBackground(background);
		cityLabel.setForeground(textColor);
		addressPanelTwo.add(cityLabel);
		
		cityField = new JTextField();	
		cityField.setMinimumSize(new Dimension(100, 25));
		cityField.setPreferredSize(new Dimension(100, 25));
		cityField.setEditable(true);
		cityField.setBackground(lightContent);
		cityField.setForeground(textColor);
		addressPanelTwo.add(cityField);
		
		JLabel stateLabel = new JLabel("State: ");
		stateLabel.setBackground(background);
		stateLabel.setForeground(textColor);
		addressPanelTwo.add(stateLabel);
		
		stateComboBox = new JComboBox<String>(statesList);
		stateComboBox.setSelectedIndex(0);
		stateComboBox.setEditable(false);
		stateComboBox.setEnabled(true);
		stateComboBox.setBackground(lightContent);
		stateComboBox.setForeground(textColor);
		stateComboBox.addMouseListener(this);
		addressPanelTwo.add(stateComboBox);
		
		JLabel zipLabel = new JLabel("Zip: ");
		zipLabel.setBackground(background);
		zipLabel.setForeground(textColor);
		addressPanelTwo.add(zipLabel);
		
		zipField = new JTextField();	
		zipField.setMinimumSize(new Dimension(75, 25));
		zipField.setPreferredSize(new Dimension(75, 25));
		zipField.setEditable(true);
		zipField.setBackground(lightContent);
		zipField.setForeground(textColor);
		addressPanelTwo.add(zipField);
				
		addNewClienttInfoPanelConstraints.gridx = 0;
		addNewClienttInfoPanelConstraints.gridy = 3;
		addNewClientInfoPanel.add(addressPanelTwo, addNewClienttInfoPanelConstraints);

		addNewClienttPanelConstraints.gridx = 0;
		addNewClienttPanelConstraints.gridy = 0;
		addNewClientPanel.add(addNewClientInfoPanel, addNewClienttPanelConstraints);
		
		JPanel addNewContactNotesPanel = new JPanel();
		addNewContactNotesPanel.setBackground(background);
		addNewContactNotesPanel.setForeground(textColor);
		addNewContactNotesPanel.setLayout(new GridBagLayout());
		GridBagConstraints addNewContactNotesPanelConstraints = new GridBagConstraints();
		
		JLabel notesLabel = new JLabel("Notes");
		notesLabel.setBackground(background);
		notesLabel.setForeground(textColor);
		
		addNewContactNotesPanelConstraints.gridx = 0;
		addNewContactNotesPanelConstraints.gridy = 0;
		addNewContactNotesPanel.add(notesLabel, addNewContactNotesPanelConstraints);
		
		createNewClientNotesTextArea = new JTextArea();	
		createNewClientNotesTextArea.setEditable(true);
		createNewClientNotesTextArea.setLineWrap(true);
		createNewClientNotesTextArea.setCaretPosition(0);
		createNewClientNotesTextArea.setBackground(lightContent);
		createNewClientNotesTextArea.setForeground(textColor);
		
		JScrollPane addNewContactNotesScrollPane = new JScrollPane(createNewClientNotesTextArea, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		addNewContactNotesScrollPane.setMinimumSize(new Dimension(center.getWidth()/2, center.getHeight()/3));
		addNewContactNotesScrollPane.setPreferredSize(new Dimension(center.getWidth()/2, center.getHeight()/3));
		addNewContactNotesScrollPane.setBackground(background);
		addNewContactNotesScrollPane.setForeground(textColor);
		addNewContactNotesScrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		addNewContactNotesPanelConstraints.gridx = 0;
		addNewContactNotesPanelConstraints.gridy = 1;
		addNewContactNotesPanel.add(addNewContactNotesScrollPane, addNewContactNotesPanelConstraints);
		
		addNewClienttPanelConstraints.gridx = 0;
		addNewClienttPanelConstraints.gridy = 1;
		addNewClientPanel.add(addNewContactNotesPanel, addNewClienttPanelConstraints);
		
		center.add(addNewClientPanel, addNewClienttPanelConstraints);
		
		bottom.removeAll();
		createNewClientSaveButton = new JButton("Save");
		createNewClientSaveButton.setMinimumSize(new Dimension(center.getWidth()/8, 35));
		createNewClientSaveButton.setPreferredSize(new Dimension(center.getWidth()/8, 35));
		createNewClientSaveButton.setBackground(darkContent);
		createNewClientSaveButton.setForeground(textColor);
		createNewClientSaveButton.addMouseListener(this);
		bottom.add(createNewClientSaveButton);
		
		createNewClientCancelButton = new JButton("Cancel");
		createNewClientCancelButton.setMinimumSize(new Dimension(center.getWidth()/8, 35));
		createNewClientCancelButton.setPreferredSize(new Dimension(center.getWidth()/8, 35));
		createNewClientCancelButton.setBackground(darkContent);
		createNewClientCancelButton.setForeground(textColor);
		createNewClientCancelButton.addMouseListener(this);
		bottom.add(createNewClientCancelButton);

		revalidate();
		repaint();
			
	}
	/**
	 * Checks to make sure at least the client name has been filled out.
	 * then saves the client into a Client Object into the ArrayLists
	 * Then tells SmalleyControl to add it to the text files.
	 */
	public void saveNewClientButton()
	{	
		boolean hasAnEmptyField = false;
		if (fNameClientField.getText().equals("") || fNameClientField.getText().equals(" "))
		{
			hasAnEmptyField = true;
		}
		if (lNameClientField.getText().equals("") || lNameClientField.getText().equals(" "))
		{
			hasAnEmptyField = true;
		}
		if (hasAnEmptyField)
		{
			JTextArea moreInfoTextArea = new JTextArea("Please make sure to fill out the form in full.");
			moreInfoTextArea.setEditable(false);
			moreInfoTextArea.setWrapStyleWord(true);
			moreInfoTextArea.setLineWrap(true);
			moreInfoTextArea.setBackground(darkContent);
			moreInfoTextArea.setForeground(textColor);
			
			JOptionPane.showMessageDialog(null, moreInfoTextArea, "Alert", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			int validZip;
			try
			{
				validZip = Integer.parseInt(zipField.getText());
			}
			catch(NumberFormatException badNumber)
			{
				validZip = 0;
			}
			
			Client newClient = new Client(masterControl.findAvailableClientAccountNumber((ArrayList<Client>)clientMasterList), 
					fNameClientField.getText(), lNameClientField.getText(), addressField.getText(), 
					cityField.getText(), stateComboBox.getSelectedIndex(), validZip,
					phoneField.getText(), emailField.getText(), " ");
			newClient.setClientActive(true);
			newClient.setClientNotes(createNewClientNotesTextArea);
			clientMasterList.add(newClient);
			clientMasterList = masterControl.editClientFile((ArrayList<Client>)clientMasterList);
		}
		addRemoveClientButton();
	}
	/**
	 * Finds selected client and deletes it from ArrayLists and has SmalleyControl
	 * delete all related text files to the selected client.
	 */
	public void deleteExistingClientButton()
	{
		if (selectedClientToDeletePos != -1)		
		{
			clientMasterList.remove(selectedClientToDeletePos);
			clientMasterList = masterControl.editClientFile((ArrayList<Client>)clientMasterList);
			selectedClientToDeletePos = -1;
			addRemoveClientButton();
		}
		else
		{
			JTextArea moreInfoTextArea = new JTextArea("Please select a Client to delete.");
			moreInfoTextArea.setEditable(false);
			moreInfoTextArea.setWrapStyleWord(true);
			moreInfoTextArea.setLineWrap(true);
			moreInfoTextArea.setBackground(darkContent);
			moreInfoTextArea.setForeground(textColor);
			
			JOptionPane.showMessageDialog(null, moreInfoTextArea, "Alert", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/**
	 * Returns user to the main menu.
	 */
	public void exitAddRemoveClientButton()
	{
		searchBar.setVisible(true);
		searchBar.setEnabled(true);
		searchBar.addMouseListener(this);
		searchStart.setVisible(true);
		searchStart.setEnabled(true);
		searchStart.addMouseListener(this);

		center.removeAll();
		bottom.removeAll();
		
		addRemoveClientButton.setBackground(darkContent);
		addRemoveClientButton.setForeground(textColor);
		bottom.add(addRemoveClientButton);
		
		contactsMainButton.setBackground(darkContent);
		contactsMainButton.setForeground(textColor);
		bottom.add(contactsMainButton);

		revalidate();
		repaint();	
	}
	/**
	 * Menu for viewing information on public contacts.
	 * Has a JRadioButton for selecting a contact to edit or delete,
	 * also has a button for creating new contacts.
	 * This is where public contacts information can be edited, and nowhere
	 * else.
	 */
	public void contactPublicMainButton()
	{
		contactMasterListGUI.clear();
		contactPublicMoreInfoButtonList.clear();
		searchBar.setVisible(false);
		searchBar.setEnabled(false);
		searchBar.removeMouseListener(this);
		searchStart.setVisible(false);
		searchStart.setEnabled(false);
		searchStart.removeMouseListener(this);
		center.removeAll();
		contactMasterList = masterControl.getContactMasterList();
		
		JPanel contactMainPanel = new JPanel();
		contactMainPanel.setBackground(background);
		contactMainPanel.setForeground(textColor);
		contactMainPanel.setLayout(new GridBagLayout());
		GridBagConstraints contactMainPanelConstraints = new GridBagConstraints();
		
		JPanel columnTitles = new JPanel();
		columnTitles.setBackground(lightContent);
		columnTitles.setForeground(textColor);
		columnTitles.setLayout(new GridLayout());
		columnTitles.setMaximumSize(new Dimension(center.getWidth()-25, 35));
		columnTitles.setPreferredSize(new Dimension(center.getWidth()-25, 35));
		
		JLabel spacerLabel = new JLabel("");
		spacerLabel.setBackground(lightContent);
		spacerLabel.setForeground(textColor);
		columnTitles.add(spacerLabel);
		
		JLabel nameLabel = new JLabel("Name");
		nameLabel.setBackground(lightContent);
		nameLabel.setForeground(textColor);
		columnTitles.add(nameLabel);
		
		JLabel titleLabel = new JLabel("Title");
		titleLabel.setBackground(lightContent);
		titleLabel.setForeground(textColor);
		columnTitles.add(titleLabel);
		
		JLabel phoneLabel = new JLabel("Phone Number");
		phoneLabel.setBackground(lightContent);
		phoneLabel.setForeground(textColor);
		columnTitles.add(phoneLabel);
		
		JLabel faxLabel = new JLabel("Fax");
		faxLabel.setBackground(lightContent);
		faxLabel.setForeground(textColor);
		columnTitles.add(faxLabel);
		
		JLabel emailLabel = new JLabel("Email");
		emailLabel.setBackground(lightContent);
		emailLabel.setForeground(textColor);
		columnTitles.add(emailLabel);
		
		JLabel spacerLabelTwo = new JLabel("");
		spacerLabelTwo.setBackground(lightContent);
		spacerLabelTwo.setForeground(textColor);
		columnTitles.add(spacerLabelTwo);

		columnTitles.setOpaque(true);
		columnTitles.setBackground(lightContent);
		columnTitles.setForeground(textColor);
				
		boolean colorSwitch = true;
		int numberOfContactsCounter = 0;
		for (int i = 0; i < contactMasterList.size(); i++)
		{
			JPanel contactMainDisplayLinePanel = new JPanel();
			contactMainDisplayLinePanel.setLayout(new GridLayout());
			contactMainDisplayLinePanel.setMaximumSize(new Dimension(center.getWidth()-25, 35));
			contactMainDisplayLinePanel.setPreferredSize(new Dimension(center.getWidth()-25, 35));
			
			if (colorSwitch)
			{
				contactMainDisplayLinePanel.setBackground(darkContent);
				contactMainDisplayLinePanel.setForeground(textColor);
				colorSwitch = false;
			}
			else
			{
				contactMainDisplayLinePanel.setBackground(lightContent);
				contactMainDisplayLinePanel.setForeground(textColor);
				colorSwitch = true;
			}
			contactMasterListGUI.add(contactMainDisplayLinePanel);

			selectionContactPublicButton = new JRadioButton("Select");
			contactMasterList.get(i).setSelectionButton(selectionContactPublicButton);
			selectionContactPublicButton.setBackground(contactMainDisplayLinePanel.getBackground());
			selectionContactPublicButton.setForeground(contactMainDisplayLinePanel.getForeground());
			
			selectionContactPublicButton.addActionListener(this);
			contactMasterListGUI.get(i).add(selectionContactPublicButton);
			
			nameContactPublic = new JTextField((contactMasterList.get(i).getName()));
			nameContactPublic.setBorder(BorderFactory.createEmptyBorder());
			nameContactPublic.setEditable(false);
			nameContactPublic.setCaretPosition(0);
			nameContactPublic.setBackground(contactMainDisplayLinePanel.getBackground());
			nameContactPublic.setForeground(contactMainDisplayLinePanel.getForeground());
			contactMasterListGUI.get(i).add(nameContactPublic);
			
			titleContactPublic = new JTextField((contactMasterList.get(i).getTitle()));
			titleContactPublic.setBorder(BorderFactory.createEmptyBorder());
			titleContactPublic.setEditable(false);
			titleContactPublic.setCaretPosition(0);
			titleContactPublic.setBackground(contactMainDisplayLinePanel.getBackground());
			titleContactPublic.setForeground(contactMainDisplayLinePanel.getForeground());
			contactMasterListGUI.get(i).add(titleContactPublic);
			
			phoneContactPublic = new JTextField((contactMasterList.get(i).getPhone()));
			phoneContactPublic.setBorder(BorderFactory.createEmptyBorder());
			phoneContactPublic.setEditable(false);
			phoneContactPublic.setCaretPosition(0);
			phoneContactPublic.setBackground(contactMainDisplayLinePanel.getBackground());
			phoneContactPublic.setForeground(contactMainDisplayLinePanel.getForeground());
			contactMasterListGUI.get(i).add(phoneContactPublic);
			
			faxContactPublic = new JTextField((contactMasterList.get(i).getFax()));
			faxContactPublic.setBorder(BorderFactory.createEmptyBorder());
			faxContactPublic.setEditable(false);
			faxContactPublic.setCaretPosition(0);
			faxContactPublic.setBackground(contactMainDisplayLinePanel.getBackground());
			faxContactPublic.setForeground(contactMainDisplayLinePanel.getForeground());
			contactMasterListGUI.get(i).add(faxContactPublic);
			
			emailContactPublic = new JTextField((contactMasterList.get(i).getEmail()));
			emailContactPublic.setBorder(BorderFactory.createEmptyBorder());
			emailContactPublic.setEditable(false);
			emailContactPublic.setCaretPosition(0);
			emailContactPublic.setBackground(contactMainDisplayLinePanel.getBackground());
			emailContactPublic.setForeground(contactMainDisplayLinePanel.getForeground());
			contactMasterListGUI.get(i).add(emailContactPublic);
			
			JButton moreContactButton = new JButton("More");
			moreContactButton.setBorder(BorderFactory.createEtchedBorder());
			moreContactButton.setBackground(contactMainDisplayLinePanel.getBackground());
			moreContactButton.setForeground(contactMainDisplayLinePanel.getForeground());
			moreContactButton.addMouseListener(this);
			contactPublicMoreInfoButtonList.add(moreContactButton);
			contactMasterListGUI.get(i).add(moreContactButton);
			
			contactMasterListGUI.get(i).setOpaque(true);
			contactMasterListGUI.get(i).setBackground(contactMainDisplayLinePanel.getBackground());
			contactMasterListGUI.get(i).setForeground(contactMainDisplayLinePanel.getForeground());
			
			numberOfContactsCounter++;
		}
		//Graphical Resizing
		if ((numberOfContactsCounter > 15 && center.getHeight() < 800) || (numberOfContactsCounter > 28 && center.getHeight() > 800))
		{
			contactMainPanelConstraints.gridx = 0;
			contactMainPanelConstraints.gridy = 0;
			contactMainPanelConstraints.weightx = 1.0;
			contactMainPanelConstraints.weighty = 1.0;
			contactMainPanel.add(columnTitles, contactMainPanelConstraints);
			for (int i = 0; i < contactMasterList.size(); i++)
			{
				contactMainPanelConstraints.gridx = 0;
				contactMainPanelConstraints.gridy = i+1;
				contactMainPanelConstraints.weightx = 1.0;
				contactMainPanelConstraints.weighty = 1.0;
				contactMainPanel.add(contactMasterListGUI.get(i), contactMainPanelConstraints);	
			}
			
			JScrollPane contactMainScrollPane = new JScrollPane(contactMainPanel,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			contactMainScrollPane.getVerticalScrollBar().setUnitIncrement(8);
			contactMainScrollPane.setBackground(background);
			contactMainScrollPane.setForeground(textColor);
			contactMainScrollPane.setBorder(BorderFactory.createEmptyBorder());
			center.add(contactMainScrollPane);
		}
		else 
		{
			center.add(columnTitles);
			for (int i = 0; i < contactMasterList.size(); i++)
			{
				center.add(contactMasterListGUI.get(i));
			}
			int amountOfFillerSlots = 0;
			if (numberOfContactsCounter <= 15 && center.getHeight() < 800)
			{
				amountOfFillerSlots = 15;
			}
			else
			{
				amountOfFillerSlots = 28;
			}
			for (int i = 0; i < amountOfFillerSlots-contactMasterListGUI.size(); i ++)
			{
				JPanel fillerPanel = new JPanel();
				fillerPanel.setLayout(new GridLayout());
				fillerPanel.setMaximumSize(new Dimension(center.getWidth()-25, 35));
				fillerPanel.setPreferredSize(new Dimension(center.getWidth()-25, 35));
				if (colorSwitch)
				{
					fillerPanel.setBackground(darkContent);
					fillerPanel.setForeground(textColor);
					colorSwitch = false;
				}
				else
				{
					fillerPanel.setBackground(lightContent);
					fillerPanel.setForeground(textColor);
					colorSwitch = true;
				}
				center.add(fillerPanel);
			}
		}
		bottom.removeAll();
		addContactPublicButton = new JButton("Add Contact");
		addContactPublicButton.setMinimumSize(new Dimension(center.getWidth()/6, 35));
		addContactPublicButton.setPreferredSize(new Dimension(center.getWidth()/6, 35));
		addContactPublicButton.setBackground(darkContent);
		addContactPublicButton.setForeground(textColor);
		addContactPublicButton.addMouseListener(this);
		bottom.add(addContactPublicButton);
		
		editContactPublicButton = new JButton("Edit Contact");
		editContactPublicButton.setMinimumSize(new Dimension(center.getWidth()/6, 35));
		editContactPublicButton.setPreferredSize(new Dimension(center.getWidth()/6, 35));
		editContactPublicButton.setBackground(darkContent);
		editContactPublicButton.setForeground(textColor);
		editContactPublicButton.addMouseListener(this);
		bottom.add(editContactPublicButton);
		
		deleteContactPublicButton = new JButton("Delete Contact");
		deleteContactPublicButton.setMinimumSize(new Dimension(center.getWidth()/6, 35));
		deleteContactPublicButton.setPreferredSize(new Dimension(center.getWidth()/6, 35));
		deleteContactPublicButton.setBackground(darkContent);
		deleteContactPublicButton.setForeground(textColor);
		deleteContactPublicButton.addMouseListener(this);
		bottom.add(deleteContactPublicButton);
		
		exitContactPublicButton = new JButton("Exit");
		exitContactPublicButton.setMinimumSize(new Dimension(center.getWidth()/6, 35));
		exitContactPublicButton.setPreferredSize(new Dimension(center.getWidth()/6, 35));
		exitContactPublicButton.setBackground(darkContent);
		exitContactPublicButton.setForeground(textColor);
		exitContactPublicButton.addMouseListener(this);
		bottom.add(exitContactPublicButton);
		
		revalidate();
		repaint();
	}
	/**
	 * Returns user to the main menu.
	 */
	public void exitContactPublicMainButton()
	{
		searchBar.setVisible(true);
		searchBar.setEnabled(true);
		searchBar.addMouseListener(this);
		searchStart.setVisible(true);
		searchStart.setEnabled(true);
		searchStart.addMouseListener(this);

		center.removeAll();
		bottom.removeAll();
		
		addRemoveClientButton.setBackground(darkContent);
		addRemoveClientButton.setForeground(textColor);
		bottom.add(addRemoveClientButton);
		
		contactsMainButton.setBackground(darkContent);
		contactsMainButton.setForeground(textColor);
		bottom.add(contactsMainButton);

		revalidate();
		repaint();
	}
	/**
	 * Finds the selected public contact and pulls up a bunch
	 * of jtextfields to edit the information. 
	 */
	public void editContactPublicMainButton()
	{
		if (selectedContactPublicPos != -1)
		{
			center.removeAll();

			JPanel contactToEditPanel = new JPanel();
			contactToEditPanel.setBackground(background);
			contactToEditPanel.setForeground(textColor);
			contactToEditPanel.setLayout(new GridBagLayout());
			GridBagConstraints contactToEditPanelConstraints = new GridBagConstraints();
			
			JPanel contactToEdit = new JPanel();
			contactToEdit.setBackground(background);
			contactToEdit.setForeground(textColor);
			contactToEdit.setLayout(new GridBagLayout());
			contactToEdit.setMaximumSize(new Dimension(center.getWidth()-25, center.getHeight()));
			contactToEdit.setPreferredSize(new Dimension(center.getWidth()-25, center.getHeight()));
			GridBagConstraints contactToEditConstraints = new GridBagConstraints();
		
			JPanel namePanel = new JPanel();
			namePanel.setBackground(background);
			namePanel.setForeground(textColor);
			
			JLabel nameLabel = new JLabel("Name: ");
			nameLabel.setBackground(background);
			nameLabel.setForeground(textColor);
			namePanel.add(nameLabel);
			
			nameContactPublicField = new JTextField(contactMasterList.get(selectedContactPublicPos).getName());
			nameContactPublicField.setMinimumSize(new Dimension(200, 25));
			nameContactPublicField.setPreferredSize(new Dimension(200, 25));
			nameContactPublicField.setEditable(true);
			nameContactPublicField.setBackground(lightContent);
			nameContactPublicField.setForeground(textColor);
			namePanel.add(nameContactPublicField);
			
			JLabel titleLabel = new JLabel("Title: ");
			titleLabel.setBackground(background);
			titleLabel.setForeground(textColor);
			namePanel.add(titleLabel);
			
			titleContactPublicField = new JTextField(contactMasterList.get(selectedContactPublicPos).getTitle());
			titleContactPublicField.setMinimumSize(new Dimension(200, 25));
			titleContactPublicField.setPreferredSize(new Dimension(200, 25));
			titleContactPublicField.setEditable(true);
			titleContactPublicField.setBackground(lightContent);
			titleContactPublicField.setForeground(textColor);
			namePanel.add(titleContactPublicField);
								
			contactToEditConstraints.gridx = 0;
			contactToEditConstraints.gridy = 0;
			contactToEdit.add(namePanel, contactToEditConstraints);
			
			JPanel phoneEmailPanel = new JPanel();
			phoneEmailPanel.setBackground(background);
			phoneEmailPanel.setForeground(textColor);
			
			JLabel phoneLabel = new JLabel("Phone: ");
			phoneLabel.setBackground(background);
			phoneLabel.setForeground(textColor);
			phoneEmailPanel.add(phoneLabel);
			
			phoneField = new JTextField(contactMasterList.get(selectedContactPublicPos).getPhone());
			phoneField.setMinimumSize(new Dimension(100, 25));
			phoneField.setPreferredSize(new Dimension(100, 25));
			phoneField.setEditable(true);
			phoneField.setBackground(lightContent);
			phoneField.setForeground(textColor);
			phoneEmailPanel.add(phoneField);
			
			JLabel faxLabel = new JLabel("Fax: ");
			faxLabel.setBackground(background);
			faxLabel.setForeground(textColor);
			phoneEmailPanel.add(faxLabel);
			
			faxContactPublicField = new JTextField(contactMasterList.get(selectedContactPublicPos).getFax());
			faxContactPublicField.setMinimumSize(new Dimension(100, 25));
			faxContactPublicField.setPreferredSize(new Dimension(100, 25));
			faxContactPublicField.setEditable(true);
			faxContactPublicField.setBackground(lightContent);
			faxContactPublicField.setForeground(textColor);
			phoneEmailPanel.add(faxContactPublicField);
			
			JLabel emailLabel = new JLabel("Email: ");
			emailLabel.setBackground(background);
			emailLabel.setForeground(textColor);
			phoneEmailPanel.add(emailLabel);
			
			emailField = new JTextField(contactMasterList.get(selectedContactPublicPos).getEmail());
			emailField.setMinimumSize(new Dimension(300, 25));
			emailField.setPreferredSize(new Dimension(300, 25));
			emailField.setEditable(true);
			emailField.setBackground(lightContent);
			emailField.setForeground(textColor);
			phoneEmailPanel.add(emailField);
			
			contactToEditConstraints.gridx = 0;
			contactToEditConstraints.gridy = 1;
			contactToEdit.add(phoneEmailPanel, contactToEditConstraints);
			
			JPanel addressPanelOne = new JPanel();
			addressPanelOne.setBackground(background);
			addressPanelOne.setForeground(textColor);
			
			JLabel addressLabel = new JLabel("Address: ");
			addressLabel.setBackground(background);
			addressLabel.setForeground(textColor);
			addressPanelOne.add(addressLabel);
			
			addressField = new JTextField(contactMasterList.get(selectedContactPublicPos).getAddress());
			addressField.setMinimumSize(new Dimension(300, 25));
			addressField.setPreferredSize(new Dimension(300, 25));
			addressField.setEditable(true);
			addressField.setBackground(lightContent);
			addressField.setForeground(textColor);
			addressPanelOne.add(addressField);
					
			contactToEditConstraints.gridx = 0;
			contactToEditConstraints.gridy = 2;
			contactToEdit.add(addressPanelOne, contactToEditConstraints);
			
			JPanel addressPanelTwo = new JPanel();
			addressPanelTwo.setBackground(background);
			addressPanelTwo.setForeground(textColor);
			
			cityField = new JTextField(contactMasterList.get(selectedContactPublicPos).getCity());
			cityField.setMinimumSize(new Dimension(100, 25));
			cityField.setPreferredSize(new Dimension(100, 25));
			cityField.setEditable(true);
			cityField.setBackground(lightContent);
			cityField.setForeground(textColor);
			addressPanelTwo.add(cityField);
			
			stateComboBox = new JComboBox<String>(statesList);
			stateComboBox.setEditable(false);
			stateComboBox.setEnabled(true);
			stateComboBox.setSelectedIndex(contactMasterList.get(selectedContactPublicPos).getState());
			stateComboBox.setBackground(lightContent);
			stateComboBox.setForeground(textColor);
			stateComboBox.addMouseListener(this);
			addressPanelTwo.add(stateComboBox);
			
			int convertingZip = contactMasterList.get(selectedContactPublicPos).getZip();
			String convertedZip = Integer.toString(convertingZip);
			zipField = new JTextField(convertedZip);
			zipField.setMinimumSize(new Dimension(75, 25));
			zipField.setPreferredSize(new Dimension(75, 25));
			zipField.setEditable(true);
			zipField.setBackground(lightContent);
			zipField.setForeground(textColor);
			addressPanelTwo.add(zipField);
					
			contactToEditConstraints.gridx = 0;
			contactToEditConstraints.gridy = 3;
			contactToEdit.add(addressPanelTwo, contactToEditConstraints);

			contactToEditPanelConstraints.gridx = 0;
			contactToEditPanelConstraints.gridy = 0;
			contactToEditPanel.add(contactToEdit, contactToEditPanelConstraints);
	
			JPanel contactNotesPanel = new JPanel();
			contactNotesPanel.setBackground(background);
			contactNotesPanel.setForeground(textColor);
			contactNotesPanel.setLayout(new GridBagLayout());
			GridBagConstraints contactNotesPanelConstraints = new GridBagConstraints();
			
			JLabel notesLabel = new JLabel("Notes");
			notesLabel.setBackground(background);
			notesLabel.setForeground(textColor);
			
			contactPublicNotesTextArea = new JTextArea(contactMasterList.get(selectedContactPublicPos).getNotes().getText());
			contactPublicNotesTextArea.setLineWrap(true);
			contactPublicNotesTextArea.setWrapStyleWord(true);
			contactPublicNotesTextArea.setEditable(true);
			contactPublicNotesTextArea.setBackground(lightContent);
			contactPublicNotesTextArea.setForeground(textColor);
			JScrollPane contactNotesScrollPane = new JScrollPane(contactPublicNotesTextArea, 
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					
			contactNotesScrollPane.setMinimumSize(new Dimension(center.getWidth()/2, center.getHeight()/3));
			contactNotesScrollPane.setPreferredSize(new Dimension(center.getWidth()/2,center.getHeight()/3));
			contactNotesScrollPane.setBackground(lightContent);
			contactNotesScrollPane.setForeground(textColor);
			
			contactNotesPanelConstraints.gridx = 0;
			contactNotesPanelConstraints.gridy = 0;
			contactNotesPanel.add(notesLabel, contactNotesPanelConstraints);
			
			contactNotesPanelConstraints.gridx = 0;
			contactNotesPanelConstraints.gridy = 1;
			contactNotesPanel.add(contactNotesScrollPane, contactNotesPanelConstraints);
			
			
			contactToEditPanelConstraints.gridx = 0;
			contactToEditPanelConstraints.gridy = 1;
			contactToEditPanel.add(contactNotesPanel, contactToEditPanelConstraints);
			
			center.add(contactToEditPanel);
			
			bottom.removeAll();
			
			editContactPublicSaveButton = new JButton ("Save");
			editContactPublicSaveButton.setMinimumSize(new Dimension(center.getWidth()/8, 35));
			editContactPublicSaveButton.setPreferredSize(new Dimension(center.getWidth()/8, 35));
			editContactPublicSaveButton.setBackground(darkContent);
			editContactPublicSaveButton.setForeground(textColor);
			editContactPublicSaveButton.addMouseListener(this);
			bottom.add(editContactPublicSaveButton);
			
			editContactPublicCancelButton = new JButton ("Cancel");
			editContactPublicCancelButton.setMinimumSize(new Dimension(center.getWidth()/8, 35));
			editContactPublicCancelButton.setPreferredSize(new Dimension(center.getWidth()/8, 35));
			editContactPublicCancelButton.setBackground(darkContent);
			editContactPublicCancelButton.setForeground(textColor);
			editContactPublicCancelButton.addMouseListener(this);
			bottom.add(editContactPublicCancelButton);

			revalidate();
			repaint();
		}
		else
		{
			JTextArea moreInfoTextArea = new JTextArea("Please select a Contact to edit.");
			moreInfoTextArea.setEditable(false);
			moreInfoTextArea.setWrapStyleWord(true);
			moreInfoTextArea.setLineWrap(true);
			moreInfoTextArea.setBackground(darkContent);
			moreInfoTextArea.setForeground(textColor);
			
			JOptionPane.showMessageDialog(null, moreInfoTextArea, "Alert", JOptionPane.INFORMATION_MESSAGE);
		}	
	}
	/**
	 * Saves the edited public contact in the ArrayLists and
	 * has SmalleyControl adjust the text files.
	 */
	public void saveEditContactPublicMainButton()
	{
		try
		{
			Integer.parseInt(zipField.getText());
			
			masterControl.editContact(new Contact(contactMasterList.get(selectedContactPublicPos).getAccountNumber(),
					nameContactPublicField.getText(), titleContactPublicField.getText(), 
					addressField.getText(), cityField.getText(), stateComboBox.getSelectedIndex(), 
					Integer.parseInt(zipField.getText()), phoneField.getText(), faxContactPublicField.getText(), 
					emailField.getText(), contactPublicNotesTextArea));
			center.removeAll();
			contactMasterList = masterControl.getContactMasterList(); 
			selectedContactPublicPos = -1;
			contactPublicMainButton();
			
			revalidate();
			repaint();
			
		}
		catch(NumberFormatException e)
		{
			JTextArea moreInfoTextArea = new JTextArea("Invalid zip code.");
			moreInfoTextArea.setEditable(false);
			moreInfoTextArea.setWrapStyleWord(true);
			moreInfoTextArea.setLineWrap(true);
			moreInfoTextArea.setBackground(darkContent);
			moreInfoTextArea.setForeground(textColor);
			
			JOptionPane.showMessageDialog(null, moreInfoTextArea, "Alert", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	/**
	 * This method will find the clicked more button and produces
	 * an alert with the public contacts address and any notes about them.
	 * @param e
	 */
	public void contactPublicMoreInfoButton(JButton e)
	{
		for (int i = 0; i < contactPublicMoreInfoButtonList.size(); i++)
		{
			if (e.equals(contactPublicMoreInfoButtonList.get(i)))
			{	JTextArea moreInfoTextArea = new JTextArea("Address: " + "\r\n" + "\r\n" +contactMasterList.get(i).getAddressFull()
						+ "\r\n" + "\r\n" + "Notes: " + "\r\n" + "\r\n" + contactMasterList.get(i).getNotes().getText());
				moreInfoTextArea.setEditable(false);
				moreInfoTextArea.setWrapStyleWord(true);
				moreInfoTextArea.setLineWrap(true);
				moreInfoTextArea.setBackground(lightContent);
				moreInfoTextArea.setForeground(textColor);
				
				JScrollPane moreInfoScrollPane = new JScrollPane(moreInfoTextArea);
				moreInfoScrollPane.setMinimumSize(new Dimension(center.getWidth()/2, center.getHeight()/3));
				moreInfoScrollPane.setPreferredSize(new Dimension(center.getWidth()/2, center.getHeight()/3));
				moreInfoScrollPane.setBackground(darkContent);
				moreInfoScrollPane.setForeground(textColor);
				moreInfoScrollPane.setBorder(BorderFactory.createEmptyBorder());
				
				JOptionPane.showMessageDialog(null, moreInfoScrollPane, "Address & Notes", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	/**
	 * This method creates a new contact, brings up
	 * JTextFields to add information as long as the 
	 * contact's name and title are filled out.
	 */
	public void createNewContactPublicButton()
	{
		center.removeAll();
		JPanel addNewContactPanel = new JPanel();
		addNewContactPanel.setBackground(background);
		addNewContactPanel.setForeground(textColor);
		addNewContactPanel.setLayout(new GridBagLayout());
		GridBagConstraints addNewContactPanelConstraints = new GridBagConstraints();
		
		JPanel addNewContactInfoPanel = new JPanel();
		addNewContactInfoPanel.setBackground(background);
		addNewContactInfoPanel.setForeground(textColor);
		addNewContactInfoPanel.setLayout(new GridBagLayout());
		GridBagConstraints addNewContactInfoPanelConstraints = new GridBagConstraints();

		JPanel namePanel = new JPanel();
		namePanel.setBackground(background);
		namePanel.setForeground(textColor);
		
		JLabel nameLabel = new JLabel("Name: ");
		nameLabel.setBackground(background);
		nameLabel.setForeground(textColor);
		namePanel.add(nameLabel);
		
		nameContactPublicField = new JTextField();
		nameContactPublicField.setMinimumSize(new Dimension(200, 25));
		nameContactPublicField.setPreferredSize(new Dimension(200, 25));
		nameContactPublicField.setEditable(true);
		nameContactPublicField.setBackground(lightContent);
		nameContactPublicField.setForeground(textColor);
		namePanel.add(nameContactPublicField);
		
		JLabel titleLabel = new JLabel("Title: ");
		titleLabel.setBackground(background);
		titleLabel.setForeground(textColor);
		namePanel.add(titleLabel);
		
		titleContactPublicField = new JTextField();
		titleContactPublicField.setMinimumSize(new Dimension(200, 25));
		titleContactPublicField.setPreferredSize(new Dimension(200, 25));
		titleContactPublicField.setEditable(true);
		titleContactPublicField.setBackground(lightContent);
		titleContactPublicField.setForeground(textColor);
		namePanel.add(titleContactPublicField);
							
		addNewContactInfoPanelConstraints.gridx = 0;
		addNewContactInfoPanelConstraints.gridy = 0;
		addNewContactInfoPanel.add(namePanel, addNewContactInfoPanelConstraints);
		
		JPanel phoneEmailPanel = new JPanel();
		phoneEmailPanel.setBackground(background);
		phoneEmailPanel.setForeground(textColor);
		
		JLabel phoneLabel = new JLabel("Phone: ");
		phoneLabel.setBackground(background);
		phoneLabel.setForeground(textColor);
		phoneEmailPanel.add(phoneLabel);
		
		phoneField = new JTextField();
		phoneField.setMinimumSize(new Dimension(100, 25));
		phoneField.setPreferredSize(new Dimension(100, 25));
		phoneField.setEditable(true);
		phoneField.setBackground(lightContent);
		phoneField.setForeground(textColor);
		phoneEmailPanel.add(phoneField);
		
		JLabel faxLabel = new JLabel("Fax: ");
		faxLabel.setBackground(background);
		faxLabel.setForeground(textColor);
		phoneEmailPanel.add(faxLabel);
		
		faxContactPublicField = new JTextField();
		faxContactPublicField.setMinimumSize(new Dimension(100, 25));
		faxContactPublicField.setPreferredSize(new Dimension(100, 25));
		faxContactPublicField.setEditable(true);
		faxContactPublicField.setBackground(lightContent);
		faxContactPublicField.setForeground(textColor);
		phoneEmailPanel.add(faxContactPublicField);
		
		JLabel emailLabel = new JLabel("Email: ");
		emailLabel.setBackground(background);
		emailLabel.setForeground(textColor);
		phoneEmailPanel.add(emailLabel);
		
		emailField = new JTextField();
		emailField.setMinimumSize(new Dimension(300, 25));
		emailField.setPreferredSize(new Dimension(300, 25));
		emailField.setEditable(true);
		emailField.setBackground(lightContent);
		emailField.setForeground(textColor);
		phoneEmailPanel.add(emailField);
		
		addNewContactInfoPanelConstraints.gridx = 0;
		addNewContactInfoPanelConstraints.gridy = 1;
		addNewContactInfoPanel.add(phoneEmailPanel, addNewContactInfoPanelConstraints);
		
		JPanel addressPanelOne = new JPanel();
		addressPanelOne.setBackground(background);
		addressPanelOne.setForeground(textColor);
		
		JLabel addressLabel = new JLabel("Address: ");
		addressLabel.setBackground(background);
		addressLabel.setForeground(textColor);
		addressPanelOne.add(addressLabel);
		
		addressField = new JTextField();
		addressField.setMinimumSize(new Dimension(300, 25));
		addressField.setPreferredSize(new Dimension(300, 25));
		addressField.setEditable(true);
		addressField.setBackground(lightContent);
		addressField.setForeground(textColor);
		addressPanelOne.add(addressField);
				
		addNewContactInfoPanelConstraints.gridx = 0;
		addNewContactInfoPanelConstraints.gridy = 2;
		addNewContactInfoPanel.add(addressPanelOne, addNewContactInfoPanelConstraints);
		
		JPanel addressPanelTwo = new JPanel();
		addressPanelTwo.setBackground(background);
		addressPanelTwo.setForeground(textColor);
		
		JLabel cityLabel = new JLabel("City: ");
		cityLabel.setBackground(background);
		cityLabel.setForeground(textColor);
		addressPanelTwo.add(cityLabel);
		
		cityField = new JTextField();
		cityField.setMinimumSize(new Dimension(100, 25));
		cityField.setPreferredSize(new Dimension(100, 25));
		cityField.setEditable(true);
		cityField.setBackground(lightContent);
		cityField.setForeground(textColor);
		addressPanelTwo.add(cityField);
		
		JLabel stateLabel = new JLabel("State: ");
		stateLabel.setBackground(background);
		stateLabel.setForeground(textColor);
		addressPanelTwo.add(stateLabel);
		
		stateComboBox = new JComboBox<String>(statesList);
		stateComboBox.setEditable(false);
		stateComboBox.setEnabled(true);
		stateComboBox.setSelectedIndex(0);
		stateComboBox.addMouseListener(this);
		stateComboBox.setBackground(lightContent);
		stateComboBox.setForeground(textColor);
		addressPanelTwo.add(stateComboBox);
		
		
		JLabel zipLabel = new JLabel("Zip: ");
		zipLabel.setBackground(background);
		zipLabel.setForeground(textColor);
		addressPanelTwo.add(zipLabel);
		
		zipField = new JTextField();
		zipField.setMinimumSize(new Dimension(75, 25));
		zipField.setPreferredSize(new Dimension(75, 25));
		zipField.setEditable(true);
		zipField.setBackground(lightContent);
		zipField.setForeground(textColor);
		addressPanelTwo.add(zipField);
				
		addNewContactInfoPanelConstraints.gridx = 0;
		addNewContactInfoPanelConstraints.gridy = 3;
		addNewContactInfoPanel.add(addressPanelTwo, addNewContactInfoPanelConstraints);

		addNewContactPanelConstraints.gridx = 0;
		addNewContactPanelConstraints.gridy = 0;
		addNewContactPanel.add(addNewContactInfoPanel, addNewContactPanelConstraints);
		
		JPanel addNewContactNotesPanel = new JPanel();
		addNewContactNotesPanel.setLayout(new GridBagLayout());
		addNewContactNotesPanel.setBackground(background);
		addNewContactNotesPanel.setForeground(textColor);
		GridBagConstraints addNewContactNotesPanelConstraints = new GridBagConstraints();
		
		JLabel notesLabel = new JLabel("Notes");
		notesLabel.setBackground(background);
		notesLabel.setForeground(textColor);
		
		addNewContactNotesPanelConstraints.gridx = 0;
		addNewContactNotesPanelConstraints.gridy = 0;
		addNewContactNotesPanel.add(notesLabel, addNewContactNotesPanelConstraints);
		
		addNewContactPublicNotesTextArea = new JTextArea();
		addNewContactPublicNotesTextArea.setEditable(true);
		addNewContactPublicNotesTextArea.setLineWrap(true);
		addNewContactPublicNotesTextArea.setCaretPosition(0);
		addNewContactPublicNotesTextArea.setBackground(lightContent);
		addNewContactPublicNotesTextArea.setForeground(textColor);
		
		JScrollPane addNewContactNotesScrollPane = new JScrollPane(addNewContactPublicNotesTextArea, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		addNewContactNotesScrollPane.setMinimumSize(new Dimension(center.getWidth()/2, center.getHeight()/3));
		addNewContactNotesScrollPane.setPreferredSize(new Dimension(center.getWidth()/2, center.getHeight()/3));
		addNewContactNotesScrollPane.setBackground(background);
		addNewContactNotesScrollPane.setForeground(textColor);
		addNewContactNotesScrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		addNewContactNotesPanelConstraints.gridx = 0;
		addNewContactNotesPanelConstraints.gridy = 1;
		addNewContactNotesPanel.add(addNewContactNotesScrollPane, addNewContactNotesPanelConstraints);
		
		addNewContactPanelConstraints.gridx = 0;
		addNewContactPanelConstraints.gridy = 1;
		addNewContactPanel.add(addNewContactNotesPanel, addNewContactPanelConstraints);
		
		center.add(addNewContactPanel, addNewContactPanelConstraints);
		
		bottom.removeAll();
		addContactPublicSaveButton = new JButton("Save Contact");
		addContactPublicSaveButton.setMinimumSize(new Dimension(center.getWidth()/6, 35));
		addContactPublicSaveButton.setPreferredSize(new Dimension(center.getWidth()/6, 35));
		addContactPublicSaveButton.setBackground(darkContent);
		addContactPublicSaveButton.setForeground(textColor);
		addContactPublicSaveButton.addMouseListener(this);
		bottom.add(addContactPublicSaveButton);
		
		addContactPublicCancelButton = new JButton("Cancel");
		addContactPublicCancelButton.setMinimumSize(new Dimension(center.getWidth()/6, 35));
		addContactPublicCancelButton.setPreferredSize(new Dimension(center.getWidth()/6, 35));
		addContactPublicCancelButton.setBackground(darkContent);
		addContactPublicCancelButton.setForeground(textColor);
		addContactPublicCancelButton.addMouseListener(this);
		bottom.add(addContactPublicCancelButton);

		revalidate();
		repaint();
	}
	/**
	 * Saves the information from the JTextFields into the ArrayLists and text Files as
	 * long as the name and title fields are filled out.
	 */
	public void createNewContactPublicSaveButton()
	{
		boolean hasAnEmptyField = false;
		if (nameContactPublicField.getText().equals("") || nameContactPublicField.getText().equals(" "))
		{
			hasAnEmptyField = true;
		}
		if (titleContactPublicField.getText().equals("") || titleContactPublicField.getText().equals(" "))
		{
			hasAnEmptyField = true;
		}
		if (hasAnEmptyField)
		{
			JTextArea moreInfoTextArea = new JTextArea("Please make sure to fill out the form in full.");
			moreInfoTextArea.setEditable(false);
			moreInfoTextArea.setWrapStyleWord(true);
			moreInfoTextArea.setLineWrap(true);
			moreInfoTextArea.setBackground(darkContent);
			moreInfoTextArea.setForeground(textColor);
			
			JOptionPane.showMessageDialog(null, moreInfoTextArea, "Alert", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			int validZip;
			try 
			{
				validZip = Integer.parseInt(zipField.getText());
			}
			catch(NumberFormatException badNumber)
			{
				validZip = 0;
			}
			contactMasterList.add(new Contact(masterControl.findAvailableContactAccountNumber((ArrayList<Contact>)contactMasterList), 
					nameContactPublicField.getText(), titleContactPublicField.getText(), addressField.getText(), 
					cityField.getText(), stateComboBox.getSelectedIndex(), validZip, 
					phoneField.getText(), faxContactPublicField.getText(), emailField.getText(), 
					addNewContactPublicNotesTextArea));
			contactMasterList = masterControl.editContactFile((ArrayList<Contact>)contactMasterList);
			contactPublicMainButton();
		}
	}
	/**
	 * Finds selected public contact and deletes them from the ArrayLists and text files.
	 */
	public void deleteExistingContactPublicButton()
	{
		if (selectedContactPublicPos != -1)
		{
			masterControl.deletePublicContactFromClient(contactMasterList.get(selectedContactPublicPos).getAccountNumber());
			contactMasterList.remove(selectedContactPublicPos);
			contactMasterList = masterControl.editContactFile((ArrayList<Contact>)contactMasterList);
			selectedContactPublicPos = -1;
			contactPublicMainButton();
			
			JTextArea moreInfoTextArea = new JTextArea("Contact deleted successfully.");
			moreInfoTextArea.setEditable(false);
			moreInfoTextArea.setWrapStyleWord(true);
			moreInfoTextArea.setLineWrap(true);
			moreInfoTextArea.setBackground(darkContent);
			moreInfoTextArea.setForeground(textColor);
			JOptionPane.showMessageDialog(null, moreInfoTextArea, "Alert", JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			JTextArea moreInfoTextArea = new JTextArea("Please select a Contact to delete.");
			moreInfoTextArea.setEditable(false);
			moreInfoTextArea.setWrapStyleWord(true);
			moreInfoTextArea.setLineWrap(true);
			moreInfoTextArea.setBackground(darkContent);
			moreInfoTextArea.setForeground(textColor);
			
			JOptionPane.showMessageDialog(null, moreInfoTextArea, "Alert", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/**
	 * When enter is hit in either the clientSearchResults JTextField or the
	 * public contacts JTextField, it'll search for any results
	 * If 
	 */
	public void actionPerformed(ActionEvent ae) 
	{
		boolean isPublicContactSelected = false;
		boolean isClientContactForRemovalSelected = false;
		boolean isClientPublicContactSelected = false;
		boolean isPublicContactSelectedForDeletion = false;
		
		//clientSearchResults or Public Contact Search "Enter" being hit.
		if (ae.getSource().equals(searchBar) || (ae.getSource().equals(searchContactPublicTextField)))
		{

			if (quickMenuSelectPaintFlag == 1 || quickMenuSelectPaintFlag == -1)
			{
				center.removeAll();
				searchResultsGUI.clear();
				searchResults.clear();
				currentSearchData = searchBar.getText();
				searchResults = masterControl.searchClientName(currentSearchData);
				
				if (searchResults.size() > 0)
				{
					bottom.removeAll();
					clientSearchResults((ArrayList<Client>)searchResults); 
				}
				else
				{
					bottom.removeAll();
					JTextField noMatchFound = new JTextField("Search found no matches.");
					noMatchFound.setPreferredSize(new Dimension(center.getWidth(), center.getHeight()));
					noMatchFound.setMinimumSize(new Dimension(center.getWidth(), center.getHeight()));
					noMatchFound.setMaximumSize(new Dimension(center.getWidth(), center.getHeight()));
					noMatchFound.setHorizontalAlignment(JTextField.CENTER);
					noMatchFound.setBorder(BorderFactory.createEmptyBorder());
					noMatchFound.setBackground(textColor);
					noMatchFound.setEditable(false);
					center.add(noMatchFound);
				}	
			}
			else if (quickMenuSelectPaintFlag == 2)
			{
				searchContactPublicButton();
			}
		}
		//Searches for the selected JRadioButton in the ClientContactList Public Search
		for (int i = 0; i < addClientContactListButtonList.size(); i++)
		{
			if (ae.getSource().equals(addClientContactListButtonList.get(i)))
			{
				for (int ii = 0; ii < addClientContactListButtonList.size(); ii++)
				{
					if (isClientPublicContactSelected == true)
					{
						addClientContactListButtonList.get(ii).setEnabled(false);
						addClientContactListButtonList.get(ii).setSelected(false);
					}
					if (addClientContactListButtonList.get(ii).isSelected())
					{	
						addClientContactSearchResultsSave.setEnabled(true);
						addClientContactSearchResultsSave.addMouseListener(this);
						
						isClientPublicContactSelected = true;
						for (int componentColorSelected = 0; componentColorSelected < addClientContactGUI.get(ii).getComponentCount(); componentColorSelected++)
						{
							addClientContactGUI.get(ii).setBackground(highlight);	
							addClientContactGUI.get(ii).getComponent(componentColorSelected).setBackground(highlight);	
							addClientContactSearchResultPos = ii; 
						}
						
						for (int previ = 0; previ < ii; previ++)
						{
							addClientContactListButtonList.get(previ).setEnabled(false);
							addClientContactListButtonList.get(previ).setSelected(false);
							
							for (int iii = 0; iii < addClientContactGUI.get(previ).getComponentCount(); iii++)
							{
								if (previ % 2 == 0)
								{
									addClientContactGUI.get(previ).setBackground(darkContent);
									addClientContactGUI.get(previ).getComponent(iii).setBackground(darkContent);
									addClientContactGUI.get(previ).getComponent(iii).setForeground(textColor);
								}
								else
								{
									addClientContactGUI.get(previ).setBackground(lightContent);
									addClientContactGUI.get(previ).getComponent(iii).setBackground(lightContent);
									addClientContactGUI.get(previ).getComponent(iii).setForeground(textColor);
								}
						
							}
						}
					}
				}
				if (isClientPublicContactSelected == false)
				{
					addRemoveClientContactRemoveButton.setEnabled(false);
					addRemoveClientContactRemoveButton.removeMouseListener(this);
					
					for (int iiii = 0; iiii < addClientContactListButtonList.size(); iiii++)
					{
						addClientContactListButtonList.get(iiii).setEnabled(true);
						addClientContactListButtonList.get(iiii).setSelected(false);
						for (int iiiii = 0; iiiii < addClientContactGUI.get(iiii).getComponentCount(); iiiii++)
						{
							if (iiii % 2 == 0)
							{
								addClientContactGUI.get(iiii).setBackground(darkContent);
								addClientContactGUI.get(iiii).getComponent(iiiii).setBackground(darkContent);
								addClientContactGUI.get(iiii).getComponent(iiiii).setForeground(textColor);
							}
							
							else
							{
								addClientContactGUI.get(iiii).setBackground(lightContent);
								addClientContactGUI.get(iiii).getComponent(iiiii).setBackground(lightContent);
								addClientContactGUI.get(iiii).getComponent(iiiii).setForeground(textColor);
							}
						}
						
					}
				}
			}
		}
		//Selected Contact for Removal
		for (int i = 0; i < removeClientContactListButtonList.size(); i++)
		{
			if (ae.getSource().equals(removeClientContactListButtonList.get(i)))
			{
				selectedClientPersonalContactPos = -1;
				for (int ii = 0; ii < removeClientContactListButtonList.size(); ii++)
				{
					if (isClientContactForRemovalSelected == true)
					{
						removeClientContactListButtonList.get(ii).setEnabled(false);
						removeClientContactListButtonList.get(ii).setSelected(false);
					}
					if (removeClientContactListButtonList.get(ii).isSelected())
					{	
						searchContactPublicTextField.setEnabled(false);
						searchContactPublicTextField.removeMouseListener(this);
						searchContactPublicStartButton.setEnabled(false);
						searchContactPublicStartButton.removeMouseListener(this);

						addRemoveClientContactRemoveButton.setEnabled(true);
						addRemoveClientContactRemoveButton.addMouseListener(this);
						
						if (ii < searchResults.get(selectedClientPos).getContactPersonalList().size())
						{
							searchContactCreatePersonalButton.setText("Edit Personal Contact");
							selectedClientPersonalContactPos = ii;
						}
						else
						{
							searchContactCreatePersonalButton.setText("Create Personal Contact");
							searchContactCreatePersonalButton.setEnabled(false);
							searchContactCreatePersonalButton.removeMouseListener(this);
						}
						
						isClientContactForRemovalSelected = true;
						for (int componentColorSelected = 0; componentColorSelected < removeClientContactGUI.get(ii).getComponentCount(); componentColorSelected++)
						{
							removeClientContactGUI.get(ii).setBackground(highlight);	
							removeClientContactGUI.get(ii).getComponent(componentColorSelected).setBackground(highlight);	
						}
						
						for (int previ = 0; previ < ii; previ++)
						{
							removeClientContactListButtonList.get(previ).setEnabled(false);
							removeClientContactListButtonList.get(previ).setSelected(false);
							
							for (int iii = 0; iii < removeClientContactGUI.get(previ).getComponentCount(); iii++)
							{
								if (previ % 2 == 0)
								{
									removeClientContactGUI.get(previ).setBackground(darkContent);
									removeClientContactGUI.get(previ).getComponent(iii).setBackground(darkContent);
									removeClientContactGUI.get(previ).getComponent(iii).setForeground(textColor);
								}
								
								else
								{
									removeClientContactGUI.get(previ).setBackground(lightContent);
									removeClientContactGUI.get(previ).getComponent(iii).setBackground(lightContent);
									removeClientContactGUI.get(previ).getComponent(iii).setForeground(textColor);
								}
						
							}
						}
						
					}
				}
				if (isClientContactForRemovalSelected == false)
				{
					addRemoveClientContactRemoveButton.setEnabled(false);
					addRemoveClientContactRemoveButton.removeMouseListener(this);
					
					searchContactPublicTextField.setEnabled(true);
					searchContactPublicTextField.addMouseListener(this);
					searchContactPublicStartButton.setEnabled(true);
					searchContactPublicStartButton.addMouseListener(this);
					
					searchContactCreatePersonalButton.setText("Create Personal Contact");
					searchContactCreatePersonalButton.setEnabled(true);
					searchContactCreatePersonalButton.addMouseListener(this);
					
					for (int iiii = 0; iiii < removeClientContactListButtonList.size(); iiii++)
					{
						removeClientContactListButtonList.get(iiii).setEnabled(true);
						removeClientContactListButtonList.get(iiii).setSelected(false);
						for (int iiiii = 0; iiiii < removeClientContactGUI.get(iiii).getComponentCount(); iiiii++)
						{
							if (iiii % 2 == 0)
							{
								removeClientContactGUI.get(iiii).setBackground(darkContent);
								removeClientContactGUI.get(iiii).getComponent(iiiii).setBackground(darkContent);
								removeClientContactGUI.get(iiii).getComponent(iiiii).setForeground(textColor);
							}
							
							else
							{
								removeClientContactGUI.get(iiii).setBackground(lightContent);
								removeClientContactGUI.get(iiii).getComponent(iiiii).setBackground(lightContent);
								removeClientContactGUI.get(iiii).getComponent(iiiii).setForeground(textColor);
							}
						}
						
					}
				}
			}
		}
		//Selected contact from the public contacts list
		for (int i = 0; i < contactMasterListGUI.size(); i++)
		{
			if (ae.getSource().equals(contactMasterListGUI.get(i).getComponent(0)))
			{
				for (int ii = 0; ii < contactMasterList.size(); ii++)
				{
					if (contactMasterList.get(ii).getSelectionButton().isSelected())
					{
						isPublicContactSelected = true;
						selectedContactPublicPos = ii;
						
						for (int componentCycleForColor = 0; componentCycleForColor < contactMasterListGUI.get(ii).getComponentCount(); componentCycleForColor++)
						{
							contactMasterListGUI.get(ii).getComponent(componentCycleForColor).setBackground(highlight);
						}
						for (int previousButtons = 0; previousButtons < ii; previousButtons++)
						{
							contactMasterList.get(previousButtons).getSelectionButton().setEnabled(false);
							contactMasterList.get(previousButtons).getSelectionButton().setSelected(false);	
						}
					}
					else if (!(contactMasterList.get(ii).getSelectionButton().isSelected()))
					{
						for (int componentCycleForColor = 0; componentCycleForColor < contactMasterListGUI.get(ii).getComponentCount(); componentCycleForColor++)
						{
							if (ii % 2 == 0)
							{
								contactMasterListGUI.get(ii).getComponent(componentCycleForColor).setBackground(darkContent);
								contactMasterListGUI.get(ii).getComponent(componentCycleForColor).setForeground(textColor);
							}
							else
							{
								contactMasterListGUI.get(ii).getComponent(componentCycleForColor).setBackground(lightContent);
								contactMasterListGUI.get(ii).getComponent(componentCycleForColor).setForeground(textColor);
							}
							if (isPublicContactSelected == true)
							{
								contactMasterList.get(ii).getSelectionButton().setEnabled(false);
								contactMasterList.get(ii).getSelectionButton().setSelected(false);
							}
							else
							{
								for (int iii = 0; iii < contactMasterList.size(); iii++)
								{
									contactMasterList.get(iii).getSelectionButton().setEnabled(true);
									selectedContactPublicPos = -1;
								}
								
							}
						}
					}
				}
			}
		}
		//Selected contact from the public contacts list for deletion
		for (int i = 0; i < clientMasterListGUI.size(); i++)
		{
			if (ae.getSource().equals(clientMasterListGUI.get(i).getComponent(0)))
			{
				for (int ii = 0; ii < clientMasterList.size(); ii++)
				{
					JRadioButton buttonSelected = (JRadioButton) clientMasterListGUI.get(ii).getComponent(0);
					
					if (buttonSelected.isSelected())
					{
						isPublicContactSelectedForDeletion = true;
						selectedClientToDeletePos = ii;
						
						for (int componentCycleForColor = 0; componentCycleForColor < clientMasterListGUI.get(ii).getComponentCount(); componentCycleForColor++)
						{
							clientMasterListGUI.get(ii).getComponent(componentCycleForColor).setBackground(highlight);
						}
						for (int previousButtons = 0; previousButtons < ii; previousButtons++)
						{
							JRadioButton setSelectedRadioButton = (JRadioButton)clientMasterListGUI.get(previousButtons).getComponent(0);
							setSelectedRadioButton.setSelected(false);
							setSelectedRadioButton.setEnabled(false);
							JTextField rewritingAccountField = (JTextField)clientMasterListGUI.get(previousButtons).getComponent(1);
							JTextField rewritingNameField = (JTextField)clientMasterListGUI.get(previousButtons).getComponent(2);
							clientMasterListGUI.get(previousButtons).removeAll();
							clientMasterListGUI.get(previousButtons).add(setSelectedRadioButton);
							clientMasterListGUI.get(previousButtons).add(rewritingAccountField);
							clientMasterListGUI.get(previousButtons).add(rewritingNameField);
						}
					}
					else if (!buttonSelected.isSelected())
					{
						for (int componentCycleForColor = 0; componentCycleForColor < clientMasterListGUI.get(ii).getComponentCount(); componentCycleForColor++)
						{
							if (ii % 2 == 0)
							{
								clientMasterListGUI.get(ii).getComponent(componentCycleForColor).setBackground(darkContent);
								clientMasterListGUI.get(ii).getComponent(componentCycleForColor).setForeground(textColor);
							}
							else
							{
								clientMasterListGUI.get(ii).getComponent(componentCycleForColor).setBackground(lightContent);
								clientMasterListGUI.get(ii).getComponent(componentCycleForColor).setForeground(textColor);
							}
							if (isPublicContactSelectedForDeletion == true)
							{
								JRadioButton setSelectedRadioButton = (JRadioButton)clientMasterListGUI.get(ii).getComponent(0);
								setSelectedRadioButton.setSelected(false);
								setSelectedRadioButton.setEnabled(false);
								JTextField rewritingAccountField = (JTextField)clientMasterListGUI.get(ii).getComponent(1);
								JTextField rewritingNameField = (JTextField)clientMasterListGUI.get(ii).getComponent(2);
								clientMasterListGUI.get(ii).removeAll();
								clientMasterListGUI.get(ii).add(setSelectedRadioButton);
								clientMasterListGUI.get(ii).add(rewritingAccountField);
								clientMasterListGUI.get(ii).add(rewritingNameField);

							}
							else
							{
								for (int iii = 0; iii < clientMasterList.size(); iii++)
								{
									JRadioButton setSelectedRadioButton = (JRadioButton)clientMasterListGUI.get(iii).getComponent(0);
									setSelectedRadioButton.setEnabled(true);
									JTextField rewritingAccountField = (JTextField)clientMasterListGUI.get(iii).getComponent(1);
									JTextField rewritingNameField = (JTextField)clientMasterListGUI.get(iii).getComponent(2);
									clientMasterListGUI.get(iii).removeAll();
									clientMasterListGUI.get(iii).add(setSelectedRadioButton);
									clientMasterListGUI.get(iii).add(rewritingAccountField);
									clientMasterListGUI.get(iii).add(rewritingNameField);
									selectedClientToDeletePos = -1;
								}
								
							}
						}
					}
				}
			}
		}
	}
	/**
	 * Source for the majority of methods being called.
	 */
	public void mouseClicked(MouseEvent e) 
	{
		if (e.getSource().equals(createClientExitButton))
		{
			exitAddRemoveClientButton();
		}
		if (e.getSource().equals(deleteExistingClientButton))
		{
			deleteExistingClientButton();
		}
		if (e.getSource().equals(createNewClientCancelButton))
		{
			addRemoveClientButton();
		}
		if (e.getSource().equals(createNewClientSaveButton))
		{
			saveNewClientButton();
		}
		if (e.getSource().equals(createNewClientButton))
		{
			createNewClientButton();
		}
		if (e.getSource().equals(addRemoveClientButton))
		{
			addRemoveClientButton();
		}
		if (e.getSource() instanceof JPanel && e.getClickCount() == 2 && selectedClientPos != -1 && quickMenuSelectPaintFlag == 1)
		{
			clientInfoButton();
		}
		if (e.getSource().equals(editRoiStatusButton))
		{
			editClientContactROIStatusButton();
		}
		if (!(clientContactMoreButtonList == null))
		{
			for (int i = 0; i < clientContactMoreButtonList.size(); i++)
			{
				if (clientContactMoreButtonList.get(i).equals(e.getSource()))
				{
					clientContactMoreInfoButton((JButton)e.getSource());
				}
			}
		}
		if (e.getSource().equals(addRemoveClientContactPersonalSave))
		{
			createClientContactPersonalSaveButton();	
		}
		if (e.getSource().equals(addRemoveClientContactPersonalCancel))
		{
			addRemoveClientContactButton();
		}
		if (e.getSource().equals(addClientContactSearchResultsSave))
		{
			searchContactPublicSaveButton();
		}
		if (e.getSource().equals(addClientContactSearchResultsCancel))
		{
			addRemoveClientContactButton();
		}
		if (e.getSource().equals(searchContactPublicTextField))
		{
			searchContactPublicTextField.setText("");
		}
		if (e.getSource().equals(searchContactPublicStartButton))
		{
			searchContactPublicButton();	
		}
		if (e.getSource().equals(searchContactCreatePersonalButton))
		{
			createClientContactPersonalButton();
		}
		if (e.getSource().equals(addRemoveClientContactRemoveButton))
		{
			removeClientContactButton();
		}
		if (e.getSource().equals(exitAddRemoveClientContactButton))
		{
			clientInfoButton();
		}
		if (e.getSource().equals(addRemoveContactClientButton))
		{
			addRemoveClientContactButton();
		}
		if (e.getSource().equals(deleteContactPublicButton))
		{
			deleteExistingContactPublicButton();
		}
		if (e.getSource().equals(addContactPublicSaveButton))
		{
			createNewContactPublicSaveButton();
		}
		if (e.getSource().equals(addContactPublicCancelButton))
		{
			selectedContactPublicPos = -1;
			contactPublicMainButton();
		}
		if (e.getSource().equals(addContactPublicButton))
		{
			createNewContactPublicButton();
		}
		if (!(contactPublicMoreInfoButtonList == null))
		{
			for (int i = 0; i < contactPublicMoreInfoButtonList.size(); i++)
			{
				if (contactPublicMoreInfoButtonList.get(i).equals(e.getSource()))
				{
					contactPublicMoreInfoButton((JButton)e.getSource());
				}
			}
		}
		if (e.getSource().equals(editContactPublicSaveButton))
		{
			saveEditContactPublicMainButton();
		}
		if (e.getSource().equals(editContactPublicCancelButton))
		{
			selectedContactPublicPos = -1;
			contactPublicMainButton();
		}
		if (e.getSource().equals(editContactPublicButton))
		{
			editContactPublicMainButton();
		}
		if (e.getSource().equals(exitContactPublicButton))
		{
			exitContactPublicMainButton();
		}
		if (e.getSource().equals(contactsMainButton) && !wasKeyPressedBefore)
		{
			contactPublicMainButton();
		}
		if(e.getSource().equals(saveEditNotesClientButton))
		{
			saveEditNotesClientButton();
		}
		if(e.getSource().equals(cancelEditNotesClientButton))
		{
			cancelEditNotesClientButton();
		}
		if (e.getSource().equals(editNotesClientButton))
		{
			editNotesClientButton();
		}
		if (e.getSource().equals(saveInfoButton))
		{
			saveEditClientRawInfoButton();
		}
		if (e.getSource().equals(cancelInfoButton))
		{
			cancelEditClientRawInfoButton();
		}
		if (e.getSource().equals(exitClientInfoButton))
		{
			selectedClientPos = -1;
			bottom.removeAll();
			searchBar.addMouseListener(this);
			searchBar.addActionListener(this);
			searchBar.setEnabled(true);
			searchBar.setVisible(true);
			searchStart.addMouseListener(this);
			searchStart.setEnabled(true);
			searchStart.setVisible(true);
			searchResults = masterControl.searchClientName(currentSearchData);
			clientSearchResults((ArrayList<Client>)searchResults);
		}
		if (e.getSource().equals(editClientInfoButton))
		{
			editClientRawInfoButton();
		}
		if (e.getSource() instanceof JPanel && !wasKeyPressedBefore)
		{
			for (int i = 0; i < searchResultsGUI.size(); i++)
			{
				JPanel clickedPanel = (JPanel)e.getSource();
				if (clickedPanel == searchResultsGUI.get(i))
				{
					clientSearchResultsSelector((JPanel)e.getSource());
				}
			}
		}
		if (e.getSource().equals(searchStart) && !wasKeyPressedBefore)
		{
			center.removeAll();
			searchResultsGUI.clear();
			searchResults.clear();
			currentSearchData = searchBar.getText();
			searchResults = masterControl.searchClientName(currentSearchData);
			if (searchResults.size() > 0)
			{
				clientSearchResults((ArrayList<Client>)searchResults); // trying to create space so that each client fills up the area. check below for the method. 5/13/17
			}
			else
			{	
				JTextField noMatchFound = new JTextField("Search found no matches.");
				noMatchFound.setPreferredSize(new Dimension(center.getWidth(), center.getHeight()));
				noMatchFound.setMinimumSize(new Dimension(center.getWidth(), center.getHeight()));
				noMatchFound.setMaximumSize(new Dimension(center.getWidth(), center.getHeight()));
				noMatchFound.setHorizontalAlignment(JTextField.CENTER);
				noMatchFound.setBorder(BorderFactory.createEmptyBorder());
				noMatchFound.setBackground(lightContent);
				noMatchFound.setForeground(textColor);
				noMatchFound.setEditable(false);
				center.add(noMatchFound);

				revalidate();	
				repaint();
			}
		}
		if (wasKeyPressedBefore && selectedClientPos != -1)
		{
			quickMenuButtonSelection();
		}	
	}
	//If the searchBar is clicked, it'll clear the field.
	public void mousePressed(MouseEvent e) 
	{
		if (e.getSource().equals(searchBar) && !wasKeyPressedBefore)
		{
			searchBar.setText("");
		}
	}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyPressed(KeyEvent e) 
	{
//		System.out.println("I pressed key: " + e.getKeyCode());
//		if (e.getKeyCode() == 157) //for Command key for MAC

		if (e.getKeyCode() == 18 && wasKeyPressedBefore == false)
		{
//				System.out.println("Command");

			if (quickMenuSelectPaintFlag == 1 && selectedClientPos != -1)
			{
				searchBar.setEnabled(false);
				searchBar.removeMouseListener(this);
				searchStart.setEnabled(false);
				searchStart.removeMouseListener(this);
				addRemoveClientButton.setEnabled(false);
				addRemoveClientButton.removeMouseListener(this);
				contactsMainButton.setEnabled(false);
				contactsMainButton.removeMouseListener(this);
				
				glassPane.addMouseListener(this);
				glassPane.addMouseMotionListener(this);
				glassPane.setOpaque(false);
				glassPane.setVisible(true);
				setMousePos(MouseInfo.getPointerInfo().getLocation());
				quickMenuLocation = getMousePos();
				quickMenu.show(quickMenuLocation, quickMenuSelectPaintFlag);
			}
			else if (quickMenuSelectPaintFlag == 2 && !isEditingClientInfo)
			{
				searchBar.setEnabled(false);
				searchBar.removeMouseListener(this);
				searchStart.setEnabled(false);
				searchStart.removeMouseListener(this);
				editClientInfoButton.setEnabled(false);
				editClientInfoButton.removeMouseListener(this);
				exitClientInfoButton.setEnabled(false);
				exitClientInfoButton.removeMouseListener(this);
				editNotesClientButton.setEnabled(false);
				editNotesClientButton.removeMouseListener(this);
				addRemoveContactClientButton.setEnabled(false);
				addRemoveContactClientButton.removeMouseListener(this);
				editRoiStatusButton.setEnabled(false);
				editRoiStatusButton.removeMouseListener(this);
				
				for (JButton a: clientContactMoreButtonList)
				{
					a.setEnabled(false);
					a.removeMouseListener(this);
				}
				
				glassPane.addMouseListener(this);
				glassPane.addMouseMotionListener(this);
				glassPane.setOpaque(false);
				glassPane.setVisible(true);
				setMousePos(MouseInfo.getPointerInfo().getLocation());

				quickMenuLocation = getMousePos();
				quickMenu.show(quickMenuLocation, quickMenuSelectPaintFlag);
			}
			wasKeyPressedBefore = true;
			this.removeMouseListener(this);
		}
		else if (e.getKeyCode() == 17 && wasKeyPressedBefore == false)
		{
			//For Future Functionality
		}
	}
	/**
	 * For QuickMenu
	 * when a client is selected in the searchResults, releasing the alt will initiate the menu changes 
	 * depending on whether we're in Client Search Results or Client Info Screen.
	 */
	public void keyReleased(KeyEvent e) 
	{
		if (quickMenuSelectPaintFlag == 1)
		{
			searchBar.setEnabled(true);
			searchBar.addMouseListener(this);
			searchStart.setEnabled(true);
			searchStart.addMouseListener(this);
			addRemoveClientButton.setEnabled(true);
			addRemoveClientButton.addMouseListener(this);
			contactsMainButton.setEnabled(true);
			contactsMainButton.addMouseListener(this);
			
			glassPane.setVisible(false);
			glassPane.removeMouseListener(this);
			glassPane.removeMouseMotionListener(this);
		}
		else if (quickMenuSelectPaintFlag == 2)
		{
			searchBar.setEnabled(true);
			searchBar.addMouseListener(this);
			searchStart.setEnabled(true);
			searchStart.addMouseListener(this);
			editClientInfoButton.setEnabled(true);
			editClientInfoButton.addMouseListener(this);
			exitClientInfoButton.setEnabled(true);
			exitClientInfoButton.addMouseListener(this);
			editNotesClientButton.setEnabled(true);
			editNotesClientButton.addMouseListener(this);
			addRemoveContactClientButton.setEnabled(true);
			addRemoveContactClientButton.addMouseListener(this);
			editRoiStatusButton.setEnabled(true);
			editRoiStatusButton.addMouseListener(this);
			
			for (JButton a: clientContactMoreButtonList)
			{
				a.setEnabled(true);
				a.addMouseListener(this);
			}
			
			glassPane.removeMouseListener(this);
			glassPane.removeMouseMotionListener(this);
			glassPane.setVisible(false);
		}
		if (wasQuickMenuButtonPressed)
		{
			//clientSearchResults
			if (quickMenuSelectPaintFlag == 1)
			{
				//Go to Client Info Menu
				if (quickMenuSelect == 1)
				{
					clientInfoButton();
					quickMenuSelect = 0;
				}
				//For buttons still in progress.
//				else if (quickMenuSelect == 2)
//				{
//					System.out.println("Button " + quickMenuSelect + " was pressed.");
//				}
//				else if (quickMenuSelect == 3)
//				{
//					System.out.println("Button " + quickMenuSelect + " was pressed.");
//				}
//				else if (quickMenuSelect == 4)
//				{
//					System.out.println("Button " + quickMenuSelect + " was pressed.");
//				}
			}
			//Client Info Menu
			else if (quickMenuSelectPaintFlag == 2)
			{
				//Edit Client Raw Info
				if (quickMenuSelect == 1)
				{
					editClientRawInfoButton();
					quickMenuSelect = 0;
				}
				//Edit Client Notes
				else if (quickMenuSelect == 2)
				{
					editNotesClientButton();
					quickMenuSelect = 0;
				}
				//Back to main menu.
				else if (quickMenuSelect == 3)
				{
					searchResults = masterControl.searchClientName(currentSearchData);
					clientSearchResults((ArrayList<Client>)searchResults);
					quickMenuSelect = 0;
				}
				//Add/Remove client contact info
				else if (quickMenuSelect == 4)
				{
					addRemoveClientContactButton();
					quickMenuSelect = 0;
				}
			}
			wasQuickMenuButtonPressed = false;
		}
		else
		{
			revalidate();
			repaint();	
		}
		wasKeyPressedBefore = false;
		this.addMouseListener(this);
		this.requestFocusInWindow();
	}
	public void keyTyped(KeyEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	/**
	 * Finds the location of the mouse on the screen.
	 */
	public void mouseMoved(MouseEvent e) 
	{		
		if (e.getSource().equals(glassPane))
		{
			setMousePos(e.getLocationOnScreen());	
		}
	}
	/**
	 * This method sets the Current Mouse Position calculated from the on-screen position of the mouse
	 * subtracting the position of the top left corner of the actual program. This gives us an
	 * accurate position on the program area regardless of Z-Order.
	 * 
	 * @param mouseOnScreenPoint (Point)
	 */
	public void setMousePos(Point mouseOnScreenPoint)
	{		
		Point base = glassPane.getLocationOnScreen();
		mousePos = new Point((mouseOnScreenPoint.x - base.x), (mouseOnScreenPoint.y - base.y));	
	}
	/**
	 * Returns the value stored in MousePos.
	 * 
	 * @return MousePos, Point
	 */
	public Point getMousePos()
	{
		return mousePos;
	}
	//For Future Functionality, to have the application
	//resize when maximized.
	WindowStateListener windowListener = new WindowAdapter()
	{
		public void windowStateChanged(WindowEvent evt)
		{
			int oldState = evt.getOldState();
			int newState = evt.getNewState();
			
			if (((oldState & Frame.ICONIFIED)==0) && ((newState & Frame.ICONIFIED) != 0))
			{
//				System.out.println("Window was Iconized");
			}
			else if (((oldState & Frame.ICONIFIED)!= 0) && ((newState & Frame.ICONIFIED)==0))
			{
//				System.out.println("Window was Deiconized");
			}
			
			if (((oldState & Frame.MAXIMIZED_BOTH)==0) && ((newState & Frame.MAXIMIZED_BOTH) !=0))
			{
//				System.out.println("Frame was maximized.");

			}
			else if (((oldState & Frame.MAXIMIZED_BOTH)!=0) && ((newState & Frame.MAXIMIZED_BOTH) ==0))
			{
//				System.out.println("Frame was minimized.");
			}
		}
	};
}




/**
 * This class represents the Quick Menu that is used for
 * quick selecting editing clients information, notes, and contacts.
 * 
 * @author Scott smalley
 * @email scottsmalley90@gmail.com
 */
package smalleysoftware;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class QuickMenu extends JComponent
{
	
	private static final long serialVersionUID = 1L;
	private JPanel glassContentPane;
	private Point location;
	private int panelInfoContentToPaint;
	private Color backgroundColor = new Color(100*(122/255), 100*(122/255), 100*(123/255), .5f);
	private Color menuColor = new Color(12, 36, 126);

	//Constructor
	public QuickMenu(JPanel glassContentPane)
	{
		this.glassContentPane = glassContentPane;
	}
	
	/**
	 * Start of the painting process. 
	 * it requires the point location to paint, 
	 * and which menu to paint. Whether it be the
	 * clientSearchResults method or the clientInfo method.
	 * 
	 * @param location
	 * @param panelInfoContentToPaint
	 */
	public void show(Point location, int panelInfoContentToPaint)
	{
		this.location = location;
		this.panelInfoContentToPaint = panelInfoContentToPaint;
		paintComponent(glassContentPane.getGraphics());
	}
	/**
	 * Paints based on the location and which panel it's painting
	 * draws the background to cover the main screen, then the menu itself.
	 */
	public void paintComponent(Graphics g)
	{
		Graphics2D gg = (Graphics2D)g;
		gg.setColor(backgroundColor);
		gg.fillRect(0, 0, glassContentPane.getWidth(), glassContentPane.getHeight());

		//black box
		gg.setColor(menuColor);		
		gg.fillRect(location.x-80, location.y-80, 160, 160);
		
		//lines
		gg.setColor(Color.LIGHT_GRAY);
		gg.drawLine(location.x, location.y-80, location.x, location.y+80);
		gg.drawLine(location.x-80, location.y, location.x+80, location.y);
		//outside lines
		gg.drawRect(location.x-80, location.y-80, 160, 160);
		
		if (panelInfoContentToPaint == 1)
		{
			gg.drawString("Client Info", location.x-68, location.y-34); //1
		
			gg.drawString("Program", location.x+18, location.y-34);	//2
			gg.drawString("(W.I.P.)", location.x+22, location.y-21);
			
			gg.drawString("Balance", location.x-61, location.y+42);	//3
			gg.drawString("(W.I.P.)", location.x-58, location.y+56);
			
			gg.drawString("Documents", location.x+10, location.y+42);	//4
			gg.drawString("(W.I.P.)", location.x+22, location.y+56);
		}
		if (panelInfoContentToPaint == 2)
		{
			gg.drawString("Edit Client", location.x-68, location.y-39); //1
			gg.drawString("Information", location.x-70, location.y-24); //1
		
			gg.drawString("Edit Notes", location.x+13, location.y-34);	//2
			
			gg.drawString("Exit", location.x-49, location.y+41);	//3
			
			gg.drawString("Edit Contacts", location.x+5, location.y+37);	//4
			gg.drawString("List", location.x+31, location.y+52);	//4
			
		}
	}
}

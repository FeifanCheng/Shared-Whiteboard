/**
 * 
 */
package Client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.SystemColor;

import javax.swing.UIManager;

/**
 * @author Feifan Cheng
 * Student ID: 1164589
 * the class is to set color as corresponding string
 * and get the color from the corresponding string information
 */

public class Colour {
	
	public void colour(String c, Graphics g) {
		/*
		 * iterate the information and get the color
		 */
		switch(c){
			case "BLACK":
				g.setColor(Color.BLACK);
				break;
			case "RED":
				g.setColor(Color.RED);
				break;
			case "ToolTip":
				g.setColor(UIManager.getColor("ToolTip.background"));
				break;
			case "YELLOW":
				g.setColor(Color.YELLOW);
				break;
			case "CYAN":
				g.setColor(Color.CYAN);
				break;
			case "LIGHT":
				g.setColor(Color.LIGHT_GRAY);
				break;
			case "PINK":
				g.setColor(Color.PINK);
				break;
			case "ORANGE":
				g.setColor(Color.ORANGE);
				break;
			case "MAGENTA":
				g.setColor(Color.MAGENTA);
				break;
			case "DARK_GRAY":
				g.setColor(Color.DARK_GRAY);
				break;
			case "controlHighlight":
				g.setColor(SystemColor.controlHighlight);
				break;
			case "CheckBox":
				g.setColor(UIManager.getColor("CheckBox.select"));
				break;
			case "PasswordField":
				g.setColor(UIManager.getColor("PasswordField.selectionBackground"));
				break;
			case "PopupMenu":
				g.setColor(UIManager.getColor("PopupMenu.selectionBackground"));
				break;
			case "borderHighlight":
				g.setColor(UIManager.getColor("InternalFrame.borderHighlight"));
				break;
			case "borderDarkShadow":
				g.setColor(UIManager.getColor("InternalFrame.borderDarkShadow"));
				break;		
			}
	}
	
	
	public String set_colour(Color c) {
		/*
		 * set a string to one color
		 */
		String set_string_color = "";
		if(c == Color.BLACK)
			set_string_color = "BLACK";
		else if(c == Color.RED)
			set_string_color = "RED";
		else if(c == UIManager.getColor("ToolTip.background"))
			set_string_color = "ToolTip";
		else if(c == Color.YELLOW)
			set_string_color = "YELLOW";
		else if(c == Color.CYAN)
			set_string_color = "CYAN";
		else if(c == Color.LIGHT_GRAY)
			set_string_color = "LIGHT";
		else if(c == Color.PINK)
			set_string_color = "PINK";
		else if(c == Color.ORANGE)
			set_string_color = "ORANGE";
		else if(c == Color.MAGENTA)
			set_string_color = "MAGENTA";
		else if(c == Color.DARK_GRAY)
			set_string_color = "DARK_GRAY";
		else if(c == SystemColor.controlHighlight)
			set_string_color = "controlHighlight";
		else if(c == UIManager.getColor("CheckBox.select"))
			set_string_color = "CheckBox";
		else if(c == UIManager.getColor("PasswordField.selectionBackground"))
			set_string_color = "PasswordField";
		else if(c == UIManager.getColor("PopupMenu.selectionBackground"))
			set_string_color = "PopupMenu";
		else if(c == UIManager.getColor("InternalFrame.borderHighlight"))
			set_string_color = "borderHighlight";
		else if(c == UIManager.getColor("InternalFrame.borderDarkShadow"))
			set_string_color = "borderDarkShadow";
		return set_string_color;
	}
}








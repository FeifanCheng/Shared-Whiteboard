/**
 * 
 */
package Client;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author Feifan Cheng
 * Student ID: 1164589
 * this class is to draw and text in the white board
 */
public class Draw {
	
	
	public Graphics_Text straight (Graphics g, int a1, int a2, int b1, int b2) {
		/*
		 * Draw Straight Line
		 */
		get_line(g, a1, a2, b1, b2);
		Color c = g.getColor();
   		return new Graphics_Text("line", c, a1, b1, a2, b2);	
		}
	
	
	public Graphics_Text oval (Graphics g, int a1, int a2, int b1, int b2) {
		/*
		 * Draw Oval
		 */
		get_oval(g, a1, a2, b1, b2);
   		Color c = g.getColor();
   		return new Graphics_Text("oval", c, a1, b1, a2, b2);	
		}
	
	
	public Graphics_Text rectangle (Graphics g, int a1, int a2, int b1, int b2) {
		/*
		 * Draw Rectangle
		 */
		get_rectangle(g, a1, a2, b1, b2);
   		Color c = g.getColor();
   		return new Graphics_Text("rectangle", c, a1, b1, a2, b2);	
		}
	
	public Graphics_Text circle (Graphics g, int a1, int a2, int b1, int b2) {
		/*
		 * Draw Circle
		 */
		get_circle(g, a1, a2, b1, b2);
		Color c = g.getColor();
   		return new Graphics_Text("circle", c, a1, b1, a2, b2);	
		}
	
	public Graphics_Text text (String ope, Graphics g,int a, int b) {
		/*
		 * text function
		 */
		g.drawString(ope, a, b);
		Color c = g.getColor();	
        return new Graphics_Text("text", c, ope, a, b);
	}
	
	public void manager (int flag, Graphics_Text[] store_graph_text, Graphics_Text graph_text) {
		/*
		 * Save shape objects in an array
		 */
		if (Action.flag > flag) {
    		flag = Action.flag ;
    	}
		store_graph_text[flag] = graph_text;
		flag = flag + 1;
	}
	


	public Graphics_Text straight1 (Graphics g, int a1, int a2, int b1, int b2, Color c) {
		/*
		 * Draw Straight Line
		 */
		get_line(g, a1, a2, b1, b2);
   		return new Graphics_Text("line", c, a1, b1, a2, b2);	
		}
	
	
	public Graphics_Text oval1 (Graphics g, int a1, int a2, int b1, int b2, Color c) {
		/*
		 * Draw Oval
		 */
		get_oval(g, a1, a2, b1, b2);
   		return new Graphics_Text("oval", c, a1, b1, a2, b2);
		}
	
	
	public Graphics_Text rectangle1 (Graphics g, int a1, int a2, int b1, int b2, Color c) {
		/*
		 * Draw Rectangle
		 */
		get_rectangle(g, a1, a2, b1, b2);
   		return new Graphics_Text("rectangle", c, a1, b1, a2, b2);	
		}
	
	public Graphics_Text circle1 (Graphics g, int a1, int a2, int b1, int b2, Color c) {
		/*
		 * Draw Circle
		 */		
		get_circle(g, a1, a2, b1, b2);
		return new Graphics_Text("circle", c, a1, b1, a2, b2);	 		
		}
	
	public Graphics_Text text1 (String ope, Graphics g,int a, int b, Color c) {
		/*
		 * text function
		 */
		g.drawString(ope, a , b);
        return new Graphics_Text("text", c, ope, a, b); 	
	}
	
	public void manager_listener( Graphics_Text[] store_graph_text, Graphics_Text graph_text) {
		store_graph_text[Action.flag] = graph_text;
		Action.flag = Action.flag + 1;
	}
	
	
	public void draw_text (Graphics g, int a1, int a2, int b1, int b2, String operation, String input_text) {
		switch (operation) 
		{
			case "line":
				get_line(g, a1, a2, b1, b2);
				break;
			case "oval":
				get_oval(g, a1, a2, b1, b2);
				break;
			case "rectangle":
				get_rectangle(g, a1, a2, b1, b2);
				break;
			case "circle":
				get_circle(g, a1, a2, b1, b2);
				break;
			case "text":
				g.drawString(input_text, a1, b1);
				break;
		}
	}
	
	public void get_oval(Graphics g, int a1, int a2, int b1, int b2) {
		/*
		 * draw oval
		 */
		g.drawOval(Math.min(a1, a2), Math.min(b1, b2), Math.abs(a1 - a2), Math.abs(b1 - b2)); 
	}
	
	
	public void get_rectangle(Graphics g, int a1, int a2, int b1, int b2) {
		/*
		 * draw rectangle
		 */
		g.drawRect(Math.min(a1, a2), Math.min(b1, b2), Math.abs(a1 - a2), Math.abs(b1 - b2)); 
	}
	
	public void get_circle(Graphics g, int a1, int a2, int b1, int b2) {
		/*
		 * draw circle
		 */
		g.drawOval(Math.min(a1, a2), Math.min(b1, b2), Math.abs(a1 - a2), Math.abs(a1-a2));
	}
	
	public void get_line(Graphics g, int a1, int a2, int b1, int b2) {
		/*
		 * draw line
		 */
		g.drawLine(a1, b1, a2, b2);
	}
	

	
}

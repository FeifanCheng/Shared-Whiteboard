package Client;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author: Feifan Cheng
 * Student ID: 1164589
 * store the objects paint in the white board
 **/

public class Graphics_Text {
	
	private int a1;
	private int	a2;
	private int b1;
	private int b2;
	private String operation;
	private String input_text;
	private Color c;
	static Draw draw = new Draw();

	
	public Graphics_Text(String operation, Color c, int a1, int b1, int a2, int b2) {
		/*
		 * set the parameter of line, oval, rectangle and circle
		 */
		this.operation = operation;
		this.c = c;
		this.a1 = a1;
		this.b1 = b1;
		this.a2 = a2;
		this.b2 = b2;
	}
	
	public Graphics_Text(String operation, Color c, String input_text, int a1, int b1) {
		/*
		 * set the parameter of a text operation
		 */
		this.operation = operation;
		this.input_text = input_text;
		this.c = c;
		this.a1 = a1;
		this.b1 = b1;
	}

	public void clear_all() {
		/*
		 * reset all the color and their parameter
		 */
		input_text = "";
		c = null;
		a1 = 0;
		b1 = 0;
		a2 = 0;
		b2 = 0;
	}


	public void text_draw(Graphics g) {
		/*
		 * set the color and Draw the corresponding shape according to the shape name
		 */
		g.setColor(c);
		draw.draw_text(g, a1, a2, b1, b2, operation, input_text);	
	}
	
	
}
















/**
 * 
 */
package Client;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author Feifan Cheng
 * Student id: 1164589
 * this class is to initial the shape of the white board
 */
public class Initial_shape {

	
	static Draw draw = new Draw();
	static Colour get_colour = new Colour();
	
	
	public void shape(Graphics g, String information, int flag, Graphics_Text[] store_graph_text) {

	    String info_list[] = information.substring(1).split(";");
	    for(int i = 0; i < info_list.length; i = i + 1){
	    	// get the operation of client
	    	String ope = info_list[i];
	    	get_shape(g, ope, flag, store_graph_text);
	   	}
	    g.setColor(Color.BLACK);
	}
	
	
	public void get_shape(Graphics g, String ope, int flag, Graphics_Text[] store_graph_text) {
		/*
		 * check if is the text function
		 */
        if(ope.substring(0,1).equals("T")){
        	text(ope ,flag, g, store_graph_text);
        }
        // check if it is other draw function
        else {
        	draw_shape(g, ope, flag, store_graph_text) ;
       	}
	}
	
	
	public void draw_shape(Graphics g, String ope, int flag, Graphics_Text[] store_graph_text) {
		/*
		 * check the draw (line, oval, rectangle, circle) function and do the corresponding action
		 */
		String get_color = "";
		int a1;
		int a2;
		int b1;
		int b2;
    	String get_coordinate [] = ope.substring(1).split(",");
    	get_color = get_coordinate[4];
    	get_colour.colour(get_color, g);
   		a1 = Integer.parseInt(get_coordinate[0]);
   		b1 = Integer.parseInt(get_coordinate[1]);
   		a2 = Integer.parseInt(get_coordinate[2]);
   		b2 = Integer.parseInt(get_coordinate[3]);
    	
   		// check if the operation is to Draw Straight Line
       	if(ope.substring(0,1).equals("L")){     
       		draw.manager(flag, store_graph_text, draw.straight(g, a1, a2, b1, b2));
       	}
       	// check if the operation is to Draw Oval
       	else if(ope.substring(0,1).equals("O")){
       		draw.manager(flag, store_graph_text, draw.oval(g, a1, a2, b1, b2));
       	}
       	// check if the operation is to Draw Rectangle
       	else if(ope.substring(0,1).equals("R")){
       		draw.manager(flag, store_graph_text, draw.rectangle(g, a1, a2, b1, b2));
       	}
       	// check if the operation is to Draw circle
       	else if(ope.substring(0,1).equals("P")){
       		draw.manager(flag, store_graph_text, draw.circle(g, a1, a2, b1, b2));	
       	}
	}
	
	
	
	public void text(String ope, int flag,  Graphics g, Graphics_Text[] store_graph_text) {
		/*
		 * check if the text function and do the action
		 */
    	String get_coordinate[] = ope.substring(1).split(",");
    	// set the color of text
    	get_colour.colour(get_coordinate[3], g);
    	// draw the text in white board
        draw.manager(flag, store_graph_text, draw.text(get_coordinate[2], g, Integer.parseInt(get_coordinate[0]), Integer.parseInt(get_coordinate[1])));
        // set and get the color
        g.setColor(g.getColor());
	}
	

}











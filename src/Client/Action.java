package Client;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;



/**
 * @author: Feifan Cheng
 * Student ID: 1164589
 * The Listener from Mouse class of the white board
 **/

public class Action implements MouseListener, ActionListener, MouseMotionListener {

	Socket socket = null;
	// ============= two coordinate =================
	// the coordinate of mouse press
	private int c1;
	private int d1;
	// the coordinate of mouse release
	private int c2;
	private int d2;
	
	
	// ============== the white board object ===============
	private Graphics g; 
	static JPanel panel; 
	private String select = "";
	// default color
	private Color color = Color.BLACK;
	// =============== store the shape and text information ===============
	static Graphics_Text[] store_graph_text;
	// to record the position in store_graph_text
	static int flag = 0;

	static Colour colour = new Colour();
	static Draw draw = new Draw();
	static Initial_shape shape = new Initial_shape();
	static Save save = new Save();

	

	public void actionPerformed(ActionEvent e) {
		/*
		 * actionPerformed
		 */
		// set the color as current color
		g.setColor(color);

		switch (e.getActionCommand()) {
			
			// Click "Send" button to send the message to server
			case "Send": {
				Date date = new Date();
				SimpleDateFormat get_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// get the time
		        String time = get_time.format(date);  
		        // get the message input by client
				String message = CreateWhiteBoard.textmessage.getText();
				// the the user's name of client
				String user = CreateWhiteBoard.user_name;
				try {
					// send the message to the server
					PrintStream out = new PrintStream(socket.getOutputStream());
					out.println("S" + user + " (" + time  + ")  :" +message);
					CreateWhiteBoard.textmessage.setText("");
				} 
				catch (IOException e1) {
					JOptionPane.showMessageDialog(panel, "Cannot send this message!", "Chat Error", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			//Click "New" button in menu bar to create a new white board
			case "New":{
				if (CreateWhiteBoard.name.equals(CreateWhiteBoard.manager)) {
					int option = JOptionPane.showConfirmDialog(panel, "Do you want to create a new white board", "New White Board",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(option == JOptionPane.YES_OPTION) {
						try {
							PrintStream out = new PrintStream(socket.getOutputStream());
							out.println("C");
						} 
						catch (IOException e1) {
							JOptionPane.showMessageDialog(panel, "Can not create a new white board!", "Create Error", JOptionPane.ERROR_MESSAGE);
						}
						clear();
					}
					else {
					}
				}
				else {
					JOptionPane.showMessageDialog(panel, "Only the manager " + CreateWhiteBoard.manager + " have permission to create new white board", "Can not create new white board", JOptionPane.INFORMATION_MESSAGE);
				}
				break;
			}
			//Click "Close" button in menu bar, the client will close
			case "Close":{
				if (CreateWhiteBoard.name.equals(CreateWhiteBoard.manager)) {
					int option = JOptionPane.showConfirmDialog(panel, "Do you want to close the client?" + "\n" + "You are the manager, if you close, the whole white board will be close!", "Close",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (option == JOptionPane.YES_OPTION) {
						try {
							PrintStream out = new PrintStream(socket.getOutputStream());
							out.println("managerclose");
							System.exit(0);
						} 
						catch (IOException e1) {
							JOptionPane.showMessageDialog(panel, "Can not close the client successfully!", "Close Error", JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
					}
				}
				else{
					int option = JOptionPane.showConfirmDialog(panel, "Do you want to close your client?", "Close",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (option == JOptionPane.YES_OPTION) {
						try {
							PrintStream out = new PrintStream(socket.getOutputStream());
							out.println("close");
							System.exit(0);
						} 
						catch (IOException e1) {
							JOptionPane.showMessageDialog(panel, "Can not close the client successfully!", "Close Error", JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
					}
				}
				break;
			}
			//Click "Kick Out" button to kick out the other users
			case "Kick Out":{
				if (CreateWhiteBoard.name.equals(CreateWhiteBoard.manager)) {
					if(CreateWhiteBoard.list_GUI.getSelectedValue() == null){
						JOptionPane.showMessageDialog(panel, "Please select one online users in the list", "Kick Out", JOptionPane.INFORMATION_MESSAGE);			
					}
					else {
						String select_name = CreateWhiteBoard.list_GUI.getSelectedValue();
						if (select_name.equals(CreateWhiteBoard.user_name)){
							JOptionPane.showMessageDialog(panel, "Can not kick out yourself", "Kick Out", JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							int option = JOptionPane.showConfirmDialog(panel, "Do you want to kick out '" + select_name + "'?", "Kick Out",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if(option == JOptionPane.YES_OPTION) {
								try {
									PrintStream out = new PrintStream(socket.getOutputStream());;
									out.println("Y" + select_name);
									JOptionPane.showMessageDialog(panel, "Kick out '" + select_name +"' successfully", "Kick Out", JOptionPane.INFORMATION_MESSAGE);
								} 
								catch (IOException e1) {
									JOptionPane.showMessageDialog(panel, "Can not kick out '" + select_name +"'!", "Kick Out Error", JOptionPane.ERROR_MESSAGE);
								}
							}
							else{
							}
						}	
					}
				}
				else {
					JOptionPane.showMessageDialog(panel, "Only the manager " + CreateWhiteBoard.manager + " have permission to kick out other users", "Can not kick out", JOptionPane.INFORMATION_MESSAGE);
				}
				break;
			}
			case "Open":{
				if (CreateWhiteBoard.name.equals(CreateWhiteBoard.manager)) {
					int option = JOptionPane.showConfirmDialog(panel, "Do you want to open the previous white board" + "\n" + "if you open the existing white board, the current one will be cleared", "Open",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (option == JOptionPane.YES_OPTION) {
					// get the shape and text information from txt file
					String shapes_text = save.open(panel);
					// send the information to the server
					try {
						PrintStream out = new PrintStream(socket.getOutputStream());
						out.println("D" + shapes_text);
					} 
					catch (IOException e1) {
						JOptionPane.showMessageDialog(panel, "Can not open the previous white board", "Open Error", JOptionPane.ERROR_MESSAGE);
					}
					// clear the white board
					clear();
					// illustrate the exiting white board in white board
	                save.upload(shapes_text, g, panel, flag, store_graph_text);
					}
					else {
					}
				}
				else {
					JOptionPane.showMessageDialog(panel, "Only the manager " + CreateWhiteBoard.manager + " have permission to open the previous white board", "Can not open the previous white board", JOptionPane.INFORMATION_MESSAGE);
				}
                break;	
			}
			//Click "Save" button to save the white board
			case "Save":{
				if (CreateWhiteBoard.name.equals(CreateWhiteBoard.manager)) {
					// send the information to server
					try {
						PrintStream out = new PrintStream(socket.getOutputStream());
						out.println("saveboard");
						save.save(panel);
					} 
					catch (IOException e1) {
						JOptionPane.showMessageDialog(panel, "Can not save the white board", "Save Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(panel, "Only the manager " + CreateWhiteBoard.manager + " have permission to save current white board", "Can not save white board", JOptionPane.INFORMATION_MESSAGE);
				}
				break;
			}
			//Click "Save As" to save the white board as a picture
			case "Save As":{
				if (CreateWhiteBoard.name.equals(CreateWhiteBoard.manager)) {
					save.Save_picture(panel, flag, store_graph_text);
				}
				else {
					JOptionPane.showMessageDialog(panel, "Only the manager " + CreateWhiteBoard.manager + " have permission to save current white board", "Can not save white board", JOptionPane.INFORMATION_MESSAGE);
				}
				break;
			}	
			// Click "Straight" button to draw straight line in white board
			case "Straight":
				select = "L";
				break;
			//Click "Oval" button to draw oval in white board
			case "Oval":
				select = "O";
				break;
			//Click "Rectangle" button to draw rectangle in white board
			case "Rectangle":
				select = "R";
				break;
			//Click "Circle" button to draw circle in white board
			case "Circle":
				select = "P";
				break;	
			//Click "Text" button to put input text in white board
			case "Text":
				select = "T";
				break;	
			//Click other button to set the color.
			case "":
				JButton different_color = (JButton) e.getSource();
				color = different_color.getBackground();
				// set the button as the current color
				CreateWhiteBoard.straight_line.setForeground(color);
				CreateWhiteBoard.oval.setForeground(color);
				CreateWhiteBoard.circle.setForeground(color);
				CreateWhiteBoard.text_t.setForeground(color);
				CreateWhiteBoard.rectangle.setForeground(color);
				CreateWhiteBoard.chat_panel.setBackground(color);
				if (color == Color.BLACK) {
					CreateWhiteBoard.manager2.setBackground(color);
					CreateWhiteBoard.manager2.setForeground(Color.WHITE);
				}
				else {
					CreateWhiteBoard.manager2.setBackground(color);
					CreateWhiteBoard.manager2.setForeground(Color.BLACK);
				}
				g.setColor(color);
				break;
		}
	}

	public void clear() {
		panel.repaint();
		if (CreateWhiteBoard.flag > flag) {
			flag = CreateWhiteBoard.flag;
		}
		for(int i = 0 ; i < flag ; i = i + 1){
			if (store_graph_text[i] == null) {
				break;
			}
			else {
				store_graph_text[i].clear_all();
			}
		}
	}
	
	
	public void mousePressed(MouseEvent e) {
		/*
		 * when the mouse press, get the coordinates
		 */
		c1 = e.getX();
		d1 = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
		/*
		 * when the mouse release, get the coordinates
		 */
		c2 = e.getX();
		d2 = e.getY();
		
		
		int a1 = c1;
		int a2 = c2;
		int b1 = d1;
		int b2 = d2;
		
		
		try{
			// set the color as the corresponding String
			String string_color = colour.set_colour(color);
			// get output stream, used to send data to the server
			PrintStream out = new PrintStream(socket.getOutputStream());
			if (CreateWhiteBoard.flag > flag) {
				flag = CreateWhiteBoard.flag;
			}
			if (select == "L") {
				// draw straight line in white board
				draw.manager_listener(store_graph_text, draw.straight1(g, a1, a2, b1, b2, color));
				// send the message to server
				out.println(graph_message(a1, a2, b1, b2, string_color));
			}
			if (select == "O") {
				// draw oval in white board
				draw.manager_listener(store_graph_text, draw.oval1(g, a1, a2, b1, b2, color));
				// send the message to server
				out.println(graph_message(a1, a2, b1, b2, string_color));
			}
			if (select == "R") {
				// draw the rectangle in white board
				draw.manager_listener(store_graph_text, draw.rectangle1(g, a1, a2, b1, b2, color));
				// send the message to server
				out.println(graph_message(a1, a2, b1, b2, string_color));
			}
			if (select == "P") {
				// draw circle in white board
				draw.manager_listener(store_graph_text, draw.circle1(g, a1, a2, b1, b2, color));
				// send the message to server
				out.println(graph_message(a1, a2, b1, b2, string_color));
			}
			if(select == "T") {	
				int a = a1;
				int b = b1 ;
			

				// set a Text Area in GUI to text word
				JTextField choose_to_text = new JTextField();
				// add its text area in GUI
				panel.add(choose_to_text);
				choose_to_text.setForeground(color);
				choose_to_text.setBounds(a ,b , 90, 30);
				choose_to_text.addFocusListener(new FocusListener(){
					
		            @Override
		            public void focusLost(FocusEvent e) { 
		            	/*
		            	 * the focus of mouse lost
		            	 */
		            	
		            	panel.remove(choose_to_text); 
		            	panel.updateUI();
		            	
		            	// draw the text in the white board
		            	draw.manager_listener(store_graph_text, draw.text1(choose_to_text.getText(), g, a + 6, b + 18, color));    
		            	// send the message to server	
		            	out.println(text_message(a + 6, b + 18, choose_to_text.getText(), string_color));
		            	
		            }
		            @Override
		             public void focusGained(FocusEvent e) {
		            	/*
		            	 * the focus of mouse gain
		            	 */
		            }     
		        });			
			}
		}
		catch(Exception e1)
		{
			JOptionPane.showMessageDialog(panel, "Can not draw or text in the white board", "Draw Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public Action(Socket s){
		/*
		 * set the socket
		 */
		this.socket = s;
	}
	
	public void set_graphics(Graphics graph) {
		/*
		 * initialize the white board
		 */
		g = graph;
	}
	
	public String graph_message(int a1, int a2, int b1, int b2, String string_color) {
		/*
		 * send the message which store graphic to sever
		 */
		return select + Integer.toString(a1) + "," + Integer.toString(b1) + "," + Integer.toString(a2) + "," + Integer.toString(b2) + ","  + string_color ;	
	}
	
	public String text_message(int a, int b, String text,String string_color) {
		/*
		 * send the message which store text to sever
		 */
		return select + Integer.toString(a) + "," + Integer.toString(b) + "," + text + "," + string_color;
	}
	
	
	public void setthis(JPanel jpanel){
		/*
		 * set the panel
		 */
		panel = jpanel;
	}

	public void setShapeArray(Graphics_Text[] store_graph_text) {
		this.store_graph_text = store_graph_text;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	

	
}	
	
	
	
	
	
	
	
	
	
	
	
	

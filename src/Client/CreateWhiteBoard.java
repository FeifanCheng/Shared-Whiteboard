package Client;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.SystemColor;


/**
 * @author: Feifan Cheng
 * Student id: 1164589
 * The Client and client GUI class of shared white board
 **/

public class CreateWhiteBoard extends JPanel {

	//=============== the socket, PrintStream and BufferedReader ===============
	static Socket socket;
	static PrintStream out;
    static BufferedReader in;
    // check if the client is connection; 1 means connection; 0 means not connection
    static int check_connection = 1;
    
	// =============== store the shape and text information ===============
	static Graphics_Text[] store_graph_text = new Graphics_Text[9999999];
	// to record the position in store_graph_text
	static int flag = 0;
	
    // =============== GUI =============
    static Graphics g;
    static JFrame frame = new JFrame();
    // the chat window to show the chat message time and user's name
    static JTextArea chat_window = new JTextArea();
    // to show the online client's name 
	static JList<String> list_GUI = new JList<String>();
	// text message in the GUI
	static JTextField textmessage = new JTextField();
	static JButton straight_line = new JButton("Straight");
	static JButton circle = new JButton("Circle");
	static JButton oval = new JButton("Oval");
	static JButton rectangle = new JButton("Rectangle");
	static JButton text_t = new JButton("Text");
	static JPanel function_panel = new JPanel();
	static JPanel chat_panel = new JPanel(); 
	static JTextField manager2 = new JTextField();
	// store the information from client which store all online users' name information
	static String name = "";
	// store the name
	static String user_name = "";
	// manager's name
	static String manager;
	// String [] to store all the online users
	static String[] online_users;
	// list to store all online users
	private static List<String> name_list = new ArrayList<String>();
	// AtomicInteger for security in Socket to get permit information from manager
	static AtomicInteger permit_from_manager = new AtomicInteger(); 
	static String shapes_text;
	static Colour get_colour =  new Colour();
	static Save save = new Save();
	static Draw draw = new Draw();
	static Initial_shape shape = new Initial_shape();
	static Update update = new Update();
	

	
	public static void main(String[] args) {
	
		try {
			if (args.length != 3) {
				JOptionPane.showMessageDialog(null, "please enter IP first ,port number second and username last. like" + "\n " + 
						"java -jar CreateWhiteBoard.jar <serverIPAddress> <serverPort> username" , "Input Error", JOptionPane.ERROR_MESSAGE);		
			}		
			else {			
				// first input in command line is the IP address
				String ip = args[0];
				// second input in command line is the port number
				String portnumber = args[1];
				int port = Integer.parseInt(portnumber);
				socket = new Socket(ip, port);
				// get the user name
				user_name = args[2];	
				CreateWhiteBoard createwhiteboard = new CreateWhiteBoard();
				new from_server_Thread(socket);
				g = createwhiteboard.WhiteBoard_GUI(socket);	    
				// wait for the permission
			    while(permit_from_manager.get() == 0) {
			    	JOptionPane.showMessageDialog(frame, "Waitting for the permission of manager!!!", "Please Wait", JOptionPane.INFORMATION_MESSAGE);
			    	}
			    // the manager permit to join
			    if(permit_from_manager.get() == 1) {
			    	if (user_name.equals(manager)) {
			    		JOptionPane.showMessageDialog(frame, "You are the manager of this white board! " +"\n" + "Only you have permission to use 'Kick Out', 'New', 'Open', 'Save', 'Save As' button", "White Board Create", JOptionPane.INFORMATION_MESSAGE);
			    	}
			    	else {
			    		JOptionPane.showMessageDialog(frame, "Join Succesfully, you can draw and chat by using this white board" , user_name , JOptionPane.INFORMATION_MESSAGE);
			    		}
			    	}
			    // the manager does not permit
			    else if (permit_from_manager.get() == 2) {
			    	try {
			    		check_connection = 0;
					    in.close();
		                out.close();
		                socket.close();
					}
					catch(Exception e){	
					}
			    	JOptionPane.showMessageDialog(frame, "Manager does not approve your connection!", "Client Close", JOptionPane.ERROR_MESSAGE);
			    	System.exit(0);
			    }
			    if	(user_name.equals(manager)) {
			    	frame.setTitle("Manager's White Board | manager's name " + manager); 
			    }
			    else {
			    	frame.setTitle("User's White Board | user's name " + user_name); 
			    }
			}
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Some Exception error in client" , "Client Error", JOptionPane.ERROR_MESSAGE);
		
		}	
	}
	
	
	
    
	public static class from_server_Thread extends Thread{
        /*
         *  check the messages from server
         */
        public from_server_Thread(Socket s){
            try { 	
            	// output the information to server
            	out = new PrintStream(s.getOutputStream());
            	// get the input from server
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out.println(user_name);
                start(); 	        
            } 
            catch (Exception e) {
            	JOptionPane.showMessageDialog(null, "Can not connect to server", "Connection Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        
        @Override
        public void run() {
            try {
                while(check_connection == 1){              	
                	String info_from_server = in.readLine();   	  
                	// manager permits the client to join        	
                	if((info_from_server.substring(0, 3)).equals("yes")){
                		// get the manager's name from server
                		permit_from_manager.getAndIncrement();
                		manager = info_from_server.substring(3).split("]")[0];
                		manager2.setText(manager);
                		// get the users name
                        name = info_from_server.substring(3).split("]")[1]; 
                        JOptionPane.showMessageDialog(frame, "The manager of this white board is '" + manager + "'", "Manager", JOptionPane.INFORMATION_MESSAGE);                 
                    }
                	// manager does not approve the client to join
                	else if ((info_from_server.substring(0, 2)).equals("no")){
                        permit_from_manager.getAndIncrement();
                        permit_from_manager.getAndIncrement();
                        break;
                    }     
                	else if((info_from_server.substring(0, 5)).equals("close")){
                		//The client applies for exit, and the server returns to confirm exit
                		check_connection = 0;
                		break;
                	}
                	//The server initializes and sends all shapes on the current White Board
                    else if ((info_from_server.substring(0,1)).equals("#")){
                        shape.shape(g, info_from_server, flag, store_graph_text);
                    }
                	// open the file
                    else if ((info_from_server.substring(0,1)).equals("X")){          		
                    	shapes_text = info_from_server.substring(1);
                	}
                	// remove the name of kicks users or the exiting user
                    else if(info_from_server.substring(0,5).equals("kicks")){
                    	update.remove(name_list, online_users, list_GUI, info_from_server.substring(5));
                   	}
                	// update Online peer list
                    else if (info_from_server.substring(0, 1).equals("$")){	    
                      
                    	update.add_name(name, name_list, info_from_server, online_users, list_GUI);
                    }	
                    
                	// chat function and update the chat information in chat window
                    else if (info_from_server.substring(0, 1).equals("S")){
                    	update.chat(info_from_server, chat_window);					
                    }
                	//Synchronize information in all the other client
                    else{	
                    	// use a String [] to store the name and corresponding operation from server
                        String from_Server[] = info_from_server.split(":");
                       	// the information and operation
                        String ope_info = from_Server[1];                   
                        // receive function from server is to update new client user
                        if(ope_info.substring(0,1).equals("$")){  
                        	name_list.clear();
                       		update.add_name(name, name_list, ope_info, online_users, list_GUI);
                       	}
                        // one client open a white board
                        else if (ope_info.substring(0, 1).equals("D")){	
                        	Action.panel.repaint();
                        	update.clear(flag, store_graph_text);
                        	// illustrate the exiting white board in white board
                        	save.upload(ope_info, g, function_panel, flag, store_graph_text);
                        }
                        // receive function from server is to create a new white board
                        else if(ope_info.substring(0,1).equals("C")){                         
                        	Action.panel.repaint();
                        	update.clear(flag, store_graph_text);
                        	Action.panel.updateUI();
                			JOptionPane.showMessageDialog(frame, "Manager create a new White Board", "New White Board", JOptionPane.INFORMATION_MESSAGE);   		
                        }
                        
                        // receive function form server is to kicks out other user
                        else if(ope_info.substring(0,1).equals("Y")){
                       		out = new PrintStream(socket.getOutputStream());
                       		out.println("close");
                        	CreateWhiteBoard.socket.close();
                        	JOptionPane.showMessageDialog(frame, "Manager kicks out you", "Kick Out", JOptionPane.INFORMATION_MESSAGE);
        					System.exit(0);
                       	}
                       	// receive the function from server is to text
                        else if(ope_info.substring(0,1).equals("T")){
                       		shape.text(ope_info, flag, g, store_graph_text);
                        }      
                        // receive the function from server is to draw
                        else {
                        	shape.draw_shape(g, ope_info, flag, store_graph_text);
                            Color currentcolor = g.getColor();
                            g.setColor(currentcolor);
                        }
                    }
                }
            }
            catch (Exception e) 
            {
            	JOptionPane.showMessageDialog(frame, "Manager closed the application, your application will be closed", "Closed!", JOptionPane.INFORMATION_MESSAGE);
            	System.exit(0);
            }
        }
    }
    
	public Graphics WhiteBoard_GUI(Socket s) {
		
		frame.setTitle("Manager Shared White Board"); 
		frame.setSize(800, 800); 
		frame.getContentPane().setBackground(Color.WHITE); 
		frame.setLocationRelativeTo(null); 
		frame.setResizable(false);
		//======================================================
		//Create and add the mouse listener
		Action manager = new Action(s);
		this.addMouseListener(manager);
		this.addMouseMotionListener(manager);
		
		// set the GUI as flow
		java.awt.FlowLayout flow = new java.awt.FlowLayout();
		
		//===========================================================
		//A “File” menu with new, open, save, saveAs and close button
		JMenuBar menuBar = new JMenuBar();
		
		menuBar.setForeground(UIManager.getColor("Menu.selectionBackground"));
		menuBar.setFont(new Font("Hiragino Sans", Font.BOLD, 16));
		menuBar.setBackground(UIManager.getColor("MenuItem.selectionBackground"));
		JMenu file_m = new JMenu("File");
		file_m.setFont(new Font("Lao Sangam MN", Font.BOLD, 18));
		file_m.setBackground(UIManager.getColor("MenuBar.disabledForeground"));
		// New
		JMenuItem new_i = new JMenuItem("New");
		new_i.setFont(new Font("Kohinoor Devanagari", Font.ITALIC, 14));
		new_i.setBackground(new Color(169, 169, 169));
		// Open
		JMenuItem open_i = new JMenuItem("Open");
		open_i.setFont(new Font("Kohinoor Devanagari", Font.ITALIC, 14));
		open_i.setBackground(new Color(105, 105, 105));
		// Save
		JMenuItem save_i = new JMenuItem("Save");
		save_i.setFont(new Font("Kohinoor Devanagari", Font.ITALIC, 14));
		save_i.setBackground(new Color(106, 90, 205));
		// Save As
		JMenuItem saveas_i = new JMenuItem("Save As");
		saveas_i.setFont(new Font("Kohinoor Devanagari", Font.ITALIC, 14));
		saveas_i.setBackground(new Color(72, 61, 139));
		// Close
		JMenuItem close_i = new JMenuItem("Close");
		close_i.setFont(new Font("Kohinoor Devanagari", Font.ITALIC, 14));
		close_i.setBackground(new Color(178, 34, 34));
		
		new_i.addActionListener(manager);	
		open_i.addActionListener(manager);	
		save_i.addActionListener(manager);	
		saveas_i.addActionListener(manager);	
		close_i.addActionListener(manager);
		
		file_m.add(new_i);
		file_m.addSeparator();
		file_m.add(open_i);
		file_m.addSeparator();
		file_m.add(save_i);
		file_m.addSeparator();
		file_m.add(saveas_i);
		file_m.addSeparator();
		file_m.add(close_i);
		menuBar.add(file_m);
		frame.setJMenuBar(menuBar);
	
		// the white board
		this.setBackground(Color.WHITE);
		this.setLayout(null);
		frame.add(this, BorderLayout.CENTER);
		
		// the color, shape and kick out function, 
		
		function_panel.setPreferredSize(new Dimension(110, 600));
		function_panel.setLayout(flow);
		frame.add(function_panel, BorderLayout.EAST);
		
		// ===================== color function (16 colors) =====================
		//Create a color button and add an action listener to the color button	
		// 1
		JButton Black = new JButton();
		Black.setPreferredSize(new Dimension(45, 15));
		Black.setBackground(Color.BLACK);
		Black.setOpaque(true); 
		Black.setBorderPainted(false);
		function_panel.add(Black);		
		// 2
		JButton red = new JButton();
		red.setPreferredSize(new Dimension(45, 15));
		red.setBackground(Color.RED);
		red.setOpaque(true); 
		red.setBorderPainted(false);
		function_panel.add(red);		
		// 3
		JButton green = new JButton();
		green.setPreferredSize(new Dimension(45, 15));
		green.setBackground(UIManager.getColor("ToolTip.background"));
		green.setOpaque(true); 
		green.setBorderPainted(false);
		function_panel.add(green);		
		// 4
		JButton yellow = new JButton();
		yellow.setPreferredSize(new Dimension(45, 15));
		yellow.setBackground(Color.YELLOW);
		yellow.setOpaque(true); 
		yellow.setBorderPainted(false);
		function_panel.add(yellow);
		// 5
		JButton cyan = new JButton();
		cyan.setPreferredSize(new Dimension(45, 15));
		cyan.setBackground(Color.CYAN);
		cyan.setOpaque(true); 
		cyan.setBorderPainted(false);
		function_panel.add(cyan);		
		// 6
		JButton light_gray = new JButton();
		light_gray.setPreferredSize(new Dimension(45, 15));
		light_gray.setBackground(Color.LIGHT_GRAY);
		light_gray.setOpaque(true); 
		light_gray.setBorderPainted(false);
		function_panel.add(light_gray);		
		// 7
		JButton pink = new JButton();
		pink.setPreferredSize(new Dimension(45, 15));
		pink.setBackground(Color.PINK);
		pink.setOpaque(true); 
		pink.setBorderPainted(false);
		function_panel.add(pink);		
		// 8
		JButton orange = new JButton();
		orange.setPreferredSize(new Dimension(45, 15));
		orange.setBackground(Color.ORANGE);
		orange.setOpaque(true); 
		orange.setBorderPainted(false);
		function_panel.add(orange);	
		// 9
		JButton magenta = new JButton();
		magenta.setPreferredSize(new Dimension(45, 15));
		magenta.setBackground(Color.MAGENTA);
		magenta.setOpaque(true); 
		magenta.setBorderPainted(false);
		function_panel.add(magenta);	
		// 10
		JButton dark_gray = new JButton();
		dark_gray.setPreferredSize(new Dimension(45, 15));
		dark_gray.setBackground(Color.DARK_GRAY);
		dark_gray.setOpaque(true); 
		dark_gray.setBorderPainted(false);
		function_panel.add(dark_gray);	
		// 11
		JButton controlHighlight = new JButton();
		controlHighlight.setPreferredSize(new Dimension(45, 15));
		controlHighlight.setBackground(SystemColor.controlHighlight);
		controlHighlight.setOpaque(true); 
		controlHighlight.setBorderPainted(false);
		function_panel.add(controlHighlight);	
		// 12
		JButton CheckBox = new JButton();
		CheckBox.setPreferredSize(new Dimension(45, 15));
		CheckBox.setBackground(UIManager.getColor("CheckBox.select"));
		CheckBox.setOpaque(true); 
		CheckBox.setBorderPainted(false);
		function_panel.add(CheckBox);	
		// 13
		JButton light_pink = new JButton();
		light_pink.setPreferredSize(new Dimension(45, 15));
		light_pink.setBackground(UIManager.getColor("PasswordField.selectionBackground"));
		light_pink.setOpaque(true); 
		light_pink.setBorderPainted(false);
		function_panel.add(light_pink);
		// 14
		JButton light_blue = new JButton();
		light_blue.setPreferredSize(new Dimension(45, 15));
		light_blue.setBackground(UIManager.getColor("PopupMenu.selectionBackground"));
		light_blue.setOpaque(true); 
		light_blue.setBorderPainted(false);
		function_panel.add(light_blue);	
		// 15
		JButton dark_blue = new JButton();
		dark_blue.setPreferredSize(new Dimension(45, 15));
		dark_blue.setBackground(UIManager.getColor("InternalFrame.borderHighlight"));
		dark_blue.setOpaque(true); 
		dark_blue.setBorderPainted(false);
		function_panel.add(dark_blue);		
		// 16
		JButton purple = new JButton();
		purple.setPreferredSize(new Dimension(45, 15));
		purple.setBackground(UIManager.getColor("InternalFrame.borderDarkShadow"));
		purple.setOpaque(true); 
		purple.setBorderPainted(false);
		function_panel.add(purple);
		
		// add 16 colors into Listener
		Black.addActionListener(manager);
		red.addActionListener(manager);
		green.addActionListener(manager);
		yellow.addActionListener(manager);
		cyan.addActionListener(manager);
		light_gray.addActionListener(manager);
		pink.addActionListener(manager);
		orange.addActionListener(manager);
		magenta.addActionListener(manager);
		dark_gray.addActionListener(manager);
		controlHighlight.addActionListener(manager);
		CheckBox.addActionListener(manager);
		light_pink.addActionListener(manager);
		light_blue.addActionListener(manager);
		dark_blue.addActionListener(manager);
		purple.addActionListener(manager);
		
		
		
		//==================== add shape and text button===================
		// Straight line		
		straight_line.setContentAreaFilled(false);
		straight_line.setPreferredSize(new Dimension(105, 25));
		straight_line.setFont(new Font("Kohinoor Devanagari", Font.ITALIC, 14));
		function_panel.add(straight_line);
		// Circle		
		circle.setContentAreaFilled(false);
		circle.setPreferredSize(new Dimension(105, 25));
		circle.setFont(new Font("Kohinoor Devanagari", Font.ITALIC, 14));
		function_panel.add(circle);
		// Oval
		oval.setContentAreaFilled(false);
		oval.setPreferredSize(new Dimension(105, 25));
		oval.setFont(new Font("Kohinoor Devanagari", Font.ITALIC, 14));
		function_panel.add(oval);
		// Rectangle	
		rectangle.setContentAreaFilled(false);
		rectangle.setPreferredSize(new Dimension(105, 25));
		rectangle.setFont(new Font("Kohinoor Devanagari", Font.ITALIC, 14));
		function_panel.add(rectangle);
		// Text
		text_t.setContentAreaFilled(false);
		text_t.setPreferredSize(new Dimension(105, 25));
		text_t.setFont(new Font("Kohinoor Devanagari", Font.ITALIC, 14));
		function_panel.add(text_t);
		
		// ad four shape and text function into listener
		straight_line.addActionListener(manager);
		circle.addActionListener(manager);
		oval.addActionListener(manager);
		rectangle.addActionListener(manager);
		text_t.addActionListener(manager);
		
		//==================== add online list and kick out function ===================
		//Add the online peer list
		JLabel manager1 = new JLabel("manager's name: ");
		function_panel.add(manager1);
		
		manager2.setPreferredSize(new Dimension(105, 30));
		manager2.setEditable(false);
		manager2.setFont(new Font("Kohinoor Telugu", Font.ITALIC, 14));
		function_panel.add(manager2);
		
		//Add the online peer list
		JLabel online = new JLabel("Online Users: ");
		function_panel.add(online);

		JScrollPane lists = new JScrollPane(list_GUI);
		lists.setPreferredSize(new Dimension(105, 145));
		function_panel.add(lists);
		
		//Add the kick out button
		JButton Kickout = new JButton("Kick Out");
		Kickout.setPreferredSize(new Dimension(80, 20));
		Kickout.setFont(new Font("Kohinoor Devanagari", Font.ITALIC, 12));
		Kickout.setBackground(Color.GRAY);
		Kickout.setContentAreaFilled(false);
		Kickout.addActionListener(manager);
		function_panel.add(Kickout);
		
		
		// =================== chat function==================
		chat_panel.setPreferredSize(new Dimension(780, 180));
		chat_panel.setLayout(flow);
		frame.add(chat_panel, BorderLayout.SOUTH);
		
		// text area (chat windows) to show the message
		chat_window.setEditable(false);
		JScrollPane show_messages = new JScrollPane(chat_window);
        show_messages.setPreferredSize(new Dimension(780, 130));
		chat_panel.add(show_messages, BorderLayout.NORTH);
		// text field to text message
		textmessage.setPreferredSize(new Dimension(700, 30));
		chat_panel.add(textmessage);
		// a button to send the message
		JButton Send = new JButton("Send");
		Send.setPreferredSize(new Dimension(80, 30));
		Send.setFont(new Font("Kohinoor Devanagari", Font.ITALIC, 14));
		Send.setContentAreaFilled(false);
		chat_panel.add(Send);
		Send.addActionListener(manager);
		
		
		manager.setthis(this);	
		frame.setVisible(true);
		java.awt.Graphics graph = this.getGraphics();
		manager.set_graphics(graph);
		manager.setShapeArray(store_graph_text);
		return graph;
	}


	public void paint(Graphics g) {
		/*
		 * override the draw method
		 * take the graphic object out and save them in store_graph_text and draw them in the white board
		 */
		super.paint(g);
		for (int i = 0, len = store_graph_text.length; i < len; i = i + 1) 
		{
			Graphics_Text graph_text = store_graph_text[i];
			if (graph_text == null) {
				break;
			} else {
				graph_text.text_draw(g);
			}
		}
	}
	
}









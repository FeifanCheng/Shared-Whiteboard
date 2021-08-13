package Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JOptionPane;

import Server.Server.ServerThread;

/**
 * @author: Feifan Cheng
 * Student ID: 1164589
 * The server class of the shared white board
 **/

public class Server extends ServerSocket {
 
	// the port number
	static int port;
	// to check if all the information in thread have been printed
	static int check_thread = 0;
	// check if the sever have been connected
	static int disconnect = 0; 
	// initial the server
	private static ServerSocket server = null;
	// store the name of all the online users
	static List<String> online_users = new ArrayList<String>();
	// store all the threads in the server
	static List<ServerThread> store_thread = new ArrayList<ServerThread>();
	// store the information of the name and message which store the information client's operation
	static LinkedList<LinkedList> information_list = new LinkedList<LinkedList>(); 
	// the number of client users
	static AtomicInteger num_client = new AtomicInteger();
	// the GUI FUNCTION
	static Server_GUI GUI = new Server_GUI(); 
	// store the newest white board shape text and information
	String shapes_text = "#"; 
	// all the online users
	String[] onlineusers; 
	// store all the chat messages
	String messages = "";
	static int a = 0;
	static String manager = "";
	static Listen_information listen = new Listen_information();
	static Function function = new Function();
	

	public static void main(String[] args) throws IOException {
		/*
		 * the main function to receive port number and lunch the server of shared white board
		 * 
		 */
		try {
			
			if (args.length != 1) {
				JOptionPane.showMessageDialog(null, "please enter the port number following. like" + "\n " + 
						"java -jar Server <serverPort>", "Can not connect", JOptionPane.ERROR_MESSAGE);
			}
			else {
			
				String portnumber = args[0];
				port = Integer.parseInt(portnumber);
			    Set_GUI(portnumber);   
				try {
					//Start the server
					new Server();
				} 
				catch (IOException e1) {
					JOptionPane.showMessageDialog(GUI.frame, "Exception error" + "\n" + "Cannot start a new Server", "Server Error", JOptionPane.INFORMATION_MESSAGE);
				}	
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "please enter the port number following. like" + "\n " + 
						"java -jar Server <serverPort>", "Can not connect", JOptionPane.ERROR_MESSAGE);
		}
	}
		
	
	
	public static void Set_GUI(String port_num) {
		/*
		 * set GUI and close server listener
		 */
	    GUI.frame.setVisible(true);;
		GUI.port_num.setText("        " + port_num);
		//Close the server function
		GUI.close_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					server.close();
					System.exit(0);
					disconnect = 1;
				} 
				catch (IOException e1) {
					JOptionPane.showMessageDialog(GUI.frame, "IOException error in the Server", "Server Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	
	public Server() throws IOException {
		/*
		 * the socket in server and create thread to listen and send message from client
		 */
		try {
			server = new ServerSocket(port);
			// To handle the information get from client
			new Information_in_thread();
			while (true) { 
				// keep listen the information and request from all the client
				Socket socket = server.accept();	
				new ServerThread(socket);	
				GUI.client_num.setText("  " + num_client.toString());
				
			}
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Sever Close", "Close", JOptionPane.INFORMATION_MESSAGE);
		} 
		finally {
			close();
		}
	}
 

 
	class Information_in_thread extends Thread {
		/*
		 *  keep listening the information from client in the queue
		 *  send information to the other client
		 */
		public Information_in_thread() {
			start();
		}
 
		@Override
		public void run() {
			while (true) {
				// check if there are message in the thread
				if (check_thread == 0) {
					try {
						// if no message in thread, set the Thread to sleep for 0.6 second
						Thread.sleep(600);
					} 
					catch (InterruptedException e) {
						JOptionPane.showMessageDialog(GUI.frame, "Some error happend in Thread queue", "Server Error", JOptionPane.ERROR_MESSAGE);
					}
					continue;
				}
				information_list = listen.listen(information_list, online_users, store_thread);
				check_thread = listen.check(information_list, check_thread);
			}
		}
	}
 
	
	
	class ServerThread extends Thread {
		/*
		 * the Thread class to handle all the information from client
		 */
		// the Socket
		Socket s;
		// the output stream writer to client
		PrintWriter out;
		// the input stream reader from client
		BufferedReader in;
		//The name of client
		String username;
		
		public ServerThread(Socket socket) throws IOException {
			// set s as socket
			s = socket;
			// set disconnect as 0 which means server still connect with shome client
			disconnect = 0;
			// get the output and input from client and to client
			out = new PrintWriter(s.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			// the first input is the user name of client
			username = in.readLine();
			// record number of client
			num_client.getAndIncrement(); 	
				
			int option = JOptionPane.showConfirmDialog(GUI.frame, username + " wants to share your whiteboard", "Connection request",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			// permit to enter
			if(option == JOptionPane.YES_OPTION) {		
				// save the manager name
				if (a == 0) {
					manager = username;
					a = 1;			
				}	
				// remove this thread from store_thread
				store_thread.add(this);
				// send to permit the user to join the white board
				out.println("yes" + manager + "]" + username);
				// get the online name list in the white board
				String namelist = function.name_send(online_users, onlineusers, username, out);
				// store information in information_list
				LinkedList<String> name_information = new LinkedList<String>();
				name_information.add(username);
				name_information.add(namelist);
				information_list.addLast(name_information);
				// there is thread to connect
				check_thread = 1;		
				// check there are painting in the white board and send the white board to the other client
				if(!(shapes_text).equals("#")){
					out.println(shapes_text);
				}			
			}
			// do not permit to enter
			else {
				out.println("no");
				num_client.getAndDecrement();
				disconnect = 1;
				in.close();
				out.close();
				s.close();
			}	
			start();
		}
 
		@Override
		public void run() {
			try {
				while (disconnect == 0) {
					String line = in.readLine();
					// One client close his client
					if(line.equals("close")) {		
						function.close(online_users, username, check_thread, onlineusers, num_client);
						// send close information to client
						out.println("close");					
						GUI.client_num.setText("  " + num_client.toString());
						// remove this thread
						store_thread.remove(this);
						for (int i = 0; i < store_thread.size(); i = i + 1) {
							// from the online users to get the client name and check if this name is itself
							String client_name =  online_users.get(i);
							if (username != client_name) {
								// send the information to pointed thread
								ServerThread thread = store_thread.get(i);
								thread.out.println("kicks" + username);
							}
						}
						for (int i = 0, len = online_users.size(); i < len; i = i + 1) {
							if (online_users.get(i) == username) {
								online_users.remove(i);
								len = len - 1;
							    i = i - 1;
							}
						}
						
						break;
					}
					// manager close his client and the whole system will be closed
					else if(line.equals("managerclose")) {
						JOptionPane.showMessageDialog(GUI.frame, "manager close his client" + "\n" + "server close", "Server closed", JOptionPane.INFORMATION_MESSAGE);		
						function.manager_close(disconnect, server);
					}
					// open the previous white board saved
					else if(line.substring(0,1).equals("D")) {
						shapes_text = line.substring(1);
						LinkedList<String> name_information = new LinkedList<String>();
						name_information.add(username);
						name_information.add(line);
						information_list.addLast(name_information);
						check_thread = 1;
					}
					// to create a new white board
					else if (line.substring(0,1).equals("C")) {
						shapes_text = "#";			
						LinkedList<String> name_information = new LinkedList<String>();
						name_information.add(username);
						name_information.add("C");
						information_list.addLast(name_information);
						check_thread = 1;
					}
					// save the current white boaed
					else if(line.equals("saveboard")){
						// send shape and text information to client
						out.println("X" + shapes_text);
					}	
					// kick out other users
					else if(line.substring(0,1).equals("Y")) {		
						String name_kick = line.substring(1);
						// get the thread of kicks out and send the information to that thread to close it
						function.kickout(line, online_users, store_thread).out.println(name_kick + ":" + "Y");	
						// save the name and information in information_list
								
						for (int i = 0; i < store_thread.size(); i = i + 1) {
							// from the online users to get the client name and check if this name is itself
							String client_name =  online_users.get(i);
							if (name_kick != client_name) {
								// send the information to pointed thread
								ServerThread thread = store_thread.get(i);
								thread.out.println("kicks" + name_kick);
							}
						}
						for (int i = 0, len = online_users.size(); i < len; i = i + 1) {
							if (online_users.get(i) == name_kick) {
								online_users.remove(i);
								len = len - 1;
							    i = i - 1;
							}
						}
					}
					// chat function 
					else if(line.substring(0,1).equals("S")) {
						// the information about all the chat content
						String message = line.substring(1);
						messages = messages+ "~" + message;
						for (int i = 0; i < store_thread.size(); i = i+1) {
							store_thread.get(i).out.println("S" + messages);		
						}						
					}
					
					// store the information of shapes and text in the white board 
					else{	
						shapes_text = shapes_text + line + ";";				
						LinkedList<String> name_information = new LinkedList<String>();
						name_information.add(username);
						name_information.add(line);
						information_list.addLast(name_information);
						check_thread = 1;
					}
				}	
			} 
			catch (Exception e) {
				JOptionPane.showMessageDialog(GUI.frame, "Can not do the corresponding action in the server", "Server Error", JOptionPane.ERROR_MESSAGE);
			}
			finally {	
				try {	
					in.close();
					out.close();
					s.close();
				} 
				catch (IOException e) {
					JOptionPane.showMessageDialog(GUI.frame, "Can not close the server", "Server Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
	}
 
	public void update_name(String name) {
		for (int i = 0; i < store_thread.size(); i = i + 1) {
			// from the online users to get the client name and check if this name is itself
			String client_name =  online_users.get(i);
			if (name != client_name) {
				// send the information to pointed thread
				ServerThread thread = store_thread.get(i);
				thread.out.println("kicks" + name);
			}
		}
		for (int i = 0, len = online_users.size(); i < len; i = i + 1) {
			if (online_users.get(i) == name) {
				online_users.remove(i);
				len = len - 1;
			    i = i - 1;
			}
		}
	}
	
	
	
}
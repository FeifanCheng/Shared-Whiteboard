/**
 * 
 */
package Client;


import java.util.List;
import javax.swing.JList;
import javax.swing.JTextArea;



/**
 * @author Feifan Cheng
 * Student id: 1164589
 * this class is to achieve the remove, add name in all the client GUI and clear the white board
 */
public class Update {

	
	
	
	public void remove(List<String> name_list, String[] online_users, JList<String> list_GUI, String usr_name) {
		/*
		 * remove the corresponding users from server and update in Client GUI
		 */
		
		// remove the user's name from users list
		name_list.remove(usr_name);
		int size = name_list.size();
		// transfer the name_list to String [] type
       	online_users = name_list.toArray(new String[size]);
       	// update in all Client GUI
   		list_GUI.setListData(online_users);
	}
	
	
	public void add_name(String user_name, List<String> name_list, String information, String[] online_users, JList<String> list_GUI) {
		/*
		 * add the client users in the server and update in Client GUI
		 */

		// use a String [] to store the name information from Server
       	String[] information_server = information.substring(1).split(",");

       	for(int i = 0; i < information_server.length; i++) {
       		// add all the name of online users
       		name_list.add(information_server[i]);    		
       	}
       	int size = name_list.size();
       	// transfer the name_list to String [] type
       	online_users = name_list.toArray(new String[size]);
       	// update in all Client GUI
   		list_GUI.setListData(online_users); 
	}
	
	public void chat(String information, JTextArea chat_window) {
		/*
		 * the chat function to get the chat information from server and update in chat window
		 */
		String message[] = information.substring(1).split("~");
    	chat_window.setText(message[0]);
    	for(int i = 1; i < message.length; i = i + 1) {
    		chat_window.append(message[i] + "\n");	
    	}
	}
	
	public void clear(int flag, Graphics_Text[] store_graph_text) {
   		/*
   		 * clear the white board function
   		 */
		
		// check all the graphics and text in white board
   		if (Action.flag > flag) {
   			flag = Action.flag;
   		}
   		// clear all the graphics and text in white board
		for(int i = 0 ; i <= flag ; i = i + 1) {
			if (store_graph_text[i] == null) {
				break;
			}
			else {
				store_graph_text[i].clear_all();
			}
		}
	}
	
}








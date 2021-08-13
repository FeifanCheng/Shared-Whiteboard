/**
 * 
 */
package Server;

import java.util.LinkedList;
import java.util.List;

import Server.Server.ServerThread;

/**
 * @author Feifan Cheng
 * Student ID: 1164589
 * listen the information in the thread queue and send information to the other clients except itself.
 */


public class Listen_information {

	
	public LinkedList<LinkedList> listen(LinkedList<LinkedList> information_list, List<String> online_users,List<ServerThread> store_thread) {
		/*
		 * store the information of the name and message which store the information client's operation
		 */
		LinkedList information_mess = (LinkedList) information_list.getFirst();
		String client = (String) information_mess.getFirst();
		String information = (String) information_mess.getLast();
		//iterate all user threads and send all the information to all the threads except itself
		for (int i = 0; i < store_thread.size(); i = i + 1) {
			// from the online users to get the client name and check if this name is itself
			String client_name =  online_users.get(i);
			if (client != client_name) {
				// send the information to pointed thread
				ServerThread thread = store_thread.get(i);
				thread.out.println(client + ":" + information);
			}
		}
		// remove this thread information and check another
		information_list.removeFirst();
		return information_list;
		
	}
	
	public int check(LinkedList<LinkedList> information_list, int check_thread){
		/*
		 * check the thread
		 */
		if (information_list.size() > 0) {
			check_thread = 1;
		}	
		else {
			check_thread = 0;
		}
		return check_thread;
	}
}

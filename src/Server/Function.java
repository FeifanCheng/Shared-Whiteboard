/**
 * 
 */
package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import Server.Server.ServerThread;

/**
 * @author Feifan Cheng
 * Student ID: 1164589
 * this class is to achieve kick out, close, manager close and send the online name list in the server
 */

public class Function {

	
	public ServerThread kickout(String line, List<String> online_users, List<ServerThread> store_thread) {
		/*
		 * kick out the user online and return his thread
		 */
		ServerThread thread = store_thread.get(online_users.indexOf(line.substring(1)));		
		return thread;
	}
	
	
	public void close(List<String> online_users,  String username, int check_thread, String[] onlineusers, AtomicInteger num_client) {
		/*
		 * close the client and record the client number
		 */
		check_thread = 1;
		num_client.decrementAndGet();
		online_users.remove(username);
		onlineusers = online_users.toArray(new String[online_users.size()]);	
	}
	
	public void manager_close(int disconnect, ServerSocket server) throws IOException {
		/*
		 * manager close the client and all the system will be closed
		 */
		disconnect = 1;
		server.close();
		System.exit(0);
	}
	
	
	public String name_send(List<String> online_users,  String[] onlineusers, String username, PrintWriter out) {
		/*
		 * store the online name list as a string
		 */
		String namelist = "$";
		online_users.add(username);
		onlineusers = online_users.toArray(new String[online_users.size()]);		
		for(int i = 0; i <online_users.size(); i = i + 1){	
			namelist += onlineusers[i] + ",";	
		} 		
		out.println(namelist);
		return namelist;
	}
	
}

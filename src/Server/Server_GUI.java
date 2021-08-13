/**
 * 
 */
package Server;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Color;

/**
 * @author Feifan Cheng
 * Student ID: 1164589
 * The Server's GUI of white board
 */

public class Server_GUI {

	public JFrame frame = new JFrame();
	public JButton close_button  = new JButton("Press to close this server");
	public JTextField port_num = new JTextField();
	public JTextField client_num = new JTextField();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server_GUI window = new Server_GUI();
					window.frame.setVisible(true);
				} 
				catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Exception error in Server GUI", "GUI Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	public Server_GUI() {
		initialize();
	}


	public void initialize() {
			
		frame.setBounds(100, 100, 300, 250);
		frame.getContentPane().setLayout(null);
		
		
		JLabel Show = new JLabel("Shared White Board Server");
		Show.setFont(new Font("Hei", Font.BOLD, 19));
		Show.setBackground(new Color(165, 42, 42));
		Show.setBounds(26, 17, 250, 30);
		frame.getContentPane().add(Show);
		
		
		// port number illustrate
		port_num.setBackground(new Color(119, 136, 153));
		port_num.setFont(new Font("Lantinghei TC", Font.BOLD, 16));
		port_num.setEditable(false);
		port_num.setBounds(145, 60, 116, 30);
		frame.getContentPane().add(port_num);
		
		JLabel num_port = new JLabel("Port number : ");
		num_port.setForeground(new Color(210, 105, 30));
		num_port.setFont(new Font("Hei", Font.BOLD, 16));
		num_port.setBackground(new Color(128, 0, 0));
		num_port.setBounds(26, 60, 250, 30);
		frame.getContentPane().add(num_port);
		
		
		// client number illustrate
		client_num.setEditable(false);
		client_num.setBackground(new Color(169, 169, 169));
		client_num.setFont(new Font("Kohinoor Telugu", Font.BOLD, 20));
		client_num.setBounds(215, 110, 46, 30);
		client_num.setText("  0");
		frame.getContentPane().add(client_num);
		
		JLabel num_client = new JLabel("Online client number : ");
		num_client.setForeground(new Color(165, 42, 42));
		num_client.setFont(new Font("Hei", Font.BOLD, 16));
		num_client.setBackground(new Color(165, 42, 42));
		num_client.setBounds(26, 110, 250, 30);
		frame.getContentPane().add(num_client);
		
		
		// the button to close the server
		close_button.setFont(new Font("Kohinoor Gujarati", Font.BOLD, 18));
		close_button.setForeground(new Color(255, 0, 0));

		close_button.setBounds(26, 152, 250, 50);
		close_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frame.getContentPane().add(close_button);	
	}
}







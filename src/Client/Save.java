/**
 * 
 */
package Client;



import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.nio.charset.StandardCharsets;

/**
 * @author Feifan Cheng
 * Student ID: 1164589
 * this class is to achieve saving the white board data and as a picture
 * open the existing white board data file and upload it in the white board
 */
public class Save {
	
	static Initial_shape shape = new Initial_shape();
	
	
	public void upload(String shapes_text,Graphics g,JPanel panel, int flag, Graphics_Text[] store_graph_text) {
		/*
		 * illustrate the exiting white board in white board
		 */
        String st_info[] = shapes_text.substring(1).split(";");
        JOptionPane.showMessageDialog(panel, "Click to see the existing white board", "Open successfully", JOptionPane.INFORMATION_MESSAGE);
        for(int i = 0; i < st_info.length; i = i + 1){	
        	String info = st_info[i];
        	if (info.substring(0,1).equals("#")){
        		info = info.substring(1);
        	}	
        	// show the text in the white board
            if(info.substring(0,1).equals("T")){ 	
            	shape.text(info, flag, g, store_graph_text);     	
            }
            // show the shapes in the white board
            else {	
            	shape.draw_shape(g, info, flag, store_graph_text);     
                g.setColor(g.getColor());
            }  
        }
	}
	
	
	
	public void save(JPanel panel){
		/*
		 * save a string in a txt file in pointed path
		 * cite from "https://stackoverflow.com/questions/19081890/saving-string-with-jfilechooser"
		 */	
		
		if (CreateWhiteBoard.shapes_text == "#") {
			JOptionPane.showMessageDialog(panel, "Please draw someting in the white board", "Save Error", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			try {
				JFileChooser select_to_save = new JFileChooser();	
				select_to_save.setDialogTitle("please choose one path to save this white board");
				select_to_save.showSaveDialog(null);
				if (select_to_save != null) {
					FileWriter fw = new FileWriter(select_to_save.getSelectedFile().getAbsolutePath() + ".txt");
					fw.write(CreateWhiteBoard.shapes_text);
					JOptionPane.showMessageDialog(panel, "Save Successful" , "Save Successful", JOptionPane.INFORMATION_MESSAGE);
					fw.close();
				}
			} 
			catch (Exception e1) {
				JOptionPane.showMessageDialog(panel, "Can not save the white board", "Save Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
		
	
	public void Save_picture(JPanel panel, int flag, Graphics_Text[] store_graph_text) {
		/*
		 * save the white board as a picture in pointed path
		 * cite from "https://www.tutorialspoint.com/java_dip/java_buffered_image.htm"
		 * cite from "https://docs.oracle.com/javase/tutorial/2d/images/saveimage.html"
		 */

		try {
			JFileChooser select_to_save = new JFileChooser();
			select_to_save.showSaveDialog(null);
			select_to_save.setDialogTitle("please choose one path to save this white board as a picture");
			if (select_to_save != null) {
				BufferedImage bi = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
				bi.getGraphics().fillRect(0, 0, 800, 800);
				Graphics2D g = bi.createGraphics();
			    panel.paint(g);
			    g.dispose();
				if (CreateWhiteBoard.flag > flag) {
					flag = CreateWhiteBoard.flag;
				}
				for (int i = 0; i < flag; i = i + 1) {
					if (store_graph_text[i] == null) {
						break;
					} 
					else {
						store_graph_text[i].text_draw(bi.getGraphics());	
					}
				}	
				ImageIO.write(bi, "png", new File(select_to_save.getSelectedFile().getAbsolutePath() + ".png"));
				JOptionPane.showMessageDialog(panel, "The white board is saved as a picture", "Picture Saved", JOptionPane.INFORMATION_MESSAGE);
			}
		} 
		catch (Exception e1) {
			JOptionPane.showMessageDialog(panel, "Can not save the white board as a picture", "Save Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	public static String open(JPanel panel) {
		/*
		 * open a txt file from pointed path
		 * cite from "https://zetcode.com/java/readtext/"
		 * cite from "https://docs.oracle.com/javase/8/javafx/api/javafx/stage/FileChooser.html"
		 */
		String shapes_text = "";
		JFileChooser select_to_save = new JFileChooser();
		select_to_save.setDialogTitle("please choose your previous white board");
		select_to_save.showOpenDialog(null);   
		if (select_to_save != null) {
			try {
			    File file_name =new File(select_to_save.getSelectedFile().getAbsolutePath());
				InputStreamReader isr = new InputStreamReader(new FileInputStream(file_name), StandardCharsets.UTF_8);
				BufferedReader br = new BufferedReader(isr);
				String line;
				while ((line = br.readLine()) != null) {
					shapes_text = line;
				}
				isr.close();	
			} 
			catch (Exception e1) {
				JOptionPane.showMessageDialog(panel, "Can not read this white board file!" + "\n" + "Please check the right format", "Wrong File", JOptionPane.ERROR_MESSAGE);
			}
		}
		return shapes_text;
	}
		
	
}

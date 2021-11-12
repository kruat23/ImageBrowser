package application;

import javax.swing.JOptionPane;

public class Application {

    public static Login form;

	public static void main(String[] args) {
    	
    	try { 
            form = new Login();  
            form.setSize(300, 100); 
            form.setVisible(true);  
        } catch(Exception e) { 
            JOptionPane.showMessageDialog(null, e.getMessage());  
        } 


    } 
}

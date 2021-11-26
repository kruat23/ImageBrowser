package application;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.shiro.subject.Subject;

public class Browser implements ActionListener {
	
	JFrame f = new JFrame("Képnézegetõ");    
	JButton change;
	JButton change_back;
	
	Browser(Subject currentUser) {		
            
        JPanel p = new JPanel() {
        	
			private static final long serialVersionUID = 1L;

			@Override
        	public void paint(Graphics g) {
        		try {
					g.drawImage(ImageIO.read(new File("images/1.png")), 0, 0, this);
					g.drawImage(ImageIO.read(new File("images/2.png")), 0, 300, this);
					g.drawImage(ImageIO.read(new File("images/3.png")), 0, 600, this);
					if (currentUser.isPermitted("all")) {
						g.drawImage(ImageIO.read(new File("images/1.jpg")), 600, 0, this);
						g.drawImage(ImageIO.read(new File("images/2.jpg")), 600, 300, this);
						g.drawImage(ImageIO.read(new File("images/3.jpg")), 600, 600, this);
					} 

				} catch (IOException e) {					
					e.printStackTrace();
				}
        	}			
        };
        
        if (currentUser.hasRole("admin_role")) {
        	change = new JButton("Jogosultság megadása");
        	change_back = new JButton("Jogosultság visszavonása");
        	change.addActionListener(this);
        	change_back.addActionListener(this);
        	p.add(change);
        	p.add(change_back);
        }
        
        f.add(p);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(1200, 1000));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DBRealm realm = new DBRealm();
		
		try {
			String response;
			if (e.getSource() == change) {
				response = realm.addPermission();
			} else {
				response = realm.removePermission();
			}		
			
			JOptionPane.showMessageDialog(f, response);
			System.out.println(response);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}	
	}
}

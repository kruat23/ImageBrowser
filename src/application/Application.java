package application;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Application {

    public static void main(String[] args) {
        JFrame f = new JFrame("Képnézegetõ");        
        JPanel p = new JPanel() {
        	
			private static final long serialVersionUID = 1L;

			@Override
        	public void paint(Graphics g) {
        		try {
					g.drawImage(ImageIO.read(new File("images/1.png")), 0, 0, this);
					g.drawImage(ImageIO.read(new File("images/2.png")), 0, 300, this);
					g.drawImage(ImageIO.read(new File("images/3.png")), 0, 600, this);
					g.drawImage(ImageIO.read(new File("images/1.jpg")), 600, 0, this);
					g.drawImage(ImageIO.read(new File("images/2.jpg")), 600, 300, this);
					g.drawImage(ImageIO.read(new File("images/3.jpg")), 600, 600, this);
				} catch (IOException e) {					
					e.printStackTrace();
				}
        	}
        };
        f.add(p);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(1200, 1000));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }  

}

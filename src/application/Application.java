package application;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Application {

    public static void main(String[] args) {
        JFrame f = new JFrame("Képnézegetõ");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(1200, 800));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }  

}

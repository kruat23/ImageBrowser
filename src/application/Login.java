package application;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm.SaltStyle;
import org.apache.shiro.subject.Subject;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

public class Login extends JFrame implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel userLabel, passLabel;
	JTextField user_textField, pass_textField;
	JButton submit;
	JPanel panel;
	
	PasswordService pwdService = new DefaultPasswordService();
	PasswordMatcher pwdMatcher = new PasswordMatcher();
	SQLiteConfig config = new SQLiteConfig();	
	DBRealm realm = new DBRealm();
	SQLiteDataSource ds = new SQLiteDataSource(config);
	SecurityManager securityManager = new DefaultSecurityManager(realm);	
	
	Login() {
		pwdMatcher.setPasswordService(pwdService);
		config.enforceForeignKeys(true);
		realm.setDataSource(ds);
		realm.setCredentialsMatcher(pwdMatcher);
		realm.setSaltStyle(SaltStyle.COLUMN);
		ds.setUrl("jdbc:sqlite:users_db.db");
		SecurityUtils.setSecurityManager(securityManager);
		
		userLabel = new JLabel();  
		userLabel.setText("Felhaszn�l�n�v"); 
		user_textField = new JTextField(15);
		
		passLabel = new JLabel(); 
		passLabel.setText("Jelsz�");
		pass_textField = new JPasswordField(15);
		
		submit = new JButton("Bel�p�s");
		
		panel = new JPanel(new GridLayout(3, 1));  
		panel.add(userLabel);    
		panel.add(user_textField);     
		panel.add(passLabel);     
		panel.add(pass_textField);   
		panel.add(submit);		
		add(panel, BorderLayout.CENTER);
		
		submit.addActionListener(this);
		setTitle("Bejelentkez�s"); 
	}  

	@Override
	public void actionPerformed(ActionEvent e) {
		
		JFrame frame = new JFrame();  
		
		String userValue = user_textField.getText();
		String passValue = pass_textField.getText();
		
		Subject currentUser = SecurityUtils.getSubject();
		
		if (! currentUser.isAuthenticated()) {
		    UsernamePasswordToken token = new UsernamePasswordToken(userValue, passValue);
		    token.setRememberMe(false);
		    
		    try {
		        currentUser.login(token);
		        JOptionPane.showMessageDialog(frame, "A felhaszn�l� [" + currentUser.getPrincipal() + "] sikeresen bejelentkezett.");		        
		        new Browser(currentUser);
		        Application.form.setVisible(false);
		    } catch (UnknownAccountException uae) {
		    	JOptionPane.showMessageDialog(frame, "Ezzel a n�vvel [" + token.getPrincipal() + "] nincs regisztr�lt felhaszn�l�.", "Felhaszn�l� nem tal�lhat�", JOptionPane.ERROR_MESSAGE);		        
		    } catch (IncorrectCredentialsException ice) {
		    	JOptionPane.showMessageDialog(frame, "A felhaszn�l�n�vhez [" + token.getPrincipal() + "] megadott jelsz� helytelen.", "Hib�s jelsz�", JOptionPane.ERROR_MESSAGE);
		    } catch (LockedAccountException lae) {
		    	JOptionPane.showMessageDialog(frame, "A felhaszn�l�n�vhez [" + token.getPrincipal() + "] tartoz� fi�k z�rolt.", "Z�rolt fi�k", JOptionPane.ERROR_MESSAGE);
		    } catch (AuthenticationException ae) {
		    	JOptionPane.showMessageDialog(frame, "Ismeretlen hiba.", "Hiteles�t�si hiba", JOptionPane.ERROR_MESSAGE);	    	
		    }
		}		
	}
}

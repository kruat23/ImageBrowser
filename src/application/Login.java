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
		userLabel.setText("Felhasználónév"); 
		user_textField = new JTextField(15);
		
		passLabel = new JLabel(); 
		passLabel.setText("Jelszó");
		pass_textField = new JPasswordField(15);
		
		submit = new JButton("Belépés");
		
		panel = new JPanel(new GridLayout(3, 1));  
		panel.add(userLabel);    
		panel.add(user_textField);     
		panel.add(passLabel);     
		panel.add(pass_textField);   
		panel.add(submit);		
		add(panel, BorderLayout.CENTER);
		
		submit.addActionListener(this);
		setTitle("Bejelentkezés"); 
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
		        JOptionPane.showMessageDialog(frame, "A felhasználó [" + currentUser.getPrincipal() + "] sikeresen bejelentkezett.");		        
		        new Browser(currentUser);
		        Application.form.setVisible(false);
		    } catch (UnknownAccountException uae) {
		    	JOptionPane.showMessageDialog(frame, "Ezzel a névvel [" + token.getPrincipal() + "] nincs regisztrált felhasználó.", "Felhasználó nem található", JOptionPane.ERROR_MESSAGE);		        
		    } catch (IncorrectCredentialsException ice) {
		    	JOptionPane.showMessageDialog(frame, "A felhasználónévhez [" + token.getPrincipal() + "] megadott jelszó helytelen.", "Hibás jelszó", JOptionPane.ERROR_MESSAGE);
		    } catch (LockedAccountException lae) {
		    	JOptionPane.showMessageDialog(frame, "A felhasználónévhez [" + token.getPrincipal() + "] tartozó fiók zárolt.", "Zárolt fiók", JOptionPane.ERROR_MESSAGE);
		    } catch (AuthenticationException ae) {
		    	JOptionPane.showMessageDialog(frame, "Ismeretlen hiba.", "Hitelesítési hiba", JOptionPane.ERROR_MESSAGE);	    	
		    }
		}		
	}
}

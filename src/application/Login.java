package application;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
		userLabel.setText("Username"); 
		user_textField = new JTextField(15);
		
		passLabel = new JLabel(); 
		passLabel.setText("Password");
		pass_textField = new JPasswordField(15);
		
		submit = new JButton("Submit");
		
		panel = new JPanel(new GridLayout(3, 1));  
		panel.add(userLabel);    
		panel.add(user_textField);     
		panel.add(passLabel);     
		panel.add(pass_textField);   
		panel.add(submit); 		
		add(panel, BorderLayout.CENTER);
		
		submit.addActionListener(this);
		setTitle("Login"); 
	}  

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String userValue = user_textField.getText();
		String passValue = pass_textField.getText();
		
		Subject currentUser = SecurityUtils.getSubject();
		
		if (! currentUser.isAuthenticated()) {
		    UsernamePasswordToken token = new UsernamePasswordToken(userValue, passValue);
		    token.setRememberMe(false);
		    
		    try {
		        currentUser.login(token);
		        System.out.println("User [" + currentUser.getPrincipal() + "] logged in successfully.");
		        new Browser();
		        Application.form.setVisible(false);
		    } catch (UnknownAccountException uae) {
		        System.out.println("There is no user with username of " + token.getPrincipal());
		    } catch (IncorrectCredentialsException ice) {
		        System.out.println("Password for account " + token.getPrincipal() + " was incorrect!");
		    } catch (LockedAccountException lae) {
		        System.out.println("The account for username " + token.getPrincipal() + " is locked.  " +
		                "Please contact your administrator to unlock it.");
		    } catch (AuthenticationException ae) {
		    	System.out.println("Unexpected error.");
		    }
		}		
	}

}

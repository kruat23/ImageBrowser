package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.jdbc.JdbcRealm;

public class DBRealm extends JdbcRealm {

    public DBRealm() {
        super();
        super.setRolePermissionResolver(new RolePermissionResolver() {

            @Override
            public Collection<Permission> resolvePermissionsInRole(String roleString) {
                Collection<Permission> perms = Collections.emptySet();
                try {
                    Set<String> permss = getPermissions(dataSource.getConnection(), roleString);
                    perms = new LinkedHashSet<Permission>(permss.size());
                    for (String perm : permss) {
                        perms.add(new WildcardPermission(perm));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return perms;
            }
        });

    }
    
    private Connection connect() {
        String url = "jdbc:sqlite:users_db.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    protected Set<String> getPermissions(Connection conn, String roleString) throws SQLException {
        List<String> roleNames = new ArrayList<String>();
        roleNames.add(roleString);
        return super.getPermissions(conn, "", roleNames);
    }
    
    protected String addPermission() throws SQLException {
    	String sql = "UPDATE roles_permissions SET permission = ? " + "WHERE role_name = ?";
    	
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "all");
            pstmt.setString(2, "user_role");
            pstmt.executeUpdate();
            return "Jogosultság megadva."; 
        } catch (SQLException e) {
            return e.getMessage();
        }		   	
    }
    
    protected String removePermission() throws SQLException {
    	String sql = "UPDATE roles_permissions SET permission = ? " + "WHERE role_name = ?";
    	
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "only_png");
            pstmt.setString(2, "user_role");
            pstmt.executeUpdate();
            return "Jogosultság megvonva."; 
        } catch (SQLException e) {
            return e.getMessage();
        }		   	
    }
}

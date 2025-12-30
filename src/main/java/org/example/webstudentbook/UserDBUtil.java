package org.example.webstudentbook;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserDBUtil {
    private DataSource dataSource;

    public UserDBUtil(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
        try {
            if (myRs != null) myRs.close();
            if (myStmt != null) myStmt.close();
            if (myConn != null) myConn.close();
        } catch (Exception e) {
            System.err.println("Erreur lors de la fermeture des ressources JDBC: " + e.getMessage());
        }
    }

    public User authenticate(String username, String password) {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        User user = null;

        try {
            myConn = dataSource.getConnection();
            String sql = "SELECT username, role, password FROM user WHERE username = ? AND password = ?";
            myStmt = myConn.prepareStatement(sql);

            myStmt.setString(1, username);
            myStmt.setString(2, password);

            myRs = myStmt.executeQuery();

            if (myRs.next()) {
                String role = myRs.getString("role");
                user = new User(username, password, role);
            }
            return user;

        } catch (Exception e) {
            System.err.println("Erreur d'authentification: " + e.getMessage());
            return null;
        }
    }
}
package org.example.webstudentbook;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {

    private DataSource dataSource;

    // Méthode pour obtenir le DataSource (Pool de Connexions) via JNDI
    private DataSource getDataSource() throws NamingException {
        String jndi = "java:comp/env/jdbc/studentdb";
        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup(jndi);

        return dataSource;
    }

    // Initialisation du servlet : récupérer le DataSource
    @Override
    public void init() throws ServletException {
        try {
            dataSource = getDataSource();
        } catch (NamingException e) {
            throw new RuntimeException("Erreur de configuration JNDI : " + e.getMessage(), e);
        }
    }

    // Méthode pour tester la connexion et afficher les emails
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Étape 1: Configurer la réponse
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");

        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        try {
            // Étape 2: Obtenir une connexion du pool
            myConn = dataSource.getConnection();

            // Étape 3: Créer et exécuter la requête SQL
            String sql = "select email from student";
            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery(sql);

            // Étape 4: Traiter le résultat et l'afficher
            while (myRs.next()) {
                String email = myRs.getString("email");
                out.println(email);
            }

        } catch (Exception exc) {
            System.out.println("Erreur de connexion/requête à la base de données : " + exc.getMessage());
            out.println("TEST ÉCHOUÉ : Vérifiez la console Tomcat pour les détails. Message: " + exc.getMessage());
        } finally {
            try {
                if (myRs != null) myRs.close();
                if (myStmt != null) myStmt.close();
                if (myConn != null) myConn.close();
            } catch (Exception e) {
                System.out.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
            }
        }
    }
}
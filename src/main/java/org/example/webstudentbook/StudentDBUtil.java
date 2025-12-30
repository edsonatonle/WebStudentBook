package org.example.webstudentbook;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDBUtil {

    private DataSource dataSource;

    public StudentDBUtil(DataSource theDataSource) {
        dataSource = theDataSource;
    }

    // Méthode utilitaire pour fermer les ressources JDBC
    private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
        try {
            if (myRs != null) myRs.close();
            if (myStmt != null) myStmt.close();
            // myConn.close() retourne la connexion au pool, ne la ferme pas vraiment
            if (myConn != null) myConn.close();
        } catch (Exception e) {
            System.err.println("Erreur lors de la fermeture des ressources JDBC: " + e.getMessage());
        }
    }

    // Méthode pour récupérer la liste des étudiants
    public List<Student> getStudents() throws Exception {
        List<Student> students = new ArrayList<>();
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        try {
            // 1. Obtenir une connexion du pool
            myConn = dataSource.getConnection();

            // 2. Créer la requête SQL
            String sql = "select * from student order by last_name";
            myStmt = myConn.createStatement();

            // 3. Exécuter la requête
            myRs = myStmt.executeQuery(sql);

            // 4. Parcourir le ResultSet
            while (myRs.next()) {
                // Lire les données de la ligne
                int id = myRs.getInt("id");
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");

                // Créer un objet Student
                Student tempStudent = new Student(id, firstName, lastName, email);

                // Ajouter à la liste
                students.add(tempStudent);
            }
            return students;
        } finally {
            // 5. S'assurer que les ressources sont fermées (la connexion est retournée au pool)
            close(myConn, myStmt, myRs);
        }
    }

    // Méthode pour ajouter un étudiant à la base de données
    public void addStudent(Student student) {
        Connection myConn = null;
        PreparedStatement myStmt = null; // Utilisation de PreparedStatement pour la sécurité

        try {
            myConn = dataSource.getConnection();

            // Requête SQL sécurisée avec des place-holders (?)
            String sql = "INSERT INTO student (first_name, last_name, email) VALUES (?, ?, ?)";
            myStmt = myConn.prepareStatement(sql);

            // Définir les paramètres (mapping des ? aux valeurs)
            myStmt.setString(1, student.getFirstName());
            myStmt.setString(2, student.getLastName());
            myStmt.setString(3, student.getEmail());

            // Exécuter l'insertion
            myStmt.execute();

        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de l'étudiant : " + e.getMessage());
            e.printStackTrace();
        } finally {
            close(myConn, myStmt, null);
        }
    }

    public Student fetchStudent(int id) {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        Student student = null;

        try {
            myConn = dataSource.getConnection();
            String sql = "select * from student where id=?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, id); // Définir l'ID

            myRs = myStmt.executeQuery();

            if (myRs.next()) {
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");
                student = new Student(id, firstName, lastName, email);
            }
            return student;
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération de l'étudiant (ID: " + id + "): " + e.getMessage());
            return null;
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

    // Nouvelle méthode: Mettre à jour un étudiant
    public void updateStudent(Student student) {
        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            myConn = dataSource.getConnection();

            String sql = "UPDATE student SET first_name=?, last_name=?, email=? WHERE id=?";
            myStmt = myConn.prepareStatement(sql);

            // 1. Définir les valeurs des champs
            myStmt.setString(1, student.getFirstName());
            myStmt.setString(2, student.getLastName());
            myStmt.setString(3, student.getEmail());
            // 2. Définir l'ID (clause WHERE)
            myStmt.setInt(4, student.getId());

            myStmt.execute();

        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour de l'étudiant (ID: " + student.getId() + "): " + e.getMessage());
        } finally {
            close(myConn, myStmt, null);
        }
    }
    public void deleteStudent(int studentId) {
        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            myConn = dataSource.getConnection();

            String sql = "DELETE FROM student WHERE id=?";
            myStmt = myConn.prepareStatement(sql);

            myStmt.setInt(1, studentId);

            myStmt.execute();

        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression de l'étudiant (ID: " + studentId + "): " + e.getMessage());
        } finally {
            close(myConn, myStmt, null);
        }
    }
}
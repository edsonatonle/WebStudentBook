package org.example.webstudentbook;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.io.IOException;
import java.util.List;

@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {

    private StudentDBUtil studentDBUtil;
    private DataSource dataSource;

    // Méthode JNDI pour obtenir le DataSource
    private DataSource getDataSource() throws NamingException {
        String jndi = "java:comp/env/jdbc/studentdb";
        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup(jndi);
        return dataSource;
    }

    // Méthode init() pour initialiser le pool de connexions et le DAO
    @Override
    public void init() throws ServletException {
        super.init(); // Appel à la méthode init() parente

        try {
            dataSource = getDataSource();
            studentDBUtil = new StudentDBUtil(dataSource);

        } catch (NamingException e) {
            System.err.println("Erreur d'initialisation JNDI dans StudentControllerServlet.");
            e.printStackTrace();
            throw new ServletException("Impossible d'initialiser le pool de connexions", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Récupérer la commande de l'URL
            String command = request.getParameter("command");

            if (command == null) {
                command = "LIST"; // Action par défaut
            }

            switch (command) {
                case "LIST":
                case "DEFAULT":
                    listStudents(request, response);
                    break;
                case "DELETE":
                    deleteStudent(request, response);
                    break;
                default:
                    listStudents(request, response);
            }

        } catch (Exception e) {
            System.err.println("Erreur dans doGet du contrôleur :");
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 1. Récupérer la liste des étudiants via le DAO
        List<Student> students = studentDBUtil.getStudents();

        // 2. Placer la liste dans l'objet 'request' pour qu'elle soit accessible à la JSP
        request.setAttribute("STUDENT_LIST", students);

        // 3. Obtenir le RequestDispatcher pour la vue
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");

        // 4. Transférer (forward) la requête/réponse à la JSP
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String role = (String) session.getAttribute("role");

            if (session == null || !"instructor".equals(role)) {
                // Rediriger ou afficher une erreur si pas instructeur
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé. Seul l'instructeur peut ajouter des étudiants.");
                return;
            }

            // 1. Lire les paramètres du formulaire
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            // 2. Créer un nouvel objet Student
            Student student = new Student(firstName, lastName, email);

            // 3. Ajouter l'étudiant via le DAO
            studentDBUtil.addStudent(student);

            // 4. Retourner à la liste des étudiants (pour afficher le nouvel étudiant)
            listStudents(request, response);

        } catch (Exception e) {
            System.err.println("Erreur dans doPost (addStudent) :");
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession(false);
        String role = (String) session.getAttribute("role");

        if (session == null || !"instructor".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé. Seul l'instructeur peut supprimer des étudiants.");
            return;
        }
        // 1. Récupérer l'ID de l'URL
        int studentId = Integer.parseInt(request.getParameter("studentId"));

        // 2. Supprimer l'étudiant via le DAO
        studentDBUtil.deleteStudent(studentId);

        // 3. Revenir à la liste
        listStudents(request, response);
    }

}
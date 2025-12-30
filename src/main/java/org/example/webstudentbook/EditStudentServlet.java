package org.example.webstudentbook;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/EditStudentServlet")
public class EditStudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private StudentDBUtil studentDbUtil;
    private DataSource dataSource;
    private int tempId;

    private DataSource getDataSource() throws NamingException {
        String jndi = "java:comp/env/jdbc/studentdb";
        Context context = new InitialContext();
        return (DataSource) context.lookup(jndi);
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            this.dataSource = getDataSource();
            studentDbUtil = new StudentDBUtil(dataSource);
        } catch (NamingException e) {
            System.err.println("Erreur d'initialisation JNDI dans EditStudentServlet.");
            e.printStackTrace();
            throw new ServletException("Impossible d'initialiser le pool de connexions", e);
        }
    }

    // Affiche le formulaire pré-rempli
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Récupérer l'ID de l'URL
        String studentIdParam = request.getParameter("studentId");
        if (studentIdParam == null) {
            response.sendRedirect("StudentControllerServlet");
            return;
        }

        try {
            int id = Integer.parseInt(studentIdParam);
            this.tempId = id;

            // 2. Récupérer l'étudiant via le DAO
            Student student = studentDbUtil.fetchStudent(id);

            // 3. Placer l'étudiant dans l'objet request
            request.setAttribute("Student", student);

            // 4. Transférer vers le formulaire d'édition
            request.getRequestDispatcher("edit-student.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération de l'étudiant à éditer: " + e.getMessage());
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Récupérer les données du formulaire
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        // 2. Créer l'objet Student (en utilisant l'ID récupéré précédemment)
        Student student = new Student(this.tempId, firstName, lastName, email);

        try {
            // 3. Mettre à jour l'étudiant dans la DB via le DAO
            studentDbUtil.updateStudent(student);
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour de l'étudiant: " + e.getMessage());
        }

        // 4. Rediriger vers la liste pour afficher les changements
        response.sendRedirect("StudentControllerServlet");
    }
}
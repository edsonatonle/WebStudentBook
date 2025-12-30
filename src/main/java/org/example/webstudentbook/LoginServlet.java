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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private UserDBUtil userDBUtil;
    private DataSource dataSource;

    // Afficher le formulaire (doGet)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Chercher le cookie du dernier utilisateur pour pré-remplir
        String lastUsername = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("lastUsername".equals(cookie.getName())) {
                    lastUsername = cookie.getValue();
                    break;
                }
            }
        }
        request.setAttribute("lastUsername", lastUsername);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    private DataSource getDataSource() throws NamingException {
        String jndi = "java:comp/env/jdbc/studentdb";
        Context context = new InitialContext();
        return (DataSource) context.lookup(jndi);
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // 1. Initialiser le pool de connexions
            this.dataSource = getDataSource();

            // 2. INITIALISATION CRUCIALE : Instancier le DAO
            this.userDBUtil = new UserDBUtil(dataSource);

        } catch (NamingException e) {
            System.err.println("Erreur d'initialisation JNDI dans LoginServlet.");
            e.printStackTrace();
            throw new ServletException("Impossible d'initialiser le pool de connexions pour UserDBUtil", e);
        }
    }

    // Traiter la soumission (doPost)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userDBUtil.authenticate(username, password);

        if (user != null) {
            // 1. Succès : Créer la session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());

            // 2. Créer le cookie pour la prochaine fois
            Cookie userCookie = new Cookie("lastUsername", username);
            userCookie.setMaxAge(60 * 60 * 24 * 30); // 30 jours
            response.addCookie(userCookie);

            // 3. Rediriger vers la page principale
            response.sendRedirect("StudentControllerServlet");

        } else {
            // Échec : Afficher l'erreur et rester sur la page de login
            request.setAttribute("errorMessage", "Nom d'utilisateur ou mot de passe invalide.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
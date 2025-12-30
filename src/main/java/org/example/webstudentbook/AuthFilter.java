package org.example.webstudentbook;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Appliquer ce filtre à toutes les pages principales
@WebFilter({"/StudentControllerServlet", "/AddStudentServlet", "/EditStudentServlet", "/DeleteStudentServlet"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false); // Récupérer la session existante

        String loginURI = request.getContextPath() + "/LoginServlet";
        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        if (loggedIn) {
            // L'utilisateur est connecté, laisser passer
            chain.doFilter(request, response);
        } else {
            // L'utilisateur n'est pas connecté, rediriger vers le login
            response.sendRedirect(loginURI);
        }
    }
}
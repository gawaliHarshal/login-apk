package com.mvc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.mvc.dao.DbConnection;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/regForm")
public class Register extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String myname = req.getParameter("name");
        String myemail = req.getParameter("email");
        String mypass = req.getParameter("password");
        String mycity = req.getParameter("city");

        try {
            Connection con = DbConnection.getConnection();
            
            String insert_Query = "INSERT INTO register(name,email,password,city) VALUES (?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(insert_Query);

            ps.setString(1, myname);
            ps.setString(2, myemail);
            ps.setString(3, mypass);
            ps.setString(4, mycity);


            int count = ps.executeUpdate();
            
            if (count > 0) {
                out.println("<h3 style='color:green'> Registered Successfully ☑️ </h3>");
                RequestDispatcher rd = req.getRequestDispatcher("/login.html");
                rd.include(req, resp);
            } else {
                out.println("<h3 style='color:red'> User Not Registered ❌ </h3>");
                RequestDispatcher rd = req.getRequestDispatcher("/register.html");
                rd.include(req, resp);
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3 style='color:red'> Error: " + e.getMessage() + " ❌ </h3>");
            RequestDispatcher rd = req.getRequestDispatcher("/register.html");
            rd.include(req, resp);
        }
    }
}

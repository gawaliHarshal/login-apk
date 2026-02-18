package com.mvc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mvc.dao.DbConnection;
import com.mvc.model.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/loginForm")
public class Login extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		PrintWriter out = resp.getWriter();
		resp.setContentType("text/html");
		String myemail = req.getParameter("email");
		String mypass = req.getParameter("password");

		try {

			Connection con = DbConnection.getConnection();
			String select_query = "select * from register where email=? and password=?";
			PreparedStatement ps = con.prepareStatement(select_query);
			ps.setString(1, myemail);
			ps.setString(2, mypass);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setCity(rs.getString("city"));

				HttpSession session = req.getSession();
				session.setAttribute("session_user", user);

				RequestDispatcher rd = req.getRequestDispatcher("/profile.jsp");
				rd.forward(req, resp);
			} else {
				out.println("<h3 style='color:red'>Email And pasword not match</h3>");
				RequestDispatcher rd = req.getRequestDispatcher("/login.html");
				rd.include(req, resp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
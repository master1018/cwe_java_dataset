package com.bijay.onlinevotingsystem.controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.bijay.onlinevotingsystem.dao.VoterDao;
import com.bijay.onlinevotingsystem.dao.VoterDaoImpl;
@WebServlet("/vLoginController")
public class VoterLoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	VoterDao voterDao = new VoterDaoImpl();
	private String host;
	private String port;
	private String user;
	private String pass;
	public String recipient;
	public int otp;
	public int giveOtp() {
		return this.otp;
	}
	public void init() {
		ServletContext context = getServletContext();
		host = context.getInitParameter("host");
		port = context.getInitParameter("port");
		user = context.getInitParameter("user");
		pass = context.getInitParameter("pass");
	}
	public VoterLoginController() {
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.invalidate();
		RequestDispatcher rd = request.getRequestDispatcher("voterlogin.jsp");
		request.setAttribute("loggedOutMsg", "Log Out Successful !!");
		rd.include(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int min = 100000;
		int max = 999999;
		otp = 5432;
		Random r = new Random();
		otp = r.nextInt(max - min) + min;
		String userName = request.getParameter("uname");
		String password = request.getParameter("pass");
		String vemail = request.getParameter("vmail");
		String recipient = vemail;
		String subject = "otp verification";
		String content = "your otp is: " + otp;
		String resultMessage = "";
		if (voterDao.loginValidate(userName, password, vemail)) {
			HttpSession session = request.getSession();
			session.setAttribute("username", userName);
			try {
				EmailSend.sendEmail(host, port, user, pass, recipient, subject, content);
			} catch (MessagingException e) {
				e.printStackTrace();
				resultMessage = "There were an error: " + e.getMessage();
			} finally {
				RequestDispatcher rd = request.getRequestDispatcher("OTP.jsp");
				rd.include(request, response);
				out.println("<script type=\"text/javascript\">");
				out.println("alert('" + resultMessage + "');");
				out.println("</script>");
			}
		} else {
			RequestDispatcher rd = request.getRequestDispatcher("voterlogin.jsp");
			request.setAttribute("loginFailMsg", "Invalid Input ! Enter again !!");
			rd.include(request, response);
		}
	}
}

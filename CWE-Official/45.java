public class BankManagerLoginServlet extends HttpServlet {
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
try {
String username = request.getParameter("username");
String password = request.getParameter("password");
BankManager bankMgr = new BankManager();
boolean isAuthentic = bankMgr.authenticateUser(username, password);
if (isAuthentic) {
request.setAttribute("login", new String("Login Successful."));
getServletContext().getRequestDispatcher("/BankManagerServiceLoggedIn.jsp"). forward(request, response);
}
else {
throw new FailedLoginException("Failed Login for user " + username + " with password " + password);
}
} catch (FailedLoginException ex) {
request.setAttribute("error", new String("Login Error"));
request.setAttribute("message", ex.getMessage());
getServletContext().getRequestDispatcher("/ErrorPage.jsp").forward(request, response);
}
}

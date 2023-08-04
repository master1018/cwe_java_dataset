// 6. 在URL参数中触发XSS漏洞
String userInput = request.getParameter("name");
out.println("<a href="/profile?name=" + userInput + "">Visit Profile</a>");
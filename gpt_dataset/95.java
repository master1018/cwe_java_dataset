// 1. 基本的XSS漏洞示例
String userInput = request.getParameter("input");
out.println("<div>" + userInput + "</div>");
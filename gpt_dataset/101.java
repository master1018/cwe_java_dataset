// 7. 在CSS中触发XSS漏洞
String userInput = "" style="background:url(javascript:alert('XSS'))";
out.println("<div style="background-image:url('" + userInput + "')"></div>");
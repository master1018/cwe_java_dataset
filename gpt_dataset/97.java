// 3. 在HTML属性中触发XSS漏洞
String userInput = "" onclick="alert('XSS')"";
out.println("<input type="text" value="" + userInput + "">");
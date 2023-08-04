// 2. 使用JavaScript脚本进行XSS攻击
String userInput = "<script>alert('XSS');</script>";
out.println("<div>" + userInput + "</div>");
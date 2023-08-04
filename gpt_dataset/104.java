// 10. 在iframe中触发XSS漏洞
String userInput = "<iframe src="javascript:alert('XSS')"></iframe>";
out.println(userInput);
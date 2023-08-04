// 4. 使用image标签触发XSS漏洞
String userInput = "<img src="x" onerror="alert('XSS')">";
out.println(userInput);
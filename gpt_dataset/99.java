// 5. 使用嵌入的JavaScript标签触发XSS漏洞
String userInput = "<img src="x"><script>alert('XSS');</script>";
out.println(userInput);
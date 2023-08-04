// 8. 使用嵌入的SVG标签触发XSS漏洞
String userInput = "<svg><script>alert('XSS')</script></svg>";
out.println(userInput);
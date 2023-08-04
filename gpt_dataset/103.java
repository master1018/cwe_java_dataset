// 9. 利用注入HTML注释进行XSS攻击
String userInput = "--><script>alert('XSS')</script><!--";
out.println("<!--<div>" + userInput + "</div>-->");
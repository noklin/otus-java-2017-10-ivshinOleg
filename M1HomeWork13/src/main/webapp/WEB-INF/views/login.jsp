<%@ page language="java" pageEncoding="utf-8"%> 
<html>
<head><title>login page</title></head>
<body>
 	<form action="${pageContext.request.contextPath}/auth" method="POST">
		<input type="text" name="login" placeholder="enter login">
		<input type="password" name="password" placeholder="enter password">
		<button type="submit">sign in</button>
	</form>
</body>
</html>
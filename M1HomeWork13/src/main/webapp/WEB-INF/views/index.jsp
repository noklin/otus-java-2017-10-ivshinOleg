<html>
<head>
<title>login page</title>
</head>
<body>
	<div>
		<form action="${pageContext.request.contextPath}/cache" method="POST">
			<table border="1">
				<caption>Cache info</caption>
				<tr>
					<th>Max size</th>
					<th>Time to live</th>
					<th>Max idle</th>
					<th>Current size</th>
					<th>Hit count</th>
					<th>Miss count</th>
				</tr>
				<tr>
					<td><input name="maxSize" value="${maxSize}"></td>
					<td><input name="ttl" value="${ttl}"></td>
					<td><input name="maxIdle" value="${maxIdle}"></td>
					<td>${size}</td>
					<td>${hitCount}</td>
					<td>${missCount}</td>
				</tr>
				<tr>
					<td><button type="submit">save changes</button></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
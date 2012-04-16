<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>index</title>
<script type="text/javascript">
	function login() {
		if (!userCheck()) {
			return;
		}
		var form = document.user;
		form.submit();
	}

	function userCheck() {
		var id = document.getElementById('userId').value;
		if (id == '' || isNaN(id)) {
			alert('user id must be number');
			return false;
		}
		var name = document.getElementById('name').Value;
		if (name == '') {
			alert('pls input your name');
			return false;
		}
		return true;
	}
</script>
</head>
<body>
<form name='user' action="login.do" method="get">
<table align="center">
 <tr>
 	<td align="right">id:</td>
 	<td><input type='text' name='userId' id="userId"></td>
 </tr>
 <tr>
 	<td align="right">name:</td>
 	<td><input type='text' name='name' id="name"></td>
 </tr>
 <tr>
 	<td colspan='2' align="center"><input type='button' value='submit' onclick='login()'></td>
 </tr>
</table>
</form>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>index</title>
<style type="text/css">
.global_container {
	*position: relative;
	width: 100%;
	height: 500px;
	display: table;
}

.middle {
	*position: absolute;
	display: table-cell;
	vertical-align: middle;
	*top: 50%;
}

.inner {
	position: relative;
	*top: -50%;
}
</style>
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
		<div class="global_container">
			<div class="middle">
				<div class="inner">
					<table style="margin-left: auto; margin-right: auto;">
						<tr>
							<td align="right">ID:</td>
							<td><input type='text' name='userId' id="userId"></td>
						</tr>
						<tr>
							<td align="right">Name:</td>
							<td><input type='text' name='name' id="name"></td>
						</tr>
						<tr>
							<td colspan='2' align="center"><input type='button'
								value='submit' onclick='login()'></td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</form>
</body>
</html>
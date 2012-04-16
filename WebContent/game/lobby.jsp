<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.List"%>
<%@ page import="com.ghh.common.game.Game"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Game Lobby</title>
<%
	List<Game> games = (List<Game>)request.getAttribute("gamelist");
%>
</head>
<body>
<table>
	<% 
		for(Game game : games) {
	%>
	<tr>
		<td><a href="gobang?p=03&gameId=<%= game.getId()%>">Game #<%= game.getId()%></a></td>
	</tr>
	<%	} %>
</table>
</body>
</html>
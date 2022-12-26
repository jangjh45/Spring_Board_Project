<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="loginOutLink" value="${sessionScope.id==null ? '/login/login' : '/login/logout' }"/>
<c:set var="loginOut" value="${sessionScope.id==null ? 'Login' : 'Logout' }"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>fastcampus</title>
</head>
<body>
<div id="menu">
	<ul>
		<li id="logo">fastcampus</li>
		<li><a href="<c:url value='/'/>">Home</a></li>
		<li><a href="<c:url value='/board/list'/>">Board</a></li>
		<li><a href="<c:url value='${loginOutLink }'/>">${loginOut }</a></li>
		<li><a href="<c:url value='/register/add'/>">Sign in</a></li>
		<li><a href=""><i class="fas fa-search small"></i></a></li>
	</ul>
</div>
<div style="text-align:center">
	<h1>This is HOME</h1>
</div>

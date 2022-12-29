<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>fastcampus</title>
    <link rel="stylesheet" href="<c:url value='/css/menu.css'/>">
</head>
<body>
<div id="menu">
    <ul>
        <li id="logo">fastcampus</li>
        <li><a href="<c:url value='/'/>">Home</a></li>
        <li><a href="<c:url value='/board/list'/>">Board</a></li>
        <li><a href="<c:url value='/login/login'/>">login</a></li>
        <li><a href="<c:url value='/register/add'/>">Sign in</a></li>
        <li><a href=""><i class="fas fa-search small"></i></a></li>
    </ul>
</div>
<script>
    let msg = "${param.msg}"
    if(msg=="MODIFY_OK") alert("수정되었습니다.");
    if(msg=="WRITE_OK") alert("등록되었습니다.");
    if(msg=="DELETE_OK") alert("삭제되었습니다.");
    if(msg=="DELETE_ERROR") alert("삭제 실패");
</script>
<div style="text-align:center">
    <button type="button" id="writeBnt" onclick="location.href='<c:url value="/board/write"/>'">글쓰기</button>
    <table border="1">
        <tr>
            <th>번호</th>
            <th>제목</th>
            <th>이름</th>
            <th>등록일</th>
            <th>조회수</th>
        </tr>
        <c:forEach var="boardDto" items="${list}">
            <tr>
                <td>${boardDto.bno}</td>
                <td><a href="<c:url value='/board/read?bno=${boardDto.bno}&page=${page}&pageSize=${pageSize}'/>">${boardDto.title}</a></td>
                <td>${boardDto.writer}</td>
                <td>${boardDto.reg_date}</td>
                <td>${boardDto.view_cnt}</td>
            </tr>
        </c:forEach>
    </table>
    <br>
    <div>
        <c:if test="${ph.showPrev}">
            <a href="<c:url value='/board/list?page=${ph.beginPage-1}&pageSize=${ph.pageSize}'/>">&lt;</a>
        </c:if>
        <c:forEach var="i" begin="${ph.beginPage}" end="${ph.endPage}">
            <a href="<c:url value='/board/list?page=${i}&pageSize=${ph.pageSize}'/>">${i}</a>
        </c:forEach>
        <c:if test="${ph.showNext}">
            <a href="<c:url value='/board/list?page=${ph.endPage+1}&pageSize=${ph.pageSize}'/>">&gt;</a>
        </c:if>
    </div>
</div>
</body>
</html>

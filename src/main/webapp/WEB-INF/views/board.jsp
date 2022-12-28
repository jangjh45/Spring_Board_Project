<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>fastcampus</title>
  <link rel="stylesheet" href="<c:url value='/css/menu.css'/>">
  <script src="https://code.jquery.com/jquery-1.11.3.js"></script>
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
  let msg = "${msg}"
  if(msg=="WRITE_ERROR") alert("게시물 등록에 실패했습니다. 다시 시도해 주세요.");
</script>
</div><div style="text-align:center">
  <h2>게시물 ${mode=="new" ? "글쓰기" : "읽기"}</h2>
  <form action="" id="form">
    <input type="hidden" name="bno" value="${boardDto.bno}">
    <input type="text" name="title" value="${boardDto.title}" ${mode=="new" ? '' : 'readonly="readonly"'}>
    <textarea name="content" id="" cols="30" rows="10" ${mode=="new" ? '' : 'readonly="readonly"'}>${boardDto.content}</textarea>
    <button type="button" id="writeBtn" class="btn">글쓰기</button>
    <button type="button" id="modifyBtn" class="btn">수정</button>
    <button type="button" id="removeBtn" class="btn">삭제</button>
    <button type="button" id="listBtn" class="btn">목록</button>
  </form>
</div>
<script>
  $(document).ready(function (){ // main()
    $('#listBtn').on("click", function(){ // 목록 버튼 눌렀을때
      // BoardController의 /board/list 으로 작동됨
      location.href = "<c:url value='/board/list'/>?page=${page}&pageSize=${pageSize}";
    });

    $('#removeBtn').on("click", function(){ // 삭제 버튼 눌렀을때, POST로 처리해야함
      // 삭제하면 ok이면 계속진행 no이면 빠져나감
      if(!confirm("정말로 삭제하시겠습니까?")) return;
      // form 에 대한 참조
      // id가 form 인 객체를 가지고오고 변수에 담음
      let form = $('#form');
      // BoardController의 /board/remove 으로 작동됨
      form.attr("action", "<c:url value='/board/remove'/>?page=${page}&pageSize=${pageSize}");
      form.attr("method", "post"); //POST로 처리
      form.submit(); // 내용 전송
    });

    $('#writeBtn').on("click", function(){ // 글쓰고 등록버튼 눌렀을때, POST로 처리해야함
      // form 에 대한 참조
      // id가 form 인 객체를 가지고오고 변수에 담음
      let form = $('#form');
      // BoardController의 /board/remove 으로 작동됨
      form.attr("action", "<c:url value='/board/write'/>");
      form.attr("method", "post"); //POST로 처리
      form.submit(); // 내용 전송
    });

    $('#modifyBtn').on("click", function(){ // 글쓰고 등록버튼 눌렀을때, POST로 처리해야함
      // form 에 대한 참조
      // id가 form 인 객체를 가지고오고 변수에 담음
      let form = $('#form');
      let isReadOnly = $("input[name=title]").attr('readonly');

      // 1. 읽기 상태이면 수정상태로 변경
      // isReadOnly 가 읽기전용(readonly) 이면 읽기전용모드를 끈다.
      if(isReadOnly=='readonly') {
        $("input[name=title]").attr('readonly', false); // title
        $("textarea").attr('readonly', false); // content
        // 수정버튼을 등록버튼으로 바꿈
        $("#modifyBtn").html("등록");
        $("h2").html("게시물 수정");
        return;
      }
      // 2. 수정 상태이면, 수정된 내용을 서버로 전송
      // BoardController의 /board/modify 으로 작동됨
      form.attr("action", "<c:url value='/board/modify'/>?page=${page}&pageSize=${pageSize}");
      form.attr("method", "post"); //POST로 처리
      form.submit(); // 내용 전송
    });

  });
</script>
</body>
</html>

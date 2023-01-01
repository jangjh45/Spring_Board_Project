<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page session="true"%>
<c:set var="loginId" value="${sessionScope.id}"/>
<c:set var="loginOutLink" value="${loginId=='' ? '/login/login' : '/login/logout'}"/>
<c:set var="loginOut" value="${loginId=='' ? 'Login' : 'ID='+=loginId}"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Practice1</title>
<%--  <link rel="stylesheet" href="<c:url value='/css/menu.css'/>">--%>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
  <script src="https://code.jquery.com/jquery-1.11.3.js"></script>
  <style>
    * {
      box-sizing: border-box;
      margin: 0;
      padding: 0;
      font-family: "Noto Sans KR", sans-serif;
    }

    .container {
      width : 50%;
      margin : auto;
    }

    .writing-header {
      position: relative;
      margin: 20px 0 0 0;
      padding-bottom: 10px;
      border-bottom: 1px solid #323232;
    }

    input {
      width: 100%;
      height: 35px;
      margin: 5px 0px 10px 0px;
      border: 1px solid #e9e8e8;
      padding: 8px;
      background: #f8f8f8;
      outline-color: #e6e6e6;
    }

    textarea {
      width: 100%;
      background: #f8f8f8;
      margin: 5px 0px 10px 0px;
      border: 1px solid #e9e8e8;
      resize: none;
      padding: 8px;
      outline-color: #e6e6e6;
    }

    .frm {
      width:100%;
    }
    .btn {
      background-color: rgb(236, 236, 236); /* Blue background */
      border: none; /* Remove borders */
      color: black; /* White text */
      padding: 6px 12px; /* Some padding */
      font-size: 16px; /* Set a font size */
      cursor: pointer; /* Mouse pointer on hover */
      border-radius: 5px;
    }

    .btn:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>
<div id="menu">
  <ul>
    <li id="logo">Practice1</li>
    <li><a href="<c:url value='/'/>">Home</a></li>
    <li><a href="<c:url value='/board/list'/>">Board</a></li>
    <li><a href="<c:url value='${loginOutLink}'/>">${loginOut}</a></li>
    <li><a href="<c:url value='/register/add'/>">Sign in</a></li>
    <li><a href=""><i class="fa fa-search"></i></a></li>
  </ul>
</div>
<script>
  let msg = "${msg}";
  if(msg=="WRITE_ERROR") alert("게시물 등록에 실패하였습니다. 다시 시도해 주세요.");
  if(msg=="MODIFY_ERROR") alert("게시물 수정에 실패하였습니다. 다시 시도해 주세요.");
</script>
<div class="container">
  <h2 class="writing-header">게시판 ${mode=="new" ? "글쓰기" : "읽기"}</h2>
  <form id="form" class="frm" action="" method="post">
    <input type="hidden" name="bno" value="${boardDto.bno}">

    <input name="title" type="text" value="${boardDto.title}" placeholder="  제목을 입력해 주세요." ${mode=="new" ? "" : "readonly='readonly'"}><br>
    <textarea name="content" rows="20" placeholder=" 내용을 입력해 주세요." ${mode=="new" ? "" : "readonly='readonly'"}>${boardDto.content}</textarea><br>

    <c:if test="${mode eq 'new'}">
      <button type="button" id="writeBtn" class="btn btn-write"><i class="fa fa-pencil"></i> 등록</button>
    </c:if>
    <c:if test="${mode ne 'new'}">
      <button type="button" id="writeNewBtn" class="btn btn-write"><i class="fa fa-pencil"></i> 글쓰기</button>
    </c:if>
    <c:if test="${boardDto.writer eq loginId}">
      <button type="button" id="modifyBtn" class="btn btn-modify"><i class="fa fa-edit"></i> 수정</button>
      <button type="button" id="removeBtn" class="btn btn-remove"><i class="fa fa-trash"></i> 삭제</button>
    </c:if>
    <button type="button" id="listBtn" class="btn btn-list"><i class="fa fa-bars"></i> 목록</button>
  </form>
</div>
<div class="comment-box">
  <h3>댓글</h3>
  comment: <input type="text" name="comment"><br>
  <button id="sendBtn" type="button">SEND</button>
  <button id="modBtn" type="button">수정</button>

  <div id="commentList"></div>
  <div id="replyForm" style="display: none">
    <input type="text" name="replyComment">
    <button id="wrtRepByn" type="button">등록</button>
  </div>
</div>
<script>
  let bno = "${boardDto.bno}";
  let showList = function (bno){
    $.ajax({
      type:'GET', // 요청 메서드
      url:'/app1/comments?bno='+bno, // 요청 URI
      success:function (result){$("#commentList").html(toHtml(result));},
      error:function (){alert("error")} // 에러가 발생했을때, 호출될 함수
    });
  }

  $(document).ready(function(){
    showList(bno); // 게시글에 진입하면 댓글 목록이 나타남!!
    let formCheck = function(){
      let form = document.getElementById("form");
      if(form.title.value=="") {
        alert("제목을 입력해 주세요.");
        form.title.focus();
        return false;
      }

      if(form.content.value=="") {
        alert("내용을 입력해 주세요.");
        form.content.focus();
        return false;
      }
      return true;
    }
    // ----------게시글----------
    $("#writeNewBtn").on("click", function(){
      location.href="<c:url value='/board/write'/>";
    });

    $("#writeBtn").on("click", function(){
      let form = $("#form");
      form.attr("action", "<c:url value='/board/write'/>");
      form.attr("method", "post");

      if(formCheck())
        form.submit();
    });

    $("#modifyBtn").on("click", function(){
      let form = $("#form");
      let isReadonly = $("input[name=title]").attr('readonly');

      // 1. 읽기 상태이면, 수정 상태로 변경
      if(isReadonly=='readonly'){
        $(".writing-header").html("게시판 수정");
        $("input[name=title]").attr('readonly', false);
        $("textarea").attr('readonly', false);
        $("#modifyBtn").html("<i class='fa fa-pencil'></i> 등록");
        return;
      }

      // 2. 수정 상태이면, 수정된 내용을 서버로 전송
      form.attr("action", "<c:url value='/board/modify'/>?page=${page}&pageSize=${pageSize}");
      form.attr("method", "post");
      if(formCheck())
        form.submit();
    });

    $("#removeBtn").on("click", function(){
      if(!confirm("정말로 삭제하시겠습니까?")) return;

      let form = $("#form");
      form.attr("action", "<c:url value='/board/remove'/>?page=${page}&pageSize=${pageSize}");
      form.attr("method", "post");
      form.submit();
    });

    $("#listBtn").on("click", function(){
      location.href="<c:url value='/board/list'/>?page=${page}&pageSize=${pageSize}";
    });

    // ----------댓글----------
    // -----댓글 쓰기-----
    $("#sendBtn").click(function (){
      // 입력한 내용을 가지고와서 변수에 담음
      let comment = $("input[name=comment]").val();

      // 댓글을 공백으로 보냈을때 처리하는 코드
      if(comment.trim()==''){
        alert("댓글을 입력해주세요.");
        $("input[name=comment]").focus()
        return;
      }

      $.ajax({
        type: 'POST',
        url: '/app1/comments?bno='+bno,
        headers: {"content-type": "application/json"},
        // JSON으로 보내면 컨트롤러가 받아서 자바객체로 변환해서 처리(@RequestBody)
        data: JSON.stringify({bno:bno, comment:comment}),
        success: function (result){
          alert(result);
          showList(bno);
        },
        error: function (){alert("error")}
      });
      // 댓글 SEND 후 안에 있는 값을 비워야한다.
      $("input[name=comment]").val('')
    });

    // -----댓글 수정----- 댓글에 있는 수정버튼을 눌렀을 때!!!!!!
    // mod 버튼이 #commentList 안에 있기 때문에 #commentList 에 이벤트를 걸어야 한다.
    $("#commentList").on("click", ".modBtn", function (){
      let cno = $(this).parent().attr("data-cno");
      // 클릭된 수정버튼의 부모(li)에 있는 span만 가지고 오는것
      let comment = $("span.comment", $(this).parent()).text();

      // 1. comment의 내용을 input에 뿌려주기
      $("input[name=comment]").val(comment);
      // 2. cno전달하기
      $("#modBtn").attr("data-cno", cno);
    });

    // -----댓글 수정작성 후 수정버튼-----
    // 댓글을 수정하고 수정버튼 누르면 작동되는 코드!!!!!
    $("#modBtn").click(function (){
      // 버튼에 있는 cno 를 가지고 온다.
      let cno = $(this).attr("data-cno");
      // 입력한 내용을 가지고와서 변수에 담음
      let comment = $("input[name=comment]").val();

      // 댓글을 공백으로 보냈을때 처리하는 코드
      if(comment.trim()==''){
        alert("댓글을 입력해주세요.");
        $("input[name=comment]").focus()
        return;
      }

      $.ajax({
        type: 'PATCH',
        url: '/app1/comments/'+cno,
        headers: {"content-type": "application/json"},
        // JSON으로 보내면 컨트롤러가 받아서 자바객체로 변환해서 처리(@RequestBody)
        data: JSON.stringify({cno:cno, comment:comment}),
        success: function (result){
          alert(result);
          showList(bno);
        },
        error: function (){alert("error")}
      });
    });

    // -----댓글 삭제-----
    // del 버튼이 #commentList 안에 있기 때문에 #commentList 에 이벤트를 걸어야 한다.
    // $(".delBtn").click(function (){ // 이거는 잘못된 줄(예시)!
    $("#commentList").on("click", ".delBtn", function (){
      let cno = $(this).parent().attr("data-cno");
      let bno = $(this).parent().attr("data-bno");

      $.ajax({
        type:'DELETE',
        url:'/app1/comments/'+cno+'?bno='+bno,
        // 삭제한 후 목록을 새로 가지고 와야함(목록 갱신)
        success:function (result){
          alert(result);
          showList(bno);
        },
        error:function (){alert("error")}
      });
    });

    // -----대댓글----- 댓글에 있는 대댓글버튼을 눌렀을 때!!!!!!
    $("#commentList").on("click", ".replyBtn", function (){
      // 1. replyForm을 옮기고 (li) 부모의 태그 뒤에 붙인다.
      $("#replyForm").appendTo($(this).parent());
      // 2. 답글을 입력할 폼을 보여줌
      $("#replyForm").css("display", "block");
    });

    // -----대댓글 작성 후 SEND 버튼-----
    $("#wrtRepByn").click(function (){
      // 입력한 내용을 가지고와서 변수에 담음
      let comment = $("input[name=replyComment]").val();
      // 대댓글의 부모 (대댓글을 쓴 댓글의 번호) pcno 를 가지고옴
      // 강의 할때 원래 data-cno 이였는데 대댓글에 대댓글을 쓰면 다른 cno가 잡아버림
      // 해결 방법으로 댓글에도 대댓글에도 같은 숫자가 있는 pcno를 가지고 온다.
      let pcno = $("#replyForm").parent().attr("data-pcno");

      // 댓글을 공백으로 보냈을때 처리하는 코드
      if(comment.trim()==''){
        alert("댓글을 입력해주세요.");
        $("input[name=comment]").focus()
        return;
      }

      $.ajax({
        type: 'POST',
        url: '/app1/comments?bno='+bno,
        headers: {"content-type": "application/json"},
        // JSON으로 보내면 컨트롤러가 받아서 자바객체로 변환해서 처리(@RequestBody)
        data: JSON.stringify({pcno:pcno, bno:bno, comment:comment}),
        success: function (result){
          alert(result);
          showList(bno);
        },
        error: function (){alert("error")}
      });
      // 대댓글이 작성하고 다시 안보이게 해야한다.
      $("#replyForm").css("display", "none")
      // 대댓글 안에 있는 값을 비워야한다.
      $("input[name=replyComment]").val('')
      // 원래위치로 돌려야한다. (원래 body 아래에 있음)
      $("#replyForm").appendTo("body");
    });

  });

  // toSting 이랑 비슷?? ,댓글들이 들어오면 HTML로 바꿈
  // comments - 배열로 들어옴
  let toHtml = function (comments){
    let tmp = "<ul>";

    // 배열에서 댓글안 내용을 하나씩 가지고와서 tmp 에 계속 쌓음
    // 띄어쓰기 잘해야함 안하면 구분이 안되서 오류남!!!!!
    comments.forEach(function (comment){
      tmp += '<li data-cno='+comment.cno
      tmp += ' data-pcno='+comment.pcno
      tmp += ' data-bno='+comment.bno +'>'
      if(comment.cno!=comment.pcno)
        tmp += 'ㄴ'
      // spen 태그는 나중에 읽어오기 쉬움 (수정할때)
      tmp += ' commenter=<span class="commenter">' + comment.commenter + '</span>'
      tmp += ' comment=<span class="comment">' + comment.comment + '</span>'
      tmp += ' up_date=' + comment.up_date
      tmp += ' <button class="delBtn">삭제</button>' // 삭제버튼 누르면 댓글 삭제 요청
      tmp += ' <button class="modBtn">수정</button>' // 수정버튼 누르면 댓글 수정 모드 진입
      tmp += ' <button class="replyBtn">대댓글</button>' // 대댓글 버튼 누르면 대댓글 작성 모드
      tmp += '</li>'
    })
    return tmp + "</ul>";
  }
</script>
</body>
</html>

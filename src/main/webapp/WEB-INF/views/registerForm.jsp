<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.net.URLDecoder"%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Register</title>
  <style>
    body {
      font-family: Arial, Helvetica, sans-serif;
      background-color: rgb(0, 0, 0);
    }

    * {
      box-sizing: border-box;
    }

    /* Add padding to containers */
    .container {
      display : flex;
      flex-direction: column;
      align-items:center;
      position : absolute;
      top:50%;
      left:50%;
      transform: translate(-50%, -50%) ;
      padding: 16px;
      background-color: rgb(255, 255, 255);
    }

    /* Full-width input fields */
    input[type=text], input[type=password] {
      width: 100%;
      padding: 15px;
      margin: 5px 0 22px 0;
      display: inline-block;
      border: none;
      background: #f1f1f1;
    }

    input[type=text]:focus, input[type=password]:focus {
      background-color: #ddd;
      outline: none;
    }

    /* Overwrite default styles of hr */
    hr {
      border: 1px solid #f1f1f1;
      margin-bottom: 25px;
    }

    /* Set a style for the submit button */
    .registerbtn {
      background-color: #04AA6D;
      color: white;
      padding: 16px 20px;
      margin: 8px 0;
      border: none;
      cursor: pointer;
      width: 100%;
      opacity: 0.9;
    }

    .registerbtn:hover {
      opacity: 1;
    }

    /* Add a blue text color to links */
    a {
      color: dodgerblue;
    }

  </style>
</head>
<body>
  <form action="<c:url value="/register/save"/>" method="POST" onsubmit="return formCheck(this)">
    <div class="container">
      <h1>Register</h1>
      <p>Please fill in this form to create an account.</p>
      <hr>

      <label for="id"><b>ID</b></label>
      <input type="text" placeholder="Enter ID" name="id" id="id" required>

      <label for="psw"><b>Password</b></label>
      <input type="text" placeholder="Enter Password" name="psw" id="psw" required>

      <label for="name"><b>name</b></label>
      <input type="text" placeholder="Enter Name" name="name" id="name" required>

      <label for="email"><b>Email</b></label>
      <input type="text" placeholder="Enter Email" name="email" id="email" required>

      <label for="birth"><b>Birth</b></label>
      <input type="text" placeholder="YYYYMMDD" name="birth" id="birth" required>

      <button class="registerbtn">Register</button>
      <p>Already have an account? <a href="#">Sign in</a>.</p>
    </div>
  </form>
  <script>
    function formCheck(frm) {
      let msg ='';

      if(frm.id.value.length<3) {
        setMessage('id의 길이는 3이상이어야 합니다.', frm.id);
        return false;
      }

      if(frm.pwd.value.length<3) {
        setMessage('pwd의 길이는 3이상이어야 합니다.', frm.pwd);
        return false;
      }

      return true;
    }

    function setMessage(msg, element){
      document.getElementById("msg").innerHTML = `<i class="fa fa-exclamation-circle"> ${'${msg}'}</i>`;

      if(element) {
        element.select();
      }
    }
  </script>
</body>
</html>
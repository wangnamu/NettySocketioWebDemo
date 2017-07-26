<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>loginView</title>

    <link href="css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
        body {
            text-algin: center;
        }

        .container {
            width: 240px;
            margin-right: auto;
            margin-left: auto;
            margin-top: 50px;
            margin-right: auto;
        }

        #btnOK {
            margin-top: 20px;
            width: 220px;
        }
    </style>
    <script src="js/socket.io/socket.io.js"></script>
    <script src="js/moment.min.js"></script>
    <script src="js/jquery-1.7.2.min.js"></script>

    <script type="text/javascript">


        $(function () {

            $("#btnOK").click(function () {
                var username = $("#inputUserName").val();
                var password = $("#inputPassword").val();

                $.post("/NettySocketioWebDemo/login", {
                    "username": username,
                    "password": password
                }, function (result) {
                    console.log(result)
                    if (result.isSuccess) {
                        location.href = "/NettySocketioWebDemo/indexView";
                    }
                }, "json");

            })
        });
    </script>
</head>
<body>

<div class="container">

    <form class="form-signin" onsubmit="return false;">
        <label for="inputUserName">用户名</label> <input type="text"
                                                      id="inputUserName" placeholder="请输入用户名" required autofocus/>
        <label
                for="inputPassword">密码</label> <input type="password"
                                                      id="inputPassword" placeholder="请输入密码" required/> </br>
        <button id="btnOK" class="btn btn-lg btn-primary btn-block"
                type="submit">确定
        </button>
    </form>

</div>


</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>indexView</title>
    <link href="css/bootstrap.css" rel="stylesheet">

    <style>
        body {
            padding: 20px;
        }

        #console {
            height: 400px;
            overflow: auto;
        }

        .username-msg {
            color: orange;
        }

        .connect-msg {
            color: green;
        }

        .disconnect-msg {
            color: red;
        }

        .send-msg {
            color: #888
        }
    </style>


    <script src="js/socket.io/socket.io.js"></script>
    <script src="js/moment.min.js"></script>
    <script src="js/jquery-1.7.2.min.js"></script>

    <script>
        function generateUUID() {
            var d = new Date().getTime();
            var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
                function (c) {
                    var r = (d + Math.random() * 16) % 16 | 0;
                    d = Math.floor(d / 16);
                    return (c == 'x' ? r : (r & 0x7 | 0x8)).toString(16);
                });
            return uuid;
        };

        var SID = '${SID}';
        var userName = '${userName}';
        var nickName = '${nickName}';
        var chatID = '${groupChatID}';

        var deviceToken = generateUUID();

        console.log(deviceToken)

        var socket = io.connect('http://192.168.3.5:3000');

        socket
            .on(
                'connect',
                function () {
                    login();
                    output('<span class="connect-msg">Client has connected to the server!</span>');
                });

        socket.on('kickoff', function (data) {
            output('<span class="disconnect-msg">kickoff->"' + data + '"</span>');
        });

        /* socket.on('notifyotherplatforms', function(data) {
            output('<span class="connect-msg">"notify from APP:' + data
                    + '"</span>');
        }); */

        socket.on('news', function (data, ackServerCallback) {
            var res = JSON.parse(data)
            output('<span class="username-msg">' + data + ':</span>');
            if (ackServerCallback) {
                ackServerCallback('success');
            }
        });

        socket.on('disconnect', function () {
            output('<span class="disconnect-msg">The client has disconnected!</span>');
        });

        function login() {

            var jsonObject = '{"DeviceToken":"' + deviceToken
                + '","DeviceType":"PC","LoginTime":' + new Date().getTime()
                + ',"NickName":"' + nickName + '","CheckStatus":false,"SID":"'
                + SID + '","UserName":"' + userName + '"}';

            console.log(jsonObject);

            socket.emit('login', jsonObject,
                function (arg) {
                    output('<span class="connect-msg">login success' + arg
                        + '</span>');
                });
        }

        function sendMessage() {
            /* var message = $('#msg').val();
            var jsonObject = '{"SID":"1000001","SenderID":"AAA","ReceiverIDs":["EEE"],"Title":"王南","Body":"'
                    + message
                    + '","Time":1482729320828,"MessageType":"text","IsAlert":true,"Category":"custom","OthersType",null,"Others":null}';
            socket.emit('news', jsonObject); */

            $.post("/NettySocketioWebDemo/sendText", {
                "senderID": SID,
                "senderDeviceToken": deviceToken,
                "chatID": chatID,
                "messageID": generateUUID(),
                "body": $('#msg').val()
            }, function (result) {
                console.log(result)

                $('#msg').val('');
                output('<span class="username-msg">' + JSON.stringify(result) + ':</span>');

            }, "json");


        }

        // function notifyOtherPlatforms() {
        // 	var jsonObject = '{"UserID":"001","SourceDeviceType":"PC","Others":null}';
        // 	socket.emit('notifyotherplatforms', jsonObject);
        // }

        function output(message) {
            var currentTime = "<span class='time'>"
                + moment().format('HH:mm:ss.SSS') + "</span>";
            var element = $("<div>" + currentTime + " " + message + "</div>");
            $('#console').prepend(element);
        }

        $(document).keydown(function (e) {
            if (e.keyCode == 13) {
                $('#send').click();
            }
        });
    </script>
</head>

<body>

<h1>Netty-socketio Demo Chat</h1>

<br/>

<div id="console" class="well"></div>

<form class="well form-inline" onsubmit="return false;">
    <input id="msg" class="input-xlarge" type="text" placeholder="输入文字..."/>
    <button type="button" onClick="sendMessage()" class="btn" id="send">发送</button>
</form>


</body>

</html>
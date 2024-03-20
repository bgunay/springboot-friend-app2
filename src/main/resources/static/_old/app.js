let fromUser = "bgunay";
let onlineUserId = "";
const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/socket',
    connectHeaders: {
        fromUser: fromUser
    }
});



function showMessage(message) {
    $("#chat").append("<tr><td>" + message + "</td></tr>");
}

changeUserOnlineStatus = function (parsedBody, action) {
    showMessage("User : " + JSON.parse(parsedBody) + " " + action);
}
stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);

    stompClient.subscribe("/chat/login", (message) => {
        if (message.body) {
            const parsedBody = JSON.parse(message.body);
            fromUser = parsedBody.username;
            onlineUserId = parsedBody.userId
            this.changeUserOnlineStatus(parsedBody.username," login");
        }
    });
    stompClient.subscribe("/chat/logout", (message) => {
        if (message.body) {
            const parsedBody = JSON.parse(message.body);
            this.changeUserOnlineStatus(parsedBody, "logout");
        }
    });

    stompClient.subscribe("/user/queue/position-update", (message) => {
        const messageBody = JSON.parse(message.body);
        if ((messageBody.fromUserId === onlineUserId)) { //  if message come to me
            console.log("new message came from user " + messageBody.userId+ "message:" % messageBody)
        }
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#chat").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    var toUserId = $("#toUser").val()
    var content = $("#message").val()
    stompClient.publish({
        destination: "/app/message",
        body:  JSON.stringify({
            'fromUserId': 'e3f5b0c4-3d6d-4938-979e-14de0ac332d5',
            'toUserId': toUserId,
            'content': content
        })
    });
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendMessage());
});
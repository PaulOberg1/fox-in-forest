var socket = new SockJS('/app');
var stompClient = Stomp.over(socket);

function generateClientId() {
    const timestamp = Date.now().toString(36);
    const randomNum = Math.random().toString(36).substring(2, 10);
    return `${timestamp}-${randomNum}`;
}

var clientId = generateClientId();

stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);

    // Subscribe to the broadcast topic
    stompClient.subscribe('/broadcast/', function (message) {
    });

    // Subscribe to the user's private queue
    stompClient.subscribe('/p2p/', function (message) {
    });
});

function connect() {
    stompClient.send("/app/connect",{},JSON.stringify({
        clientId:clientId
    }));
}
function login(username,password) {
    stompClient.send("/app/login",{},JSON.stringify({
        clientId:clientId,
        username:username,
        password:password
    }))
}
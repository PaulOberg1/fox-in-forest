const stompClient = new StompJs.Client({
    webSocketFactory: function () {
        return new SockJS('/game');
    },
    debug: function (str) {
        console.log(str);
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
})

function generateId() {
    const timestamp = Date.now().toString(36);
    const randomNum = Math.random().toString(36).substring(2, 10);
    return `${timestamp}-${randomNum}`;
}

function toggleForms() {
    document.getElementById('loginForm').classList.toggle('hidden');
    document.getElementById('signupForm').classList.toggle('hidden');
}

var clientId = generateId(); //Sent with every request to uniquely identify sender

stompClient.onConnect = function (frame) {    
    stompClient.subscribe(`/user/${clientId}/p2p/homePage`, function (message) {
        /*
            user has now logged in/signed up
            display menu allowing them to start a new game or join existing game
            if new game started: call newGame()
            if "join existing game" selected:
                prompt them to enter game ID, display "connect" button which will call joinGame(gameId)
        */
	document.getElementById('loginForm').classList.add('hidden');
	document.getElementById('signupForm').classList.add('hidden');
	document.getElementById('homePage').classList.remove('hidden');
    });


    stompClient.subscribe(`/user/${clientId}/p2p/verifyConnect`, function (message) {
        /*
            client id has been successfully mapped to a new session on the backend
            no other actions can be taken until this function has been called
        */
	document.getElementById('loginForm').classList.remove('hidden');
    });


    stompClient.subscribe(`/user/${clientId}/p2p/${gameId}/loadGame`, function (message) {
        /*
            new game has been started, player loaded in, display the game
            player cannot do anything yet
        */
    });

    stompClient.subscribe(`/broadcast/${gameId}/centralDeckUpdate`, function (message) {
        /*
            CentralDeckMessage DTO object returned
            update the display of the central deck to display these cards
        */
    })

    stompClient.subscribe(`/user/${clientId}/p2p/playerControlUpdate`, function (message) {
        /*
            indicates our player can play
            enable calls to playCard(suit,rank) if specific card selected
        */
    })

    stompClient.subscribe(`/broadcast/${gameId}/endGame`, function (message) {
        /*
            object returned from backend storing final score, display this
        */
    })
    connect();
};

stompClient.onStompError = function (frame) {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

stompClient.activate();

function connect() {
    stompClient.publish({
        destination: "/app/connect",
        body: JSON.stringify({clientId:clientId})
    }); //will result in return call to /p2p/verifyConnect
}
function login(username,password) {
    stompClient.publish({
        destination: "/app/login",
        body: JSON.stringify({clientId:clientId,username:username,password:password})
    }) //will result in return call to /p2p/homePage
}
function signup(username,password) {
    stompClient.publish({
        destination: "/app/signup",
        body: JSON.stringify({clientId:clientId,username:username,password:password})
    }) //will result in return call to /p2p/homePage
}
function joinGame(gameIdTemp) {
    gameId = gameIdTemp;
    stompClient.publish({
        destination: "/app/joinGame",
        body: JSON.stringify({clientId:clientId,gameId:gameId})
    }) //wil result in call to /p2p/loadGame
}
function newGame() {
    joinGame(generateId()); //wil result in call to /p2p/loadGame
}
function playCard(suit,rank) {
    stompClient.publish({
        destination: "/app/playCard",
        body: JSON.stringify({clientId:clientId,suit:suit,rank:rank})
    }) //will result in call to broadcast/centralDeckUpdate, and broadcast/updatePlayerScores
}


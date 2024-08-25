var socket = new SockJS('/app');
var stompClient = Stomp.over(socket);



function generateId() {
    const timestamp = Date.now().toString(36);
    const randomNum = Math.random().toString(36).substring(2, 10);
    return `${timestamp}-${randomNum}`;
}

var clientId = generateId(); //Sent with every request to uniquely identify sender



stompClient.connect({}, function (frame) {
    
    stompClient.subscribe('/p2p/homePage', function (message) {
        /*
            user has now logged in/signed up
            display menu allowing them to start a new game or join existing game
            if new game started: call newGame()
            if "join existing game" selected:
                prompt them to enter game ID, display "connect" button which will call joinGame(gameId)
        */
    });


    stompClient.subscribe('/p2p/verifyConnect', function (message) {
        /*
            client id has been successfully mapped to a new session on the backend
            no other actions can be taken until this function has been called
        */
    });


    stompClient.subscribe('/p2p/{game_id}/loadGame', function (message) {
        /*
            new game has been started, player loaded in, display the game
            player cannot do anything yet
        */
    });

    stompClient.subscribe("/broadcast/{game_id}/centralDeckUpdate", function (message) {
        /*
            CentralDeckMessage DTO object returned
            update the display of the central deck to display these cards
        */
    })

    stompClient.subscribe("/broadcast/{game_id}/playerControlUpdate", function (message) {
        /*
            object returned from backend storing client id of player whose turn is next
            compare this id with our clientId attribute, if matching then our player's turn
            enable calls to playCard(suit,rank) if specific card selected
        */
    })

    stompClient.subscribe("/broadcast/{game_id}/endGame", function (message) {
        /*
            object returned from backend storing final score, display this
        */
    })


});

function connect() {
    stompClient.send("/app/connect",{},JSON.stringify({
        clientId:clientId
    })); //will result in return call to /p2p/verifyConnect
}
function login(username,password) {
    stompClient.send("/app/login",{},JSON.stringify({
        clientId:clientId,
        username:username,
        password:password
    })) //will result in return call to /p2p/homePage
}
function signup(username,password) {
    stompClient.send("/app/signup",{},JSON.stringify({
        clientId:clientId,
        username:username,
        password:password
    })) //will result in return call to /p2p/homePage
}
function joinGame(gameId) {
    stompClient.send("/app/joinGame",{},JSON.stringify({
        clientId:clientId,
        gameId:gameId
    })) //wil result in call to /p2p/loadGame
}
function newGame() {
    var gameId = generateId();
    joinGame(gameId); //wil result in call to /p2p/loadGame
}
function playCard(suit,rank) {
    stompClient.send("/app/playCard",{},JSON.stringify({
        clientId:clientId,
        suit:suit,
        rank:rank
    })) 
    //will result in call to broadcast/centralDeckUpdate, and broadcast/updatePlayerScores
}   
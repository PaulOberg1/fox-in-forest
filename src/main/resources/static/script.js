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

let clientId = generateId(); //Sent with every request to uniquely identify sender
let gameId = null;
let cardList = [];
let selectedCardIndex = null;
let wonPairs = 0;
let lostPairs = 0;
let firstCardSuitPlayed = null;
let opponentCardNum = 13;
let roundWon = false;

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


    stompClient.subscribe(`/user/${clientId}/p2p/addPlayerUpdate`, function (message) {
        /*
            new game has been started, player loaded in, display the game
            player cannot do anything yet
        */
        document.getElementById('homePage').classList.add('hidden');
        document.getElementById('gameIdDisplay').innerText = `Game ID: ${gameId}`
        newSubscriptions();
        cardList = JSON.parse(message.body).cardList;
        displayCards();
        displayOpponentCards(opponentCardNum);
        document.getElementById('wonPairs').innerText = wonPairs;
        document.getElementById('lostPairs').innerText = lostPairs;
    });
    
    stompClient.subscribe(`/user/${clientId}/p2p/playerControlUpdate`, function (message) {
        /*
            indicates our player can play
            enable calls to playCard(suit,rank) if specific card selected
        */
        document.getElementById('playButton').classList.remove('hidden');
    })

    stompClient.subscribe(`/user/${clientId}/p2p/serverAlertMessage`, function (message) {
        /*
            recieves a message from the server, should be displayed as an alert
        */
        alert(JSON.parse(message.body).serverMessage);
    })

    stompClient.subscribe(`/user/${clientId}/p2p/decreeCardUpdate`, function (message) {
        /*
            DecreeCardMessage DTO object returned
            update display of decree card to display the card
        */
        const data = JSON.parse(message.body);
 
        const cardElement = document.createElement('img');
        cardElement.src = 'path/to/card/image.png1'; //image path goes here
        cardElement.alt = `${data.suit} ${data.rank}`;
        cardElement.style = 'margin: 0 10px; cursor: pointer;';

        decreeCardDiv = document.getElementById('decreeCard');
        decreeCardDiv.appendChild(cardElement);
    })

   connect();
};

function newSubscriptions() {
    stompClient.subscribe(`/broadcast/${gameId}/centralDeckUpdate`, function (message) {
        /*
            CentralDeckMessage DTO object returned
            update the display of the central deck to display these cards
        */
        document.getElementById('centralDeck').classList.remove('hidden');
        const data = JSON.parse(message.body);
        displayCentralDeck(data.playerIds, data.cardSuits, data.cardRanks, data.winner, data.endRound);
    })

    stompClient.subscribe(`/broadcast/${gameId}/endGame`, function (message) {
        /*
            object returned from backend storing final score, display this
        */
        document.getElementById('endGame').classList.remove('hidden');
        const data = JSON.parse(message.body);
        if (data.player1Id === clientId) {
            document.getElementById('player1score').innerText = data.player1Points;
            document.getElementById('player2score').innerText = data.player2Points;
        }
        else {
            document.getElementById('player1score').innerText = data.player2Points;
            document.getElementById('player2score').innerText = data.player1Points;
        }
    })
}

function displayCards() {
    const cardDisplay = document.getElementById('cardDisplay');
    cardDisplay.innerHTML = ''; // Clear previous cards

    cardList.forEach((card, index) => {
        const cardElement = document.createElement('img');
        cardElement.src = 'path/to/card/image.png'; // Image path goes here
        cardElement.alt = `${card.suit} ${card.rank}`;
        cardElement.style = 'margin: 0 10px; cursor: pointer;';
        cardElement.draggable = true; // Make the card draggable
        cardElement.dataset.index = index; // Store the card's index

        cardElement.onclick = function() {
            selectCard(index);
        };

        cardElement.ondragstart = function(event) {
            event.dataTransfer.setData('text/plain', index);
        };

        cardElement.ondragover = function(event) {
            event.preventDefault(); // Allow the drop
        };

        cardElement.ondrop = function(event) {
            event.preventDefault();
            const draggedIndex = event.dataTransfer.getData('text/plain');
            swapCards(draggedIndex, index);
        };

        cardDisplay.appendChild(cardElement);
    });

    document.getElementById('gameContainer').classList.remove('hidden');
}

function swapCards(fromIndex, toIndex) {
    const temp = cardList[fromIndex];
    cardList[fromIndex] = cardList[toIndex];
    cardList[toIndex] = temp;
    displayCards(); // Re-render cards after swapping
}

function selectCard(index) {
    // Deselect any selected card
    if (selectedCardIndex !== null) {
        document.querySelector(`#cardDisplay img.selected`).classList.remove('selected');
    }

    selectedCardIndex = index;

    const selectedCardElement = document.querySelectorAll('#cardDisplay img')[index];
    selectedCardElement.classList.add('selected');

    console.log(`Selected card: ${cardList[index].suit} ${cardList[index].rank}`);
}

function playSelectedCard() {
    if (selectedCardIndex === null) {
        alert('Please select a card to play');
        return;
    }
    const selectedCard = cardList[selectedCardIndex];
    if (!isValidCard(selectedCard.suit)) {
        alert('Please select a card that matches the opponent\'s suit')
        return;
    }
    playCard(selectedCard.suit, selectedCard.rank);
    cardList.splice(selectedCardIndex, 1);//remove selected card
    displayCards(); //refresh display
    selectedCardIndex = null;
    document.getElementById('playButton').classList.add('hidden');
}

function isValidCard(suit) {
    if (document.getElementById('player2').innerHTML === '' || firstCardSuitPlayed === suit || roundWon) {
        roundWon = false;
        return true;
    }
    for (let index = 0; index < cardList.length; index++) {
        if (cardList[index].suit === firstCardSuitPlayed) {
            return false;
        }
    }
    return true;
}

function displayCentralDeck(playerIds, cardSuits, cardRanks, winner, endRound) {
    //clear previous cards
    document.getElementById('player1').innerHTML = '';
    document.getElementById('player2').innerHTML = '';

    firstCardSuitPlayed = cardSuits[0];

    if (playerIds[playerIds.length-1] !== clientId) {
        displayOpponentCards(--opponentCardNum);
    }

    playerIds.forEach((playerId, index) => {
        let playerSection;
        if (playerId === clientId) {
            playerSection = document.getElementById('player1');
        }
        else {
            playerSection = document.getElementById('player2');
        }
        const centralCard = document.createElement('img');
        centralCard.src = 'path/to/card/image2.png'; //image path goes here
        centralCard.alt = `${cardSuits[index]} ${cardRanks[index]}`;
        centralCard.style = 'margin: 0 8px; cursor: pointer;';
        playerSection.appendChild(centralCard);
     })
     if (endRound) {
        if (winner === clientId) {
            wonPairs++;
            roundWon = true;
        }
        else {
            lostPairs++;
        }
        document.getElementById('wonPairs').innerText = wonPairs;
        document.getElementById('lostPairs').innerText = lostPairs;
    }
}

function displayOpponentCards(opponentCardNum) {
    const opponentDiv = document.getElementById('player2cards');
    opponentDiv.innerHTML = '';
    for (let index = 0; index < opponentCardNum; index++) {
        const card = document.createElement('img');
        card.src = 'path/to/card/image3.png'; //image path goes here
        card.alt = 'Card';
        card.style = 'margin: 0 8px; cursor: pointer;';
        opponentDiv.appendChild(card);
    }
}

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
    if (!username || !password) {
        alert('Fill in username and password fields');
        return;
    }
    stompClient.publish({
        destination: "/app/login",
        body: JSON.stringify({clientId:clientId,username:username,password:password})
    }) //will result in return call to /p2p/homePage
}
function signup(username,password) {
    if (!username || !password) {
        alert('Fill in username and password fields');
        return;
    }
    stompClient.publish({
        destination: "/app/signup",
        body: JSON.stringify({clientId:clientId,username:username,password:password})
    }) //will result in return call to /p2p/homePage
}
function joinGame(gameIdTemp) {
    if (!gameIdTemp) {
        alert('Fill in game ID field');
        return;
    }
    gameId = gameIdTemp;
    stompClient.publish({
        destination: "/app/addPlayer",
        body: JSON.stringify({clientId:clientId,gameId:gameId})
    }) //wil result in call to /p2p/loadGame
}
function newGame() {
    gameId = generateId();
    stompClient.publish({
        destination: "/app/newGame",
        body: JSON.stringify({clientId:clientId,gameId:gameId})
    }) //wil result in call to /p2p/loadGame
}
function playCard(suit,rank) {
    stompClient.publish({
        destination: "/app/playCard",
        body: JSON.stringify({clientId:clientId,gameId:gameId,suit:suit,rank:rank})
    }) //will result in call to broadcast/centralDeckUpdate, and broadcast/updatePlayerScores
}

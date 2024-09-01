package com.foxingarden.FoxInGarden.mcts;

import java.util.ArrayList;

import com.foxingarden.FoxInGarden.model.domain.Card;
import com.foxingarden.FoxInGarden.model.domain.Deck;
import com.foxingarden.FoxInGarden.model.domain.Game;

import lombok.Getter;

@Getter
public class State {
    private Deck playerDeck;
    private Deck opDeck;

    private Card decreeCard;
    private Card curPlayerCard;
    private Card curOpCard;

    private int playerScore;
    private int opScore;

    public State(Deck playerDeck, Deck opDeck, Card decreeCard) {
        this.playerDeck = playerDeck;
        this.opDeck = opDeck;
        this.decreeCard = decreeCard;
        this.playerScore = 0;
        this.opScore = 0;
    }

    public int getReward() {
        Game game = new Game("");
        int playerPoints = game.computePointsFromScore(playerScore);
        int opPoints = game.computePointsFromScore(opScore);

        return opPoints - playerPoints;
    }

    public void performAction(Action action) {
        if (action.isPlayer()) {
            Card playerCard = action.getCard();
            playerDeck.remove(playerCard);
            curPlayerCard = playerCard;
        }
        else {
            Card opCard = action.getCard();
            opDeck.remove(opCard);
            curOpCard = opCard;
        }
        if (curPlayerCard!=null && curOpCard!=null) {
            if (computeWinner(curPlayerCard,curOpCard,decreeCard)==0)
                playerScore++;
            else
                opScore++;
        }

    }

    public int computeWinner(Card card1, Card card2, Card decreeCard) {
        String trumpSuit = decreeCard.getSuit();
        if (card1.getSuit()==trumpSuit && card2.getSuit()==trumpSuit) {
            if (card1.getRank()>card2.getRank())
                return 0;
            return 1;
        }
        else if (card1.getSuit()==trumpSuit)
            return 0;
        else if (card2.getSuit()==trumpSuit)
            return 1;
        else {
            if (card1.getSuit()!=card2.getSuit())
                return 0;
            else if (card1.getRank()>card2.getRank())
                return 0;
            else
                return 1;
        }
        
    }

    public ArrayList<Action> getLegalActions(boolean isPlayerTurn) {
        ArrayList<Action> legalActions = new ArrayList<>();
        if (isPlayerTurn) {
            for (Card playerCard : playerDeck.getCards()) {
                Action action = new Action(playerCard,isPlayerTurn);
                legalActions.add(action);
            }
        }
        else {
            for (Card opCard : opDeck.getCards()) {
                Action action = new Action(opCard, isPlayerTurn);
                legalActions.add(action);
            }
        }
        return legalActions;
    }

    public boolean isTerminal() {
        return (playerDeck.length()==0 && opDeck.length()==0);
    }

}

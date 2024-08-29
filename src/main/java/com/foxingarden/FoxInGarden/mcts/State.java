package com.foxingarden.FoxInGarden.mcts;

import java.util.ArrayList;

import com.foxingarden.FoxInGarden.model.domain.Card;

import lombok.Getter;

@Getter
public class State {
    private int reward;
    private ArrayList<Card> playerCards;
    private ArrayList<Card> opCards;

    int playerScore;
    int opScore;

    public State(ArrayList<Card> playerCards, ArrayList<Card> opCards, int reward) {
        this.playerCards = playerCards;
        this.opCards = opCards;
        this.reward = reward;
        this.playerScore = 0;
        this.opScore = 0;
    }

    public void performAction(Action action) {
        Card playerCard = action.getPlayerCard();
        playerCards.remove(playerCard);

        Card opCard = action.getOpCard();
        opCards.remove(opCard);
    }

    public ArrayList<Action> getLegalActions() {
        ArrayList<Action> legalActions = new ArrayList<>();
        for (Card playerCard : playerCards) {
            for (Card opCard : opCards) {
                Action action = new Action(playerCard, opCard);
                legalActions.add(action);
            }
        }
        return legalActions;
    }

    public boolean isTerminal() {
        return (playerCards.size()==0 && opCards.size()==0);
    }

}

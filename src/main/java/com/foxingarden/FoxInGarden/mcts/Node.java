package com.foxingarden.FoxInGarden.mcts;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.ArrayList;

import lombok.Getter;

@Getter
public class Node {
    private int visits;
    private int totalReward;
    private State state;
    private Node parent;
    private boolean isPlayerTurn;
    private HashMap<Action,Node> children;
    private ArrayList<Action> untriedActions;

    private int playerScore;
    private int opScore;
    
    public Node(State state, Node parent, boolean isPlayerTurn) {
        this.state = state;
        this.parent = parent;
        this.isPlayerTurn = isPlayerTurn;
        this.children = new HashMap<>();
        this.totalReward = 0;
        this.visits = 0;
        this.untriedActions = getLegalActions(state);

        playerScore = 0;
        opScore = 0;
    }

    public ArrayList<Action> getLegalActions(State state) {
        return state.getLegalActions(isPlayerTurn);
    }

    public Node getChildByAction(Action action) {
        return children.get(action);
    }

    public Node selectChild() {
        int maxChildReward = 0;
        Node bestChild = null;

        double logVisits = Math.log(visits);
        
        for (Entry<Action,Node> entry : children.entrySet()) {
            Node child = entry.getValue();
            double childReward = child.getTotalReward();
            double childVisits = child.getVisits();

            int childTotalReward = (int)((childReward/childVisits) + 2*Math.sqrt(logVisits/childVisits));

            if (childTotalReward>maxChildReward) {
                maxChildReward = childTotalReward;
                bestChild = child;
            }
        }
        return bestChild;
    }

    public Action getBestAction() {
        int maxChildReward = 0;
        Action bestAction = null;

        double logVisits = Math.log(visits);
        
        for (Entry<Action,Node> entry : children.entrySet()) {
            Node child = entry.getValue();
            double childReward = child.getTotalReward();
            double childVisits = child.getVisits();

            int childTotalReward = (int)((childReward/childVisits) + 2*Math.sqrt(logVisits/childVisits));

            if (childTotalReward>maxChildReward) {
                maxChildReward = childTotalReward;
                bestAction = entry.getKey();
            }
        }
        return bestAction;
    }

    public boolean isFullyExpanded() {
        return untriedActions.size() != 0;
    }

    public void addChild(State state, Action action) {
        if (!children.containsKey(action)) {
            Node child = new Node(state,this,!isPlayerTurn);
            children.put(action,child);
        }
    }

    public void updateReward(int reward) {
        this.totalReward+=reward;
    }
}
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
    private HashMap<Action,Node> children;
    private ArrayList<Action> untriedActions;
    
    public Node(State state, Node parent) {
        this.state = state;
        this.parent = parent;
        this.children = new HashMap<>();
        this.totalReward = 0;
        this.visits = 0;
        this.untriedActions = getLegalActions(state);
    }

    public ArrayList<Action> getLegalActions(State state) {
        return state.getLegalActions();
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

    public boolean isFullyExpanded() {
        return untriedActions.size() != 0;
    }

    public void addChild(State state, Action action) {
        if (!children.containsKey(action)) {
            Node child = new Node(state,this);
            children.put(action,child);
        }
    }

    public void updateReward(int reward) {
        this.totalReward+=reward;
    }
}
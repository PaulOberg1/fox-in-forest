package com.foxingarden.FoxInGarden.mcts;

import java.util.ArrayList;

public class MCTS {
    private Node root;

    public MCTS(Node root) {
        this.root = root;
    }

    public void search(int iterations) {
        for (int i=0; i<iterations; i++) {
            Node node = select(root);
            if (!node.isFullyExpanded()) {
                node = expand(node);
            }
            int reward = simulate(node.getState());
            backProp(node, reward);
        }
    }

    int simulate(State state) {
        while (!state.isTerminal()) {
            ArrayList<Action> legalActions = state.getLegalActions();
            int i = (int)Math.floor(Math.random()*legalActions.size());
            Action action = legalActions.get(i);
            state.performAction(action);
        }
        return state.getReward();
    }

    public Node expand(Node node) {
        ArrayList<Action> untriedActions = node.getUntriedActions();
        int i = (int)Math.floor(Math.random()*untriedActions.size());
        Action action = untriedActions.get(i);
        node.getState().performAction(action);
        node.addChild(node.getState(),action);
        return node;
    }

    public Node select(Node node) {
        Node curNode = node;
        while (curNode.getChildren().size()!=0 && !curNode.isFullyExpanded()) {
            curNode = node.selectChild();
        }
        return curNode;
    }

    public void backProp(Node node, int reward) {
        while (node!=null) {
            node.updateReward(reward);
            node = node.getParent();
        }
    }
}



package com.foxingarden.FoxInGarden.mcts;

import java.util.ArrayList;

import lombok.Getter;

@Getter
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

    public int simulate(State state) {
        while (!state.isTerminal()) {
            ArrayList<Action> opActions = state.getLegalActions(false);
            int i = (int)Math.floor(Math.random()*opActions.size());
            Action action = opActions.get(i);
            state.performAction(action);

            ArrayList<Action> playerActions = state.getLegalActions(true);
            i = (int)Math.floor(Math.random()*playerActions.size());
            action = playerActions.get(i);
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

    public Action getBestAction() {
        return root.getBestAction();
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

    public void updateRoot(Action action) {
        this.root = root.getChildByAction(action);
    }
}



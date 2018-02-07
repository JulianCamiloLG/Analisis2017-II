/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisis.modelos;

import java.util.ArrayList;

/**
 *
 * @author Sebastian
 */
public class Node {
    private int x;
    private int y;
    private int cost;
    private ArrayList<Node> neighbors;
    private Node predecessor;
    private boolean inRoute;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.cost = 0;
        neighbors = new ArrayList<>();
        this.predecessor = null;
        this.inRoute = false;
    }
    
    public void addNeighbor(Node node){
        neighbors.add(node);
    }
    
    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setInRoute(boolean inRoute) {
        this.inRoute = inRoute;
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getCost() {
        return cost;
    }

    public ArrayList<Node> getNeighbors() {
        return neighbors;
    }

    public Node getPredecessor() {
        return predecessor;
    }
    
    
}

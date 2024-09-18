package com.jwetherell.algorithms.cluster;

import com.jwetherell.algorithms.data_structures.MultidimensionalNode;

import java.util.List;

/**
 * node entity implements in DBSCAN, extends the class MultidimensionalNode
 *
 * @author dawei xiong
 */
public class DBNode extends MultidimensionalNode {
    private int groupId=-1; // assign DBNode into different group
    private boolean visited=false; // record if the node is visited, default value false

    public DBNode(List<Double> coordinates){
        super(coordinates);
    }

    public DBNode(List<Double> coordinates, int groupId, boolean visited) {
        super(coordinates);
        this.groupId = groupId;
        this.visited = visited;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public String toString() {
        return "DBNode{" +
                "dimension="+ super.getDimension() +
                ", coordinates="+ super.getCoordinates().toString() +
                ", groupId=" + groupId +
                ", visited=" + visited +
                '}';
    }
}

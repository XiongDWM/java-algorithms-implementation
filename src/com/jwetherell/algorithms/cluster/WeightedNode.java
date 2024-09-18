package com.jwetherell.algorithms.cluster;

import com.jwetherell.algorithms.data_structures.MultidimensionalNode;

import java.util.List;

public class WeightedNode extends MultidimensionalNode {
    private Long uniqueTag;  // node's tag
    private int weight; // node's weight in integer

    public WeightedNode(List<Double> coordinates) {
        super(coordinates);
    }

    public WeightedNode(List<Double>coordinates,Long uniqueTag,int weight){
        super(coordinates);
        this.uniqueTag=uniqueTag;
        this.weight=weight;
    }

    public Long getUniqueTag() {
        return uniqueTag;
    }

    public void setUniqueTag(Long uniqueTag) {
        this.uniqueTag = uniqueTag;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

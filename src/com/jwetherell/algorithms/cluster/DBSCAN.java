package com.jwetherell.algorithms.cluster;

import com.jwetherell.algorithms.mathematics.ConvexHull;
import com.jwetherell.algorithms.mathematics.Distance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DBSCAN {
    private final List<DBNode> points;
    private static final int NOISE = -1;

    public DBSCAN(List<DBNode> points) {
        this.points=points;
    }

    public List<List<DBNode>> fit(double eps, int minPts,boolean convexHull) {
        int clusterId = 0;
        for (DBNode point : points) {
            if (!point.isVisited()) {
                point.setVisited(true);
                List<DBNode> neighbors = getNeighbors(point, points, eps);
                if (neighbors.size() >= minPts) {
                    expandCluster(point, neighbors, clusterId, points, eps, minPts);
                    clusterId++;
                } else {
                    point.setGroupId(NOISE);
                }
            }
        }
        List<List<DBNode>> clusters=extractClusters();
        if(convexHull)return extractOutline(clusters);
        return clusters;
    }

    private List<DBNode> getNeighbors(DBNode point, List<DBNode> points, double eps) {
        List<DBNode> neighbors = new ArrayList<>();
        for (DBNode neighbor : points) {
            double distance= Distance.squaredDistanceHighDimension(point.getCoordinates(),neighbor.getCoordinates());
            if (distance<= eps) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }


    private void expandCluster(DBNode point, List<DBNode> neighbors, int clusterId, List<DBNode> points, double eps, int minPts) {
        point.setGroupId(clusterId);
        int index = 0;
        while (index < neighbors.size()) {
            DBNode neighbor = neighbors.get(index);
            if (!neighbor.isVisited()) {
                neighbor.setVisited(true);
                List<DBNode> neighborNeighbors = getNeighbors(neighbor, points, eps);
                if (neighborNeighbors.size() >= minPts) {
                    neighbors.addAll(neighborNeighbors);
                }
            }
            if (neighbor.getGroupId() == NOISE) {
                neighbor.setGroupId(clusterId);
            }
            index++;
        }
    }

    private List<List<DBNode>> extractClusters() {
        return new ArrayList<>(points.stream().collect(Collectors.groupingBy(DBNode::getGroupId)).values());
    }

    private List<List<DBNode>> extractOutline(List<List<DBNode>> clusters){
        List<List<DBNode>> result=new ArrayList<>();
        for(List<DBNode>c:clusters){
            try {
                List<DBNode>shape= ConvexHull.grahamScan(c);
                shape.add(shape.get(0));
                result.add(shape);
            }catch (Exception e){
                System.out.println(e.getLocalizedMessage());
            }
        }
        return result;
    }

    public double findEpsilon(int k) {  // generally, k equal to minPts
        List<Double> distances = new ArrayList<>();

        for (DBNode point : points) {
            List<Double> pointDistances = new ArrayList<>();
            for (DBNode other : points) {
                if (point != other) {
                    pointDistances.add(Distance.squaredDistanceHighDimension(point.getCoordinates(),other.getCoordinates()));
                }
            }
            Collections.sort(pointDistances);
            distances.add(pointDistances.get(k - 1));
        }

        distances.sort(Comparator.reverseOrder());
        System.out.println("dis-array: "+distances);
        double maxDiff = 0;
        int elbowIndex = 0;
        for (int i = 1; i < distances.size(); i++) {
            double diff = distances.get(i - 1) - distances.get(i);
            if (diff > maxDiff) {
                maxDiff = diff;
                elbowIndex = i;
            }
        }
        return distances.get(elbowIndex);
    }

}

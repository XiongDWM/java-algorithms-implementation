package com.jwetherell.algorithms.cluster;

import com.jwetherell.algorithms.data_structures.MultidimensionalNode;
import com.jwetherell.algorithms.mathematics.ConvexHull;
import com.jwetherell.algorithms.mathematics.Distance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements the DBSCAN (Density-Based Spatial Clustering of Applications with Noise) algorithm.
 * DBSCAN is a density-based clustering algorithm that groups together points that are closely packed together,
 * marking points that are in low-density regions as outliers (noise).
 *
 * @author dawei xiong
 */
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
                List<DBNode> neighbors = getNeighbors(point, eps);
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

    /**
     * @return node point's neighbors
     */
    private List<DBNode> getNeighbors(DBNode point, double eps) {
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
                List<DBNode> neighborNeighbors = getNeighbors(neighbor, eps);
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

    /**
     * extract the outlines of the clusters
     */
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

    /**
     * to find the k distance for each node and then, find the epsilon between those node
     * @param k generally, k equal to minPts
     */
    public double findEpsilon(int k) {
        List<List<Double>> coords=points.stream().map(MultidimensionalNode::getCoordinates).collect(Collectors.toList());
        List<Double> distances = Distance.kDistance(coords,k);
        distances.sort(Comparator.reverseOrder());
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

    public static void main(String[] args) {
        List<DBNode> points = new ArrayList<>();
        points.add(new DBNode(List.of(426d,1400d)));
        points.add(new DBNode(List.of(430d, 1402d)));
        points.add(new DBNode(List.of(428d, 1407d)));
        points.add(new DBNode(List.of(423d,1500d)));
        points.add(new DBNode(List.of(426d, 1402d)));
        points.add(new DBNode(List.of(425d, 1407d)));
        points.add(new DBNode(List.of(525d, 1202d)));
        points.add(new DBNode(List.of(527d, 1203d)));
        points.add(new DBNode(List.of(529d, 1207d)));
        points.add(new DBNode(List.of(528d, 1203d)));
        points.add(new DBNode(List.of(529d, 1207d)));
        points.add(new DBNode(List.of(325d, 1600d)));
        points.add(new DBNode(List.of(328d, 1570d)));
        points.add(new DBNode(List.of(327d, 1573d)));
        points.add(new DBNode(List.of(330d, 1603d)));
        points.add(new DBNode(List.of(323d, 1550d)));
        DBSCAN dbscan = new DBSCAN(points);
        int minPts = 3;
        double eps = dbscan.findEpsilon(minPts);
        List<DBNode>polyLine=new ArrayList<>(); //nodes not in cluster
        polyLine.add(new DBNode(List.of(420d, 1390d)));
        polyLine.add(new DBNode(List.of(426d, 1410d)));
        polyLine.add(new DBNode(List.of(430d, 1500d)));
        polyLine.add(new DBNode(List.of(428d, 1000d)));
        List<List<DBNode>> clusters = dbscan.fit(eps, minPts,true);
        int i=1;
        List<DBNode>hull=clusters.get(0);
        boolean isCross=ConvexHull.doesPolylineIntersectHull(polyLine,hull);
        System.out.println(isCross);
        for (List<DBNode> cluster : clusters) {
            System.out.println("Cluster: "+i);
            System.out.println(cluster);
            i++;
        }
    }
}

package com.jwetherell.algorithms.mathematics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Distance {

    private Distance() { }

    /*
     * Chess distance
     */
    public static final long chebyshevDistance(long[] point1, long[] point2) {
        long x1 = point1[0];
        long y1 = point1[1];
        long x2 = point2[0];
        long y2 = point2[1];
        return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    public static final double squaredDistance(double x1, double y1, double x2, double y2) {
        double x = x1 - x2;
        double y = y1 - y2;
        double sqr = (x * x) + (y * y);
        return sqr;
    }

    public static final double euclideanDistance(double x1, double y1, double x2, double y2) {
        double x = Math.pow((x1 - x2), 2);
        double y = Math.pow((y1 - y2), 2);
        double sqrt = Math.sqrt(x + y);
        return sqrt;
    }

    /**
     * Manhattan distance
     */

    public static double manhattanDistance(Number x1, Number y1, Number x2, Number y2) {
        double h=Math.abs(y1.doubleValue()-y2.doubleValue());
        double w=Math.abs(x1.doubleValue()-x2.doubleValue());
        return h+w;
    }

    private static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    /**
     * Haversine formula
     * calculates the shortest path between two points on a spherical surface.
     */
    public static double haversineDistance(double x1,double y1,double x2,double y2,double radius){
        double dLat = deg2rad(x2 - x1);  // deg2rad below
        double dLon = deg2rad(y2 - y1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(deg2rad(x1)) * Math.cos(deg2rad(x2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return radius * c;
    }

    /**
     * k-distance
     * Receives a list points containing multiple two-dimensional points
     * (each represented as a double[2] array, x=array[0] and y=array[1]),
     * and an integer k, which indicates the k-th nearest distance to be found.
     */
    public static List<Double> kDistance(List<double[]> points, int k){
        List<Double> distances = new ArrayList<>();
        for (double[] point : points) {
            List<Double> pointDistances = new ArrayList<>();
            for (double[] other : points) {
                if (point[0] == other[0] && point[1] == other[1]) continue;
                pointDistances.add(squaredDistance(point[0],point[1],other[0],other[1]));
            }
            Collections.sort(pointDistances);
            System.out.println(point[0]+","+point[1]+"::"+pointDistances);
            distances.add(pointDistances.get(k - 1));
        }
        return distances;
    }
}

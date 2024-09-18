package com.jwetherell.algorithms.mathematics;

import com.jwetherell.algorithms.cluster.DBNode;
import com.jwetherell.algorithms.cluster.exception.PolyShapeBuildException;
import com.jwetherell.algorithms.data_structures.MultidimensionalNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author dawei xiong
 */
public class ConvexHull {

    public static <T extends MultidimensionalNode> List<T> grahamScan(List<T> nodes) throws Exception {
        // points less than 3 always are collinear or can say this specific cluster is too small to be noticed, there are no `edge` for such cluster;
        if (nodes.size() < 3) throw new PolyShapeBuildException("points less than 3 cannot combine as a shape");
        nodes.sort(Comparator.comparing(it-> it.getCoordinates().get(0)));
        // Check if all points are collinear
        if (areCollinear(nodes)) throw new PolyShapeBuildException("points are collinear");
        // lower hull
        List<T> lower = new ArrayList<>();
        for (T p : nodes) {
            while (lower.size() >= 2 && cross(lower.get(lower.size() - 2), lower.get(lower.size() - 1), p) <= 0) {
                lower.remove(lower.size() - 1);
            }
            lower.add(p);
        }
        // upper hull
        List<T> upper = new ArrayList<>();
        for (int i = nodes.size() - 1; i >= 0; i--) {
            T p = nodes.get(i);
            while (upper.size() >= 2 && cross(upper.get(upper.size() - 2), upper.get(upper.size() - 1), p) <= 0) {
                upper.remove(upper.size() - 1);
            }
            upper.add(p);
        }
        lower.remove(lower.size() - 1);
        upper.remove(upper.size() - 1);
        // combine as the full hull
        lower.addAll(upper);
        return lower;
    }

    public static <T extends MultidimensionalNode> double cross(T o, T a, T b) {
        List<Double>oCoordinates=o.getCoordinates();
        List<Double>aCoordinates=a.getCoordinates();
        List<Double>bCoordinates=b.getCoordinates();
        return (aCoordinates.get(0) - oCoordinates.get(0)) * (bCoordinates.get(1)
                - oCoordinates.get(1)) - (aCoordinates.get(1) - o.getCoordinates().get(1)) * (bCoordinates.get(0) - oCoordinates.get(0));
    }

    public static <T extends MultidimensionalNode> boolean areCollinear(List<T> nodes) {
        T p0 = nodes.get(0);
        T p1 = nodes.get(1);
        for (int i = 2; i < nodes.size(); i++) {
            if (cross(p0, p1, nodes.get(i)) != 0) {
                return false;
            }
        }
        return true;
    }

    public static <T extends MultidimensionalNode> boolean doIntersect(T p1, T q1, T p2, T q2) {
        int d1 = direction(p1, q1, p2);
        int d2 = direction(p1, q1, q2);
        int d3 = direction(p2, q2, p1);
        int d4 = direction(p2, q2, q1);
        return d1 != d2 && d3 != d4;
    }

    private static <T extends MultidimensionalNode> int direction(T a, T b, T c) {
        double value=cross(a,b,c);
        if (value == 0) return 0;  // collinear
        return (value > 0) ? 1 : 2; // clockwise or counterclockwise
    }

    public static <T extends MultidimensionalNode> boolean doesPolylineIntersectHull(List<T> polyline, List<T> hull) {
        for (int i = 0; i < polyline.size() - 1; i++) {
            T p1 = polyline.get(i);
            T q1 = polyline.get(i + 1);

            for (int j = 0; j < hull.size(); j++) {
                T p2 = hull.get(j);
                T q2 = hull.get((j + 1) % hull.size());

                if (doIntersect(p1, q1, p2, q2)) {
                    return true;
                }
            }
        }
        return false;
    }
}

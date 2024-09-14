package com.jwetherell.algorithms.mathematics;

import com.jwetherell.algorithms.cluster.DBNode;
import com.jwetherell.algorithms.cluster.exception.PolyShapeBuildException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author dawei xiong
 */
public class ConvexHull {

    public static List<DBNode> grahamScan(List<DBNode> nodes) throws Exception {
        // points less than 3 always are collinear or can say this specific cluster is too small to be noticed, there are no `edge` for such cluster;
        if (nodes.size() < 3) throw new PolyShapeBuildException("points less than 3 cannot combine as a shape");
        nodes.sort(Comparator.comparing(it-> it.getCoordinates().get(0)));
        // Check if all points are collinear
        if (areCollinear(nodes)) throw new PolyShapeBuildException("points are collinear");
        // lower hull
        List<DBNode> lower = new ArrayList<>();
        for (DBNode p : nodes) {
            while (lower.size() >= 2 && cross(lower.get(lower.size() - 2), lower.get(lower.size() - 1), p) <= 0) {
                lower.remove(lower.size() - 1);
            }
            lower.add(p);
        }
        // upper hull
        List<DBNode> upper = new ArrayList<>();
        for (int i = nodes.size() - 1; i >= 0; i--) {
            DBNode p = nodes.get(i);
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

    public static double cross(DBNode o, DBNode a, DBNode b) {
        List<Double>oCoordinates=o.getCoordinates();
        List<Double>aCoordinates=a.getCoordinates();
        List<Double>bCoordinates=b.getCoordinates();
        return (aCoordinates.get(0) - oCoordinates.get(0)) * (bCoordinates.get(1)
                - oCoordinates.get(1)) - (aCoordinates.get(1) - o.getCoordinates().get(1)) * (bCoordinates.get(0) - oCoordinates.get(0));
    }

    public static boolean areCollinear(List<DBNode> nodes) {
        DBNode p0 = nodes.get(0);
        DBNode p1 = nodes.get(1);
        for (int i = 2; i < nodes.size(); i++) {
            if (cross(p0, p1, nodes.get(i)) != 0) {
                return false;
            }
        }
        return true;
    }
}

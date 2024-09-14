package com.jwetherell.algorithms.data_structures;

import java.util.Objects;
import java.util.List;

/**
 * Multidimensional node
 */
public class MultidimensionalNode {
    private List<Double> coordinates;
    private final int dimension;

    public MultidimensionalNode(List<Double> coordinates) {
        this.coordinates = coordinates;
        this.dimension=coordinates.size();

    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public int getDimension() {
        return dimension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultidimensionalNode that = (MultidimensionalNode) o;
        return dimension == that.dimension && Objects.equals(coordinates,that.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates, dimension);
    }
}

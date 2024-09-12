package com.jwetherell.algorithms.mathematics;

/*
 * Statistic Class
 *
 * This class provides static methods to calculate basic statistical measures
 * for an array of numbers, including the mean, variance, and standard deviation.
 *
 * Methods:
 *
 * 1. mean(Number[] values)
 *    - Calculates the arithmetic mean (average) of the provided array of numbers.
 *
 * 2. variance(Number[] values)
 *    - Calculates the variance of the provided array of numbers.
 *
 * 3. standardDeviation(Number[] values)
 *    - Calculates the standard deviation of the provided array of numbers.
 *    - Standard deviation is a measure of the amount of variation or dispersion
 *      of a set of values.
 *
 * Note:
 * - The methods in this class are designed to handle arrays of Number objects,
 *   which allows for flexibility in input types (e.g., Integer, Double, etc.).
 * - Each method ensures that calculations are performed using double precision
 *   to maintain accuracy.
 *
 * @author dawei xiong
 *
 */
public class Statistic {

    public static double mean(Number[] values) {
        double sum = 0;
        for (Number value : values) {
            sum += value.doubleValue();
        }
        return sum / values.length;
    }

    public static double variance(Number[] values) {
        double mean = mean(values);
        double sum = 0;
        for (Number value : values) {
            sum += Math.pow(value.doubleValue() - mean, 2);
        }
        return sum / values.length;
    }

    public static double standardDeviation(Number[] values) {
        return Math.sqrt(variance(values));
    }

}

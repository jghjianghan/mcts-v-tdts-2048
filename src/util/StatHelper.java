package util;

import java.util.Arrays;

/**
 * Helper class for statistic calculations
 *
 * @author Jiang Han
 */
public class StatHelper {

    /**
     * Mencari elemen dengan nilai terkecil dalam sebuah array dan mengembalikan
     * indeks dari elemen tersebut. Jika ada beberapa nilai minimum,
     * dikembalikan indeks yang terkecil.
     *
     * @param data Array data yang ingin dicari nilai minimumnya.
     * @return Indeks dari elemen dalam data dengan nilai terkecil.
     */
    public static int idOfMinimum(int[] data) {
        int minId = 0;

        for (int i = 1; i < data.length; i++) {
            if (data[minId] > data[i]) {
                minId = i;
            }
        }

        return minId;
    }

    /**
     * Mencari elemen dengan nilai terbesar dalam sebuah array dan mengembalikan
     * indeks dari elemen tersebut. Jika ada beberapa nilai maksimum,
     * dikembalikan indeks yang terkecil.
     *
     * @param data Array data yang ingin dicari nilai maksimumnya.
     * @return Indeks dari elemen dalam data dengan nilai terbesar.
     */
    public static int idOfMaximum(int[] data) {
        int maxId = 0;

        for (int i = 1; i < data.length; i++) {
            if (data[maxId] < data[i]) {
                maxId = i;
            }
        }

        return maxId;
    }

    /**
     * Menghitung nilai rata-rata dari sebuah array.
     *
     * @param data Array data yang ingin dihitung rata-ratanya.
     * @return Nilai rata-rata dari data.
     */
    public static double average(int[] data) {
        return Arrays.stream(data).average().getAsDouble();
    }

    /**
     * Menghitung nilai rata-rata dari sebuah array.
     *
     * @param data Array data yang ingin dihitung rata-ratanya.
     * @return Nilai rata-rata dari data.
     */
    public static double median(int[] data) {
        int[] copy = Arrays.copyOf(data, data.length);

        Arrays.sort(copy);
        if (copy.length % 2 == 0) {
            return (copy[copy.length / 2] + copy[copy.length / 2 - 1]) / 2.0;
        } else {
            return copy[copy.length / 2];
        }
    }

    /**
     * Calculate the sample variance from an array of data
     *
     * @param data The source data to be calculated
     * @return The sample variance of the data
     */
    public static double sampleVariance(int data[]) {
        double avg = average(data);
        return Arrays.stream(data)
                .asDoubleStream()
                .reduce(0, (deltaSquared, xi) -> deltaSquared + Math.pow(avg - xi, 2)
                ) / (data.length - 1);
    }
}

import java.util.Random;

public class MatrixMultiplication {

    private static final Random random = new Random();
    private static final int[] sizes = new int[] { 100, 200, 300, 400, 500, 600, 700, 800 };

    public static void main(String[] args) {
        System.out.println("<java_matrix_multiplication>");
        for (int i = 0; i < sizes.length; ++i) {
            System.out.println("<benchmarkset>");
            System.out.println("<name>matrix_multiplication_" + sizes[i] + "</name>");
            for (int bench = 1; bench <= 20; ++bench) {
                doBenchmark(sizes[i], bench);
            }
            System.out.println("</benchmarkset>");
        }
        System.out.println("</java_matrix_multiplication>");
    }

    private static void doBenchmark(int size, int number) {
        System.out.println("<benchmark>");
        System.out.println("<number>" + number + "</number>");
        long t = getMSCount();
        int[][] A = genData(size);
        int[][] B = genData(size);
        int[][] C = multiply(A, B);
        System.out.println("<data>" + C[0][0] + "," + C[0][C.length - 1] + "," + C[C.length - 1][0] + "," + C[C.length - 1][C.length - 1] + "</data>");
        System.out.println("<time>" + (getMSCount() - t) + "</time>");
        System.out.println("</benchmark>");
    }

    private static long getMSCount() {
        return System.nanoTime() / (1024 * 1024);
    }

    private static int[][] genData(int size) {
        int[][] data = new int[size][];
        for (int i = 0; i < size; ++i) {
            data[i] = new int[size];
            for (int j = 0; j < size; ++j) {
                data[i][j] = random.nextInt();
            }
        }

        return data;
    }

    private static int[][] multiply(int[][] A, int[][] B) {
        int [][] C = new int[A.length][];
        for (int i = 0; i < C.length; ++i) {
            C[i] = new int[A.length];
        }

        for (int i = 0; i < A.length; ++i) {
            for (int j = 0; j < A[i].length; ++j) {
                int result = 0;
                for (int k = 0; k < A[i].length; ++k) {
                    result = result + A[i][k] * B[k][j];
                }
                C[i][j] = result;
            }
        }

        return C;
    }

}

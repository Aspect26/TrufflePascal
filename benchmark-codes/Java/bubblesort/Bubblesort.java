public class Bubblesort {

    private static final int[] sizes = new int[] { 500, 1000, 2000, 4000, 6000, 10000, 13000, 17000, 25000, 32000 };

    public static void main(String[] args) {
        System.out.println("<java_bubblesort>");
        for (int i = 0; i < sizes.length; ++i) {
            System.out.println("<benchmarkset>");
            System.out.println("<name>bubblesort_" + sizes[i] + "</name>");
            for (int bench = 1; bench <= 20; ++bench) {
                doBenchmark(sizes[i], bench);
            }
            System.out.println("</benchmarkset>");
        }
        System.out.println("<java_bubblesort>");
    }

    private static void doBenchmark(int size, int number) {
        System.out.println("<benchmark>");
        System.out.println("<number>" + number + "</number>");
        long t = getMSCount();
        int[] data = genData(size);
        bubblesort(data);
        System.out.println("<data>" + data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4] + "," + data[data.length - 2] + "</data>");
        System.out.println("<time>" + (getMSCount() - t) + "</time>");
        System.out.println("</benchmark>");
    }

    private static long getMSCount() {
        return System.nanoTime() / (1024 * 1024);
    }

    private static int[] genData(int size) {
        int[] data = new int[size];
        for (int i = 0; i < size; ++i) {
            data[i] = 25; // TODO: random
        }

        return data;
    }

    private static void bubblesort(int[] data) {
        int tmp;
        for (int i = 0; i < data.length - 1; ++i) {
            for (int j = 0; j < data.length - 1; ++j) {
                if (data[j] > data[j + 1]) {
                    tmp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = tmp;
                }
            }
        }
    }

}

function getMSCount() {
    return nanoTime() / (1000 * 1000);
}

function sumit(n) {
    result = 0;
    cycles = 0;
    while (cycles < n) {
        result = result + 10;
        cycles = cycles + 1;
    }
    return result; 
}

function doBenchmark(count) {
    t = getMSCount();
    println("<benchmark>");
    println("<number>" + count + "</number>");
    println("<data>" + sumit(5000) + "</data>");
    println("<time>" + (getMSCount() - t) + "</time>");
    println("</benchmark>");
}

function main() {
    println("<benchmarkset>");
    println("<name>fibonacci</name>");
    bench = 1;
    while (bench <= 20) {
        doBenchmark(bench);
        bench = bench + 1;
    }
    println("</benchmarkset>");
}

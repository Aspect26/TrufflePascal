function getMSCount() {
    return nanoTime() / (1000 * 1000);
}

function fibonacci(i) {
    if (i < 3) { 
        return 1; 
    }
    return fibonacci(i-1) + fibonacci(i-2);
}

function doBenchmark(count) {
    t = getMSCount();
    println("<benchmark>");
    println("<number>" + count + "</number>");
    println("<data>" + fibonacci(30) + "</data>");
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

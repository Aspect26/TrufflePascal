program fibon;
uses sysutils;

const size = BENCH_SIZE;
const iterations = BENCH_ITERS;

var blackhole : integer; (* Used to prevent FPC from removing unused parts of code *)

function fibonacci(i: integer): integer;
begin
    if i < 3 then fibonacci := 1
    else fibonacci := fibonacci(i-1) + fibonacci(i-2);
end;

function doBenchmark : int64;
var res: integer;
    start, fini:int64;
begin
    start := getTickCount;
    res := fibonacci(size);
    fini := getTickCount;
    
    blackhole := blackhole + res;
    
    (* dump(data); *)
    
    doBenchmark := fini - start;
end;

var i: integer;
    results: array[1..iterations] of int64;

begin
    blackhole := 15;
    
    for i := 1 to iterations do
        results[i] := doBenchmark;

    if blackhole = 12 then
        writeln('bench,size,iteration,score')
    else begin
        write('bench,size,');
        writeln('iteration,score');
    end;
    for i := 1 to iterations do
        writeln('fibon,', size, ',', i, ',', results[i]);
end.

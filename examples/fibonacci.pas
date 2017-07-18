(*
    This is a trivial program used in one of our benchmarks in the thesis. It computes one number from fibonacci's sequence.
    If compiled with FPC, it is better to use getTickCount from sysutils unit for time measurements because it is more
    precise. Our implementation of getMSCount, however, uses Java's most precise time measurement function which is 
    System.nanoTime().
*)
program fibonacciBenchmark;
uses dos;

const size = 26;
const iterations = 10;

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
    start := getMSCount;
    res := fibonacci(size);
    fini := getMSCount;
    
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

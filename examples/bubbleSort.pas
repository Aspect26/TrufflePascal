program bubbleSortBenchmark;
uses sysutils;

const size = BENCH_SIZE;
const iterations = BENCH_ITERS;

type arrType = array[1..size] of integer;

var blackhole : integer; (* Used to prevent FPC from removing unused parts of code *)

function gen: arrType;
var i: integer;
    r: arrType;
    seed: integer;
begin
    seed := 1234;
    for i:= 1 to size do begin
       seed := seed * 7919 mod 16127 + 7;
       r[i] := seed div 8;
    end;
    gen := r;
end;

procedure dump(var arr: arrType);
var i : integer;
begin
    for i := 1 to size do
        write(arr[i], ' ');
    writeln;
end;

procedure bubbleSort(var arr: arrType);
var i,j: integer;
    tmp: integer;
begin
    for i:= 1 to size - 1 do begin
        for j:= 1 to size - i do begin
            if arr[j] > arr[j+1] then begin
                tmp := arr[j];
                arr[j] := arr[j+1];
                arr[j+1] := tmp;
            end;
        end;
        (* dump(arr); *)
    end;
end;

function doBenchmark : int64;
var data:arrType;
    start, fini:int64;
begin
    data := gen;
    (* dump(data); *)
    
    start := getTickCount;
    bubbleSort(data);
    fini := getTickCount;
    
    blackhole := blackhole + data[1];
    
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
        writeln('bubble,', size, ',', i, ',', results[i]);
end.

